package io.github.lucfr1746.llibrary.requirement;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Requirement {

    public abstract boolean evaluate(Player player);

    private List<Action> acceptHandler;
    private List<Action> denyHandler;

    public List<Action> getDenyHandlers() {
        return this.denyHandler;
    }

    public Requirement setDenyHandler(List<Action> denyHandlers) {
        this.denyHandler = denyHandlers;
        return this;
    }

    public boolean hasDenyHandler() {
        return this.denyHandler != null && !this.denyHandler.isEmpty();
    }

    public List<Action> getAcceptHandlers() {
        return this.acceptHandler;
    }

    public Requirement setSuccessHandler(List<Action> acceptHandlers) {
        this.acceptHandler = acceptHandlers;
        return this;
    }

    public boolean hasAcceptHandler() {
        return this.acceptHandler != null && !this.acceptHandler.isEmpty();
    }
}
