package dev.ithundxr.railwaystweaks.mixin;

import com.mojang.authlib.GameProfile;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(ServerStatusPacketListenerImpl.class)
public class ServerStatusPacketListenerImplMixin {
    @Shadow @Mutable @Final private ServerStatus status;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void railwaysTweaks$noSpying(ServerStatus status, Connection connection, CallbackInfo ci) {
        List<String> names = List.of("Stop", "Trying", "To", "Spy");
        List<GameProfile> profiles = new ArrayList<>();

        for (String name : names) {
            profiles.add(new GameProfile(UUID.randomUUID(), name));
        }

        Optional<ServerStatus.Players> players = Optional.of(new ServerStatus.Players(
                1, 1, profiles
        ));

        this.status = new ServerStatus(
                status.description(),
                players,
                status.version(),
                status.favicon(),
                status.enforcesSecureChat()
        );
    }
}
