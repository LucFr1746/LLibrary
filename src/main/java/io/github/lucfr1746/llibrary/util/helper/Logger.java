package io.github.lucfr1746.llibrary.util.helper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class Logger {

    private final Plugin plugin;

    public Logger(Plugin plugin) {
        this.plugin = plugin;
    }

    public Logger(String pluginName) {
        this.plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (this.plugin == null) {
            throw new IllegalArgumentException("Plugin '" + pluginName + "' not found!");
        }
    }

    public void info(String message) {
        this.plugin.getLogger().log(Level.INFO, message);
    }

    public void warning(String message) {
        this.plugin.getLogger().log(Level.WARNING, message);
    }

    public void error(String message) {
        this.plugin.getLogger().log(Level.SEVERE, message);
    }

    public void success(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + getPluginPrefix() + message);
    }

    private String getPluginPrefix() {
        return "[" + plugin.getName() + "] ";
    }
}
