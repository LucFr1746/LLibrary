package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.OminousBottleMeta;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code OminousBottleBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link OminousBottleMeta} of an {@link ItemStack}.
 * This class allows setting and checking the amplifier for an Ominous Bottle's bad omen effect.
 */
public class OminousBottleBuilder extends ItemBuilder {

    private final OminousBottleMeta ominousBottleMeta;

    /**
     * Constructs an {@code OminousBottleBuilder} with a new {@link Material#OMINOUS_BOTTLE}.
     */
    public OminousBottleBuilder() {
        super(Material.OMINOUS_BOTTLE);
        if (!(getItemMeta() instanceof OminousBottleMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of OminousBottleMeta");
        }
        this.ominousBottleMeta = meta;
    }

    /**
     * Constructs an {@code OminousBottleBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link OminousBottleMeta}
     */
    public OminousBottleBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof OminousBottleMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of OminousBottleMeta");
        }
        this.ominousBottleMeta = meta;
    }

    /**
     * Sets the amplifier level for the Ominous Bottle's bad omen effect.
     *
     * @param amplifier the amplifier level to set (must be >= 0)
     * @return this {@code OminousBottleBuilder} instance for chaining
     * @throws IllegalArgumentException if amplifier is negative
     */
    public OminousBottleBuilder setAmplifier(int amplifier) {
        if (amplifier < 0) {
            throw new IllegalArgumentException("Amplifier level must be >= 0");
        }
        ominousBottleMeta.setAmplifier(amplifier);
        return this;
    }

    /**
     * Gets the amplifier level of the Ominous Bottle's bad omen effect.
     *
     * @return the amplifier level
     */
    public int getAmplifier() {
        return ominousBottleMeta.getAmplifier();
    }

    /**
     * Checks if the Ominous Bottle has an amplifier set.
     *
     * @return true if an amplifier is set, false otherwise
     */
    public boolean hasAmplifier() {
        return ominousBottleMeta.hasAmplifier();
    }
}