package dev.ithundxr.railwaystweaks.mixin.compat.tconstruct;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.ModifierManager;
import slimeknights.tconstruct.library.modifiers.util.LazyModifier;

@Mixin(LazyModifier.class)
public class LazyModifierMixin {
    @Inject(
            method = "get()Lslimeknights/tconstruct/library/modifiers/Modifier;",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void railwayTweaks$suppressException(CallbackInfoReturnable<Modifier> cir) {
        if (!ModifierManager.INSTANCE.isDynamicModifiersLoaded())
            cir.setReturnValue(ModifierManager.INSTANCE.getDefaultValue());
    }
}
