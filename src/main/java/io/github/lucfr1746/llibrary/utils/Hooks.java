package io.github.lucfr1746.llibrary.utils;

import org.bukkit.Bukkit;

/**
 * Utility class for checking the availability of certain plugins.
 */
public class Hooks {

    /**
     * Checks if the Vault plugin is enabled.
     *
     * @return true if Vault is enabled, false otherwise.
     */
    public static boolean isVault() {
        return isEnabled("Vault");
    }

    /**
     * Checks if PlaceholderAPI is enabled.
     *
     * @return true if PlaceholderAPI is enabled, false otherwise.
     */
    public static boolean isPAPIEnabled() {
        return isEnabled("PlaceholderAPI");
    }

    /**
     * Generic method to check if a plugin is enabled by its name.
     *
     * @param pluginName The name of the plugin to check.
     * @return true if the plugin is enabled, false otherwise.
     */
    public static boolean isEnabled(String pluginName) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }
}
