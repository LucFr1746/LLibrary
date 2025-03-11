package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Represents an action that executes a console command.
 */
public class ConsoleAction extends Action {

    private String command;

    /**
     * Constructs a new ConsoleAction with the specified command.
     *
     * @param command The command to be executed by the console.
     */
    public ConsoleAction(String command) {
        this.command = command;
    }

    /**
     * Gets the command to be executed.
     *
     * @return The command as a string.
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Executes the console command, replacing placeholders if PlaceholderAPI is enabled.
     *
     * @param target The player whose placeholders will be used for the command.
     */
    @Override
    public void execute(Player target) {
        if (LLibrary.isPlaceholderAPIEnabled()) {
            this.command = PlaceholderAPI.setPlaceholders(target, this.command);
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command);
    }
}