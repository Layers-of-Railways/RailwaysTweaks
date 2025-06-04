package dev.ithundxr.railwaystweaks.mixin.client.compat.copycatsplus;

import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(ScaledBlockAndTintGetter.class)
@SuppressWarnings("UnstableApiUsage")
public class ScaledBlockAndTintGetterMixin {
    @Shadow(remap = false) @Final protected Predicate<BlockPos> filter;
    @Shadow(remap = false) @Final protected BlockAndTintGetter wrapped;

    @Unique
    private int railwaysTweaks$origin$x;
    @Unique
    private int railwaysTweaks$origin$y;
    @Unique
    private int railwaysTweaks$origin$z;

    @Unique
    private int railwaysTweaks$scale$x;
    @Unique
    private int railwaysTweaks$scale$y;
    @Unique
    private int railwaysTweaks$scale$z;

    @Unique
    private int railwaysTweaks$originShift$x;
    @Unique
    private int railwaysTweaks$originShift$y;
    @Unique
    private int railwaysTweaks$originShift$z;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void initInts(String renderingProperty, BlockAndTintGetter wrapped, BlockPos origin, Vec3i originInner, Vec3i scale, Predicate<BlockPos> filter, CallbackInfo ci) {
        railwaysTweaks$origin$x = origin.getX();
        railwaysTweaks$origin$y = origin.getY();
        railwaysTweaks$origin$z = origin.getZ();

        railwaysTweaks$scale$x = scale.getX();
        railwaysTweaks$scale$y = scale.getY();
        railwaysTweaks$scale$z = scale.getZ();

        railwaysTweaks$originShift$x = originInner.getX() - railwaysTweaks$origin$x;
        railwaysTweaks$originShift$y = originInner.getX() - railwaysTweaks$origin$y;
        railwaysTweaks$originShift$z = originInner.getX() - railwaysTweaks$origin$z;
    }

    /**
     * @author Slimeist
     * @reason Violent optimizations
     */
    @Overwrite(remap = false)
    public BlockPos getTruePos(BlockPos pos) {
        // the original calculation was:
        // int x = t.o.x + (int) Math.floor(
        //     (double) (pos.x + t.oI.x - t.o.x) / (double) t.s.x
        // );
        return new BlockPos(
            railwaysTweaks$origin$x + Math.floorDiv(pos.getX() + railwaysTweaks$originShift$x, railwaysTweaks$scale$x),
            railwaysTweaks$origin$y + Math.floorDiv(pos.getY() + railwaysTweaks$originShift$y, railwaysTweaks$scale$y),
            railwaysTweaks$origin$z + Math.floorDiv(pos.getZ() + railwaysTweaks$originShift$z, railwaysTweaks$scale$z)
        );
    }

    @Unique
    private final BlockPos.MutableBlockPos railwaysTweaks$blockStatePos = new BlockPos.MutableBlockPos();

    @Unique
    private final Long2ObjectMap<BlockState> railwaysTweaks$blockStateCache = new Long2ObjectOpenHashMap<>();

    /**
     * @author Slimeist
     * @reason Violent optimizations
     */
    @Overwrite(remap = false)
    public @NotNull BlockState getBlockState(@NotNull BlockPos pPos) {
        long key = pPos.asLong();
        return railwaysTweaks$blockStateCache.computeIfAbsent(key, $ -> {
            if (!this.filter.test(pPos)) {
                return Blocks.AIR.defaultBlockState();
            } else {
                railwaysTweaks$blockStatePos.set(
                    railwaysTweaks$origin$x + Math.floorDiv(pPos.getX() + railwaysTweaks$originShift$x, railwaysTweaks$scale$x),
                    railwaysTweaks$origin$y + Math.floorDiv(pPos.getY() + railwaysTweaks$originShift$y, railwaysTweaks$scale$y),
                    railwaysTweaks$origin$z + Math.floorDiv(pPos.getZ() + railwaysTweaks$originShift$z, railwaysTweaks$scale$z)
                );
                return this.wrapped.getBlockState(railwaysTweaks$blockStatePos);
            }
        });
    }
}
