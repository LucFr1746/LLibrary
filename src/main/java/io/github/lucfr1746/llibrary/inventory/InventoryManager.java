package io.github.lucfr1746.llibrary.inventory;

import dev.jorel.commandapi.CommandAPI;
import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.util.helper.FileAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

/**
 * Manages custom inventories in the plugin.
 * Handles opening, closing, and interaction events for GUIs.
 */
public class InventoryManager {

    private final Map<Inventory, InventoryHandler> activeInventories = new HashMap<>();
    private final Map<Inventory, InventoryBuilder> registeredInventories = new HashMap<>();

    /**
     * Opens a custom GUI for a player.
     * @param inventoryBuilder The InventoryBuilder that constructs the GUI.
     * @param player The player who will see the GUI.
     */
    public void openGUI(InventoryBuilder inventoryBuilder, Player player) {
        InventoryListener guiListener = new InventoryListener(this);
        Bukkit.getPluginManager().registerEvents(guiListener, LLibrary.getInstance());

        inventoryBuilder.decorate(player);
        this.registerActiveInventory(inventoryBuilder.getInventoryView().getTopInventory(), inventoryBuilder);
        Bukkit.getScheduler().runTask(LLibrary.getInstance(), () -> player.openInventory(inventoryBuilder.getInventoryView()));
    }

    /**
     * Loads necessary configurations for the inventory system.
     */
    public void load() {
//        LLibrary.getPluginLogger().info("Loading ItemBuilder GUI...");
//        new ItemBuilderGUI(new FileAPI(LLibrary.getInstance()).getOrCreateDefaultYAMLConfiguration("item-builder-gui.yml", "itembuilder"));
    }

    /**
     * Disables the inventory manager and unregisters open commands.
     */
    public void disable() {
        registeredInventories.forEach((inv, invBuilder) -> {
            if (!invBuilder.getOpenCommands().isEmpty()) {
                Bukkit.getScheduler().runTask(LLibrary.getInstance(), () -> {
                    for (String cmd : invBuilder.getOpenCommands()) {
                        CommandAPI.unregister(cmd);
                    }
                });
            }
        });
    }

    /**
     * Registers an active inventory to track interactions.
     * @param inventory The inventory instance.
     * @param inventoryBuilderAPI The builder that created the inventory.
     */
    private void registerActiveInventory(Inventory inventory, InventoryBuilder inventoryBuilderAPI) {
        activeInventories.put(inventory, inventoryBuilderAPI);
        registeredInventories.put(inventory, inventoryBuilderAPI);
    }

    /**
     * Unregisters an inventory, removing it from tracking.
     * @param inventory The inventory instance.
     */
    private void unregisterInventory(Inventory inventory) {
        activeInventories.remove(inventory);
        registeredInventories.remove(inventory);
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
        InventoryHandler handler = activeInventories.get(inventory);
        if (handler != null) {
            eventProcessor.process(handler);
        }
    }

    /**
     * Functional interface for processing inventory events.
     */
    @FunctionalInterface
    private interface InventoryEventProcessor {
        void process(InventoryHandler handler);
    }
}