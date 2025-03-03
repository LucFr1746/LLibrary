package io.github.lucfr1746.llibrary.utils;

import dev.jorel.commandapi.CommandAPI;
import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.inventory.loader.InventoryLoader;
import io.github.lucfr1746.llibrary.itemstack.gui.ItemBuilderGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PluginLoader {

    private final LLibrary plugin;

    private static final List<InventoryLoader> registeredInv = new ArrayList<>();

    public PluginLoader(LLibrary plugin) {
        this.plugin = plugin;
    }

    public PluginLoader loadPlugin() {
        LLibrary.getLoggerAPI().info("Loading ItemBuilder GUI...");
        new ItemBuilderGUI(this.plugin);
        return this;
    }

    public PluginLoader disablePlugin() {
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
