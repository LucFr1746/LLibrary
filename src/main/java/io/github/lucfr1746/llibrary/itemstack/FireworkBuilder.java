package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * A builder for creating and modifying firework ItemStacks in Bukkit.
 * Provides methods to customize firework effects and properties.
 *
 * @param firework is the ItemStack to be customized. Must have FireworkMeta.
 */
public record FireworkBuilder(@NotNull ItemStack firework) {

    /**
     * Constructs a FireworkBuilder.
     *
     * @param firework the firework ItemStack must not be null or AIR and must have FireworkMeta.
     * @throws NullPointerException if the firework is null.
     * @throws IllegalArgumentException if the firework is AIR.
     * @throws IllegalStateException if the firework does not have FireworkMeta.
     */
    public FireworkBuilder {
        Objects.requireNonNull(firework, "Firework ItemStack cannot be null.");
        if (firework.getType() == Material.AIR) {
            throw new IllegalArgumentException("The ItemStack cannot be AIR!");
        }
        if (!(firework.getItemMeta() instanceof FireworkMeta)) {
            throw new IllegalStateException("The ItemStack must have FireworkMeta!");
        }
        firework = firework.clone();
    }

    /**
     * Clears all effects from the firework.
     *
     * @return this builder for chaining.
     */
    public FireworkBuilder clearEffects() {
        updateMeta(FireworkMeta::clearEffects);
        return this;
    }

    /**
     * Gets all effects applied to the firework.
     *
     * @return a list of FireworkEffects.
     */
    public List<FireworkEffect> effects() {
        return getFireworkMeta().getEffects();
    }

    /**
     * Gets the effect at the specified index.
     *
     * @param index the index of the effect.
     * @return the FireworkEffect at the given index.
     */
    public FireworkEffect effect(int index) {
        return effects().get(index);
    }

    /**
     * Gets the number of effects on the firework.
     *
     * @return the number of effects.
     */
    public int numberOfEffects() {
        return getFireworkMeta().getEffectsSize();
    }

    /**
     * Gets the power level of the firework.
     *
     * @return the power level.
     */
    public int power() {
        return getFireworkMeta().getPower();
    }

    /**
     * Checks if the firework has any effects.
     *
     * @return true if the firework has effects, false otherwise.
     */
    public boolean hasEffects() {
        return getFireworkMeta().hasEffects();
    }

    /**
     * Checks if the firework has a power level set.
     *
     * @return true if the firework has a power level, false otherwise.
     */
    public boolean hasPower() {
        return getFireworkMeta().hasPower();
    }

    /**
     * Removes the effect at the specified index.
     *
     * @param index the index of the effect to remove.
     * @return this builder for chaining.
     */
    public FireworkBuilder removeEffect(int index) {
        updateMeta(meta -> meta.removeEffect(index));
        return this;
    }

    /**
     * Sets the power level of the firework.
     *
     * @param power the power level to set.
     * @return this builder for chaining.
     */
    public FireworkBuilder power(int power) {
        updateMeta(meta -> meta.setPower(power));
        return this;
    }

    /**
     * Gets the FireworkMeta of the firework.
     *
     * @return the FireworkMeta.
     * @throws NullPointerException if the FireworkMeta is null.
     */
    private @NotNull FireworkMeta getFireworkMeta() {
        return (FireworkMeta) Objects.requireNonNull(firework.getItemMeta(), "FireworkMeta cannot be null.");
    }

    /**
     * Updates the FireworkMeta using the provided action.
     *
     * @param action the action to perform on the FireworkMeta.
     */
    private void updateMeta(java.util.function.Consumer<FireworkMeta> action) {
        FireworkMeta meta = getFireworkMeta();
        action.accept(meta);
        firework.setItemMeta(meta);
    }

    /**
     * A builder for creating FireworkEffects.
     */
    public static class FireworkEffectBuilder {

        private final FireworkEffect.Builder builder = FireworkEffect.builder();

        /**
         * Sets whether the effect should flicker.
         *
         * @param flicker true if the effect should flicker.
         * @return this builder for chaining.
         */
        public FireworkEffectBuilder flicker(boolean flicker) {
            builder.flicker(flicker);
            return this;
        }

        /**
         * Sets whether the effect should have a trail.
         *
         * @param trail true if the effect should have a trail.
         * @return this builder for chaining.
         */
        public FireworkEffectBuilder trail(boolean trail) {
            builder.trail(trail);
            return this;
        }

        /**
         * Sets the type of the firework effect.
         *
         * @param type the type of the firework effect.
         * @return this builder for chaining.
         */
        public FireworkEffectBuilder with(FireworkEffect.Type type) {
            builder.with(type);
            return this;
        }

        /**
         * Adds colors to the firework effect.
         *
         * @param colors the colors to add.
         * @return this builder for chaining.
         */
        public FireworkEffectBuilder withColor(Color... colors) {
            builder.withColor(colors);
            return this;
        }

        /**
         * Adds fade colors to the firework effect.
         *
         * @param colors the colors to fade to.
         * @return this builder for chaining.
         */
        public FireworkEffectBuilder withFade(Color... colors) {
            builder.withFade(colors);
            return this;
        }

        /**
         * Builds the FireworkEffect.
         *
         * @return the built FireworkEffect.
         */
        public FireworkEffect build() {
            return builder.build();
        }
    }
}
