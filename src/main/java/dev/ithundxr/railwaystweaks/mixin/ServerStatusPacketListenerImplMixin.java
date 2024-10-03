package dev.ithundxr.railwaystweaks.mixin;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.network.ServerStatusPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Optional;

@Mixin(ServerStatusPacketListenerImpl.class)
public class ServerStatusPacketListenerImplMixin {
    @Shadow @Mutable @Final private ServerStatus status;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void railwaysTweaks$noSpying(ServerStatus status, Connection connection, CallbackInfo ci) {
        if (Boolean.getBoolean("railwaystweaks.mask.players")) {
            this.status = new ServerStatus(
                    status.description(),
                    Optional.of(new ServerStatus.Players(0, 0, Collections.emptyList())),
                    status.version(),
                    status.favicon(),
                    status.enforcesSecureChat()
            );
        }
    }
}
