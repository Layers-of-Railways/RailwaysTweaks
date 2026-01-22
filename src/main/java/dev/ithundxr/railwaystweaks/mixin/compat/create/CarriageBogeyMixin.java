package dev.ithundxr.railwaystweaks.mixin.compat.create;

import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import static com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity.BOGEY_STYLE_KEY;

@Mixin(CarriageBogey.class)
public class CarriageBogeyMixin {
    @Shadow public CompoundTag bogeyData;

    @Unique
    @Nullable
    private BogeyStyle railwaysTweaks$cachedStyle;

    /**
     * @author Slimeist
     * @reason Add caching to prevent extremely expensive NBT reads every tick/frame.
     */
    @Overwrite(remap = false)
    public BogeyStyle getStyle() {
        // This extremely aggressive caching is safe because the style is never changed after the CarriageBogey is created.
        if (railwaysTweaks$cachedStyle != null) return railwaysTweaks$cachedStyle;

        ResourceLocation location = NBTHelper.readResourceLocation(this.bogeyData, BOGEY_STYLE_KEY);
        BogeyStyle style = AllBogeyStyles.BOGEY_STYLES.get(location);

        return railwaysTweaks$cachedStyle = style != null ? style : AllBogeyStyles.STANDARD; // just for safety
    }
}
