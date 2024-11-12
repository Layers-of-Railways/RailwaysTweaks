package dev.ithundxr.railwaystweaks.mixin.compat.enchancement;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlideComponent.class)
public class SlideComponentMixin {
    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "doubleValue=1"))
    private static double railwayTweaks$modifySlideAttribute(double original) {
        return 0;
    }
}
