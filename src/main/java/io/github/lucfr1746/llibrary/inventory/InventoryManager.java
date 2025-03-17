package io.github.lucfr1746.llibrary.inventory;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.itemstack.gui.ItemBuilderGUI;
import io.github.lucfr1746.llibrary.util.helper.FileAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
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

    private final Map<String, InventoryBuilder> baseMenus = new HashMap<>();
    private final Map<Inventory, InventoryHandler> activeInventories = new HashMap<>();

    private final List<InventoryBuilder> pluginGUIs = new ArrayList<>();

    /**
     * Opens a custom GUI for a player by menu ID.
     * @param menuID The ID of the menu to open.
     * @param player The player who will see the GUI.
     */
    public void openGUI(String menuID, Player player) {
        InventoryBuilder baseMenu = baseMenus.get(menuID);
        if (baseMenu == null) {
            LLibrary.getPluginLogger().warning("No menu found with ID: " + menuID);
            return;
        }

        // Clone the base menu for the player
        InventoryBuilder menuClone = baseMenu.clone();
        menuClone.decorate(player);

        // Register the clone to track interactions
        registerActiveInventory(menuClone.getInventoryView().getTopInventory(), menuClone);

        // Open the inventory for the player
        Bukkit.getScheduler().runTask(LLibrary.getInstance(), () -> player.openInventory(menuClone.getInventoryView()));
    }

    /**
     * Loads necessary configurations for the inventory system.
     * This method initializes the base menus by loading configurations
     * from stored menu files.
     * Menu registration happens dynamically when a player opens a menu.
     */
    public void load() {
        LLibrary.getPluginLogger().info("Loading base menus...");
        this.pluginGUIs.add(new ItemBuilderGUI());

        FileAPI fileAPI = new FileAPI(LLibrary.getInstance(), true);
        fileAPI.createFolderIfNotExist("menu");
        fileAPI.getAllFiles("menu").forEach(file -> registerInventoryBuilder(fileAPI.convertToYAMLConfiguration(file)));

        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), LLibrary.getInstance());
    }

    /**
     * Registers a new InventoryBuilder instance.
     * This method adds the given inventory builder to the base menu map
     * and registers associated open commands.
     *
     * @param inventoryBuilder The InventoryBuilder instance to register.
     * @throws IllegalStateException if an inventory with the same ID already exists.
     */
    public void registerInventoryBuilder(InventoryBuilder inventoryBuilder) {
        if (baseMenus.containsKey(inventoryBuilder.getId()))
            throw new IllegalStateException("Duplicate inventory builder id detected: " + inventoryBuilder.getId());
        baseMenus.put(inventoryBuilder.getId(), inventoryBuilder);
        registerOpenCommands(inventoryBuilder);
    }


    /**
     * Registers a new InventoryBuilder from a YAML file configuration.
     * This method creates an InventoryBuilder from the provided configuration,
     * loads its data, and registers it.
     *
     * @param fileConfiguration The FileConfiguration containing the menu data.
     */
    public void registerInventoryBuilder(FileConfiguration fileConfiguration) {
        InventoryBuilder inventoryBuilder = new InventoryBuilder();
        inventoryBuilder.loadFromFile(fileConfiguration);
        registerInventoryBuilder(inventoryBuilder);
    }

    /**
     * Unregisters an InventoryBuilder, removing its commands and closing active inventories.
     * @param inventoryBuilder The InventoryBuilder to unregister.
     */
    public void unregisterInventoryBuilder(InventoryBuilder inventoryBuilder) {
        unregisterOpenCommands(inventoryBuilder);

        Set<Inventory> inventoriesToRemove = new HashSet<>();
        activeInventories.forEach((inventory, handler) -> {
            if (handler.equals(inventoryBuilder)) {
                inventoriesToRemove.add(inventory);
            }
        });

        inventoriesToRemove.forEach(inventory -> {
            inventory.getViewers().stream()
                    .filter(entity -> entity instanceof Player)
                    .forEach(HumanEntity::closeInventory);

            unregisterInventory(inventory);
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
        List<String> openCommands = inventoryBuilder.getOpenCommands();

        if (openCommands == null || openCommands.isEmpty()) return;

        for (String cmd : openCommands) {
            CommandAPI.unregister(cmd);
        }
    }

    /**
     * Disables the inventory manager and closes all active inventories.
     */
    public void disable() {
        this.pluginGUIs.forEach(this::unregisterInventoryBuilder);
    }

    /**
     * Registers an active inventory to track interactions.
     * @param inventory The inventory instance.
     * @param handler The handler to manage events for this inventory.
     */
    private void registerActiveInventory(Inventory inventory, InventoryHandler handler) {
        activeInventories.put(inventory, handler);
    }

    /**
     * Unregisters an inventory, removing it from tracking.
     * @param inventory The inventory instance.
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
