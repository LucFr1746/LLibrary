package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The {@code SuspiciousStewBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link SuspiciousStewMeta} of an {@link ItemStack}.
 * This includes adding, removing, and retrieving custom potion effects.
 */
public class SuspiciousStewBuilder extends ItemBuilder {

    private final SuspiciousStewMeta stewMeta;

    /**
     * Constructs a {@code SuspiciousStewBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack The item stack to modify.
     * @throws IllegalArgumentException If the item is not a Suspicious Stew.
     */
    public SuspiciousStewBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof SuspiciousStewMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of SuspiciousStewMeta");
        }
        this.stewMeta = meta;
    }

    /**
     * Constructs a {@code SuspiciousStewBuilder} with a new Suspicious Stew item.
     *
     * @param amount The quantity of stews in the stack.
     */
    public SuspiciousStewBuilder(int amount) {
        super(Material.SUSPICIOUS_STEW, amount);
        if (!(getItemMeta() instanceof SuspiciousStewMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of SuspiciousStewMeta");
        }
        this.stewMeta = meta;
    }

    /**
     * Adds a custom potion effect to the suspicious stew.
     *
     * @param effect    The potion effect to add.
     * @param overwrite Whether to overwrite an existing effect of the same type.
     * @return This {@code SuspiciousStewBuilder} instance for chaining.
     */
    public SuspiciousStewBuilder addEffect(@NotNull PotionEffect effect, boolean overwrite) {
        stewMeta.addCustomEffect(effect, overwrite);
        return this;
    }

    /**
     * Removes a custom potion effect from the suspicious stew.
     *
     * @param type The type of potion effect to remove.
     * @return This {@code SuspiciousStewBuilder} instance for chaining.
     */
    public SuspiciousStewBuilder removeEffect(@NotNull PotionEffectType type) {
        stewMeta.removeCustomEffect(type);
        return this;
    }

    /**
     * Removes all custom potion effects from the suspicious stew.
     *
     * @return This {@code SuspiciousStewBuilder} instance for chaining.
     */
    public SuspiciousStewBuilder clearEffects() {
        stewMeta.clearCustomEffects();
        return this;
    }

    /**
     * Checks if the stew has any custom potion effects.
     *
     * @return True if there are custom effects, false otherwise.
     */
    public boolean hasEffects() {
        return stewMeta.hasCustomEffects();
    }

    /**
     * Checks if the stew has a specific potion effect.
     *
     * @param type The type of potion effect to check for.
     * @return True if the effect is present, false otherwise.
     */
    public boolean hasEffect(@NotNull PotionEffectType type) {
        return stewMeta.hasCustomEffect(type);
    }

    /**
     * Gets an immutable list of all custom potion effects on the suspicious stew.
     *
     * @return A list of {@link PotionEffect} instances.
     */
    @NotNull
    public List<PotionEffect> getEffects() {
        return stewMeta.getCustomEffects();
    }
}