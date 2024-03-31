package dev.ithundxr.railwaystweaks.mixin.createdeco;

import com.github.talrey.createdeco.blocks.CatwalkRailingBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CatwalkRailingBlock.class)
public abstract class CatwalkRailingBlockMixin implements IWrenchable {
    @Shadow public static BooleanProperty fromDirection(Direction face) { throw new AssertionError(); }

    @Shadow @Final public static BooleanProperty EAST_FENCE;
    @Shadow @Final public static BooleanProperty WEST_FENCE;
    @Shadow @Final public static BooleanProperty NORTH_FENCE;
    @Shadow @Final public static BooleanProperty SOUTH_FENCE;

    /**
     * @author IThundxr
     * @reason Create deco load's client classes on the server;
     */
    @Overwrite
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Vec3 subbox = context.getClickLocation().subtract(pos.getCenter());
        Direction face = context.getClickedFace();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        var x = subbox.x;
        var z = subbox.z;

        if (level.isClientSide)
            return InteractionResult.PASS;

        //check if the top face is wrenched, remove side
        if (face == Direction.UP) {
            boolean bottomleft = x < -z;
            boolean topleft = x < z;
            var dir = Direction.WEST;
            if (!bottomleft && topleft) dir = Direction.SOUTH;
            if (!bottomleft && !topleft) dir = Direction.EAST;
            if (bottomleft && !topleft) dir = Direction.NORTH;
            if (bottomleft && topleft) dir = Direction.WEST;

            //obscure edge case where a corner of the top face cannot be wrenched
            if (state.getValue(fromDirection(dir))) {
                state = state.setValue(fromDirection(dir), false);
                level.setBlock(pos, state, 3);
                playRemoveSound(level, pos);
                if (!player.getAbilities().instabuild) player.addItem(new ItemStack(state.getBlock().asItem()));
                return InteractionResult.SUCCESS;
            }
            else return InteractionResult.PASS;
        }

        //check for wrenching the inside faces
        if (x == 0.375 || x == -0.375 || z == 0.375 || z == -0.375) state = state.setValue(fromDirection(face.getOpposite()), false);

        //check for wrenching the outside faces
        if (x == 0.5 || x == -0.5 || z == 0.5 || z == -0.5) {
            if (!state.getValue(fromDirection(face))) {
                if (x >= 0.375) state = state.setValue(EAST_FENCE, false);
                if (x <= -0.375) state = state.setValue(WEST_FENCE, false);
                if (z <= -0.375) state = state.setValue(NORTH_FENCE, false);
                if (z >= 0.375) state = state.setValue(SOUTH_FENCE, false);
            }
            else state = state.setValue(fromDirection(face), false);
        }

        level.setBlock(pos, state, 3);
        playRemoveSound(level, pos);
        if (!player.getAbilities().instabuild) player.addItem(new ItemStack(state.getBlock().asItem()));
        return InteractionResult.SUCCESS;
    }
}
