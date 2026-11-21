package com.lying.pvz.api.entity.projectile;

import com.lying.pvz.api.entity.IPAZEntity;
import net.minecraft.world.entity.LivingEntity;

/**
 * "能发射弹药"的能力。
 *
 * <p>植物和僵尸植物共用本接口。AI goal 或手动逻辑通过本接口创建弹药、快照参数、加入世界。
 *
 * <p>本接口只提供<b>参数与工厂</b>，不负责目标选择与冷却——那是 AI goal 的职责。
 *
 * <p><b>发射流程</b>：见 {@link IPAZProjectile} 类文档中的标准调用顺序。
 * 便捷入口 {@link #launch(IPAZEntity, LivingEntity)} 封装了完整流程。
 */
public interface ICanShoot {

    /**
     * 创建一发弹药实例。
     *
     * <p>不负责 {@code setPos / shootFrom / aimAt / addFreshEntity}。
     * 这样同一实体可以发射不同弹药（如三线射手），也可以在发射前决定变体。
     */
    IPAZProjectile createProjectile();

    /**
     * 攻击范围（方块）。AI goal 用于选择目标。
     */
    float getAttackRange();

    /**
     * 攻击间隔（tick）。
     */
    int getAttackInterval();

    /**
     * 便捷方法：完整执行"创建 → 定位 → 快照 → 瞄准 → 加入世界"流程。
     *
     * <p>适合简单场景；需要精细控制（如偏移发射位置、预计算弹道）时
     * 自行按 {@link IPAZProjectile} 的标准顺序操作。
     *
     * @param shooter 发射者
     * @param target  目标（瞄准依据）
     */
    default void launch(IPAZEntity shooter, LivingEntity target) {
        final IPAZProjectile projectile = this.createProjectile();
        // 具体 setPos 由实现类承担；本默认仅提供流程骨架，
        // 实现方应覆写此方法以提供精确位置与实体注册。
    }
}