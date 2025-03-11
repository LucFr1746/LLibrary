package io.github.lucfr1746.llibrary.requirement;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Represents a requirement that must be evaluated for a player.
 * Requirements can have actions executed upon acceptance or denial.
 * <p>
 * Extend it and register through {@link RequirementLoader} if you want to create a new requirement.
 */
public abstract class Requirement {

    /**
     * Evaluates whether the player meets the requirement.
     *
     * @param player The player to evaluate.
     * @return {@code true} if the requirement is met, otherwise {@code false}.
     */
    public abstract boolean evaluate(Player player);

    private List<Action> acceptHandler;
    private List<Action> denyHandler;

    /**
     * Sets the actions to execute when the requirement is met.
     *
     * @param acceptHandler The list of actions to execute on acceptance.
     * @return The current requirement instance.
     */
    public Requirement setAcceptHandler(List<Action> acceptHandler) {
        this.acceptHandler = acceptHandler;
        return this;
    }

    /**
     * Sets the actions to execute when the requirement is not met.
     *
     * @param denyHandler The list of actions to execute on denial.
     * @return The current requirement instance.
     */
    public Requirement setDenyHandler(List<Action> denyHandler) {
        this.denyHandler = denyHandler;
        return this;
    }

    /**
     * Gets the actions that execute when the requirement is met.
     *
     * @return A list of acceptance actions.
     */
    public List<Action> getAcceptHandler() {
        return this.acceptHandler;
    }

    /**
     * Gets the actions that execute when the requirement is not met.
     *
     * @return A list of denial actions.
     */
    public List<Action> getDenyHandler() {
        return this.denyHandler;
    }

    /**
     * Checks if there are actions assigned for when the requirement is met.
     *
     * @return {@code true} if there are acceptance actions, otherwise {@code false}.
     */
    public boolean hasAcceptHandler() {
        return this.acceptHandler != null && !this.acceptHandler.isEmpty();
    }

    /**
     * Checks if there are actions assigned for when the requirement is not met.
     *
     * @return {@code true} if there are denial actions, otherwise {@code false}.
     */
    public boolean hasDenyHandler() {
        return this.denyHandler != null && !this.denyHandler.isEmpty();
    }
}