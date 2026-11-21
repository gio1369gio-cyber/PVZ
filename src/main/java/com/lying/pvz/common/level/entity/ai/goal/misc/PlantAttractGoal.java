package com.lying.pvz.common.level.entity.ai.goal.misc;


import com.lying.pvz.api.entity.ability.ICanAttract;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.world.entity.ai.goal.Goal;

public class PlantAttractGoal extends Goal {

	private final ICanAttract attracter;

	private final PlantEntity plantEntity;
	private final int attractCD;
	private int attractTick;

	public PlantAttractGoal(PlantEntity plantEntity, ICanAttract attracter, int cd) {
		this.attracter = attracter;
		this.plantEntity = plantEntity;
		this.attractCD = cd;
	}

	@Override
	public boolean canUse() {
		return this.plantEntity.canNormalUpdate();
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	@Override
	public void tick() {
		++attractTick;
		if (attractTick >= attractCD) {
			this.attract();
			this.attractTick = 0;
		}
	}

	protected void attract() {
		float range = this.attracter.getAttractRange();
		EntityUtil.getTargetableLivings(this.plantEntity, EntityUtil.getEntityAABB(this.plantEntity, range, range))
				.stream().filter(this.attracter::canAttract).forEach(this.attracter::attract);
	}
}