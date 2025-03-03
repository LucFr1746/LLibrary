package io.github.lucfr1746.llibrary.action;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BroadcastSoundAction extends Action {

    private final Sound sound;
    private final float volume;
    private final float pitch;

    public BroadcastSoundAction(Sound sound, float volume, float pitch) {
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

    public void run() {
        for (final Player broadcastTarget : Bukkit.getOnlinePlayers()) {
            broadcastTarget.playSound(broadcastTarget.getLocation(), this.sound, this.volume, this.pitch);
        }
    }
}
