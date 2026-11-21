package com.lying.pvz.api.entity;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.BodyType;

/**
 * 掉落肢体的渲染数据视图。
 *
 * <p>用于渲染独立实体（如断手、断头）时描述其"属于哪只僵尸、什么部位、
 * 是否迷你"等信息。与"多部件碰撞箱"（{@code IHasMultiPart}）不同：
 * 本接口描述<b>独立掉落肢体实体</b>的数据来源。
 */
public interface IBodyEntity {

	IZombieType getZombieType();

	BodyType getBodyType();

	boolean hasHandDefence();

	boolean isMini();

	int getAnimTime();
}