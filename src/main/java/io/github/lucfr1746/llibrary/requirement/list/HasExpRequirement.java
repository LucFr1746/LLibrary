package io.github.lucfr1746.llibrary.requirement.list;

import io.github.lucfr1746.llibrary.requirement.Requirement;
import org.bukkit.entity.Player;

/**
 * Represents a requirement that checks if a player has a minimum amount of experience.
 */
public class HasExpRequirement extends Requirement {

    private final int neededExp;

    /**
     * Creates a new experience requirement.
     *
     * @param amount The minimum experience points required.
     */
    public HasExpRequirement(int amount) {
        this.neededExp = amount;
    }

    /**
     * Evaluates whether the player meets the required experience.
     *
     * @param player The player to check.
     * @return {@code true} if the player has at least the required experience, otherwise {@code false}.
     */
    @Override
    public boolean evaluate(Player player) {
        return player.getTotalExperience() >= neededExp;
    }
}