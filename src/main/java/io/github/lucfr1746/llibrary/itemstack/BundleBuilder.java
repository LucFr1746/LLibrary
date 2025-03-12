package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The {@code BundleBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link BundleMeta} of an {@link ItemStack}.
 * This class allows adding, retrieving, and setting items within a bundle.
 */
public class BundleBuilder extends ItemBuilder {

    private final BundleMeta bundleMeta;

    /**
     * Constructs a {@code BundleBuilder} with a new bundle item.
     */
    public BundleBuilder() {
        super(Material.BUNDLE);
        if (!(getItemMeta() instanceof BundleMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of BundleMeta");
        }
        this.bundleMeta = meta;
    }

    /**
     * Constructs a {@code BundleBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link BundleMeta}
     */
    public BundleBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof BundleMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of BundleMeta");
        }
        this.bundleMeta = meta;
    }

    /**
     * Adds an item to the bundle.
     *
     * @param item the item to add
     * @return this {@code BundleBuilder} instance for method chaining
     */
    public BundleBuilder addItem(@NotNull ItemStack item) {
        bundleMeta.addItem(item);
        return this;
    }

    /**
     * Sets the items stored in the bundle.
     *
     * @param items the list of items to store in the bundle
     * @return this {@code BundleBuilder} instance for method chaining
     */
    public BundleBuilder setItems(@NotNull List<ItemStack> items) {
        bundleMeta.setItems(items);
        return this;
    }

    /**
     * Retrieves the items stored in the bundle.
     *
     * @return a list of items in the bundle
     */
    public List<ItemStack> getItems() {
        return bundleMeta.getItems();
    }

    /**
     * Checks if the bundle contains any items.
     *
     * @return {@code true} if the bundle has items, {@code false} otherwise
     */
    public boolean hasItems() {
        return bundleMeta.hasItems();
    }
}