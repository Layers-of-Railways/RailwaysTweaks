package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import dev.ithundxr.railwaystweaks.mixinsupport.BasinBlockEntity_Duck;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BasinRecipe.class)
public class BasinRecipeMixin {
    @Redirect(
        method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z",
        at = @At(
            value = "INVOKE",
            target = "Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;getHeatLevelOf(Lnet/minecraft/world/level/block/state/BlockState;)Lcom/simibubi/create/content/processing/burner/BlazeBurnerBlock$HeatLevel;"
        )
    )
    private static BlazeBurnerBlock.HeatLevel useCachedHeat(BlockState state, BasinBlockEntity basin) {
        return ((BasinBlockEntity_Duck) basin).railwaysTweaks$getHeatLevel();
    }

    // no need to do BlockState lookups anymore
    @Redirect(
        method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;getFluidStorage(Lnet/minecraft/core/Direction;)Lnet/fabricmc/fabric/api/transfer/v1/storage/Storage;"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;getHeatLevelOf(Lnet/minecraft/world/level/block/state/BlockState;)Lcom/simibubi/create/content/processing/burner/BlazeBurnerBlock$HeatLevel;"
            )
        )
    )
    private static BlockState skipBlockState(Level instance, BlockPos pos) {
        return Blocks.AIR.defaultBlockState();
    }
}
