package com.lying.pvz.common.level.entity.plant.electric;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.level.entity.drop.DropEntity;
import com.lying.pvz.common.level.entity.drop.SunEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantShooterEntity;
import com.lying.pvz.common.level.entity.plant.light.SunFlowerEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * 棱镜草（放大镜草）。
 *
 * <p>消耗阳光发射 {@link com.lying.pvz.common.projectile.BulletKinds#LIGHT_BEAM} 光束；
 * 大招期间蓄力并读附近向日葵数量，对目标造成范围伤害。
 */
public class MagnifyingGrassEntity extends PlantShooterEntity {
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET =
            SynchedEntityData.defineId(MagnifyingGrassEntity.class, EntityDataSerializers.INT);

    private LivingEntity clientSideCachedAttackTarget;
    private int clientSideAttackTime;

    public int sunCost = 50;

    public MagnifyingGrassEntity(EntityType<? extends PlantShooterEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SuperAttackGoal(this));
        this.targetSelector.addGoal(1, new PVZNearestTargetGoal(this, false, true, 10, 10));
    }

    @Override
    protected @Nullable BulletKind getBulletKind() {
        return BulletKinds.LIGHT_BEAM;
    }

    @Override
    protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
        return PvZEntityRegister.LIGHT_BEAM.get();
    }

    @Override
    protected void configureShot(ShotSpec spec) {
        spec.baseDamage(this.getAttackDamage());
    }

    @Override
    public float getAttackDamage() {
        return this.getSkillValue(SkillTypes.LIGHT_BEAM_DAMAGE.get());
    }

    public float getSuperAttackDamage() {
        return this.getSkillValue(SkillTypes.MAGNIFYING_GRASS_DAMAGE.get());
    }


    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        // 超级模式期间由 SuperAttackGoal 蓄力攻击，不响应手动右键，避免扣阳光却因 shootBullet 被拦截而"吞掉"。
        if (!this.level().isClientSide() && !this.isPlantInSuperMode() && this.getAttackTime() <= 0) {
            if (PlayerUtil.getResource(player, Resources.SUN_NUM) > sunCost && this.getTarget() != null && this.getAttackTime() == 0 && !this.isDeadOrDying()) {
                if (!player.isCreative()) PlayerUtil.addResource(player, Resources.SUN_NUM, -sunCost);
                this.setAttackTime(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.interact(player, hand, location);
    }


    @Override
    public void shootBullet() {
        if (!this.isPlantInSuperMode()) {
            this.performShoot(0.2, 0.0, 0.0, this.getAttackTime() == 1, 0.0);
        }
    }

    @Override
    public int getShootCD() {
        return getSuperTime();
    }

    @Override
    public int getSuperTimeLength() {
        return 200;
    }

    @Override
    public void startShootAttack() {}

    private void setActiveAttackTarget(int activeAttackTargetId) {
        this.entityData.set(DATA_ID_ATTACK_TARGET, activeAttackTargetId);
    }

    public boolean hasActiveAttackTarget() {
        return this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    @Override
    public void normalPlantTick() {
        super.normalPlantTick();
        Optional.ofNullable(this.getTarget()).ifPresent((target) -> {
            EntityUtil.getNormalisedVector2d(this, target);

            if (this.hasActiveAttackTarget()) {
                if (this.clientSideAttackTime < 0) {
                    ++this.clientSideAttackTime;
                }

                LivingEntity lv3 = this.getActiveAttackTarget();
                if (lv3 != null) {
                    this.getLookControl().setLookAt(lv3, 90.0F, 90.0F);
                    this.getLookControl().tick();
                }
            } else tickSunCollect();
        });
    }

    private void tickSunCollect() {
        if (this.level().isClientSide() || this.tickCount % 20 != 0) {
            return;
        }
        List<SunEntity> sunList = this.level().getEntitiesOfClass(SunEntity.class, MathUtil.getAABBWithPos(this.blockPosition(), 10.0),
                sun -> sun.getDropState() == DropEntity.DropStates.NORMAL && !sun.isRemoved());

        if (sunList.isEmpty()) {
            return;
        }

        SunEntity sunEntity = sunList.get(0);
        if (sunEntity == null) {
            return;
        }

        if (this.getAttackTime() <= 0 && sunEntity.getAmount() >= this.sunCost) {
            double speed = 0.15;
            Vec3 now = new Vec3(this.blockPosition().getX() + 0.5, this.blockPosition().getY() + 1.0, this.blockPosition().getZ() + 0.5);
            Vec3 vec = now.subtract(sunEntity.position());

            if (vec.length() <= 1.0) {
                if (sunEntity.getAmount() <= 0) {
                    sunEntity.discard();
                } else {
                    sunEntity.setAmount(sunEntity.getAmount() - this.sunCost);
                }
                this.setAttackTime(1);
            } else {
                sunEntity.setDeltaMovement(vec.normalize().scale(speed));
            }
        }
    }

    @Nullable
    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        } else if (this.level().isClientSide()) {
            if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            } else {
                Entity entity = this.level().getEntity(this.entityData.get(DATA_ID_ATTACK_TARGET));
                if (entity instanceof LivingEntity living) {
                    this.clientSideCachedAttackTarget = living;
                    return this.clientSideCachedAttackTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_ATTACK_TARGET, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataParameter) {
        super.onSyncedDataUpdated(dataParameter);
        if (DATA_ID_ATTACK_TARGET.equals(dataParameter)) {
            this.clientSideAttackTime = 0;
            this.clientSideCachedAttackTarget = null;
        }
    }

    public int getAttackDuration() {
        return this.getSuperTime();
    }

    static class SuperAttackGoal extends Goal {
        private final MagnifyingGrassEntity grass;
        private int attackTime;

        public SuperAttackGoal(MagnifyingGrassEntity grass) {
            this.grass = grass;
        }

        @Override
        public boolean canUse() {
            LivingEntity lv = this.grass.getTarget();
            return lv != null && lv.isAlive() && this.grass.isPlantInSuperMode();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity lv = this.grass.getTarget();
            return super.canContinueToUse() && (EntityUtil.isEntityValid(lv) && this.grass.distanceToSqr(lv) > 1.0) && this.grass.isPlantInSuperMode();
        }

        @Override
        public void start() {
            LivingEntity lv = this.grass.getTarget();

            this.attackTime = -10;
            if (EntityUtil.isEntityValid(lv)) this.grass.getLookControl().setLookAt(lv, 90.0F, 90.0F);
            this.grass.getNavigation().stop();
        }

        @Override
        public void stop() {
            this.grass.setActiveAttackTarget(0);
            this.grass.setTarget(null);
        }

        @Override
        public void tick() {
            LivingEntity living = this.grass.getTarget();
            if (EntityUtil.isEntityValid(living)) {
                this.grass.getLookControl().setLookAt(living, 90.0F, 90.0F);
                if (!this.grass.hasLineOfSight(living)) {
                    this.grass.setTarget(null);
                } else {
                    ++this.attackTime;
                    if (this.attackTime == 0 && this.grass.getTarget() != null) {
                        this.grass.setActiveAttackTarget(this.grass.getTarget().getId());
                        // 注：原版 byte 21 是守卫者激光事件，会令客户端把实体强转 Guardian 而崩溃，
                        // 故不广播 entity event，光束外观由子弹 trail 粒子负责。
                    } else if (this.attackTime >= this.grass.getAttackDuration()) {
                        float baseDamage = this.grass.getSuperAttackDamage();
                        float inflate = 8.0f;
                        AABB entityAABB = this.grass.getBoundingBox().inflate(inflate, inflate / 2, inflate);
                        List<SunFlowerEntity> sunFlower = this.grass.level().getEntitiesOfClass(SunFlowerEntity.class, entityAABB);
                        float finalDamage = baseDamage * sunFlower.size();

                        living.hurt(PvZEntityDamageSource.magnifyingGrass(this.grass, this.grass), finalDamage);
                        // 攻击一次后结束本次蓄力，避免 targetSelector 立即重填 target 导致反复攻击刷屏。
                        this.grass.setSuperTime(0);
                        this.grass.setTarget(null);
                        this.stop();
                    }
                }
                super.tick();
            }
        }
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.MAGNIFYING_GRASS.get();
    }
}