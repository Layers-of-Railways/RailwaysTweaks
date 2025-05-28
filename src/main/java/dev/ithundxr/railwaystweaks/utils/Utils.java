package dev.ithundxr.railwaystweaks.utils;

import net.fabricmc.loader.api.FabricLoader;

public class Utils {
    public static boolean isDevEnv() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
