package dev.ithundxr.railwaystweaks;

import com.simibubi.create.content.equipment.wrench.WrenchItem;
import com.simibubi.create.content.trains.track.TrackBlock;
import dev.ithundxr.railwaystweaks.commands.RailwaysTweaksCommands;
import dev.ithundxr.railwaystweaks.compat.BanModCompat;
import dev.ithundxr.railwaystweaks.compat.create.recipe_trie.RecipeTrieFinder;
import dev.ithundxr.railwaystweaks.utils.MSPTTracker;
import dev.ithundxr.railwaystweaks.utils.UnfillableItemsCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
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

        var resourceManager = ResourceManagerHelper.get(PackType.SERVER_DATA);
        resourceManager.registerReloadListener(RecipeTrieFinder.LISTENER);
        resourceManager.registerReloadListener(UnfillableItemsCache.LISTENER);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
            BanModCompat.init();
    }

    public static ResourceLocation asResource(String id) {
        return new ResourceLocation(MODID, id);
    }
}
