package dev.ithundxr.railwaystweaks.mixin.atomicsavedata;

import dev.ithundxr.railwaystweaks.utils.IOUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.IOException;

@Mixin(SavedData.class)
public abstract class SavedDataMixin {
	@Shadow public abstract boolean isDirty();

	@Shadow public abstract CompoundTag save(CompoundTag compoundTag);

	@Shadow @Final private static Logger LOGGER;

	@Shadow public abstract void setDirty(boolean dirty);

	/**
	 * @author IThundxr
	 * @reason Improve SavedData saving to be resident via Atomic writes
	 */
	@Overwrite
	public void save(File file) {
		if (isDirty()) {
			CompoundTag compoundTag = new CompoundTag();
			compoundTag.put("data", save(new CompoundTag()));
			NbtUtils.addCurrentDataVersion(compoundTag);

			CompoundTag copied = compoundTag.copy();
			
			IOUtilities.withIOWorker(() -> {
				try {
					IOUtilities.writeNbtCompressed(copied, file.toPath());
				} catch (IOException e) {
					LOGGER.error("Could not save data {}", this, e);
				}
			});

			setDirty(false);
		}
	}
}
