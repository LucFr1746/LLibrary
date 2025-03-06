package io.github.lucfr1746.llibrary;

import io.github.lucfr1746.llibrary.util.helper.Logger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public final class LLibrary extends JavaPlugin {

    private static LLibrary plugin;
    private static PluginLoader pluginLoader;

    @Override
    public void onLoad() {
        plugin = this;
        pluginLoader = new PluginLoader(this);
    }

    @Override
    public void onEnable() {
        if (pluginLoader == null)
            pluginLoader = new PluginLoader(this);
        pluginLoader.enable();
    }

    @Override
    public void onDisable() {
        if (pluginLoader == null)
            pluginLoader = new PluginLoader(this);
        pluginLoader.disable();
    }

    public static LLibrary getInstance() {
        return plugin;
    }

    public static Logger getPluginLogger() {
        return pluginLoader.getLogger();
    }

    public static BukkitAudiences getAudiences() {
        return pluginLoader.getAudiences();
    }

    public static Economy getEconomy() {
        return pluginLoader.getEconomy();
    }

    public static Permission getPermission() {
        return pluginLoader.getPermission();
    }

    public static boolean isVaultEnabled() {
        return pluginLoader.getHooks().isVaultEnabled();
    }

    public static boolean isPlaceholderAPIEnabled() {
        return pluginLoader.getHooks().isPAPIEnabled();
    }
}
