package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BasinBlockEntity.class)
public class BasinBlockEntityMixin {

    @Unique @Final int railwayTweaks$BACKUP_CHECK_INTERVAL = 120;

    @Shadow int recipeBackupCheck;

    @Redirect(
            method = "lazyTick",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;recipeBackupCheck:I",
                    ordinal = 1,
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void railwayTweaks$changeRecipeBackupCheckInterval(BasinBlockEntity bbe, int recipeBackupCheck) {
        this.recipeBackupCheck = railwayTweaks$BACKUP_CHECK_INTERVAL;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void railwayTweaks$init(CallbackInfo ci) {
        this.recipeBackupCheck = railwayTweaks$BACKUP_CHECK_INTERVAL;
    }
}
