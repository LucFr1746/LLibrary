package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

/**
 * Represents an action that sends a MiniMessage-formatted message to a player.
 */
public class MiniMessageAction extends Action {

    private String message;

    /**
     * Constructs a MiniMessageAction with the specified message.
     *
     * @param message The MiniMessage-formatted string to send to the player.
     */
    public MiniMessageAction(String message) {
        this.message = message;
    }

    /**
     * Gets the MiniMessage-formatted message.
     *
     * @return The message string.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Executes the action, sending the MiniMessage-formatted message to the target player.
     * If PlaceholderAPI is enabled, placeholders will be replaced before sending.
     *
     * @param target The player who will receive the message.
     */
    @Override
    public void execute(Player target) {
        if (LLibrary.isPlaceholderAPIEnabled()) {
            this.message = PlaceholderAPI.setPlaceholders(target, this.message);
        }
        LLibrary.getAudiences().player(target).sendMessage(MiniMessage.miniMessage().deserialize(this.message));
    }
}