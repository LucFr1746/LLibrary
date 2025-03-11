package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

/**
 * Represents an action that grants a permission to a player.
 */
public class GivePermissionAction extends Action {

    private final String permission;

    /**
     * Constructs a GivePermissionAction with the specified permission.
     *
     * @param permission The permission to grant to the player.
     */
    public GivePermissionAction(String permission) {
        this.permission = permission;
    }

    /**
     * Gets the permission that will be granted.
     *
     * @return The permission string.
     */
    public String getPermission() {
        return this.permission;
    }

    /**
     * Executes the action, granting the specified permission to the target player.
     *
     * @param target The player who will receive the permission.
     * @throws IllegalStateException if there is no permission system hooked.
     */
    @Override
    public void execute(Player target) {
        if (LLibrary.getPermission() == null) {
            throw new IllegalStateException("There is no Permission API hooked!");
        }
        LLibrary.getPermission().playerAdd(target, this.permission);
    }
}