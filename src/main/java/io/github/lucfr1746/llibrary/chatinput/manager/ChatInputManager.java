package io.github.lucfr1746.llibrary.chatinput.manager;

import io.github.lucfr1746.llibrary.chatinput.input.ChatInput;
import io.github.lucfr1746.llibrary.chatinput.input.enums.InputMessage;
import io.github.lucfr1746.llibrary.chatinput.request.ChatInputRequest;
import io.github.lucfr1746.llibrary.chatinput.response.ChatInputResponse;
import io.github.lucfr1746.llibrary.chatinput.response.enums.InputStatus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * The main class that provides the function to prompt for input.
 * */
public class ChatInputManager {
    private static JavaPlugin plugin;
    private static final Map<UUID, Queue<ChatInputRequest>> requestsQueue = new HashMap<>();

    /**
     * Prompts the player with the given unique id for input of a given form
     * defined by the ChatInput object.
     *
     * @param plugin the plugin
     * @param uuid the unique id
     * @param input the input type
     *
     * @return a CompletableFuture that returns the response to the prompt.
     * */
    public static CompletableFuture<ChatInputResponse> promptInput(@NotNull JavaPlugin plugin,
                                                                   @NotNull UUID uuid,
                                                                   @NotNull ChatInput input) {
        if (ChatInputManager.plugin == null) initialize(plugin);
        Queue<ChatInputRequest> queue = requestsQueue.computeIfAbsent(uuid, k -> new LinkedList<>());
        ChatInputRequest request = new ChatInputRequest(input);

        queue.add(request);
        if (queue.size() == 1)
            initializeInputRequest(uuid, request);

        return request.getFuture();
    }

    /* Initializes the tool for the hosting plugin. */
    private static void initialize(@NotNull JavaPlugin plugin) {
        ChatInputManager.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new EventsListener(), plugin);
    }

    /* Initializes the new request with the timeout task and sending the prompt message. */
    private static void initializeInputRequest(@NotNull UUID uuid, @NotNull ChatInputRequest request) {
        /* Sending the prompt message. */
        sendInputMessage(uuid, request.getInput(), InputMessage.PROMPT);

        /* Initializing the timeout task. */
        if (request.getInput().getTimeout() < 0) return;
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!requestsQueue.containsKey(uuid)) return;
            Queue<ChatInputRequest> requests = requestsQueue.get(uuid);

            if (requests.element() != request) return;
            sendInputMessage(uuid, request.getInput(), InputMessage.TIMEOUT);
            completeCurrentRequest(uuid, new ChatInputResponse(InputStatus.TIMEOUT, ""));
        }, request.getInput().getTimeout() * 20L);
    }
    
    /* Sends the input message to the player with the given unique id if he's online. */
    private static void sendInputMessage(@NotNull UUID uuid, @NotNull ChatInput input, @NotNull InputMessage message) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) input.sendMessage(message, player);
    }

    /* Gets the current head of the input requests queue. */
    @Nullable
    protected static ChatInput getCurrentRequest(@NotNull UUID uuid) {
        if (!requestsQueue.containsKey(uuid)) return null;
        return requestsQueue.get(uuid).element().getInput();
    }

    /* Completes the CompletableFuture of the current head of the input requests queue. */
    protected static void completeCurrentRequest(@NotNull UUID uuid, @NotNull ChatInputResponse response) {
        Queue<ChatInputRequest> requests = requestsQueue.get(uuid);

        requests.element().getFuture().complete(response);
        requests.remove();

        if (requests.isEmpty()) requestsQueue.remove(uuid);
        else initializeInputRequest(uuid, requests.element());
    }

    /* Completes all the CompletableFuture from the input requests queue. */
    protected static void completeAllRequests(@NotNull UUID uuid, @NotNull ChatInputResponse response) {
        if (!requestsQueue.containsKey(uuid)) return;

        requestsQueue.get(uuid).forEach(request -> request.getFuture().complete(response));
        requestsQueue.remove(uuid);
    }

    /* Clears all the input requests without completing them. */
    protected static void clearAllRequests(@NotNull UUID uuid) {
        requestsQueue.remove(uuid);
    }
}
