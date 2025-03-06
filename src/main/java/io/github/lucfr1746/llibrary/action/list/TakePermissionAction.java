package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
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
        if (LLibrary.getPermission() == null) {
            throw new IllegalStateException("There is no Permission API hooked!");
        }
        LLibrary.getPermission().playerRemove(target, this.permission);
    }
}
