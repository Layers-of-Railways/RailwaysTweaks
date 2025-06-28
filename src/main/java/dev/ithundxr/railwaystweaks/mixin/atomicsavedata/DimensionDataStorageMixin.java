package dev.ithundxr.railwaystweaks.mixin.atomicsavedata;

import dev.ithundxr.railwaystweaks.utils.IOUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;

@Mixin(DimensionDataStorage.class)
public class DimensionDataStorageMixin {
	@Shadow @Final private File dataFolder;

	@Inject(method = "readTagFromDisk", at = @At("TAIL"))
	private void railwaysTweaks$cleanLeftoverTempFiles(String name, int levelVersion, CallbackInfoReturnable<CompoundTag> cir) throws IOException {
		IOUtilities.cleanupTempFiles(dataFolder.toPath(), name);
	}
}
