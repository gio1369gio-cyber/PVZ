package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 周日版读报僵尸。
 *
 * <p>继承 {@link NewspaperZombieEntity}，是读报僵尸的强化版：
 * 本体 350 血、报纸 100 血、暴怒力量等级上限 10、啃食伤害
 * {@link ZombieUtil#LITTLE_HIGH}、行走 {@link ZombieUtil#WALK_LITTLE_FAST}。
 *
 * <p>禁用寒冷（{@code canBeCold = false}）：报纸是其核心防御层，
 * 寒冷效果会显著削弱“破报暴怒”的节奏；冻结/黄油仍可生效。
 */
public class SundayEditionZombieEntity extends NewspaperZombieEntity {

	public SundayEditionZombieEntity(EntityType<? extends NewspaperZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canBeCold = false;
	}

	@Override
	public int getAngryLevel() {
		return 10;
	}

	@Override
	public float getEatDamage() {
		return ZombieUtil.LITTLE_HIGH;
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_LITTLE_FAST;
	}

	@Override
	public float getLife() {
		return 350;
	}

	@Override
	public float getOuterLife() {
		return 100;
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.SUNDAY_EDITION_ZOMBIE.get();
	}
}