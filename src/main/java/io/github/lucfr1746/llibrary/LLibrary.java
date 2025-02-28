package io.github.lucfr1746.llibrary;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import io.github.lucfr1746.llibrary.metrics.bStats;
import io.github.lucfr1746.llibrary.utils.Hooks;
import io.github.lucfr1746.llibrary.utils.LoggerAPI;
import io.github.lucfr1746.llibrary.utils.Util;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class for the {@link LLibrary} plugin.
 * This class extends {@link JavaPlugin} and handles the plugin's loading, enabling, and disabling.
 * It also integrates the {@link CommandAPI} for command handling.
 */
public final class LLibrary extends JavaPlugin {

    /**
     * Default constructor for the {@link LLibrary} plugin.
     * This constructor is used by Bukkit to initialize the plugin instance.
     */
    public LLibrary() {
        super();
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
    }

    @Override
    public void onEnable() {
        new bStats(this, 23768);
        CommandAPI.onEnable();
        // Plugin startup logic
        if (Util.hasPaperAPI()) {
            new LoggerAPI(this).success("PaperAPI detected, some features are now enabled!");
        }
        if (Hooks.isPAPIEnabled()) {
            try {
                new LoggerAPI(this).success("Hooking into PlaceHolderAPI");
            } catch (Exception e) {
                new LoggerAPI(this).error(e.getMessage());
            }
        }
        if (Hooks.isVault()) {
            try {
                new LoggerAPI(this).success("Hooking into Vault");
            } catch (Exception e) {
                new LoggerAPI(this).error(e.getMessage());
            }
        }
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        // Plugin shutdown logic
    }

    /**
     * Retrieves the instance of the {@link LLibrary} plugin.
     * This method returns the singleton instance of the {@link LLibrary} class
     * by fetching the plugin instance using the {@link JavaPlugin#getPlugin(Class)} method.
     *
     * @return The singleton instance of the {@link LLibrary} plugin.
     */
    public static LLibrary getInstance() {
        return JavaPlugin.getPlugin(LLibrary.class);
    }
}
