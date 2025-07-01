package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.trains.RailwaySavedData;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.saveddata.SavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Mixin(value = SavedData.class, priority = 2000)
public class SavedDataMixin {
	@SuppressWarnings("ConstantValue")
	@WrapOperation(method = "save(Ljava/io/File;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtIo;writeCompressed(Lnet/minecraft/nbt/CompoundTag;Ljava/io/File;)V"))
	private void railwaysTweaks$createDatOld(CompoundTag compoundTag, File file, Operation<Void> original) throws IOException {
		if ((Object) this instanceof RailwaySavedData) {
			String savedDataName = file.getName().split("\\.")[0];
			File temp = File.createTempFile(savedDataName, ".dat", file.getParentFile());
			NbtIo.writeCompressed(compoundTag, temp);
			File oldFile = Paths.get(file.getParent(), savedDataName + ".dat_old").toFile();
			Util.safeReplaceFile(file, temp, oldFile);
		} else {
			original.call(compoundTag, file);
		}
	}
}
