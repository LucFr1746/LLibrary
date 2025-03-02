package io.github.lucfr1746.llibrary.requirement;

import org.bukkit.entity.Player;

public class HasPermissionRequirement extends Requirement {

    private final String permission;

    public HasPermissionRequirement(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean evaluate(Player player) {
        return player.hasPermission(permission);
    }
}
