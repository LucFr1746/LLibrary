package io.github.lucfr1746.llibrary.utils;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.itemstack.gui.ItemBuilderGUI;

public class PluginLoader {

    private final LLibrary plugin;

    public PluginLoader(LLibrary plugin) {
        this.plugin = plugin;

        LLibrary.getLoggerAPI().info("Loading ItemBuilder GUI...");
        new ItemBuilderGUI(this.plugin);
    }
}
