package dev.ithundxr.railwaystweaks.mixin.compat.dcintegration;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.erdbeerbaerlp.dcintegration.fabric.util.FabricMessageUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FabricMessageUtils.class)
public class FabricMessageUtilsMixin {
    @ModifyExpressionValue(method = "genItemStackEmbedIfAvailable", at = @At(value = "INVOKE", target = "Ljava/lang/StringBuilder;toString()Ljava/lang/String;"))
    private static String railwaysTweaks$fixCrashWhenDescriptionIsNull(String original) {
        return original == null ? "" : original;
    }
}
