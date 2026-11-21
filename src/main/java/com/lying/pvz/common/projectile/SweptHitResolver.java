package com.lying.pvz.common.projectile;

import com.lying.pvz.api.entity.IPAZEntity;
import com.lying.pvz.api.projectile.BulletImpact;
import com.lying.pvz.api.projectile.IBullet;
import com.lying.pvz.common.level.entity.MultiPartEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * 扫掠命中解析。
 *
 * <p>返回的接触点已经按弹药渲染半径（命中盒半宽）向外偏移：
 * 子弹中心停在目标真实 AABB 外半个视觉宽度处，渲染面恰好贴住目标。
 *
 * <p>多部件实体先映射到宿主，再走阵营 / 可命中判定；返回的
 * {@link BulletImpact.Hit#target()} 是宿主。
 */
public final class SweptHitResolver {

    private static final TagKey<EntityType<?>> AIRBORNE =
            TagKey.create(Registries.ENTITY_TYPE, StringUtil.id("airborne"));
    private static final double AIRBORNE_Y_THRESHOLD = 1.5D;

    private SweptHitResolver() {
    }

    public static @Nullable BulletImpact.Hit resolve(IBullet bullet, Vec3 from, Vec3 to) {
        final Entity self = bullet.self();
        final Level level = bullet.level();

        final BlockHitResult blockHit = level.clip(new ClipContext(
                from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, self));
        final boolean ignoreBlock = blockHit.getType() == HitResult.Type.BLOCK
                && bullet.kind().ignoreBlocks().test(level.getBlockState(blockHit.getBlockPos()));
        final Vec3 end = (blockHit.getType() == HitResult.Type.BLOCK && !ignoreBlock)
                ? blockHit.getLocation()
                : to;

        final AABB search = self.getBoundingBox()
                .expandTowards(end.subtract(from))
                .inflate(1.0D);
        final float margin = Math.max(0.0F,
                bullet.kind().hitbox(bullet.variant()).width() * 0.5F);

        // 贴脸：起点已在某可命中实体碰撞盒内时，ProjectileUtil 的扫掠会漏判，
        // 这里直接按「起点命中」返回该实体，避免贴脸时子弹穿透无伤害。
        {
            Entity closest = null;
            double closestSq = Double.MAX_VALUE;
            for (Entity candidate : level.getEntities(self, search, c -> canHit(bullet, c))) {
                if (!candidate.getBoundingBox().inflate(margin).contains(from)) {
                    continue;
                }
                final double d = candidate.distanceToSqr(self);
                if (d < closestSq) {
                    closestSq = d;
                    closest = candidate;
                }
            }
            if (closest != null) {
                final Entity host = resolveOwnerEntity(closest);
                if (host != null && canHit(bullet, host)) {
                    return BulletImpact.Hit.entity(closest, from);
                }
            }
        }

        final EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                level, self, from, end, search,
                candidate -> canHit(bullet, candidate), margin);

        if (entityHit != null) {
            final Entity hitEntity = entityHit.getEntity();
            final Entity host = resolveOwnerEntity(hitEntity);
            if (host != null && canHit(bullet, host)) {
                return BulletImpact.Hit.entity(hitEntity, entityHit.getLocation());
            }
        }


        if (!ignoreBlock && blockHit.getType() == HitResult.Type.BLOCK) {
            final BlockState state = level.getBlockState(blockHit.getBlockPos());
            final Vec3 normal = Vec3.atLowerCornerOf(blockHit.getDirection().getStep());
            return BulletImpact.Hit.block(blockHit.getLocation(), normal, state);
        }
        return null;
    }

    /** 阵营 + 空中 + 目标侧可命中性。 */
    public static boolean canHit(IBullet bullet, Entity candidate) {
        if (!bullet.kind().motion().canHit(bullet)) {
            return false;
        }
        if (candidate == null || candidate == bullet.self() || !candidate.isPickable()) {
            return false;
        }
        final Entity target = resolveOwnerEntity(candidate);
        if (!(target instanceof LivingEntity living) || !living.isAlive()) {
            return false;
        }
        if (bullet.hasHit(living)) {
            return false;
        }
        if (EntityUtil.getEntityGroup(living) == bullet.ownerGroup()) {
            return false;
        }
        if (!bullet.kind().stats().canHitAirborne() && isAirborne(bullet, target)) {
            return false;
        }
        if (EntityUtil.isFogHiddenTo(bullet.self(), living)) {
            return false;
        }
        return !(living instanceof IPAZEntity paz) || paz.canBeHitBy(bullet.self());
    }

    @Nullable
    private static Entity resolveOwnerEntity(@Nullable Entity target) {
        if (target instanceof MultiPartEntity part) {
            return part.getOwner();
        }
        return target;
    }

    private static boolean isAirborne(IBullet bullet, Entity target) {
        if (target.is(AIRBORNE)) {
            return true;
        }
        return !target.onGround() && target.getY() - bullet.position().y > AIRBORNE_Y_THRESHOLD;
    }
}
