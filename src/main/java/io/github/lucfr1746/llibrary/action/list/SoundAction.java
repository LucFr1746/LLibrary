package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Represents an action that plays a sound for a player.
 */
public class SoundAction extends Action {

    private final Sound sound;
    private final float volume;
    private final float pitch;

    /**
     * Constructs a SoundAction with the specified sound, volume, and pitch.
     *
     * @param sound  The sound to be played.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public SoundAction(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Gets the sound that will be played.
     *
     * @return The sound.
     */
    public Sound getSound() {
        return this.sound;
    }

    /**
     * Gets the volume of the sound.
     *
     * @return The volume.
     */
    public float getVolume() {
        return this.volume;
    }

    /**
     * Gets the pitch of the sound.
     *
     * @return The pitch.
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Executes the action, playing the sound at the target player's location.
     *
     * @param target The player who will hear the sound.
     */
    @Override
    public void execute(Player target) {
        target.playSound(target.getLocation(), this.sound, this.volume, this.pitch);
    }
}