package io.github.lucfr1746.llibrary.itemstack.component;

import org.bukkit.JukeboxSong;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents a custom implementation of the {@link JukeboxPlayableComponent} interface.
 * This class allows customizing jukebox behavior, such as setting custom songs,
 * their keys, and whether they show up in item tooltips.
 */
public class CustomJukeboxPlayableComponent implements JukeboxPlayableComponent {

    private NamespacedKey songKey;
    private JukeboxSong jukeboxSong;
    private boolean showInTooltip;

    /**
     * Constructs a new CustomJukeboxPlayableComponent.
     *
     * @param songKey      the key identifying the song
     * @param jukeboxSong  the song to be played by the jukebox
     * @param showInTooltip whether the song should be shown in the item's tooltip
     */
    public CustomJukeboxPlayableComponent(NamespacedKey songKey, JukeboxSong jukeboxSong, boolean showInTooltip) {
        this.songKey = songKey;
        this.jukeboxSong = jukeboxSong;
        this.showInTooltip = showInTooltip;
    }

    /**
     * Gets the song associated with this component.
     *
     * @return the JukeboxSong, or null if not set
     */
    @Nullable
    @Override
    public JukeboxSong getSong() {
        return this.jukeboxSong;
    }

    /**
     * Gets the key of the song assigned to this component.
     *
     * @return the NamespacedKey representing the song's key
     */
    @NotNull
    @Override
    public NamespacedKey getSongKey() {
        return this.songKey;
    }

    /**
     * Sets the song for this component.
     *
     * @param song the JukeboxSong to set
     */
    @Override
    public void setSong(@NotNull JukeboxSong song) {
        this.jukeboxSong = song;
    }

    /**
     * Sets the song key for this component.
     *
     * @param songKey the NamespacedKey representing the song's key
     */
    @Override
    public void setSongKey(@NotNull NamespacedKey songKey) {
        this.songKey = songKey;
    }

    /**
     * Checks whether the song should be shown in the item's tooltip.
     *
     * @return true if the song is shown in the tooltip, false otherwise
     */
    @Override
    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    /**
     * Sets whether the song should be shown in the item's tooltip.
     *
     * @param show true to show the song in the tooltip, false otherwise
     */
    @Override
    public void setShowInTooltip(boolean show) {
        this.showInTooltip = show;
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
                "songKey", this.songKey.toString(),
                "song", this.jukeboxSong.getTranslationKey(),
                "showInTooltip", this.showInTooltip
        );
    }
}