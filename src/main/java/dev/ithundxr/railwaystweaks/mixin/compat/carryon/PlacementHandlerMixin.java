package dev.ithundxr.railwaystweaks.mixin.compat.carryon;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tschipp.carryon.common.carry.PlacementHandler;

@Mixin(PlacementHandler.class)
public class PlacementHandlerMixin {
    @WrapOperation(method = "tryStackEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getBbHeight()F", ordinal = 0))
    private static float railwaysTweaks$fixMissingNullcheck(Entity instance, Operation<Float> original, @Cancellable CallbackInfo ci) {
        if (instance == null) {
            ci.cancel();
            return 0;
        }

        return original.call(instance);
    }
}
