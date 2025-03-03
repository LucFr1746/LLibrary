package io.github.lucfr1746.llibrary.action;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.utils.Hooks;
import org.bukkit.entity.Player;

public class GiveMoneyAction extends Action {

    private final Double amount;

    public GiveMoneyAction(Double amount) {
        this.amount = amount;
        if (this.amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative!");
        }
    }

    public Double getAmount() {
        return amount;
    }

    public void run(Player target) {
        if (!Hooks.isVault()) {
            throw new IllegalStateException("There is no economy API hooked!");
        }
        LLibrary.getEconomy().depositPlayer(target, this.amount);
    }
}
