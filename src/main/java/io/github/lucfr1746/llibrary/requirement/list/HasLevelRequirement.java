package io.github.lucfr1746.llibrary.requirement.list;

import io.github.lucfr1746.llibrary.requirement.Requirement;
import org.bukkit.entity.Player;

public class HasLevelRequirement extends Requirement {

    private final int neededLvl;

    public HasLevelRequirement(int neededLvl) {
        this.neededLvl = neededLvl;
    }

    public int getNeededLvl() {
        return this.neededLvl;
    }

    @Override
    public boolean evaluate(Player player) {
        return player.getLevel() >= this.neededLvl;
    }
}
