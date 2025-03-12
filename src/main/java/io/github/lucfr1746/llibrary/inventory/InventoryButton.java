package io.github.lucfr1746.llibrary.inventory;

import io.github.lucfr1746.llibrary.requirement.Requirement;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a button in a custom inventory system.
 * This button can have a dynamic icon, handle click events, and have visibility requirements.
 */
public class InventoryButton implements Cloneable {

    private Function<Player, ItemStack> iconCreator;
    private Consumer<InventoryClickEvent> eventConsumer;
    private String id;
    private int priority;
    private List<Requirement> viewRequirements;

    /**
     * Sets the function that generates the button's icon.
     * @param iconCreator A function that takes a Player and returns an ItemStack.
     * @return The current InventoryButton instance.
     */
    public InventoryButton creator(Function<Player, ItemStack> iconCreator) {
        this.iconCreator = iconCreator;
        return this;
    }

    /**
     * Sets the consumer that handles inventory click events.
     * @param eventConsumer A consumer that processes InventoryClickEvent.
     * @return The current InventoryButton instance.
     */
    public InventoryButton consumer(Consumer<InventoryClickEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
        return this;
    }

    /**
     * Sets the identifier for the button.
     * @param id A unique string identifier.
     * @return The current InventoryButton instance.
     */
    public InventoryButton id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the priority of the button, which may be used for sorting.
     * @param priority An integer representing the button's priority.
     * @return The current InventoryButton instance.
     */
    public InventoryButton priority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Sets the list of requirements that determine whether the button is visible.
     * @param requirements A list of Requirement objects.
     * @return The current InventoryButton instance.
     */
    public InventoryButton viewRequirements(List<Requirement> requirements) {
        this.viewRequirements = requirements;
        return this;
    }

    /**
     * Gets the function that generates the button's icon.
     * @return A function that takes a Player and returns an ItemStack.
     */
    public Function<Player, ItemStack> getIconCreator() {
        return this.iconCreator;
    }

    /**
     * Gets the consumer that handles inventory click events.
     * @return A consumer that processes InventoryClickEvent.
     */
    public Consumer<InventoryClickEvent> getEventConsumer() {
        return this.eventConsumer;
    }

    /**
     * Gets the identifier of the button.
     * @return A unique string identifier.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gets the priority of the button.
     * @return An integer representing the button's priority.
     */
    public int getPriority() {
        return this.priority;
    }

    @Override
    public InventoryButton clone() {
        try {
            InventoryButton clone = (InventoryButton) super.clone();
            clone.id = this.id;
            clone.priority = this.priority;
            clone.iconCreator = this.iconCreator;
            clone.eventConsumer = this.eventConsumer;
            clone.viewRequirements = this.viewRequirements != null ? List.copyOf(this.viewRequirements) : List.of();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }

    /**
     * Gets the list of requirements that determine whether the button is visible.
     * @return A list of Requirement objects.
     */
    public List<Requirement> getViewRequirements() {
        return this.viewRequirements;
    }
}