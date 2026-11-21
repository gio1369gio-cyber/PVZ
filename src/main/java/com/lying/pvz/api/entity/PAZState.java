package com.lying.pvz.api.entity;

/**
 * 植物与僵尸共用的运行时状态位。
 *
 * <p>bit 由本枚举统一分配，植物和僵尸不再各自定义 bit 常量。
 *
 * <p>本枚举只放<b>运行时状态</b>，不放类型属性（如"是不是蘑菇"）。
 * 类型属性属于 {@code IPlantType} / {@code IZombieType} 的 trait。
 *
 * <p><b>持久化警告</b>：本枚举的 {@code ordinal()} 与 {@code int} 位一一对应，
 * 会被 NBT 存档持久化（{@code paz_states}）。<b>新状态只能追加在末尾</b>，
 * 不能在中间插入或重排，否则已存档的位语义会错位。
 */
public enum PAZState {

    /**
     * 魅惑：阵营翻转。植物被魅惑属僵尸阵营，僵尸被魅惑属植物阵营。
     */
    CHARMED,

    /**
     * 睡眠：蘑菇白天睡觉，可用咖啡豆唤醒。
     */
    SLEEPING,

    /**
     * 金属覆层 / 梯子：外层防御存在。
     */
    METAL,

    /**
     * 迷你化。
     */
    MINI,

    /**
     * 掉手。
     */
    LOST_HAND,

    /**
     * 掉头。
     */
    LOST_HEAD,

    /**
     * 南瓜壳（外层植物）。
     */
    PUMPKIN,

    /**
     * 超级模式。
     */
    SUPER,

    /**
     * 升起中（僵尸从土里爬出）。
     */
    RISING,

    /**
     * 加速（被 boost 类效果影响）。
     */
    BOOSTED,

    /**
     * 地雷就绪（土豆雷等）。
     */
    MINE_READY,

    /**
     * 地雷升起中。
     */
    MINE_RISING;

    private final int bit;

    PAZState() {
        this.bit = 1 << ordinal();
    }

    /**
     * 状态对应的 bit 值。
     */
    public int bit() {
        return this.bit;
    }

    /**
     * 读取 {@code state} 中是否置位本状态。
     */
    public boolean isSet(int state) {
        return (state & this.bit) != 0;
    }

    /**
     * 在 {@code state} 上设置或清除本状态，返回新值。调用方负责写回实体数据。
     */
    public int apply(int state, boolean on) {
        return on ? (state | this.bit) : (state & ~this.bit);
    }
}