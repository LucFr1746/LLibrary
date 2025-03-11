package io.github.lucfr1746.llibrary.itemstack.gui;

import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import org.bukkit.configuration.file.FileConfiguration;

public class ItemBuilderGUI extends InventoryBuilder {

    public ItemBuilderGUI(FileConfiguration guiConfig) {
        this.loadFromFile(guiConfig);
    }
}