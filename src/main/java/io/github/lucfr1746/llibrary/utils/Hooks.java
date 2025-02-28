package io.github.lucfr1746.llibrary.utils;

import org.bukkit.Bukkit;

public class Hooks {

    public static boolean isVault() {
        return isEnabled("Vault");
    }

    public static boolean isPAPIEnabled() {
        return isEnabled("PlaceholderAPI");
    }

    public static boolean isEnabled(String pluginName) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }
}
