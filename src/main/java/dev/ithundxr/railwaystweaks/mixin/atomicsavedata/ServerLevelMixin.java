package dev.ithundxr.railwaystweaks.mixin.atomicsavedata;

import dev.ithundxr.railwaystweaks.utils.IOUtilities;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
	@Inject(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;saveAll()V", shift = Shift.AFTER))
	private static void railwaysTweaks$waitUntilIOWorkerComplete(CallbackInfo ci) {
		IOUtilities.waitUntilIOWorkerComplete();
	}
}
