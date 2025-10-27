package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dev.ithundxr.railwaystweaks.mixinsupport.BasinBlockEntity_Duck;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BasinBlockEntity.class)
public abstract class BasinBlockEntityMixin extends SmartBlockEntity implements BasinBlockEntity_Duck {
    @Shadow int recipeBackupCheck;

    @Unique @Final int railwayTweaks$BACKUP_CHECK_INTERVAL = 120;

    @Unique
    private @Nullable BlazeBurnerBlock.HeatLevel railwaysTweaks$cachedHeatLevel = null;

    private BasinBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

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

    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    private void clearHeatLevel(CallbackInfo ci) {
        railwaysTweaks$cachedHeatLevel = null;
    }

    @Override
    public @NotNull BlazeBurnerBlock.HeatLevel railwaysTweaks$getHeatLevel() {
        if (railwaysTweaks$cachedHeatLevel == null) {
            Level lvl = getLevel();
            if (lvl == null) {
                return BlazeBurnerBlock.HeatLevel.NONE;
            }
            railwaysTweaks$cachedHeatLevel = BasinBlockEntity.getHeatLevelOf(
                lvl.getBlockState(
                    getBlockPos().below(1)
                )
            );
        }
        return railwaysTweaks$cachedHeatLevel;
    }
}
