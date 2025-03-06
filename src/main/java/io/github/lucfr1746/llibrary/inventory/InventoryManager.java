package io.github.lucfr1746.llibrary.inventory;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class InventoryManager {

    private final Map<Inventory, InventoryHandler> activeInventories = new HashMap<>();

    public void openGUI(InventoryBuilder inventoryBuilder, Player player) {
        InventoryListener guiListener = new InventoryListener(this);
        Bukkit.getPluginManager().registerEvents(guiListener, LLibrary.getInstance());

        inventoryBuilder.decorate(player);
        this.registerActiveInventory(inventoryBuilder.getInventoryView().getTopInventory(), inventoryBuilder);
        Bukkit.getScheduler().runTask(LLibrary.getInstance(), () -> player.openInventory(inventoryBuilder.getInventoryView()));
    }

    private void registerActiveInventory(Inventory inventory, InventoryBuilder inventoryBuilderAPI) {
        activeInventories.put(inventory, inventoryBuilderAPI);
    }

    private void unregisterInventory(Inventory inventory) {
        activeInventories.remove(inventory);
    }

    void handleClick(InventoryClickEvent event) {
        processInventoryEvent(event.getInventory(), handler -> handler.onClick(event));
    }

    void handleOpen(InventoryOpenEvent event) {
        processInventoryEvent(event.getInventory(), handler -> handler.onOpen(event));
    }

    void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        processInventoryEvent(inventory, handler -> handler.onClose(event));
        unregisterInventory(inventory);
    }

    private void processInventoryEvent(Inventory inventory, InventoryEventProcessor eventProcessor) {
        InventoryHandler handler = activeInventories.get(inventory);
        if (handler != null) {
            eventProcessor.process(handler);
        }
    }

    @FunctionalInterface
    private interface InventoryEventProcessor {
        void process(InventoryHandler handler);
    }
}
