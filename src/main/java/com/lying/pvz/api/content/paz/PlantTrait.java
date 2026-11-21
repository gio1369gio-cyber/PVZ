package com.lying.pvz.api.content.paz;

/**
 * 植物特征标记。替代旧版零散布尔（如 {@code isShroomPlant()} / {@code isWaterPlant()}）。
 *
 * <p>与 {@link PlantCategory} / {@link PlantCombatStyle} 正交：特征描述"是什么"，
 * 分类描述"干什么"。一朵蘑菇可以是 {@link PlantCategory#RANGED} 也可以是
 * {@link PlantCategory#SPECIAL}。
 *
 * <p>{@link #WATER} 与旧版 {@code isWaterPlant()} 语义一致；
 * {@link #OUTER} 与 {@code isOuterPlant()} 一致：外层植物不占内层槽。
 * {@link #IMMUNE_TO_WILT} 与"会枯萎"能力接口配合：基类默认所有植物会枯萎，
 * 本 trait 让植物免疫枯萎。
 *
 * <p>本枚举的 {@code ordinal()} <b>不</b>被持久化——它只是编译期元数据。
 */
public enum PlantTrait {

    /**
     * 蘑菇：白天睡觉，可用咖啡豆唤醒。
     */
    SHROOM,

    /**
     * 水生植物：只能放水面，不淹死。
     */
    WATER,

    /**
     * 外层植物：南瓜壳、梯子、睡莲。不占内层槽。
     */
    OUTER,

    /**
     * 仅可放置于金块 / 金地砖（黄金叶）。
     */
    GOLD_PLACEABLE,

    /**
     * 可被坐骑 / 咖啡豆类植物附着。
     */
    CAN_RIDE,

    /**
     * 免疫枯萎（如怪猫）。
     */
    IMMUNE_TO_WILT,

    /**
     * 不参与植物间推挤。
     */
    NO_PLANT_COLLISION
}