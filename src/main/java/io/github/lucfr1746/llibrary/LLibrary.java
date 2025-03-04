package io.github.lucfr1746.llibrary;

import dev.jorel.commandapi.*;
import dev.jorel.commandapi.arguments.*;
import io.github.lucfr1746.llibrary.metrics.bStats;
import io.github.lucfr1746.llibrary.utils.*;
import io.github.lucfr1746.llibrary.utils.APIs.LoggerAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the LLibrary plugin.
 * Handles plugin lifecycle and integrates with CommandAPI, Vault, and other utilities.
 */
public final class LLibrary extends JavaPlugin {

    private static LoggerAPI logger;
    private BukkitAudiences audiences;
    private static Economy econ;
    private static Permission perms;

    /**
     * Initializes CommandAPI configuration on plugin load.
     */
    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
    }

    /**
     * Plugin enables logic: sets up integrations, loads commands, and starts bStats.
     */
    @Override
    public void onEnable() {
        logger = new LoggerAPI(this);
        audiences = BukkitAudiences.create(this);
        CommandAPI.onEnable();
        new bStats(this, 23768);
        registerCommands();
        checkIntegrations();
        new PluginLoader(this).loadPlugin();
    }

    /**
     * Properly disables CommandAPI and Adventure API on plugin disable.
     */
    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        if (audiences != null) {
            audiences.close();
        }
    }

    /**
     * Registers LLibrary commands using CommandAPI.
     */
    private void registerCommands() {
        new CommandAPICommand("llibrary")
                .withAliases("llib")
                .withArguments(new StringArgument("action")
                        .replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(info ->
                                new IStringTooltip[]{
                                        StringTooltip.ofString("reload", "Reload the plugin")
                                }
                        )))
                .executes((sender, args) -> {
                    sender.sendMessage("Reloading LLibrary...");
                    new PluginLoader(this).disablePlugin().loadPlugin();
                    sender.sendMessage(ChatColor.GREEN + "Successfully reloaded LLibrary!");
                })
                .register();
    }

    /**
     * Checks for and sets up integrations with Vault, PlaceholderAPI, and Paper.
     */
    private void checkIntegrations() {
        if (Util.hasPaperAPI()) {
            logger.success("PaperAPI detected, enabling Paper-specific features.");
        }
        if (Hooks.isPAPIEnabled()) {
            logger.success("Hooked into PlaceholderAPI.");
        }
        if (Hooks.isVault()) {
            setupEconomy();
            setupPermissions();
            logger.success("Hooked into Vault.");
        }
    }

    /**
     * Sets up Vault's economy service.
     */
    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            econ = rsp.getProvider();
        } else {
            logger.error("Failed to hook into Vault's economy.");
        }
    }

    /**
     * Sets up Vault's permission service.
     */
    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) {
            perms = rsp.getProvider();
        } else {
            logger.error("Failed to hook into Vault's permissions.");
        }
    }

    /**
     * Gets the plugin's Adventure API instance.
     * @return BukkitAudiences instance.
     */
    public BukkitAudiences audiences() {
        if (audiences == null) {
            throw new IllegalStateException("Adventure API not initialized.");
        }
        return audiences;
    }

    /**
     * Gets the hooked Vault economy service.
     * @return Economy instance.
     */
    public static Economy getEconomy() {
        if (econ == null) {
            throw new IllegalStateException("Vault economy not hooked.");
        }
        return econ;
    }

    /**
     * Gets the hooked Vault permission service.
     * @return Permission instance.
     */
    public static Permission getPermissions() {
        if (perms == null) {
            throw new IllegalStateException("Vault permissions not hooked.");
        }
        return perms;
    }

    /**
     * Gets the singleton instance of LLibrary.
     * @return LLibrary instance.
     */
    public static LLibrary getInstance() {
        return JavaPlugin.getPlugin(LLibrary.class);
    }

    /**
     * Gets the plugin's LoggerAPI instance.
     * @return LoggerAPI instance.
     */
    public static LoggerAPI getLoggerAPI() {
        return logger;
    }
}
