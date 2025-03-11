package io.github.lucfr1746.llibrary;

import io.github.lucfr1746.llibrary.util.helper.Logger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class for the LLibrary plugin.
 * This serves as the core plugin that manages initialization, enabling, and disabling logic.
 */
public final class LLibrary extends JavaPlugin {

    private static LLibrary plugin;
    private static PluginLoader pluginLoader;

    /**
     * Called when the plugin is loaded before the server starts.
     * Initializes the plugin instance and plugin loader.
     */
    @Override
    public void onLoad() {
        plugin = this;
        pluginLoader = new PluginLoader(this);
    }

    /**
     * Called when the plugin is enabled.
     * Ensures the plugin loader is initialized and starts it.
     */
    @Override
    public void onEnable() {
        if (pluginLoader == null)
            pluginLoader = new PluginLoader(this);
        pluginLoader.enable();
    }

    /**
     * Called when the plugin is disabled.
     * Ensures the plugin loader is initialized and stops it.
     */
    @Override
    public void onDisable() {
        if (pluginLoader == null)
            pluginLoader = new PluginLoader(this);
        pluginLoader.disable();
    }

    /**
     * Gets the instance of this plugin.
     *
     * @return The LLibrary plugin instance.
     */
    public static LLibrary getInstance() {
        return plugin;
    }

    /**
     * Gets the custom logger for this plugin.
     *
     * @return The plugin's logger.
     */
    public static Logger getPluginLogger() {
        return pluginLoader.getLogger();
    }

    /**
     * Gets the Adventure API's audience manager.
     *
     * @return The BukkitAudiences instance.
     */
    public static BukkitAudiences getAudiences() {
        return pluginLoader.getAudiences();
    }

    /**
     * Gets the Vault economy handler.
     *
     * @return The economy handler, or null if Vault is not hooked.
     */
    public static Economy getEconomy() {
        return pluginLoader.getEconomy();
    }

    /**
     * Gets the Vault permission handler.
     *
     * @return The permission handler, or null if Vault is not hooked.
     */
    public static Permission getPermission() {
        return pluginLoader.getPermission();
    }

    /**
     * Checks if Paper API is available.
     *
     * @return {@code true} if Paper API is hooked, otherwise {@code false}.
     */
    public static boolean hasPaperAPI() {
        return pluginLoader.getHooks().hasPaperAPI();
    }

    /**
     * Checks if Vault is enabled.
     *
     * @return {@code true} if Vault is enabled, otherwise {@code false}.
     */
    public static boolean isVaultEnabled() {
        return pluginLoader.getHooks().isVaultEnabled();
    }

    /**
     * Checks if PlaceholderAPI is enabled.
     *
     * @return {@code true} if PlaceholderAPI is enabled, otherwise {@code false}.
     */
    public static boolean isPlaceholderAPIEnabled() {
        return pluginLoader.getHooks().isPAPIEnabled();
    }
}