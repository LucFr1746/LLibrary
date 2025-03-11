package io.github.lucfr1746.llibrary.action;

import org.bukkit.entity.Player;

/**
 * Represents an abstract action that can be executed on a player.
 * Extends and register this custom action using {@link ActionLoader}
 */
public abstract class Action {

    /**
     * Executes the action on the specified player.
     * @param player The player on whom the action is performed.
     */
    public abstract void execute(Player player);
}
