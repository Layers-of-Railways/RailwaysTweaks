package dev.ithundxr.railwaystweaks;

import dev.ithundxr.railwaystweaks.commands.RailwaysTweaksCommands;
import dev.ithundxr.railwaystweaks.compat.BanModCompat;
import dev.ithundxr.railwaystweaks.utils.MSPTTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RailwaysTweaks implements ModInitializer {

    public static final String MODID = "railwaystweaks";
    public static final String NAME = "RailwaysTweaks";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static final MSPTTracker MSPT_TRACKER = new MSPTTracker(200);

    @Override
    public void onInitialize() {
        LOGGER.info("Railways Tweaks is loading...");

        RailwaysTweaksCommands.init();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
            BanModCompat.init();
    }

    public static ResourceLocation asResource(String id) {
        return new ResourceLocation(MODID, id);
    }
}
