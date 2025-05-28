package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBogeyBlockEntity.class)
public abstract class AbstractBogeyBlockEntityMixin {
    @Shadow public abstract CompoundTag getBogeyData();

    @Shadow public abstract BogeyStyle getDefaultStyle();

    @Shadow public abstract void setBogeyStyle(@NotNull BogeyStyle style);

    @Shadow @Final public static String BOGEY_STYLE_KEY;

    @Unique
    @Nullable
    private BogeyStyle railwaysTweaks$cachedStyle;

    @Inject(method = "setBogeyData", at = @At("HEAD"))
    private void clearCache(CompoundTag data, CallbackInfo ci) {
        railwaysTweaks$cachedStyle = null;
    }

    @Inject(method = "setBogeyStyle", at = @At("HEAD"), remap = false)
    private void updateCache(BogeyStyle style, CallbackInfo ci) {
        railwaysTweaks$cachedStyle = style;
    }

    @Inject(method = "load", at = @At("HEAD"))
    private void clearCache2(CompoundTag pTag, CallbackInfo ci) {
        railwaysTweaks$cachedStyle = null;
    }

    @Inject(method = "createBogeyData", at = @At("HEAD"))
    private void clearCache3(CallbackInfoReturnable<CompoundTag> cir) {
        railwaysTweaks$cachedStyle = null;
    }

    /**
     * @author Slimeist
     * @reason Add caching to prevent extremely expensive NBT reads every tick/frame.
     */
    @Overwrite(remap = false)
    @NotNull
    public BogeyStyle getStyle() {
        if (railwaysTweaks$cachedStyle != null) return railwaysTweaks$cachedStyle;

        CompoundTag data = this.getBogeyData();
        ResourceLocation currentStyle = NBTHelper.readResourceLocation(data, BOGEY_STYLE_KEY);
        BogeyStyle style = AllBogeyStyles.BOGEY_STYLES.get(currentStyle);
        if (style == null) {
            setBogeyStyle(getDefaultStyle());
            return railwaysTweaks$cachedStyle = getStyle();
        }
        return railwaysTweaks$cachedStyle = style;
    }
}
