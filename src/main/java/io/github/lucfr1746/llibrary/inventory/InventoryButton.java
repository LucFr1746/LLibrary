package io.github.lucfr1746.llibrary.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a button in an inventory GUI. The button is defined by its icon
 * (which is dynamically created for each player) and an event consumer that
 * handles click events when the button is clicked.
 */
public class InventoryButton {

    /**
     * A function that creates an {@link ItemStack} representing the button's icon.
     * The icon can be dynamically created based on the player.
     */
    private Function<Player, ItemStack> iconCreator;

    /**
     * A consumer that handles the {@link InventoryClickEvent} when the button is clicked.
     */
    private Consumer<InventoryClickEvent> eventConsumer;

    /**
     * The id of the button, which can be used for identification.
     */
    private String id;

    /**
     * Default constructor that initializes the InventoryButton instance with default values.
     * This constructor does not set any of the button's properties and allows for later configuration.
     */
    public InventoryButton() {
        // Default constructor with no predefined values.
    }

    /**
     * Sets the function to create the button's icon.
     *
     * @param iconCreator A {@link Function} that takes a {@link Player} and returns an {@link ItemStack}.
     * @return The current {@link InventoryButton} instance for method chaining.
     */
    public InventoryButton creator(Function<Player, ItemStack> iconCreator) {
        this.iconCreator = iconCreator;
        return this;
    }

    /**
     * Sets the consumer that handles the {@link InventoryClickEvent} when the button is clicked.
     *
     * @param eventConsumer A {@link Consumer} that processes the {@link InventoryClickEvent}.
     * @return The current {@link InventoryButton} instance for method chaining.
     */
    public InventoryButton consumer(Consumer<InventoryClickEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
        return this;
    }

    /**
     * Sets the id of the button.
     *
     * @param buttonID The id of the button, usually for identification purposes.
     * @return The current {@link InventoryButton} instance for method chaining.
     */
    public InventoryButton id(String buttonID) {
        this.id = buttonID;
        return this;
    }



    /**
     * Gets the event consumer for handling the {@link InventoryClickEvent}.
     *
     * @return The {@link Consumer} for handling the click event, or {@code null} if not set.
     */
    public Consumer<InventoryClickEvent> getEventConsumer() {
        return this.eventConsumer;
    }

    /**
     * Gets the function that creates the icon for the button based on the player.
     *
     * @return The {@link Function} that generates the button's icon for a given {@link Player}.
     */
    public Function<Player, ItemStack> getIconCreator() {
        return this.iconCreator;
    }

    /**
     * Gets the id of the button.
     *
     * @return The id of the button, or {@code null} if not set.
     */
    public String getID() {
        return this.id;
    }
}
