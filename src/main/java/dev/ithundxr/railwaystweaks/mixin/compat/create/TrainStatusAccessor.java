package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.content.trains.entity.TrainStatus;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(TrainStatus.class)
public interface TrainStatusAccessor {
    @Accessor("queued")
    List<Component> getQueued();
}
