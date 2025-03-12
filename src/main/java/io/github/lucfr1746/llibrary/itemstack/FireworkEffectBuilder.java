package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code FireworkEffectBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link FireworkEffectMeta} of an {@link ItemStack}.
 * This class allows setting firework effects, including colors, fade colors, flicker, and trail.
 */
public class FireworkEffectBuilder extends ItemBuilder {

    private final FireworkEffectMeta fireworkEffectMeta;

    /**
     * Constructs a {@code FireworkEffectBuilder} with a new {@link Material#FIREWORK_STAR}.
     */
    public FireworkEffectBuilder() {
        super(Material.FIREWORK_STAR);
        if (!(getItemMeta() instanceof FireworkEffectMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of FireworkEffectMeta");
        }
        this.fireworkEffectMeta = meta;
    }

    /**
     * Constructs a {@code FireworkEffectBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link FireworkEffectMeta}
     */
    public FireworkEffectBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof FireworkEffectMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of FireworkEffectMeta");
        }
        this.fireworkEffectMeta = meta;
    }

    /**
     * Sets the firework effect.
     *
     * @param effect the firework effect
     * @return this {@code FireworkEffectBuilder} instance for method chaining
     * @throws IllegalArgumentException if the effect is null
     */
    public FireworkEffectBuilder setEffect(@NotNull FireworkEffect effect) {
        if (effect == null) {
            throw new IllegalArgumentException("FireworkEffect cannot be null");
        }
        fireworkEffectMeta.setEffect(effect);
        return this;
    }

    /**
     * Gets the firework effect.
     *
     * @return the firework effect, or null if not set
     */
    public FireworkEffect getEffect() {
        return fireworkEffectMeta.getEffect();
    }

    /**
     * Checks if the firework effect exists.
     *
     * @return true if there is a firework effect, false otherwise
     */
    public boolean hasEffect() {
        return fireworkEffectMeta.hasEffect();
    }
}