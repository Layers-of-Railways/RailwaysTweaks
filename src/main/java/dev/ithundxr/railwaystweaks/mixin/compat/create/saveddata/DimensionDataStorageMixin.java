package dev.ithundxr.railwaystweaks.mixin.compat.create.saveddata;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

@Mixin(value = DimensionDataStorage.class, priority = 2000)
public abstract class DimensionDataStorageMixin {
	@Shadow
	@Final
	private File dataFolder;

	@Shadow
	public abstract CompoundTag readTagFromDisk(String pName, int pLevelVersion) throws IOException;

	@Inject(method = "readSavedData", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), cancellable = true)
	private <T extends SavedData> void railwaysTweaks$tryLoadingFromDatOldIfFailedToLoad(Function<CompoundTag, T> loadFunction, String name, CallbackInfoReturnable<T> cir) {
		// Try loading old data if it's create's SavedData
		if (name.startsWith("create_")) {
			RailwaysTweaks.LOGGER.info("Trying to restore {} from .dat_old", name);
			try {
				String path = name + ".dat_old";
				File oldFile = new File(dataFolder, path);
				if (oldFile.exists()) {
					CompoundTag compoundtag = readTagFromDisk(path, SharedConstants.getCurrentVersion().getDataVersion().getVersion());
					T data = loadFunction.apply(compoundtag.getCompound("data"));
					cir.setReturnValue(data);
					RailwaysTweaks.LOGGER.info("Successfully restored track data from {}", path);
				}
			} catch (Exception exception) {
				RailwaysTweaks.LOGGER.error("Error restoring from old saved data: {}", name, exception);
			}
		}
	}
	
	@WrapOperation(method = "readTagFromDisk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/DimensionDataStorage;getDataFile(Ljava/lang/String;)Ljava/io/File;"))
	private File railwaysTweaks$fixGetDataFile(DimensionDataStorage instance, String name, Operation<File> original) {
		boolean old = name.endsWith("_old");
		if (old) RailwaysTweaks.LOGGER.info("Reading .dat_old file for {}", name);
		return old ? new File(this.dataFolder, name) : original.call(instance, name);
	}
}