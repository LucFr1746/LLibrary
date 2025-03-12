package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The {@code MusicInstrumentBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link MusicInstrumentMeta} of an {@link ItemStack}.
 * This class allows setting the instrument for goat horns.
 */
public class MusicInstrumentBuilder extends ItemBuilder {

    private final MusicInstrumentMeta musicInstrumentMeta;

    /**
     * Constructs a {@code MusicInstrumentBuilder} with a new {@link Material#GOAT_HORN}.
     */
    public MusicInstrumentBuilder() {
        super(Material.GOAT_HORN);
        if (!(getItemMeta() instanceof MusicInstrumentMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of MusicInstrumentMeta");
        }
        this.musicInstrumentMeta = meta;
    }

    /**
     * Constructs a {@code MusicInstrumentBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link MusicInstrumentMeta}
     */
    public MusicInstrumentBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof MusicInstrumentMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of MusicInstrumentMeta");
        }
        this.musicInstrumentMeta = meta;
    }

    /**
     * Sets the music instrument of the goat horn.
     *
     * @param instrument the {@link MusicInstrument} to set
     * @return this {@code MusicInstrumentBuilder} instance for chaining
     * @throws IllegalArgumentException if the instrument is null
     */
    public MusicInstrumentBuilder setInstrument(@NotNull MusicInstrument instrument) {
        if (instrument == null) {
            throw new IllegalArgumentException("MusicInstrument cannot be null");
        }
        musicInstrumentMeta.setInstrument(instrument);
        return this;
    }

    /**
     * Gets the music instrument of the goat horn.
     *
     * @return the current {@link MusicInstrument}, or null if none is set
     */
    @Nullable
    public MusicInstrument getInstrument() {
        return musicInstrumentMeta.getInstrument();
    }

    /**
     * Checks if the goat horn has a music instrument set.
     *
     * @return true if an instrument is set, false otherwise
     */
    public boolean hasInstrument() {
        return musicInstrumentMeta.getInstrument() != null;
    }
}