package com.lying.pvz.utils;


import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import net.minecraft.core.BlockPos;

import java.util.List;

public class ZombieUtil {

	public static List<IZombieType> getDefaultZombies() {
		return List.of(
				GrassZombies.NORMAL_ZOMBIE.get(),
				GrassZombies.CONEHEAD_ZOMBIE.get(),
				GrassZombies.BUCKETHEAD_ZOMBIE.get(),
				GrassZombies.SCREENDOOR_ZOMBIE.get(),
				GrassZombies.NEWSPAPER_ZOMBIE.get());
	}

	// move speed / attack damage / range 常量全部保留（纯 float，无改动）

	public static final float WALK_SUPER_SLOW = 0.15F;
	public static final float WALK_HUGE_SLOW = 0.16F;
	public static final float WALK_VERY_SLOW = 0.17F;
	public static final float WALK_SLOW = 0.18F;
	public static final float WALK_LITTLE_SLOW = 0.19F;
	public static final float WALK_NORMAL = 0.20F;
	public static final float WALK_LITTLE_FAST = 0.225F;
	public static final float WALK_FAST = 0.25F;
	public static final float WALK_VERY_FAST = 0.275F;
	public static final float WALK_HUGE_FAST = 0.3F;
	public static final float WALK_SUPER_FAST = 0.325F;
	public static final float WATER_FAST = 0.8F;
	public static final float FLY_FAST = 0.5F;

	public static final float HUGE_LOW = 3;
	public static final float VERY_LOW = 4;
	public static final float LOW = 5;
	public static final float LITTLE_LOW = 6;
	public static final float NORMAL_DAMAGE = 8;
	public static final float LITTLE_HIGH = 12;
	public static final float HIGH = 20;
	public static final float VERY_HIGH = 30;
	public static final float HUGE_HIGH = 50;
	public static final float SUPER_HIGH = 100;

	public static final float CLOSE_TARGET_RANGE = 40;
	public static final float LITTLE_CLOSE_TARGET_RANGE = 50;
	public static final float NORMAL_TARGET_RANGE = 60;
	public static final float LITTLE_FAR_TARGET_RANGE = 70;
	public static final float LOW_TARGET_HEIGHT = 20;
	public static final float NORMAL_TARGET_HEIGHT = 30;
	public static final float LITTLE_HIGH_TARGET_HEIGHT = 40;
	public static final float HIGH_TARGET_HEIGHT = 50;

	public static void copySummonZombieData(PVZZombieEntity old, PVZZombieEntity now) {
		now.setCharmed(old.isCharmed());
		now.setMiniZombie(old.isMiniZombie());
	}

	public static void onZombieSpawn(PVZZombieEntity old, PVZZombieEntity now, BlockPos pos) {
		ZombieUtil.copySummonZombieData(old, now);
		EntityUtil.onEntitySpawn(old.level(), now, pos);
	}
}