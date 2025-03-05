package io.github.lucfr1746.llibrary.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder class that manages an inventory GUI. This class is responsible for
 * creating and updating inventories with buttons and event handling, including
 * locking interactions based on the specified {@link LockMode}.
 * <p>
 * The {@link InventoryBuilder} class allows customization of the inventory layout,
 * adding buttons, handling clicks, and setting lock modes to control interaction
 * with the inventory.
 * </p>
 */
public abstract class InventoryBuilder implements InventoryHandler {

    /**
     * Enumeration that defines different lock modes for the inventory interaction.
     * <p>
     * The {@link LockMode} enum provides various lock states that control how the player
     * can interact with the inventory. Each lock mode defines whether the player is allowed
     * to interact with the inventory's GUI, their player inventory, or neither.
     * </p>
     */
    public enum LockMode {

        /**
         * Prevents all interaction with the inventory, both the GUI and the player inventory.
         * <p>
         * When this lock mode is active, players cannot interact with the inventory in any way.
         * </p>
         */
        ALL,

        /**
         * Prevents interaction with the GUI, allowing players to interact only with their player inventory.
         * <p>
         * When this lock mode is active, players cannot modify the items in the GUI but can still interact
         * with their own inventory (e.g., pick up or move items from/to their inventory).
         * </p>
         */
        GUI,

        /**
         * Prevents interaction with the player inventory, allowing players to interact only with the GUI.
         * <p>
         * When this lock mode is active, players can interact with the inventory's GUI but cannot modify
         * their own player inventory (e.g., they cannot pick up or move items in their player inventory).
         * </p>
         */
        PLAYER,
    }

    /**
     * Constructs an instance of the {@link InventoryBuilder}.
     * <p>
     * This constructor is used to create an {@link InventoryBuilder} object, which can then be
     * customized by setting the title, lock mode, and adding buttons to build a dynamic inventory.
     * </p>
     */
    public InventoryBuilder() {
    }

    /**
     * A map that associates inventory slot positions with {@link InventoryButton}s.
     */
    private final Map<Integer, InventoryButton> buttonMap = new HashMap<>();

    /**
     * The lock mode of the inventory, which controls player interaction.
     */
    private LockMode lockMode = LockMode.ALL;

    /**
     * The title of the inventory.
     */
    private String title = "";

    /**
     * The type of the inventory, defining its size and layout.
     */
    private MenuType menuType = MenuType.GENERIC_9X3;

    /**
     * The view of the inventory, which holds the top inventory and allows for item manipulation.
     */
    private InventoryView inventoryView;

    /**
     * Gets the inventory associated with this builder.
     *
     * @return The top inventory associated with the current inventory view.
     */
    public InventoryView getInventoryView() {
        return this.inventoryView;
    }

    /**
     * Sets the menu type (size/layout) of the inventory.
     *
     * @param menuType The {@link MenuType} to be set for the inventory.
     */
    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    /**
     * Gets the menu type (size/layout) of the inventory.
     *
     * @return The current {@link MenuType}.
     */
    public MenuType getMenuType() {
        return this.menuType;
    }

    /**
     * Sets the title of the inventory.
     *
     * @param title The title to be set for the inventory.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the title of the inventory.
     *
     * @return The current title of the inventory.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the lock mode of the inventory.
     *
     * @param lockMode The lock mode to be set.
     */
    public void setLockMode(LockMode lockMode) {
        this.lockMode = lockMode;
    }

    /**
     * Gets the current lock mode of the inventory.
     *
     * @return The current lock mode.
     */
    public LockMode getLockMode() {
        return this.lockMode;
    }

    /**
     * Adds a button to the inventory at the specified slot.
     *
     * @param slot The slot index where the button should be placed.
     * @param button The {@link InventoryButton} to be added.
     */
    public void addButton(int slot, InventoryButton button) {
        this.buttonMap.put(slot, button);
    }

    /**
     * Gets a map of all buttons added to this inventory builder.
     *
     * @return A map of buttons associated with their slot positions.
     */
    public Map<Integer, InventoryButton> getButtonMap() {
        return this.buttonMap;
    }

    /**
     * Decorates the inventory view by setting the icons of all buttons.
     * This method creates the inventory and assigns the icons for each button.
     *
     * @param player The player for whom the inventory is being decorated.
     */
    public void decorate(Player player) {
        this.inventoryView = menuType.typed().create(player, getTitle());
        this.buttonMap.forEach((slot, button) -> {
            if (button.getViewRequirements().stream().allMatch(req -> req.evaluate(player))) {
                ItemStack icon = button.getIconCreator().apply(player);
                this.inventoryView.getTopInventory().setItem(slot, icon);
            } else {
                button.getViewRequirements().stream().filter(req -> !req.evaluate(player))
                        .forEach(req -> req.getDenyHandlers().forEach(handler -> handler.execute(player)));
            }
        });
    }

    /**
     * Updates the button at the specified slot and re-decorates the inventory.
     *
     * @param player The player for whom the button should be updated.
     * @param slot The slot index of the button to update.
     * @param button The new {@link InventoryButton} to replace the old button.
     */
    public void updateButton(Player player, int slot, InventoryButton button) {
        this.buttonMap.put(slot, button);
        this.decorate(player);
    }

    /**
     * Updates multiple buttons at once and re-decorates the inventory.
     *
     * @param player The player for whom the buttons should be updated.
     * @param buttons A map of buttons to be updated, where the key is the slot index.
     */
    public void updateButtons(Player player, Map<Integer, InventoryButton> buttons) {
        this.buttonMap.putAll(buttons);
        this.decorate(player);
    }

    /**
     * Handles the {@link InventoryClickEvent} for this inventory.
     * This method checks the lock mode and cancels the event if necessary.
     *
     * @param event The {@link InventoryClickEvent} to handle.
     */
    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        switch (this.lockMode) {
            case ALL -> {
                event.setCancelled(true);
                if (event.getClickedInventory() instanceof PlayerInventory) {
                    return;
                }
            }
            case GUI -> {
                if (event.getClickedInventory() instanceof PlayerInventory) {
                    return;
                }
                event.setCancelled(true);
            }
            case PLAYER -> {
                if (!(event.getClickedInventory() instanceof PlayerInventory)) {
                    return;
                }
                event.setCancelled(true);
            }
            default -> event.setCancelled(true);
        }

        int slot = event.getRawSlot();
        InventoryButton button = this.buttonMap.get(slot);
        if (button != null && button.getEventConsumer() != null) {
            button.getEventConsumer().accept(event);
        }
    }

    /**
     * Handles the {@link InventoryOpenEvent} for this inventory.
     * This method decorates the inventory for the player when it is opened.
     *
     * @param event The {@link InventoryOpenEvent} to handle.
     */
    @Override
    public void onOpen(InventoryOpenEvent event) {
    }

    /**
     * Handles the {@link InventoryCloseEvent} for this inventory.
     * This method can be used to clean up when the inventory is closed.
     *
     * @param event The {@link InventoryCloseEvent} to handle.
     */
    @Override
    public void onClose(InventoryCloseEvent event) {
    }
}
