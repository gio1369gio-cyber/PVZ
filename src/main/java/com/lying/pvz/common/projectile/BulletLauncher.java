package com.lying.pvz.common.projectile;

import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * 发射入口：创建实体 → 定位 → 绑定 Shot → 瞄准 → 加入世界。
 *
 * <p>植物、僵尸植物、道具统一走这里，不再让每个发射者自己拼
 * {@code setPos / shootFrom / aimAt / addFreshEntity}。
 */
public final class BulletLauncher {

    private BulletLauncher() {
    }

    public static @Nullable BulletEntity launch(
            ServerLevel level,
            @Nullable EntityType<? extends BulletEntity> type,
            Entity shooter,
            GroupType group,
            BulletKind kind,
            ShotSpec spec,
            Vec3 position,
            @Nullable LivingEntity target,
            @Nullable Vec3 direction
    ) {
        if (type == null) {
            return null;
        }
        final BulletEntity bullet = type.create(level, EntitySpawnReason.MOB_SUMMONED);
        if (bullet == null) {
            return null;
        }

        bullet.setPos(position);
        bullet.applyShot(ShotResolver.resolve(shooter, group, kind, spec));
        bullet.setTarget(target);

        kind.motion().aim(bullet, target, direction);
        level.addFreshEntity(bullet);
        return bullet;
    }

    /**
     * 以指定初速度发射（不做瞄准）。土豆雷的随机散射使用。
     */
    public static @Nullable BulletEntity launchRaw(
            ServerLevel level,
            @Nullable EntityType<? extends BulletEntity> type,
            Entity shooter,
            GroupType group,
            BulletKind kind,
            ShotSpec spec,
            Vec3 position,
            Vec3 velocity
    ) {
        if (type == null) {
            return null;
        }
        final BulletEntity bullet = type.create(level, EntitySpawnReason.MOB_SUMMONED);
        if (bullet == null) {
            return null;
        }

        bullet.setPos(position);
        bullet.applyShot(ShotResolver.resolve(shooter, group, kind, spec));
        bullet.setVelocity(velocity);
        level.addFreshEntity(bullet);
        return bullet;
    }

    /**
     * 朝固定坐标发射（投石车/玉米炮对地面目标）。
     */
    public static @Nullable BulletEntity launchAtPoint(
            ServerLevel level,
            @Nullable EntityType<? extends BulletEntity> type,
            Entity shooter,
            GroupType group,
            BulletKind kind,
            ShotSpec spec,
            Vec3 position,
            Vec3 point
    ) {
        if (type == null) {
            return null;
        }
        final BulletEntity bullet = type.create(level, EntitySpawnReason.MOB_SUMMONED);
        if (bullet == null) {
            return null;
        }

        bullet.setPos(position);
        bullet.applyShot(ShotResolver.resolve(shooter, group, kind, spec));

        kind.motion().aimAtPoint(bullet, point);
        level.addFreshEntity(bullet);
        return bullet;
    }
}
