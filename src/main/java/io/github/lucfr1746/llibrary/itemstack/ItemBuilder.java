package io.github.lucfr1746.llibrary.itemstack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.lucfr1746.llibrary.utils.Util;
import io.github.lucfr1746.llibrary.utils.UtilsString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
     * Retrieves a FireworkBuilder instance based on the current itemStack.
     * <p>
     * If the itemStack is invalid, this method returns {@code null}.
     * Otherwise, it constructs and returns a new FireworkBuilder.
     * </p>
     *
     * @return a new {@link FireworkBuilder} if the itemStack is valid; {@code null} if invalid.
     */
    public FireworkBuilder getFireworkBuilder() {
        if (isInvalidItemStack()) return null;
        return new FireworkBuilder(this.itemStack);
    }

    /**
     * Retrieves a FoodBuilder instance for the current item stack.
     *
     * @return a new FoodBuilder if the item stack is valid; otherwise, returns null.
     */
    public FoodBuilder getFoodBuilder() {
        if (isInvalidItemStack()) return null;
        return new FoodBuilder(this.itemStack);
    }

    /**
     * Sets whether the item should have an enchantment glint override.
     *
     * @param value true to enable enchantment glint override, false to disable
     * @return the current ItemBuilder instance or null if the item stack is invalid
     */
    public ItemBuilder setEnchantmentGliderOverride(boolean value) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        meta.setEnchantmentGlintOverride(value);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Checks if the item has an enchantment glint override.
     *
     * @return true, if the item has an enchantment glint override, false otherwise
     */
    public boolean hasEnchantmentGliderOverride() {
        if (isInvalidItemStack()) return false;
        return getItemMeta().getEnchantmentGlintOverride();
    }

    /**
     * Sets the music instrument type for the item.
     *
     * @param type the MusicInstrument to set
     * @return the current ItemBuilder instance or null if the item stack is invalid
     * @throws IllegalStateException if the item does not have MusicInstrumentMeta
     */
    public ItemBuilder setMusicInstrument(MusicInstrument type) {
        if (isInvalidItemStack()) return null;
        if (getItemMeta() instanceof MusicInstrumentMeta meta) {
            meta.setInstrument(type);
            this.itemStack.setItemMeta(meta);
        } else {
            throw new IllegalStateException("Required ItemStack has MusicInstrumentMeta!");
        }
        return this;
    }

    /**
     * Gets the music instrument type of the item.
     *
     * @return the MusicInstrument of the item or null if the item stack is invalid
     * @throws IllegalStateException if the item does not have MusicInstrumentMeta
     */
    public MusicInstrument getMusicInstrument() {
        if (isInvalidItemStack()) return null;
        if (getItemMeta() instanceof MusicInstrumentMeta meta) {
            return meta.getInstrument();
        } else {
            throw new IllegalStateException("Required ItemStack has MusicInstrumentMeta!");
        }
    }

    /**
     * Adds item flags to the item.
     *
     * @param flags the item flags to add
     * @return the current ItemBuilder instance or null if the item stack is invalid
     */
    public ItemBuilder setFlags(@NotNull ItemFlag... flags) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        for (ItemFlag flag : List.of(flags)) {
            handleFlagChange(true, flag, this.itemStack, meta);
        }
        meta.addItemFlags(flags);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Adds all possible item flags to the item.
     *
     * @return the current ItemBuilder instance or null if the item stack is invalid
     */
    public ItemBuilder setAllFlags() {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        handleFlagsChange(this.itemStack, meta);
        meta.addItemFlags(ItemFlag.values());
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Removes item flags from the item.
     *
     * @param flags the item flags to remove
     * @return the current ItemBuilder instance or null if the item stack is invalid
     */
    public ItemBuilder removeFlags(@NotNull ItemFlag... flags) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        for (ItemFlag flag : List.of(flags)) {
            handleFlagChange(false, flag, this.itemStack, meta);
        }
        meta.removeItemFlags(flags);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Checks if the item has a specific item flag.
     *
     * @param flag the item flag to check
     * @return true if the item has the flag, false otherwise
     */
    public boolean hasFlag(@NotNull ItemFlag flag) {
        if (isInvalidItemStack()) return false;
        return getItemMeta().hasItemFlag(flag);
    }

    /**
     * Handles changes to item flags, specifically the HIDE_ATTRIBUTES flag.
     *
     * @param put  true to add attributes, false to remove them.
     * @param flag the ItemFlag being modified.
     * @param item the ItemStack whose attributes are being changed.
     * @param meta the ItemMeta of the item.
     */
    private void handleFlagChange(boolean put, ItemFlag flag, ItemStack item, ItemMeta meta) {
        if (!Util.hasPaperAPI()) {
            return;
        }
        if (flag != ItemFlag.HIDE_ATTRIBUTES) {
            return;
        }
        if (put) {
            if (meta.getAttributeModifiers() != null) {
                return;
            }
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                item.getType().getDefaultAttributeModifiers(slot).forEach(meta::addAttributeModifier);
            }
            return;
        }

        Multimap<Attribute, AttributeModifier> mods = meta.getAttributeModifiers();
        if (mods == null) {
            return;
        }

        HashMultimap<Attribute, AttributeModifier> mods2 = HashMultimap.create();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            mods2.putAll(item.getType().getDefaultAttributeModifiers(slot));
        }

        if (mods.equals(mods2)) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                meta.removeAttributeModifier(slot);
            }
        }
    }

    /**
     * Ensures that default attribute modifiers are added to the item if none exist.
     *
     * @param item the ItemStack whose attributes are being updated.
     * @param meta the ItemMeta of the item.
     */
    private void handleFlagsChange(ItemStack item, ItemMeta meta) {
        if (!Util.hasPaperAPI()) {
            return;
        }
        if (meta.getAttributeModifiers() != null) {
            return;
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            item.getType().getDefaultAttributeModifiers(slot).forEach(meta::addAttributeModifier);
        }
    }

    /**
     * Toggles the hiding of item tooltips.
     *
     * @param value true to hide tooltips, false to show them
     * @return the current ItemBuilder instance or null if the item stack is invalid
     */
    public ItemBuilder hideToolTip(boolean value) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        meta.setHideTooltip(value);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Checks if item tooltips are hidden.
     *
     * @return true if tooltips are hidden, false otherwise
     */
    public boolean isHideToolTip() {
        if (isInvalidItemStack()) return false;
        return getItemMeta().isHideTooltip();
    }

    /**
     * Sets the custom item model using a NamespacedKey.
     *
     * @param key the NamespacedKey representing the item model.
     * @return the current ItemBuilder instance, or null if the ItemStack is invalid.
     */
    public ItemBuilder setItemModel(@Nullable NamespacedKey key) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        meta.setItemModel(key);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the custom item model using a namespace and key.
     *
     * @param key  the namespace of the item model.
     * @param name the key representing the item model.
     * @return the current ItemBuilder instance.
     */
    public ItemBuilder setItemModel(@NotNull String key, @NotNull String name) {
        return setItemModel(new NamespacedKey(key, name));
    }

    /**
     * Sets the custom item model using the "minecraft" namespace and a key.
     *
     * @param name the key representing the item model.
     * @return the current ItemBuilder instance.
     */
    public ItemBuilder setItemModel(@NotNull String name) {
        return setItemModel(new NamespacedKey(NamespacedKey.MINECRAFT, name));
    }

    /**
     * Replaces all occurrences of a string in the item lore.
     *
     * @param from the string to be replaced
     * @param to the replacement string
     * @return this ItemBuilder instance, or null if the item stack is invalid
     */
    public ItemBuilder loreReplace(String from, String to) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        List<String> lore = Optional.ofNullable(meta.getLore()).orElse(new ArrayList<>());

        from = UtilsString.fix(from, null, true);
        to = UtilsString.fix(to, null, true);
        String finalFrom = from;
        String finalTo = to;
        lore.replaceAll(line -> line.replace(finalFrom, finalTo));

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Copies the lore from another ItemStack.
     *
     * @param target the ItemStack to copy lore from
     * @return this ItemBuilder instance, or null if the item stack or target is invalid
     */
    public ItemBuilder loreCopyFrom(ItemStack target) {
        if (isInvalidItemStack() || target == null) return null;
        ItemMeta targetMeta = target.getItemMeta();
        if (targetMeta == null || !targetMeta.hasLore() || targetMeta.getLore() == null) return null;

        ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<>(targetMeta.getLore()));
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Copies lore from a book's pages.
     *
     * @param book the book ItemStack to copy pages from
     * @return this ItemBuilder instance, or null if the item stack or book is invalid
     */
    public ItemBuilder loreCopyFromBook(ItemStack book) {
        if (isInvalidItemStack() || !(book.getItemMeta() instanceof BookMeta bookMeta)) {
            return null;
        }

        List<String> lore = bookMeta.getPages().stream()
                .filter(Objects::nonNull)
                .flatMap(page -> Arrays.stream(page.split("\n")))
                .map(Util::formatText)
                .toList();

        ItemMeta meta = getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Adds new lines to the lore.
     *
     * @param texts the lines to add
     * @return this ItemBuilder instance, or null if the item stack is invalid or texts are null/empty
     */
    public ItemBuilder loreAdd(String... texts) {
        if (isInvalidItemStack() || texts == null || texts.length == 0) return null;
        ItemMeta meta = getItemMeta();
        List<String> lore = Optional.ofNullable(meta.getLore()).orElse(new ArrayList<>());

        Arrays.stream(texts).map(Util::formatText).forEach(lore::add);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Inserts a line at a specific index in the lore.
     *
     * @param line the index to insert at
     * @param text the line to insert
     * @return this ItemBuilder instance
     * @throws IllegalArgumentException if the line is negative
     */
    public ItemBuilder loreInsert(int line, String text) {
        if (isInvalidItemStack() || line < 0) throw new IllegalArgumentException("Invalid line number");
        ItemMeta meta = getItemMeta();
        List<String> lore = Optional.ofNullable(meta.getLore()).orElse(new ArrayList<>());

        while (lore.size() <= line) lore.add("");
        lore.add(line, Util.formatText(text));

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets a specific line in the lore.
     *
     * @param line the index to set
     * @param text the new line content
     * @return this ItemBuilder instance
     * @throws IllegalArgumentException if the line is negative
     */
    public ItemBuilder loreSet(int line, String text) {
        if (isInvalidItemStack() || line < 0) throw new IllegalArgumentException("Invalid line number");
        ItemMeta meta = getItemMeta();
        List<String> lore = Optional.ofNullable(meta.getLore()).orElse(new ArrayList<>());

        while (lore.size() <= line) lore.add("");
        lore.set(line, Util.formatText(text));

        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets multiple lines of lore.
     *
     * @param lores the list of lines to set as lore
     * @return this ItemBuilder instance
     */
    public ItemBuilder loresSet(List<String> lores) {
        if (isInvalidItemStack()) return null;
        if (lores == null) return this;
        ItemMeta meta = getItemMeta();
        List<String> formattedLores = lores.stream().map(Util::formatText).toList();

        meta.setLore(formattedLores);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Removes a specific line from the lore.
     *
     * @param line the index of the line to remove (1-based index)
     * @return this ItemBuilder instance
     * @throws IllegalArgumentException if line is less than or equal to 0
     */
    public ItemBuilder loreRemove(int line) {
        if (isInvalidItemStack() || line <= 0) throw new IllegalArgumentException("Invalid line number");
        ItemMeta meta = getItemMeta();
        List<String> lore = Optional.ofNullable(meta.getLore()).orElse(new ArrayList<>());

        if (line - 1 < lore.size()) lore.remove(line - 1);

        meta.setLore(lore.isEmpty() ? null : lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Clears all lore.
     *
     * @return this ItemBuilder instance
     */
    public ItemBuilder loreReset() {
        if (isInvalidItemStack()) return this;
        ItemMeta meta = getItemMeta();
        meta.setLore(null);
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the maximum stack size for this ItemStack.
     *
     * @param amount The maximum stack size, clamped between 0 and 99.
     * @return {@code this} for method chaining, or {@code null} if the ItemStack is invalid.
     */
    public ItemBuilder setMaxStackSize(int amount) {
        if (isInvalidItemStack()) return null;
        amount = Math.max(0, Math.min(amount, 99));
        ItemMeta meta = getItemMeta();
        meta.setMaxStackSize(amount);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the maximum stack size of this ItemStack.
     *
     * @return The maximum stack size, or {@code 0} if the ItemStack is invalid.
     */
    public int getMaxStackSize() {
        if (isInvalidItemStack()) return 0;
        return getItemMeta().getMaxStackSize();
    }

    /**
     * Removes a specific potion effect from the ItemStack.
     *
     * @param effect The potion effect types to remove.
     * @return {@code this} for method chaining, or {@code null} if the ItemStack is invalid or does not have potion effect metadata.
     */
    public ItemBuilder removePotionEffect(@NotNull PotionEffectType effect) {
        if (isInvalidItemStack() || !hasPotionEffectMeta()) return null;

        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.removeCustomEffect(effect);
        } else if (meta instanceof SuspiciousStewMeta stewMeta) {
            stewMeta.removeCustomEffect(effect);
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Adds a custom potion effect to the ItemStack.
     *
     * @param effect The potion effect type to add.
     * @param durationInSeconds The duration of the effect in seconds.
     * @param level The amplifier level of the effect (0-127).
     * @param particles Whether the effect should show particles.
     * @param ambient Whether the effect is considered ambient.
     * @param icon Whether the effect should show an icon.
     * @return {@code this} for method chaining, or {@code null} if the ItemStack is invalid or does not have potion effect metadata.
     * @throws IllegalArgumentException if the level is not between 0 and 127.
     */
    public ItemBuilder addPotionEffect(@NotNull PotionEffectType effect, int durationInSeconds, int level, boolean particles, boolean ambient, boolean icon) {
        if (isInvalidItemStack() || !hasPotionEffectMeta()) return null;

        if (level < 0 || level > 127)
            throw new IllegalArgumentException("Potion effect level must be between 0 and 127.");

        int durationInTicks = durationInSeconds >= 0 ? durationInSeconds * 20 : Integer.MAX_VALUE;
        PotionEffect potionEffect = new PotionEffect(effect, durationInTicks, level, ambient, particles, icon);

        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.addCustomEffect(potionEffect, true);
        } else if (meta instanceof SuspiciousStewMeta stewMeta) {
            stewMeta.addCustomEffect(potionEffect, true);
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Clears all custom potion effects from the ItemStack.
     *
     * @return {@code this} for method chaining, or {@code null} if the ItemStack is invalid or does not have potion effect metadata.
     */
    public ItemBuilder clearPotionEffects() {
        if (isInvalidItemStack() || !hasPotionEffectMeta()) return null;

        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.clearCustomEffects();
        } else if (meta instanceof SuspiciousStewMeta stewMeta) {
            stewMeta.clearCustomEffects();
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Retrieves the list of custom potion effects on the ItemStack.
     *
     * @return A list of PotionEffects, or an empty list if the ItemStack is invalid or does not have potion effect metadata.
     */
    public List<PotionEffect> getPotionEffects() {
        if (isInvalidItemStack() || !hasPotionEffectMeta()) return Collections.emptyList();

        ItemMeta meta = getItemMeta();
        if (meta instanceof PotionMeta potionMeta) {
            return potionMeta.getCustomEffects();
        } else if (meta instanceof SuspiciousStewMeta stewMeta) {
            return stewMeta.getCustomEffects();
        }
        return Collections.emptyList();
    }

    /**
     * Sets the rarity of the ItemStack.
     *
     * @param rarity The ItemRarity to set.
     * @return {@code this} for method chaining, or {@code null} if the ItemStack is invalid.
     */
    public ItemBuilder setRarity(@NotNull ItemRarity rarity) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        meta.setRarity(rarity);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the rarity of the ItemStack.
     *
     * @return The ItemRarity of the ItemStack, or {@code null} if the ItemStack is invalid.
     */
    public ItemRarity getRarity() {
        if (isInvalidItemStack()) return null;
        return getItemMeta().getRarity();
    }

    /**
     * Sets the display name of the ItemStack.
     *
     * @param name The new display name for the ItemStack.
     *             <ul>
     *                 <li>If {@code null} or "clear" (case-insensitive), the display name is removed (reset to default).</li>
     *                 <li>If empty or blank, the display name is set to white.</li>
     *                 <li>Otherwise, the name is formatted using {@link Util#formatText(String)} and color codes are translated.</li>
     *             </ul>
     * @return The current ItemBuilder instance for method chaining, or {@code null} if the ItemStack is invalid.
     */
    public ItemBuilder rename(@Nullable String name) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();

        if (name == null || name.equalsIgnoreCase("clear")) {
            meta.setDisplayName(null);
            this.itemStack.setItemMeta(meta);
            return this;
        }

        if (name.isBlank() || name.isEmpty()) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f"));
            this.itemStack.setItemMeta(meta);
            return this;
        }

        name = Util.formatText(name);
        meta.setDisplayName(name);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the display name of the ItemStack in MiniMessage format.
     *
     * @return The display name converted to MiniMessage format, or {@code null} if the ItemStack is invalid.
     */
    public String getDisplayName() {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        Component component = LegacyComponentSerializer.legacySection().deserialize(meta.getDisplayName());
        return MiniMessage.miniMessage().serialize(component);
    }

    /**
     * Sets the repair cost of the ItemStack.
     *
     * @param cost The repair cost to set.
     * @return The current ItemBuilder instance for method chaining, or {@code null} if the ItemStack is invalid.
     */
    public ItemBuilder setRepairCost(int cost) {
        if (isInvalidItemStack()) return null;
        Repairable meta = (Repairable) getItemMeta();
        meta.setRepairCost(cost);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the repair cost of the ItemStack.
     *
     * @return The repair cost, or {@code 0} if the ItemStack is invalid.
     */
    public int getRepairCost() {
        if (isInvalidItemStack()) return 0;
        return ((Repairable) getItemMeta()).getRepairCost();
    }

    /**
     * Sets the tooltip style of the ItemStack.
     *
     * @param value The tooltip style to set. Use "clear" to reset the tooltip style to default.
     *              Format can be either "namespace:key" or "key" (which defaults to the "minecraft" namespace).
     * @return The current ItemBuilder instance for method chaining, or {@code null} if the ItemStack is invalid.
     */
    public ItemBuilder setToolTipStyle(String value) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        if (value.equalsIgnoreCase("clear")) {
            meta.setTooltipStyle(null);
            this.itemStack.setItemMeta(meta);
            return this;
        }
        String pre;
        String post;
        if (!value.contains(":")) {
            pre = NamespacedKey.MINECRAFT;
            post = value;
        } else {
            pre = value.split(":")[0];
            post = value.split(":")[1];
        }
        meta.setTooltipStyle(new NamespacedKey(pre, post));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the tooltip style of the ItemStack.
     *
     * @return The tooltip style as a {@link NamespacedKey}, or {@code null} if the ItemStack is invalid or has no tooltip style set.
     */
    public NamespacedKey getToolTipStyle() {
        if (isInvalidItemStack()) return null;
        return getItemMeta().getTooltipStyle();
    }

    /**
     * Sets the armor trim of the ItemStack using the specified trim material and pattern.
     *
     * @param trimMaterial The material to use for the armor trim. Must not be null.
     * @param trimPattern The pattern to use for the armor trim. Must not be null.
     * @return The current ItemBuilder instance for method chaining, or {@code null} if the ItemStack is invalid.
     * @throws IllegalStateException if the ItemStack does not have {@link ArmorMeta}.
     */
    public ItemBuilder trimArmor(@NotNull TrimMaterial trimMaterial, @NotNull TrimPattern trimPattern) {
        if (isInvalidItemStack()) return null;
        if (!(getItemMeta() instanceof ArmorMeta meta))
            throw new IllegalStateException("The ItemStack must have ArmorMeta!");
        meta.setTrim(new ArmorTrim(trimMaterial, trimPattern));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the armor trim of the ItemStack.
     *
     * @return The {@link ArmorTrim} of the ItemStack, or {@code null} if the ItemStack is invalid or has no trim set.
     * @throws IllegalStateException if the ItemStack does not have {@link ArmorMeta}.
     */
    public ArmorTrim getArmorTrim() {
        if (isInvalidItemStack()) return null;
        if (!(getItemMeta() instanceof ArmorMeta meta))
            throw new IllegalStateException("The ItemStack must have ArmorMeta!");
        return meta.getTrim();
    }

    /**
     * Sets the body color of the tropical fish in the ItemStack.
     *
     * @param color The body color to set. Must not be null.
     * @return The current ItemBuilder instance for method chaining, or {@code null} if the ItemStack is invalid.
     * @throws IllegalStateException if the ItemStack does not have {@link TropicalFishBucketMeta}.
     */
    public ItemBuilder setTropicalFishBodyColor(@NotNull DyeColor color) {
        if (isInvalidItemStack()) return null;
        if (!(getItemMeta() instanceof TropicalFishBucketMeta meta))
            throw new IllegalStateException("The ItemStack must have TropicalFishBucketMeta!");
        meta.setBodyColor(color);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the body color of the tropical fish in the ItemStack.
     *
     * @return The {@link DyeColor} of the tropical fish's body, or {@code null} if the ItemStack is invalid.
     * @throws IllegalStateException if the ItemStack does not have {@link TropicalFishBucketMeta}.
     */
    public DyeColor getTropicalFishBodyColor() {
        if (isInvalidItemStack()) return null;
        if (!(getItemMeta() instanceof TropicalFishBucketMeta meta))
            throw new IllegalStateException("The ItemStack must have TropicalFishBucketMeta!");
        return meta.getBodyColor();
    }

    /**
     * Sets the pattern color of the tropical fish in the ItemStack.
     *
     * @param color The pattern color to set. Must not be null.
     * @return The current ItemBuilder instance for method chaining, or {@code null} if the ItemStack is invalid.
     * @throws IllegalStateException if the ItemStack does not have {@link TropicalFishBucketMeta}.
     */
    public ItemBuilder setTropicalFishPatternColor(@NotNull DyeColor color) {
        if (isInvalidItemStack()) return null;
        if (!(getItemMeta() instanceof TropicalFishBucketMeta meta))
            throw new IllegalStateException("The ItemStack must have TropicalFishBucketMeta!");
        meta.setPatternColor(color);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the pattern color of the tropical fish in the ItemStack.
     *
     * @return The {@link DyeColor} of the tropical fish's pattern, or {@code null} if the ItemStack is invalid.
     * @throws IllegalStateException if the ItemStack does not have {@link TropicalFishBucketMeta}.
     */
    public DyeColor getTropicalFishPatternColor() {
        if (isInvalidItemStack()) return null;
        if (!(getItemMeta() instanceof TropicalFishBucketMeta meta))
            throw new IllegalStateException("The ItemStack must have TropicalFishBucketMeta!");
        return meta.getPatternColor();
    }

    /**
     * Sets the pattern of the tropical fish in the ItemStack.
     *
     * @param pattern The pattern to set. Must not be null.
     * @return The current ItemBuilder instance for method chaining, or {@code null} if the ItemStack is invalid.
     * @throws IllegalStateException if the ItemStack does not have {@link TropicalFishBucketMeta}.
     */
    public ItemBuilder setTropicalFishPattern(@NotNull TropicalFish.Pattern pattern) {
        if (isInvalidItemStack()) return null;
        if (!(getItemMeta() instanceof TropicalFishBucketMeta meta))
            throw new IllegalStateException("The ItemStack must have TropicalFishBucketMeta!");
        meta.setPattern(pattern);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Gets the pattern of the tropical fish in the ItemStack.
     *
     * @return The {@link TropicalFish.Pattern} of the tropical fish, or {@code null} if the ItemStack is invalid.
     * @throws IllegalStateException if the ItemStack does not have {@link TropicalFishBucketMeta}.
     */
    public TropicalFish.Pattern getTropicalFishPattern() {
        if (isInvalidItemStack()) return null;
        if (!(getItemMeta() instanceof TropicalFishBucketMeta meta))
            throw new IllegalStateException("The ItemStack must have TropicalFishBucketMeta!");
        return meta.getPattern();
    }

    /**
     * Sets the type of the ItemStack.
     *
     * @param type The material type to set. Must not be null or {@link Material#AIR}.
     * @return The current ItemBuilder instance for method chaining, or {@code null} if the ItemStack is invalid.
     * @throws IllegalArgumentException if the provided material type is {@link Material#AIR}.
     */
    public ItemBuilder setType(Material type) {
        if (isInvalidItemStack()) return null;
        if (type == Material.AIR)
            throw new IllegalArgumentException("The ItemStack can't be air!");
        this.itemStack.setType(type);
        return this;
    }

    /**
     * Gets the type of the ItemStack.
     *
     * @return The {@link Material} type of the ItemStack.
     */
    public Material getType() {
        return this.itemStack.getType();
    }

    /**
     * Sets whether the ItemStack is unbreakable.
     *
     * @param value {@code true} to make the ItemStack unbreakable, {@code false} to allow it to break.
     * @return The current ItemBuilder instance for method chaining, or {@code null} if the ItemStack is invalid.
     */
    public ItemBuilder setUnbreakable(boolean value) {
        if (isInvalidItemStack()) return null;
        ItemMeta meta = getItemMeta();
        meta.setUnbreakable(value);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Checks if the ItemStack is unbreakable.
     *
     * @return {@code true} if the ItemStack is unbreakable, {@code false} if it can break or if the ItemStack is invalid.
     */
    public boolean isUnbreakable() {
        if (isInvalidItemStack()) return false;
        return getItemMeta().isUnbreakable();
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
        ItemMeta meta = this.itemStack.getItemMeta();
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
        ItemMeta meta = getItemMeta();
        return meta instanceof PotionMeta || meta instanceof LeatherArmorMeta || meta instanceof FireworkEffectMeta;
    }

    /**
     * Checks if the ItemStack has potion effect-related metadata.
     *
     * @return {@code true} if the ItemMeta is an instance of PotionMeta or SuspiciousStewMeta,
     *         {@code false} otherwise.
     */
    private boolean hasPotionEffectMeta() {
        ItemMeta meta = getItemMeta();
        return meta instanceof PotionMeta || meta instanceof SuspiciousStewMeta;
    }
}
