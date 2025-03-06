package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
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

    @Override
    public void execute(Player target) {
        if (LLibrary.getEconomy() == null) {
            throw new IllegalStateException("There is no Economy API hooked!");
        }
        LLibrary.getEconomy().depositPlayer(target, this.amount);
    }
}
