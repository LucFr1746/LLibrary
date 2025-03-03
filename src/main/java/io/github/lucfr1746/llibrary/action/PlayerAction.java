package io.github.lucfr1746.llibrary.action;

import org.bukkit.entity.Player;

public class PlayerAction extends Action {

    private final String command;

    public PlayerAction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public void run(Player target) {
        target.chat("/" + this.command);
    }
}
