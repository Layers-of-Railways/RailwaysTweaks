package dev.ithundxr.railwaystweaks.mixin.client.compat.create;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import io.github.fabricators_of_create.porting_lib.util.StorageProvider;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(BasinBlockEntity.class)
public class BasinBlockEntityMixin {
    @Shadow @Final private Map<Direction, Pair<StorageProvider<ItemVariant>, StorageProvider<FluidVariant>>> spoutputOutputs;

    @Unique
    private final Map<Direction, Pair<StorageProvider<ItemVariant>, StorageProvider<FluidVariant>>> railwaysTweaks$backupSpoutputOutputs = new HashMap<>();

    // BlockEntityRenderHelper.renderBlockEntities calls setLevel with the VirtualRenderWorld and then the ClientWorld
    // when rendering a contraption, which causes huge numbers of Client*LookupCache instances to be created.
    // We can simply restore the previous spoutputOutputs after the render, since they are still perfectly valid
    @Inject(method = "setLevel", at = @At(value = "INVOKE", target = "Ljava/util/Map;clear()V"), cancellable = true)
    private void preventGCExplosion(Level level, CallbackInfo ci) {
        if (level instanceof VirtualRenderWorld) {
            railwaysTweaks$backupSpoutputOutputs.clear();
            railwaysTweaks$backupSpoutputOutputs.putAll(spoutputOutputs);
            // Create code is safe to run here, since it just gets Empty*LookupCache instances
        } else if (!railwaysTweaks$backupSpoutputOutputs.isEmpty()) {
            spoutputOutputs.clear();
            spoutputOutputs.putAll(railwaysTweaks$backupSpoutputOutputs);
            railwaysTweaks$backupSpoutputOutputs.clear();
            ci.cancel(); // cancel Create code, which will memory spam huge numbers of Client*LookupCache instances
        }
    }
}
