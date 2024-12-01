package dev.ithundxr.railwaystweaks.mixin.compat.sereneseasons;

import dev.ithundxr.railwaystweaks.utils.TickCounter;
import glitchcore.event.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sereneseasons.season.RandomUpdateHandler;

@Mixin(RandomUpdateHandler.class)
public class RandomUpdateHandlerMixin {

    @Unique private static TickCounter railwaystweaks$tickCounter = new TickCounter(100);

    @Inject(method = "onWorldTick", at = @At("HEAD"), cancellable = true, remap = false)
    private static void railwaystweaks$reduceRandomTicks(TickEvent.Level event, CallbackInfo ci) {
        railwaystweaks$tickCounter.increment();
        if (!railwaystweaks$tickCounter.test()) ci.cancel();
    }
}
