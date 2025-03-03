package io.github.lucfr1746.llibrary.action;

import org.bukkit.Sound;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ActionLoader {

    private final String input;

    private static final Map<String, Function<String, Action>> ACTIONS = new HashMap<>();

    static {
        ACTIONS.put(ActionType.CONSOLE.getIdentifier(), ConsoleAction::new);
        ACTIONS.put(ActionType.PLAYER.getIdentifier(), PlayerAction::new);
        ACTIONS.put(ActionType.CHAT.getIdentifier(), ChatAction::new);
        ACTIONS.put(ActionType.MESSAGE.getIdentifier(), MessageAction::new);
        ACTIONS.put(ActionType.BROADCAST.getIdentifier(), BroadcastAction::new);
        ACTIONS.put(ActionType.TAKE_MONEY.getIdentifier(), input -> new TakeMoneyAction(Double.parseDouble(input)));
        ACTIONS.put(ActionType.GIVE_MONEY.getIdentifier(), input -> new GiveMoneyAction(Double.parseDouble(input)));
        ACTIONS.put(ActionType.TAKE_EXP.getIdentifier(), input -> new TakeExpAction(Integer.parseInt(input)));
        ACTIONS.put(ActionType.GIVE_EXP.getIdentifier(), input -> new GiveExpAction(Integer.parseInt(input)));
        ACTIONS.put(ActionType.TAKE_PERM.getIdentifier(), TakePermissionAction::new);
        ACTIONS.put(ActionType.GIVE_PERM.getIdentifier(), GivePermissionAction::new);
    }

    public ActionLoader(String input) {
        this.input = input.stripLeading();
    }

    public Action getAction() {
        String[] args = input.split(" ", 2);
        Function<String, Action> actionCreator = ACTIONS.get(args[0]);

        if (actionCreator != null) {
            return actionCreator.apply(args.length > 1 ? args[1] : "");
        }

        return getSoundAction(args);
    }

    private Action getSoundAction(String[] args) {
        if (args.length < 2) return null;

        try {
            Sound sound = Sound.valueOf(args[1]);
            float volume = args.length > 2 ? parseFloatOrDefault(args[2]) : 1;
            float pitch = args.length > 3 ? parseFloatOrDefault(args[3]) : 1;

            return switch (args[0]) {
                case "BROADCAST_SOUND" -> new BroadcastSoundAction(sound, volume, pitch);
                case "BROADCAST_WORLD_SOUND" -> new BroadcastWorldSoundAction(sound, volume, pitch);
                case "SOUND" -> new SoundAction(sound, volume, pitch);
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
            return (float) 1;
        }
    }
}
