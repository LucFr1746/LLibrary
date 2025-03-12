package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@code AxolotlBucketBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link AxolotlBucketMeta} of an {@link ItemStack}.
 * This class allows setting and retrieving axolotl variants for axolotl buckets.
 */
public class AxolotlBucketBuilder extends ItemBuilder {

    private final AxolotlBucketMeta axolotlMeta;

    /**
     * Constructs an {@code AxolotlBucketBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link AxolotlBucketMeta}
     */
    public AxolotlBucketBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof AxolotlBucketMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of AxolotlBucketMeta");
        }
        this.axolotlMeta = meta;
    }

    /**
     * Constructs an {@code AxolotlBucketBuilder} with a default AXOLOTL_BUCKET.
     */
    public AxolotlBucketBuilder() {
        super(Material.AXOLOTL_BUCKET);
        if (!(getItemMeta() instanceof AxolotlBucketMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of AxolotlBucketMeta");
        }
        this.axolotlMeta = meta;
    }

    /**
     * Retrieves the {@link Axolotl.Variant} of the axolotl in the bucket.
     *
     * @return the axolotl variant, or {@code null} if none is set
     */
    @Nullable
    public Axolotl.Variant getVariant() {
        return axolotlMeta.getVariant();
    }

    /**
     * Checks whether the axolotl bucket has a variant set.
     *
     * @return {@code true} if a variant is set, {@code false} otherwise
     */
    public boolean hasVariant() {
        return axolotlMeta.hasVariant();
    }

    /**
     * Sets the {@link Axolotl.Variant} for the axolotl in the bucket.
     *
     * @param variant the axolotl variant to set
     * @return this {@code AxolotlBucketBuilder} instance for method chaining
     */
    public AxolotlBucketBuilder setVariant(@NotNull Axolotl.Variant variant) {
        axolotlMeta.setVariant(variant);
        return this;
    }
}