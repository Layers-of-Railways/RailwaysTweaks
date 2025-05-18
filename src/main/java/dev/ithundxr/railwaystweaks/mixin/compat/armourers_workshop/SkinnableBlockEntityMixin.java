package dev.ithundxr.railwaystweaks.mixin.compat.armourers_workshop;

import moe.plushie.armourers_workshop.core.blockentity.RotableContainerBlockEntity;
import moe.plushie.armourers_workshop.core.blockentity.SkinnableBlockEntity;
import moe.plushie.armourers_workshop.init.ModLog;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SkinnableBlockEntity.class, remap = false)
public abstract class SkinnableBlockEntityMixin {

    @Shadow public abstract @Nullable SkinnableBlockEntity getParent();
    @Shadow public abstract void kill();

    /**
     * @author Sleepy_Evelyn
     * @reason Disable logspam
     */
    @Overwrite
    protected void childTick() {
        var parent = this.getParent();
        if (parent == null) {
            this.kill();
        }
    }
}
