package io.github.lucfr1746.llibrary.itemstack.helper;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.jetbrains.annotations.NotNull;

/**
 * A builder class for creating custom {@link FireworkEffect}.
 * Supports method chaining for easy configuration of effects.
 */
public class FireworkEffectBuilder {

    private final FireworkEffect.Builder builder;

    /**
     * Constructs a new {@code FireworkEffectBuilder}.
     */
    public FireworkEffectBuilder() {
        this.builder = FireworkEffect.builder();
    }

    /**
     * Sets the type of the firework effect.
     *
     * @param type the firework effect type
     * @return this builder instance
     */
    public FireworkEffectBuilder withType(@NotNull Type type) {
        builder.with(type);
        return this;
    }

    /**
     * Adds one or more primary colors to the firework effect.
     *
     * @param colors the primary colors
     * @return this builder instance
     */
    public FireworkEffectBuilder withColor(@NotNull Color... colors) {
        builder.withColor(colors);
        return this;
    }

    /**
     * Adds one or more fade colors to the firework effect.
     *
     * @param colors the fade colors
     * @return this builder instance
     */
    public FireworkEffectBuilder withFade(@NotNull Color... colors) {
        builder.withFade(colors);
        return this;
    }

    /**
     * Adds flicker to the firework effect.
     *
     * @return this builder instance
     */
    public FireworkEffectBuilder withFlicker() {
        builder.withFlicker();
        return this;
    }

    /**
     * Adds trail to the firework effect.
     *
     * @return this builder instance
     */
    public FireworkEffectBuilder withTrail() {
        builder.withTrail();
        return this;
    }

    /**
     * Builds and returns the configured {@link FireworkEffect}.
     *
     * @return the built firework effect
     */
    public FireworkEffect build() {
        return builder.build();
    }
}