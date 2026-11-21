package com.lying.pvz.common.level.entity.ai.processor;

import com.lying.pvz.common.level.entity.zombie.base.SwimmerZombieEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;

public class ZombieNodeProcessor extends AmphibiousNodeEvaluator {


	public ZombieNodeProcessor() {
		super(false);
	}

	@Override
	public void prepare(PathNavigationRegion region, Mob mob) {
		super.prepare(region, mob);
		if (mob instanceof SwimmerZombieEntity) {
			mob.setPathfindingMalus(PathType.WATER, 0.0F);
			mob.setPathfindingMalus(PathType.WALKABLE, 3F);
			mob.setPathfindingMalus(PathType.WATER_BORDER, 4.0F);
		} else {
			mob.setPathfindingMalus(PathType.WATER, 2.0F);
			mob.setPathfindingMalus(PathType.WALKABLE, 0F);
			mob.setPathfindingMalus(PathType.WATER_BORDER, 4.0F);
		}
	}
}