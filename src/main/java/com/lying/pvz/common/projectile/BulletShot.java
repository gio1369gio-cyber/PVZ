package com.lying.pvz.common.projectile;

import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.projectile.BulletImpact;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 一次发射的不可变结果。
 *
 * <p>由 {@link ShotResolver} 生成；飞行中只有
 * {@code damageAdd / damageMultiplier / pierceLeft / variant} 允许被
 * 触发器修改，其余数值不再变化。
 */
public record BulletShot(
        UUID owner,
        GroupType group,
        int variant,
        float baseDamage,
        float damageMultiplier,
        float speed,
        int lifeTicks,
        int pierce,
        double maxFlyDistance,
        List<BulletImpact.Step> impact
) {

    public BulletShot {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(group, "group");
        impact = List.copyOf(impact);
    }
}
