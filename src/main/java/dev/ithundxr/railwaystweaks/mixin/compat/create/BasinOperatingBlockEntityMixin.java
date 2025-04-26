package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(value = BasinOperatingBlockEntity.class, remap = false)
public abstract class BasinOperatingBlockEntityMixin {

    @Shadow protected abstract Optional<BasinBlockEntity> getBasin();
    @Shadow protected abstract Object getRecipeCacheKey();

    @Shadow protected abstract <C extends Container> boolean matchStaticFilters(Recipe<C> recipe);
    @Shadow protected abstract <C extends Container> boolean matchBasinRecipe(Recipe<C> recipe);

    /**
     * @author Sleepy Evelyn
     * @reason Improve Basin recipe lookup speed
     */
    @Overwrite
    protected List<Recipe<?>> getMatchingRecipes() {
        if (getBasin().map(BasinBlockEntity::isEmpty).orElse(true))
            return(new ArrayList<>());

        var basinBE = getBasin().get();
        List<Recipe<?>> list = new ArrayList<>();

        for (var recipe : RecipeFinder.get(getRecipeCacheKey(), basinBE.getLevel(), this::matchStaticFilters))
            if (matchBasinRecipe(recipe))
                list.add(recipe);

        list.sort((r1, r2) -> r2.getIngredients().size() - r1.getIngredients().size());
        return list;
    }
}
