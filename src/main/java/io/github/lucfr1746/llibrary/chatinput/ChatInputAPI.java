package io.github.lucfr1746.llibrary.chatinput;

import io.github.lucfr1746.llibrary.chatinput.input.ChatInput;
import io.github.lucfr1746.llibrary.chatinput.input.enums.InputFlag;
import io.github.lucfr1746.llibrary.chatinput.input.enums.InputMessage;
import io.github.lucfr1746.llibrary.chatinput.manager.ChatInputManager;
import io.github.lucfr1746.llibrary.chatinput.response.enums.InputStatus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * API for handling chat input interactions with players.
 * Allows setting various parameters like flags, allowed commands, messages, and timeouts.
 */
public class ChatInputAPI {

    private final JavaPlugin plugin;
    private final Player player;
    private final ChatInput input;

    /**
     * Constructs a new ChatInputAPI instance.
     *
     * @param plugin    The plugin instance.
     * @param player    The player who will provide input.
     * @param inputType The type of chat input to handle.
     * @throws NullPointerException if any parameter is null.
     */
    public ChatInputAPI(JavaPlugin plugin, Player player, ChatInput inputType) {
        this.plugin = Objects.requireNonNull(plugin, "Plugin cannot be null");
        this.player = Objects.requireNonNull(player, "Player cannot be null");
        this.input = Objects.requireNonNull(inputType, "InputType cannot be null");
    }

    /**
     * Creates a new instance of ChatInputAPI.
     *
     * @param plugin    The plugin instance.
     * @param player    The player who will provide input.
     * @param inputType The type of chat input to handle.
     * @return A new ChatInputAPI instance.
     */
    public static ChatInputAPI create(JavaPlugin plugin, Player player, ChatInput inputType) {
        return new ChatInputAPI(plugin, player, inputType);
    }

    /**
     * Sets input flags.
     *
     * @param inputFlags Flags to set.
     * @return The current ChatInputAPI instance for method chaining.
     */
    public ChatInputAPI setFlags(@NotNull InputFlag... inputFlags) {
        this.input.setFlags(inputFlags);
        return this;
    }

    /**
     * Sets the maximum number of attempts allowed.
     *
     * @param attempts The number of attempts.
     * @return The current ChatInputAPI instance for method chaining.
     */
    public ChatInputAPI setAttempts(int attempts) {
        this.input.setAttempts(attempts);
        return this;
    }

    /**
     * Sets allowed commands during input.
     *
     * @param commands List of allowed commands.
     * @return The current ChatInputAPI instance for method chaining.
     */
    public ChatInputAPI setAllowedCommands(@NotNull List<String> commands) {
        this.input.setAllowedCommands(commands);
        return this;
    }

    /**
     * Sets allowed commands during input.
     *
     * @param commands Array of allowed commands.
     * @return The current ChatInputAPI instance for method chaining.
     */
    public ChatInputAPI setAllowedCommands(@NotNull String... commands) {
        return setAllowedCommands(List.of(commands));
    }

    /**
     * Sets a custom message for the chat input prompt.
     *
     * @param inputMessage The type of message to set.
     * @param message      The message text.
     * @return The current ChatInputAPI instance for method chaining.
     */
    public ChatInputAPI setMessage(@NotNull InputMessage inputMessage, @NotNull String message) {
        this.input.setMessage(inputMessage, message);
        return this;
    }

    /**
     * Sets the timeout duration for the chat input.
     *
     * @param timeout Timeout in seconds.
     * @return The current ChatInputAPI instance for method chaining.
     */
    public ChatInputAPI setTimeout(int timeout) {
        this.input.setTimeout(timeout);
        return this;
    }

    /**
     * Executes the chat input prompt and processes the response.
     *
     * @param onSuccess Callback function to handle successful input.
     */
    public void execute(@NotNull Consumer<String> onSuccess) {
        ChatInputManager.promptInput(this.plugin, this.player.getUniqueId(), this.input)
                .thenAccept(response -> {
                    if (response.status() == InputStatus.SUCCESS) {
                        onSuccess.accept(response.value());
                    } else {
                        this.plugin.getLogger().log(Level.WARNING,
                                "ChatInput failed for player {0} with status {1}",
                                new Object[]{player.getName(), response.status()});
                    }
                });
    }

    /**
     * Executes the chat input prompt and provides callbacks for both success and failure cases.
     *
     * @param onSuccess Callback function to handle successful input.
     * @param onFailure Callback function to handle input failures.
     */
    public void execute(@NotNull Consumer<String> onSuccess, @NotNull Consumer<InputStatus> onFailure) {
        ChatInputManager.promptInput(this.plugin, this.player.getUniqueId(), this.input)
                .thenAccept(response -> {
                    if (response.status() == InputStatus.SUCCESS) {
                        onSuccess.accept(response.value());
                    } else {
                        onFailure.accept(response.status());
                    }
                });
    }
}