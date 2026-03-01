package dev.ithundxr.railwaystweaks.mixin;

import dev.ithundxr.railwaystweaks.utils.UUIDReplacementManager;
import net.minecraft.core.UUIDUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(UUIDUtil.class)
public class UUIDUtilMixin {
    @Inject(method = "uuidFromIntArray", at = @At("RETURN"), cancellable = true)
    private static void replaceUUID(int[] bits, CallbackInfoReturnable<UUID> cir) {
        UUID id = cir.getReturnValue();
        UUID replaced = UUIDReplacementManager.REPLACEMENTS.get(id);
        if (replaced != null) {
            cir.setReturnValue(replaced);
        }
    }
}
