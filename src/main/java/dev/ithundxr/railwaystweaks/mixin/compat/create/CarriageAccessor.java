package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Carriage.class)
public interface CarriageAccessor {
    @Accessor("serialisedEntity")
    CompoundTag railwaysTweaks$getSerializedEntity();
}
