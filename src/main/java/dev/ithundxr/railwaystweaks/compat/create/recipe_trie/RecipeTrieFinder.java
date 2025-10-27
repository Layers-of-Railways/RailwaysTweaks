package dev.ithundxr.railwaystweaks.compat.create.recipe_trie;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

public class RecipeTrieFinder {
    private static final Cache<Object, RecipeTrie<?>> cachedTries = CacheBuilder.newBuilder().build();

    public static RecipeTrie<?> get(@NotNull Object cacheKey, Level world, Predicate<Recipe<?>> conditions) throws ExecutionException {
        return cachedTries.get(cacheKey, () -> {
            List<Recipe<?>> list = RecipeFinder.get(cacheKey, world, conditions);

            RecipeTrie.Builder<Recipe<?>> builder = RecipeTrie.builder();
            for (Recipe<?> recipe : list) {
                builder.insert(recipe);
            }

            return builder.build();
        });
    }

    public static final IdentifiableResourceReloadListener LISTENER = new SimpleSynchronousResourceReloadListener() {
        @Override
        public ResourceLocation getFabricId() {
            return RailwaysTweaks.asResource("recipe_finder");
        }

        @Override
        public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
            cachedTries.invalidateAll();
        }
    };
}
