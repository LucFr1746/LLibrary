package io.github.lucfr1746.llibrary.requirement.list;

import io.github.lucfr1746.llibrary.requirement.Requirement;
import org.bukkit.entity.Player;

public class HasPermissionRequirement extends Requirement {

    private final String permission;

    public HasPermissionRequirement(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }

    @Override
    public boolean evaluate(Player player) {
        return player.hasPermission(this.permission);
    }
}
