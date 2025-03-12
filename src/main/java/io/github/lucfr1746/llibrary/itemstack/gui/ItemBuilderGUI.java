package io.github.lucfr1746.llibrary.itemstack.gui;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import io.github.lucfr1746.llibrary.util.helper.FileAPI;

public class ItemBuilderGUI extends InventoryBuilder {

    public ItemBuilderGUI() {
        FileAPI fileAPI = new FileAPI(LLibrary.getInstance(), true);
        LLibrary.getInventoryManager().registerInventoryBuilder(fileAPI.getOrCreateDefaultYAMLConfiguration("item-builder-gui.yml", "itembuilder"));
    }
}