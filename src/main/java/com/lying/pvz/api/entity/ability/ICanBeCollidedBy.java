package com.lying.pvz.api.entity.ability;

import net.minecraft.world.entity.Entity;

/**
 * 自定义碰撞规则。
 *
 * <p>与近战防御型植物配合：基类先检查对方植物的战斗形态是否为
 * {@code PlantCombatStyle#MELEE_DEFENSE}，再问本方法"我能不能被对方碰到"。
 * 地刺、南瓜壳、大嘴花等覆写以排除某些实体（如地刺不与飞行实体碰撞）。
 *
 * <p>默认行为：所有实体默认可被碰到。
 */
public interface ICanBeCollidedBy {

    /**
     * 本实体是否与 {@code other} 发生碰撞。
     *
     * @param other 可能与自己碰撞的实体
     * @return true 表示允许碰撞
     */
    boolean canBeCollidedBy(Entity other);
}