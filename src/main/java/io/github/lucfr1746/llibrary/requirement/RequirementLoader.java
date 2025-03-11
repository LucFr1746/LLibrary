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

/**
 * Handles the loading and registration of various requirement types for use in the system.
 */
public class RequirementLoader {

    private final ActionLoader actionLoader = new ActionLoader();
    private final Map<String, Function<Object, Requirement>> requirementFactories = new HashMap<>();

    /**
     * Initializes the RequirementLoader and registers default requirement types.
     */
    public RequirementLoader() {
        registerRequirementType("PERMISSION", perm -> new HasPermissionRequirement((String) perm));
        registerRequirementType("EXP", amount -> new HasExpRequirement((int) amount));
        registerRequirementType("LEVEL", amount -> new HasLevelRequirement((int) amount));
    }

    /**
     * Registers a new requirement type.
     *
     * @param name    The name of the requirement type.
     * @param factory A function that takes an object and returns a new instance of the requirement.
     */
    public void registerRequirementType(String name, Function<Object, Requirement> factory) {
        requirementFactories.put(name.toUpperCase(), factory);
    }

    /**
     * Retrieves a list of requirements from a configuration section.
     *
     * @param checkingSection The configuration section containing requirement data.
     * @return A list of requirements.
     */
    public List<Requirement> getRequirements(@Nullable ConfigurationSection checkingSection) {
        if (checkingSection == null) return Collections.emptyList();

        return checkingSection.getKeys(false).stream()
                .map(key -> loadRequirement(checkingSection.getConfigurationSection(key), key))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Loads a requirement from a given configuration section.
     *
     * @param section The configuration section defining the requirement.
     * @param key     The key for the requirement in the configuration.
     * @return The loaded requirement, or null if an error occurs.
     */
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

    /**
     * Loads actions associated with a requirement from the configuration section.
     *
     * @param section The configuration section.
     * @param path    The path to the action data.
     * @return A list of actions, or an empty list if none are found.
     */
    private List<Action> loadActions(ConfigurationSection section, String path) {
        Object actionData = section.get(path);
        if (actionData instanceof String) return List.of(actionLoader.getAction((String) actionData));
        if (actionData instanceof List<?>) return actionLoader.getActions(section.getStringList(path));
        return Collections.emptyList();
    }

    /**
     * Parses an integer amount from a configuration string.
     *
     * @param amountString The amount string to parse.
     * @param key          The configuration key where the amount is located.
     * @return The parsed integer amount.
     * @throws IllegalArgumentException If the amount is invalid or cannot be parsed.
     */
    private int parseAmount(String amountString, String key) {
        try {
            return Integer.parseInt(Optional.ofNullable(amountString).orElse(""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid 'amount' value: " + amountString + " in section: " + key + ". Skipping...");
        }
    }
}