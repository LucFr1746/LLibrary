package io.github.lucfr1746.llibrary.requirement;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.action.ActionLoader;
import io.github.lucfr1746.llibrary.requirement.list.HasExpRequirement;
import io.github.lucfr1746.llibrary.requirement.list.HasLevelRequirement;
import io.github.lucfr1746.llibrary.requirement.list.HasPermissionRequirement;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RequirementLoader {

    private final ActionLoader actionLoader = new ActionLoader();
    private final Map<String, Function<Object, Requirement>> requirementFactories = new HashMap<>();

    public RequirementLoader() {
        registerRequirementType("PERMISSION", perm -> new HasPermissionRequirement((String) perm));
        registerRequirementType("EXP", amount -> new HasExpRequirement((int) amount));
        registerRequirementType("LEVEL", amount -> new HasLevelRequirement((int) amount));
    }

    public void registerRequirementType(String name, Function<Object, Requirement> factory) {
        requirementFactories.put(name.toUpperCase(), factory);
    }

    public List<Requirement> getRequirements(@Nullable ConfigurationSection checkingSection) {
        if (checkingSection == null) return Collections.emptyList();

        return checkingSection.getKeys(false).stream()
                .map(key -> loadRequirement(checkingSection.getConfigurationSection(key), key))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Requirement loadRequirement(ConfigurationSection section, String key) {
        if (section == null) return null;

        String typeString = Optional.ofNullable(section.getString("type")).orElse("").toUpperCase();
        Function<Object, Requirement> factory = requirementFactories.get(typeString);
        if (factory == null) {
            LLibrary.getPluginLogger().error("Invalid or missing requirement type: " + typeString + " in section: " + key + ". Skipping...");
            return null;
        }

        List<Action> denyActions = loadActions(section, "deny-action");
        List<Action> acceptActions = loadActions(section, "accept-action");

        try {
            Object arg = switch (typeString) {
                case "PERMISSION" -> section.getString("permission");
                case "EXP", "LEVEL" -> parseAmount(section.getString("amount"), key);
                default -> null;
            };

            if (arg == null) throw new IllegalArgumentException("Missing or invalid argument for requirement type: " + typeString + " in section: " + key);

            Requirement requirement = factory.apply(arg);
            return requirement.setDenyHandler(denyActions).setAcceptHandler(acceptActions);
        } catch (IllegalArgumentException e) {
            LLibrary.getPluginLogger().error(e.getMessage());
            return null;
        }
    }

    private List<Action> loadActions(ConfigurationSection section, String path) {
        Object actionData = section.get(path);
        if (actionData instanceof String) return List.of(actionLoader.getAction((String) actionData));
        if (actionData instanceof List<?>) return actionLoader.getActions(section.getStringList(path));
        return Collections.emptyList();
    }

    private int parseAmount(String amountString, String key) {
        try {
            return Integer.parseInt(Optional.ofNullable(amountString).orElse(""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid 'amount' value: " + amountString + " in section: " + key + ". Skipping...");
        }
    }
}
