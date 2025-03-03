package io.github.lucfr1746.llibrary.action;

import org.jetbrains.annotations.NotNull;

public enum ActionType {
    CONSOLE("[console]", "Execute a command from the console"),
    PLAYER("[player]", "Execute a command for the player"),
    CHAT("[chat]", "Send a chat message as the player performing the action"),
    MESSAGE("[message]", "Send a message to the player"),
    BROADCAST("[broadcast]", "Broadcast a message to the server"),
    BROADCAST_SOUND("[broadcastsound]", "Broadcast a sound to the server"),
    BROADCAST_WORLD_SOUND("[broadcastworld]", "Broadcast a sound to the player's world"),
    SOUND("[sound]", "Play a sound for a the specific player"),
    TAKE_MONEY("[takemoney]", "Take money from a player (requires Vault)"),
    GIVE_MONEY("[givemoney]", "Give money to a player (requires Vault)"),
    TAKE_EXP("[takeexp]", "Take exp points/levels from a player"),
    GIVE_EXP("[giveexp]", "Give exp points/levels to a player"),
    TAKE_PERM("[takepermission]", "Take a permission from a player (requires Vault)"),
    GIVE_PERM("[givepermission]", "Give a permission to a player (requires Vault)");

    private final String identifier;
    private final String description;

    ActionType(@NotNull final String identifier, @NotNull final String description) {
        this.identifier = identifier;
        this.description = description;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getDescription() {
        return this.description;
    }
}
