package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@code MapBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link MapMeta} of an {@link ItemStack}.
 * This class allows setting map colors, map views, and scaling.
 */
public class MapBuilder extends ItemBuilder {

    private final MapMeta mapMeta;

    /**
     * Constructs a {@code MapBuilder} with a new {@link Material#MAP}.
     */
    public MapBuilder() {
        super(Material.MAP);
        if (!(getItemMeta() instanceof MapMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of MapMeta");
        }
        this.mapMeta = meta;
    }

    /**
     * Constructs a {@code MapBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link MapMeta}
     */
    public MapBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof MapMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of MapMeta");
        }
        this.mapMeta = meta;
    }

    /**
     * Sets the map color.
     *
     * @param color the {@link Color} to set for the map
     * @return this {@code MapBuilder} instance for chaining
     * @throws IllegalArgumentException if color is null
     */
    public MapBuilder setColor(@NotNull Color color) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        mapMeta.setColor(color);
        return this;
    }

    /**
     * Gets the current map color.
     *
     * @return the {@link Color} of the map, or default color if none is set
     */
    @Nullable
    public Color getColor() {
        return mapMeta.getColor();
    }

    /**
     * Checks if the map has a custom color.
     *
     * @return true if the map has a custom color, false otherwise
     */
    public boolean hasColor() {
        return mapMeta.hasColor();
    }

    /**
     * Sets the associated {@link MapView} for this map.
     *
     * @param mapView the map view to associate
     * @return this {@code MapBuilder} instance for chaining
     * @throws IllegalArgumentException if mapView is null
     */
    public MapBuilder setMapView(@NotNull MapView mapView) {
        if (mapView == null) {
            throw new IllegalArgumentException("MapView cannot be null");
        }
        mapMeta.setMapView(mapView);
        return this;
    }

    /**
     * Gets the associated {@link MapView} of this map.
     *
     * @return the map view, or null if none is set
     */
    @Nullable
    public MapView getMapView() {
        return mapMeta.getMapView();
    }

    /**
     * Checks if the map has an associated {@link MapView}.
     *
     * @return true if a map view is associated, false otherwise
     */
    public boolean hasMapView() {
        return mapMeta.hasMapView();
    }

    /**
     * Sets whether the map should scale dynamically.
     *
     * @param scaling true to enable scaling, false to disable
     * @return this {@code MapBuilder} instance for chaining
     */
    public MapBuilder setScaling(boolean scaling) {
        mapMeta.setScaling(scaling);
        return this;
    }

    /**
     * Checks if the map is set to scale dynamically.
     *
     * @return true if scaling is enabled, false otherwise
     */
    public boolean isScaling() {
        return mapMeta.isScaling();
    }
}