package com.lying.pvz.common.level.entity.plant.light;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.OtherPlants;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.level.block.special.GoldTileBlock;
import com.lying.pvz.common.level.entity.plant.base.PlantBomberEntity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * 黄金叶。
 *
 * <p>继承 {@link PlantBomberEntity}，但 {@link #getReadyTime()} 完成后不爆炸，而是把脚下方块
 * 升级为对应等级的黄金地砖。{@link #hasBombAlamancs} 关闭后不显示爆炸参数。
 * 金块产出逻辑在父类 {@code PlantEntity.normalPlantTick} 中统一处理（{@code GoldLeafEntity.GOLD_GEN_CD}）。
 */
public class GoldLeafEntity extends PlantBomberEntity {

	public static final int GOLD_GEN_CD = 400;

	public GoldLeafEntity(EntityType<? extends PlantBomberEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canCollideWithPlant = false;
		this.hasBombAlamancs = false;
	}

	@Override
	protected void startBomb(boolean server) {
		final Block block = this.level().getBlockState(this.blockPosition().below()).getBlock();
		final int lvl = getBlockGoldLevel(block);
		if (server && lvl >= 0 && lvl < this.getTileLevel()) {
			this.level().setBlockAndUpdate(this.blockPosition().below(),
					getGoldTileByLvl(this.getTileLevel()).defaultBlockState());
		}
	}

	/**
	 * 读取方块的黄金等级。{@code GoldTileBlock} 的 {@code lvl} 字段在新架构中若改为
	 * {@code getLevel()} 方法，请同步替换。
	 */
	public static int getBlockGoldLevel(Block block) {
		if (block instanceof GoldTileBlock goldTile) {
			return goldTile.lvl;
		}
		if (block == Blocks.GOLD_BLOCK) {
			return 0;
		}
		return -1;
	}

	public static int getGoldGenAmount(int lvl) {
		return lvl == 1 ? 25 : lvl == 2 ? 35 : 50;
	}

	public static Block getGoldTileByLvl(int lvl) {
		return lvl == 1 ? PvZBlockRegister.GOLD_TILE1.get()
				: lvl == 2 ? PvZBlockRegister.GOLD_TILE2.get()
				: PvZBlockRegister.GOLD_TILE3.get();
	}

	public int getTileLevel() {
		return (int) this.getSkillValue(SkillTypes.ADVANCE_GOLD.get());
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.6F, 1F);
	}

	@Override
	public int getReadyTime() {
		return 60;
	}

	@Override
	public IPlantType getPlantType() {
		return OtherPlants.GOLD_LEAF.get();
	}
}