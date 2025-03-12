package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The {@code PotionBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link PotionMeta} of an {@link ItemStack}.
 * This class allows setting potion types, custom effects, and colors.
 */
public class PotionBuilder extends ItemBuilder {

    private final PotionMeta potionMeta;

    /**
     * Constructs a {@code PotionBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link PotionMeta}
     */
    public PotionBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof PotionMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of PotionMeta");
        }
        this.potionMeta = meta;
    }

    /**
     * Constructs a {@code PotionBuilder} with the specified {@link Material} and amount.
     *
     * @param material the material for the item stack
     * @param amount   the quantity of items in the stack
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link PotionMeta}
     */
    public PotionBuilder(@NotNull Material material, int amount) {
        super(material, amount);
        if (!(getItemMeta() instanceof PotionMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of PotionMeta");
        }
        this.potionMeta = meta;
    }

    /**
     * Sets the base potion type.
     *
     * @param type the base potion type to set
     * @return this {@code PotionBuilder} instance for chaining
     */
    public PotionBuilder setBasePotionType(@NotNull PotionType type) {
        potionMeta.setBasePotionType(type);
        return this;
    }

    /**
     * Gets the base potion type.
     *
     * @return the base potion type
     */
    public PotionType getBasePotionType() {
        return potionMeta.getBasePotionType();
    }

    /**
     * Adds a custom potion effect.
     *
     * @param effect    the custom potion effect to add
     * @param overwrite whether to overwrite existing effects of the same type
     * @return this {@code PotionBuilder} instance for chaining
     */
    public PotionBuilder addCustomEffect(@NotNull PotionEffect effect, boolean overwrite) {
        potionMeta.addCustomEffect(effect, overwrite);
        return this;
    }

    /**
     * Removes a custom potion effect by type.
     *
     * @param type the type of potion effect to remove
     * @return true if the effect was removed, false if it did not exist
     */
    public boolean removeCustomEffect(@NotNull PotionEffectType type) {
        return potionMeta.removeCustomEffect(type);
    }

    /**
     * Gets all custom potion effects.
     *
     * @return a list of custom potion effects
     */
    public List<PotionEffect> getCustomEffects() {
        return potionMeta.getCustomEffects();
    }

    /**
     * Checks if the potion has any custom effects.
     *
     * @return true if there are custom effects, false otherwise
     */
    public boolean hasCustomEffects() {
        return potionMeta.hasCustomEffects();
    }

    /**
     * Sets the potion color.
     *
     * @param color the color to set
     * @return this {@code PotionBuilder} instance for chaining
     */
    public PotionBuilder setColor(@NotNull Color color) {
        potionMeta.setColor(color);
        return this;
    }

    /**
     * Gets the potion color.
     *
     * @return the potion color, or null if not set
     */
    public Color getColor() {
        return potionMeta.getColor();
    }

    /**
     * Checks if the potion has a custom color.
     *
     * @return true if a color is set, false otherwise
     */
    public boolean hasColor() {
        return potionMeta.hasColor();
    }

    /**
     * Sets a custom name for the potion.
     *
     * @param name the custom name to set
     * @return this {@code PotionBuilder} instance for chaining
     */
    public PotionBuilder setCustomName(@NotNull String name) {
        potionMeta.setCustomName(name);
        return this;
    }

    /**
     * Gets the custom name of the potion.
     *
     * @return the custom name, or null if not set
     */
    public String getCustomName() {
        return potionMeta.getCustomName();
    }

    /**
     * Checks if the potion has a custom name.
     *
     * @return true if a custom name is set, false otherwise
     */
    public boolean hasCustomName() {
        return potionMeta.hasCustomName();
    }
}