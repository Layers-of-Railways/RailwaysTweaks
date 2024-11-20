package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.foundation.utility.BlockHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockHelper.class)
public class BlockHelperMixin {
    @Redirect(method = "placeSchematicBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;getType()Lnet/minecraft/world/level/block/entity/BlockEntityType;", ordinal = 1))
    private static BlockEntityType<?> railwayTweaks$fixSchematicannonCrash(BlockEntity instance, @Local(ordinal = 1) BlockEntity loaded) {
        return loaded != null ? instance.getType() : null;
    }
}
