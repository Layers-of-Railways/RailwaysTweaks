package dev.ithundxr.railwaystweaks.mixinsupport;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public record BlockFaceUtils$faceMatch$Key(long fromPos, long toPos, BlockState fromState, BlockState toState, Direction fromFace) {}
