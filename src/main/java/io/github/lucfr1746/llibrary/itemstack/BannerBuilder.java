package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * The {@code BannerBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link BannerMeta} of an {@link ItemStack}.
 * This class allows setting, adding, and removing patterns for custom banners.
 */
public class BannerBuilder extends ItemBuilder {

    private final BannerMeta bannerMeta;

    /**
     * Constructs a {@code BannerBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link BannerMeta}
     */
    public BannerBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof BannerMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of BannerMeta");
        }
        this.bannerMeta = meta;
    }

    /**
     * Constructs a {@code BannerBuilder} with a default banner.
     */
    public BannerBuilder() {
        super(Material.WHITE_BANNER);
        if (!(getItemMeta() instanceof BannerMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of BannerMeta");
        }
        this.bannerMeta = meta;
    }

    /**
     * Adds a new pattern to the banner.
     *
     * @param color the pattern color
     * @param pattern the pattern type
     * @return this {@code BannerBuilder} instance for method chaining
     */
    public BannerBuilder addPattern(@NotNull DyeColor color, @NotNull PatternType pattern) {
        bannerMeta.addPattern(new Pattern(color, pattern));
        return this;
    }

    /**
     * Adds a new pattern to the banner.
     *
     * @param pattern the pattern to add
     * @return this {@code BannerBuilder} instance for method chaining
     */
    public BannerBuilder addPattern(@NotNull Pattern pattern) {
        bannerMeta.addPattern(pattern);
        return this;
    }

    /**
     * Sets the pattern at the specified index.
     *
     * @param index the index to set the pattern at
     * @param pattern the pattern to set
     * @return this {@code BannerBuilder} instance for method chaining
     */
    public BannerBuilder setPattern(int index, @NotNull Pattern pattern) {
        bannerMeta.setPattern(index, pattern);
        return this;
    }

    /**
     * Retrieves the pattern at the specified index.
     *
     * @param index the index of the pattern
     * @return the pattern at the specified index
     */
    public Pattern getPattern(int index) {
        return bannerMeta.getPattern(index);
    }

    /**
     * Returns a list of all patterns applied to the banner.
     *
     * @return a list of patterns on the banner
     */
    public List<Pattern> getPatterns() {
        return bannerMeta.getPatterns();
    }

    /**
     * Returns the number of patterns applied to the banner.
     *
     * @return the number of patterns
     */
    public int numberOfPatterns() {
        return bannerMeta.numberOfPatterns();
    }

    /**
     * Removes the pattern at the specified index.
     *
     * @param index the index of the pattern to remove
     * @return this {@code BannerBuilder} instance for method chaining
     */
    public BannerBuilder removePattern(int index) {
        bannerMeta.removePattern(index);
        return this;
    }

    /**
     * Sets the patterns used on the banner.
     *
     * @param patterns the list of patterns to set
     * @return this {@code BannerBuilder} instance for method chaining
     */
    public BannerBuilder setPatterns(@NotNull List<Pattern> patterns) {
        bannerMeta.setPatterns(patterns);
        return this;
    }
}