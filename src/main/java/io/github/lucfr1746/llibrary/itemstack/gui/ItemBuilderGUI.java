package io.github.lucfr1746.llibrary.itemstack.gui;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.inventory.loader.InventoryLoader;
import io.github.lucfr1746.llibrary.utils.APIs.FileAPI;

public class ItemBuilderGUI {

    private final LLibrary plugin;

    public ItemBuilderGUI(LLibrary plugin) {
        this.plugin = plugin;

        new InventoryLoader(new FileAPI(this.plugin).getOrCreateDefaultYAMLConfiguration("item-builder-gui.yml", "itembuilder"));
    }
}
