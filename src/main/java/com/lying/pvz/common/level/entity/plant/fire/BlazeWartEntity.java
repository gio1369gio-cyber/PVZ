package com.lying.pvz.common.level.entity.plant.fire;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.pool.BalloonZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;


/**
 * 烈焰疣。
 *
 * <p>无弹药；近战点燃目标，大招持续回血并点燃周围僵尸；
 * 血量低于 25% 后失去地狱疣，进入缓慢回血状态。
 */
public class BlazeWartEntity extends PlantEntity {

    public BlazeWartEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BlazeWartAttackGoal(this));
        this.targetSelector.addGoal(0, new PVZNearestTargetGoal(this, true, false, 3.0F, 3.0F));
    }

    public boolean hasNetherWart() {
        return !(this.getHealth() / this.getMaxHealth() < 0.25F);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (!this.hasNetherWart()) {
            return super.hurtServer(serverLevel, source, amount * 0.25F);
        }
        return super.hurtServer(serverLevel, source, amount);
    }

    @Override
    protected void normalPlantTick() {
        super.normalPlantTick();
        if (!this.level().isClientSide() && this.isPlantInSuperMode() && this.getSuperTime() % 5 == 0) {
            this.heal(this.getMaxHealth() - this.getHealth());
            float range = 5.0F;
            EntityUtil.getTargetableEntities(this, EntityUtil.getEntityAABB(this, range, range)).forEach((target) -> {
                target.hurt(PvZEntityDamageSource.normal(this), this.getAttackDamage() * 5.0F);
                EntityUtil.playSound(this, PvZSoundRegister.SWING.get());
                target.igniteForSeconds(5);
            });
        }
        if (!this.hasNetherWart() && this.getExistTick() % this.getHealCd() == 0) {
            this.heal(4);
        }
    }

    public int getHealCd() {
        return (int) this.getSkillValue(SkillTypes.WART_HEAL_CD.get());
    }

    public void attackTarget(LivingEntity target) {
        EntityUtil.playSound(this, PvZSoundRegister.SWING.get());
        target.hurt(PvZEntityDamageSource.normal(this), this.getAttackDamage());
        target.igniteForTicks(3);
    }

    @Override
    public float getLife() {
        return 80;
    }

    @Override
    public boolean canPAZTarget(Entity entity) {
        return !(entity instanceof BalloonZombieEntity) && super.canPAZTarget(entity);
    }

    @Override
    public boolean isNoAi() {
        return super.isNoAi() || !this.hasNetherWart();
    }

    @Override
    public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
        super.addAlmanacEntries(list);
        list.addAll(Arrays.asList(Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage()), Pair.of(PAZAlmanacs.ATTACK_CD, this.getAttackCD())));
    }

    public int getAttackCD() {
        return 15;
    }

    public float getAttackDamage() {
        return this.getSkillValue(SkillTypes.MORE_SWING_DAMAGE.get()) * 0.75f;
    }

    @Override
    public int getSuperTimeLength() {
        return 120;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return EntityDimensions.scalable(0.75f, 0.8f);
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.BLAZE_WART.get();
    }

    private static final class BlazeWartAttackGoal extends Goal {
        private final BlazeWartEntity attacker;

        public BlazeWartAttackGoal(BlazeWartEntity attacker) {
            this.attacker = attacker;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity living = this.attacker.getTarget();
            if (!EntityUtil.isEntityValid(living) || !this.attacker.hasNetherWart()) {
                return false;
            } else {
                return this.attacker.hasLineOfSight(living) && EntityUtil.getAttackRange(this.attacker, living, 3.0) >= EntityUtil.getNearestDistance(this.attacker, living);
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity living = this.attacker.getTarget();
            if (!EntityUtil.isEntityValid(living) || !this.attacker.hasNetherWart()) {
                return false;
            } else {
                return this.attacker.hasLineOfSight(living) && EntityUtil.getAttackRange(this.attacker, living, 3.0) >= EntityUtil.getNearestDistance(this.attacker, living);
            }
        }

        @Override
        public void stop() {
            this.attacker.setTarget(null);
            this.attacker.setAttackTime(0);
        }

        @Override
        public void tick() {
            LivingEntity target = this.attacker.getTarget();
            if (target == null) {
                return;
            }
            this.attacker.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.attacker.getAttackTime() == 0) {
                if (this.attacker.getAttackDamage() >= EntityUtil.getCurrentHealth(target)) {
                    this.attacker.setAttackTime(1);
                } else {
                    this.attacker.setAttackTime(-1);
                }
            } else if (this.attacker.getAttackTime() > 0) {
                this.attacker.setAttackTime(this.attacker.getAttackTime() + 1);
                if (this.attacker.getAttackTime() == this.attacker.getAttackCD() * 4 / 5) {
                    this.attacker.attackTarget(target);
                } else if (this.attacker.getAttackTime() >= this.attacker.getAttackCD()) {
                    this.attacker.setAttackTime(0);
                }
            } else {
                this.attacker.setAttackTime(this.attacker.getAttackTime() - 1);
                if (-this.attacker.getAttackTime() == this.attacker.getAttackCD() * 4 / 5) {
                    this.attacker.attackTarget(target);
                } else if (-this.attacker.getAttackTime() >= this.attacker.getAttackCD()) {
                    this.attacker.setAttackTime(0);
                }
            }
        }
    }
}