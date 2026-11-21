package com.lying.pvz.api.content.skill;

/**
 * 技能作用面。决定技能等级从卡片传导到实体的路径。
 */
public enum SkillScope {

    /**
     * 只作用于卡片本身（阳光、冷却等），不传导到实体。
     */
    CARD,

    /**
     * 召唤时传导给植物 / 僵尸实体。
     */
    PLANT,

    /**
     * 召唤时由植物暂存，发射弹药时再传给弹药实体。
     */
    PROJECTILE
}