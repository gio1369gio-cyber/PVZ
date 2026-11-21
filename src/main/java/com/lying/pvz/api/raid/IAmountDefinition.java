package com.lying.pvz.api.raid;

import net.minecraft.util.RandomSource;

/**
 * 生成数量配置。
 *
 * <p><b>实现契约</b>：{@link #getMinAmount()} 与 {@link #getMaxAmount()} 是
 * 数量范围与实际生成数量不符。
 */
public interface IAmountDefinition {

    String type();
    int getMinAmount();
    int getMaxAmount();
    /** 服务端掷一次实际数量；客户端禁止调用。 */
    int roll(RandomSource random);
}