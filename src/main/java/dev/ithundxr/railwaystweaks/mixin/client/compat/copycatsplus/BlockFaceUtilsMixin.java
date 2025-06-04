package dev.ithundxr.railwaystweaks.mixin.client.compat.copycatsplus;

import com.copycatsplus.copycats.utility.BlockFaceUtils;
import dev.ithundxr.railwaystweaks.mixinsupport.BlockFaceUtils$faceMatch$Key;
import dev.ithundxr.railwaystweaks.mixinsupport.Object2BooleanCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.BiFunction;

@Mixin(BlockFaceUtils.class)
public abstract class BlockFaceUtilsMixin {
    @Shadow
    private static boolean processBlockFace(BlockGetter level, BlockState fromState, BlockPos fromPos, BlockState toState, BlockPos toPos, Direction fromFace, BiFunction<VoxelShape, VoxelShape, Boolean> operation) {
        throw new AssertionError();
    }

    @Unique
    private static final ThreadLocal<Object2BooleanCache<BlockFaceUtils$faceMatch$Key>> railwaysTweaks$faceMatchCache = ThreadLocal.withInitial(() -> new Object2BooleanCache<>(2048));

    /**
     * @author Slimeist
     * @reason Violent optimizations
     */
    @Overwrite
    public static boolean faceMatch(BlockGetter level, BlockState fromState, BlockPos fromPos, BlockState toState, BlockPos toPos, Direction fromFace) {
        BlockFaceUtils$faceMatch$Key key = new BlockFaceUtils$faceMatch$Key(fromPos.asLong(), toPos.asLong(), fromState, toState, fromFace);
        return railwaysTweaks$faceMatchCache.get().get(key, () ->
            processBlockFace(level, fromState, fromPos, toState, toPos, fromFace, (from, to) -> !Shapes.joinIsNotEmpty(from, to, BooleanOp.NOT_SAME))
        );
    }
}
