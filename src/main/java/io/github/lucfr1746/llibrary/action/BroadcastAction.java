package io.github.lucfr1746.llibrary.action;

import io.github.lucfr1746.llibrary.LLibrary;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class BroadcastAction extends Action {

    public final String message;

    public BroadcastAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void run() {
        LLibrary.getInstance().audiences().all().sendMessage(MiniMessage.miniMessage().deserialize(this.message));
    }
}
