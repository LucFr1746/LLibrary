package io.github.lucfr1746.llibrary.inventory.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.jorel.commandapi.CommandAPICommand;
import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.action.ActionLoader;
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import io.github.lucfr1746.llibrary.inventory.InventoryManager;
import io.github.lucfr1746.llibrary.requirement.*;
import io.github.lucfr1746.llibrary.utils.APIs.LoggerAPI;
import io.github.lucfr1746.llibrary.utils.PluginLoader;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class InventoryLoader extends InventoryBuilder {

    private final FileConfiguration guiConfig;
    private final String menuId;
    private final List<String> openCommands = new ArrayList<>();
    private final Map<String, Requirement> openRequirements = new HashMap<>();
    private final List<Action> openActions = new ArrayList<>();

    public InventoryLoader(FileConfiguration guiConfig) {
        this.guiConfig = Objects.requireNonNull(guiConfig, "guiConfig cannot be null");
        this.menuId = guiConfig.getString("menu-id");
        if (this.menuId == null) {
            LLibrary.getLoggerAPI().error("Missing menu-id in " + guiConfig.getCurrentPath());
            LLibrary.getLoggerAPI().error("Skipping this menu...");
            return;
        }

        loadOpenRequirements();
        loadOpenCommands();
        loadOpenActions();
        loadMenuProperties();

        PluginLoader.registerInv(this);
    }

    public String getMenuId() {
        return this.menuId;
    }

    public List<String> getOpenCommands() {
        return this.openCommands;
    }

    public Map<String, Requirement> getOpenRequirements() {
        return this.openRequirements;
    }

    public List<Action> getOpenActions() {
        return this.openActions;
    }

    private void loadOpenRequirements() {
        ConfigurationSection openRequirementSection = guiConfig.getConfigurationSection("open-requirement");
        if (openRequirementSection == null) return;

        LoggerAPI logger = LLibrary.getLoggerAPI();
        openRequirementSection.getKeys(false).forEach(key -> {
            ConfigurationSection requirementSection = openRequirementSection.getConfigurationSection(key);
            if (requirementSection == null) {
                logger.error("Missing requirement type in " + key + " of menu " + menuId);
                LLibrary.getLoggerAPI().error("Skipping this requirement...");
                return;
            }

            try {
                RequirementType requirementType = RequirementType.valueOf(requirementSection.getString("type", ""));
                switch (requirementType) {
                    case PERMISSION -> addPermissionRequirement(RequirementType.PERMISSION, key, requirementSection);
                    case EXP -> addPermissionRequirement(RequirementType.EXP, key, requirementSection);
                    case LEVEL -> addPermissionRequirement(RequirementType.LEVEL, key, requirementSection);
                    default -> logger.error("Unknown requirement type in " + key + " of menu " + menuId);
                }
            } catch (IllegalArgumentException e) {
                logger.error("Invalid requirement type in " + key + " of menu " + menuId);
                LLibrary.getLoggerAPI().error("Skipping this requirement...");
            }
        });
    }

    private void addPermissionRequirement(RequirementType type, String key, ConfigurationSection section) {
        switch (type) {
            case PERMISSION -> {
                String permission = section.getString("permission");
                if (permission != null) {
                    List<Action> denyActions = new ActionLoader().getActions(section.getStringList("deny-action"));
                    openRequirements.put(key, new HasPermissionRequirement(permission).setDenyHandler(denyActions));
                } else {
                    LLibrary.getLoggerAPI().error("Missing permission value for " + key + " in menu " + menuId);
                }
            }
            case EXP -> {
                int amount = section.getInt("amount");
                List<Action> denyActions = new ActionLoader().getActions(section.getStringList("deny-action"));
                openRequirements.put(key, new HasExpRequirement(amount).setDenyHandler(denyActions));
            }
            case LEVEL -> {
                int amount = section.getInt("amount");
                List<Action> denyActions = new ActionLoader().getActions(section.getStringList("deny-action"));
                openRequirements.put(key, new HasLevelRequirement(amount).setDenyHandler(denyActions));
            }
        }
    }

    private void loadOpenCommands() {
        String singleCmd = guiConfig.getString("open-command");
        if (singleCmd != null && !singleCmd.isBlank()) {
            try {
                JsonElement element = JsonParser.parseString(singleCmd);
                if (element.isJsonArray()) openCommands.addAll(guiConfig.getStringList("open-command"));
                else openCommands.add(singleCmd);
            } catch (Exception e) {
                openCommands.add(singleCmd);
            }
        } else {
            openCommands.addAll(guiConfig.getStringList("open-command"));
        }

        if (!openCommands.isEmpty()) {
            registerOpenCommands();
        }
    }

    private void loadOpenActions() {
        openActions.addAll(new ActionLoader().getActions(guiConfig.getStringList("open-action")));
    }

    private void loadMenuProperties() {
        Optional.ofNullable(guiConfig.getString("menu-title")).ifPresent(this::setTitle);
        Optional.ofNullable(guiConfig.getString("menu-type")).ifPresent(type ->
                setMenuType(Registry.MENU.get(new NamespacedKey(NamespacedKey.MINECRAFT, type.toLowerCase())))
        );
    }

    private void registerOpenCommands() {
        if (openCommands.isEmpty()) return;

        new CommandAPICommand(openCommands.getFirst())
                .withAliases(openCommands.subList(1, openCommands.size()).toArray(String[]::new))
                .executesPlayer((player, args) -> {
                    if (openRequirements.values().stream().allMatch(req -> req instanceof HasPermissionRequirement)) {
                        boolean hasAllPermissions = openRequirements.values().stream()
                                .filter(req -> req instanceof HasPermissionRequirement)
                                .map(req -> (HasPermissionRequirement) req)
                                .allMatch(permissionReq -> permissionReq.evaluate(player));

                        if (hasAllPermissions) {
                            new InventoryManager().openGUI(this, player);
                        } else {
                            openRequirements.values().stream()
                                    .filter(req -> req instanceof HasPermissionRequirement)
                                    .map(req -> (HasPermissionRequirement) req)
                                    .forEach(permissionReq -> {
                                        if (!permissionReq.evaluate(player)) {
                                            permissionReq.getDenyHandlers().forEach(handler -> handler.execute(player));
                                        }
                                    });
                        }
                    }
                })
                .register();
    }
}
