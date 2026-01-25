package dev.ithundxr.railwaystweaks.utils;

import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

public class UnfillableItemsCache extends TickedCacheSet<ResourceLocation> {

    public static final UnfillableItemsCache INSTANCE = new UnfillableItemsCache();

    private UnfillableItemsCache() {
        super(1000, 10 * 60);
    }

    public static final IdentifiableResourceReloadListener LISTENER = new SimpleSynchronousResourceReloadListener() {
        @Override
        public ResourceLocation getFabricId() {
            return RailwaysTweaks.asResource("unfillable_items_cache");
        }

        @Override
        public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
            INSTANCE.clearAndReset();
        }
    };
}
