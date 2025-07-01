package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.RailwaySavedData;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Mixin(RailwaySavedData.class)
public abstract class RailwaySavedDataMixin extends SavedData {
	@Override
	public void save(@NotNull File file) {
		if (this.isDirty()) {
			CompoundTag compoundtag = new CompoundTag();
			compoundtag.put("data", this.save(new CompoundTag()));
			NbtUtils.addCurrentDataVersion(compoundtag);

			String savedDataName = file.getName().split("\\.")[0];

			try {
				File temp = File.createTempFile(savedDataName, ".dat", file.getParentFile());
				NbtIo.writeCompressed(compoundtag, temp);
				File oldFile = Paths.get(file.getParent(), savedDataName + ".dat_old").toFile();
				Util.safeReplaceFile(file, temp, oldFile);
			} catch (IOException ioexception) {
				Create.LOGGER.error("Could not save data {}", this, ioexception);
			}

			this.setDirty(false);
		}
	}
}
