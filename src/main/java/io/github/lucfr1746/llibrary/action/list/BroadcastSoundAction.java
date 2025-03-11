package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Represents an action that broadcasts a sound to all online players.
 */
public class BroadcastSoundAction extends Action {

    private final Sound sound;
    private final float volume;
    private final float pitch;

    /**
     * Constructs a BroadcastSoundAction with the specified sound, volume, and pitch.
     * @param sound The sound to broadcast.
     * @param volume The volume level of the sound.
     * @param pitch The pitch level of the sound.
     */
    public BroadcastSoundAction(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Gets the broadcasted sound.
     * @return The sound being played.
     */
    public Sound getSound() {
        return this.sound;
    }

    /**
     * Gets the volume level of the sound.
     * @return The volume level.
     */
    public float getVolume() {
        return this.volume;
    }

    /**
     * Gets the pitch level of the sound.
     * @return The pitch level.
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Executes the broadcast sound action, playing the sound for all online players.
     * @param target The player triggering the action (not directly used in this implementation).
     */
    @Override
    public void execute(Player target) {
        for (final Player broadcastTarget : Bukkit.getOnlinePlayers()) {
            broadcastTarget.playSound(broadcastTarget.getLocation(), this.sound, this.volume, this.pitch);
        }
    }
}