package io.github.lucfr1746.llibrary.inventory.loader;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.action.ActionLoader;
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import io.github.lucfr1746.llibrary.inventory.InventoryButton;
import io.github.lucfr1746.llibrary.inventory.InventoryManager;
import io.github.lucfr1746.llibrary.itemstack.ItemBuilder;
import io.github.lucfr1746.llibrary.requirement.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
            LLibrary.getLoggerAPI().error("Missing menu-id in " + guiConfig.getName());
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
        loadItems();
    }

    private void loadItems() {
        Map<Integer, TreeMap<Integer, InventoryButton>> items = new HashMap<>();
        ConfigurationSection itemKey = guiConfig.getConfigurationSection("items");
        if (itemKey == null) return;

        for (String key : itemKey.getKeys(false)) {
            ConfigurationSection itemSection = Objects.requireNonNull(itemKey.getConfigurationSection(key));

            Material material;
            try {
                material = Material.valueOf(itemSection.getString("material"));
            } catch (IllegalArgumentException | NullPointerException exception) {
                LLibrary.getLoggerAPI().error("Invalid material -> " + "\"" + itemSection.getString("material") + "\"");
                break;
            }

            List<Integer> slots = new ArrayList<>();
            Optional.ofNullable(itemSection.get("slot")).ifPresent(slot -> {
                if (slot instanceof Integer s) slots.add(s);
                else slots.addAll(itemSection.getIntegerList("slot"));
            });
            if (slots.isEmpty() || slots.stream().anyMatch(s -> s < 0)) {
                LLibrary.getLoggerAPI().error("Invalid slot(s): " + slots);
                return;
            }

            int priority = itemSection.getInt("priority", -1);
            if (priority <= -1) {
                LLibrary.getLoggerAPI().error("Invalid priority -> " + "\"" + itemSection.getString("priority") + "\"");
                LLibrary.getLoggerAPI().error("Setting priority of this item to: 0");
            }

            String displayName = itemSection.getString("display-name", null);
            List<String> lores = new ArrayList<>();
            Optional.ofNullable(itemSection.get("lore")).ifPresent(lore -> {
                if (lore instanceof String) lores.add((String) lore);
                else lores.addAll(itemSection.getStringList("lore"));
            });

            List<Requirement> viewRequirement = new RequirementLoader().getRequirements(itemSection.getConfigurationSection("view-requirement"));

            ItemBuilder item = new ItemBuilder(material);
            if (displayName == null && lores.isEmpty())
                item.hideToolTip(true);
            else {
                item.rename(displayName);
                item.loresSet(lores);
            }

            switch (key) {
                case "close" -> {
                    InventoryButton close = new InventoryButton()
                            .creator(player -> item.build())
                            .viewRequirements(viewRequirement)
                            .consumer(event -> event.getWhoClicked().closeInventory());

                    slots.forEach(slot -> {
                        items.computeIfAbsent(slot, k -> new TreeMap<>());
                        TreeMap<Integer, InventoryButton> buttonMap = items.get(slot);

                        if (buttonMap.isEmpty() || priority > buttonMap.lastKey()) {
                            buttonMap.put(priority, close);
                        }
                    });
                }
                case "filled-items" -> {
                    InventoryButton filled = new InventoryButton()
                            .viewRequirements(viewRequirement)
                            .creator(player -> item.build());

                    slots.forEach(slot -> {
                        items.computeIfAbsent(slot, k -> new TreeMap<>());
                        TreeMap<Integer, InventoryButton> buttonMap = items.get(slot);

                        if (buttonMap.isEmpty() || priority > buttonMap.lastKey()) {
                            buttonMap.put(priority, filled);
                        }
                    });
                }
                default -> {

                }
            }

            for (Map.Entry<Integer, TreeMap<Integer, InventoryButton>> entry : items.entrySet()) {
                Integer slot = entry.getKey();
                TreeMap<Integer, InventoryButton> priorityMap = entry.getValue();

                if (!priorityMap.isEmpty()) {
                    this.addButton(slot, priorityMap.lastEntry().getValue());
                }
            }
        }
    }
}
