package io.github.lucfr1746.llibrary.util.helper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * A custom logger utility for Minecraft plugins using the Spigot API.
 * This class allows for standardized logging with support for different log levels
 * and console messages with color formatting.
 */
public class Logger {

    private final Plugin plugin;

    /**
     * Constructs a Logger associated with a given plugin.
     *
     * @param plugin The plugin instance this logger is bound to.
     */
    public Logger(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Constructs a Logger by looking up a plugin by its name.
     *
     * @param pluginName The name of the plugin.
     * @throws IllegalArgumentException if the plugin cannot be found.
     */
    public Logger(String pluginName) {
        this.plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (this.plugin == null) {
            throw new IllegalArgumentException("Plugin '" + pluginName + "' not found!");
        }
    }

    /**
     * Logs an informational message.
     *
     * @param message The message to log at INFO level.
     */
    public void info(String message) {
        this.plugin.getLogger().log(Level.INFO, message);
    }

    /**
     * Logs a warning message.
     *
     * @param message The message to log at WARNING level.
     */
    public void warning(String message) {
        this.plugin.getLogger().log(Level.WARNING, message);
    }

    /**
     * Logs an error message.
     *
     * @param message The message to log at SEVERE level.
     */
    public void error(String message) {
        this.plugin.getLogger().log(Level.SEVERE, message);
    }

    /**
     * Sends a success message to the console with green formatting.
     *
     * @param message The message to display as a success.
     */
    public void success(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + getPluginPrefix() + message);
    }

    /**
     * Sends a debug message to the console with blue formatting.
     *
     * @param message The message to display for debugging purposes.
     */
    public void debug(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + getPluginPrefix() + message);
    }

    /**
     * Retrieves the plugin's log prefix.
     *
     * @return The formatted prefix containing the plugin's name.
     */
    private String getPluginPrefix() {
        return "[" + plugin.getName() + "] ";
    }
}