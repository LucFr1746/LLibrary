package io.github.lucfr1746.llibrary.action;

import io.github.lucfr1746.llibrary.LLibrary;
import org.bukkit.entity.Player;

public class GivePermissionAction extends Action {

    private final String permission;

    public GivePermissionAction(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }

    @Override
    public void execute(Player target) {
        LLibrary.getPermissions().playerAdd(target, this.permission);
    }
}
