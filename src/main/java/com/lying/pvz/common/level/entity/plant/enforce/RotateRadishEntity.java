package com.lying.pvz.common.level.entity.plant.enforce;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * 转转萝卜。
 *
 * <p>无弹药；近战击退并伤害周围僵尸，大招对更大范围持续造成伤害。
 */
public class RotateRadishEntity extends PlantEntity {

    public RotateRadishEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new RotateAttackGoal(this));
        this.targetSelector.addGoal(0, new PVZNearestTargetGoal(this, false, false, 3.0F, 3.0F));
    }

    public void performAttack() {
        for (int i = 0; i < 5; ++i) {
            EntityUtil.spawnParticle(this, 7);
        }

        float range = 2.0F;
        EntityUtil.getTargetableEntities(this, EntityUtil.getEntityAABB(this, range, range)).forEach((target) -> {
            double d1 = target.getX() - this.getX();

            double d0;
            for (d0 = target.getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01) {
                d1 = (Math.random() - Math.random()) * 0.01;
            }
            if (target instanceof LivingEntity living && EntityUtil.isEntityValid(target)) {
                living.knockback(2, d1, d0, PvZEntityDamageSource.normal(this), this.getAttackDamage());
            }
            target.hurt(PvZEntityDamageSource.normal(this), this.getAttackDamage());
        });
        EntityUtil.playSound(this, PvZSoundRegister.SWING.get());
    }

    // 大招
    @Override
    protected void normalPlantTick() {
        super.normalPlantTick();
        if (!this.level().isClientSide() && this.isPlantInSuperMode() && this.getSuperTime() % 5 == 0) {
            float range = 5.0F;
            EntityUtil.getTargetableEntities(this, EntityUtil.getEntityAABB(this, range, range)).forEach((target) -> {
                double d1 = target.getX() - this.getX();

                double d0;
                for (d0 = target.getZ() - this.getZ(); d1 * d1 + d0 * d0 < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01) {
                    d1 = (Math.random() - Math.random()) * 0.01;
                }

                target.hurt(PvZEntityDamageSource.normal(this), this.getAttackDamage() * 5.0F);

                ((LivingEntity) target).knockback(0.5f, d1, d0, PvZEntityDamageSource.normal(this), this.getAttackDamage() * 5.0F);
                EntityUtil.spawnParticle(target, 7);
                EntityUtil.playSound(this, PvZSoundRegister.SWING.get());
            });
        }
    }

    @Override
    public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
        super.addAlmanacEntries(list);
        list.addAll(Arrays.asList(
                Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage()),
                Pair.of(PAZAlmanacs.ATTACK_CD, this.getAttackCD())
        ));
    }

    public int getAttackCD() {
        return 30;
    }

    public float getAttackDamage() {
        return this.getSkillValue(SkillTypes.MORE_SWING_DAMAGE.get()) / 1.5F;
    }

    private static final class RotateAttackGoal extends Goal {
        private final RotateRadishEntity attacker;

        public RotateAttackGoal(RotateRadishEntity attacker) {
            this.attacker = attacker;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity living = this.attacker.getTarget();
            if (!EntityUtil.isEntityValid(living)) {
                return false;
            } else {
                return this.attacker.hasLineOfSight(living)
                        && EntityUtil.getAttackRange(this.attacker, living, 3.0) >= EntityUtil.getNearestDistance(this.attacker, living);
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity living = this.attacker.getTarget();
            if (!EntityUtil.isEntityValid(living)) {
                return false;
            } else {
                return this.attacker.hasLineOfSight(living)
                        && EntityUtil.getAttackRange(this.attacker, living, 3.0) >= EntityUtil.getNearestDistance(this.attacker, living);
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
            if (this.attacker.getAttackTime() == 0) {
                if (this.attacker.getAttackDamage() >= EntityUtil.getCurrentHealth(target)) {
                    this.attacker.setAttackTime(1);
                } else {
                    this.attacker.setAttackTime(-1);
                }
            } else if (this.attacker.getAttackTime() > 0) {
                this.attacker.setAttackTime(this.attacker.getAttackTime() + 1);
                if (this.attacker.getAttackTime() == this.attacker.getAttackCD() * 4 / 5) {
                    this.attacker.performAttack();
                } else if (this.attacker.getAttackTime() >= this.attacker.getAttackCD()) {
                    this.attacker.setAttackTime(0);
                }
            } else {
                this.attacker.setAttackTime(this.attacker.getAttackTime() - 1);
                if (-this.attacker.getAttackTime() == this.attacker.getAttackCD() * 4 / 5) {
                    this.attacker.performAttack();
                } else if (-this.attacker.getAttackTime() >= this.attacker.getAttackCD()) {
                    this.attacker.setAttackTime(0);
                }
            }
        }
    }

    @Override
    public int getSuperTimeLength() {
        return 120;
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.ROTATE_RADISH.get();
    }
}