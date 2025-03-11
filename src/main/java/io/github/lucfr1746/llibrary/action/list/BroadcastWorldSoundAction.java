package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Represents an action that broadcasts a sound to all players in the same world as the target player.
 */
public class BroadcastWorldSoundAction extends Action {

    private final Sound sound;
    private final float volume;
    private final float pitch;

    /**
     * Constructs a BroadcastWorldSoundAction with the specified sound, volume, and pitch.
     * @param sound The sound to broadcast.
     * @param volume The volume of the sound.
     * @param pitch The pitch of the sound.
     */
    public BroadcastWorldSoundAction(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Gets the sound being broadcasted.
     * @return The sound.
     */
    public Sound getSound() {
        return this.sound;
    }

    /**
     * Gets the volume of the sound.
     * @return The volume.
     */
    public float getVolume() {
        return this.volume;
    }

    /**
     * Gets the pitch of the sound.
     * @return The pitch.
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Executes the broadcast world sound action, playing the sound for all players in the same world as the target.
     * @param target The player triggering the action.
     */
    @Override
    public void execute(Player target) {
        for (final Player broadcastTarget : target.getWorld().getPlayers()) {
            broadcastTarget.playSound(broadcastTarget.getLocation(), this.sound, this.volume, this.pitch);
        }
    }
}