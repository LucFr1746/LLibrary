package io.github.lucfr1746.llibrary.action;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleAction extends Action {

    private final String command;

    public ConsoleAction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public void run(Player target) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(target, this.command));
    }
}
