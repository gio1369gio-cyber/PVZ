package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 旗帜僵尸。
 *
 * <p>与普通僵尸的唯一差异：行走 {@link ZombieUtil#WALK_LITTLE_FAST}，血量 19。
 * 通常作为波次前导出现，不携带额外逻辑。
 */
public class FlagZombieEntity extends NormalZombieEntity {

	public FlagZombieEntity(EntityType<? extends NormalZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_LITTLE_FAST;
	}

	@Override
	public float getLife() {
		return 19;
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.FLAG_ZOMBIE.get();
	}
}