package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A utility class for building and manipulating banner {@link ItemStack}s in Minecraft.
 * This class provides a fluent API to modify banner patterns and metadata.
 *
 * <p>The {@code BannerBuilder} ensures that the provided {@link ItemStack} is a valid banner and
 * clones it to maintain immutability of the original {@link ItemStack}. This allows users to
 * safely manipulate banners using this class.</p>
 *
 * @param banner the {@link ItemStack} representing the banner that will have its patterns modified.
 *               Must not be {@code null} and should be a valid banner item with {@link BannerMeta}.
 */
public record BannerBuilder(@NotNull ItemStack banner) {

    /**
     * Constructs a new {@code BannerBuilder} from the specified banner {@link ItemStack}.
     * <p>
     * The provided {@code ItemStack} must be a valid banner with {@link BannerMeta}. The original item
     * is cloned to maintain immutability.
     * </p>
     *
     * @param banner the banner {@link ItemStack}. Must not be {@code null} and must have {@link BannerMeta}.
     * @throws NullPointerException     if {@code banner} is {@code null}.
     * @throws IllegalArgumentException if {@code banner} is {@link Material#AIR}.
     * @throws IllegalStateException    if {@code banner} does not have {@link BannerMeta}.
     */
    public BannerBuilder(ItemStack banner) {
        if (banner.getType() == Material.AIR) {
            throw new IllegalArgumentException("The ItemStack cannot be AIR!");
        }
        if (!(banner.getItemMeta() instanceof BannerMeta)) {
            throw new IllegalStateException("The ItemStack must have BannerMeta!");
        }
        this.banner = banner.clone();
    }

    /**
     * Adds a pattern to the banner.
     *
     * @param pattern the {@link Pattern} to add. Must not be {@code null}.
     * @return the updated {@code BannerBuilder} instance.
     * @throws NullPointerException if {@code pattern} is {@code null}.
     */
    public BannerBuilder addPattern(@NotNull Pattern pattern) {
        BannerMeta bannerMeta = getBannerMeta();
        bannerMeta.addPattern(pattern);
        this.banner.setItemMeta(bannerMeta);
        return this;
    }

    /**
     * Adds a pattern to the banner using the specified {@link DyeColor} and {@link PatternType}.
     *
     * @param dyeColor    the color of the pattern. Must not be {@code null}.
     * @param patternType the type of the pattern. Must not be {@code null}.
     * @return the updated {@code BannerBuilder} instance.
     * @throws NullPointerException if {@code dyeColor} or {@code patternType} is {@code null}.
     */
    public BannerBuilder addPattern(@NotNull DyeColor dyeColor, @NotNull PatternType patternType) {
        return addPattern(new Pattern(dyeColor, patternType));
    }

    /**
     * Builds and returns the modified {@link ItemStack}.
     *
     * @return the modified {@link ItemStack}.
     */
    public ItemStack build() {
        return this.banner.clone();
    }

    /**
     * Changes the color of an existing pattern at the specified index.
     *
     * @param index    the index of the pattern to change. Must be valid.
     * @param dyeColor the new color. Must not be {@code null}.
     * @return the updated {@code BannerBuilder} instance.
     * @throws NullPointerException      if {@code dyeColor} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     */
    public BannerBuilder changePatternColor(int index, @NotNull DyeColor dyeColor) {
        validatePatternIndex(index);
        PatternType patternType = getBannerMeta().getPattern(index).getPattern();
        return setPattern(index, new Pattern(dyeColor, patternType));
    }

    /**
     * Changes the type of existing pattern at the specified index.
     *
     * @param index       the index of the pattern to change. Must be valid.
     * @param patternType the new pattern type. Must not be {@code null}.
     * @return the updated {@code BannerBuilder} instance.
     * @throws NullPointerException      if {@code patternType} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     */
    public BannerBuilder changePatternType(int index, @NotNull PatternType patternType) {
        validatePatternIndex(index);
        DyeColor dyeColor = getBannerMeta().getPattern(index).getColor();
        return setPattern(index, new Pattern(dyeColor, patternType));
    }

    /**
     * Removes all patterns from the banner.
     *
     * @return the updated {@code BannerBuilder} instance.
     */
    public BannerBuilder clearPatterns() {
        return setPatterns(Collections.emptyList());
    }

    /**
     * Retrieves a pattern by index.
     *
     * @param index the index of the pattern.
     * @return an {@link Optional} containing the pattern if it exists, otherwise an empty {@code Optional}.
     */
    public Optional<Pattern> getPattern(int index) {
        BannerMeta bannerMeta = getBannerMeta();
        if (index < 0 || index >= bannerMeta.getPatterns().size()) {
            return Optional.empty();
        }
        return Optional.of(bannerMeta.getPattern(index));
    }

    /**
     * Retrieves all patterns on the banner.
     *
     * @return a list of {@link Pattern}s.
     */
    public List<Pattern> getPatterns() {
        return getBannerMeta().getPatterns();
    }

    /**
     * Removes a pattern at the specified index.
     *
     * @param index the index of the pattern to remove. Must be valid.
     * @return the updated {@code BannerBuilder} instance.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     */
    public BannerBuilder removePattern(int index) {
        validatePatternIndex(index);
        BannerMeta bannerMeta = getBannerMeta();
        bannerMeta.removePattern(index);
        this.banner.setItemMeta(bannerMeta);
        return this;
    }

    /**
     * Remove multiple patterns at the specified index.
     *
     * @param ids the index of the pattern to remove. Must be valid.
     * @return the updated {@code BannerBuilder} instance.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     */
    public BannerBuilder removePatterns(int... ids) {
        for (int id : ids) {
            removePattern(id);
        }
        return this;
    }

    /**
     * Sets a pattern at the specified index.
     *
     * @param index   the index of the pattern to set. Must be valid.
     * @param pattern the {@link Pattern} to set. Must not be {@code null}.
     * @return the updated {@code BannerBuilder} instance.
     * @throws NullPointerException      if {@code pattern} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     */
    public BannerBuilder setPattern(int index, @NotNull Pattern pattern) {
        validatePatternIndex(index);
        BannerMeta bannerMeta = getBannerMeta();
        bannerMeta.setPattern(index, pattern);
        this.banner.setItemMeta(bannerMeta);
        return this;
    }

    /**
     * Sets a pattern at the specified index using the given {@link DyeColor} and {@link PatternType}.
     *
     * @param index       the index of the pattern to set. Must be valid.
     * @param dyeColor    the color of the pattern. Must not be {@code null}.
     * @param patternType the type of the pattern. Must not be {@code null}.
     * @return the updated {@code BannerBuilder} instance.
     * @throws NullPointerException      if {@code dyeColor} or {@code patternType} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     */
    public BannerBuilder setPattern(int index, @NotNull DyeColor dyeColor, @NotNull PatternType patternType) {
        return setPattern(index, new Pattern(dyeColor, patternType));
    }

    /**
     * Replaces all patterns on the banner.
     *
     * @param patterns the list of {@link Pattern}s to set. Must not be {@code null}.
     * @return the updated {@code BannerBuilder} instance.
     * @throws NullPointerException if {@code patterns} is {@code null}.
     */
    public BannerBuilder setPatterns(@NotNull List<Pattern> patterns) {
        BannerMeta bannerMeta = getBannerMeta();
        bannerMeta.setPatterns(patterns);
        this.banner.setItemMeta(bannerMeta);
        return this;
    }

    /**
     * Retrieves the number of patterns on the banner.
     *
     * @return the number of patterns.
     */
    public int getNumberOfPatterns() {
        return getBannerMeta().numberOfPatterns();
    }

    /**
     * Converts the {@code BannerBuilder} to an {@link ItemBuilder}.
     *
     * @return an {@link ItemBuilder} initialized with the current banner.
     */
    public ItemBuilder toItemBuilder() {
        return new ItemBuilder(this.banner);
    }

    /**
     * Retrieves the {@link BannerMeta} from the banner.
     *
     * @return the {@link BannerMeta}.
     * @throws IllegalStateException if the {@link ItemStack} does not have {@link BannerMeta}.
     */
    private @NotNull BannerMeta getBannerMeta() {
        if (!(this.banner.getItemMeta() instanceof BannerMeta bannerMeta)) {
            throw new IllegalStateException("Expected BannerMeta but found: " + this.banner.getItemMeta());
        }
        return bannerMeta;
    }

    /**
     * Validates a pattern index.
     *
     * @param index the index to validate.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    private void validatePatternIndex(int index) {
        int size = getBannerMeta().getPatterns().size();
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for patterns size " + size);
        }
    }
}
