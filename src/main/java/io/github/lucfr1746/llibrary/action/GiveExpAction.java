package io.github.lucfr1746.llibrary.action;

import org.bukkit.entity.Player;

public class GiveExpAction extends Action {

    private final int amount;

    public GiveExpAction(int amount) {
        this.amount = amount;
        if (this.amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative!");
        }
    }

    public int getAmount() {
        return this.amount;
    }

    public void run(Player target) {
        target.setExp(target.getExp() + this.amount);
    }
}
