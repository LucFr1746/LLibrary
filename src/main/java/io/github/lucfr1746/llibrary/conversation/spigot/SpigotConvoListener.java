package io.github.lucfr1746.llibrary.conversation.spigot;

import io.github.lucfr1746.llibrary.conversation.base.ConversationContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class SpigotConvoListener implements Listener {

    private final SpigotConversationManager convoManager;

    public SpigotConvoListener(SpigotConversationManager convoManager) {
        this.convoManager = convoManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!convoManager.hasActiveConversation(player.getUniqueId())) {
            return;
        }
        MiniMessage miniMessage = MiniMessage.miniMessage();
        BungeeComponentSerializer bungeeSerializer = BungeeComponentSerializer.get();

        event.setCancelled(true);
        Component message = miniMessage.deserialize(event.getMessage());
        BaseComponent[] baseComponents = bungeeSerializer.serialize(message);

        convoManager.acceptInput(player.getUniqueId(), baseComponents);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        convoManager.removePartner(event.getPlayer().getUniqueId());
        convoManager.unregisterConversation(
                event.getPlayer().getUniqueId(), ConversationContext.EndState.PARTNER_DISCONNECT);
    }
}
