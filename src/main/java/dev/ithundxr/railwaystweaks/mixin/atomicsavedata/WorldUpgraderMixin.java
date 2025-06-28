package dev.ithundxr.railwaystweaks.mixin.atomicsavedata;

import dev.ithundxr.railwaystweaks.utils.IOUtilities;
import net.minecraft.util.worldupdate.WorldUpgrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldUpgrader.class)
public class WorldUpgraderMixin {
	@Inject(method = "work", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/DimensionDataStorage;save()V", shift = Shift.AFTER))
	private static void railwaysTweaks$waitUntilIOWorkerComplete(CallbackInfo ci) {
		IOUtilities.waitUntilIOWorkerComplete();
	}
}
