package io.github.lucfr1746.llibrary.utils;

public class Util {

    private static final boolean hasPaper = initPaper();

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
}
