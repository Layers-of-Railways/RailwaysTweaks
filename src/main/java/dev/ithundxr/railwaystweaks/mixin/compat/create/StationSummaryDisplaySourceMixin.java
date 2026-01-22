package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.StationSummaryDisplaySource;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.simibubi.create.content.trains.display.FlapDisplaySection.MONOSPACE;

@Mixin(value = StationSummaryDisplaySource.class, remap = false)
public abstract class StationSummaryDisplaySourceMixin extends DisplaySource {
    @ModifyExpressionValue(
            method = "lambda$provideFlapDisplayText$0(ZLjava/util/List;Ljava/lang/String;Lcom/simibubi/create/content/trains/display/GlobalTrainDisplayData$TrainDeparturePrediction;)V",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=11700"
            )
    )
    private static int increaseEtaDisplayLimitTo60m(int original) {
        // increase max ETA time shown on Display Boards from 12000 ticks (10 min) to 72000 ticks (60 min)
        return 72000 - 15 * 20;
    }

    @ModifyArg(
            method = "loadFlapDisplayLayout",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/trains/display/FlapDisplaySection;<init>(FLjava/lang/String;ZZ)V",
                    ordinal = 0
            ),
            index = 0
    )
    private float doubleMinuteSectionWidth(float width) {
        return MONOSPACE * 2; // double the width of the minute section
    }

    @ModifyVariable(method = "loadFlapDisplayLayout", at = @At("STORE"), name = "minutes")
    private FlapDisplaySection setRightAlignedOnMinuteSection(FlapDisplaySection minutes) {
        return minutes.rightAligned();
    }

    @ModifyVariable(method = "loadFlapDisplayLayout", at = @At("STORE"), name = "totalSize")
    private float decreaseAvailableSpaceForOtherSections(float totalSize) {
        return totalSize - MONOSPACE; // subtract the amount added above
    }
}