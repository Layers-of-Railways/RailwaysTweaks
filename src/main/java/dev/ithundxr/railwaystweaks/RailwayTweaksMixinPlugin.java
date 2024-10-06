package dev.ithundxr.railwaystweaks;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class RailwayTweaksMixinPlugin implements IMixinConfigPlugin {
    private static final boolean FLATTEN_CHUNK_PALETTES = Boolean.getBoolean("railwayTweaks.fireblanket.flattenChunkPalettes");
    
    @Override
    public void onLoad(String mixinPackage) { } // NO-OP

    @Override
    public String getRefMapperConfig() { return null; } // DEFAULT

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("LevelChunkSectionMixin")) return FLATTEN_CHUNK_PALETTES;
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { } // NO-OP

    @Override
    public List<String> getMixins() { return null; } // DEFAULT

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { } // NO-OP

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { } // NO-OP
}
