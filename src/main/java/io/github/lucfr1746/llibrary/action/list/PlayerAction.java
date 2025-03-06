package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

public class PlayerAction extends Action {

    private final String command;

    public PlayerAction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    @Override
    public void execute(Player target) {
        target.chat("/" + this.command);
    }
}
