package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

/**
 * Represents an action that removes a permission from a player.
 */
public class TakePermissionAction extends Action {

    private final String permission;

    /**
     * Constructs a TakePermissionAction with a specified permission to remove.
     *
     * @param permission The permission to be removed.
     */
    public TakePermissionAction(String permission) {
        this.permission = permission;
    }

    /**
     * Gets the permission that will be removed from the player.
     *
     * @return The permission string.
     */
    public String getPermission() {
        return this.permission;
    }

    /**
     * Executes the action, removing the specified permission from the target player.
     * If the permission system is not available, an exception is thrown.
     *
     * @param target The player from whom the permission will be removed.
     * @throws IllegalStateException If there is no permission system hooked.
     */
    @Override
    public void execute(Player target) {
        if (LLibrary.getPermission() == null) {
            throw new IllegalStateException("There is no Permission API hooked!");
        }
        LLibrary.getPermission().playerRemove(target, this.permission);
    }
}
