package io.github.lucfr1746.llibrary.action;

import org.bukkit.entity.Player;

public class TakeExpAction extends Action {

    private final int amount;

    public TakeExpAction(int amount) {
        this.amount = amount;
        if (this.amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative!");
        }
    }

    public int getAmount() {
        return this.amount;
    }

    public void run(Player target) {
        target.setExp(Math.max(0, target.getExp() - this.amount));
    }
}
