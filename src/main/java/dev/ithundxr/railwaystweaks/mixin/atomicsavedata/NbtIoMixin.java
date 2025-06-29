package dev.ithundxr.railwaystweaks.mixin.atomicsavedata;

import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import dev.ithundxr.railwaystweaks.utils.IOUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.File;
import java.io.IOException;

@Mixin(NbtIo.class)
public class NbtIoMixin {
	/**
	 * @author IThundxr
	 * @reason Improve SavedData saving to be resident via Atomic writes 
	 */
	@Overwrite
	public static void writeCompressed(CompoundTag compoundTag, File file) {
		CompoundTag copied = compoundTag.copy();
		
		IOUtilities.withIOWorker(() -> {
			try {
				IOUtilities.writeNbtCompressed(copied, file.toPath());
			} catch (IOException exception) {
				RailwaysTweaks.LOGGER.error("Failed to write nbt data to file {}, {}", file, copied, exception);
			}
		});
	}
}
