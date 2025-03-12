package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code RepairableBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link Repairable} meta of an {@link ItemStack}.
 * This class allows setting repair costs for items repairable at an anvil.
 */
public class RepairableBuilder extends ItemBuilder {

    private final Repairable repairableMeta;

    /**
     * Constructs a {@code RepairableBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link Repairable}
     */
    public RepairableBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        ItemMeta meta = getItemMeta();
        if (!(meta instanceof Repairable repairable)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of Repairable");
        }
        this.repairableMeta = repairable;
    }

    /**
     * Constructs a {@code RepairableBuilder} with the specified {@link Material} and amount.
     *
     * @param material the material for the item stack
     * @param amount   the quantity of items in the stack
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link Repairable}
     */
    public RepairableBuilder(@NotNull Material material, int amount) {
        super(material, amount);
        ItemMeta meta = getItemMeta();
        if (!(meta instanceof Repairable repairable)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of Repairable");
        }
        this.repairableMeta = repairable;
    }

    /**
     * Sets the repair cost for the item.
     *
     * @param cost the repair cost (penalty)
     * @return this {@code RepairableBuilder} instance for chaining
     */
    public RepairableBuilder setRepairCost(int cost) {
        repairableMeta.setRepairCost(cost);
        return this;
    }

    /**
     * Gets the repair cost of the item.
     *
     * @return the repair cost
     */
    public int getRepairCost() {
        return repairableMeta.getRepairCost();
    }

    /**
     * Checks if the item has a repair cost set.
     *
     * @return true if the item has a repair cost, false otherwise
     */
    public boolean hasRepairCost() {
        return repairableMeta.hasRepairCost();
    }
}