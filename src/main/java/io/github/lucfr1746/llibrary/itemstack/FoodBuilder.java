package io.github.lucfr1746.llibrary.itemstack;

import io.github.lucfr1746.llibrary.utils.Keys;
import io.github.lucfr1746.llibrary.utils.ParsedItem;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.consumable.ConsumableComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A builder class for customizing food properties in an ItemStack.
 */
public class FoodBuilder {

    private ItemStack itemstack;

    /**
     * Constructs a FoodBuilder with a given ItemStack.
     *
     * @param itemstack The ItemStack to modify.
     * @throws NullPointerException if the itemstack is null.
     * @throws IllegalArgumentException if the itemstack is AIR.
     */
    public FoodBuilder(@NotNull ItemStack itemstack) {
        Objects.requireNonNull(itemstack, "ItemStack cannot be null.");
        if (itemstack.getType() == Material.AIR) {
            throw new IllegalArgumentException("The ItemStack cannot be AIR!");
        }
        this.itemstack = itemstack.clone();
    }

    /**
     * Sets the sound played when the food is consumed.
     *
     * @param sound The sound to play.
     * @return The current FoodBuilder instance.
     */
    public FoodBuilder setFoodSound(Sound sound) {
        ParsedItem parsed = new ParsedItem(this.itemstack);
        parsed.set(((Keyed) sound).getKey().toString(), Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "sound");
        this.itemstack = parsed.toItemStack();
        return this;
    }

    /**
     * Sets the animation displayed when consuming the food.
     *
     * @param animation The animation type.
     * @return The current FoodBuilder instance.
     */
    public FoodBuilder setFoodAnimation(String animation) {
        Set<String> animations = new LinkedHashSet<>(Arrays.asList(
                "drink", "eat", "crossbow", "none",
                "block", "bow", "spear", "spyglass",
                "toot_horn", "brush"
        ));
        animation = animations.contains(animation.toLowerCase()) ? animation.toLowerCase() : null;
        if (animation == null) {
            throw new IllegalArgumentException("Invalid animation. Expected: \"drink\", \"eat\", \"crossbow\", \"none\", \"block\", \"bow\", \"spear\", \"spyglass\", \"toot_horn\", \"brush\"");
        }

        ParsedItem parsed = new ParsedItem(this.itemstack);
        parsed.set(animation, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "animation");
        this.itemstack = parsed.toItemStack();
        return this;
    }

    /**
     * Enables or disables particles shown when consuming the food.
     *
     * @param value True to show particles, false otherwise.
     * @return The current FoodBuilder instance.
     */
    public FoodBuilder setFoodConsumeParticles(boolean value) {
        ParsedItem parsed = new ParsedItem(this.itemstack);
        parsed.set(value, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "has_consume_particles");
        this.itemstack = parsed.toItemStack();
        return this;
    }

    /**
     * Clears all custom food properties from the ItemStack.
     *
     * @return The current FoodBuilder instance.
     */
    public FoodBuilder foodClear() {
        ItemMeta meta = this.itemstack.getItemMeta();
        if (meta == null || !meta.hasCustomModelData()) return this;

        ParsedItem parsed = new ParsedItem(this.itemstack);
        parsed.remove(Keys.Component.CROSS_VERSION_CONSUMABLE.toString());
        parsed.remove(Keys.Component.FOOD.toString());
        this.itemstack = parsed.toItemStack();
        return this;
    }

    /**
     * Sets whether the food can be eaten even if the player is not hungry.
     *
     * @param value True to allow always eating, false otherwise.
     * @return The current FoodBuilder instance.
     */
    public FoodBuilder setFoodCanAlwaysEat(boolean value) {
        ParsedItem parsed = new ParsedItem(this.itemstack);
        parsed.loadEmptyMap(Keys.Component.FOOD.toString());
        parsed.set(value, Keys.Component.FOOD.toString(), "can_always_eat");
        this.itemstack = parsed.toItemStack();
        return this;
    }

    /**
     * Sets the list of potion effects applied when the food is consumed.
     *
     * @param value A list of FoodPojo representing the effects.
     * @return The current FoodBuilder instance.
     */
    public FoodBuilder setFoodEffects(List<FoodPojo> value) {
        ParsedItem parsed = new ParsedItem(this.itemstack);
        List<Map<String, Object>> consumeEffects = new ArrayList<>();

        if (value != null) {
            value.forEach(food -> {
                Map<String, Object> effectMap = new LinkedHashMap<>();
                effectMap.put("type", Keys.EffectType.APPLY_EFFECTS);
                effectMap.put("probability", food.probability());

                Map<String, Object> potionMap = new LinkedHashMap<>();
                PotionEffect pot = food.potionEffect();
                potionMap.put("id", pot.getType().getKeyOrThrow().toString());
                potionMap.put("duration", pot.getDuration());
                potionMap.put("amplifier", pot.getAmplifier());
                potionMap.put("ambient", pot.isAmbient());
                potionMap.put("show_particles", pot.hasParticles());
                potionMap.put("show_icon", pot.hasIcon());

                effectMap.put("effects", Collections.singletonList(potionMap));
                consumeEffects.add(effectMap);
            });
        }

        parsed.set(consumeEffects, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "on_consume_effects");
        this.itemstack = parsed.toItemStack();
        return this;
    }

    /**
     * Gets the list of FoodPojo effects applied when the food is consumed.
     *
     * @return A list of FoodPojo representing the effects.
     */
    public List<FoodPojo> getFoodEffects() {
        ParsedItem parsed = new ParsedItem(this.itemstack);
        List<Map<String, Object>> effects = parsed.readList(Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "on_consume_effects");
        if (effects == null) return new ArrayList<>();

        List<FoodPojo> foodEffects = new ArrayList<>();
        for (Map<String, Object> map : effects) {
            if (!Keys.EffectType.APPLY_EFFECTS.equals(map.get("type"))) continue;

            float probability = (float) map.getOrDefault("probability", 1f);
            List<Map<String, Object>> effectList = (List<Map<String, Object>>) map.get("effects");

            for (Map<String, Object> effect : effectList) {
                PotionEffectType type = Registry.EFFECT.get(Objects.requireNonNull(NamespacedKey.fromString((String) effect.get("id"))));
                if (type == null) continue;

                int duration = (int) effect.get("duration");
                int amplifier = (int) effect.get("amplifier");
                boolean ambient = (boolean) effect.get("ambient");
                boolean showParticles = (boolean) effect.get("show_particles");
                boolean showIcon = (boolean) effect.get("show_icon");

                foodEffects.add(new FoodPojo(new PotionEffect(type, duration, amplifier, ambient, showParticles, showIcon), probability));
            }
        }
        return foodEffects;
    }

    /**
     * Retrieves the animation type of the food when consumed.
     *
     * @param item The ItemStack to check.
     * @return The animation type as a string.
     */
    public String getAnimation(ItemStack item) {
        ParsedItem parsedItem = new ParsedItem(item);
        return parsedItem.readString("eat", Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "animation");
    }

    /**
     * Retrieves the sound that plays when the food is consumed.
     *
     * @param item The ItemStack to check.
     * @return The corresponding Sound.
     */
    public Sound getSound(ItemStack item) {
        ParsedItem parsedItem = new ParsedItem(item);
        String val = parsedItem.readString((String) null, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "sound");
        if (val == null)
            return Sound.ENTITY_GENERIC_EAT;
        try {
            Sound value = Registry.SOUNDS.get(new NamespacedKey(val.split(":")[0], val.split(":")[1]));
            return value == null ? Sound.ENTITY_GENERIC_EAT : value;
        } catch (Exception e) {
            return Sound.ENTITY_GENERIC_EAT;
        }
    }

    /**
     * Checks if the food has consumed particles enabled.
     *
     * @param item The ItemStack to check.
     * @return True if consume particles are enabled, false otherwise.
     */
    public boolean hasConsumeParticles(ItemStack item) {
        ParsedItem parsedItem = new ParsedItem(item);
        return parsedItem.readBoolean(true, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "has_consume_particles");
    }

    /**
     * Retrieves the saturation value of the food.
     *
     * @param item The ItemStack to check.
     * @return The saturation value.
     */
    public float getSaturation(ItemStack item) {
        ParsedItem parsedItem = new ParsedItem(item);
        Map<String, Object> food = ParsedItem.getMap(parsedItem.getMap(), "food");
        if (food == null || !food.containsKey("saturation"))
            return 0;
        return ParsedItem.readFloat(food, "saturation", 0f);
    }

    /**
     * Retrieves the nutrition value of the food.
     *
     * @param item The ItemStack to check.
     * @return The nutrition value.
     */
    public int getNutrition(ItemStack item) {
        ParsedItem parsedItem = new ParsedItem(item);
        Map<String, Object> food = ParsedItem.getMap(parsedItem.getMap(), "food");
        if (food == null || !food.containsKey("nutrition"))
            return 0;
        return ParsedItem.readInt(food, "nutrition", 0);
    }

    /**
     * Sets the use remainder of the food (item that remains after consumption).
     *
     * @param d The ItemStack to set as the remainder.
     * @return The current FoodBuilder instance.
     */
    public FoodBuilder setUseRemainder(ItemStack d) {
        if (this.itemstack.getItemMeta() == null) return null;
        ItemMeta meta = this.itemstack.getItemMeta();
        meta.setUseRemainder(d);
        this.itemstack.setItemMeta(meta);
        return this;
    }

    /**
     * Retrieves the use remainder of the food (item that remains after consumption).
     *
     * @return The ItemStack that remains after consumption.
     */
    public ItemStack getUseRemainder() {
        return Objects.requireNonNull(this.itemstack.getItemMeta()).getUseRemainder();
    }

    /**
     * Checks if the food item can always be eaten, even when the player is not hungry.
     *
     * @return true if the item can always be eaten, false otherwise.
     */
    public boolean canAlwaysEat() {
        ParsedItem parsed = new ParsedItem(this.itemstack);
        return parsed.readBoolean(false, Keys.Component.FOOD.toString(), "can_always_eat");
    }

    /**
     * Sets the time it takes to eat the food in seconds.
     *
     * @param value The time in ticks (20 ticks = 1 second).
     * @return The current FoodBuilder instance for chaining.
     */
    public FoodBuilder foodEatTicks(float value) {
        value = Math.max(0, value);
        ParsedItem parsedItem = new ParsedItem(this.itemstack);
        parsedItem.loadEmptyMap(Keys.Component.CROSS_VERSION_CONSUMABLE.toString());
        parsedItem.set(value / 20, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "consume_seconds");
        this.itemstack = parsedItem.toItemStack();
        return this;
    }

    /**
     * Gets the time it takes to eat the food in seconds.
     *
     * @param stack The ItemStack to check.
     * @return The time in seconds.
     */
    public float getEatSeconds(ItemStack stack) {
        ParsedItem parsed = new ParsedItem(stack);
        return parsed.readFloat(1.6F, Keys.Component.CROSS_VERSION_CONSUMABLE.toString(), "consume_seconds");
    }

    /**
     * Sets the nutrition value (hunger points) of the food.
     *
     * @param value The nutrition value.
     * @return The current FoodBuilder instance for chaining.
     */
    public FoodBuilder setFoodNutrition(int value) {
        ParsedItem parsedItem = new ParsedItem(this.itemstack);
        parsedItem.loadEmptyMap(Keys.Component.CROSS_VERSION_CONSUMABLE.toString());
        parsedItem.set(value, Keys.Component.FOOD.toString(), "nutrition");
        parsedItem.load(0F, Keys.Component.FOOD.toString(), "saturation");
        this.itemstack = parsedItem.toItemStack();
        return this;
    }

    /**
     * Gets the nutrition value of the food.
     *
     * @return The nutrition value, or 0 if not set.
     */
    public int getNutrition() {
        ParsedItem parsedItem = new ParsedItem(this.itemstack);
        Map<String, Object> food = ParsedItem.getMap(parsedItem.getMap(), "food");
        if (food == null || !food.containsKey("nutrition"))
            return 0;
        return ParsedItem.readInt(food, "nutrition", 0);
    }

    /**
     * Sets the saturation value of the food.
     *
     * @param value The saturation value.
     * @return The current FoodBuilder instance for chaining.
     */
    public FoodBuilder setFoodSaturation(float value) {
        ParsedItem parsedItem = new ParsedItem(this.itemstack);
        parsedItem.loadEmptyMap(Keys.Component.CROSS_VERSION_CONSUMABLE.toString());
        parsedItem.set(0, Keys.Component.FOOD.toString(), "nutrition");
        parsedItem.load(value, Keys.Component.FOOD.toString(), "saturation");
        this.itemstack = parsedItem.toItemStack();
        return this;
    }

    /**
     * Gets the saturation value of the food.
     *
     * @return The saturation value, or 0 if not set.
     */
    public float getSaturation() {
        ParsedItem parsedItem = new ParsedItem(this.itemstack);
        Map<String, Object> food = ParsedItem.getMap(parsedItem.getMap(), "food");
        if (food == null || !food.containsKey("saturation"))
            return 0;
        return ParsedItem.readFloat(food, "saturation", 0f);
    }

    /**
     * Adds a food effect (PotionEffect) with a given chance to apply.
     *
     * @param effectType        The type of potion effect.
     * @param durationInSecond  The duration in ticks.
     * @param level             The level of the effect.
     * @param enableParticles   Whether particles are shown.
     * @param ambient           Whether the effect is ambient.
     * @param icon              Whether the effect icon is shown.
     * @param chanceInPercent   The chance for the effect to apply.
     * @return The current FoodBuilder instance for chaining.
     */
    public FoodBuilder addFoodEffect(PotionEffectType effectType, int durationInSecond, int level, boolean enableParticles, boolean ambient, boolean icon, float chanceInPercent) {
        if ((level < 0) || (level > 127))
            throw new IllegalArgumentException();
        durationInSecond = durationInSecond >= 0 ? durationInSecond * 20 : Integer.MAX_VALUE;
        chanceInPercent = Math.max(0, Math.min(chanceInPercent, 100)) / 100;

        List<FoodPojo> values = getFoodEffects();
        values.add(new FoodPojo(new PotionEffect(effectType, durationInSecond, level, ambient, enableParticles, icon), chanceInPercent));
        return setFoodEffects(values);
    }

    /**
     * Removes a specific food effect.
     *
     * @param effectType The type of potion effect to remove.
     * @param level      The level of the effect, or null to remove any level.
     * @return The current FoodBuilder instance for chaining.
     */
    public FoodBuilder removeFoodEffect(PotionEffectType effectType, @Nullable Integer level) {
        List<FoodPojo> values = getFoodEffects();
        values.removeIf(effect -> effect.getPotionEffect().getType().equals(effectType) &&
                (level == null || effect.getPotionEffect().getAmplifier() == level));
        return setFoodEffects(values);
    }

    /**
     * Clears all food effects from the item.
     *
     * @return The current FoodBuilder instance for chaining.
     */
    public FoodBuilder foodClearEffects() {
        return setFoodEffects(null);
    }

    /**
     * Sets the item that remains after the food is consumed.
     *
     * @param target The resulting ItemStack.
     * @param amount The amount of the resulting item.
     * @return The current FoodBuilder instance for chaining.
     */
    public FoodBuilder convertFoodTo(@NotNull ItemStack target, int amount) {
        if (target.getType().isAir()) target = null;
        if (target != null) target.setAmount(amount);
        return setUseRemainder(target);
    }

    /**
     * Represents a food effect that includes a potion effect and its probability.
     *
     * @param potionEffect the potion effect to be applied when the food is consumed
     * @param probability  the chance (from 0.0 to 1.0) that the effect will be applied
     */
    public record FoodPojo(PotionEffect potionEffect, float probability) {

        /**
         * Retrieves the potion effect.
         *
         * @return the potion effect associated with this food effect
         */
        public PotionEffect getPotionEffect() {
            return potionEffect;
        }

        /**
         * Retrieves the probability of applying the potion effect.
         *
         * @return the probability value
         */
        public float getProbability() {
            return probability;
        }
    }

}
