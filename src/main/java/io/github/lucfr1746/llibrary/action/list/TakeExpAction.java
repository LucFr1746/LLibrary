package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

/**
 * Represents an action that removes experience points from a player.
 */
public class TakeExpAction extends Action {

    private final int amount;

    /**
     * Constructs a TakeExpAction with a specified amount of experience to remove.
     *
     * @param amount The amount of experience to remove.
     * @throws IllegalArgumentException If the amount is negative.
     */
    public TakeExpAction(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative!");
        }
        this.amount = amount;
    }

    /**
     * Gets the amount of experience to be removed.
     *
     * @return The experience amount.
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Executes the action, deducting experience points from the target player.
     * The experience cannot go below zero.
     *
     * @param target The player whose experience will be reduced.
     */
    @Override
    public void execute(Player target) {
        target.setTotalExperience(Math.max(0, target.getTotalExperience() - this.amount));
    }
}