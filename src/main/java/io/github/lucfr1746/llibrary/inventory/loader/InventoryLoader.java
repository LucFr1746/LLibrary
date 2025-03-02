package io.github.lucfr1746.llibrary.inventory.loader;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import io.github.lucfr1746.llibrary.inventory.InventoryManager;
import io.github.lucfr1746.llibrary.requirement.HasPermissionRequirement;
import io.github.lucfr1746.llibrary.requirement.Requirement;
import io.github.lucfr1746.llibrary.requirement.RequirementType;
import io.github.lucfr1746.llibrary.utils.APIs.LoggerAPI;
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
        loadMenuProperties();
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
                    case PERMISSION -> addPermissionRequirement(key, requirementSection);
                    default -> logger.error("Unknown requirement type in " + key + " of menu " + menuId);
                }
            } catch (IllegalArgumentException e) {
                logger.error("Invalid requirement type in " + key + " of menu " + menuId);
                LLibrary.getLoggerAPI().error("Skipping this requirement...");
            }
        });
    }

    private void addPermissionRequirement(String key, ConfigurationSection section) {
        String permission = section.getString("permission");
        if (permission != null) {
            openRequirements.put(key, new HasPermissionRequirement(permission));
        } else {
            LLibrary.getLoggerAPI().error("Missing permission value for " + key + " in menu " + menuId);
        }
    }

    private void loadOpenCommands() {
        openCommands.addAll(guiConfig.getStringList("open-command"));
        if (!openCommands.isEmpty()) {
            registerOpenCommands();
        }
    }

    private void loadMenuProperties() {
        Optional.ofNullable(guiConfig.getString("menu-title")).ifPresent(this::setTitle);
        Optional.ofNullable(guiConfig.getString("menu-type")).ifPresent(type ->
                setMenuType(Registry.MENU.get(new NamespacedKey(NamespacedKey.MINECRAFT, type.toLowerCase())))
        );
    }

    private void registerOpenCommands() {
        if (openCommands.isEmpty()) return;

        new CommandAPICommand(openCommands.get(0))
                .withAliases(openCommands.subList(1, openCommands.size()).toArray(String[]::new))
                .executesPlayer((player, args) -> {
                    if (openRequirements.values().stream().allMatch(req -> req instanceof HasPermissionRequirement permissionReq && permissionReq.evaluate(player))) {
                        new InventoryManager().openGUI(this, player);
                    }
                })
                .register();
    }
}
