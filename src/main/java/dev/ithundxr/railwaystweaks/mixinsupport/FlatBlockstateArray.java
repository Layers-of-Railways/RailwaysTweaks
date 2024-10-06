package dev.ithundxr.railwaystweaks.mixinsupport;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FlatBlockstateArray {
	public static BlockState[] FROM_ID;

	public static void apply() {
		int size = Block.BLOCK_STATE_REGISTRY.size();

		FROM_ID = new BlockState[size];
		int i = 0;
		for (BlockState b : Block.BLOCK_STATE_REGISTRY) {
			FROM_ID[i++] = b;
		}
	}
}