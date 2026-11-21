package com.lying.pvz.common.level.entity.zombie.pool;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.level.entity.zombie.base.SwimmerZombieEntity;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 海豚骑士僵尸（骑手形态）。
 *
 * <p>由 {@link DolphinRiderEntity#separate()} 分裂出来的独立单位。
 * 本体 30 血，行走 {@link ZombieUtil#WALK_VERY_FAST}，继承
 * {@link SwimmerZombieEntity} 的水中姿态切换。
 */
public class DolphinRiderZombieEntity extends SwimmerZombieEntity {

	public DolphinRiderZombieEntity(EntityType<? extends SwimmerZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_VERY_FAST;
	}

	@Override
	public float getLife() {
		return 30;
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.DOLPHIN_RIDER_ZOMBIE.get();
	}
}