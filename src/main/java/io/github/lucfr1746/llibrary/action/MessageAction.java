package io.github.lucfr1746.llibrary.action;

import io.github.lucfr1746.llibrary.LLibrary;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class MessageAction extends Action {

    private final String message;

    public MessageAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void run(Player target) {
        LLibrary.getInstance().audiences().player(target).sendMessage(MiniMessage.miniMessage().deserialize(this.message));
    }
}
