package com.lying.pvz.common.level.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * 旧版掉落物 / 身体部件 / 保龄球共用的基础实体。
 *
 * <p>新弹药走 {@code AbstractPAZProjectile}（{@code setPos} 硬穿），不继承本类；
 * 本类服务的是阳光、金币、能量、僵尸身体部件等走原版 {@link MoverType#SELF}
 * 碰撞的物品型实体。
 */
public abstract class PVZBaseEntity extends Entity {

    public PVZBaseEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // 基类不定义数据；子类覆写时必须调 super.defineSynchedData(builder)
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount <= 5) {
            this.refreshDimensions();
        }
    }

    /**
     * 按当前 {@code deltaMovement} 移动一步，水中减速 + 重力。
     *
     * <p>用 {@link MoverType#SELF} 走碰撞，这是掉落物 / 身体部件需要的行为。
     * 弹药不走本类，{@code AbstractPAZProjectile#tickMove} 用 {@code setPos} 直接穿。
     */
    protected void tickMove() {
        if (!this.canSimulateMovement()) {
            return;
        }
        Vec3 motion = this.getDeltaMovement();
        double nextX = this.getX() + motion.x;
        double nextY = this.getY() + motion.y;
        double nextZ = this.getZ() + motion.z;

        float drag;
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                this.level().addParticle(ParticleTypes.BUBBLE,
                        nextX - motion.x * 0.25D, nextY - motion.y * 0.25D, nextZ - motion.z * 0.25D,
                        motion.x, motion.y, motion.z);
            }
            drag = 0.8F;
        } else {
            drag = 1F;
        }
        this.setDeltaMovement(motion.scale(drag));
        if (!this.isNoGravity()) {
            Vec3 current = this.getDeltaMovement();
            this.setDeltaMovement(current.x, current.y - this.getGravityVelocity(), current.z);
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    protected float getGravityVelocity() {
        return 0.05F;
    }
}