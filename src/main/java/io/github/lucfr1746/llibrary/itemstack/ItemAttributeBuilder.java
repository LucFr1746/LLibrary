package io.github.lucfr1746.llibrary.itemstack;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A builder class for managing and modifying attribute modifiers on an {@link ItemStack}.
 * This builder provides a fluent API for handling attributes and ensures the immutability of the original {@link ItemStack}.
 *
 * @param itemStack The {@link ItemStack} to which attribute modifiers will be applied.
 */
public record ItemAttributeBuilder(@NotNull ItemStack itemStack) {

    /**
     * Constructs an {@code ItemAttributeBuilder} with the specified {@link ItemStack}.
     * The {@code ItemStack} is cloned to prevent modifications to the original instance.
     *
     * @param itemStack the {@link ItemStack} to base the builder on (must not be null).
     * @throws IllegalArgumentException if {@code itemStack} is null.
     */
    public ItemAttributeBuilder {
        itemStack = itemStack.clone();
    }

    /**
     * Adds an attribute modifier to the {@link ItemStack}.
     *
     * @param attribute the attribute to add.
     * @param modifier  the attribute modifier to add.
     * @return a new {@code ItemAttributeBuilder} with the updated {@link ItemStack}.
     * @throws IllegalStateException if the {@link ItemMeta} is null.
     */
    public ItemAttributeBuilder addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        ItemMeta meta = getItemMeta();
        meta.addAttributeModifier(attribute, modifier);
        ItemStack updatedStack = itemStack.clone();
        updatedStack.setItemMeta(meta);
        return new ItemAttributeBuilder(updatedStack);
    }

    /**
     * Adds an attribute modifier with detailed parameters.
     *
     * @param attribute the attribute to add.
     * @param amount    the value of the modifier.
     * @param operation the operation (defaults to {@link AttributeModifier.Operation#ADD_NUMBER} if null).
     * @param slot      the equipment slot this modifier applies to (nullable for global effect).
     * @return a new {@code ItemAttributeBuilder} with the updated {@link ItemStack}.
     */
    public ItemAttributeBuilder addAttributeModifier(
            @NotNull Attribute attribute,
            double amount,
            @Nullable AttributeModifier.Operation operation,
            @Nullable EquipmentSlotGroup slot
    ) {
        if (operation == null) {
            operation = AttributeModifier.Operation.ADD_NUMBER;
        }
        if (slot == null) {
            slot = EquipmentSlotGroup.ANY;
        }
        AttributeModifier modifier = new AttributeModifier(attribute.getKeyOrThrow(), amount, operation, slot);
        return addAttributeModifier(attribute, modifier);
    }

    /**
     * Adds an attribute modifier with the specified attribute, amount, and operation.
     *
     * @param attribute the attribute to modify. Must not be {@code null}.
     * @param amount    the value of the modifier.
     * @param operation the operation to apply to the attribute. Must not be {@code null}.
     * @return a new {@code ItemAttributeBuilder} with the updated {@link ItemStack}.
     * @throws NullPointerException if {@code attribute} or {@code operation} is {@code null}.
     */
    public ItemAttributeBuilder addAttributeModifier(
            @NotNull Attribute attribute,
            double amount,
            @NotNull AttributeModifier.Operation operation
    ) {
        return addAttributeModifier(attribute, amount, operation, null);
    }

    /**
     * Adds an attribute modifier with the specified attribute, amount, and equipment slot.
     *
     * @param attribute the attribute to modify. Must not be {@code null}.
     * @param amount    the value of the modifier.
     * @param slot      the equipment slot this modifier applies to. Must not be {@code null}.
     * @return a new {@code ItemAttributeBuilder} with the updated {@link ItemStack}.
     * @throws NullPointerException if {@code attribute} or {@code slot} is {@code null}.
     */
    public ItemAttributeBuilder addAttributeModifier(
            @NotNull Attribute attribute,
            double amount,
            @NotNull EquipmentSlotGroup slot
    ) {
        return addAttributeModifier(attribute, amount, null, slot);
    }

    /**
     * Builds and returns the modified {@link ItemStack}.
     *
     * @return the modified {@link ItemStack}.
     */
    public ItemStack build() {
        return itemStack.clone();
    }

    /**
     * Retrieves all attribute modifiers from the {@link ItemStack}.
     *
     * @return a {@link Multimap} of attributes and their associated modifiers, or an empty map if none exist.
     */
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        ItemMeta meta = getItemMeta();
        return meta.getAttributeModifiers() != null
                ? meta.getAttributeModifiers()
                : ArrayListMultimap.create();
    }

    /**
     * Retrieves attribute modifiers for a specific {@link Attribute}.
     *
     * @param attribute the attribute to query.
     * @return a collection of {@link AttributeModifier}, or an empty collection if none are found.
     */
    public Collection<AttributeModifier> getAttributeModifiers(@NotNull Attribute attribute) {
        return getAttributeModifiers().get(attribute);
    }

    /**
     * Retrieves all attribute modifiers applied to a specific equipment slot.
     *
     * @param slot the equipment slot to query. Must not be {@code null}.
     * @return a {@link Multimap} of attributes and their associated modifiers for the specified slot,
     *         or an empty map if none exist.
     * @throws NullPointerException if {@code slot} is {@code null}.
     */
    public Multimap<Attribute,AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot) {
        ItemMeta meta = getItemMeta();
        return meta.getAttributeModifiers() != null
                ? meta.getAttributeModifiers(slot)
                : ArrayListMultimap.create();
    }

    /**
     * Checks if the {@link ItemStack} has any attribute modifiers.
     *
     * @return true if modifiers exist, false otherwise.
     */
    public boolean hasAttributeModifiers() {
        return !getAttributeModifiers().isEmpty();
    }

    /**
     * Removes a specific attribute modifier for the given attribute from the {@link ItemStack}.
     *
     * @param attribute        the attribute to remove the modifier from. Must not be {@code null}.
     * @param attributeModifier the specific modifier to remove. Can be {@code null} to remove all modifiers
     *                          for the given attribute.
     * @return a new {@code ItemAttributeBuilder} with the updated {@link ItemStack}.
     * @throws NullPointerException if {@code attribute} is {@code null}.
     */
    public ItemAttributeBuilder removeAttributeModifier(@NotNull Attribute attribute, AttributeModifier attributeModifier) {
        ItemMeta meta = getItemMeta();
        if (attributeModifier == null) {
            meta.removeAttributeModifier(attribute);
        } else {
            meta.removeAttributeModifier(attribute, attributeModifier);
        }
        ItemStack updatedStack = itemStack.clone();
        updatedStack.setItemMeta(meta);
        return new ItemAttributeBuilder(updatedStack);
    }

    /**
     * Removes all modifiers for a specific {@link Attribute}.
     *
     * @param attribute the attribute to remove modifiers from.
     * @return a new {@code ItemAttributeBuilder} with the updated {@link ItemStack}.
     */
    public ItemAttributeBuilder removeAttributeModifier(@NotNull Attribute attribute) {
        return removeAttributeModifier(attribute, null);
    }

    /**
     * Removes all modifiers applied to a specific {@link EquipmentSlot}.
     *
     * @param slot the equipment slot to clear.
     * @return a new {@code ItemAttributeBuilder} with the updated {@link ItemStack}.
     */
    public ItemAttributeBuilder removeAttributeModifier(@NotNull EquipmentSlot slot) {
        ItemMeta meta = getItemMeta();
        meta.removeAttributeModifier(slot);
        ItemStack updatedStack = itemStack.clone();
        updatedStack.setItemMeta(meta);
        return new ItemAttributeBuilder(updatedStack);
    }

    /**
     * Sets custom attribute modifiers for the {@link ItemStack}.
     *
     * @param modifiers the {@link Multimap} of attributes and their modifiers.
     * @return a new {@code ItemAttributeBuilder} with the updated {@link ItemStack}.
     * @throws IllegalArgumentException if {@code modifiers} is null.
     */
    public ItemAttributeBuilder setAttributeModifiers(@NotNull Multimap<Attribute, AttributeModifier> modifiers) {
        ItemMeta meta = getItemMeta();
        meta.setAttributeModifiers(modifiers);
        ItemStack updatedStack = itemStack.clone();
        updatedStack.setItemMeta(meta);
        return new ItemAttributeBuilder(updatedStack);
    }

    /**
     * Converts the {@code AttributeBuilder} to an {@link ItemBuilder}.
     *
     * @return an {@link ItemBuilder} initialized with the current item stack.
     */
    public ItemBuilder toItemBuilder() {
        return new ItemBuilder(this.itemStack);
    }

    /**
     * Retrieves the {@link ItemMeta} from the {@link ItemStack}.
     *
     * @return the {@link ItemMeta} instance.
     * @throws IllegalStateException if the {@link ItemMeta} is null.
     */
    private ItemMeta getItemMeta() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            throw new IllegalStateException("Failed to retrieve item meta.");
        }
        return meta;
    }
}
