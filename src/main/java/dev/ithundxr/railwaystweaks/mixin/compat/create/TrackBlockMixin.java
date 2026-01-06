package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.trains.track.TrackBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrackBlock.class)
public class TrackBlockMixin {

    @Inject(method = "onSneakWrenched", at = @At("HEAD"), cancellable = true)
    private void disableSneakWrench(BlockState state, UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        cir.setReturnValue(InteractionResult.SUCCESS);
    }
}
