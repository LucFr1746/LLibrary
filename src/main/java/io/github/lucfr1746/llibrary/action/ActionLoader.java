package io.github.lucfr1746.llibrary.action;

import io.github.lucfr1746.llibrary.action.list.*;
import org.bukkit.Sound;

import java.util.*;
import java.util.function.Function;

public class ActionLoader {

    private final Map<String, Function<String, Action>> actionFactories = new HashMap<>();

    public ActionLoader() {
        registerAction("[console]", ConsoleAction::new);
        registerAction("[player]", PlayerAction::new);
        registerAction("[chat]", ChatAction::new);
        registerAction("[message]", MessageAction::new);
        registerAction("[mini-message]", MiniMessageAction::new);
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

    public void registerAction(String identifier, Function<String, Action> factory) {
        actionFactories.put(identifier.toLowerCase(), factory);
    }

    public Action getAction(String input) {
        String[] args = input.split(" ", 2);
        Function<String, Action> factory = actionFactories.get(args[0].toLowerCase());

        if (factory != null) {
            return factory.apply(args.length > 1 ? args[1] : "");
        }

        return null;
    }

    public List<Action> getActions(List<String> inputs) {
        List<Action> actions = new ArrayList<>();
        for (String actionStr : inputs) {
            Action action = getAction(actionStr);
            if (action != null) actions.add(action);
        }
        return actions;
    }

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

    private float parseFloatOrDefault(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return 1.0f;
        }
    }
}
