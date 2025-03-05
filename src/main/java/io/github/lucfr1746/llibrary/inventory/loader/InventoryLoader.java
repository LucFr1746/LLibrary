package io.github.lucfr1746.llibrary.inventory.loader;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.action.ActionLoader;
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import io.github.lucfr1746.llibrary.inventory.InventoryButton;
import io.github.lucfr1746.llibrary.inventory.InventoryManager;
import io.github.lucfr1746.llibrary.requirement.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class InventoryLoader extends InventoryBuilder {

    private final FileConfiguration guiConfig;
    private final String menuId;
    private final List<String> openCommands = new ArrayList<>();
    private final List<Requirement> openRequirements = new ArrayList<>();
    private final List<Action> openActions = new ArrayList<>();

    public InventoryLoader(@NotNull FileConfiguration guiConfig) {
        this.guiConfig = guiConfig;
        this.menuId = guiConfig.getString("menu-id");
        if (this.menuId == null) {
            LLibrary.getLoggerAPI().error("Missing menu-id in " + guiConfig.getCurrentPath());
            LLibrary.getLoggerAPI().error("Skipping this menu...");
            return;
        }

        this.openRequirements.addAll(new RequirementLoader().getRequirements(guiConfig.getConfigurationSection("open-requirement")));
        this.openActions.addAll(new ActionLoader().getActions(guiConfig.getStringList("open-action")));

        loadOpenCommands();
        loadMenuProperties();
    }

    public String getMenuId() {
        return menuId;
    }
    public List<String> getOpenCommands() {
        return Collections.unmodifiableList(openCommands);
    }
    public List<Requirement> getOpenRequirements() {
        return Collections.unmodifiableList(openRequirements);
    }
    public List<Action> getOpenActions() {
        return Collections.unmodifiableList(openActions);
    }

    private void loadOpenCommands() {
        Optional.ofNullable(guiConfig.get("open-command")).ifPresent(cmd -> {
            if (cmd instanceof String) openCommands.add((String) cmd);
            else openCommands.addAll(guiConfig.getStringList("open-command"));
        });

        if (!openCommands.isEmpty()) registerOpenCommands();
    }

    private void registerOpenCommands() {
        Bukkit.getScheduler().runTask(LLibrary.getInstance(),
                () -> new CommandAPICommand(openCommands.getFirst())
                .withAliases(openCommands.stream().skip(1).toArray(String[]::new))
                .executesPlayer((player, args) -> {
                    if (openRequirements.stream().allMatch(req -> req.evaluate(player))) {
                        new InventoryManager().openGUI(this, player);
                    } else {
                        openRequirements.stream().filter(req -> !req.evaluate(player))
                                .forEach(req -> req.getDenyHandlers().forEach(handler -> handler.execute(player)));
                    }
                }).register());

    }

    private void loadMenuProperties() {
        Optional.ofNullable(guiConfig.getString("menu-title")).ifPresent(this::setTitle);
        Optional.ofNullable(guiConfig.getString("menu-type")).ifPresent(type ->
                setMenuType(Registry.MENU.get(new NamespacedKey(NamespacedKey.MINECRAFT, type.toLowerCase())))
        );
    }
}
