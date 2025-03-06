package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class MiniMessageAction extends Action {

    private String message;

    public MiniMessageAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void execute(Player target) {
        if (LLibrary.isPlaceholderAPIEnabled()) {
            this.message = PlaceholderAPI.setPlaceholders(target, this.message);
        }
        LLibrary.getAudiences().player(target).sendMessage(MiniMessage.miniMessage().deserialize(this.message));
    }
}
