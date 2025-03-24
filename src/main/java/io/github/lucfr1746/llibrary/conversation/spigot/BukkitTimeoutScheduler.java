package io.github.lucfr1746.llibrary.conversation.spigot;

import io.github.lucfr1746.llibrary.conversation.base.timeout.TimeoutScheduler;
import io.github.lucfr1746.llibrary.conversation.base.timeout.TimeoutTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

/**
 * {@inheritDoc}
 */
public class BukkitTimeoutScheduler implements TimeoutScheduler {

    private final Plugin plugin;

    public BukkitTimeoutScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeoutTask schedule(Runnable task, long time, TimeUnit timeUnit) {
        return new BukkitTimeoutTask(
                plugin
                        .getServer()
                        .getScheduler()
                        .runTaskLater(plugin, task, timeUnit.toSeconds(time) * 20));
    }

    private static class BukkitTimeoutTask implements TimeoutTask {

        private final BukkitTask bukkitTask;

        public BukkitTimeoutTask(BukkitTask bukkitTask) {
            this.bukkitTask = bukkitTask;
        }

        @Override
        public boolean hasCalled() {
            return !Bukkit.getScheduler().isCurrentlyRunning(bukkitTask.getTaskId())
                    && !Bukkit.getScheduler().isQueued(bukkitTask.getTaskId());
        }

        @Override
        public void cancel() {
            bukkitTask.cancel();
        }
    }
}
