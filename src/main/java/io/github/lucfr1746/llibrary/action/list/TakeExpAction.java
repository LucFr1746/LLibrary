package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
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

    @Override
    public void execute(Player target) {
        target.setTotalExperience(Math.max(0, target.getTotalExperience() - this.amount));
    }
}
