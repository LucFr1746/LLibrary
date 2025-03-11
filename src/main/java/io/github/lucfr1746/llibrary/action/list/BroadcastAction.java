package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.util.helper.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Represents an action that broadcasts a formatted message to all online players.
 */
public class BroadcastAction extends Action {

    private final String message;

    /**
     * Constructs a BroadcastAction with the specified message.
     * @param message The message to broadcast.
     */
    public BroadcastAction(String message) {
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
     * Executes the broadcast action, sending the formatted message to all online players.
     * @param target The player triggering the action (not directly used in this implementation).
     */
    @Override
    public void execute(Player target) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(StringUtil.format(this.message, player)));
    }
}