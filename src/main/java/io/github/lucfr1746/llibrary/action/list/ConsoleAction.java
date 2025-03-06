package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleAction extends Action {

    private String command;

    public ConsoleAction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    @Override
    public void execute(Player target) {
        if (LLibrary.isPlaceholderAPIEnabled()) {
            this.command = PlaceholderAPI.setPlaceholders(target, this.command);
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command);
    }
}
