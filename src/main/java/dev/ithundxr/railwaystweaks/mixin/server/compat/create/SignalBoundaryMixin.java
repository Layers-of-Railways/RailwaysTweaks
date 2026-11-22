package dev.ithundxr.railwaystweaks.mixin.server.compat.create;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.signal.SignalBlockEntity.SignalState;
import com.simibubi.create.content.trains.signal.SignalBoundary;
import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(SignalBoundary.class)
public class SignalBoundaryMixin {
	@Definition(id = "invalid", local = @Local(type = boolean.class, name = "invalid"))
	@Expression("invalid = true")
	@Inject(method = "resolveSignalChain", at = @At("MIXINEXTRAS:EXPRESSION"))
	private void rt$logCacheThrows(TrackGraph graph, boolean side, CallbackInfoReturnable<SignalState> cir, @Local(name = "uuid") UUID uuid) {
		RailwaysTweaks.LOGGER.info("Other signal is null: {}", uuid);
	}
}
