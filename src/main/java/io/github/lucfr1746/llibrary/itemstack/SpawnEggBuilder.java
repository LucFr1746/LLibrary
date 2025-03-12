package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * The {@code SpawnEggBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link SpawnEggMeta} of an {@link ItemStack}.
 * This includes setting custom spawned entities and manipulating persistent data.
 */
public class SpawnEggBuilder extends ItemBuilder {

    private final SpawnEggMeta spawnEggMeta;

    /**
     * Constructs a {@code SpawnEggBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item is not a spawn egg
     */
    public SpawnEggBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof SpawnEggMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of SpawnEggMeta");
        }
        this.spawnEggMeta = meta;
    }

    /**
     * Constructs a {@code SpawnEggBuilder} with the specified {@link EntityType}.
     * Automatically select the appropriate spawn egg material based on the entity type.
     *
     * @param entityType the type of entity the spawn egg will spawn
     * @param amount     the quantity of spawn eggs in the stack
     * @throws IllegalArgumentException if no spawn egg material is found for the entity type
     */
    public SpawnEggBuilder(@NotNull EntityType entityType, int amount) {
        super(getSpawnEggMaterial(entityType), amount);
        if (!(getItemMeta() instanceof SpawnEggMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of SpawnEggMeta");
        }
        this.spawnEggMeta = meta;
    }

    /**
     * Sets the entity snapshot for the spawn egg.
     * The spawned entity will retain the state defined by the provided entity's snapshot.
     *
     * @param entity the entity to create a snapshot from
     * @return this {@code SpawnEggBuilder} instance for chaining
     */
    public SpawnEggBuilder setSpawnedEntity(@NotNull Entity entity) {
        spawnEggMeta.setSpawnedEntity(Objects.requireNonNull(entity.createSnapshot()));
        return this;
    }

    /**
     * Gets the entity type associated with the spawn egg.
     *
     * @return the {@link EntityType} the spawn egg will spawn, or null if not set
     */
    @Nullable
    public EntityType getSpawnedEntityType() {
        return Objects.requireNonNull(spawnEggMeta.getSpawnedEntity()).getEntityType();
    }

    /**
     * Modifies the persistent data container of the spawn egg.
     * This allows adding custom NBT data or modifying existing tags.
     *
     * @param action a {@link Consumer} that accepts the spawn egg's {@link PersistentDataContainer}
     * @return this {@code SpawnEggBuilder} instance for chaining
     */
    public SpawnEggBuilder modifyPersistentData(@NotNull Consumer<PersistentDataContainer> action) {
        action.accept(spawnEggMeta.getPersistentDataContainer());
        return this;
    }

    /**
     * Gets the corresponding {@link Material} for the given {@link EntityType}'s spawn egg.
     *
     * @param entityType the type of entity
     * @return the matching spawn egg material
     * @throws IllegalArgumentException if the entity type has no corresponding spawn egg
     */
    private static Material getSpawnEggMaterial(EntityType entityType) {
        try {
            return Material.valueOf(entityType.name() + "_SPAWN_EGG");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No spawn egg material found for entity type: " + entityType);
        }
    }
}