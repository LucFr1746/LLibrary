package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The {@code CrossbowBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link CrossbowMeta} of an {@link ItemStack}.
 * This class allows setting, adding, and retrieving charged projectiles.
 */
public class CrossbowBuilder extends ItemBuilder {

    private final CrossbowMeta crossbowMeta;

    /**
     * Constructs a {@code CrossbowBuilder} with a new crossbow item.
     */
    public CrossbowBuilder() {
        super(Material.CROSSBOW);
        if (!(getItemMeta() instanceof CrossbowMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of CrossbowMeta");
        }
        this.crossbowMeta = meta;
    }

    /**
     * Constructs a {@code CrossbowBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link CrossbowMeta}
     */
    public CrossbowBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof CrossbowMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of CrossbowMeta");
        }
        this.crossbowMeta = meta;
    }

    /**
     * Adds a charged projectile to the crossbow.
     *
     * @param item the projectile to add
     * @return this {@code CrossbowBuilder} instance for method chaining
     */
    public CrossbowBuilder addChargedProjectile(@NotNull ItemStack item) {
        crossbowMeta.addChargedProjectile(item);
        return this;
    }

    /**
     * Sets the list of charged projectiles for the crossbow.
     *
     * @param projectiles the list of projectiles to set
     * @return this {@code CrossbowBuilder} instance for method chaining
     */
    public CrossbowBuilder setChargedProjectiles(@NotNull List<ItemStack> projectiles) {
        crossbowMeta.setChargedProjectiles(projectiles);
        return this;
    }

    /**
     * Gets the list of charged projectiles from the crossbow.
     *
     * @return an immutable list of charged projectiles
     */
    public List<ItemStack> getChargedProjectiles() {
        return crossbowMeta.getChargedProjectiles();
    }

    /**
     * Checks if the crossbow has any charged projectiles.
     *
     * @return true if the crossbow has charged projectiles, false otherwise
     */
    public boolean hasChargedProjectiles() {
        return crossbowMeta.hasChargedProjectiles();
    }
}