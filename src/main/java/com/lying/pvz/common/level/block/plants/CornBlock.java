package com.lying.pvz.common.level.block.plants;


import com.lying.pvz.common.init.PvZItemRegister;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

/**
 * 玉米作物方块。
 *
 */
public class CornBlock extends CropBlock {

	public CornBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return PvZItemRegister.CORN_SEEDS.get();
	}
}