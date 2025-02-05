package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for building and modifying {@link ItemStack} objects.
 * This class provides methods to easily create and customize {@link ItemStack} instances.
 */
public class ItemBuilder {

    private final @NotNull ItemStack itemStack;

    /**
     * Constructs an {@link ItemBuilder} with a predefined {@link ItemStack}.
     *
     * @param itemStack The {@link ItemStack} to wrap. Cannot be null.
     */
    public ItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Constructs an {@link ItemBuilder} with a {@link Material} and a specified amount.
     *
     * @param material The {@link Material} of the item. Cannot be null.
     * @param amount   The amount of the item. Must be between 1 and 127 (inclusive).
     * @throws IllegalArgumentException if the amount is invalid.
     */
    public ItemBuilder(@NotNull Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    /**
     * Constructs an {@link ItemBuilder} with a {@link Material} and a default amount of 1.
     *
     * @param material The {@link Material} of the item. Cannot be null.
     */
    public ItemBuilder(@NotNull Material material) {
        this(material, 1);
    }

    /**
     * Returns the {@link ItemStack} that this builder wraps.
     *
     * @return The {@link ItemStack} instance. Never null.
     */
    public @NotNull ItemStack build() {
        return this.itemStack;
    }

    /**
     * Sets the amount of the {@link ItemStack}.
     * If the amount is less than 1 or greater than 127, an exception is thrown.
     * If the amount is negative, the current amount is adjusted, but not below 0.
     *
     * @param amount The amount to set or adjust by.
     * @return The current {@link ItemBuilder} instance for method chaining.
     * @throws IllegalArgumentException if the amount exceeds bounds (1-127) or if the item is invalid.
     */
    public ItemBuilder setAmount(int amount) {
        if (isInvalidItemStack()) return null;
        if (amount < 0)
            this.itemStack.setAmount(Math.max(0, this.itemStack.getAmount() + amount));
        else if ((amount > 127) || (amount < 1))
            throw new IllegalArgumentException("Wrong amount number");
        else
            this.itemStack.setAmount(amount);
        return this;
    }

    /**
     * Gets the current amount of the {@link ItemStack}.
     *
     * @return The amount of the item, or -1 if the item is invalid.
     */
    public int getAmount() {
        if (isInvalidItemStack()) return -1;
        return this.itemStack.getAmount();
    }

    /**
     * Creates and returns an {@link ItemAttributeBuilder} for further customization of the {@link ItemStack}.
     *
     * @return An {@link ItemAttributeBuilder} instance for the item, or null if the item is invalid.
     */
    public ItemAttributeBuilder getItemAttributeBuilder() {
        if (isInvalidItemStack()) return null;
        return new ItemAttributeBuilder(this.itemStack);
    }

    /**
     * Retrieves the {@link Axolotl.Variant} from the {@link ItemStack} if it contains {@link AxolotlBucketMeta}.
     *
     * @return The {@link Axolotl.Variant} associated with the item.
     * @throws IllegalStateException if the {@link ItemStack}'s meta is not an instance of {@link AxolotlBucketMeta}.
     */
    public Axolotl.Variant getAxolotlVariant() {
        if (!(this.itemStack.getItemMeta() instanceof AxolotlBucketMeta axolotlBucketMeta)) {
            throw new IllegalStateException("The ItemStack does not have AxolotlBucketMeta!");
        }

        return axolotlBucketMeta.getVariant();
    }

    /**
     * Checks if the {@link ItemStack} with {@link AxolotlBucketMeta} contains an {@link Axolotl.Variant}.
     *
     * @return {@code true} if the {@link ItemStack} has an {@link Axolotl.Variant}, {@code false} otherwise.
     * @throws IllegalStateException if the {@link ItemStack}'s meta is not an instance of {@link AxolotlBucketMeta}.
     */
    public boolean hasAxolotlVariant() {
        if (!(this.itemStack.getItemMeta() instanceof AxolotlBucketMeta axolotlBucketMeta)) {
            throw new IllegalStateException("The ItemStack does not have AxolotlBucketMeta!");
        }

        return axolotlBucketMeta.hasVariant();
    }

    /**
     * Sets the {@link Axolotl.Variant} for the {@link ItemStack} if it contains {@link AxolotlBucketMeta}.
     *
     * @param axolotlVariant The {@link Axolotl.Variant} to set. Cannot be null.
     * @return The current {@link ItemBuilder} instance for method chaining.
     * @throws IllegalStateException if the {@link ItemStack}'s meta is not an instance of {@link AxolotlBucketMeta}.
     */
    public ItemBuilder setAxolotlVariant(@NotNull Axolotl.Variant axolotlVariant) {
        if (!(this.itemStack.getItemMeta() instanceof AxolotlBucketMeta axolotlBucketMeta)) {
            throw new IllegalStateException("The ItemStack does not have AxolotlBucketMeta!");
        }

        axolotlBucketMeta.setVariant(axolotlVariant);
        this.itemStack.setItemMeta(axolotlBucketMeta);

        return this;
    }

    /**
     * Converts the current {@link ItemStack} into a {@link BannerBuilder}.
     * The {@code ItemStack} is passed into the {@code BannerBuilder} constructor to allow for
     * pattern modifications on the banner.
     *
     * @return a new {@link BannerBuilder} instance, initialized with the current {@link ItemStack}.
     * @throws IllegalArgumentException if the {@link ItemStack} is invalid or does not have {@link BannerMeta}.
     */
    public BannerBuilder getBannerBuilder() {
        return new BannerBuilder(this.itemStack);
    }

    /**
     * Converts the current {@link ItemStack} into a {@link BookBuilder}.
     * The {@code ItemStack} is passed into the {@code BookBuilder} constructor to allow for
     * further modifications on the book.
     *
     * @return a new {@link BookBuilder} instance, initialized with the current {@link ItemStack}.
     */
    public BookBuilder getBookBuilder() {
        return new BookBuilder(this.itemStack);
    }

    /**
     * Validates the {@link ItemStack}.
     * Throws an exception if the item's material is {@link Material#AIR}.
     *
     * @return {@code false} if the {@link ItemStack} is valid.
     * @throws IllegalArgumentException if the item's material is air.
     */
    private boolean isInvalidItemStack() {
        if (this.itemStack.getType() == Material.AIR) {
            throw new IllegalArgumentException("The item's material is currently air!");
        }
        return false;
    }
}
