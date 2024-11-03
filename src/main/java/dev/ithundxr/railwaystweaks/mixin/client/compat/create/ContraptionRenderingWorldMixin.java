package dev.ithundxr.railwaystweaks.mixin.client.compat.create;

import com.jozufozu.flywheel.event.BeginFrameEvent;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionHandler;
import com.simibubi.create.content.contraptions.render.ContraptionRenderInfo;
import com.simibubi.create.content.contraptions.render.ContraptionRenderingWorld;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.ref.WeakReference;
import java.util.List;

@Mixin(ContraptionRenderingWorld.class)
public abstract class ContraptionRenderingWorldMixin<C extends ContraptionRenderInfo> {
    @Shadow @Final protected Level world;
    
    @Shadow private int removalTimer;
    
    @Shadow @Final protected Int2ObjectMap<C> renderInfos;
    @Shadow @Final protected List<C> visible;
    
    @Shadow public abstract void removeDeadRenderers();
    @Shadow public abstract C getRenderInfo(Contraption c);
    

    /**
     * @author IThundxr
     * @reason Replace stream with for loops to help with performance
     */
    @Overwrite
    public void tick() {
        removalTimer++;
        if (removalTimer >= 20) {
            removeDeadRenderers();
            removalTimer = 0;
        }
        
        for (WeakReference<AbstractContraptionEntity> ref : ContraptionHandler.loadedContraptions.get(world).values()) {
            AbstractContraptionEntity entity = ref.get();

            // contraptions that are too large will not be synced, and un-synced contraptions will be null
            if (entity != null && entity.getContraption() != null) {
                getRenderInfo(entity.getContraption());
            }
        }
    }

    /**
     * @author IThundxr
     * @reason Replace stream with for loops to help with performance
     */
    @Overwrite
    public void beginFrame(BeginFrameEvent event) {
        renderInfos.forEach((key, renderInfo) ->
                renderInfo.beginFrame(event)
        );

        collectVisible();
    }

    /**
     * @author IThundxr
     * @reason Replace stream with for loops to help with performance
     */
    @Overwrite
    protected void collectVisible() {
        visible.clear();

        renderInfos.forEach((key, renderInfo) -> {
            if (renderInfo.isVisible()) {
                visible.add(renderInfo);
            }
        });
    }
}
