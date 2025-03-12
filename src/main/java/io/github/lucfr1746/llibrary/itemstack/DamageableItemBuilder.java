package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code DamageableBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link Damageable} items in an {@link ItemStack}.
 * This class allows setting, retrieving, and checking damage values.
 */
public class DamageableItemBuilder extends ItemBuilder {

    private final Damageable damageableMeta;

    /**
     * Constructs a {@code DamageableItemBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link Damageable}
     */
    public DamageableItemBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof Damageable meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of Damageable");
        }
        this.damageableMeta = meta;
    }

    /**
     * Constructs a {@code DamageableItemBuilder} with the specified {@link Material} and amount.
     *
     * @param material the material for the item stack
     * @param amount   the quantity of items in the stack
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link Damageable}
     */
    public DamageableItemBuilder(@NotNull Material material, int amount) {
        super(material, amount);
        if (!(getItemMeta() instanceof Damageable meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of Damageable");
        }
        this.damageableMeta = meta;
    }

    /**
     * Constructs a {@code DamageableItemBuilder} with the specified {@link Material}.
     *
     * @param material the material for the item stack
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link Damageable}
     */
    public DamageableItemBuilder(@NotNull Material material) {
        super(material);
        if (!(getItemMeta() instanceof Damageable meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of Damageable");
        }
        this.damageableMeta = meta;
    }

    /**
     * Gets the current damage value of the item.
     *
     * @return the current damage value
     */
    public int getDamage() {
        return damageableMeta.getDamage();
    }

    /**
     * Sets the damage value of the item.
     *
     * @param damage the damage value to set
     * @return this {@code DamageableItemBuilder} instance for method chaining
     */
    public DamageableItemBuilder setDamage(int damage) {
        damageableMeta.setDamage(damage);
        return this;
    }

    /**
     * Gets the maximum amount of damage this item can take.
     *
     * @return the maximum damage value
     */
    public int getMaxDurability() {
        return damageableMeta.getMaxDamage();
    }

    /**
     * Sets the maximum damage value for the item.
     *
     * @param maxDurability the maximum damage value to set
     * @return this {@code DamageableItemBuilder} instance for method chaining
     */
    public DamageableItemBuilder setMaxDurability(int maxDurability) {
        damageableMeta.setMaxDamage(maxDurability);
        return this;
    }

    /**
     * Checks if the item has a damage value.
     *
     * @return true if the item has damage, false otherwise
     */
    public boolean hasDamage() {
        return damageableMeta.hasDamage();
    }

    /**
     * Checks if the item has a maximum damage value.
     *
     * @return true if the item has a max damage value, false otherwise
     */
    public boolean hasMaxDurability() {
        return damageableMeta.hasMaxDamage();
    }
}