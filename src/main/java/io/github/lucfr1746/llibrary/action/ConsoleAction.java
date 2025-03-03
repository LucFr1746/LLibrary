package io.github.lucfr1746.llibrary.action;

import org.bukkit.Bukkit;

public class ConsoleAction extends Action {

    private final String command;

    public ConsoleAction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public void run() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command);
    }
}
