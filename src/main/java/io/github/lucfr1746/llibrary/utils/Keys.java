package io.github.lucfr1746.llibrary.utils;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility class for managing and creating NamespacedKey constants used across the library.
 */
public class Keys {

    /**
     * Creates a NamespacedKey with the "minecraft" namespace and the given postfix.
     *
     * @param postfix the postfix to append to the "minecraft" namespace
     * @return a new NamespacedKey instance
     */
    private static NamespacedKey craftKey(String postfix) {
        return new NamespacedKey(NamespacedKey.MINECRAFT, postfix);
    }

    /**
     * Abstract class defining various effect types as NamespacedKey constants.
     */
    public static abstract class EffectType implements Keyed {

        /** Key representing the application of effects. */
        public static final NamespacedKey APPLY_EFFECTS = craftKey("apply_effects");
        /** Key representing the removal of effects. */
        public static final NamespacedKey REMOVE_EFFECTS = craftKey("remove_effects");
        /** Key representing the clearing of all effects. */
        public static final NamespacedKey CLEAR_ALL_EFFECTS = craftKey("clear_all_effects");
        /** Key representing the random teleportation effect. */
        public static final NamespacedKey TELEPORT_RANDOMLY = craftKey("teleport_randomly");
        /** Key representing the playing of sound effect. */
        public static final NamespacedKey PLAY_SOUND = craftKey("play_sound");
    }

    /**
     * Abstract class defining component keys used for food and consumable properties.
     */
    public static abstract class Component implements Keyed {

        /** Key representing food-related properties. */
        public static final NamespacedKey FOOD = craftKey("food");
        /** Key representing consumable-related properties. */
        public static final NamespacedKey CONSUMABLE = craftKey("consumable");
        /** Key representing the item to remain after consumption. */
        public static final NamespacedKey USE_REMAINDER = craftKey("use_remainder");
        /** Alias for consumable component to support cross-version compatibility. */
        public static final NamespacedKey CROSS_VERSION_CONSUMABLE = CONSUMABLE;
    }

    /**
     * A simple registry implementation that maps NamespacedKey to Keyed instances.
     *
     * @param <T> the type of Keyed elements to be stored in the registry
     */
    private static class KeyRegistry<T extends Keyed> implements Registry<T> {
        private final LinkedHashMap<NamespacedKey, T> values = new LinkedHashMap<>();

        /**
         * Constructs a new KeyRegistry with the given collection of Keyed elements.
         *
         * @param collection a collection of Keyed elements to be registered
         */
        public KeyRegistry(Collection<T> collection) {
            collection.forEach((v) -> this.values.put(v.getKey(), v));
        }

        /**
         * Constructs a new KeyRegistry with the given array of Keyed elements.
         *
         * @param collection an array of Keyed elements to be registered
         */
        @SafeVarargs
        public KeyRegistry(T... collection) {
            for (T value : collection)
                this.values.put(value.getKey(), value);
        }

        /**
         * Retrieves the element associated with the given NamespacedKey.
         *
         * @param namespacedKey the key to look up
         * @return the corresponding element, or {@code null} if not found
         */
        @Nullable
        @Override
        public T get(@NotNull NamespacedKey namespacedKey) {
            return values.get(namespacedKey);
        }

        /**
         * Retrieves the element associated with the given NamespacedKey or throws an exception if not found.
         *
         * @param namespacedKey the key to look up
         * @return the corresponding element
         * @throws IllegalArgumentException if no element is associated with the given key
         */
        @NotNull
        @Override
        public T getOrThrow(@NotNull NamespacedKey namespacedKey) {
            if (!values.containsKey(namespacedKey))
                throw new IllegalArgumentException("No element found for key: " + namespacedKey);
            return values.get(namespacedKey);
        }

        /**
         * Returns a sequential {@code Stream} with this registry as its source.
         *
         * @return a {@code Stream} of the registry elements
         */
        @NotNull
        @Override
        public Stream<T> stream() {
            return StreamSupport.stream(this.spliterator(), false);
        }

        /**
         * Returns an iterator over the elements in the registry.
         *
         * @return an {@code Iterator} of the registry elements
         */
        @NotNull
        @Override
        public Iterator<T> iterator() {
            return values.values().iterator();
        }
    }
}
