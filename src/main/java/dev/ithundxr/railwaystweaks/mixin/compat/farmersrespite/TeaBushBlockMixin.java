package dev.ithundxr.railwaystweaks.mixin.compat.farmersrespite;

import com.chefsdelights.farmersrespite.common.block.TeaBushBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.chefsdelights.farmersrespite.common.block.TeaBushBlock.AGE;

@Mixin(TeaBushBlock.class)
public class TeaBushBlockMixin {
    @Inject(
            method = "performBonemeal",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stopGrowthAcceleratorCrash(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        int i = blockState.getValue(AGE);
        if (i > 2) ci.cancel();
    }
}
