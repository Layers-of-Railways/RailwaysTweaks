package dev.ithundxr.railwaystweaks.mixinsupport;

public class LevelLoadAppliers {
    private static boolean ran = false;

    public static void init() {
        if (ran) {
            return;
        }
        ran = true;

        FlatBlockstateArray.apply();
    }
}
