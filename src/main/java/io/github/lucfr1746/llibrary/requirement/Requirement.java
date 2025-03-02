package io.github.lucfr1746.llibrary.requirement;

import org.bukkit.entity.Player;

public abstract class Requirement {

    public abstract boolean evaluate(Player player);

//    private ActionHandler acceptHandler;
//    private ActionHandler denyHandler;
//
//    public ActionHandler getDenyHandler() {
//        return this.denyHandler;
//    }
//
//    public void setDenyHandler(ActionHandler denyHandler) {
//        this.denyHandler = denyHandler;
//    }
//
//    public boolean hasDenyHandler() {
//        return this.denyHandler != null;
//    }
//
//    public ActionHandler getAcceptHandler() {
//        return this.acceptHandler;
//    }
//
//    public void setSuccessHandler(ActionHandler acceptHandler) {
//        this.acceptHandler = acceptHandler;
//    }
//
//    public boolean hasAcceptHandler() {
//        return this.acceptHandler != null;
//    }
}
