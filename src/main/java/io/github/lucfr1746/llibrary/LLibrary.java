package io.github.lucfr1746.llibrary;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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
        CommandAPI.onEnable();
        // Plugin startup logic
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
