package io.github.lucfr1746.llibrary.requirement.list;

import io.github.lucfr1746.llibrary.requirement.Requirement;
import org.bukkit.entity.Player;

/**
 * Represents a requirement that checks if a player has a minimum level.
 */
public class HasLevelRequirement extends Requirement {

    private final int neededLvl;

    /**
     * Creates a new level requirement.
     *
     * @param neededLvl The minimum level required.
     */
    public HasLevelRequirement(int neededLvl) {
        this.neededLvl = neededLvl;
    }

    /**
     * Gets the required level for this requirement.
     *
     * @return The required level.
     */
    public int getNeededLvl() {
        return this.neededLvl;
    }

    /**
     * Evaluates whether the player meets the required level.
     *
     * @param player The player to check.
     * @return {@code true} if the player has at least the required level, otherwise {@code false}.
     */
    @Override
    public boolean evaluate(Player player) {
        return player.getLevel() >= this.neededLvl;
    }
}