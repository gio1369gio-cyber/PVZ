package com.lying.pvz.common.projectile;

import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.projectile.BulletImpact;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 发射时解析：把 {@link ShotSpec} + {@link BulletKind} 绑定成
 * {@link BulletShot}。
 *
 * <p>标记来源在这里绑定为发射者 UUID，所以同一株植物的子弹只会读写
 * 自己的标记层；A 植物与 B 植物即便同类型也互不可见。
 */
public final class ShotResolver {

    private ShotResolver() {
    }

    public static BulletShot resolve(Entity shooter, GroupType group,
                                     BulletKind kind, ShotSpec spec) {
        final UUID owner = shooter.getUUID();
        final int variant = kind.variants().has(spec.variant()) ? spec.variant() : 0;

        float baseDamage = spec.hasBaseDamageOverride()
                ? spec.baseDamage() : kind.stats().baseDamage();
        baseDamage += spec.damageAdd();

        final float speed = spec.hasSpeedOverride() ? spec.speed() : kind.stats().speed();
        final int lifeTicks = spec.hasLifeOverride() ? spec.lifeTicks() : kind.stats().lifeTicks();
        final int pierce = spec.hasPierceOverride() ? spec.pierce() : kind.stats().pierce();

        final List<BulletImpact.Step> impact = new ArrayList<>();
        for (BulletImpact.Factory factory : kind.impact()) {
            impact.add(factory.bind(owner, variant, kind, spec));
        }
        // 每发标记追加在 Kind 管线之后：默认先伤害（读旧层），再叠新层。
        for (ShotSpec.MarkRequest request : spec.marks()) {
            impact.add(BulletImpacts.mark(request.mark(), request.stacks())
                    .bind(owner, variant, kind, spec));
        }

        return new BulletShot(owner, group, variant, baseDamage, spec.damageMultiplier(),
                speed, lifeTicks, pierce, kind.stats().maxFlyDistance(), impact);
    }
}
