package io.github.lucfr1746.llibrary.action;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.utils.Hooks;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BroadcastAction extends Action {

    public String message;

    public BroadcastAction(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void execute(Player target) {
        if (Hooks.isPAPIEnabled()) {
            this.message = PlaceholderAPI.setPlaceholders(target, this.message);
        }
        LLibrary.getInstance().audiences().player(target).sendMessage(MiniMessage.miniMessage().deserialize(this.message));
    }
}
