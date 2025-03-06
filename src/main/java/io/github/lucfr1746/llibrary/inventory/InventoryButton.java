package io.github.lucfr1746.llibrary.inventory;

import io.github.lucfr1746.llibrary.requirement.Requirement;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class InventoryButton {

    private Function<Player, ItemStack> iconCreator;

    private Consumer<InventoryClickEvent> eventConsumer;

    private String id;

    private int priority;

    private List<Requirement> viewRequirements;

    public InventoryButton creator(Function<Player, ItemStack> iconCreator) {
        this.iconCreator = iconCreator;
        return this;
    }

    public InventoryButton consumer(Consumer<InventoryClickEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
        return this;
    }

    public InventoryButton id(String id) {
        this.id = id;
        return this;
    }

    public InventoryButton priority(int priority) {
        this.priority = priority;
        return this;
    }

    public InventoryButton viewRequirements(List<Requirement> requirements) {
        this.viewRequirements = requirements;
        return this;
    }

    public Function<Player, ItemStack> getIconCreator() {
        return this.iconCreator;
    }

    public Consumer<InventoryClickEvent> getEventConsumer() {
        return this.eventConsumer;
    }

    public String getId() {
        return this.id;
    }

    public int getPriority() {
        return this.priority;
    }

    public List<Requirement> getViewRequirements() {
        return this.viewRequirements;
    }
}
