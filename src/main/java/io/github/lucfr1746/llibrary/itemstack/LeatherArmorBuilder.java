package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code LeatherArmorBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link LeatherArmorMeta} of an {@link ItemStack}.
 * This class allows setting leather armor colors.
 */
public class LeatherArmorBuilder extends ItemBuilder {

    private final LeatherArmorMeta leatherArmorMeta;

    /**
     * Constructs a {@code LeatherArmorBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link LeatherArmorMeta}
     */
    public LeatherArmorBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof LeatherArmorMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of LeatherArmorMeta");
        }
        this.leatherArmorMeta = meta;
    }

    /**
     * Constructs a {@code LeatherArmorBuilder} with the specified {@link Material} and amount.
     *
     * @param material the leather armor material
     * @param amount   the quantity of items in the stack
     * @throws IllegalArgumentException if the material is not a leather armor piece
     */
    public LeatherArmorBuilder(@NotNull Material material, int amount) {
        super(material, amount);
        if (!(getItemMeta() instanceof LeatherArmorMeta meta)) {
            throw new IllegalArgumentException("Material is not a leather armor piece");
        }
        this.leatherArmorMeta = meta;
    }

    /**
     * Sets the color of the leather armor.
     *
     * @param color the {@link Color} to set
     * @return this {@code LeatherArmorBuilder} instance for chaining
     */
    public LeatherArmorBuilder setColor(@NotNull Color color) {
        leatherArmorMeta.setColor(color);
        return this;
    }

    /**
     * Sets the color of the leather armor using a HEX color code.
     *
     * @param hex the HEX color code (e.g., "#FF5733")
     * @return this {@code LeatherArmorBuilder} instance for chaining
     * @throws IllegalArgumentException if the HEX code is invalid
     */
    public LeatherArmorBuilder setColor(@NotNull String hex) {
        if (!hex.matches("^#([A-Fa-f0-9]{6})$")) {
            throw new IllegalArgumentException("Invalid HEX color format. Use #RRGGBB.");
        }
        int rgb = Integer.parseInt(hex.substring(1), 16);
        Color color = Color.fromRGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        return setColor(color);
    }

    /**
     * Gets the color of the leather armor.
     *
     * @return the current {@link Color} of the armor
     */
    public Color getColor() {
        return leatherArmorMeta.getColor();
    }
}