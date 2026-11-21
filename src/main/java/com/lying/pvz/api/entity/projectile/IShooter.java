package com.lying.pvz.api.entity.projectile;

import net.minecraft.world.entity.Entity;

/**
 * 直线射手行为契约。
 *
 * <p>继承 {@link ICanShoot}：跨模组查询统一走 {@code createProjectile} /
 * {@code getAttackRange} / {@code getAttackInterval}；本接口额外暴露
 * 动画节奏（{@link #getShootCD} / {@link #startShootAttack}）、弹速与朝向校验，
 * 供直线射手 AI goal 在 tick 中驱动。
 */
public interface IShooter extends ICanShoot {

	/**
	 * 立即发射一发弹药。
	 */
	void shootBullet();

	/**
	 * 发射冷却（tick）。
	 */
	int getShootCD();

	/**
	 * 弹药初速度（方块 / tick）。
	 */
	float getBulletSpeed();

	/**
	 * 播放发射前动画。
	 */
	void startShootAttack();

	/**
	 * 目标 Y 轴校验（同行判定）。用于直线射手排除不同高度的目标。
	 */
	boolean checkY(Entity target);
}