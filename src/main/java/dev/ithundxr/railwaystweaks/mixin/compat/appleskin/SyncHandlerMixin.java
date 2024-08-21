package dev.ithundxr.railwaystweaks.mixin.compat.appleskin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import squeek.appleskin.network.SyncHandler;

@Pseudo
@Mixin(SyncHandler.class)
public class SyncHandlerMixin {
    @Inject(method = "onPlayerUpdate", at = @At("HEAD"), cancellable = true)
    private static void railwaysTweaks$fixAppleSkinCrash(ServerPlayer player, CallbackInfo ci) {
        if (player.connection == null)
            ci.cancel();
    }
}
