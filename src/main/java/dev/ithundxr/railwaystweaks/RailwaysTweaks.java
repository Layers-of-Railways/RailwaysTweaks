package dev.ithundxr.railwaystweaks;

import dev.ithundxr.railwaystweaks.compat.BanModCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.MixinEnvironment;
import dev.ithundxr.railwaystweaks.commands.RailwaysTweaksCommands;

public class RailwaysTweaks implements ModInitializer {
    public static final String MODID = "railwaystweaks";
    public static final String NAME = "RailwaysTweaks";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    
    @Override
    public void onInitialize() {
        LOGGER.info("Railways Tweaks is loading...");

        RailwaysTweaksCommands.init();

        //if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
            BanModCompat.init();

        //if (FabricLoader.getInstance().isDevelopmentEnvironment())
        //    MixinEnvironment.getCurrentEnvironment().audit();
    }
}
