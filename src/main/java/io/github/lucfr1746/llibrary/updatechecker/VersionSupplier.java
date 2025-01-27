package io.github.lucfr1746.llibrary.updatechecker;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;

/**
 * Functional interface to supply the latest version of the plugin.
 * This is used in conjunction with {@link UpdateChecker#UpdateChecker(JavaPlugin, VersionSupplier)}.
 * The {@code getLatestVersionString} method should return the latest version of the plugin.
 */
@FunctionalInterface
public interface VersionSupplier {

    /**
     * Retrieves the latest version of your plugin.
     * This method is called asynchronously, so you should avoid accessing any Bukkit API directly.
     *
     * @return The latest version string of the plugin.
     * @throws IOException If an error occurs while retrieving the version (e.g., network issues).
     */
    String getLatestVersionString() throws IOException;
}
