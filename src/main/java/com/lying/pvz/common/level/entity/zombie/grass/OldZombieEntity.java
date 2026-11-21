package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 旧版读报僵尸。
 *
 * <p>继承 {@link NewspaperZombieEntity}，仅调整数值：
 * 本体 90 血、报纸 40 血、暴怒力量等级上限 5、啃食伤害
 * {@link ZombieUtil#LITTLE_LOW}。行为与读报僵尸一致。
 */
public class OldZombieEntity extends NewspaperZombieEntity {

	public OldZombieEntity(EntityType<? extends NewspaperZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public int getAngryLevel() {
		return 5;
	}

	@Override
	public float getEatDamage() {
		return ZombieUtil.LITTLE_LOW;
	}

	@Override
	public float getLife() {
		return 90;
	}

	@Override
	public float getOuterLife() {
		return 40;
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.OLD_ZOMBIE.get();
	}
}