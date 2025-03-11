package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.util.helper.StringUtil;
import org.bukkit.entity.Player;

/**
 * Represents an action that broadcasts a message to all players in the same world as the target.
 */
public class BroadcastWorldAction extends Action {

    private final String message;

    /**
     * Constructs a BroadcastWorldAction with the specified message.
     * @param message The message to broadcast.
     */
    public BroadcastWorldAction(String message) {
        this.message = message;
    }

    /**
     * Gets the broadcast message.
     * @return The message being broadcasted.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Executes the broadcast world action, sending the message to all players in the same world as the target.
     * @param target The player triggering the action.
     */
    @Override
    public void execute(Player target) {
        for (final Player broadcastTarget : target.getWorld().getPlayers()) {
            broadcastTarget.sendMessage(StringUtil.format(this.message, broadcastTarget));
        }
    }
}