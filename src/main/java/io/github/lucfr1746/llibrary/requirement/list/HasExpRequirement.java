package io.github.lucfr1746.llibrary.requirement.list;

import io.github.lucfr1746.llibrary.requirement.Requirement;
import org.bukkit.entity.Player;

public class HasExpRequirement extends Requirement {

    private final int neededExp;

    public HasExpRequirement(int amount) {
        this.neededExp = amount;
    }

    @Override
    public boolean evaluate(Player player) {
        return player.getTotalExperience() >= neededExp;
    }
}
