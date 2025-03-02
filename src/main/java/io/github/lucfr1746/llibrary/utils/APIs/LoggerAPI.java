package io.github.lucfr1746.llibrary.utils.APIs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

/**
 * A utility class for logging messages to the Minecraft server console
 * with color-coded output for different log levels.
 * This class supports logging information, warnings, errors, successes, and debug messages.
 */
public class LoggerAPI {

    private final Plugin plugin;

    /**
     * Constructs a Logger instance for a specific plugin.
     *
     * @param plugin the plugin instance to associate with this logger.
     * @throws IllegalArgumentException if the plugin is null.
     */
    public LoggerAPI(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null.");
        }
        this.plugin = plugin;
    }

    /**
     * Constructs a Logger instance for a plugin by its name.
     *
     * @param pluginName the name of the plugin to associate with this logger.
     * @throws IllegalArgumentException if the plugin with the specified name is not found.
     */
    public LoggerAPI(String pluginName) {
        this.plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (this.plugin == null) {
            throw new IllegalArgumentException("Plugin '" + pluginName + "' not found!");
        }
    }

    /**
     * Logs an informational message to the console.
     *
     * @param message the message to log.
     */
    public void info(String message) {
        sendMessage(ChatColor.WHITE, message);
    }

    /**
     * Logs a warning message to the console.
     *
     * @param message the message to log.
     */
    public void warning(String message) {
        sendMessage(ChatColor.YELLOW, message);
    }

    /**
     * Logs a warning message and an exception to the console.
     *
     * @param message   the warning message to log.
     * @param exception the exception to log.
     */
    public void warning(String message, Exception exception) {
        sendMessage(ChatColor.YELLOW, message);
        sendMessage(ChatColor.YELLOW, "Exception: " + exception.getMessage());
    }

    /**
     * Logs an error message to the console.
     *
     * @param message the error message to log.
     */
    public void error(String message) {
        sendMessage(ChatColor.RED, message);
    }

    /**
     * Logs an error message and an exception to the console.
     *
     * @param message   the error message to log.
     * @param exception the exception to log.
     */
    public void error(String message, Exception exception) {
        sendMessage(ChatColor.RED, message);
        sendMessage(ChatColor.RED, "Exception: " + exception.getMessage());
    }

    /**
     * Logs a success message to the console.
     *
     * @param message the success message to log.
     */
    public void success(String message) {
        sendMessage(ChatColor.GREEN, message);
    }

    /**
     * Logs a debug message to the console.
     *
     * @param message the debug message to log.
     */
    public void debug(String message) {
        sendMessage(ChatColor.BLUE, message);
    }

    /**
     * Sends a message to the console with a specified color.
     *
     * @param color   the color to use for the message.
     * @param message the message to log.
     */
    private void sendMessage(ChatColor color, String message) {
        Bukkit.getConsoleSender().sendMessage(color + "[" + plugin.getName() + "] " + message + ChatColor.RESET);
    }
}
