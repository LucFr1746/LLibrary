package io.github.lucfr1746.llibrary.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

/**
 * Utility class for version checks, text formatting, and platform detection.
 */
public class Util {

    /** Main version of the game (e.g., 1.x.x -> 1). */
    public static final int GAME_MAIN_VERSION = Integer.parseInt(
            Bukkit.getBukkitVersion().split("-")[0].split("\\.")[0]);

    /** Minor version of the game (e.g., x.21.x -> 21). */
    public static final int GAME_VERSION = Integer.parseInt(
            Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);

    /** Subversion of the game (e.g., x.x.4 -> 4 or 0 if not present). */
    public static final int GAME_SUB_VERSION = Bukkit.getBukkitVersion().split("-")[0].split("\\.").length < 3 ? 0 : Integer.parseInt(
            Bukkit.getBukkitVersion().split("-")[0].split("\\.")[2]);

    /** Whether Paper API is present. */
    private static final boolean hasPaper = initPaper();

    /** Whether Folia API is present. */
    private static final boolean hasFolia = initFolia();

    /** Whether Purpur API is present. */
    private static final boolean hasPurpur = initPurpur();

    /**
     * Formats text using MiniMessage and ChatColor.
     * Supports both legacy color codes: normal and hex.
     *
     * @param text The text to format.
     * @return The formatted text.
     */
    public static String formatText(String text) {
        text = MiniMessage.miniMessage().deserialize(text).toString();
        text = ChatColor.translateAlternateColorCodes('&', text);
        int from = 0;
        while (text.indexOf("&#", from) >= 0) {
            from = text.indexOf("&#", from) + 1;
            text = text.replace(text.substring(from - 1, from + 7),
                    net.md_5.bungee.api.ChatColor.of(text.substring(from, from + 7)).toString());
        }
        return text;
    }

    /**
     * Checks if the game version is at least the specified version.
     * Inclusive: isVersionUpTo(1,9) returns true for 1.9.0.
     *
     * @param mainVersion The main version to check (e.g., 1 for 1.9).
     * @param version The minor version to check (e.g., 9 for 1.9).
     * @return true if the current version is greater than or equal to the specified version.
     */
    public static boolean isVersionUpTo(int mainVersion, int version) {
        return isVersionUpTo(mainVersion, version, 99);
    }

    /**
     * Checks if the game version is at least the specified version.
     * Inclusive: isVersionUpTo(1,9,4) returns true for 1.9.4.
     *
     * @param mainVersion The main version to check.
     * @param version The minor version to check.
     * @param subVersion The subversion to check.
     * @return true if the current version is greater than or equal to the specified version.
     */
    public static boolean isVersionUpTo(int mainVersion, int version, int subVersion) {
        if (GAME_MAIN_VERSION > mainVersion)
            return false;
        if (GAME_MAIN_VERSION < mainVersion)
            return true;
        if (GAME_VERSION > version)
            return false;
        if (GAME_VERSION < version)
            return true;
        return GAME_SUB_VERSION <= subVersion;
    }

    /**
     * Checks if the game version is at most the specified version.
     * Inclusive: isVersionAfter(1,9) returns true for 1.9.0.
     *
     * @param mainVersion The main version to check.
     * @param version The minor version to check.
     * @return true if the current version is less than or equal to the specified version.
     */
    public static boolean isVersionAfter(int mainVersion, int version) {
        return isVersionAfter(mainVersion, version, 0);
    }

    /**
     * Checks if the game version is at most the specified version.
     * Inclusive: isVersionAfter(1,9,4) returns true for 1.9.4.
     *
     * @param mainVersion The main version to check.
     * @param version The minor version to check.
     * @param subVersion The subversion to check.
     * @return true if the current version is less than or equal to the specified version.
     */
    public static boolean isVersionAfter(int mainVersion, int version, int subVersion) {
        if (GAME_MAIN_VERSION < mainVersion)
            return false;
        if (GAME_MAIN_VERSION > mainVersion)
            return true;
        if (GAME_VERSION < version)
            return false;
        if (GAME_VERSION > version)
            return true;
        return GAME_SUB_VERSION >= subVersion;
    }

    /**
     * Checks if the game version falls within a range (inclusive).
     *
     * @param mainVersionMin The minimum main version.
     * @param versionMin The minimum minor version.
     * @param mainVersionMax The maximum main version.
     * @param versionMax The maximum minor version.
     * @return true if the current version falls within the specified range.
     */
    public static boolean isVersionInRange(int mainVersionMin, int versionMin,
                                           int mainVersionMax, int versionMax) {
        return isVersionInRange(mainVersionMin, versionMin, 0,
                mainVersionMax, versionMax, 99);
    }

    /**
     * Checks if the game version falls within a range (inclusive).
     *
     * @param mainVersionMin The minimum main version.
     * @param versionMin The minimum minor version.
     * @param subVersionMin The minimum subversion.
     * @param mainVersionMax The maximum main version.
     * @param versionMax The maximum minor version.
     * @param subVersionMax The maximum subversion.
     * @return true if the current version falls within the specified range.
     */
    public static boolean isVersionInRange(int mainVersionMin, int versionMin, int subVersionMin,
                                           int mainVersionMax, int versionMax, int subVersionMax) {
        return isVersionAfter(mainVersionMin, versionMin, subVersionMin)
                && isVersionUpTo(mainVersionMax, versionMax, subVersionMax);
    }

    /**
     * Initializes the Paper API check by attempting to load a Paper-specific class.
     *
     * @return {@code true} if Paper API is detected, {@code false} otherwise.
     */
    private static boolean initPaper() {
        try {
            Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData");
            return true;
        } catch (NoClassDefFoundError | ClassNotFoundException ex) {
            return false;
        }
    }

    /**
     * Checks if the Paper API is available.
     *
     * @return {@code true} if Paper API is present, {@code false} otherwise.
     */
    public static boolean hasPaperAPI() {
        return hasPaper;
    }

    /**
     * Initializes the Purpur API check by attempting to load a Purpur-specific class.
     *
     * @return {@code true} if Purpur API is detected, {@code false} otherwise.
     */
    private static boolean initPurpur() {
        try {
            Class.forName("org.purpurmc.purpur.event.PlayerAFKEvent");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Initializes the Folia API check by attempting to load a Folia-specific class.
     *
     * @return {@code true} if Folia API is detected, {@code false} otherwise.
     */
    private static boolean initFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Checks if the Purpur API is available.
     *
     * @return {@code true} if Purpur API is present, {@code false} otherwise.
     */
    public static boolean hasPurpurAPI() {
        return hasPurpur;
    }

    /**
     * Checks if the Folia API is available.
     *
     * @return {@code true} if Folia API is present, {@code false} otherwise.
     */
    public static boolean hasFoliaAPI() {
        return hasFolia;
    }
}
