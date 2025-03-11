package io.github.lucfr1746.llibrary.itemstack.component;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.components.UseCooldownComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents a custom implementation of the {@link UseCooldownComponent} interface.
 * This class allows defining a custom cooldown for item usage,
 * specifying the cooldown time and the group it belongs to.
 */
public class CustomUseCooldownComponent implements UseCooldownComponent {

    private float cooldownSeconds;
    private NamespacedKey cooldownGroup;

    /**
     * Constructs a new CustomUseCooldownComponent with the specified cooldown time and group.
     *
     * @param cooldownSeconds the cooldown duration in seconds
     * @param cooldownGroup   the {@link NamespacedKey} representing the cooldown group
     */
    public CustomUseCooldownComponent(float cooldownSeconds, NamespacedKey cooldownGroup) {
        this.cooldownSeconds = cooldownSeconds;
        this.cooldownGroup = cooldownGroup;
    }

    /**
     * Gets the cooldown time in seconds.
     *
     * @return the cooldown duration in seconds
     */
    @Override
    public float getCooldownSeconds() {
        return this.cooldownSeconds;
    }

    /**
     * Sets the cooldown time in seconds.
     *
     * @param cooldownSeconds the new cooldown duration in seconds
     */
    @Override
    public void setCooldownSeconds(float cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

    /**
     * Gets the cooldown group represented by a {@link NamespacedKey}.
     *
     * @return the cooldown group, or null if not set
     */
    @Nullable
    @Override
    public NamespacedKey getCooldownGroup() {
        return this.cooldownGroup;
    }

    /**
     * Sets the cooldown group.
     *
     * @param cooldownGroup the {@link NamespacedKey} representing the cooldown group, or null to remove the group
     */
    @Override
    public void setCooldownGroup(@Nullable NamespacedKey cooldownGroup) {
        this.cooldownGroup = cooldownGroup;
    }

    /**
     * Serializes this cooldown component into a map for data storage.
     *
     * @return a map containing the cooldown group and its corresponding cooldown time
     */
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of(cooldownGroup.getKey(), cooldownSeconds);
    }
}