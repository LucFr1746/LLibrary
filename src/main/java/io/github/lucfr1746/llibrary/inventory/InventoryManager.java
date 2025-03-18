package io.github.lucfr1746.llibrary.inventory;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.util.helper.FileAPI;
import io.github.lucfr1746.llibrary.util.helper.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages custom inventories in the plugin.
 * Handles opening, closing, and interaction events for GUIs.
 */
public class InventoryManager {

    private final Map<String, InventoryBuilder> baseMenus = new HashMap<>();
    private final Map<Inventory, InventoryHandler> activeInventories = new ConcurrentHashMap<>();
    private final List<InventoryBuilder> pluginGUIs = new ArrayList<>();
    private final Set<String> registeredCommands = new HashSet<>();
    private Logger logger;

    /**
     * Opens a custom GUI for a player by menu ID.
     * @param menuID The ID of the menu to open.
     * @param player The player who will see the GUI.
     */
    public void openGUI(String menuID, Player player) {
        Optional.ofNullable(baseMenus.get(menuID)).ifPresentOrElse(baseMenu -> {
            InventoryBuilder menuClone = baseMenu.clone();
            menuClone.decorate(player);
            Inventory inventory = menuClone.getInventoryView().getTopInventory();
            registerActiveInventory(inventory, menuClone);
            Bukkit.getScheduler().runTask(LLibrary.getInstance(), () -> player.openInventory(menuClone.getInventoryView()));
        }, () -> logger.warning("No menu found with ID: " + menuID));
    }

    /**
     * Loads necessary configurations for the inventory system.
     * This method initializes the base menus by loading configurations
     * from stored menu files.
     */
    public void load() {
        this.logger = LLibrary.getPluginLogger();
        this.logger.info("Loading base menus...");
        FileAPI fileAPI = new FileAPI(LLibrary.getInstance(), true);
        fileAPI.createFolderIfNotExist("menu");
        fileAPI.getAllFiles("menu").forEach(file -> {
            InventoryBuilder inventoryBuilder = new InventoryBuilder();
            inventoryBuilder.loadFromFile(fileAPI.convertToYAMLConfiguration(file));
            registerInventoryBuilder(inventoryBuilder);
            pluginGUIs.add(inventoryBuilder);
        });
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), LLibrary.getInstance());
    }

    /**
     * Registers a new InventoryBuilder instance.
     * @param inventoryBuilder The InventoryBuilder instance to register.
     */
    public void registerInventoryBuilder(InventoryBuilder inventoryBuilder) {
        if (baseMenus.putIfAbsent(inventoryBuilder.getId(), inventoryBuilder) != null) {
            throw new IllegalStateException("Duplicate inventory builder ID: " + inventoryBuilder.getId());
        }
        registerOpenCommands(inventoryBuilder);
    }

    /**
     * Unregisters an InventoryBuilder, removing its commands and closing active inventories.
     * @param inventoryBuilder The InventoryBuilder to unregister.
     */
    public void unregisterInventoryBuilder(InventoryBuilder inventoryBuilder) {
        unregisterOpenCommands(inventoryBuilder);
        activeInventories.entrySet().removeIf(entry -> {
            if (entry.getValue().equals(inventoryBuilder)) {
                entry.getKey().getViewers().forEach(HumanEntity::closeInventory);
                return true;
            }
            return false;
        });
        baseMenus.remove(inventoryBuilder.getId());
    }

    /**
     * Registers open commands for a given InventoryBuilder.
     * @param inventoryBuilder The InventoryBuilder containing open commands.
     */
    private void registerOpenCommands(InventoryBuilder inventoryBuilder) {
        List<String> openCommands = inventoryBuilder.getOpenCommands();

        if (openCommands == null || openCommands.isEmpty()) return;

        Bukkit.getScheduler().runTask(LLibrary.getInstance(), () -> new CommandAPICommand(openCommands.getFirst())
                .withAliases(openCommands.stream().skip(1).toArray(String[]::new))
                .executesPlayer((player, args) -> {
                    if (inventoryBuilder.getOpenRequirements().stream().allMatch(req -> req.evaluate(player))) {
                        openGUI(inventoryBuilder.getId(), player);
                    } else {
                        inventoryBuilder.getOpenRequirements().stream()
                                .filter(req -> !req.evaluate(player))
                                .forEach(req -> req.getDenyHandler().forEach(handler -> handler.execute(player)));
                    }
                }).register());
    }

    /**
     * Unregisters open commands for a given InventoryBuilder.
     * @param inventoryBuilder The InventoryBuilder containing commands to unregister.
     */
    private void unregisterOpenCommands(InventoryBuilder inventoryBuilder) {
        inventoryBuilder.getOpenCommands().forEach(cmd -> {
            if (registeredCommands.remove(cmd)) {
                CommandAPI.unregister(cmd);
            }
        });
    }

    /**
     * Disables the inventory manager and closes all active inventories.
     */
    public void disable() {
        pluginGUIs.forEach(this::unregisterInventoryBuilder);
    }

    /**
     * Registers an active inventory to track interactions.
     * @param inventory The inventory instance.
     * @param handler The handler associated with this inventory.
     */
    private void registerActiveInventory(Inventory inventory, InventoryHandler handler) {
        activeInventories.put(inventory, handler);
    }

    /**
     * Unregisters an inventory, removing it from tracking.
     * @param inventory The inventory instance to unregister.
     */
    private void unregisterInventory(Inventory inventory) {
        activeInventories.remove(inventory);
    }

    /**
     * Handles inventory click events.
     * @param event The inventory click event.
     */
    void handleClick(InventoryClickEvent event) {
        processInventoryEvent(event.getInventory(), handler -> handler.onClick(event));
    }

    /**
     * Handles inventory open events.
     * @param event The inventory open event.
     */
    void handleOpen(InventoryOpenEvent event) {
        processInventoryEvent(event.getInventory(), handler -> handler.onOpen(event));
    }

    /**
     * Handles inventory close events.
     * @param event The inventory close event.
     */
    void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        processInventoryEvent(inventory, handler -> handler.onClose(event));
        unregisterInventory(inventory);
    }

    /**
     * Processes inventory-related events with a given processor.
     * @param inventory The inventory to process.
     * @param eventProcessor The processor to handle the event.
     */
    private void processInventoryEvent(Inventory inventory, InventoryEventProcessor eventProcessor) {
        Optional.ofNullable(activeInventories.get(inventory)).ifPresent(eventProcessor::process);
    }

    /**
     * Functional interface for processing inventory events.
     */
    @FunctionalInterface
    private interface InventoryEventProcessor {
        void process(InventoryHandler handler);
    }
}