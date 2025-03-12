package io.github.lucfr1746.llibrary.action;

import io.github.lucfr1746.llibrary.action.list.*;
import org.bukkit.Sound;

import java.util.*;
import java.util.function.Function;

/**
 * Loads and manages different types of actions in the plugin.
 * Provides a way to register, retrieve, and process actions based on string inputs.
 */
public class ActionLoader {

    private final Map<String, Function<String, Action>> actionFactories = new HashMap<>();

    /**
     * Initializes the ActionLoader and registers all available actions.
     */
    public ActionLoader() {
        registerAction("[console]", ConsoleAction::new);
        registerAction("[player]", PlayerAction::new);
        registerAction("[chat]", ChatAction::new);
        registerAction("[message]", MessageAction::new);
        registerAction("[mini-message]", MiniMessageAction::new);
        registerAction("[open-menu]", OpenMenuAction::new);
        registerAction("[broadcast]", BroadcastAction::new);
        registerAction("[broadcast-world]", BroadcastWorldAction::new);
        registerAction("[take-money]", input -> new TakeMoneyAction(Double.parseDouble(input)));
        registerAction("[give-money]", input -> new GiveMoneyAction(Double.parseDouble(input)));
        registerAction("[take-exp]", input -> new TakeExpAction(Integer.parseInt(input)));
        registerAction("[give-exp]", input -> new GiveExpAction(Integer.parseInt(input)));
        registerAction("[take-permission]", TakePermissionAction::new);
        registerAction("[give-permission]", GivePermissionAction::new);
        registerAction("[sound]", input -> parseSoundAction("[sound]", input));
        registerAction("[broadcast-sound]", input -> parseSoundAction("[broadcast-sound]", input));
        registerAction("[broadcast-world-sound]", input -> parseSoundAction("[broadcast-world-sound]", input));
    }

    /**
     * Registers an action with a specific identifier.
     * @param identifier The string identifier for the action.
     * @param factory The function that creates the action instance.
     */
    public void registerAction(String identifier, Function<String, Action> factory) {
        actionFactories.put(identifier.toLowerCase(), factory);
    }

    /**
     * Retrieves an Action instance based on an input string.
     * @param input The input string defining the action.
     * @return The corresponding Action instance or null if not found.
     */
    public Action getAction(String input) {
        String[] args = input.split(" ", 2);
        Function<String, Action> factory = actionFactories.get(args[0].toLowerCase());

        if (factory != null) {
            return factory.apply(args.length > 1 ? args[1] : "");
        }

        return null;
    }

    /**
     * Retrieves a list of Action instances based on a list of input strings.
     * @param inputs A list of action input strings.
     * @return A list of corresponding Action instances.
     */
    public List<Action> getActions(List<String> inputs) {
        List<Action> actions = new ArrayList<>();
        for (String actionStr : inputs) {
            Action action = getAction(actionStr);
            if (action != null) actions.add(action);
        }
        return actions;
    }

    /**
     * Parses a sound action from an input string.
     * @param type The type of sound action.
     * @param input The input defining the sound, volume, and pitch.
     * @return A corresponding sound action or null if invalid.
     */
    private Action parseSoundAction(String type, String input) {
        String[] args = input.split(" ");
        if (args.length < 1) return null;
        try {
            Sound sound = Sound.valueOf(args[0].toUpperCase());
            float volume = args.length > 1 ? parseFloatOrDefault(args[1]) : 1;
            float pitch = args.length > 2 ? parseFloatOrDefault(args[2]) : 1;

            return switch (type) {
                case "[broadcast-sound]" -> new BroadcastSoundAction(sound, volume, pitch);
                case "[broadcast-world-sound]" -> new BroadcastWorldSoundAction(sound, volume, pitch);
                case "[sound]" -> new SoundAction(sound, volume, pitch);
                default -> null;
            };
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid sound: " + args[1]);
        }
    }

    /**
     * Parses a float value from a string, returning a default value if invalid.
     * @param value The string to parse.
     * @return The parsed float value or 1.0f if parsing fails.
     */
    private float parseFloatOrDefault(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return 1.0f;
        }
    }
}