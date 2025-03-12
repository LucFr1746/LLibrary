package io.github.lucfr1746.llibrary.itemstack.helper;

import org.bukkit.World;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code MapViewBuilder} class allows for configuring {@link MapView} properties,
 * such as center coordinates, scale, world, and renderers.
 */
public class MapViewBuilder {

    private final MapView mapView;

    /**
     * Constructs a {@code MapViewBuilder} for the given {@link MapView}.
     *
     * @param mapView the map view to configure
     */
    public MapViewBuilder(@NotNull MapView mapView) {
        this.mapView = mapView;
    }

    /**
     * Sets the center X coordinate of the map.
     *
     * @param x the center X position
     * @return this {@code MapViewBuilder} instance for chaining
     */
    public MapViewBuilder setCenterX(int x) {
        mapView.setCenterX(x);
        return this;
    }

    /**
     * Sets the center Z coordinate of the map.
     *
     * @param z the center Z position
     * @return this {@code MapViewBuilder} instance for chaining
     */
    public MapViewBuilder setCenterZ(int z) {
        mapView.setCenterZ(z);
        return this;
    }

    /**
     * Sets the world the map is associated with.
     *
     * @param world the world
     * @return this {@code MapViewBuilder} instance for chaining
     */
    public MapViewBuilder setWorld(@NotNull World world) {
        mapView.setWorld(world);
        return this;
    }

    /**
     * Sets the scale of the map.
     *
     * @param scale the map's scale
     * @return this {@code MapViewBuilder} instance for chaining
     */
    public MapViewBuilder setScale(@NotNull MapView.Scale scale) {
        mapView.setScale(scale);
        return this;
    }

    /**
     * Sets whether the map is locked.
     *
     * @param locked true to lock the map, false to unlock
     * @return this {@code MapViewBuilder} instance for chaining
     */
    public MapViewBuilder setLocked(boolean locked) {
        mapView.setLocked(locked);
        return this;
    }

    /**
     * Sets whether the map should track player positions.
     *
     * @param tracking true to track player positions, false otherwise
     * @return this {@code MapViewBuilder} instance for chaining
     */
    public MapViewBuilder setTrackingPosition(boolean tracking) {
        mapView.setTrackingPosition(tracking);
        return this;
    }

    /**
     * Sets whether the map has unlimited tracking.
     *
     * @param unlimited true to enable unlimited tracking, false otherwise
     * @return this {@code MapViewBuilder} instance for chaining
     */
    public MapViewBuilder setUnlimitedTracking(boolean unlimited) {
        mapView.setUnlimitedTracking(unlimited);
        return this;
    }

    /**
     * Adds a renderer to the map.
     *
     * @param renderer the {@link MapRenderer} to add
     * @return this {@code MapViewBuilder} instance for chaining
     */
    public MapViewBuilder addRenderer(@NotNull MapRenderer renderer) {
        mapView.addRenderer(renderer);
        return this;
    }

    /**
     * Removes a renderer from the map.
     *
     * @param renderer the {@link MapRenderer} to remove
     * @return this {@code MapViewBuilder} instance for chaining
     */
    public MapViewBuilder removeRenderer(@NotNull MapRenderer renderer) {
        mapView.removeRenderer(renderer);
        return this;
    }

    /**
     * Gets the configured {@link MapView}.
     *
     * @return the configured map view
     */
    public MapView build() {
        return mapView;
    }
}