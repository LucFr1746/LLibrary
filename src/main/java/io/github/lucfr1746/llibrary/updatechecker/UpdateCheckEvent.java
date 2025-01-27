package io.github.lucfr1746.llibrary.updatechecker;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called whenever an update check is finished.
 * It contains information about the update check result, success, and the CommandSenders who requested it.
 */
public class UpdateCheckEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UpdateChecker instance;
    private final UpdateCheckResult result;
    private final UpdateCheckSuccess success;
    private CommandSender[] requesters = null;

    /**
     * Constructs an {@link UpdateCheckEvent} with the given success status and associated {@link UpdateChecker}.
     *
     * @param success The {@link UpdateCheckSuccess} status indicating whether the update check was successful or failed.
     * @param checker The {@link UpdateChecker} instance that performed the update check.
     */
    protected UpdateCheckEvent(UpdateCheckSuccess success, UpdateChecker checker) {
        instance = checker;
        this.success = success;
        if (success == UpdateCheckSuccess.FAIL && instance.getLatestVersion() == null) {
            result = UpdateCheckResult.UNKNOWN;
        } else {
            if (instance.isUsingLatestVersion()) {
                result = UpdateCheckResult.RUNNING_LATEST_VERSION;
            } else {
                result = UpdateCheckResult.NEW_VERSION_AVAILABLE;
            }
        }
    }

    /**
     * Gets the {@link UpdateChecker} instance that performed the update check.
     *
     * @return The {@link UpdateChecker} instance that performed the update check.
     */
    public UpdateChecker getChecker() {
        return instance;
    }

    /**
     * Returns the list of handlers for this event.
     *
     * @return The list of handlers for this event.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Returns the latest version string found by the UpdateChecker, or null if all
     * previous checks have failed.
     *
     * @return Latest version string found by the UpdateChecker, or null if no version was found.
     */
    public String getLatestVersion() {
        return instance.getLatestVersion();
    }

    /**
     * Gets an array of all CommandSenders who have requested this update check.
     * Normally, this will either be the ConsoleCommandSender or a player.
     *
     * @return Array of all CommandSenders who have requested this update check, or null if none.
     */
    public CommandSender[] getRequesters() {
        if (requesters == null || requesters.length == 0)
            return null;
        return requesters;
    }

    /**
     * Sets the CommandSenders who requested this update check.
     *
     * @param requesters CommandSenders who requested this update check.
     * @return The current instance of {@link UpdateCheckEvent} for method chaining.
     */
    protected UpdateCheckEvent setRequesters(CommandSender... requesters) {
        this.requesters = requesters;
        return this;
    }

    /**
     * Gets the result of the update check, i.e., whether a new version is available or not.
     *
     * @return The {@link UpdateCheckResult} indicating the result of the update check.
     */
    public UpdateCheckResult getResult() {
        return result;
    }

    /**
     * Checks whether the update check attempt was successful or failed.
     *
     * @return The {@link UpdateCheckSuccess} status of this update check.
     */
    public UpdateCheckSuccess getSuccess() {
        return success;
    }

    /**
     * Gets the version string of the currently used plugin version.
     *
     * @return The version string of the currently used plugin version.
     */
    public String getUsedVersion() {
        return instance.getUsedVersion();
    }
}
