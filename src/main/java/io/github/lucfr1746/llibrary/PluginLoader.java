package io.github.lucfr1746.llibrary;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.StringTooltip;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.lucfr1746.llibrary.inventory.loader.InventoryLoader;
import io.github.lucfr1746.llibrary.itemstack.gui.ItemBuilderGUI;
import io.github.lucfr1746.llibrary.utils.APIs.FileAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

class PluginLoader {

    private final LLibrary plugin;

    public PluginLoader(LLibrary plugin) {
        this.plugin = plugin;
    }

    public void enablePlugin() {
        registerReloadCommands();
        loadPlugin();
    }

    public void loadPlugin() {
        new InventoryManager(this.plugin).load();
    }

    public void disablePlugin() {
        new InventoryManager(this.plugin).disable();
    }

    private void registerReloadCommands() {
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
                    LLibrary.getLoggerAPI().info("Reloading LLibrary...");
                    if (isPlayer) sender.sendMessage("Reloading LLibrary...");

                    Bukkit.getScheduler().runTask(this.plugin, () -> {
                        try {
                            disablePlugin();
                            loadPlugin();
                            LLibrary.getLoggerAPI().success("Successfully reloaded LLibrary!");
                            if (isPlayer) sender.sendMessage(ChatColor.GREEN + "Successfully reloaded LLibrary!");
                        } catch (Exception e) {
                            LLibrary.getLoggerAPI().error("There was an error while reloading LLibrary!");
                            if (isPlayer) sender.sendMessage(ChatColor.RED + "There was an error while reloading LLibrary!");
                        }
                    });
                })
                .register();
    }

    static class InventoryManager {

        private final LLibrary plugin;

        private static final List<InventoryLoader> registeredInv = new ArrayList<>();

        public InventoryManager(LLibrary plugin) {
            this.plugin = plugin;
        }

        public InventoryManager load() {
            LLibrary.getLoggerAPI().info("Loading ItemBuilder GUI...");
            registerInv(new ItemBuilderGUI(new FileAPI(this.plugin).getOrCreateDefaultYAMLConfiguration("item-builder-gui.yml", "itembuilder")));
            return this;
        }

        public InventoryManager disable() {
            for (InventoryLoader loader : registeredInv) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getOpenInventory() instanceof InventoryLoader)
                        player.closeInventory();
                }
                unregisterInvCMD(loader);
            }
            return this;
        }

        public static void registerInv(InventoryLoader inventoryLoader) {
            registeredInv.add(inventoryLoader);
        }

        private void unregisterInvCMD(InventoryLoader inventoryLoader) {
            for (String cmd : inventoryLoader.getOpenCommands()) {
                CommandAPI.unregister(cmd);
            }
        }
    }
}
