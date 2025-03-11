package io.github.lucfr1746.llibrary.requirement.list;

import io.github.lucfr1746.llibrary.requirement.Requirement;
import org.bukkit.entity.Player;

/**
 * Represents a requirement that checks if a player has a specific permission.
 */
public class HasPermissionRequirement extends Requirement {

    private final String permission;

    /**
     * Creates a new permission requirement.
     *
     * @param permission The permission node required.
     */
    public HasPermissionRequirement(String permission) {
        this.permission = permission;
    }

    /**
     * Gets the required permission node.
     *
     * @return The required permission.
     */
    public String getPermission() {
        return this.permission;
    }

    /**
     * Evaluates whether the player has the required permission.
     *
     * @param player The player to check.
     * @return {@code true} if the player has the required permission, otherwise {@code false}.
     */
    @Override
    public boolean evaluate(Player player) {
        return player.hasPermission(this.permission);
    }
}