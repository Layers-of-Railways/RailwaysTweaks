/**
 * Copied from Elytra-Bounce which is licensed under Apache 2.0
 * <p>
 * https://github.com/infernalstudios/Elytra-Bounce/blob/1d5374837a9fc426cb620deaf86925a2f0fc93ef/LICENSE
 */

package dev.ithundxr.railwaystweaks.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique private int elytraBounce$ticksOnGround = 0;
    @Unique private boolean elytraBounce$wasGoodBefore = false;

    @ModifyArg(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setSharedFlag(IZ)V"), index = 1)
    private boolean elytraBounce$travel(boolean in) {
        if (elytraBounce$ticksOnGround <= 5) {
            return true;
        }

        return in;
    }

    @Inject(method = "updateFallFlying()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setSharedFlag(IZ)V", shift = At.Shift.AFTER))
    private void elytraBounce$updateFallFlying(CallbackInfo ci, @Local boolean flag) {
        if (elytraBounce$wasGoodBefore && !flag && onGround()) {
            elytraBounce$ticksOnGround++;
            elytraBounce$wasGoodBefore = true;
            setSharedFlag(Entity.FLAG_FALL_FLYING, true);
            return;
        }

        elytraBounce$ticksOnGround = 0;
    }
}
