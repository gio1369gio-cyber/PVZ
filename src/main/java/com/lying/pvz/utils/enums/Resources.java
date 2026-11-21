package com.lying.pvz.utils.enums;


import com.lying.pvz.utils.ConfigUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * 玩家资源枚举。
 *
 * <p>权威数据在 {@code PlayerStats}（{@code int[]} 按 ordinal 索引）。
 * 部分资源的实际生效上限由 facade 层动态计算（如 {@link #SUN_NUM} 依赖 {@link #TREE_LVL}），
 * 此处 {@code min}/{@code max} 仅作为兜底 clamp 范围。
 */
public enum Resources {

    /** 树等级。 */
    TREE_LVL(1, 200),
    /** 树经验。溢出逻辑在 facade 层处理。 */
    TREE_XP(0, Integer.MAX_VALUE),
    /** 阳光。实际上限由 {@code getPlayerMaxSunNum(TREE_LVL)} 动态决定。 */
    SUN_NUM(0, Integer.MAX_VALUE),
    /** 最大能量上限。 */
    MAX_ENERGY_NUM(1, 10),
    /** 当前能量。实际上限受 {@link #MAX_ENERGY_NUM} 约束。 */
    ENERGY_NUM(0, 10),
    /** 金币。 */
    MONEY(0, 1000000),
    /** 宝石。 */
    GEM_NUM(0, 1000000),
    /** 抽奖券。 */
    LOTTERY_CHANCE(0, 9999999),
    /** 玩家分组。由 {@code EntityGroupHander} 使用。 */
    GROUP_TYPE(-2, 2),
    /** 可放卡片的热键栏槽位数（1~9）。等级提升时自动解锁。 */
    SLOT_NUM(1, 9),
    /** 任务完成累计次数（历史）。 */
    MISSION_FINISH_TIME(0, 9999999),
    /** 迷雾倒计时。迷雾系统暂不实现，字段保留。 */
    NO_FOG_TICK(0, 9999999);

    public static final int INF = 9999999;

    public final int min;
    public final int max;

    Resources(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static int getInitialValue(Resources res) {
        return switch (res) {
            case SUN_NUM -> 50;
            case LOTTERY_CHANCE -> 10;
            case GROUP_TYPE -> ConfigUtil.getPlayerInitialGroup();
            default -> res.min;
        };
    }

    public MutableComponent getText() {
        return Component.translatable("resource.pvz." + this.toString().toLowerCase());
    }
}