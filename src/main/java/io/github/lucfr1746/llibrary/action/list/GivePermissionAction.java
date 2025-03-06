package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
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
        if (LLibrary.getPermission() == null) {
            throw new IllegalStateException("There is no Permission API hooked!");
        }
        LLibrary.getPermission().playerAdd(target, this.permission);
    }
}
