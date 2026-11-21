package com.lying.pvz.common.level.entity.ai.goal.target;

import com.lying.pvz.utils.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class PVZGlobalTargetGoal extends PVZNearestTargetGoal {

	public PVZGlobalTargetGoal(Mob mobIn, boolean checkSight, boolean memory, float w, float h) {
		super(mobIn, checkSight, memory, w, h);
	}

	@Override
	protected boolean checkSenses(Entity entity) {
		return EntityUtil.canSeeEntity(this.mob, entity);
	}
}