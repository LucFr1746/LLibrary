package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code CompassBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link CompassMeta} of an {@link ItemStack}.
 * This class allows setting and retrieving lodestone locations and tracking status.
 */
public class CompassBuilder extends ItemBuilder {

    private final CompassMeta compassMeta;

    /**
     * Constructs a {@code CompassBuilder} with a new compass item.
     */
    public CompassBuilder() {
        super(Material.COMPASS);
        if (!(getItemMeta() instanceof CompassMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of CompassMeta");
        }
        this.compassMeta = meta;
    }

    /**
     * Constructs a {@code CompassBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link CompassMeta}
     */
    public CompassBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof CompassMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of CompassMeta");
        }
        this.compassMeta = meta;
    }

    /**
     * Sets the lodestone location for the compass.
     *
     * @param lodestone the lodestone location to set
     * @return this {@code CompassBuilder} instance for method chaining
     */
    public CompassBuilder setLodestone(@NotNull Location lodestone) {
        compassMeta.setLodestone(lodestone);
        return this;
    }

    /**
     * Gets the lodestone location of the compass.
     *
     * @return the lodestone location, or null if none is set
     */
    public Location getLodestone() {
        return compassMeta.getLodestone();
    }

    /**
     * Checks if the compass has a lodestone location set.
     *
     * @return true if a lodestone is set, false otherwise
     */
    public boolean hasLodestone() {
        return compassMeta.hasLodestone();
    }

    /**
     * Sets whether the compass is tracking a lodestone.
     *
     * @param tracked true if the compass should track a lodestone, false otherwise
     * @return this {@code CompassBuilder} instance for method chaining
     */
    public CompassBuilder setLodestoneTracked(boolean tracked) {
        compassMeta.setLodestoneTracked(tracked);
        return this;
    }

    /**
     * Checks if the compass is tracking a lodestone.
     *
     * @return true if the compass is tracking a lodestone, false otherwise
     */
    public boolean isLodestoneTracked() {
        return compassMeta.isLodestoneTracked();
    }
}