package io.github.lucfr1746.llibrary.action;

import io.github.lucfr1746.llibrary.utils.Hooks;
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
        if (Hooks.isPAPIEnabled()) {
            this.command = PlaceholderAPI.setPlaceholders(target, this.command);
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command);
    }
}
