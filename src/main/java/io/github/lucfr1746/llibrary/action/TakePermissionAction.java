package io.github.lucfr1746.llibrary.action;

import io.github.lucfr1746.llibrary.LLibrary;
import org.bukkit.entity.Player;

public class TakePermissionAction extends Action {

    private final String permission;

    public TakePermissionAction(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }

    @Override
    public void execute(Player target) {
        LLibrary.getPermissions().playerRemove(target, this.permission);
    }
}
