package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The {@code EnchantmentStorageBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link EnchantmentStorageMeta} of an {@link ItemStack}.
 * This class allows adding, removing, and checking stored enchantments in enchanted books.
 */
public class EnchantmentStorageBuilder extends ItemBuilder {

    private final EnchantmentStorageMeta enchantmentStorageMeta;

    /**
     * Constructs a {@code EnchantmentStorageBuilder} with a new enchanted book.
     */
    public EnchantmentStorageBuilder() {
        super(Material.ENCHANTED_BOOK);
        if (!(getItemMeta() instanceof EnchantmentStorageMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of EnchantmentStorageMeta");
        }
        this.enchantmentStorageMeta = meta;
    }

    /**
     * Constructs a {@code EnchantmentStorageBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link EnchantmentStorageMeta}
     */
    public EnchantmentStorageBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof EnchantmentStorageMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of EnchantmentStorageMeta");
        }
        this.enchantmentStorageMeta = meta;
    }

    /**
     * Adds a stored enchantment to the item.
     *
     * @param enchantment           the enchantment to add
     * @param level                 the level of the enchantment
     * @param ignoreLevelRestriction whether to ignore level restrictions
     * @return this {@code EnchantmentStorageBuilder} instance for method chaining
     */
    public EnchantmentStorageBuilder addStoredEnchant(@NotNull Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        enchantmentStorageMeta.addStoredEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    /**
     * Removes a stored enchantment from the item.
     *
     * @param enchantment the enchantment to remove
     * @return this {@code EnchantmentStorageBuilder} instance for method chaining
     */
    public EnchantmentStorageBuilder removeStoredEnchant(@NotNull Enchantment enchantment) {
        enchantmentStorageMeta.removeStoredEnchant(enchantment);
        return this;
    }

    /**
     * Gets the level of a stored enchantment.
     *
     * @param enchantment the enchantment to check
     * @return the level of the enchantment, or 0 if not present
     */
    public int getStoredEnchantLevel(@NotNull Enchantment enchantment) {
        return enchantmentStorageMeta.getStoredEnchantLevel(enchantment);
    }

    /**
     * Gets all stored enchantments on the item.
     *
     * @return a map of enchantments and their levels
     */
    public Map<Enchantment, Integer> getStoredEnchants() {
        return enchantmentStorageMeta.getStoredEnchants();
    }

    /**
     * Checks if the item has a specific stored enchantment.
     *
     * @param enchantment the enchantment to check for
     * @return true if the enchantment is stored, false otherwise
     */
    public boolean hasStoredEnchant(@NotNull Enchantment enchantment) {
        return enchantmentStorageMeta.hasStoredEnchant(enchantment);
    }

    /**
     * Checks if the item has any stored enchantments.
     *
     * @return true if there are stored enchantments, false otherwise
     */
    public boolean hasStoredEnchants() {
        return enchantmentStorageMeta.hasStoredEnchants();
    }

    /**
     * Checks if the specified enchantment conflicts with any stored enchantments.
     *
     * @param enchantment the enchantment to check for conflicts
     * @return true if there is a conflict, false otherwise
     */
    public boolean hasConflictingStoredEnchant(@NotNull Enchantment enchantment) {
        return enchantmentStorageMeta.hasConflictingStoredEnchant(enchantment);
    }
}