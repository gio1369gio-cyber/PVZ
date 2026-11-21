package com.lying.pvz.common.level.entity.zombie.custom;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.zombie.CustomZombies;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.base.PlantPultEntity;
import com.lying.pvz.common.level.entity.zombie.base.DefenceZombieEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.common.level.entity.zombie.part.PVZHealthPartEntity;
import com.lying.pvz.utils.EffectUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class TargetArrowZombieEntity extends DefenceZombieEntity {
    public TargetArrowZombieEntity(EntityType<? extends DefenceZombieEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public SoundEvent getPartHurtSound() {
        return SoundEvents.WOOL_FALL;
    }

    @Override
    public void resetParts() {
        this.removeParts();
        this.part = new PVZHealthPartEntity(this, 1f, 1f);
        this.part.setOwner(this);
    }

    @Override
    protected PVZHealthPartEntity createPart() {
        return this.part;
    }

    @Override
    protected float getPartHeightOffset() {
        if (this.isMiniZombie()) return 0.2F;
        return 0.7f;
    }

    @Override
    public void onOuterDefenceBroken() {
        super.onOuterDefenceBroken();
        if (!this.level().isClientSide()) {
            this.addEffect(EffectUtil.effect(MobEffects.SPEED, 120000, 1));
            EntityUtil.playSound(this, PvZSoundRegister.ZOMBIE_ANGRY.get());
        }
    }

    @Override
    public boolean canLostHand() {
        return super.canLostHand() && !this.canPartsExist();
    }

    @Override
    protected void setBodyStates(ZombieDropBodyEntity body) {
        super.setBodyStates(body);
        body.setHandDefence(this.canPartsExist());
    }

    @Override
    public SoundEvent getPartDeathSound() {
        return SoundEvents.SHIELD_BLOCK.value();
    }

    @Override
    public float getWalkSpeed() {
        return ZombieUtil.WALK_LITTLE_SLOW;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.canPartsExist() && this.canHit(source)) {
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    private boolean canHit(DamageSource source) {
        if (source instanceof PvZEntityDamageSource pvzSource) {
            return pvzSource.isParabola();
        }
        return false;
    }

    @Override
    public boolean canBeTargetBy(LivingEntity living) {
        if (living instanceof PlantPultEntity && this.canPartsExist()) {
            return false;
        }
        return super.canBeTargetBy(living);
    }

    @Override
    public float getLife() {
        return 22;
    }

    @Override
    public float getOuterLife() {
        return 24;
    }

    @Override
    public IZombieType getZombieType() {
        return CustomZombies.TARGET_ARROW_ZOMBIE.get();
    }
}