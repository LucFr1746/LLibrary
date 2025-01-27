package io.github.lucfr1746.llibrary.updatechecker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Builds a User-Agent string for the update checker.
 * The string starts with "JEFF-Media-GbR-SpigotUpdateChecker/[version]"
 * followed by parameters like plugin version, Bukkit version, server version,
 * and others, to provide relevant metadata.
 */
public class UserAgentBuilder {

    private final StringBuilder builder = new StringBuilder("JEFF-Media-GbR-SpigotUpdateChecker/").append(UpdateChecker.VERSION);
    private final UpdateChecker instance;
    private final List<String> list = new ArrayList<>();
    private final Plugin plugin;

    /**
     * Constructs a UserAgentBuilder for the specified UpdateChecker instance.
     *
     * @param checker The UpdateChecker instance used to create the User-Agent string.
     */
    public UserAgentBuilder(UpdateChecker checker) {
        this.instance = checker;
        this.plugin = instance.getPlugin();
    }

    /**
     * Adds the Bukkit version to the User-Agent string.
     * Example: "BukkitVersion/1.16.5-R0.1-SNAPSHOT".
     *
     * @return The current UserAgentBuilder instance.
     */
    public UserAgentBuilder addBukkitVersion() {
        list.add("BukkitVersion/" + Bukkit.getBukkitVersion());
        return this;
    }

    /**
     * Adds a custom key/value pair to the User-Agent string.
     * Example: "foo/bar".
     *
     * @param key The key part of the key-value pair.
     * @param value The value part of the key-value pair.
     * @return The current UserAgentBuilder instance.
     */
    public UserAgentBuilder addKeyValue(String key, String value) {
        list.add(key + "/" + value);
        return this;
    }

    /**
     * Adds a plain text string to the User-Agent string.
     * Example: "foo".
     *
     * @param text The custom string to add.
     * @return The current UserAgentBuilder instance.
     */
    public UserAgentBuilder addPlaintext(String text) {
        list.add(text);
        return this;
    }

    /**
     * Adds the plugin name and version to the User-Agent string.
     * Example: "AngelChest/3.11.0".
     *
     * @return The current UserAgentBuilder instance.
     */
    public UserAgentBuilder addPluginNameAndVersion() {
        list.add(plugin.getName() + "/" + plugin.getDescription().getVersion());
        return this;
    }

    /**
     * Adds the server version to the User-Agent string.
     * Example: "ServerVersion/git-Paper-584 (MC: 1.16.5)".
     *
     * @return The current UserAgentBuilder instance.
     */
    public UserAgentBuilder addServerVersion() {
        list.add("ServerVersion/" + Bukkit.getVersion());
        return this;
    }

    /**
     * Adds the Spigot User ID to the User-Agent string, if the plugin is paid.
     * Example: "SpigotUID/175238".
     *
     * @return The current UserAgentBuilder instance.
     */
    public UserAgentBuilder addSpigotUserId() {
        String uid = instance.isUsingPaidVersion() ? instance.getSpigotUserId() : "none";
        list.add("SpigotUID/" + uid);
        return this;
    }

    /**
     * Adds information whether the plugin is a paid version from SpigotMC.org.
     * Example: "Paid/true".
     *
     * @return The current UserAgentBuilder instance.
     */
    public UserAgentBuilder addUsingPaidVersion() {
        list.add("Paid/" + instance.isUsingPaidVersion());
        return this;
    }

    /**
     * Builds the final User-Agent string by concatenating all the parameters.
     *
     * @return The final User-Agent string.
     */
    protected String build() {
        if (!list.isEmpty()) {
            builder.append(" (");
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                builder.append(it.next());
                if (it.hasNext()) {
                    builder.append(", ");
                }
            }
            builder.append(")");
        }
        return builder.toString();
    }
}
