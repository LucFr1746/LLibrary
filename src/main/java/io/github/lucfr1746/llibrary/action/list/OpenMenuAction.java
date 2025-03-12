package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

/**
 * Represents an action that opens a custom GUI menu for a player.
 * This action uses the InventoryManager to open a menu based on its ID.
 */
public class OpenMenuAction extends Action {

    private final String menuID;

    /**
     * Constructs an OpenMenuAction with the specified menu ID.
     *
     * @param menuID The ID of the menu to open.
     */
    public OpenMenuAction(String menuID) {
        this.menuID = menuID;
    }

    /**
     * Executes the action by opening the menu associated with the given ID for the specified player.
     *
     * @param player The player who will see the opened menu.
     */
    @Override
    public void execute(Player player) {
        if (menuID == null || menuID.isBlank()) {
            LLibrary.getPluginLogger().warning("Menu ID is null or empty. Cannot open menu.");
            return;
        }
        LLibrary.getInventoryManager().openGUI(menuID, player);
    }
}