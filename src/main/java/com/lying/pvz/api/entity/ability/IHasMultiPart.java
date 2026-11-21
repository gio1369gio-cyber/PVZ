package com.lying.pvz.api.entity.ability;

/**
 * 多部件碰撞箱能力。
 *
 * <p>僵尸的头 / 手 / 身各自持有独立碰撞箱时使用。本接口只暴露生命周期操作，
 * <b>不暴露</b>具体部件实体类型——部件实体是实现层细节，第三方无需直接操作。
 *
 * <p>与 {@code IBodyEntity}（掉落肢体的渲染描述）不同：后者是独立实体，
 * 前者是主实体的附属碰撞箱。
 */
public interface IHasMultiPart {

    void resetParts();

    void removeParts();

    void updateParts();

    /**
     * 当前是否允许部件存在。默认恒 {@code true}；
     * 需要"部件随状态消失"的实体覆写。
     */
    default boolean canPartsExist() {
        return true;
    }
}