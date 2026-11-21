package com.lying.pvz.api.entity.projectile;

/**
 * 弹药变体。
 *
 * <p>变体影响伤害、尺寸、生命周期、命中效果，是<b>行为差异</b>而非纯数据，
 * 因此不用 {@code DataComponentMap}，而用"每类弹药自己的枚举 + 本接口"。
 *
 * <p>实现类约定：枚举 {@code ordinal()} 与 {@link #id()} 一致，
 * 但 {@link #id()} 显式声明，避免依赖 {@code ordinal()}。
 *
 * <p><b>持久化警告</b>：{@link #id()} 被 NBT / 网络持久化。
 * 已有变体的 ID <b>不得更改</b>；新增变体必须使用未用过的 ID。
 */
public interface IVariant {

    /**
     * 变体的稳定整数 ID，用于同步与 NBT。
     */
    int id();
}