package com.lying.pvz.api.projectile;

import com.lying.pvz.api.entity.ability.GroupType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * 新弹药体系对外的<b>唯一实体契约</b>。
 *
 * <p>与旧 {@code IPAZProjectile} 的区别：
 * <ul>
 *   <li>不再暴露 {@code shootFrom / aimAt / maxLiveTick} 这类"发射时配置"方法——
 *       这些由 {@code ShotSpec} 与 {@code ShotResolver} 在生成前完成；</li>
 *   <li>只暴露飞行中真正需要读写的东西：位置、速度、变体、阵营、伤害快照、
 *       穿透余额、目标与生命周期；</li>
 *   <li>{@code ownerUuid} 允许为空——少数无主弹药不参与标记与卡牌数据。</li>
 * </ul>
 *
 * <p><b>触发者约定</b>：{@link #ownerUuid()} 是标记系统的来源键。
 * 每株植物独立，因此同一植物类型的两株植物也会得到不同的标记层。
 */
public interface IBullet {

    /** 实体自身。伤害源 / 扫掠检测需要 {@link Entity} 视图。 */
    Entity self();

    /* ===== 类型 ===== */

    BulletKind kind();

    /**
     * 当前变体 id。0 恒为默认变体。
     *
     * <p>变体是服务端与客户端之间唯一需要同步的"外观 + 数值"开关：
     * 火炬树桩变色、冰西瓜变 ICE、星星变大，都只改这一个值。
     */
    int variant();

    void setVariant(int variant);

    /* ===== 运动 ===== */

    Vec3 position();

    Vec3 velocity();

    void setVelocity(Vec3 velocity);

    Level level();

    /* ===== 归属 ===== */

    @Nullable
    Entity ownerEntity();

    /**
     * 发射者 UUID。标记系统用它做 {@code MarkKey.source}。
     *
     * <p>无主弹药返回 {@code null}，此时不会绑定任何带来源的标记。
     */
    @Nullable
    UUID ownerUuid();

    /** 发射时快照的阵营。客户端不需要它，但契约保持完整。 */
    GroupType ownerGroup();

    /* ===== 目标 ===== */

    @Nullable
    LivingEntity target();

    void setTarget(@Nullable LivingEntity target);

    /* ===== 生命周期 ===== */

    int tickCount();

    /** 通用状态槽（磁力弹用 0..3 编码状态机）。默认 0。 */
    default int state() {
        return 0;
    }

    default void setState(int state) {
    }

    boolean isRemoved();

    void discard();

    /* ===== 伤害快照 ===== */

    /** 发射时由 Kind/ShotSpec 解析出的基础伤害，不含变体倍率。 */
    float baseDamage();

    /** 飞行中由触发器累加的伤害（服务端）。 */
    float damageAdd();

    /** 飞行中由触发器累加的伤害倍率（服务端），默认 1。 */
    float damageMultiplier();

    /* ===== 穿透 ===== */

    int pierceLeft();

    void setPierceLeft(int pierce);

    /** 该目标（按宿主计）是否已被本发弹药命中过。 */
    boolean hasHit(Entity target);

    /** 记录一次命中（含多部件到宿主的映射）。 */
    void addHitEntity(Entity target);

    /* ===== 运动 ===== */

    /** 本发弹速。直线运动用它朝方向缩放。 */
    float speed();

    /* ===== 飞行触发器 ===== */

    /** 是否已经对 {@code (transformer, provider)} 触发过。 */
    boolean hasTriggered(Identifier transformer, int providerId);

    void markTriggered(Identifier transformer, int providerId);

    /** 应用一次触发器结果：变体、伤害加算、伤害乘算。 */
    void applyTransform(BulletTransformer.Result result);
}
