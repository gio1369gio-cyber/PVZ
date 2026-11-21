package com.lying.pvz.common.level.entity.plant.appease;


import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class GatlingPeaEntity extends RepeaterEntity {
	public int animTime = 0;

	public GatlingPeaEntity(EntityType<? extends GatlingPeaEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			if (this.getAttackTime() > 0) {
				this.animTime = 15;
			} else {
				if (this.getAttackTime() == 0 && this.animTime > 0) {
					--this.animTime;
				}
			}
		}
	}

	@Override
	protected int getBigPeaNum() {
		return 1;
	}

	@Override
	public int getSuperTimeLength() {
		return 200;
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(4);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.GATLING_PEA.get();
	}
}