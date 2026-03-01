package dev.ithundxr.railwaystweaks.mixin;

import com.mojang.authlib.GameProfile;
import dev.ithundxr.railwaystweaks.utils.UUIDReplacementManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "canPlayerLogin", at = @At("HEAD"), cancellable = true)
    private void blockReplacedLogins(SocketAddress socketAddress, GameProfile gameProfile, CallbackInfoReturnable<Component> cir) {
        if (UUIDReplacementManager.REPLACEMENTS.containsKey(gameProfile.getId())) {
            cir.setReturnValue(Component.literal("Your UUID is being redirected. Please login with your new account."));
        }
    }
}
