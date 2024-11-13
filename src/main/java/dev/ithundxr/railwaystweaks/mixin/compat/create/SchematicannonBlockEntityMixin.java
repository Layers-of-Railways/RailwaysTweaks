package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import dev.ithundxr.railwaystweaks.RailwaysTweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SchematicannonBlockEntity.class)
public class SchematicannonBlockEntityMixin {

    @Shadow private int printerCooldown;

    @Inject(method = "tickPrinter", at = @At("TAIL"), remap = false)
    private void railwaytweaks$tickPrinter(CallbackInfo ci) {
        double mspt = RailwaysTweaks.MSPT_TRACKER.getAverageMSPT();

        if (mspt > 55)
            this.printerCooldown = 60;
        else if (mspt > 45)
            this.printerCooldown = 20;
        else
            this.printerCooldown = 10;
    }
}