package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Train.class, priority = 1000000)
public abstract class TrainMixin {

    @WrapOperation(
            method = "collideWithOtherTrains",
            at=@At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/trains/entity/Train;findCollidingTrain(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/resources/ResourceKey;)Lcom/simibubi/create/foundation/utility/Pair;"
            )
    )
    private Pair<Train, Vec3> railwayTweaks$removeTrainCollisions(Train instance, Level otherLeading, Vec3 otherTrailing, Vec3 otherDimension, ResourceKey<Level> start2, Operation<Pair<Train, Vec3>> original) {
        return null;
    }

    @ModifyExpressionValue(
            method = "updateNavigationTarget",
            at = @At(value = "CONSTANT", args = "intValue=100"),
            remap = false
    )
    private int increaseFullRefreshDistance(int original) {
        // Reduce navigation calls by increasing the distance before a new full calculation is made
        return 200;
    }
}
