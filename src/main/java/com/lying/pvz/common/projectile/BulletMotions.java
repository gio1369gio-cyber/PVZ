package com.lying.pvz.common.projectile;

import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.api.projectile.BulletMotion;
import com.lying.pvz.api.projectile.IBullet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * 内置运动策略。
 *
 * <p>Phase 2 只落地直线；抛物线 / 追踪 / 磁力状态机在后续阶段追加，
 * 不改变 {@link BulletMotion} 契约。
 */
public final class BulletMotions {

    private BulletMotions() {
    }

    public static BulletMotion direct() {
        return Direct.INSTANCE;
    }

    public static BulletMotion lob() {
        return lob(12.0F);
    }

    /** 指定弧高的抛物线；重力读 {@code BulletStats.gravity()}。 */
    public static BulletMotion lob(float arcHeight) {
        return new Lob(arcHeight);
    }

    public static BulletMotion thorn() {
        return Thorn.INSTANCE;
    }

    public static BulletMotion metal() {
        return Metal.INSTANCE;
    }

    /**
     * 磁力弹四阶段状态机。状态存 {@link IBullet#state()}：
     * 0 ABSORB / 1 BULLET / 2 WAIT / 3 SHOOT。
     */
    private enum Metal implements BulletMotion {

        INSTANCE;

        static final int ABSORB = 0;
        static final int BULLET = 1;
        static final int WAIT = 2;
        static final int SHOOT = 3;

        @Override
        public SyncMode syncMode() {
            return SyncMode.AUTHORITATIVE;
        }

        @Override
        public void aim(IBullet bullet, @Nullable LivingEntity target, @Nullable Vec3 direction) {
            bullet.setVelocity(Vec3.ZERO);
        }

        @Override
        public void tick(IBullet bullet) {
            final Entity owner = bullet.ownerEntity();
            if (!(owner instanceof LivingEntity thrower)) {
                bullet.discard();
                return;
            }

            final int state = bullet.state();
            if (state == ABSORB || state == BULLET) {
                if (bullet.self().distanceToSqr(thrower) <= 3.0D) {
                    if (state == ABSORB) {
                        if (thrower instanceof com.lying.pvz.common.level.entity.plant.assist.MagnetShroomEntity shroom) {
                            final MetalTypes[] types = MetalTypes.values();
                            final int variant = bullet.variant();
                            shroom.setMetalType((variant >= 0 && variant < types.length)
                                    ? types[variant] : MetalTypes.EMPTY);
                        }
                        bullet.discard();
                    } else {
                        bullet.setState(WAIT);
                    }
                    return;
                }
                final Vec3 pull = thrower.position().add(0.0D, thrower.getBbHeight(), 0.0D)
                        .subtract(bullet.position()).normalize().scale(0.8D);
                bullet.setVelocity(pull);
                return;
            }

            if (state == WAIT) {
                bullet.setVelocity(Vec3.ZERO);
                if (bullet.tickCount() % 50 != 0) {
                    return;
                }
                for (LivingEntity candidate : bullet.level().getEntitiesOfClass(
                        LivingEntity.class,
                        bullet.self().getBoundingBox().inflate(20.0D))) {
                    if (SweptHitResolver.canHit(bullet, candidate)) {
                        bullet.setTarget(candidate);
                        bullet.setState(SHOOT);
                        final Vec3 shoot = candidate.position().add(0.0D, candidate.getEyeHeight(), 0.0D)
                                .subtract(bullet.position()).normalize().scale(1.4D);
                        bullet.setVelocity(shoot);
                        return;
                    }
                }
            }
            // SHOOT：速度已由 WAIT 阶段设定，位移交给 BulletEntity。
        }

        @Override
        public boolean canHit(IBullet bullet) {
            return bullet.state() == SHOOT;
        }
    }

    /**
     * 荆棘/猫尾追踪弹。变体 id 编码模式与强化：
     * {@code mode = variant / 2}（0 普通 / 1 制导 / 2 自动），{@code powered = variant % 2 == 1}。
     */
    private enum Thorn implements BulletMotion {

        INSTANCE;

        private static final double AUTO_SEARCH_RANGE = 40.0D;
        private static final int AUTO_RETARGET_INTERVAL = 20;

        @Override
        public SyncMode syncMode() {
            return SyncMode.AUTHORITATIVE;
        }

        @Override
        public void aim(IBullet bullet, @Nullable LivingEntity target, @Nullable Vec3 direction) {
            if (target != null) {
                this.aimAt(bullet, target);
            } else if (direction != null && direction.lengthSqr() > 1.0E-7D) {
                bullet.setVelocity(direction.normalize().scale(bullet.speed()));
            }
        }

        @Override
        public void tick(IBullet bullet) {
            final int mode = bullet.variant() / 2;
            if (mode == 0) {
                return;
            }

            LivingEntity target = bullet.target();
            if (target != null && bullet.hasHit(target)) {
                bullet.setTarget(null);
                target = null;
            }
            if (target != null) {
                this.aimAt(bullet, target);
                return;
            }
            if (mode == 2 && bullet.tickCount() % AUTO_RETARGET_INTERVAL == 0) {
                this.retarget(bullet);
            }
        }

        private void aimAt(IBullet bullet, LivingEntity target) {
            final Vec3 dir = target.position()
                    .add(0.0D, target.getBbHeight() * 0.5D, 0.0D)
                    .subtract(bullet.position());
            if (dir.lengthSqr() > 1.0E-7D) {
                bullet.setVelocity(dir.normalize().scale(bullet.speed()));
            }
        }

        private void retarget(IBullet bullet) {
            for (LivingEntity candidate : bullet.level().getEntitiesOfClass(
                    LivingEntity.class,
                    bullet.self().getBoundingBox().inflate(AUTO_SEARCH_RANGE))) {
                if (SweptHitResolver.canHit(bullet, candidate)) {
                    bullet.setTarget(candidate);
                    return;
                }
            }
        }

        @Override
        public double timeToTarget(IBullet bullet, LivingEntity target) {
            return bullet.position().distanceTo(target.position()) / Math.max(0.001D, bullet.speed());
        }
    }

    /**
     * 抛物线：固定最高点反解初速。
     *
     * <p>与旧投掷弹的 {@code aimAt} 一致：飞行时间由弧高与重力固定，
     * 水平速度按距离反算；提前量使用目标当前 {@code deltaMovement} 做两轮定点迭代。
     */
    private record Lob(float arcHeight) implements BulletMotion {

        private static double gravity(IBullet bullet) {
            final double g = bullet.kind().stats().gravity();
            return g > 0.0D ? g : 0.1D;
        }

        @Override
        public void aim(IBullet bullet, @Nullable LivingEntity target, @Nullable Vec3 direction) {
            if (target == null) {
                if (direction != null && direction.lengthSqr() > 1.0E-7D) {
                    bullet.setVelocity(direction.normalize().scale(bullet.speed()));
                }
                return;
            }

            final double g = gravity(bullet);
            final double t1 = Math.sqrt(2.0D * this.arcHeight / g);
            Vec3 predicted = target.position();
            double t2 = 0.0D;
            for (int i = 0; i < 2; i++) {
                final double targetTop = predicted.y + target.getBbHeight();
                final double apex = bullet.position().y + this.arcHeight;
                t2 = (apex - targetTop >= 0.0D)
                        ? Math.sqrt(2.0D * (apex - targetTop) / g)
                        : 0.0D;
                predicted = target.position().add(target.getDeltaMovement().scale(t1 + t2));
            }
            this.aimAt(bullet, predicted, g, t1, t2);
        }

        @Override
        public void aimAtPoint(IBullet bullet, Vec3 point) {
            final double g = gravity(bullet);
            final double t1 = Math.sqrt(2.0D * this.arcHeight / g);
            final double apex = bullet.position().y + this.arcHeight;
            final double t2 = (apex - point.y >= 0.0D)
                    ? Math.sqrt(2.0D * (apex - point.y) / g)
                    : 0.0D;
            this.aimAt(bullet, point, g, t1, t2);
        }

        private void aimAt(IBullet bullet, Vec3 point, double g, double t1, double t2) {
            final double dx = point.x - bullet.position().x;
            final double dz = point.z - bullet.position().z;
            final double dxz = Math.sqrt(dx * dx + dz * dz);
            if (dxz < 1.0E-7D) {
                bullet.setVelocity(new Vec3(0.0D, g * t1, 0.0D));
                return;
            }
            final double vxz = dxz / (t1 + t2);
            bullet.setVelocity(new Vec3(vxz * dx / dxz, g * t1, vxz * dz / dxz));
        }

        @Override
        public void tick(IBullet bullet) {
            // 位移前不改速度；重力在 postTick 施加。
        }

        @Override
        public void postTick(IBullet bullet) {
            final double g = gravity(bullet);
            final Vec3 velocity = bullet.velocity();
            bullet.setVelocity(new Vec3(velocity.x, velocity.y - g, velocity.z));
        }

        @Override
        public double timeToTarget(IBullet bullet, LivingEntity target) {
            final double g = gravity(bullet);
            final double t1 = Math.sqrt(2.0D * this.arcHeight / g);
            final double targetTop = target.position().y + target.getBbHeight();
            final double apex = bullet.position().y + this.arcHeight;
            final double t2 = (apex - targetTop >= 0.0D)
                    ? Math.sqrt(2.0D * (apex - targetTop) / g)
                    : 0.0D;
            return t1 + t2;
        }
    }

    private enum Direct implements BulletMotion {

        INSTANCE;

        @Override
        public void aim(IBullet bullet, @Nullable LivingEntity target, @Nullable Vec3 direction) {
            Vec3 dir = direction;
            if (dir == null && target != null) {
                dir = target.position()
                        .add(0.0D, target.getBbHeight() * 0.5D, 0.0D)
                        .subtract(bullet.position());
            }
            if (dir == null || dir.lengthSqr() < 1.0E-7D) {
                bullet.setVelocity(Vec3.ZERO);
                return;
            }
            bullet.setVelocity(dir.normalize().scale(bullet.speed()));
        }

        @Override
        public void tick(IBullet bullet) {
            // 直线弹：速度恒定，由 BulletEntity 负责积分。
        }

        @Override
        public void postTick(IBullet bullet) {
            final double gravity = bullet.kind().stats().gravity();
            if (gravity <= 0.0D) {
                return;
            }
            final Vec3 velocity = bullet.velocity();
            bullet.setVelocity(new Vec3(velocity.x, velocity.y - gravity, velocity.z));
        }
    }
}
