package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

/**
 * Represents an action that deducts money from a player's balance.
 */
public class TakeMoneyAction extends Action {

    private final Double amount;

    /**
     * Constructs a TakeMoneyAction with a specified amount to deduct.
     *
     * @param amount The amount of money to deduct.
     * @throws IllegalArgumentException If the amount is negative.
     */
    public TakeMoneyAction(Double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative!");
        }
        this.amount = amount;
    }

    /**
     * Gets the amount of money to be deducted.
     *
     * @return The amount of money.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Executes the action, withdrawing money from the target player's balance.
     * If the economy system is not available, an exception is thrown.
     *
     * @param target The player whose balance will be deducted.
     * @throws IllegalStateException If there is no economy system hooked.
     */
    @Override
    public void execute(Player target) {
        if (LLibrary.getEconomy() == null) {
            throw new IllegalStateException("There is no Economy API hooked!");
        }
        LLibrary.getEconomy().withdrawPlayer(target, this.amount);
    }
}