package com.lying.pvz.common.level.entity.zombie.boss;

import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.projectile.AbstractPAZProjectile;
import com.lying.pvz.common.level.entity.zombie.grass.TombStoneEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

/**
 * 狮身终结者的导弹：TNT 材质，直线追踪带标记的植物。
 *
 * <p>命中植物 → 摧毁植物 + 在旁侧空地生成 2~3 个墓碑（{@link TombStoneEntity}）。
 * 走 {@link AbstractPAZProjectile} 的标准弹道/命中管线，owner 阵营为僵尸。
 */
public class SphinxMissileEntity extends AbstractPAZProjectile {

    /** 命中后生成的墓碑数量范围。 */
    private static final int TOMB_MIN = 2;
    private static final int TOMB_MAX = 3;

    public SphinxMissileEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    /** BOSS 发射构造：绑定 owner 与追踪目标植物。 */
    public SphinxMissileEntity(Level level, LivingEntity owner, LivingEntity target) {
        this(PvZEntityRegister.SPHINX_MISSILE.get(), level);
        this.setOwnerUUID(owner.getUUID());
        this.ownerGroup = EntityUtil.getEntityGroup(owner);
        this.setTargetId(target.getUUID());
        this.shootOrigin = this.position();
        // 从 BOSS 眼睛高度射向植物头顶
        this.setPos(owner.getX(), owner.getY() + owner.getEyeHeight(), owner.getZ());
        Vec3 aim = target.position().add(0, target.getBbHeight() * 0.5, 0).subtract(this.position()).normalize();
        this.aimAt(aim);
    }

    @Override
    protected float getBaseBulletSpeed() {
        return 0.6F;
    }

    @Override
    protected float getBaseAttackDamage() {
        return 0F; // 不靠伤害，直接 kill + 墓碑
    }

    @Override
    public boolean isHoming() {
        return true;
    }

    @Override
    protected void onImpact(HitResult result) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            this.discard();
            return;
        }

        if (result instanceof EntityHitResult entityHit
                && entityHit.getEntity() instanceof PlantEntity plant) {
            plant.kill(serverLevel);
            spawnTombsNear(serverLevel, plant.blockPosition());
            serverLevel.sendParticles(ParticleTypes.EXPLOSION,
                    plant.getX(), plant.getY() + 1, plant.getZ(), 12, 0.8, 0.8, 0.8, 0.02);
        } else {
            // 打空/打方块：也在落点附近尝试生成墓碑
            BlockPos p = BlockPos.containing(result.getLocation());
            spawnTombsNear(serverLevel, p);
        }
        this.discard();
    }

    /** 在落点旁侧空地生成 2~3 个墓碑。 */
    private void spawnTombsNear(ServerLevel level, BlockPos center) {
        final int count = TOMB_MIN + this.getRandom().nextInt(TOMB_MAX - TOMB_MIN + 1);
        int placed = 0;
        // 在中心周围环形找空地
        for (int radius = 1; radius <= 4 && placed < count; ++radius) {
            for (int dx = -radius; dx <= radius && placed < count; ++dx) {
                for (int dz = -radius; dz <= radius && placed < count; ++dz) {
                    if (Math.abs(dx) != radius && Math.abs(dz) != radius) continue;
                    BlockPos p = WorldUtil.getSuitableHeightPos(level, center.offset(dx, 0, dz));
                    if (level.getBlockState(p).isAir()
                            && level.getBlockState(p.below()).isSolidRender()) {
                        TombStoneEntity.spawnTombStone(level, p);
                        ++placed;
                    }
                }
            }
        }
    }

    @Override
    public int getMaxLiveTick() {
        return 200;
    }
}
