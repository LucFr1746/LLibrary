package io.github.lucfr1746.llibrary.itemstack.helper;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code EntitySnapshotBuilder} class provides a way to create and modify
 * {@link EntitySnapshot} instances before using them.
 */
public class EntitySnapshotBuilder {

    private final EntityType entityType;
    private Location location;

    /**
     * Creates an {@code EntitySnapshotBuilder} for the specified entity type.
     *
     * @param entityType The type of entity to snapshot.
     */
    public EntitySnapshotBuilder(@NotNull EntityType entityType) {
        this.entityType = entityType;
    }

    /**
     * Sets the location where the entity will be spawned.
     *
     * @param location The spawn location.
     * @return This {@code EntitySnapshotBuilder} instance for chaining.
     */
    public EntitySnapshotBuilder setLocation(@NotNull Location location) {
        this.location = location;
        return this;
    }

    /**
     * Creates an {@link EntitySnapshot} from an existing entity.
     *
     * @param entity The entity to snapshot.
     * @return An immutable {@link EntitySnapshot} of the given entity.
     */
    public static EntitySnapshot fromEntity(@NotNull Entity entity) {
        return entity.createSnapshot();
    }

    /**
     * Builds an {@link EntitySnapshot} based on the provided data.
     *
     * @param world The world where the entity will be spawned.
     * @return The created {@link EntitySnapshot}.
     * @throws IllegalStateException If location or world is not set.
     */
    public EntitySnapshot build(@NotNull World world) {
        Entity entity = world.spawnEntity(location, entityType);
        return entity.createSnapshot();
    }
}