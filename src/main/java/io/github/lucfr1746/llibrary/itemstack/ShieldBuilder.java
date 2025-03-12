package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ShieldMeta;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code ShieldBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link ShieldMeta} of an {@link ItemStack}.
 * This class allows setting the base color of shields.
 */
public class ShieldBuilder extends ItemBuilder {

    private final ShieldMeta shieldMeta;

    /**
     * Constructs a {@code ShieldBuilder} with a new {@link Material#SHIELD}.
     */
    public ShieldBuilder() {
        super(Material.SHIELD);
        if (!(getItemMeta() instanceof ShieldMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of ShieldMeta");
        }
        this.shieldMeta = meta;
    }

    /**
     * Constructs a {@code ShieldBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link ShieldMeta}
     */
    public ShieldBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof ShieldMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of ShieldMeta");
        }
        this.shieldMeta = meta;
    }

    /**
     * Sets the base color of the shield.
     *
     * @param color the base color to set
     * @return this {@code ShieldBuilder} instance for chaining
     */
    public ShieldBuilder setBaseColor(@NotNull DyeColor color) {
        shieldMeta.setBaseColor(color);
        return this;
    }

    /**
     * Gets the base color of the shield.
     *
     * @return the base color, or null if not set
     */
    public DyeColor getBaseColor() {
        return shieldMeta.getBaseColor();
    }

    /**
     * Checks if the shield has a base color set.
     *
     * @return true if a base color is set, false otherwise
     */
    public boolean hasBaseColor() {
        return shieldMeta.getBaseColor() != null;
    }
}