package com.lying.pvz.common.projectile;

import com.lying.pvz.api.projectile.BulletTransformer;
import com.lying.pvz.api.projectile.IBullet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * 飞行中穿体触发器。
 *
 * <p>只检测"本 tick 位移段"与 provider AABB 的相交，而不是当前位置重叠：
 * 子弹停在植物旁边不会每 tick 重复触发。每个
 * {@code (transformer, provider)} 每发子弹只触发一次。
 */
public final class PassThroughScanner {

    /** 体表容差：让擦边飞过的子弹也能触发。 */
    private static final double SURFACE_TOLERANCE = 0.3D;
    private static final double SWEEP_INFLATE = 0.6D;

    private PassThroughScanner() {
    }

    public static void scan(IBullet bullet, Vec3 from, Vec3 to) {
        if (!(bullet.level() instanceof ServerLevel)) {
            return;
        }
        if (BulletTransformers.isEmpty()) {
            return;
        }

        final AABB sweep = new AABB(from, to).inflate(SWEEP_INFLATE);
        for (Entity provider : bullet.level().getEntities(bullet.self(), sweep)) {
            if (provider.getBoundingBox().inflate(SURFACE_TOLERANCE).clip(from, to).isEmpty()) {
                continue;
            }
            for (BulletTransformer transformer : BulletTransformers.forProvider(provider)) {
                if (bullet.hasTriggered(transformer.id(), provider.getId())) {
                    continue;
                }
                if (!transformer.applies(bullet, provider)) {
                    continue;
                }
                final BulletTransformer.Result result = transformer.transform(bullet, provider);
                if (!result.isNone()) {
                    bullet.applyTransform(result);
                }
                bullet.markTriggered(transformer.id(), provider.getId());
            }
        }
    }
}
