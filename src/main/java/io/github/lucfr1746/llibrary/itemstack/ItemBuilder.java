package io.github.lucfr1746.llibrary.itemstack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.itemstack.component.*;
import io.github.lucfr1746.llibrary.util.NamespaceKey;
import io.github.lucfr1746.llibrary.util.helper.StringUtil;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.JukeboxSong;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A utility class for building and modifying {@link ItemStack} objects in Minecraft.
 * Supports setting item material, amount, display names, and more.
 */
public class ItemBuilder {

    private final @NotNull ItemStack itemStack;
    private @NotNull ItemMeta itemMeta;

    /**
     * Creates an ItemBuilder from an existing {@link ItemStack}.
     *
     * @param itemStack the ItemStack to clone and modify
     * @throws NullPointerException if itemStack is null
     */
    public ItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        if (this.itemStack.getItemMeta() == null) {
            throw new RuntimeException("Failed to retrieve item meta.");
        }
        this.itemMeta = this.itemStack.getItemMeta();
    }

    /**
     * Creates an ItemBuilder with a specified {@link Material} and amount.
     *
     * @param material the material of the item
     * @param amount the quantity of items (must be between 1 and 127)
     * @throws IllegalArgumentException if the amount is less than 1 or greater than 127
     * @throws NullPointerException if material is null
     */
    public ItemBuilder(@NotNull Material material, int amount) {
        if (amount < 1 || amount > 127) {
            throw new IllegalArgumentException("The amount must be between 1 and 127 -> " + amount);
        }
        this.itemStack = new ItemStack(material, amount);
        if (this.itemStack.getItemMeta() == null) {
            throw new RuntimeException("Failed to retrieve item meta.");
        }
        this.itemMeta = this.itemStack.getItemMeta();
    }

    /**
     * Creates an ItemBuilder with a specified {@link Material} and a default amount of 1.
     *
     * @param material the material of the item
     * @throws IllegalArgumentException if material is air
     * @throws NullPointerException if material is null
     */
    public ItemBuilder(@NotNull Material material) {
        if (material.isAir()) {
            throw new IllegalArgumentException("The material cannot be air -> " + material);
        }
        this.itemStack = new ItemStack(material);
        if (this.itemStack.getItemMeta() == null) {
            throw new RuntimeException("Failed to retrieve item meta.");
        }
        this.itemMeta = this.itemStack.getItemMeta();
    }

    /**
     * Builds and returns the final {@link ItemStack}.
     *
     * @return a clone of the constructed ItemStack
     */
    public @NotNull ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

    /**
     * Sets the number of items in the stack.
     *
     * @param amount the number of items must be between 1 and 127 (inclusive)
     * @return this ItemBuilder instance for method chaining
     * @throws IllegalArgumentException if the amount is less than 1 or greater than 127
     */
    public ItemBuilder setAmount(int amount) {
        if (amount < 1 || amount > 127) {
            throw new IllegalArgumentException("The amount must be between 1 and 127 -> " + amount);
        }
        this.itemStack.setAmount(amount);
        return this;
    }

    /**
     * Gets the number of items in the stack.
     *
     * @return the number of items in the stack
     */
    public int getAmount() {
        return this.itemStack.getAmount();
    }

    /**
     * Sets the type of the item stack.
     *
     * @param material the material to set, must not be air
     * @return this ItemBuilder instance for method chaining
     * @throws IllegalArgumentException if the material is air
     */
    public ItemBuilder setType(@NotNull Material material) {
        if (material.isAir()) {
            throw new IllegalArgumentException("The material cannot be air -> " + material);
        }
        this.itemStack.setType(material);
        return this;
    }

    /**
     * Gets the type of material of the item stack.
     *
     * @return the material type of the item stack
     */
    public @NotNull Material getType() {
        return this.itemStack.getType();
    }

    /**
     * Gets the translation key for this item, used for localization.
     *
     * @return the translation key for the item
     */
    public @NotNull String getTranslationKey() {
        return this.itemStack.getTranslationKey();
    }

    /**
     * Sets the custom texture of a player head item.
     *
     * @param texture The Base64-encoded texture string.
     * @return The current ItemBuilder instance for chaining.
     * @throws IllegalArgumentException if the item is not a PLAYER_HEAD.
     */
    public ItemBuilder setSkullTexture(String texture) {
        if (getType() != Material.PLAYER_HEAD) {
            throw new IllegalArgumentException("The itemstack must be a PLAYER_HEAD");
        }

        this.itemStack.setItemMeta(this.itemMeta);

        NBT.modifyComponents(this.itemStack, nbt -> {
            ReadWriteNBT profileNbt = nbt.getOrCreateCompound("minecraft:profile");
            profileNbt.setUUID("id", UUID.randomUUID());
            ReadWriteNBTCompoundList propertiesList = profileNbt.getCompoundList("properties");

            ReadWriteNBT textureCompound = null;
            for (ReadWriteNBT compound : propertiesList) {
                if ("textures".equals(compound.getString("name"))) {
                    textureCompound = compound;
                    break;
                }
            }

            if (textureCompound != null) {
                textureCompound.setString("value", texture);
            } else {
                ReadWriteNBT newTextureCompound = propertiesList.addCompound();
                newTextureCompound.setString("name", "textures");
                newTextureCompound.setString("value", texture);
            }
        });

        this.itemMeta = Objects.requireNonNull(this.itemStack.getItemMeta(), "Failed to retrieve item meta.");
        return this;
    }

    /**
     * Retrieves the custom texture of a player head item.
     *
     * @return The Base64-encoded texture string, or "NONE" if not found.
     * @throws IllegalArgumentException if the item is not a PLAYER_HEAD.
     */
    public String getSkullTexture() {
        if (getType() != Material.PLAYER_HEAD) {
            throw new IllegalArgumentException("The itemstack must be a PLAYER_HEAD");
        }

        return NBT.modifyComponents(this.itemStack, nbt -> {
            ReadWriteNBT profileNbt = nbt.getCompound("minecraft:profile");
            if (profileNbt == null) return null;

            ReadWriteNBTCompoundList propertiesList = profileNbt.getCompoundList("properties");
            if (propertiesList == null || propertiesList.isEmpty()) return null;

            ReadWriteNBT propertiesNbt = propertiesList.get(0);

            return propertiesNbt.getOrDefault("value", null);
        });
    }

    /**
     * Removes the custom texture from a player head item.
     *
     * @return The current ItemBuilder instance for chaining.
     * @throws IllegalArgumentException if the item is not a PLAYER_HEAD.
     */
    public ItemBuilder removeSkullTexture() {
        if (getType() != Material.PLAYER_HEAD) {
            throw new IllegalArgumentException("The itemstack must be a PLAYER_HEAD");
        }

        this.itemStack.setItemMeta(this.itemMeta);

        NBT.modifyComponents(this.itemStack, nbt -> {
            if (nbt.hasTag("minecraft:profile")) {
                nbt.removeKey("minecraft:profile");
            }
        });

        this.itemMeta = Objects.requireNonNull(this.itemStack.getItemMeta(), "Failed to retrieve item meta.");
        return this;
    }

    /**
     * Sets the display name of the item.
     *
     * @param name the new display name can be null to remove the display name
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setDisplayName(String name) {
        this.itemMeta.setDisplayName(name == null ? null : StringUtil.format(name, null));
        return this;
    }

    /**
     * Gets the display name of the item.
     *
     * @return the display name if a set, or the default item translation key if not
     */
    public String getDisplayName() {
        return this.itemMeta.hasDisplayName()
                ? StringUtil.revertColors(this.itemMeta.getDisplayName())
                : new TranslatableComponent(this.itemStack.getTranslationKey()).toPlainText();
    }

    /**
     * Sets the lore of the item using a list of strings.
     *
     * @param lores the list of lore strings to set
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setLores(List<String> lores) {
        this.itemMeta.setLore(lores.stream().map(text -> StringUtil.format(text, null)).toList());
        return this;
    }

    /**
     * Sets the lore of the item using an array of strings.
     *
     * @param lores the array of lore strings to set
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setLores(String... lores) {
        return setLores(lores != null ? Arrays.asList(lores) : new ArrayList<>());
    }

    /**
     * Replaces occurrences of a specific substring in the item's lore.
     *
     * @param from the substring to replace
     * @param to the substring to replace with
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder replaceLore(@NotNull String from, @NotNull String to) {
        List<String> lores = this.itemMeta.hasLore() && this.itemMeta.getLore() != null
                ? this.itemMeta.getLore()
                : new ArrayList<>();
        lores.replaceAll(string -> string.replace(from, to));
        this.itemMeta.setLore(lores);
        return this;
    }

    /**
     * Adds a single lore line to the item.
     *
     * @param lore the lore string to add
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder addLore(@NotNull String lore) {
        List<String> lores = this.itemMeta.hasLore() && this.itemMeta.getLore() != null
                ? this.itemMeta.getLore()
                : new ArrayList<>();
        lores.add(StringUtil.format(lore, null));
        this.itemMeta.setLore(lores);
        return this;
    }

    /**
     * Inserts a lore line at a specified index.
     *
     * @param lore the lore string to insert
     * @param index the position to insert the lore at
     * @return the current ItemBuilder instance for method chaining
     * @throws IllegalArgumentException if the index is negative
     */
    public ItemBuilder insertLore(@NotNull String lore, int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index must be a non-negative number -> " + index);
        }
        List<String> lores = this.itemMeta.hasLore() && this.itemMeta.getLore() != null
                ? this.itemMeta.getLore()
                : new ArrayList<>();
        while (lores.size() <= index) lores.add("");
        lores.add(index, StringUtil.format(lore, null));
        this.itemMeta.setLore(lores);
        return this;
    }

    /**
     * Checks if the item has a Custom Model Data set.
     *
     * @return true if the item has Custom Model Data, false otherwise
     */
    public boolean hasCustomModelData() {
        return this.itemMeta.hasCustomModelData();
    }

    /**
     * Gets the Custom Model Data of the item.
     *
     * @return the Custom Model Data value
     * @throws IllegalArgumentException if the item does not have Custom Model Data
     */
    public int getCustomModelData() {
        if (!hasCustomModelData())
            throw new IllegalArgumentException("The item doesn't have Custom Model Data!");
        return this.itemMeta.getCustomModelData();
    }

    /**
     * Sets the Custom Model Data for the item.
     *
     * @param data the Custom Model Data to set, or null to remove it
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setCustomModelData(Integer data) {
        this.itemMeta.setCustomModelData(data);
        return this;
    }

    /**
     * Checks if the item has an enchantable value set.
     *
     * @return true if the item has an enchantable value, false otherwise
     */
    public boolean hasEnchantable() {
        return this.itemMeta.hasEnchantable();
    }

    /**
     * Gets the enchantable value of the item.
     *
     * @return the enchantable value
     */
    public int getEnchantable() {
        return this.itemMeta.getEnchantable();
    }

    /**
     * Sets the enchantable value of the item. If null is provided, the enchantable value will be removed.
     *
     * @param enchantable the enchantable value to set, or null to remove it
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setEnchantable(@Nullable Integer enchantable) {
        this.itemMeta.setEnchantable(enchantable);
        return this;
    }

    /**
     * Checks if the item has any enchantments.
     *
     * @return true if the item has enchantments, false otherwise
     */
    public boolean hasEnchants() {
        return this.itemMeta.hasEnchants();
    }

    /**
     * Checks if the item has a specific enchantment.
     *
     * @param enchantment the enchantment to check for
     * @return true if the item has the specified enchantment, false otherwise
     */
    public boolean hasEnchant(@NotNull Enchantment enchantment) {
        return this.itemMeta.hasEnchant(enchantment);
    }

    /**
     * Gets the level of a specific enchantment on the item.
     *
     * @param enchantment the enchantment to check
     * @return the level of the enchantment
     */
    public int getEnchantLevel(@NotNull Enchantment enchantment) {
        return this.itemMeta.getEnchantLevel(enchantment);
    }

    /**
     * Gets all enchantments on the item.
     *
     * @return a map of enchantments and their levels
     */
    public @NotNull Map<Enchantment, Integer> getEnchants() {
        return this.itemMeta.getEnchants();
    }

    /**
     * Adds or updates an enchantment on the item.
     * If the level is 0, the enchantment will be removed.
     *
     * @param enchantment the enchantment to add or update
     * @param level the level of the enchantment
     * @param ignoreLevelLimit whether to bypass level restrictions
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder addEnchant(@NotNull Enchantment enchantment, int level, boolean ignoreLevelLimit) {
        if (level == 0)
            return removeEnchant(enchantment);
        else
            this.itemMeta.addEnchant(enchantment, level, ignoreLevelLimit);
        return this;
    }

    /**
     * Adds or updates an enchantment on the item with level limit check.
     *
     * @param enchantment the enchantment to add or update
     * @param level the level of the enchantment
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder addEnchant(@NotNull Enchantment enchantment, int level) {
        return addEnchant(enchantment, level, false);
    }

    /**
     * Adds an enchantment to the item with level 1 and level limit check.
     *
     * @param enchantment the enchantment to add
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder addEnchant(@NotNull Enchantment enchantment) {
        return addEnchant(enchantment, 1, false);
    }

    /**
     * Removes a specific enchantment from the item.
     *
     * @param enchantment the enchantment to remove
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder removeEnchant(@NotNull Enchantment enchantment) {
        this.itemMeta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Removes all enchantments from the item.
     *
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder removeEnchants() {
        this.itemMeta.removeEnchantments();
        return this;
    }

    /**
     * Checks if an enchantment conflicts with any existing enchantments on the item.
     *
     * @param enchantment the enchantment to check
     * @return true if there is a conflict, false otherwise
     */
    public boolean hasConflictingEnchant(@NotNull Enchantment enchantment) {
        return this.itemMeta.hasConflictingEnchant(enchantment);
    }

    /**
     * Adds the specified item flags to the item.
     * If HIDE_ATTRIBUTES are added and Paper API is available, default attributes are set.
     *
     * @param itemFlags the item flags to add
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder addItemFlags(@NotNull ItemFlag... itemFlags) {
        Arrays.stream(itemFlags).toList().forEach(flag -> {
            if (flag == ItemFlag.HIDE_ATTRIBUTES && LLibrary.hasPaperAPI()) {
                if (this.itemMeta.getAttributeModifiers() == null) {
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        getType().getDefaultAttributeModifiers(slot).forEach(this.itemMeta::addAttributeModifier);
                    }
                }
            }
        });

        this.itemMeta.addItemFlags(itemFlags);
        return this;
    }

    /**
     * Removes the specified item flags from the item.
     * If HIDE_ATTRIBUTES are removed and Paper API is available, default attributes are cleared.
     *
     * @param itemFlags the item flags to remove
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder removeItemFlags(@NotNull ItemFlag... itemFlags) {
        Arrays.stream(itemFlags).toList().forEach(flag -> {
            if (flag == ItemFlag.HIDE_ATTRIBUTES && LLibrary.hasPaperAPI()) {
                Multimap<Attribute, AttributeModifier> mods = this.itemMeta.getAttributeModifiers();
                if (mods == null) {
                    return;
                }

                HashMultimap<Attribute, AttributeModifier> mods2 = HashMultimap.create();
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    mods2.putAll(getType().getDefaultAttributeModifiers(slot));
                }

                if (mods.equals(mods2)) {
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        this.itemMeta.removeAttributeModifier(slot);
                    }
                }
            }
        });

        this.itemMeta.removeItemFlags(itemFlags);
        return this;
    }

    /**
     * Hides all item flags from the item.
     * If Paper API is available and the item has no attribute modifiers, default attributes are set.
     *
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder hideAllItemFlags() {
        if (LLibrary.hasPaperAPI() && this.itemMeta.getAttributeModifiers() == null) {
            Arrays.stream(EquipmentSlot.values())
                    .iterator()
                    .forEachRemaining(slot
                            -> getType()
                            .getDefaultAttributeModifiers(slot)
                            .forEach(this.itemMeta::addAttributeModifier));
        }

        this.itemMeta.addItemFlags(ItemFlag.values());
        return this;
    }

    /**
     * Gets all item flags present on the item.
     *
     * @return a set of item flags
     */
    public @NotNull Set<ItemFlag> getItemFlags() {
        return this.itemMeta.getItemFlags();
    }

    /**
     * Checks if a specific item flag is present on the item.
     *
     * @param flag the item flag to check for
     * @return true if the item contains the specified flag, false otherwise
     */
    public boolean hasItemFlag(@NotNull ItemFlag flag) {
        return this.itemMeta.hasItemFlag(flag);
    }

    /**
     * Checks if the item has tooltips hidden.
     *
     * @return true if tooltips are hidden, false otherwise
     */
    public boolean isHideTooltip() {
        return this.itemMeta.isHideTooltip();
    }

    /**
     * Sets whether tooltips should be hidden for the item.
     *
     * @param hideTooltip true to hide tooltips, false to show them
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setHideTooltip(boolean hideTooltip) {
        this.itemMeta.setHideTooltip(hideTooltip);
        return this;
    }

    /**
     * Checks if the item has a tooltip style.
     *
     * @return true if the item has a tooltip style, false otherwise
     */
    public boolean hasTooltipStyle() {
        return this.itemMeta.hasTooltipStyle();
    }

    /**
     * Gets the tooltip style of the item.
     *
     * @return the tooltip style as a NamespacedKey, or null if not set
     */
    public @Nullable NamespacedKey getTooltipStyle() {
        return this.itemMeta.getTooltipStyle();
    }

    /**
     * Sets the tooltip style of the item.
     *
     * @param tooltipStyle the NamespacedKey for the tooltip style, or null to remove it
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setTooltipStyle(@Nullable NamespacedKey tooltipStyle) {
        this.itemMeta.setTooltipStyle(tooltipStyle);
        return this;
    }

    /**
     * Sets the tooltip style of the item using a string.
     * If the string does not contain a namespace, "minecraft" is used by default.
     *
     * @param value the string representing the tooltip style
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setTooltipStyle(@NotNull String value) {
        return setTooltipStyle(NamespaceKey.from(value));
    }

    /**
     * Checks if the item has a custom item model.
     *
     * @return true if the item has a custom item model, false otherwise
     */
    public boolean hasItemModel() {
        return this.itemMeta.hasItemModel();
    }

    /**
     * Gets the item model of the item.
     *
     * @return the item model as a NamespacedKey, or null if not set
     */
    public @Nullable NamespacedKey getItemModel() {
        return this.itemMeta.getItemModel();
    }

    /**
     * Sets the item model of the item.
     *
     * @param itemModel the NamespacedKey for the item model, or null to remove it
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setItemModel(@Nullable NamespacedKey itemModel) {
        this.itemMeta.setItemModel(itemModel);
        return this;
    }

    /**
     * Sets the item model of the item using a string.
     * If the string does not contain a namespace, "minecraft" is used by default.
     *
     * @param value the string representing the item model
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setItemModel(@NotNull String value) {
        return setItemModel(NamespaceKey.from(value));
    }

    /**
     * Checks if the item is unbreakable.
     *
     * @return true if the item is unbreakable, false otherwise
     */
    public boolean isUnbreakable() {
        return this.itemMeta.isUnbreakable();
    }

    /**
     * Sets the unbreakable status of the item.
     *
     * @param unbreakable true to make the item unbreakable, false otherwise
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Checks if the item has an enchantment glint override.
     *
     * @return true, if the item has an enchantment glint override, false otherwise
     */
    public boolean hasEnchantmentGlintOverride() {
        return this.itemMeta.hasEnchantmentGlintOverride();
    }

    /**
     * Gets the enchantment glint override status of the item.
     *
     * @return true if the glint is overridden, false if not, or null if unset
     */
    public @Nullable Boolean getEnchantmentGlintOverride() {
        return this.itemMeta.getEnchantmentGlintOverride();
    }

    /**
     * Sets or removes the enchantment glint override.
     *
     * @param override true to enable the glint, false to disable it, or null to remove the override
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setEnchantmentGlintOverride(@Nullable Boolean override) {
        this.itemMeta.setEnchantmentGlintOverride(override);
        return this;
    }

    /**
     * Checks if the item is a glider.
     *
     * @return true if the item is a glider, false otherwise
     */
    public boolean isGlider() {
        return this.itemMeta.isGlider();
    }

    /**
     * Sets the glider status of the item.
     *
     * @param glider true to make the item a glider, false otherwise
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setGlider(boolean glider) {
        this.itemMeta.setGlider(glider);
        return this;
    }

    /**
     * Checks if the item has damage resistance.
     *
     * @return true if the item has damage resistance, false otherwise
     */
    public boolean hasDamageResistant() {
        return this.itemMeta.hasDamageResistant();
    }

    /**
     * Gets the damage resistance tag of the item.
     *
     * @return the damage resistance tag as a {@link Tag<DamageType>}, or null if not set
     */
    public @Nullable Tag<DamageType> getDamageResistant() {
        return this.itemMeta.getDamageResistant();
    }

    /**
     * Sets the damage resistance tag of the item.
     *
     * @param tag the {@link Tag<DamageType>} for damage resistance, or null to remove it
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setDamageResistant(@Nullable Tag<DamageType> tag) {
        this.itemMeta.setDamageResistant(tag);
        return this;
    }

    /**
     * Checks if the item has a custom max stack size.
     *
     * @return true if the item has a custom max stack size, false otherwise
     */
    public boolean hasMaxStackSize() {
        return this.itemMeta.hasMaxStackSize();
    }

    /**
     * Gets the max stack size of the item.
     *
     * @return the max stack size, or the default stack size if not set
     */
    public int getMaxStackSize() {
        return hasMaxStackSize() ? this.itemMeta.getMaxStackSize() : getType().getMaxStackSize();
    }

    /**
     * Sets the max stack size of the item.
     *
     * @param max the new max stack size, or null to reset to the default stack size
     * @return the current ItemBuilder instance for method chaining
     * @throws IllegalArgumentException if the max stack size is not between 1 and 99
     */
    public ItemBuilder setMaxStackSize(@Nullable Integer max) {
        if (max == null) {
            this.itemMeta.setMaxStackSize(getType().getMaxStackSize());
            return this;
        }
        if (max < 1 || max > 99)
            throw new IllegalArgumentException("The max stack size must be between 1 and 99 -> " + max);
        this.itemMeta.setMaxStackSize(max);
        return this;
    }

    /**
     * Checks if the item has a custom rarity.
     *
     * @return true if the item has a custom rarity, false otherwise
     */
    public boolean hasRarity() {
        return this.itemMeta.hasRarity();
    }

    /**
     * Gets the rarity of the item.
     *
     * @return the item's rarity, or ItemRarity. COMMON if not set
     */
    public @NotNull ItemRarity getRarity() {
        return hasRarity() ? this.itemMeta.getRarity() : ItemRarity.COMMON;
    }

    /**
     * Sets the rarity of the item.
     *
     * @param rarity the ItemRarity to set, or null to remove the custom rarity
     * @return the current ItemBuilder instance for method chaining
     */
    public ItemBuilder setRarity(@Nullable ItemRarity rarity) {
        this.itemMeta.setRarity(rarity);
        return this;
    }

    /**
     * Checks if the item has a use remainder.
     *
     * @return true if the item has a use remainder, false otherwise.
     */
    public boolean hasUseRemainder() {
        return this.itemMeta.hasUseRemainder();
    }

    /**
     * Gets the item that remains after using this item, if any.
     *
     * @return the remainder ItemStack, or null if there is none.
     */
    public @Nullable ItemStack getUseRemainder() {
        return this.itemMeta.getUseRemainder();
    }

    /**
     * Sets the item that will remain after using this item.
     *
     * @param remainder the ItemStack to set as the use remainder, or null to remove it.
     * @return the current ItemBuilder instance for chaining.
     */
    public ItemBuilder setUseRemainder(@Nullable ItemStack remainder) {
        this.itemMeta.setUseRemainder(remainder);
        return this;
    }

    /**
     * Checks if the item has a use cooldown component.
     *
     * @return true if the item has a use cooldown, false otherwise.
     */
    public boolean hasUseCooldownComponent() {
        return this.itemMeta.hasUseCooldown();
    }

    /**
     * Gets the cooldown component associated with this item, or creates an empty cooldown instance.
     *
     * @return the UseCooldownComponent set on this item, or creates an empty cooldown instance.
     */
    public @NotNull UseCooldownComponent getUseCooldownComponent() {
        return this.itemMeta.getUseCooldown();
    }

    /**
     * Sets the cooldown component for this item.
     *
     * @param cooldown the UseCooldownComponent to set, or null to remove the cooldown.
     * @return the current ItemBuilder instance for chaining.
     */
    public ItemBuilder setUseCooldownComponent(@Nullable UseCooldownComponent cooldown) {
        this.itemMeta.setUseCooldown(cooldown);
        return this;
    }

    /**
     * Sets a custom use cooldown for this item.
     *
     * @param cooldownSeconds the cooldown duration in seconds.
     * @param cooldownGroup the NamespacedKey representing the cooldown group.
     * @return the current ItemBuilder instance for chaining.
     */
    public ItemBuilder setUseCooldownComponent(float cooldownSeconds, @NotNull NamespacedKey cooldownGroup) {
        return setUseCooldownComponent(new CustomUseCooldownComponent(cooldownSeconds, cooldownGroup));
    }

    /**
     * Creates and sets a custom use cooldown for this item using a string value for the namespace key.
     *
     * @param cooldownSeconds the cooldown duration in seconds.
     * @param keyValue the string representation of the NamespacedKey.
     * @return the current ItemBuilder instance for chaining.
     */
    public ItemBuilder setUseCooldownComponent(float cooldownSeconds, @NotNull String keyValue) {
        return setUseCooldownComponent(new CustomUseCooldownComponent(cooldownSeconds, NamespaceKey.from(keyValue)));
    }

    /**
     * Checks if the item has a food component.
     *
     * @return true if the item has a food component, false otherwise.
     */
    public boolean hasFoodComponent() {
        return this.itemMeta.hasFood();
    }

    /**
     * Gets the food component associated with this item, or creates an empty food instance.
     *
     * @return the FoodComponent, or creates an empty food instance.
     */
    public @NotNull FoodComponent getFoodComponent() {
        return this.itemMeta.getFood();
    }

    /**
     * Sets the food component for this item.
     *
     * @param food the FoodComponent to set, or null to remove it.
     * @return the current ItemBuilder instance for chaining.
     */
    public ItemBuilder setFoodComponent(@Nullable FoodComponent food) {
        this.itemMeta.setFood(food);
        return this;
    }

    /**
     * Creates and sets a custom food component for this item.
     *
     * @param nutrition the amount of nutrition the food provides.
     * @param saturation the saturation modifier of the food.
     * @param canEatOnFull whether the food can be eaten even if the player is not hungry.
     * @return the current ItemBuilder instance for chaining.
     */
    public ItemBuilder setFood(int nutrition, float saturation, boolean canEatOnFull) {
        return setFoodComponent(new CustomFoodComponent(nutrition, saturation, canEatOnFull));
    }

    /**
     * Checks if the item has a {@link ToolComponent} attached.
     *
     * @return true if the item has a ToolComponent, false otherwise
     */
    public boolean hasToolComponent() {
        return this.itemMeta.hasTool();
    }

    /**
     * Gets the {@link ToolComponent} of the item.
     *
     * @return the ToolComponent associated with this item
     * @throws NullPointerException if the item does not have a ToolComponent
     */
    public @NotNull ToolComponent getToolComponent() {
        return this.itemMeta.getTool();
    }

    /**
     * Sets the {@link ToolComponent} for the item.
     *
     * @param tool the ToolComponent to set, or null to remove the existing ToolComponent
     * @return this ItemBuilder instance for method chaining
     */
    public ItemBuilder setToolComponent(@Nullable ToolComponent tool) {
        this.itemMeta.setTool(tool);
        return this;
    }

    /**
     * Creates and sets a new {@link CustomToolComponent} for the item
     * with the specified default mining speed and damage per block.
     *
     * @param defaultMiningSpeed the default mining speed of the tool
     * @param damagePerBlock the amount of damage the tool takes per block broken
     * @return this ItemBuilder instance for method chaining
     */
    public ItemBuilder setToolComponent(float defaultMiningSpeed, int damagePerBlock) {
        return setToolComponent(new CustomToolComponent(defaultMiningSpeed, damagePerBlock));
    }

    /**
     * Checks whether the item has an equippable component.
     *
     * @return true if the item has an equippable component, false otherwise
     */
    public boolean hasEquippableComponent() {
        return this.itemMeta.hasEquippable();
    }

    /**
     * Gets the equippable component of the item.
     *
     * @return the equippable component, never null
     */
    public @NotNull EquippableComponent getEquippableComponent() {
        return this.itemMeta.getEquippable();
    }

    /**
     * Sets the equippable component for the item.
     *
     * @param equippable the equippable component to set, or null to remove it
     * @return the current ItemBuilder instance for chaining
     */
    public ItemBuilder setEquippableComponent(@Nullable EquippableComponent equippable) {
        this.itemMeta.setEquippable(equippable);
        return this;
    }

    /**
     * Creates and sets a custom equippable component for the item using the specified equipment slot.
     *
     * @param equipmentSlot the slot where the item can be equipped
     * @return the current ItemBuilder instance for chaining
     */
    public ItemBuilder setEquippableComponent(EquipmentSlot equipmentSlot) {
        return setEquippableComponent(new CustomEquippableComponent(equipmentSlot));
    }

    /**
     * Checks if the item has a JukeboxPlayableComponent.
     *
     * @return true if the item has a JukeboxPlayableComponent, false otherwise
     */
    public boolean hasJukeboxPlayableComponent() {
        return this.itemMeta.hasJukeboxPlayable();
    }

    /**
     * Gets the JukeboxPlayableComponent of the item.
     *
     * @return the JukeboxPlayableComponent, or null if not present
     */
    public @Nullable JukeboxPlayableComponent getJukeboxPlayableComponent() {
        return this.itemMeta.getJukeboxPlayable();
    }

    /**
     * Sets the JukeboxPlayableComponent of the item.
     *
     * @param jukeboxPlayable the JukeboxPlayableComponent to set, or null to remove it
     * @return the current ItemBuilder instance for chaining
     */
    public ItemBuilder setJukeboxPlayableComponent(@Nullable JukeboxPlayableComponent jukeboxPlayable) {
        this.itemMeta.setJukeboxPlayable(jukeboxPlayable);
        return this;
    }

    /**
     * Creates and sets a custom JukeboxPlayableComponent using a song key, song, and tooltip visibility.
     *
     * @param songKey      the key identifying the song
     * @param jukeboxSong  the JukeboxSong to be played
     * @param showInTooltip whether the song should be shown in the item's tooltip
     * @return the current ItemBuilder instance for chaining
     */
    public ItemBuilder setJukeboxPlayableComponent(NamespacedKey songKey, JukeboxSong jukeboxSong, boolean showInTooltip) {
        return setJukeboxPlayableComponent(new CustomJukeboxPlayableComponent(songKey, jukeboxSong, showInTooltip));
    }

    /**
     * Creates and sets a custom JukeboxPlayableComponent using a key value string, song, and tooltip visibility.
     *
     * @param keyValue     the string representation of the song's key (to be converted to a NamespacedKey)
     * @param jukeboxSong  the JukeboxSong to be played
     * @param showInTooltip whether the song should be shown in the item's tooltip
     * @return the current ItemBuilder instance for chaining
     */
    public ItemBuilder setJukeboxPlayableComponent(String keyValue, JukeboxSong jukeboxSong, boolean showInTooltip) {
        return setJukeboxPlayableComponent(new CustomJukeboxPlayableComponent(NamespaceKey.from(keyValue), jukeboxSong, showInTooltip));
    }

    /**
     * Checks if the ItemMeta has any attribute modifiers.
     *
     * @return true if attribute modifiers exist, false otherwise.
     */
    public boolean hasAttributeModifiers() {
        return this.itemMeta.hasAttributeModifiers();
    }

    /**
     * Return an immutable copy of all Attributes and their modifiers in this ItemMeta.
     * Returns null if none exist.
     *
     * @return A Multimap containing attributes and their respective modifiers, or null if none exist.
     */
    public @Nullable Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.itemMeta.getAttributeModifiers();
    }

    /**
     * Return an immutable copy of all {@link Attribute}s and their {@link AttributeModifier}s for a given {@link EquipmentSlot}.
     * Any {@link AttributeModifier} that does have a given {@link EquipmentSlot} will be returned. This is because {@link AttributeModifier}s without a slot are active in any slot.
     * If there are no attributes set for the given slot, an empty map will be returned.
     *
     * @param slot The EquipmentSlot to filter attribute modifiers for.
     * @return A non-null Multimap containing attributes and their modifiers for the specified slot.
     */
    public @NotNull Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot) {
        return this.itemMeta.getAttributeModifiers(slot);
    }

    /**
     * Return an immutable copy of all {@link AttributeModifier}s for a given {@link Attribute}
     *
     * @param attribute The Attribute to retrieve modifiers for.
     * @return A collection of AttributeModifiers associated with the given attribute, or null if none exist.
     */
    public @Nullable Collection<AttributeModifier> getAttributeModifiers(@NotNull Attribute attribute) {
        return this.itemMeta.getAttributeModifiers(attribute);
    }

    /**
     * Add an Attribute and it's Modifier.
     * AttributeModifiers can now support {@link EquipmentSlot}s.
     * If not set, the {@link AttributeModifier} will be active in ALL slots.
     * Two {@link AttributeModifier}s that have the same {@link UUID} cannot exist on the same Attribute.
     *
     * @param attribute The Attribute to modify.
     * @param modifier The AttributeModifier specifying the modification.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        this.itemMeta.addAttributeModifier(attribute, modifier);
        return this;
    }

    /**
     * Set all {@link Attribute}s and their {@link AttributeModifier}s.
     * To clear all currently set Attributes and AttributeModifiers use null or an empty Multimap.
     * If not null nor empty, this will filter all entries that are not null and add them to the ItemStack.
     *
     * @param attributeModifiers The new Multimap of attributes and their modifiers, or null to clear all modifiers.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
        this.itemMeta.setAttributeModifiers(attributeModifiers);
        return this;
    }

    /**
     * Remove all {@link AttributeModifier}s associated with the given {@link Attribute}.
     *
     * @param attribute The Attribute to remove all modifiers for.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder removeAttributeModifier(@NotNull Attribute attribute) {
        this.itemMeta.removeAttributeModifier(attribute);
        return this;
    }

    /**
     * Remove all {@link Attribute}s and {@link AttributeModifier}s for a given {@link EquipmentSlot}.
     * If the given {@link EquipmentSlot} is null, this will remove all {@link AttributeModifier}s that do not have an EquipmentSlot set.
     *
     * @param slot The EquipmentSlot to clear all attributes and their modifiers for.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder removeAttributeModifier(@NotNull EquipmentSlot slot) {
        this.itemMeta.removeAttributeModifier(slot);
        return this;
    }

    /**
     * Remove a specific {@link Attribute} and {@link AttributeModifier}. AttributeModifiers are matched according to their {@link UUID}.
     *
     * @param attribute The Attribute to remove the modifier from.
     * @param modifier The specific AttributeModifier to remove.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder removeAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        this.itemMeta.removeAttributeModifier(attribute, modifier);
        return this;
    }

    public @NotNull ItemMeta getItemMeta() {
        return this.itemMeta;
    }
}