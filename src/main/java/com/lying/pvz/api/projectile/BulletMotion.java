package com.lying.pvz.api.projectile;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * 弹药运动策略。只描述"怎么飞"，不描述"打谁、打中后做什么"。
 *
 * <p>每个策略必须保证：给定相同的初始状态与 tick，客户端与服务端得到相同结果，
 * 否则不能使用 {@link SyncMode#PREDICTED}。
 */
public interface BulletMotion {

    enum SyncMode {
        /** 纯函数运动：客户端本地重放，不发送逐 tick 位置。 */
        PREDICTED,
        /** 状态机运动：服务端权威，客户端按服务端位置插值。 */
        AUTHORITATIVE
    }

    /**
     * 发射时瞄准。
     *
     * <p>{@code target} 与 {@code direction} 至少有一个非空：
     * 直线弹通常用 direction，抛物线/追踪弹用 target。
     */
    void aim(IBullet bullet, @Nullable LivingEntity target, @Nullable Vec3 direction);

    /**
     * 朝固定点瞄准。投石车/玉米炮对地面坐标发射时使用；
     * 默认退化为直线指向。
     */
    default void aimAtPoint(IBullet bullet, Vec3 point) {
        final Vec3 dir = point.subtract(bullet.position());
        if (dir.lengthSqr() > 1.0E-7D) {
            bullet.setVelocity(dir.normalize().scale(bullet.speed()));
        }
    }

    /** 服务端每 tick 推进运动；{@link SyncMode#PREDICTED} 时客户端也会调用。 */
    void tick(IBullet bullet);

    /**
     * 位移之后的运动修正（重力、空气阻力）。
     *
     * <p>与 {@link #tick} 分开是为了保持旧投掷弹的手感：先按当前速度移动，
     * 再对下一 tick 施加重力。
     */
    default void postTick(IBullet bullet) {
    }

    /** 是否允许当前帧命中实体。磁力弹只在 SHOOT 阶段为 true。 */
    default boolean canHit(IBullet bullet) {
        return true;
    }

    default SyncMode syncMode() {
        return SyncMode.PREDICTED;
    }

    /**
     * 从当前状态飞到目标所需时间（tick）。用于提前量；不支持的策略返回 0。
     */
    default double timeToTarget(IBullet bullet, LivingEntity target) {
        return 0.0D;
    }
}
