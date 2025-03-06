package io.github.lucfr1746.llibrary.action.list;

import io.github.lucfr1746.llibrary.action.Action;
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

    @Override
    public void execute(Player target) {
        target.setExp(target.getExp() + this.amount);
    }
}
