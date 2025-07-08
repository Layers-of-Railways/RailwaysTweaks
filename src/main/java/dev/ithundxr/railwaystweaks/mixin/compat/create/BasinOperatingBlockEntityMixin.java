package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(BasinOperatingBlockEntity.class)
public abstract class BasinOperatingBlockEntityMixin extends KineticBlockEntity {

    @Shadow protected abstract Optional<BasinBlockEntity> getBasin();

    @Shadow protected abstract Object getRecipeCacheKey();

    @Shadow protected abstract <C extends Container> boolean matchStaticFilters(Recipe<C> recipe);

    @Shadow protected abstract <C extends Container> boolean matchBasinRecipe(Recipe<C> recipe);

    private BasinOperatingBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    /**
     * @author Slimeist
     * @reason Replace stream with for loops to help with performance
     */
    @Overwrite(remap = false)
    protected List<Recipe<?>> getMatchingRecipes() {
        if (getBasin().map(BasinBlockEntity::isEmpty)
            .orElse(true))
            return new ArrayList<>();

        List<Recipe<?>> list = new ArrayList<>();
        for (Recipe<?> r : RecipeFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters))
            if (matchBasinRecipe(r))
                list.add(r);

        list.sort((r1, r2) -> r2.getIngredients().size() - r1.getIngredients().size());

        return list;
    }
}
