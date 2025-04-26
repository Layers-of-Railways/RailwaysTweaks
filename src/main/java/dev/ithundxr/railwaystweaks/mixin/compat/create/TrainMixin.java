package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Train.class)
public class TrainMixin {

    @Redirect(
            method="collideWithOtherTrains",
            at=@At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/trains/entity/Train;findCollidingTrain(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/resources/ResourceKey;)Lcom/simibubi/create/foundation/utility/Pair;"
            )
    )
    private Pair<Train, Vec3> railwayTweaks$removeTrainCollisions(Train instance, Level otherLeading, Vec3 otherTrailing, Vec3 otherDimension, ResourceKey<Level> start2) {
        return null;
    }
}
