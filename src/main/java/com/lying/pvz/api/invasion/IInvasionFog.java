package com.lying.pvz.api.invasion;
/**
 * 迷雾入侵策略（数据驱动）。
 *
 * <p>由 {@code com.lying.pvz.common.invasion.type.InvasionFog} 实现；数据包 JSON 中的
 * {@code fog} 字段反序列化为该类型。{@link #strength()} 用于视觉雾，其余字段
 * （{@link #nearDistance()}/{@link #farDistance()}/{@link #hideRaiders()} 等）预留给功能雾阶段。
 *
 * <p><b>第三方模组不应实现本接口</b>——签名将在未来版本扩展，实现方需要同步调整。
 */
public interface IInvasionFog {

    /** 强度/不透明度 0~1。 */
    float strength();

    /** 可视近界：该距离内无雾。 */
    int nearDistance();

    /** 雾区半径/可视远界：该距离外完全遮蔽。 */
    int farDistance();

    /** 持续 tick；<=0 表示整场入侵常驻。 */
    int duration();

    /** 结束时淡出 tick。 */
    int fadeTicks();

    /** 是否雾隐怪物（对标 PVZ 迷雾，默认 true）。 */
    boolean hideRaiders();

    /** 是否雾隐植物（对标 PVZ，默认 false——自己种的植物可见）。 */
    boolean hidePlants();
}