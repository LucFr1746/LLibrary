package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

/**
 * Represents an action that gives money to a player using an economy system.
 */
public class GiveMoneyAction extends Action {

    private final Double amount;

    /**
     * Constructs a GiveMoneyAction with the specified amount of money.
     *
     * @param amount The amount of money to give.
     * @throws IllegalArgumentException if the amount is negative.
     */
    public GiveMoneyAction(Double amount) {
        this.amount = amount;
        if (this.amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative!");
        }
    }

    /**
     * Gets the amount of money to be given.
     *
     * @return The amount of money.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Executes the action, giving money to the target player.
     *
     * @param target The player who will receive the money.
     * @throws IllegalStateException if there is no economy system hooked.
     */
    @Override
    public void execute(Player target) {
        if (LLibrary.getEconomy() == null) {
            throw new IllegalStateException("There is no Economy API hooked!");
        }
        LLibrary.getEconomy().depositPlayer(target, this.amount);
    }
}