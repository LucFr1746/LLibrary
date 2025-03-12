package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@code TropicalFishBucketBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link TropicalFishBucketMeta} of an {@link ItemStack}.
 * This includes setting the body color, pattern, and pattern color of the tropical fish.
 */
public class TropicalFishBucketBuilder extends ItemBuilder {

    private final TropicalFishBucketMeta fishMeta;

    /**
     * Constructs a {@code TropicalFishBucketBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack The item stack to modify.
     * @throws IllegalArgumentException If the item is not a Tropical Fish Bucket.
     */
    public TropicalFishBucketBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof TropicalFishBucketMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of TropicalFishBucketMeta");
        }
        this.fishMeta = meta;
    }

    /**
     * Constructs a {@code TropicalFishBucketBuilder} with a new Tropical Fish Bucket item.
     *
     * @param amount The quantity of buckets in the stack.
     */
    public TropicalFishBucketBuilder(int amount) {
        super(Material.TROPICAL_FISH_BUCKET, amount);
        if (!(getItemMeta() instanceof TropicalFishBucketMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of TropicalFishBucketMeta");
        }
        this.fishMeta = meta;
    }

    /**
     * Sets the body color of the tropical fish in the bucket.
     *
     * @param color The body color of the fish.
     * @return This {@code TropicalFishBucketBuilder} instance for chaining.
     */
    public TropicalFishBucketBuilder setBodyColor(@NotNull DyeColor color) {
        fishMeta.setBodyColor(color);
        return this;
    }

    /**
     * Gets the body color of the tropical fish in the bucket.
     *
     * @return The body color of the fish, or null if not set.
     */
    @Nullable
    public DyeColor getBodyColor() {
        return fishMeta.getBodyColor();
    }

    /**
     * Sets the pattern of the tropical fish in the bucket.
     *
     * @param pattern The fish pattern.
     * @return This {@code TropicalFishBucketBuilder} instance for chaining.
     */
    public TropicalFishBucketBuilder setPattern(@NotNull TropicalFish.Pattern pattern) {
        fishMeta.setPattern(pattern);
        return this;
    }

    /**
     * Gets the pattern of the tropical fish in the bucket.
     *
     * @return The fish pattern, or null if not set.
     */
    @Nullable
    public TropicalFish.Pattern getPattern() {
        return fishMeta.getPattern();
    }

    /**
     * Sets the pattern color of the tropical fish in the bucket.
     *
     * @param color The pattern color of the fish.
     * @return This {@code TropicalFishBucketBuilder} instance for chaining.
     */
    public TropicalFishBucketBuilder setPatternColor(@NotNull DyeColor color) {
        fishMeta.setPatternColor(color);
        return this;
    }

    /**
     * Gets the pattern color of the tropical fish in the bucket.
     *
     * @return The pattern color of the fish, or null if not set.
     */
    @Nullable
    public DyeColor getPatternColor() {
        return fishMeta.getPatternColor();
    }

    /**
     * Checks if the bucket contains a specific tropical fish variant.
     *
     * @return True if a variant is set, false otherwise.
     */
    public boolean hasVariant() {
        return fishMeta.hasVariant();
    }
}