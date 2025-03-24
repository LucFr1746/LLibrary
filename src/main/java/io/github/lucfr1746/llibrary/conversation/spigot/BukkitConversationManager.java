package io.github.lucfr1746.llibrary.conversation.spigot;

import io.github.lucfr1746.llibrary.conversation.base.Conversation;
import io.github.lucfr1746.llibrary.conversation.base.ConversationContext;
import io.github.lucfr1746.llibrary.conversation.base.ConversationManager;
import io.github.lucfr1746.llibrary.conversation.base.timeout.TimeoutScheduler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a conversation manager for the bukkit server software.
 *
 * @author MrIvanPlays
 */
public class BukkitConversationManager
        extends ConversationManager<String, BukkitConversationPartner> {

    private final Map<UUID, BukkitConversationPartner> partners = new HashMap<>();
    private final TimeoutScheduler timeoutScheduler;

    public BukkitConversationManager(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");
        plugin.getServer().getPluginManager().registerEvents(new BukkitConvoListener(this), plugin);
        timeoutScheduler = new BukkitTimeoutScheduler(plugin);
    }

    void removePartner(UUID uuid) {
        partners.remove(uuid);
    }

    /**
     * Returns the {@link TimeoutScheduler} for this conversation manager.
     *
     * @return timeout scheduler
     */
    public TimeoutScheduler getTimeoutScheduler() {
        return timeoutScheduler;
    }

    /**
     * Returns a new {@link Conversation.Builder} populated with the specified {@link Player} {@code
     * player} as a conversation partner, with this manager as a parent manager and with this
     * manager's {@link #getTimeoutScheduler()}
     *
     * @param player conversation partner player
     * @return conversation builder
     */
    public Conversation.Builder<String, BukkitConversationPartner> newConversationBuilder(
            Player player) {
        Objects.requireNonNull(player, "player");
        return Conversation.<String, BukkitConversationPartner>newBuilder()
                .parentManager(this)
                .withTimeoutScheduler(timeoutScheduler)
                .withConversationPartner(
                        partners.computeIfAbsent(
                                player.getUniqueId(), $ -> new BukkitConversationPartner(player)));
    }

    @Override
    protected void unregisterConversation(UUID conversationPartner, ConversationContext.EndState endState) {
        super.unregisterConversation(conversationPartner, endState);
    }
}
