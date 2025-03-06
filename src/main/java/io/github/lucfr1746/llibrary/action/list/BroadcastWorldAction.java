package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.util.helper.StringUtil;
import org.bukkit.entity.Player;

public class BroadcastWorldAction extends Action {

    public String message;

    public BroadcastWorldAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void execute(Player target) {
        for (final Player broadcastTarget : target.getWorld().getPlayers()) {
            broadcastTarget.sendMessage(StringUtil.format(this.message, broadcastTarget));
        }
    }
}
