package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.util.helper.StringUtil;
import org.bukkit.entity.Player;

/**
 * Represents an action that sends a formatted message to a player.
 */
public class MessageAction extends Action {

    private String message;

    /**
     * Constructs a MessageAction with the specified message.
     *
     * @param message The message to send to the player.
     */
    public MessageAction(String message) {
        this.message = message;
    }

    /**
     * Gets the message that will be sent.
     *
     * @return The message string.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Executes the action, sending the formatted message to the target player.
     *
     * @param target The player who will receive the message.
     */
    @Override
    public void execute(Player target) {
        target.sendMessage(StringUtil.format(this.message, target));
    }
}