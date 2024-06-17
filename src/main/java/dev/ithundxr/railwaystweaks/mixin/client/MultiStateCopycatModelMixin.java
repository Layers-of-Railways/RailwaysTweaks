package dev.ithundxr.railwaystweaks.mixin.client;

import com.copycatsplus.copycats.content.copycat.base.model.multistate.fabric.MultiStateCopycatModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.Set;

@Pseudo
@Mixin(MultiStateCopycatModel.class)
public class MultiStateCopycatModelMixin {
    @Unique
    private Set<Map.Entry<String, BlockState>> railwaysTweaks$set;
    
    @WrapOperation(method = "emitBlockQuads", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
    private Set<Map.Entry<String, BlockState>> railwaysTweaks$fixCopycatPlusCME(Map<String, BlockState> instance, Operation<Set<Map.Entry<String, BlockState>>> original) {
        railwaysTweaks$set = original.call(instance);
        return railwaysTweaks$set;
    }
}
