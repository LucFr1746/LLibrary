package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The {@code FireworkBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link FireworkMeta} of an {@link ItemStack}.
 * This class allows adding, removing, and customizing firework effects, as well as setting firework power.
 */
public class FireworkBuilder extends ItemBuilder {

    private final FireworkMeta fireworkMeta;

    /**
     * Constructs a {@code FireworkBuilder} with a new firework rocket.
     */
    public FireworkBuilder() {
        super(Material.FIREWORK_ROCKET);
        if (!(getItemMeta() instanceof FireworkMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of FireworkMeta");
        }
        this.fireworkMeta = meta;
    }

    /**
     * Constructs a {@code FireworkBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link FireworkMeta}
     */
    public FireworkBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof FireworkMeta meta)) {
            throw new IllegalArgumentException("ItemStack does not have FireworkMeta");
        }
        this.fireworkMeta = meta;
    }

    /**
     * Adds a single firework effect to the firework.
     *
     * @param effect the firework effect to add
     * @return this {@code FireworkBuilder} instance for method chaining
     */
    public FireworkBuilder addEffect(@NotNull FireworkEffect effect) {
        fireworkMeta.addEffect(effect);
        return this;
    }

    /**
     * Adds multiple firework effects to the firework.
     *
     * @param effects the firework effects to add
     * @return this {@code FireworkBuilder} instance for method chaining
     */
    public FireworkBuilder addEffects(@NotNull FireworkEffect... effects) {
        fireworkMeta.addEffects(effects);
        return this;
    }

    /**
     * Adds a collection of firework effects to the firework.
     *
     * @param effects the iterable collection of firework effects to add
     * @return this {@code FireworkBuilder} instance for method chaining
     */
    public FireworkBuilder addEffects(@NotNull Iterable<FireworkEffect> effects) {
        fireworkMeta.addEffects(effects);
        return this;
    }

    /**
     * Removes a firework effect at the specified index.
     *
     * @param index the index of the effect to remove
     * @return this {@code FireworkBuilder} instance for method chaining
     */
    public FireworkBuilder removeEffect(int index) {
        fireworkMeta.removeEffect(index);
        return this;
    }

    /**
     * Clears all firework effects from the firework.
     *
     * @return this {@code FireworkBuilder} instance for method chaining
     */
    public FireworkBuilder clearEffects() {
        fireworkMeta.clearEffects();
        return this;
    }

    /**
     * Gets the list of firework effects.
     *
     * @return a list of firework effects
     */
    public List<FireworkEffect> getEffects() {
        return fireworkMeta.getEffects();
    }

    /**
     * Gets the number of firework effects.
     *
     * @return the number of firework effects
     */
    public int getEffectsSize() {
        return fireworkMeta.getEffectsSize();
    }

    /**
     * Sets the power of the firework, determining its flight duration.
     *
     * @param power the firework's power (0-127)
     * @return this {@code FireworkBuilder} instance for method chaining
     * @throws IllegalArgumentException if power is out of range
     */
    public FireworkBuilder setPower(int power) {
        if (power < 0 || power > 127) {
            throw new IllegalArgumentException("Firework power must be between 0 and 127");
        }
        fireworkMeta.setPower(power);
        return this;
    }

    /**
     * Gets the current power of the firework.
     *
     * @return the power of the firework
     */
    public int getPower() {
        return fireworkMeta.getPower();
    }

    /**
     * Checks if the firework has any effects.
     *
     * @return true if the firework has effects, false otherwise
     */
    public boolean hasEffects() {
        return fireworkMeta.hasEffects();
    }

    /**
     * Checks if the firework has a set power value.
     *
     * @return true if the firework has a power set, false otherwise
     */
    public boolean hasPower() {
        return fireworkMeta.hasPower();
    }
}