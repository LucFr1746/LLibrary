package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BroadcastWorldSoundAction extends Action {

    private final Sound sound;
    private final float volume;
    private final float pitch;

    public BroadcastWorldSoundAction(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Sound getSound() {
        return this.sound;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    @Override
    public void execute(Player target) {
        for (final Player broadcastTarget : target.getWorld().getPlayers()) {
            broadcastTarget.playSound(broadcastTarget.getLocation(), this.sound, this.volume, this.pitch);
        }
    }
}
