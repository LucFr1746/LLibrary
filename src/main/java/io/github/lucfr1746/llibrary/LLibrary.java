package io.github.lucfr1746.llibrary;

import dev.jorel.commandapi.*;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.lucfr1746.llibrary.metrics.bStats;
import io.github.lucfr1746.llibrary.utils.Hooks;
import io.github.lucfr1746.llibrary.utils.APIs.LoggerAPI;
import io.github.lucfr1746.llibrary.utils.PluginLoader;
import io.github.lucfr1746.llibrary.utils.Util;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * The main class for the {@link LLibrary} plugin.
 * This class extends {@link JavaPlugin} and handles the plugin's loading, enabling, and disabling.
 * It also integrates the {@link CommandAPI} for command handling.
 */
public final class LLibrary extends JavaPlugin {

    private static LoggerAPI logger;

    private BukkitAudiences audiences;

    private static Economy econ = null;
    private static Permission perms = null;

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
        logger = new LoggerAPI(this);
        this.audiences = BukkitAudiences.create(this);
        CommandAPI.onEnable();
        registerCommands();
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
                setupEconomy();
                setupPermissions();
                new LoggerAPI(this).success("Hooking into Vault");
            } catch (Exception e) {
                new LoggerAPI(this).error(e.getMessage());
            }
        }
        new PluginLoader(this).loadPlugin();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        if (this.audiences != null) {
            this.audiences.close();
            this.audiences = null;
        }
    }

    private void registerCommands() {
        List<Argument<?>> arguments = new ArrayList<>();
        arguments.add(new StringArgument("reload")
                .replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(info ->
                        new IStringTooltip[] {
                                StringTooltip.ofString("reload", "Reload the plugin"),
                        }
                ))
        );

        new CommandAPICommand("llibrary")
                .withAliases("llib")
                .withArguments(arguments)
                .executes((sender, args) -> {
                    sender.sendMessage("Reloading LLibrary...");
                    new PluginLoader(this).disablePlugin().loadPlugin();
                    sender.sendMessage(ChatColor.GREEN + "Successfully reloaded LLibrary!");
                })
                .register();
    }

    public BukkitAudiences audiences() {
        if (this.audiences == null) {
            throw new IllegalStateException("Something wrong with the Adventure API!");
        }
        return this.audiences;
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return;
        }
        perms = rsp.getProvider();
    }

    public static Economy getEconomy() {
        if (econ == null) {
            throw new IllegalStateException("Vault not hooked!");
        }
        return econ;
    }

    public static Permission getPermissions() {
        if (perms == null) {
            throw new IllegalStateException("Vault not hooked!");
        }
        return perms;
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

    public static LoggerAPI getLoggerAPI() {
        return logger;
    }
}
