package com.lying.pvz.common.level.blockentity;

import com.lying.pvz.common.init.PvZBlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SunFlowerTrophyBlockEntity extends BaseBlockEntity {

	public SunFlowerTrophyBlockEntity(BlockPos pos, BlockState state) {
		super(PvZBlockEntityRegister.SUNFLOWER_TROPHY.get(), pos, state);
	}


}