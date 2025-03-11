package io.github.lucfr1746.llibrary.itemstack.component;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a custom implementation of the {@link EquippableComponent} interface.
 * This class allows customizing equipment behavior, such as defining equip slots,
 * sounds, allowed entities, and other properties.
 */
public class CustomEquippableComponent implements EquippableComponent {

    private EquipmentSlot slot;
    private Sound equipSound;
    private NamespacedKey model;
    private NamespacedKey cameraOverlay;
    private final Set<EntityType> allowedEntities = new HashSet<>();
    private boolean dispensable;
    private boolean swappable;
    private boolean damageOnHurt;

    /**
     * Constructs a new CustomEquippableComponent with the given equipment slot and default values.
     *
     * @param equipmentSlot the slot where the equipment can be placed
     */
    public CustomEquippableComponent(EquipmentSlot equipmentSlot) {
        this.slot = equipmentSlot;
        this.equipSound = Sound.ITEM_ARMOR_EQUIP_GENERIC;
        this.model = null;
        this.cameraOverlay = null;
        this.dispensable = true;
        this.swappable = true;
        this.damageOnHurt = true;
    }

    /**
     * Gets the slot the item can be equipped to.
     *
     * @return the equipment slot
     */
    @NotNull
    @Override
    public EquipmentSlot getSlot() {
        return this.slot;
    }

    /**
     * Sets the slot the item can be equipped to.
     *
     * @param slot the new equipment slot
     */
    @Override
    public void setSlot(@NotNull EquipmentSlot slot) {
        this.slot = slot;
    }

    /**
     * Gets the sound to play when the item is equipped.
     *
     * @return the equip sound, or null if none is set
     */
    @Nullable
    @Override
    public Sound getEquipSound() {
        return this.equipSound;
    }

    /**
     * Sets the sound to play when the item is equipped.
     *
     * @param sound the new equipped sound, or null to remove the sound
     */
    @Override
    public void setEquipSound(@Nullable Sound sound) {
        this.equipSound = sound;
    }

    /**
     * Gets the key of the model to use when equipped.
     *
     * @return the model key, or null if none is set
     */
    @Nullable
    @Override
    public NamespacedKey getModel() {
        return this.model;
    }

    /**
     * Sets the key of the model to use when equipped.
     *
     * @param key the new model key, or null to remove the model
     */
    @Override
    public void setModel(@Nullable NamespacedKey key) {
        this.model = key;
    }

    /**
     * Gets the key of the camera overlay to use when equipped.
     *
     * @return the camera overlay key, or null if none is set
     */
    @Nullable
    @Override
    public NamespacedKey getCameraOverlay() {
        return this.cameraOverlay;
    }

    /**
     * Sets the key of the camera overlay to use when equipped.
     *
     * @param key the new camera overlay key, or null to remove the overlay
     */
    @Override
    public void setCameraOverlay(@Nullable NamespacedKey key) {
        this.cameraOverlay = key;
    }

    /**
     * Gets the entities which can equip this item.
     *
     * @return a collection of allowed entity types
     */
    @Nullable
    @Override
    public Collection<EntityType> getAllowedEntities() {
        return Collections.unmodifiableSet(this.allowedEntities);
    }

    /**
     * Sets the entity which can equip this item.
     *
     * @param entity the allowed entity, or null to clear the list
     */
    @Override
    public void setAllowedEntities(@Nullable EntityType entity) {
        this.allowedEntities.clear();
        if (entity != null) {
            this.allowedEntities.add(entity);
        }
    }

    /**
     * Sets the entities which can equip this item.
     *
     * @param entities a collection of allowed entities, or null to clear the list
     */
    @Override
    public void setAllowedEntities(@Nullable Collection<EntityType> entities) {
        this.allowedEntities.clear();
        if (entities != null) {
            this.allowedEntities.addAll(entities);
        }
    }

    /**
     * Set the entity types (represented as an entity {@link Tag}) which can equip this item.
     *
     * @param tag the tag containing allowed entities, or null to clear the list
     */
    @Override
    public void setAllowedEntities(@Nullable Tag<EntityType> tag) {
        this.allowedEntities.clear();
        if (tag != null) {
            this.allowedEntities.addAll(tag.getValues());
        }
    }

    /**
     * Gets whether the item can be equipped by a dispenser.
     *
     * @return true if dispensable, false otherwise
     */
    @Override
    public boolean isDispensable() {
        return this.dispensable;
    }

    /**
     * Sets whether the item can be equipped by a dispenser.
     *
     * @param dispensable true to make it dispensable, false otherwise
     */
    @Override
    public void setDispensable(boolean dispensable) {
        this.dispensable = dispensable;
    }

    /**
     * Gets if the item is swappable by right-clicking.
     *
     * @return true if swappable, false otherwise
     */
    @Override
    public boolean isSwappable() {
        return this.swappable;
    }

    /**
     * Sets if the item is swappable by right-clicking.
     *
     * @param swappable true to make it swappable, false otherwise
     */
    @Override
    public void setSwappable(boolean swappable) {
        this.swappable = swappable;
    }

    /**
     * Checks if the equipment takes damage when the wearer is hurt.
     *
     * @return true if it takes damage, false otherwise
     */
    @Override
    public boolean isDamageOnHurt() {
        return this.damageOnHurt;
    }

    /**
     * Sets whether the equipment takes damage when the wearer is hurt.
     *
     * @param damage true to enable damage on hurt, false otherwise
     */
    @Override
    public void setDamageOnHurt(boolean damage) {
        this.damageOnHurt = damage;
    }

    /**
     * Serializes this component into a map of key-value pairs.
     *
     * @return a map containing the serialized data
     */
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "slot", this.slot.name(),
                "equipSound", this.equipSound != null ? this.equipSound.name() : null,
                "model", this.model != null ? this.model.toString() : null,
                "cameraOverlay", this.cameraOverlay != null ? this.cameraOverlay.toString() : null,
                "allowedEntities", this.allowedEntities.stream().map(EntityType::name).toList(),
                "dispensable", this.dispensable,
                "swappable", this.swappable,
                "damageOnHurt", this.damageOnHurt
        );
    }
}
