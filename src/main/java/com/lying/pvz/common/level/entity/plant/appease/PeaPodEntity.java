package com.lying.pvz.common.level.entity.plant.appease;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.plant.PVZPlants;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 豌豆荚。
 *
 * <p>继承 {@link PeaShooterEntity} 的 {@link com.lying.pvz.common.projectile.BulletKinds#PEA} 弹药，
 * 荚内头数越多单轮发射的豌豆越多（最多 5 颗），可通过交互叠加头数。
 */
public class PeaPodEntity extends PeaShooterEntity {
    private static final EntityDataAccessor<Integer> DATA_COUNT =
            SynchedEntityData.defineId(PeaPodEntity.class, EntityDataSerializers.INT);
    private static final int MAX_COUNT = 5;
    private static final double SIDE_OFFSET = 1.0D;

    public PeaPodEntity(EntityType<? extends PeaShooterEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public void shootBullet() {
        if (this.isPlantInSuperMode()) {
            if (this.getCount() >= 1 && this.tickCount % 20 == 0) {
                this.performShoot(SHOOT_OFFSET, 0.0, -0.75, true, FORWARD_SHOOT_ANGLE);
            }
            return;
        }
        if (this.getCount() >= 1) this.performShoot(SHOOT_OFFSET, 0.0, -0.75, true, FORWARD_SHOOT_ANGLE);
        if (this.getCount() >= 2) this.performShoot(SHOOT_OFFSET, SIDE_OFFSET, -0.75, false, -4.5);
        if (this.getCount() >= 3) this.performShoot(SHOOT_OFFSET, -SIDE_OFFSET, -0.75, false, 4.5);
        if (this.getCount() >= 4) this.performShoot(SHOOT_OFFSET, 0.0, 0.0, false, FORWARD_SHOOT_ANGLE);
        if (this.getCount() >= 5) this.performShoot(SHOOT_OFFSET, 0.0, 0.75, false, FORWARD_SHOOT_ANGLE);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.getCount() >= MAX_COUNT) {
            return InteractionResult.PASS;
        }
        final InteractionResult result = super.mobInteract(player, hand);
        if (result == InteractionResult.SUCCESS_SERVER) {
            this.addCount();
        }
        return result;
    }

    /**
     * 满血时用同种豌豆荚卡片交互也能加头：这里在满血情况下直接返回「已处理」，
     * 让 {@link #mobInteract} 拿到 SUCCESS_SERVER 后执行 {@code addCount()}。
     * 不走治疗逻辑，因此不会误加再生效果；非同种卡片仍按原逻辑返回 false。
     */
    @Override
    public boolean onHealBy(IPlantType plantType, float percent) {
        if (plantType == this.getPlantType() && this.getHealth() >= this.getMaxHealth()) {
            return true;
        }
        return super.onHealBy(plantType, percent);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_COUNT, 1);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Count", this.getCount() - 1);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        int i = input.getIntOr("Count", 0);
        if (i < 0) {
            i = 0;
        }
        this.setCount(i + 1);
    }

    private void setCount(int count) {
        this.entityData.set(DATA_COUNT, count);
    }

    private void addCount() {
        this.entityData.set(DATA_COUNT, this.getCount() + 1);
    }

    public int getCount() {
        return this.entityData.get(DATA_COUNT);
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.PEA_POD.get();
    }
}