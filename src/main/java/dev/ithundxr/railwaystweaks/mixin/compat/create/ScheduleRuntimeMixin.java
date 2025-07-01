package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.trains.schedule.ScheduleRuntime;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ScheduleRuntime.class)
public class ScheduleRuntimeMixin {
	@Shadow List<CompoundTag> conditionContext;

	@Inject(method = "destinationReached", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V"))
	private void railwaysTweaks$clearConditionContext(CallbackInfo ci) {
		conditionContext.clear();
	}
}
