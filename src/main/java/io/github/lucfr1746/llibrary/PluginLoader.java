package io.github.lucfr1746.llibrary;

import dev.jorel.commandapi.*;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.lucfr1746.llibrary.inventory.InventoryManager;
import io.github.lucfr1746.llibrary.util.helper.Logger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

class PluginLoader {

    private final LLibrary plugin;

    private final Hooks hooks;

    private final InventoryManager inventoryManager;

    private final Logger logger;
    private BukkitAudiences audiences;
    private Economy economy;
    private Permission permission;

    public PluginLoader(LLibrary plugin) {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(plugin).silentLogs(true));
        this.plugin = plugin;
        this.logger = new Logger(this.plugin);

        this.hooks = new Hooks();
        this.hooks.hooking();

        setupEconomy();
        setupPermission();
        registerCommands();

        this.inventoryManager = new InventoryManager();
    }

    public void enable() {
        CommandAPI.onEnable();
        this.audiences = BukkitAudiences.create(this.plugin);
        this.inventoryManager.load();
    }

    public void disable() {
        this.inventoryManager.disable();
    }

    public Logger getLogger() {
        return this.logger;
    }

    public BukkitAudiences getAudiences() {
        return this.audiences;
    }

    public Hooks getHooks() {
        return this.hooks;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public Permission getPermission() {
        return this.permission;
    }

    private void setupEconomy() {
        if (!this.hooks.isVault) {
            this.logger.warning("Unable to register Vault's Economy API, some features may not be available!");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) this.economy = rsp.getProvider();
        else this.logger.warning("Unable to register Economy API, some features may not be available!");
    }

    private void setupPermission() {
        if (!this.hooks.isVault) {
            this.logger.warning("Unable to register Vault's Permission API, some features may not be available!");
            return;
        }
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) this.permission = rsp.getProvider();
        else this.logger.warning("Unable to register Permission API, some features may not be available!");
    }

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
                    boolean isPlayer = sender instanceof Player;
                    LLibrary.getPluginLogger().info("Reloading LLibrary...");
                    if (isPlayer) sender.sendMessage("Reloading LLibrary...");

                    Bukkit.getScheduler().runTask(this.plugin, () -> {
                        try {
                            disable();
                            enable();
                            LLibrary.getPluginLogger().success("Successfully reloaded LLibrary!");
                            if (isPlayer) sender.sendMessage(ChatColor.GREEN + "Successfully reloaded LLibrary!");
                        } catch (Exception e) {
                            LLibrary.getPluginLogger().error("There was an error while reloading LLibrary!");
                            LLibrary.getPluginLogger().error(e.getMessage());
                            if (isPlayer) sender.sendMessage(ChatColor.RED + "There was an error while reloading LLibrary!");
                        }
                    });
                })
                .register();
    }

    static class Hooks {

        private boolean hasPaper = false;

        private boolean isVault = false;
        private boolean isPAPI = false;

        public void hooking() {
            this.hasPaper = initPaper();

            this.isVault = Bukkit.getPluginManager().isPluginEnabled("Vault");
            this.isPAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        }

        public boolean hasPaperAPI() {
            return this.hasPaper;
        }

        public boolean isVaultEnabled() {
            return this.isVault;
        }

        public boolean isPAPIEnabled() {
            return this.isPAPI;
        }

        private boolean initPaper() {
            try {
                Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData");
                return true;
            } catch (NoClassDefFoundError | ClassNotFoundException ex) {
                return false;
            }
        }
    }
}
