package com.lying.pvz.common.level.entity.plant.assist;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.ability.*; // ICanAttract / ICanBeAttracted / IHasWheel，路径待核实
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 炮灰灌木。
 *
 * <p>无弹药；吸引僵尸仇恨，被吃掉时反伤并随机附加虚弱/减速，可扎破轮胎。
 */
public class FodderBushEntity extends PlantEntity implements ICanAttract {

    public FodderBushEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public boolean canAttract(LivingEntity target) {
        return !(target instanceof ICanBeAttracted attracted) || attracted.canBeAttractedBy(this);
    }

    public void attract(LivingEntity target) {
        if (target instanceof Mob mob && EntityUtil.isEntityValid(target)) {
            mob.setTarget(this);
        }
    }

    public float getAttractRange() {
        return 3.5F;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (this.level() instanceof ServerLevel serverLevel
                && source instanceof PvZEntityDamageSource pds && pds.isEatDamage()) {
            final int i = this.random.nextInt(100);
            final Entity entity = source.getEntity();
            if (entity instanceof IHasWheel wheel && i < 23) {
                wheel.spikeWheelBy(this);
                this.kill(serverLevel);
            } else if (entity instanceof LivingEntity living) {
                if (i < 33 && EntityUtil.isEntityValid(living) && !living.hasEffect(MobEffects.WEAKNESS)) {
                    living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, (int) (this.getLife() * 8)));
                } else if (i < 66 && !living.hasEffect(MobEffects.SLOWNESS)) {
                    living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, (int) (this.getLife() * 8)));
                }
                final PvZEntityDamageSource damageSource = PvZEntityDamageSource.bush(this, this);
                living.hurtServer(serverLevel, damageSource, this.getLife() / 4);
            }
        }

        EntityUtil.playSound(this, PvZSoundRegister.HYPNO.get());
    }

    @Override
    public boolean canStartSuperMode() {
        return false;
    }

    @Override
    public float getLife() {
        return this.getSkillValue(SkillTypes.PLANT_MORE_LIFE.get());
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return EntityDimensions.scalable(0.6F, 1.0F);
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.FODDER_BUSH.get();
    }
}