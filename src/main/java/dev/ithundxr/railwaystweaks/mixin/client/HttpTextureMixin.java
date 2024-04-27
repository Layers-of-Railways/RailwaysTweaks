package dev.ithundxr.railwaystweaks.mixin.client;

import net.minecraft.client.renderer.texture.HttpTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HttpTexture.class)
public class HttpTextureMixin {
    @Inject(method = "setNoAlpha", at = @At("HEAD"), cancellable = true)
    private static void railwaysTweaks$noAlpha(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "doNotchTransparencyHack", at = @At("HEAD"), cancellable = true)
    private static void railwaysTweaks$noTransparencyHack(CallbackInfo ci) {
        ci.cancel();
    }
}
