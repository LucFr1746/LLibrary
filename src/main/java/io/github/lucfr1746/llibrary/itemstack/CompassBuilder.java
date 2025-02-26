package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A builder utility for modifying and retrieving properties of a {@link CompassMeta} ItemStack.
 *
 * @param compass The ItemStack, which must be a valid compass.
 */
public record CompassBuilder(@NotNull ItemStack compass) {

    /**
     * Constructs a new {@code CompassBuilder} with the specified compass ItemStack.
     *
     * @param compass The ItemStack, which must be a compass.
     * @throws NullPointerException     If the ItemStack is null.
     * @throws IllegalArgumentException If the ItemStack is air.
     * @throws IllegalStateException    If the ItemStack is not a compass.
     */
    public CompassBuilder(ItemStack compass) {
        Objects.requireNonNull(compass, "Compass ItemStack cannot be null.");
        if (compass.getType() == Material.AIR) {
            throw new IllegalArgumentException("The ItemStack cannot be AIR!");
        }
        if (!(compass.getItemMeta() instanceof CompassMeta)) {
            throw new IllegalStateException("The ItemStack must have CompassMeta!");
        }
        this.compass = compass.clone(); // Ensure immutability
    }

    /**
     * Gets the location the compass is pointing to.
     *
     * @return The pointed location, or {@code null} if none is set.
     */
    public Location getPointedLocation() {
        return getCompassMeta().getLodestone();
    }

    /**
     * Checks if the compass has a pointed location.
     *
     * @return {@code true} if the compass has a lodestone location, otherwise {@code false}.
     */
    public boolean hasPointedLocation() {
        return getCompassMeta().hasLodestone();
    }

    /**
     * Checks if the compass is tracking a custom lodestone location.
     *
     * @return {@code true} if the compass is tracking lodestone, otherwise {@code false}.
     */
    public boolean isPointedCustomLocation() {
        return getCompassMeta().isLodestoneTracked();
    }

    /**
     * Sets the location the compass should point to.
     *
     * @param location The new lodestone location.
     * @return A new {@code CompassBuilder} instance with the updated location.
     */
    public CompassBuilder setPointedLocation(Location location) {
        CompassMeta meta = getCompassMeta();
        meta.setLodestoneTracked(false);
        meta.setLodestone(location);
        ItemStack updatedCompass = compass.clone();
        updatedCompass.setItemMeta(meta);
        return new CompassBuilder(updatedCompass);
    }

    /**
     * Retrieves the {@link CompassMeta} of the compass.
     *
     * @return The {@code CompassMeta} instance.
     */
    private @NotNull CompassMeta getCompassMeta() {
        return (CompassMeta) Objects.requireNonNull(compass.getItemMeta(), "CompassMeta cannot be null.");
    }
}
