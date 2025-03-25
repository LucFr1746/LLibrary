package io.github.lucfr1746.llibrary.chatinput.manager;

import io.github.lucfr1746.llibrary.chatinput.input.ChatInput;
import io.github.lucfr1746.llibrary.chatinput.input.enums.InputFlag;
import io.github.lucfr1746.llibrary.chatinput.input.enums.InputMessage;
import io.github.lucfr1746.llibrary.chatinput.response.ChatInputResponse;
import io.github.lucfr1746.llibrary.chatinput.response.enums.InputStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

/**
 * An event listener class that is responsible for getting the input values,
 * restricting the player and complete input requests when needed.
 * */
public class EventsListener implements Listener {

    /* Gets the input from chat, filters it and possibly completes the input request. */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ChatInput input = ChatInputManager.getCurrentRequest(player.getUniqueId());

        if (input == null) return;
        event.setCancelled(true);

        if (input.isValidInput(event.getMessage())) {
            input.sendMessage(InputMessage.SUCCESS, player, event.getMessage());
            ChatInputManager.completeCurrentRequest(
                    player.getUniqueId(),
                    new ChatInputResponse(InputStatus.SUCCESS, event.getMessage()));

            return;
        }

        if (input.getAttempts() > 0)
            input.setAttempts(input.getAttempts() - 1);

        if (input.getAttempts() == 0) {
            ChatInputManager.completeCurrentRequest(
                    player.getUniqueId(),
                    new ChatInputResponse(InputStatus.FAILED_ATTEMPTS, ""));
            return;
        }

        input.sendMessage(InputMessage.INVALID_INPUT, player);
    }

    /* Handles the case when commands are not allowed. */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        ChatInput input = ChatInputManager.getCurrentRequest(player.getUniqueId());

        if (input == null) return;
        if (!input.hasFlag(InputFlag.DISABLE_COMMANDS)) return;
        String command = event.getMessage().substring(1).split(" ")[0];

        if (input.isCommandAllowed(command)) return;
        input.sendMessage(InputMessage.DISABLED_COMMANDS, player, command);
        event.setCancelled(true);
    }

    /* Handles the case when movement is not allowed. */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() == null) return;
        Player player = event.getPlayer();
        ChatInput input = ChatInputManager.getCurrentRequest(player.getUniqueId());

        if (input == null) return;
        if (!input.hasFlag(InputFlag.DISABLE_MOVEMENT)) return;
        if (event.getFrom().distanceSquared(event.getTo()) == 0) return;
        input.sendMessage(InputMessage.DISABLED_MOVEMENT, player);
        event.setCancelled(true);
    }

    /* Handles the case when interactions are not allowed. */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ChatInput input = ChatInputManager.getCurrentRequest(player.getUniqueId());

        if (input == null) return;
        if (!input.hasFlag(InputFlag.DISABLE_INTERACTION)) return;
        input.sendMessage(InputMessage.DISABLED_INTERACTION, player);
        event.setCancelled(true);
    }

    /* Handles the case when interactions are not allowed. */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteracted(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ChatInput input = ChatInputManager.getCurrentRequest(player.getUniqueId());

        if (input == null) return;
        if (!input.hasFlag(InputFlag.DISABLE_INTERACTION)) return;
        input.sendMessage(InputMessage.DISABLED_INTERACTION, player);
        event.setCancelled(true);
    }

    /* Handles the case when there are somehow input requests for the player when he joins. */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        ChatInputManager.clearAllRequests(event.getPlayer().getUniqueId());
    }

    /* Clears the input requests when the player leaves the server. */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        ChatInputManager.completeAllRequests(
                event.getPlayer().getUniqueId(),
                new ChatInputResponse(InputStatus.PLAYER_QUIT, ""));
    }

    /* Clears the input requests when the player dies. */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        ChatInputManager.completeAllRequests(
                event.getEntity().getUniqueId(),
                new ChatInputResponse(InputStatus.PLAYER_QUIT, ""));
    }
}
