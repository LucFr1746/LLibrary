package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

/**
 * Represents an action that gives experience points to a player.
 */
public class GiveExpAction extends Action {

    private final int amount;

    /**
     * Constructs a GiveExpAction with the specified amount of experience points.
     *
     * @param amount The amount of experience to give.
     * @throws IllegalArgumentException if the amount is negative.
     */
    public GiveExpAction(int amount) {
        this.amount = amount;
        if (this.amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative!");
        }
    }

    /**
     * Gets the amount of experience to be given.
     *
     * @return The experience amount.
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Executes the action, giving experience points to the target player.
     *
     * @param target The player who will receive the experience.
     */
    @Override
    public void execute(Player target) {
        target.setExp(target.getExp() + this.amount);
    }
}