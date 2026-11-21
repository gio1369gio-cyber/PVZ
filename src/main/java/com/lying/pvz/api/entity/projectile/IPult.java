package com.lying.pvz.api.entity.projectile;

/**
 * 投掷型行为契约。
 *
 * <p>继承 {@link ICanShoot}：对外快照统一走 {@code createProjectile} /
 * {@code getAttackRange} / {@code getAttackInterval}；本接口额外暴露
 * 动画节奏与范围轰炸触发点，供投手 AI goal 驱动。
 */
public interface IPult extends ICanShoot {

	/**
	 * 当前是否应当投掷。
	 */
	boolean shouldPult();

	/**
	 * 投掷冷却（tick）。
	 */
	int getPultCD();

	/**
	 * 播放投掷动画。
	 */
	void startPultAttack();

	/**
	 * 投掷射程（方块）。
	 */
	float getPultRange();

	/**
	 * 投掷弹药（完成瞄准与发射）。
	 */
	void pultBullet();
}