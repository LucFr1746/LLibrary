package io.github.lucfr1746.llibrary.requirement;

import io.github.lucfr1746.llibrary.action.Action;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Requirement {

    public abstract boolean evaluate(Player player);

    private List<Action> acceptHandler;
    private List<Action> denyHandler;

    public Requirement setAcceptHandler(List<Action> acceptHandler) {
        this.acceptHandler = acceptHandler;
        return this;
    }

    public Requirement setDenyHandler(List<Action> denyHandler) {
        this.denyHandler = denyHandler;
        return this;
    }

    public List<Action> getAcceptHandler() {
        return this.acceptHandler;
    }

    public List<Action> getDenyHandler() {
        return this.denyHandler;
    }

    public boolean hasAcceptHandler() {
        return this.acceptHandler!= null &&! this.acceptHandler.isEmpty();
    }

    public boolean hasDenyHandler() {
        return this.denyHandler!= null &&! this.denyHandler.isEmpty();
    }
}
