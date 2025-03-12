package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code ArmorMetaBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link ArmorMeta} of an {@link ItemStack}.
 * This class allows setting and retrieving armor trims for supported armor items.
 */
public class ArmorBuilder extends ItemBuilder {

    private final ArmorMeta armorMeta;

    /**
     * Constructs an {@code ArmorMetaBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link ArmorMeta}
     */
    public ArmorBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof ArmorMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of ArmorMeta");
        }
        this.armorMeta = meta;
    }

    /**
     * Constructs an {@code ArmorMetaBuilder} with the specified {@link Material} and amount.
     *
     * @param material the material for the item stack
     * @param amount the quantity of items in the stack
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link ArmorMeta}
     */
    public ArmorBuilder(@NotNull Material material, int amount) {
        super(material, amount);
        if (!(getItemMeta() instanceof ArmorMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of ArmorMeta");
        }
        this.armorMeta = meta;
    }

    /**
     * Constructs an {@code ArmorMetaBuilder} with the specified {@link Material}.
     *
     * @param material the material for the item stack
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link ArmorMeta}
     */
    public ArmorBuilder(@NotNull Material material) {
        super(material);
        if (!(getItemMeta() instanceof ArmorMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of ArmorMeta");
        }
        this.armorMeta = meta;
    }

    /**
     * Retrieves the {@link ArmorTrim} applied to the armor item.
     *
     * @return the armor trim, or {@code null} if none is set
     */
    public ArmorTrim getTrim() {
        return armorMeta.getTrim();
    }

    /**
     * Checks whether the armor item has an {@link ArmorTrim} applied.
     *
     * @return {@code true} if the item has trim, {@code false} otherwise
     */
    public boolean hasTrim() {
        return armorMeta.hasTrim();
    }

    /**
     * Sets the {@link ArmorTrim} for the armor item.
     *
     * @param trim the armor trim to apply
     * @return this {@code ArmorMetaBuilder} instance for method chaining
     */
    public ArmorBuilder setTrim(ArmorTrim trim) {
        armorMeta.setTrim(trim);
        return this;
    }
}