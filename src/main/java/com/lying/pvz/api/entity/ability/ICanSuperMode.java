package com.lying.pvz.api.entity.ability;

/**
 * 有超级模式的植物。
 *
 * <p>超级模式 = 短时间内强化（攻击翻倍、范围扩大、自动反击等），
 * 由具体植物在 {@link #onSuperStart(boolean)} 里实现效果。
 *
 * <p>默认 {@link #getSuperTimeLength()} 返回 0，表示该植物不能启动超级模式；
 * 有 super 的植物覆写返回 {@code > 0} 的时长。
 */
public interface ICanSuperMode {

    /**
     * 超级模式持续时间（tick）。必须 {@code > 0} 才可启动。
     */
    int getSuperTimeLength();

    /**
     * 进入超级模式时回调。
     *
     * @param first 是否首次进入（用于触发成就 / 外层植物 super 等一次性效果）
     */
    void onSuperStart(boolean first);

    /**
     * 当前是否可启动超级模式。
     */
    default boolean canStartSuperMode() {
        return this.getSuperTimeLength() > 0;
    }
}