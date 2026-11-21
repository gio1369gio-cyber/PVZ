package com.lying.pvz.api.content.paz;

/**
 * 植物大类。PVZ 里植物的第一层分类。
 *
 * <p>与 {@link PlantCombatStyle} 配合使用：大类决定"这植物干什么"，
 * 战斗形态决定"它怎么打 / 怎么防"。
 *
 * <p>本枚举的 {@code ordinal()} <b>不</b>被持久化——它只是编译期元数据，
 * 通过 {@code IPlantType#getCategory()} 返回。NBT 持久化的类型由
 * {@code Identifier} 承担。因此可以安全地在中间插入新值。
 */
public enum PlantCategory {

    /**
     * 近战：大嘴花、窝瓜。
     */
    MELEE,

    /**
     * 远程：豌豆射手、投石车、西瓜。
     */
    RANGED,

    /**
     * 防御：坚果墙、地刺、大蒜。
     */
    DEFENSE,

    /**
     * 特殊：向日葵、咖啡豆、三叶草、模仿者、土豆雷。
     */
    SPECIAL
}