package dev.ithundxr.railwaystweaks.mixin.createdeco;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.blocks.CatwalkStairBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CatwalkStairBlock.class)
public class CatwalkStairBlockMixin implements IWrenchable {
    @Shadow @Final public static BooleanProperty RAILING_RIGHT;
    @Shadow @Final public static BooleanProperty RAILING_LEFT;
    @Shadow @Final public String metal;

    /**
     * @author IThundxr
     * @reason Create deco load's client classes on the server;
     */
    @Overwrite
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Vec3 subbox = context.getClickLocation().subtract(pos.getCenter());
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (state.getValue(RAILING_RIGHT) || state.getValue(RAILING_LEFT)) {
            var xPos = subbox.x;
            var zPos = subbox.z;

            var dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean left = false;

            if (dir == Direction.NORTH) left = xPos > 0;
            if (dir == Direction.SOUTH) left = xPos < 0;
            if (dir == Direction.EAST) left = zPos > 0;
            if (dir == Direction.WEST) left = zPos < 0;

            if (level.isClientSide || !state.getValue(left ? CatwalkStairBlock.RAILING_LEFT : CatwalkStairBlock.RAILING_RIGHT))
                return InteractionResult.PASS;

            level.setBlock(pos, state.setValue(left ? CatwalkStairBlock.RAILING_LEFT : CatwalkStairBlock.RAILING_RIGHT, false), 3);

            if (!player.getAbilities().instabuild) player.addItem(new ItemStack(
                    BlockRegistry.CATWALK_RAILINGS.get(metal)
            ));
            playRemoveSound(level, pos);
            return InteractionResult.SUCCESS;
        }

        level.removeBlock(pos, false);
        if (!player.getAbilities().instabuild) player.addItem(new ItemStack(state.getBlock().asItem()));
        playRemoveSound(level, pos);
        return InteractionResult.SUCCESS;
    }
}
