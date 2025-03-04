package io.github.lucfr1746.llibrary.requirement;

import org.bukkit.entity.Player;

public class HasLevelRequirement extends Requirement {

    private final int neededLvl;

    public HasLevelRequirement(int amount) {
        this.neededLvl = amount;
    }

    @Override
    public boolean evaluate(Player player) {
        return player.getLevel() >= neededLvl;
    }
}
