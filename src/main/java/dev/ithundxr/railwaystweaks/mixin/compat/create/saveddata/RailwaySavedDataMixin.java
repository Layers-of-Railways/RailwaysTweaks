package dev.ithundxr.railwaystweaks.mixin.compat.create.saveddata;

import com.simibubi.create.content.trains.RailwaySavedData;
import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RailwaySavedData.class)
public class RailwaySavedDataMixin {
	@Inject(method = "save", at = @At("HEAD"))
	private static void railwaysTweaks$logWrite(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
		RailwaysTweaks.LOGGER.info("Saving track data as create_tracks.dat");
	}

	@Inject(method = "load(Lnet/minecraft/nbt/CompoundTag;)Lcom/simibubi/create/content/trains/RailwaySavedData;", at = @At("HEAD"))
	private static void railwaysTweaks$logRead(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
		RailwaysTweaks.LOGGER.info("Reading track data from create_tracks.dat");
	}
}
