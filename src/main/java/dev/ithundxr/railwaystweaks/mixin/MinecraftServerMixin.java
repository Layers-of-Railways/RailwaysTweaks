package dev.ithundxr.railwaystweaks.mixin;

import dev.ithundxr.railwaystweaks.mixinsupport.LevelLoadAppliers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "createLevels", at = @At("HEAD"))
    private void railwaysTweaks$setupEntityTypeFilters(ChunkProgressListener listener, CallbackInfo ci) {
        LevelLoadAppliers.init();
    }
}
