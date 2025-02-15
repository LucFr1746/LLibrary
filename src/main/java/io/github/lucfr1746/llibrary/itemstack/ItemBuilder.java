package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
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
     * @return The {@link Axolotl.Variant} associated with the item, or null if the item is invalid.
     * @throws IllegalStateException if the {@link ItemStack}'s meta is not an instance of {@link AxolotlBucketMeta}.
     */
    public Axolotl.Variant getAxolotlVariant() {
        if (isInvalidItemStack()) return null;
        if (!(this.itemStack.getItemMeta() instanceof AxolotlBucketMeta axolotlBucketMeta)) {
            throw new IllegalStateException("The ItemStack does not have AxolotlBucketMeta!");
        }

        return axolotlBucketMeta.getVariant();
    }

    /**
     * Checks if the {@link ItemStack} with {@link AxolotlBucketMeta} contains an {@link Axolotl.Variant}.
     *
     * @return {@code true} if the {@link ItemStack} has an {@link Axolotl.Variant}, {@code false} otherwise, false if the item is invalid.
     * @throws IllegalStateException if the {@link ItemStack}'s meta is not an instance of {@link AxolotlBucketMeta}.
     */
    public boolean hasAxolotlVariant() {
        if (isInvalidItemStack()) return false;
        if (!(this.itemStack.getItemMeta() instanceof AxolotlBucketMeta axolotlBucketMeta)) {
            throw new IllegalStateException("The ItemStack does not have AxolotlBucketMeta!");
        }

        return axolotlBucketMeta.hasVariant();
    }

    /**
     * Sets the {@link Axolotl.Variant} for the {@link ItemStack} if it contains {@link AxolotlBucketMeta}.
     *
     * @param axolotlVariant The {@link Axolotl.Variant} to set. Cannot be null.
     * @return The current {@link ItemBuilder} instance for method chaining, or null if the item is invalid.
     * @throws IllegalStateException if the {@link ItemStack}'s meta is not an instance of {@link AxolotlBucketMeta}.
     */
    public ItemBuilder setAxolotlVariant(@NotNull Axolotl.Variant axolotlVariant) {
        if (isInvalidItemStack()) return null;
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
     * Retrieves the color of the ItemStack if applicable.
     *
     * @return the color of the ItemStack, or null if not applicable.
     * @throws IllegalStateException if the ItemStack does not support color.
     */
    public Color getColor() {
        if (isInvalidItemStack()) return null;
        if (!hasColorMeta()) {
            throw new IllegalStateException("The ItemStack must have PotionMeta, LeatherArmorMeta, or FireworkEffectMeta!");
        }
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta instanceof PotionMeta potionMeta) return potionMeta.getColor();
        if (meta instanceof LeatherArmorMeta leatherArmorMeta) return leatherArmorMeta.getColor();
        throw new IllegalArgumentException("The ItemStack has FireworkEffectMeta and should be returned as a list of colors!");
    }

    /**
     * Checks if the ItemStack has a color.
     *
     * @return true if the ItemStack has a color, false otherwise.
     * @throws IllegalStateException if the ItemStack does not support color.
     */
    public boolean hasColor() {
        if (isInvalidItemStack()) return false;
        if (!hasColorMeta()) {
            throw new IllegalStateException("The ItemStack must have PotionMeta, LeatherArmorMeta, or FireworkEffectMeta!");
        }
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta instanceof PotionMeta potionMeta) return potionMeta.hasColor();
        if (meta instanceof FireworkEffectMeta fireworkEffectMeta) {
            FireworkEffect effect = fireworkEffectMeta.getEffect();
            return effect != null && !effect.getColors().isEmpty();
        }
        return true;
    }

    /**
     * Sets the color of the ItemStack.
     *
     * @param color the color to set.
     * @return the updated ItemBuilder instance.
     * @throws IllegalStateException if the ItemStack does not support color.
     */
    public ItemBuilder setColor(Color color) {
        if (isInvalidItemStack()) return null;
        if (!hasColorMeta()) {
            throw new IllegalStateException("The ItemStack must have PotionMeta, LeatherArmorMeta, or FireworkEffectMeta!");
        }
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.setColor(color);
            this.itemStack.setItemMeta(potionMeta);
        } else if (meta instanceof LeatherArmorMeta leatherArmorMeta) {
            leatherArmorMeta.setColor(color);
            this.itemStack.setItemMeta(leatherArmorMeta);
        } else if (meta instanceof FireworkEffectMeta fireworkEffectMeta) {
            FireworkEffect oldEffect = fireworkEffectMeta.getEffect();
            FireworkEffect.Builder newEffect = FireworkEffect.builder()
                    .flicker(oldEffect != null && oldEffect.hasFlicker())
                    .trail(oldEffect != null && oldEffect.hasTrail())
                    .withColor(color);
            if (oldEffect != null) newEffect.withFade(oldEffect.getFadeColors());
            fireworkEffectMeta.setEffect(newEffect.build());
            this.itemStack.setItemMeta(fireworkEffectMeta);
        }
        return this;
    }

    /**
     * Sets the color of the ItemStack using RGB values.
     *
     * @param red   the red component (0-255).
     * @param green the green component (0-255).
     * @param blue  the blue component (0-255).
     * @return the updated ItemBuilder instance.
     * @throws IllegalArgumentException if any RGB value is out of range.
     */
    public ItemBuilder setColor(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be between 0 and 255.");
        }
        return setColor(Color.fromRGB(red, green, blue));
    }

    /**
     * Sets the color of the ItemStack using a hex color code.
     *
     * @param hexColor the hex color string (e.g., "#FF5733").
     * @return the updated ItemBuilder instance.
     * @throws IllegalArgumentException if the hex color format is invalid.
     */
    public ItemBuilder setColor(String hexColor) {
        if (!hexColor.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            throw new IllegalArgumentException("Invalid HEX color format: " + hexColor);
        }
        if (hexColor.length() == 4) {
            hexColor = "#" + hexColor.charAt(1) + hexColor.charAt(1)
                    + hexColor.charAt(2) + hexColor.charAt(2)
                    + hexColor.charAt(3) + hexColor.charAt(3);
        }
        return setColor(
                Integer.parseInt(hexColor.substring(1, 3), 16),
                Integer.parseInt(hexColor.substring(3, 5), 16),
                Integer.parseInt(hexColor.substring(5, 7), 16)
        );
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

    /**
     * Checks if the ItemStack has metadata supporting colors.
     *
     * @return true if the ItemStack has color metadata, false otherwise.
     */
    private boolean hasColorMeta() {
        ItemMeta meta = this.itemStack.getItemMeta();
        return meta instanceof PotionMeta || meta instanceof LeatherArmorMeta || meta instanceof FireworkEffectMeta;
    }
}
