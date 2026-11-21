package com.lying.pvz.common.level.entity.plant.appease;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 双发射手。
 *
 * <p>进入 super 时把 {@link #getBigPeaNum()} 加入 {@code bigPeaNum}；
 * 之后每 {@link #SHOOT_BIG_CD} tick 消耗 1 发改为 BIG 弹。
 */
public class RepeaterEntity extends PeaShooterEntity {
	protected static final int SHOOT_BIG_CD = 100;
	protected int bigPeaNum = 0;
	protected int shootBigTick = 0;

	public RepeaterEntity(EntityType<? extends PeaShooterEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void normalPlantTick() {
		super.normalPlantTick();
		if (!this.level().isClientSide()) {
			++this.shootBigTick;
		}
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}；{@code startSuperMode} 已委托到此方法。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		if (!this.level().isClientSide()) {
			this.bigPeaNum += this.getBigPeaNum();
		}
	}

	protected int getBigPeaNum() {
		return 1;
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(2);
	}

	@Override
	public int getSuperTimeLength() {
		return 120;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.REPEATER.get();
	}
}