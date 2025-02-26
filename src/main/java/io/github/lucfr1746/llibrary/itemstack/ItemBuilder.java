package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.tag.DamageTypeTags;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
     * @throws IllegalArgumentException if any, RGB value is out of range.
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
     * Retrieves the custom model data of the ItemStack.
     *
     * @return the custom model data, or -1 if invalid or not set.
     */
    public int getCustomModelData() {
        if (isInvalidItemStack()) return -1;
        ItemMeta meta = getItemMeta();
        return hasCustomModelData() ? meta.getCustomModelData() : -1;
    }

    /**
     * Checks if the ItemStack has custom model data.
     *
     * @return {@code true} if custom model data is present, otherwise {@code false}.
     */
    public boolean hasCustomModelData() {
        if (isInvalidItemStack()) return false;
        ItemMeta meta = getItemMeta();
        return meta.hasCustomModelData();
    }

    /**
     * Sets the custom model data for the ItemStack.
     *
     * @param customModelData the custom model data value to set.
     * @return the updated {@code ItemBuilder} instance.
     * @throws IllegalStateException if the ItemStack is invalid.
     */
    public ItemBuilder setCustomModelData(int customModelData) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        meta.setCustomModelData(customModelData);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the current damage value of the ItemStack.
     *
     * @return the amount of damage, or -1 if the ItemStack is invalid or not damageable.
     * @throws IllegalStateException if the ItemStack does not have Damageable metadata.
     */
    public int getDamaged() {
        if (isInvalidItemStack()) return -1;

        ItemMeta meta = this.itemStack.getItemMeta();
        if (!(meta instanceof Damageable)) {
            throw new IllegalStateException("The ItemStack must have Damageable metadata!");
        }

        return ((Damageable) meta).getDamage();
    }

    /**
     * Gets the maximum durability of the ItemStack.
     *
     * @return the maximum durability value, or -1 if the ItemStack is invalid or not damageable.
     * @throws IllegalStateException if the ItemStack does not have Damageable metadata.
     */
    public int getMaxDurability() {
        if (isInvalidItemStack()) return -1;

        ItemMeta meta = this.itemStack.getItemMeta();
        if (!(meta instanceof Damageable)) {
            throw new IllegalStateException("The ItemStack must have Damageable metadata!");
        }

        return ((Damageable) meta).getMaxDamage();
    }

    /**
     * Checks if the ItemStack has taken any damage.
     *
     * @return true if the ItemStack has been damaged, false otherwise.
     * @throws IllegalStateException if the ItemStack does not have Damageable metadata.
     */
    public boolean hasDamaged() {
        if (isInvalidItemStack()) return false;

        ItemMeta meta = this.itemStack.getItemMeta();
        if (!(meta instanceof Damageable)) {
            throw new IllegalStateException("The ItemStack must have Damageable metadata!");
        }

        return ((Damageable) meta).hasDamage();
    }

    /**
     * Checks if the ItemStack has a maximum durability value set.
     *
     * @return true if the ItemStack has a maximum durability value, false otherwise.
     * @throws IllegalStateException if the ItemStack does not have Damageable metadata.
     */
    public boolean hasMaxDurability() {
        if (isInvalidItemStack()) return false;

        ItemMeta meta = this.itemStack.getItemMeta();
        if (!(meta instanceof Damageable)) {
            throw new IllegalStateException("The ItemStack must have Damageable metadata!");
        }

        return ((Damageable) meta).hasMaxDamage();
    }

    /**
     * Sets the damage value of the ItemStack.
     *
     * @param amount the damage amount to set. Clamped between 0 and the max durability of the ItemStack.
     * @return the modified ItemBuilder instance, or null if the ItemStack is invalid.
     * @throws IllegalStateException if the ItemStack does not have Damageable metadata.
     */
    public ItemBuilder setDamaged(int amount) {
        if (isInvalidItemStack()) return null;

        ItemMeta meta = this.itemStack.getItemMeta();
        if (!(meta instanceof Damageable damageableMeta)) {
            throw new IllegalStateException("The ItemStack must have Damageable metadata!");
        }

        amount = Math.max(0, Math.min(amount, this.itemStack.getType().getMaxDurability()));
        damageableMeta.setDamage(amount);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the maximum durability of the ItemStack.
     *
     * @param amount the maximum durability value to set.
     * @return the modified ItemBuilder instance, or null if the ItemStack is invalid.
     * @throws IllegalStateException if the ItemStack does not have Damageable metadata.
     */
    public ItemBuilder setMaxDurability(int amount) {
        if (isInvalidItemStack()) return null;

        ItemMeta meta = this.itemStack.getItemMeta();
        if (!(meta instanceof Damageable damageableMeta)) {
            throw new IllegalStateException("The ItemStack must have Damageable metadata!");
        }

        damageableMeta.setMaxDamage(amount);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Checks if the item stack contains the specified enchantment.
     *
     * @param enchantment the enchantment to check
     * @return true if the item contains the enchantment, false otherwise
     */
    public boolean containsEnchantment(@NotNull Enchantment enchantment) {
        return !isInvalidItemStack() && this.itemStack.containsEnchantment(enchantment);
    }

    /**
     * Gets the level of a specific enchantment on the item stack.
     *
     * @param enchantment the enchantment to check
     * @return the level of the enchantment, or 0 if not present or the item stack is invalid
     */
    public int getEnchantmentLevel(@NotNull Enchantment enchantment) {
        return isInvalidItemStack() ? 0 : this.itemStack.getEnchantmentLevel(enchantment);
    }

    /**
     * Retrieves all enchantments applied to the item stack.
     *
     * @return a map of enchantments and their levels, or an empty map if the item stack is invalid
     */
    @NotNull
    public Map<Enchantment, Integer> getEnchantments() {
        return isInvalidItemStack() ? Collections.emptyMap() : this.itemStack.getEnchantments();
    }

    /**
     * Adds multiple enchantments to the item stack.
     *
     * @param enchantments    a map of enchantments and their levels
     * @param byPassMaxLevel  if true, allows levels higher than the enchantment's max level
     * @return the current ItemBuilder instance
     */
    public ItemBuilder addEnchantments(@NotNull Map<Enchantment, Integer> enchantments, boolean byPassMaxLevel) {
        enchantments.forEach((enchantment, level) -> addEnchantment(enchantment, level, byPassMaxLevel));
        return this;
    }

    /**
     * Adds multiple enchantments to the item stack, respecting their max levels.
     *
     * @param enchantments a map of enchantments and their levels
     * @return the current ItemBuilder instance
     */
    public ItemBuilder addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        return addEnchantments(enchantments, false);
    }

    /**
     * Adds an enchantment to the item stack.
     *
     * @param enchantment    the enchantment to add
     * @param level          the level of the enchantment
     * @param byPassMaxLevel if true, allows levels higher than the enchantment's max level
     * @return the current ItemBuilder instance
     */
    public ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level, boolean byPassMaxLevel) {
        if (isInvalidItemStack()) return this;
        int lvl = byPassMaxLevel ? level : Math.min(enchantment.getMaxLevel(), level);
        if (lvl == 0) {
            removeEnchantment(enchantment);
        } else {
            this.itemStack.addUnsafeEnchantment(enchantment, lvl);
        }
        return this;
    }

    /**
     * Adds an enchantment to the item stack, respecting the enchantment's max level.
     *
     * @param enchantment the enchantment to add
     * @param level       the level of the enchantment
     * @return the current ItemBuilder instance
     */
    public ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
        return addEnchantment(enchantment, level, false);
    }

    /**
     * Removes a specific enchantment from the item stack.
     *
     * @param enchantment the enchantment to remove
     * @return the level of the removed enchantment, or -1 if the item stack is invalid
     */
    public int removeEnchantment(@NotNull Enchantment enchantment) {
        return isInvalidItemStack() ? -1 : this.itemStack.removeEnchantment(enchantment);
    }

    /**
     * Removes all enchantments from the item stack.
     *
     * @return the current ItemBuilder instance
     */
    public ItemBuilder removeEnchantments() {
        if (!isInvalidItemStack()) {
            this.itemStack.removeEnchantments();
        }
        return this;
    }

    /**
     * Checks if the item stack has damage resistance.
     *
     * @return true if the item has damage resistance, false otherwise
     */
    public boolean hasDamageResistant() {
        return !isInvalidItemStack() && getItemMeta().hasDamageResistant();
    }

    /**
     * Gets the damage resistance tag of the item stack.
     *
     * @return the damage resistance tag, or null if the item stack is invalid or has no resistance
     */
    @Nullable
    public Tag<DamageType> getDamageResistant() {
        return isInvalidItemStack() ? null : getItemMeta().getDamageResistant();
    }

    /**
     * Sets the damage resistance tag for the item stack.
     *
     * @param tag the damage resistance tag to set, or null to remove damage resistance
     * @return the current ItemBuilder instance
     */
    public ItemBuilder setDamageResistant(@Nullable Tag<DamageType> tag) {
        if (isInvalidItemStack()) return this;
        ItemMeta meta = getItemMeta();
        meta.setDamageResistant(tag);
        this.itemStack.setItemMeta(meta);
        return this;
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
     * Retrieves the {@link ItemMeta} from the {@link ItemStack}.
     *
     * @return the {@link ItemMeta} instance.
     * @throws IllegalStateException if the {@link ItemMeta} is null.
     */
    private @NotNull ItemMeta getItemMeta() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            throw new IllegalStateException("Failed to retrieve item meta.");
        }
        return meta;
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
