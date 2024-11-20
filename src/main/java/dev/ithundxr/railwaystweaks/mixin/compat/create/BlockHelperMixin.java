package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.foundation.utility.BlockHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockHelper.class)
public class BlockHelperMixin {
    @ModifyExpressionValue(method = "placeSchematicBlock", at = @At(value = "INVOKE", target = "Ljava/lang/Object;equals(Ljava/lang/Object;)Z"))
    private static boolean railwayTweaks$fixSchematicannonCrash(boolean original, @Local(ordinal = 1) BlockEntity loaded) {
        return loaded != null && original;
    }
}
