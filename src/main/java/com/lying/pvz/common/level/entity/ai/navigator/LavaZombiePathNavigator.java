package com.lying.pvz.common.level.entity.ai.navigator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class LavaZombiePathNavigator extends GroundPathNavigation {

	public LavaZombiePathNavigator(Mob p_i45875_1_, Level p_i45875_2_) {
		super(p_i45875_1_, p_i45875_2_);
	}


	@Override
	public boolean isStableDestination(BlockPos pos) {

		return this.level.getBlockState(pos).is(Blocks.LAVA) || super.isStableDestination(pos);
	}
}