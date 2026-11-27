package com.lying.pvz.common.level.entity.ai.goal;

import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;


public class PVZLookRandomlyGoal extends RandomLookAroundGoal {

	private Mob plant;

	public PVZLookRandomlyGoal(Mob entitylivingIn) {
		super(entitylivingIn);
		this.plant = entitylivingIn;
	}

	@Override
	public boolean canUse() {
		return this.canExecute() && super.canUse();
	}

	private boolean canExecute() {
		if(this.plant instanceof PlantEntity && !((PlantEntity) this.plant).canNormalUpdate()) {
			return false;
		}
		if(this.plant instanceof PVZZombieEntity && !((PVZZombieEntity) this.plant).canNormalUpdate()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		return this.canExecute() && super.canContinueToUse();
	}
}