package com.lying.pvz.common.level.entity.plant.magic;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.ability.ICanAttract;
import com.lying.pvz.api.entity.ability.ICanBeAttracted;
import com.lying.pvz.api.entity.ability.ICanBeCharmed;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.misc.PlantAttractGoal;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.entity.zombie.roof.GargantuarEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

/**
 * 催眠菇。
 *
 * <p>被僵尸啃咬致死时，按以下规则处理：
 * <ul>
 *   <li>普通模式：将啃咬者 {@code onCharmedBy(this)}，阵营翻转；</li>
 *   <li>super 模式：直接 {@code discard} 啃咬者，并在其位置召唤一只
 *       生命值为 {@link #getSummonHealth()} 倍的魅惑巨人僵尸（继承自身魅惑状态）。</li>
 * </ul>
 * 同时自身实现 {@link ICanAttract}，可吸引附近僵尸目标。
 */
public class HypnoShroomEntity extends PlantEntity implements ICanAttract {

    public HypnoShroomEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PlantAttractGoal(this, this, 25));
    }

    @Override
    public boolean canAttract(LivingEntity target) {
        if (target instanceof ICanBeAttracted attracted
                && !attracted.canBeAttractedBy(this)) {
            return false;
        }
        if (!this.getSensing().hasLineOfSight(target)) {
            return false;
        }
        if (target instanceof Mob mob) {
            return !(mob.getTarget() instanceof HypnoShroomEntity);
        }
        return false;
    }

    @Override
    public void attract(LivingEntity target) {
        if (target instanceof Mob mob) {
            mob.setTarget(this);
        }
    }

    @Override
    public float getAttractRange() {
        return 2.5F;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (this.level().isClientSide() || !this.canNormalUpdate()) {
            return;
        }
        if (!(source instanceof PvZEntityDamageSource pds) || !pds.isEatDamage()) {
            return;
        }
        if (this.isPlantInSuperMode()) {
            if (source.getEntity() != null) {
                source.getEntity().discard();
                final GargantuarEntity gar = PvZEntityRegister.GARGANTUAR.get()
                        .create(this.level(), EntitySpawnReason.MOB_SUMMONED);
                if (gar == null) {
                    return;
                }
                EntityUtil.onEntitySpawn(this.level(), gar, source.getEntity().blockPosition());
                gar.setZombieType(PVZZombieEntity.VariantType.NORMAL);
                gar.setHealth(gar.getMaxHealth() * this.getSummonHealth());
                gar.setCharmed(!this.isCharmed());
            }
        } else if (source.getEntity() instanceof ICanBeCharmed charmed) {
            charmed.onCharmedBy(this);
        }
        EntityUtil.playSound(this, PvZSoundRegister.HYPNO.get());
    }

    @Override
    protected float getLife() {
        return 20;
    }

    public float getSummonHealth() {
        return 0.7F;
    }

    @Override
    public boolean isPlantImmuneTo(DamageSource source) {
        return false;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose poseIn) {
        return EntityDimensions.scalable(0.7F, 1.9F);
    }

    @Override
    public int getSuperTimeLength() {
        return 2400;
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.HYPNO_SHROOM.get();
    }
}