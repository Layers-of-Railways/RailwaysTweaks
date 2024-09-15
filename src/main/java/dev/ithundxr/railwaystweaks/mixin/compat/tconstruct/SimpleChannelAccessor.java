package dev.ithundxr.railwaystweaks.mixin.compat.tconstruct;

import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SimpleChannel.class)
public interface SimpleChannelAccessor {
    @Accessor
    Map<Class<? extends S2CPacket>, Integer> getS2cIdMap();

    @Accessor
    Map<Class<? extends C2SPacket>, Integer> getC2sIdMap();
}
