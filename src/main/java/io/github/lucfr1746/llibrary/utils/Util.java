package io.github.lucfr1746.llibrary.utils;

import org.bukkit.Bukkit;

public class Util {

    public static final int GAME_MAIN_VERSION = Integer.parseInt(
            Bukkit.getBukkitVersion().split("-")[0].split("\\.")[0]);
    public static final int GAME_VERSION = Integer.parseInt(
            Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);
    public static final int GAME_SUB_VERSION = Bukkit.getBukkitVersion().split("-")[0].split("\\.").length < 3 ? 0 : Integer.parseInt(
            Bukkit.getBukkitVersion().split("-")[0].split("\\.")[2]);
    private static final boolean hasPaper = initPaper();
    private static final boolean hasFolia = initFolia();
    private static final boolean hasPurpur = initPurpur();

    /**
     * Inclusive
     * isVersionUpTo(1,9) on 1.9.0 is true
     */
    public static boolean isVersionUpTo(int mainVersion, int version) {
        return isVersionUpTo(mainVersion, version, 99);
    }

    /**
     * Inclusive
     * isVersionUpTo(1,9,4) on 1.9.4 is true
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
     * Inclusive
     * isVersionAfter(1,9) on 1.9.0 is true
     */
    public static boolean isVersionAfter(int mainVersion, int version) {
        return isVersionAfter(mainVersion, version, 0);
    }

    /**
     * Inclusive
     * isVersionAfter(1,9,4) on 1.9.4 is true
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
     * Inclusive
     */
    public static boolean isVersionInRange(int mainVersionMin, int versionMin,
                                           int mainVersionMax, int versionMax) {
        return isVersionInRange(mainVersionMin, versionMin, 0,
                mainVersionMax, versionMax, 99);
    }

    /**
     * Inclusive
     */
    public static boolean isVersionInRange(int mainVersionMin, int versionMin, int subVersionMin,
                                           int mainVersionMax, int versionMax, int subVersionMax) {
        return isVersionAfter(mainVersionMin, versionMin, subVersionMin)
                && isVersionUpTo(mainVersionMax, versionMax, subVersionMax);
    }

    private static boolean initPaper() {
        try {
            Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData");
            return true;
        } catch (NoClassDefFoundError | ClassNotFoundException ex) {
            return false;
        }
    }

    public static boolean hasPaperAPI() {
        return hasPaper;
    }

    private static boolean initPurpur() {
        try {
            Class.forName("org.purpurmc.purpur.event.PlayerAFKEvent");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean initFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean hasPurpurAPI() {
        return hasPurpur;
    }

    public static boolean hasFoliaAPI() {
        return hasFolia;
    }
}
