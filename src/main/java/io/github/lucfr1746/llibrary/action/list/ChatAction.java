package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

/**
 * Represents an action where a player sends a chat message.
 */
public class ChatAction extends Action {

    private final String message;

    /**
     * Constructs a ChatAction with the specified message.
     *
     * @param message The chat message to be sent by the player.
     */
    public ChatAction(String message) {
        this.message = message;
    }

    /**
     * Gets the chat message.
     *
     * @return The message to be sent.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Executes the chat action, making the player send the predefined message.
     *
     * @param target The player who will send the message.
     */
    @Override
    public void execute(Player target) {
        target.chat(this.message);
    }
}