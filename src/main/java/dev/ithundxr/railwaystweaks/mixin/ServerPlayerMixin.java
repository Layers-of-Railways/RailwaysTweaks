package dev.ithundxr.railwaystweaks.mixin;

import net.fabricmc.fabric.impl.event.interaction.FakePlayerNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Shadow public ServerGamePacketListenerImpl connection;

    @Inject(method = "tick", at = @At("HEAD"))
    private void railwaysTweaks$preventNullNetworkHandlerCrash(CallbackInfo ci) {
        if (connection == null)
            //noinspection UnstableApiUsage
            connection = new FakePlayerNetworkHandler((ServerPlayer) (Object) this);
    }
}
