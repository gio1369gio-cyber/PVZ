package com.lying.pvz.common.level.entity.zombie.pool;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.level.entity.zombie.base.SwimmerZombieEntity;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 浮潜僵尸。
 *
 * <p>继承 {@link SwimmerZombieEntity}，本体 20 血，行走
 * {@link ZombieUtil#WALK_LITTLE_FAST}。在水中采用游泳姿态，上岸站立。
 */
public class SnorkelZombieEntity extends SwimmerZombieEntity {

	public SnorkelZombieEntity(EntityType<? extends SwimmerZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_LITTLE_FAST;
	}

	@Override
	public float getLife() {
		return 20;
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.SNORKEL_ZOMBIE.get();
	}
}