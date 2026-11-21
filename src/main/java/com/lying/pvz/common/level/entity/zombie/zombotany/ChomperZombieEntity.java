package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;

public class ChomperZombieEntity extends AbstractZombotanyEntity {
    private static final EntityDataAccessor<Integer> REST_TICK =
            SynchedEntityData.defineId(ChomperZombieEntity.class, EntityDataSerializers.INT);
    public static final int ATTACK_ANIM_CD = 30;

    public ChomperZombieEntity(EntityType<? extends AbstractZombotanyEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        AttributeInstance instance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (instance != null) {
            instance.setBaseValue(0.25);
        }
    }

    @Override
    public void normalZombieTick() {
        super.normalZombieTick();
        if (!this.level().isClientSide() && this.getTarget() != null) {
            this.lookControl.setLookAt(this.getTarget(), 30.0F, 30.0F);
        }
        if (this.isResting()) {
            this.setRestTick(this.getRestTick() - 1);
            return;
        }
        if (!this.level().isClientSide() && this.getRestTick() <= 0 && this.getTarget() != null && this.distanceTo(this.getTarget()) <= 3) {
            if (this.getAttackTime() < 15) {
                if (!EntityUtil.isEntityValid(this.getTarget())) {
                    this.setAttackTime(0);
                    return;
                }

                this.setAttackTime(this.getAttackTime() + 1);
                if (this.getAttackTime() == 15) {
                    this.performAttack();
                }
            } else {
                this.setAttackTime(this.getAttackTime() + 1);
                if (this.getAttackTime() > ATTACK_ANIM_CD) {
                    this.setAttackTime(0);
                }
            }
        }
    }
    protected void performAttack() {
        LivingEntity target = this.getTarget();
        if (target != null) {
            Level level = target.level();
            if (level instanceof ServerLevel serverLevel) {
                if (EntityUtil.isEntityValid(target)) {
                    this.doHurtTarget(serverLevel, target);
                }
                this.setRestTick(this.getRestCD());

                EntityUtil.playSound(this, PvZSoundRegister.BIG_CHOMP.get());
            }
        }
    }

    public int getRestCD() {
        return 800;
    }

    public boolean isResting() {
        return this.getRestTick() > 0;
    }

    public int getRestTick() {
        return this.entityData.get(REST_TICK);
    }

    public void setRestTick(int tick) {
        this.entityData.set(REST_TICK, tick);
    }

    @Override
    protected float getModifyAttackDamage(Entity entity, float f) {
        return !this.isResting() ? this.getEatDamage() * 10 : this.getEatDamage();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(REST_TICK, 0);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setRestTick(input.getIntOr("rest_tick", this.getRestTick()));
    }

    public float getEatDamage() {
        return 5.0F;
    }

    @Override
    public float getLife() {
        return 44.0F;
    }

    @Override
    public IZombieType getZombieType() {
        return Zombotanies.CHOMPER_ZOMBIE.get();
    }
}