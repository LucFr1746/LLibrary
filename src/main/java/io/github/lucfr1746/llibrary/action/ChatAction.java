package io.github.lucfr1746.llibrary.action;

import org.bukkit.entity.Player;

public class ChatAction extends Action {

    private final String message;

    public ChatAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void execute(Player target) {
        target.chat(this.message);
    }
}
