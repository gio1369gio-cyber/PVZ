package com.lying.pvz.api.entity.projectile;

import com.lying.pvz.api.entity.IPAZEntity;
import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.entity.ability.IHasOwner;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * 弹药对外的<b>最小契约</b>。
 *
 * <p>API 层只暴露"能发射、能瞄准、能读阵营"；具体飞行 / 命中 / 穿透 / 变体
 * 由实现类承担。
 *
 * <p><b>典型调用顺序</b>（由发射方保证，缺一不可）：
 * <ol>
 *   <li>{@code ICanShoot.createProjectile()} 创建实例</li>
 *   <li>{@code setPos(...)} 设置位置（Minecraft {@code Entity} API）</li>
 *   <li>{@link #shootFrom(IPAZEntity)} 快照发射者参数</li>
 *   <li>{@link #aimAt(LivingEntity)} 或 {@link #aimAt(Vec3)} 瞄准</li>
 *   <li>{@code addFreshEntity(...)} 加入世界</li>
 * </ol>
 *
 * <p>{@link #shootFrom(IPAZEntity)} <b>必须</b>在加入世界前调用一次；
 * 飞行中不再回头读发射者。
 */
public interface IPAZProjectile extends IHasOwner {

    /**
     * 从发射者快照参数：阵营、PROJECTILE 面技能、卡片附魔、生命周期、
     * 弹道角度、发射者提供的效果。
     *
     * <p>必须在加入世界之前调用一次。飞行中不再回头读发射者。
     */
    void shootFrom(IPAZEntity shooter);

    /**
     * 朝目标瞄准（按轨迹类型决定直线或弹道）。
     */
    void aimAt(LivingEntity target);

    /**
     * 朝方向瞄准。
     */
    void aimAt(Vec3 direction);

    /**
     * 发射时快照的阵营。僵尸植物发射的弹药为 {@link GroupType#ZOMBIES}。
     */
    GroupType getOwnerGroup();

    /**
     * 剩余存活 tick。
     */
    int getMaxLiveTick();

    /**
     * 设置存活 tick（发射者可通过此方法覆盖默认值）。
     */
    void setMaxLiveTick(int ticks);

    /**
     * 解析拥有者实体。无主或拥有者不在世界中时返回空。
     *
     * <p>默认基于 {@link IHasOwner#getOwnerUUID()} 在 {@code level} 中查找。
     *
     * <p><b>注意</b>：不能用 {@code flatMap}——{@code Level.getEntity(UUID)}
     * 返回可空的 {@code Entity}，而非 {@code Optional<Entity>}。用
     * {@code map} 即可，它会在 mapper 返回 {@code null} 时自动转为
     * {@link Optional#empty()}。
     *
     * <p>实现可覆写为缓存命中，避免每次查找实体索引。
     */
    default Optional<Entity> getOwnerEntity(Level level) {
        return this.getOwnerUUID().map(level::getEntity);
    }
}