package com.lying.pvz.common.level.entity.plant.magic;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.drop.CoinEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantProducerEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 金盏花。产出金币：{@link #getGenCD()} = 1200 tick 一次，按
 * {@link #getSilverChance()} / {@link #getGoldChance()} 权重随机铜/银/金；
 * super 模式额外产出 {@link #getSuperGenCnt()} 枚金币 + 1 枚金币。
 */
public class MariGoldEntity extends PlantProducerEntity {

	public MariGoldEntity(EntityType<? extends PlantProducerEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void genSomething() {
		final CoinEntity coin = PvZEntityRegister.COIN.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
		if (coin == null) {
			return;
		}
		coin.setAmount(this.getRandomAmount());
		EntityUtil.onEntityRandomPosSpawn(this.level(), coin, this.blockPosition(), 3);
	}

	protected void genSpecCoin(CoinEntity.CoinType type) {
		final CoinEntity coin = PvZEntityRegister.COIN.get().create(this.level(),EntitySpawnReason.MOB_SUMMONED);
		if (coin == null) {
			return;
		}
		coin.setAmountByType(type);
		EntityUtil.onEntityRandomPosSpawn(this.level(), coin, this.blockPosition(), 3);
	}

	@Override
	protected void genSuper() {
		for (int i = 0; i < this.getSuperGenCnt(); ++i) {
			this.genSomething();
		}
		this.genSpecCoin(CoinEntity.CoinType.GOLD);
	}

	private int getRandomAmount() {
		final int num = this.getRandom().nextInt(100);
		final int silverNum = this.getSilverChance();
		final int goldNum = this.getGoldChance();
		if (num < goldNum) {
			return CoinEntity.CoinType.GOLD.money;
		}
		if (num < silverNum + goldNum) {
			return CoinEntity.CoinType.SILVER.money;
		}
		return CoinEntity.CoinType.COPPER.money;
	}

	public int getSilverChance() {
		return 10;
	}

	public int getGoldChance() {
		return 1;
	}

	public int getSuperGenCnt() {
		return 5;
	}

	@Override
	public int getGenCD() {
		return 1200;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 1.6F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.MARIGOLD.get();
	}
}