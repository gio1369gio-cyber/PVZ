package com.lying.pvz.common.level.entity.plant.enforce;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.ability.ICanPushBack;
import com.lying.pvz.api.entity.projectile.IPAZProjectile;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * 保护伞（Umbrella Leaf）。
 *
 * <p>范围内可被击退的实体（{@link ICanPushBack}，如蹦极僵尸）调 {@code pushBack()}，
 * 其余弹药（{@link IPAZProjectile}）用随机速度吹飞。
 *
 * <p>动画 {@code inc}（±1）驱动 {@link #ANIM_TICK} 范围内的开合状态。
 */
public class UmbrellaLeafEntity extends PlantEntity {

    public static final int ANIM_TICK = 10;
    private int inc = 0;

    public UmbrellaLeafEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void normalPlantTick() {
        super.normalPlantTick();
        if (this.level().isClientSide()) {
            return;
        }
        this.tickLeaf();
        if (this.getAttackTime() == 0 && this.inc > 0) {
            EntityUtil.playSound(this, PvZSoundRegister.DRAG.get());
        }
        this.setAttackTime(Mth.clamp(this.getAttackTime() + this.inc, 0, ANIM_TICK));
    }

    public void tickLeaf() {
        final double range = this.getWorkRange();
        final double height = this.getWorkHeight();
        final AABB area = new AABB(
                this.getX() - range, this.getY() - height, this.getZ() - range,
                this.getX() + range, this.getY() + height, this.getZ() + range);
        final List<Entity> list = this.level().getEntitiesOfClass(Entity.class,
                area,
                target -> EntityUtil.canTargetEntity(this, target) && !target.onGround());
        if (list.isEmpty()) {
            this.inc = -1;
            return;
        }
        this.inc = 1;
        list.forEach(target -> {
            if (target instanceof ICanPushBack pushBack) {
                pushBack.pushBack();
            } else {
                target.setDeltaMovement(
                        this.getRandom().nextGaussian(),
                        this.getRandom().nextDouble() * 2 + 0.5,
                        this.getRandom().nextGaussian());
            }
        });
    }

    @Override
    public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
        super.addAlmanacEntries(list);
        list.add(Pair.of(PAZAlmanacs.WORK_RANGE, this.getWorkRange()));
    }

    public float getWorkRange() {
        return 4.5F;
    }

    /** 垂直作用范围（格），比水平范围窄，避免隔层误弹。 */
    public float getWorkHeight() {
        return 2F;
    }

    @Override
    public boolean canPAZTarget(Entity target) {
        return target instanceof ICanPushBack
                || target instanceof IPAZProjectile
                || target instanceof BulletEntity;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose poseIn) {
        return EntityDimensions.scalable(0.7F, 1.2F);
    }

    @Override
    public int getSuperTimeLength() {
        return 0;
    }

    /* ================================================================== */
    /* NBT（ValueInput / ValueOutput）                                      */
    /* ================================================================== */

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.inc = input.getIntOr("anim_inc_num", 0);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("anim_inc_num", this.inc);
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.UMBRELLA_LEAF.get();
    }
}