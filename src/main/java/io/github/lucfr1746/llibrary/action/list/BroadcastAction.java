package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.util.helper.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastAction extends Action {

    public String message;

    public BroadcastAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void execute(Player target) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(StringUtil.format(this.message, player)));
    }
}
