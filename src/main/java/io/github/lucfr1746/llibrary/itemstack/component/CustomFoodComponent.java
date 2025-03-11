package io.github.lucfr1746.llibrary.itemstack.component;

import org.bukkit.inventory.meta.components.FoodComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents a custom implementation of the {@link FoodComponent}.
 * This class allows defining custom food properties such as nutrition, saturation,
 * and whether the item can be eaten when the player is not hungry.
 */
public class CustomFoodComponent implements FoodComponent {

    private int nutrition;
    private float saturation;
    private boolean canEatOnFull;

    /**
     * Constructs a new CustomFoodComponent with the specified properties.
     *
     * @param nutrition the amount of nutrition the food provides
     * @param saturation the saturation modifier of the food
     * @param canEatOnFull whether the food can be eaten even if the player is not hungry
     */
    public CustomFoodComponent(int nutrition, float saturation, boolean canEatOnFull) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.canEatOnFull = canEatOnFull;
    }

    /**
     * Gets the nutrition value of the food.
     *
     * @return the nutrition value
     */
    @Override
    public int getNutrition() {
        return this.nutrition;
    }

    /**
     * Sets the nutrition value of the food.
     *
     * @param nutrition the new nutrition value
     */
    @Override
    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    /**
     * Gets the saturation value of the food.
     *
     * @return the saturation value
     */
    @Override
    public float getSaturation() {
        return this.saturation;
    }

    /**
     * Sets the saturation value of the food.
     *
     * @param saturation the new saturation value
     */
    @Override
    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    /**
     * Checks if the food can be eaten when the player is not hungry.
     *
     * @return true if the food can be eaten on full hunger, false otherwise
     */
    @Override
    public boolean canAlwaysEat() {
        return this.canEatOnFull;
    }

    /**
     * Sets whether the food can be eaten when the player is not hungry.
     *
     * @param canAlwaysEat true if the food can be eaten on full hunger, false otherwise
     */
    @Override
    public void setCanAlwaysEat(boolean canAlwaysEat) {
        this.canEatOnFull = canAlwaysEat;
    }

    /**
     * Serializes the food component properties into a map.
     *
     * @return a map containing the serialized properties of the food component
     */
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of("nutrition", getNutrition(), "saturation", getSaturation(), "canAlwaysEat", canAlwaysEat());
    }
}