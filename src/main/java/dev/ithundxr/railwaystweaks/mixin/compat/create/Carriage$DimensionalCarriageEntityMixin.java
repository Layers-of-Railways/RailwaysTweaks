package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Carriage.DimensionalCarriageEntity.class)
public class Carriage$DimensionalCarriageEntityMixin {
    @Final
    @Shadow(aliases = "this$0")
    Carriage this$0;

    @Shadow public Vec3 positionAnchor;

    @Inject(method = "createEntity", at = @At("HEAD"))
    private void railwaysTweaks$fixDimensionalTrainCrash(Level level, boolean loadPassengers, CallbackInfo ci) {
        ((CarriageAccessor) this$0).railwaysTweaks$getSerializedEntity().put(
                "Pos",
                railwaysTweaks$newDoubleList(
                        positionAnchor.x(),
                        positionAnchor.y(),
                        positionAnchor.z()
                )
        );
    }
    
    @WrapWithCondition(method = "createEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;moveTo(Lnet/minecraft/world/phys/Vec3;)V"))
    private boolean railwaysTweaks$fixDimensionalTrainCrash2(Entity instance, Vec3 vec) {
        return false;
    }

    @Unique
    private static ListTag railwaysTweaks$newDoubleList(double... pValues) {
        ListTag listtag = new ListTag();

        for (double d : pValues)
            listtag.add(DoubleTag.valueOf(d));

        return listtag;
    }
}
