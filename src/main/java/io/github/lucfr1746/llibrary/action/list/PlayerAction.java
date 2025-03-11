package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

/**
 * Represents an action that forces a player to execute a command.
 */
public class PlayerAction extends Action {

    private final String command;

    /**
     * Constructs a PlayerAction with the specified command.
     *
     * @param command The command to be executed by the player.
     */
    public PlayerAction(String command) {
        this.command = command;
    }

    /**
     * Gets the command that will be executed by the player.
     *
     * @return The command string.
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Executes the action, making the target player run the specified command.
     *
     * @param target The player who will execute the command.
     */
    @Override
    public void execute(Player target) {
        target.chat("/" + this.command);
    }
}