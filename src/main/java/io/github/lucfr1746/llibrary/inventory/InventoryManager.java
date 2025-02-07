package io.github.lucfr1746.llibrary.inventory;

import io.github.lucfr1746.llibrary.LLibrary;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.HashMap;

/**
 * Manages the active inventories and their corresponding handlers.
 * Provides methods for opening, handling events, and unregistering inventories.
 */
public class InventoryManager {

    /**
     * Default constructor for {@link InventoryManager}.
     * Initializes an empty set of active inventories.
     */
    public InventoryManager() {
        // Default constructor with no specific initialization logic
    }

    /**
     * A map that holds active inventories and their corresponding handlers.
     */
    private final Map<Inventory, InventoryHandler> activeInventories = new HashMap<>();

    /**
     * Opens the specified GUI for the given player and registers an {@link InventoryListener} to handle inventory events.
     * The inventory is registered as active before being opened for the player.
     *
     * @param inventoryBuilder The {@link InventoryBuilder} instance representing the GUI to be opened.
     * @param player The {@link Player} who will see the opened GUI.
     */
    public void openGUI(InventoryBuilder inventoryBuilder, Player player) {
        InventoryListener guiListener = new InventoryListener(this);
        Bukkit.getPluginManager().registerEvents(guiListener, LLibrary.getInstance());

        inventoryBuilder.decorate(player);
        this.registerActiveInventory(inventoryBuilder.getInventoryView().getTopInventory(), inventoryBuilder);
        Bukkit.getScheduler().runTask(LLibrary.getInstance(), () -> player.openInventory(inventoryBuilder.getInventoryView()));
    }


    /**
     * Registers an inventory and its associated {@link InventoryBuilder} as the handler for the inventory.
     *
     * @param inventory The {@link Inventory} that is being registered.
     * @param inventoryBuilderAPI The {@link InventoryBuilder} instance that handles events for the inventory.
     */
    private void registerActiveInventory(Inventory inventory, InventoryBuilder inventoryBuilderAPI) {
        activeInventories.put(inventory, inventoryBuilderAPI);
    }

    /**
     * Unregisters an inventory from the active inventories map.
     * Removes both the inventory and its associated handler.
     *
     * @param inventory The {@link Inventory} to be unregistered.
     */
    private void unregisterInventory(Inventory inventory) {
        activeInventories.remove(inventory);
    }

    /**
     * Handles the {@link InventoryClickEvent} by delegating to the corresponding handler.
     *
     * @param event The {@link InventoryClickEvent} that occurred.
     */
    void handleClick(InventoryClickEvent event) {
        processInventoryEvent(event.getInventory(), handler -> handler.onClick(event));
    }

    /**
     * Handles the {@link InventoryOpenEvent} by delegating to the corresponding handler.
     *
     * @param event The {@link InventoryOpenEvent} that occurred.
     */
    void handleOpen(InventoryOpenEvent event) {
        processInventoryEvent(event.getInventory(), handler -> handler.onOpen(event));
    }

    /**
     * Handles the {@link InventoryCloseEvent} by delegating to the corresponding handler.
     * After processing, the inventory is unregistered.
     *
     * @param event The {@link InventoryCloseEvent} that occurred.
     */
    void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        processInventoryEvent(inventory, handler -> handler.onClose(event));
        unregisterInventory(inventory);
    }

    /**
     * Processes an inventory event by retrieving the associated handler
     * and delegating the event processing to the handler.
     *
     * @param inventory The {@link Inventory} being processed.
     * @param eventProcessor The action that will be performed on the {@link InventoryHandler}.
     */
    private void processInventoryEvent(Inventory inventory, InventoryEventProcessor eventProcessor) {
        InventoryHandler handler = activeInventories.get(inventory);
        if (handler != null) {
            eventProcessor.process(handler);
        }
    }

    /**
     * A functional interface used to process events in the {@link InventoryHandler}.
     *
     * @see InventoryHandler
     */
    @FunctionalInterface
    private interface InventoryEventProcessor {
        /**
         * Processes the given {@link InventoryHandler} with the event logic.
         *
         * @param handler The {@link InventoryHandler} that will handle the event.
         */
        void process(InventoryHandler handler);
    }
}
