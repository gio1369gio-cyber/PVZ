package com.lying.pvz.common.level.entity.zombie.roof;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.attack.PVZZombieAttackGoal;
import com.lying.pvz.common.level.entity.plant.spear.SpikeRockEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class Edgar090547Entity extends ZombieEntity {
    public Edgar090547Entity(EntityType<? extends ZombieEntity> type, Level level) {
        super(type, level);
        this.setIsWholeBody();
    }

    protected PvZEntityDamageSource getZombieAttackDamageSource() {
        return PvZEntityDamageSource.causeCrushDamage(this);
    }

    public int getCrushCD() {
        return 30;
    }

    private static final class CrushAttackGoal extends Goal {
        private final Edgar090547Entity attacker;

        public CrushAttackGoal(Edgar090547Entity creature) {
            this.attacker = creature;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.attacker.getAttackTime() > 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.attacker.getAttackTime() > 0;
        }

        @Override
        public void tick() {
            Level level = this.attacker.level();
            if (level != null) {
                if (level instanceof ServerLevel serverLevel) {
                    if (this.attacker.canNormalUpdate() && this.attacker.getAttackTime() > 0) {
                        this.attacker.setAttackTime(this.attacker.getAttackTime() - 1);
                        if (this.attacker.getAttackTime() == this.attacker.getCrushCD() / 3 && this.attacker.getTarget() != null) {
                            this.attacker.doHurtTarget(serverLevel,this.attacker.getTarget());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void registerAttackGoals() {
        this.goalSelector.addGoal(3, new MoveToTargetGoal(this, true));
        this.goalSelector.addGoal(2, new CrushAttackGoal(this));
    }

    private final class MoveToTargetGoal extends PVZZombieAttackGoal {
        public MoveToTargetGoal(Edgar090547Entity creature, boolean useLongMemory) {
            super(creature, useLongMemory);
        }

        @Override
        public boolean canUse() {
            return this.zombie.getAttackTime() == 0 && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.zombie.getAttackTime() == 0 && super.canContinueToUse();
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            double dis = EntityUtil.getNearestDistance(this.attacker, target);
            double range = this.getAttackReachSqr(target);
            if (range >= dis && this.attackTick <= 0) {
                this.attackTick = this.zombie.getAttackCD();
                if (random.nextInt(2) == 1) {
                    this.attacker.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT);
                } else {
                    this.attacker.swing(InteractionHand.OFF_HAND, SwingAnimation.DEFAULT);
                }
                this.attacker.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT);
                this.zombie.setAttackTime(Edgar090547Entity.this.getCrushCD());
            }
        }
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null) {
            attribute.setBaseValue(angry() ? 0.36000000596046448 : 0.25000000596046448);
        }
    }

    public boolean angry() {
        return this.canNormalUpdate() && this.getHealth() / this.getMaxHealth() < 0.5F;
    }

    @Override
    protected float getModifyAttackDamage(Entity entity, float f) {
        return this.angry() ? EntityUtil.getMaxHealthDamage((LivingEntity) entity) : 15;
    }

    @Override
    public float getLife() {
        return 300.0f;
    }

    public float getArmorToughness() {
        return 15;
    }

    @Override
    public boolean canPAZTarget(Entity target) {
        if (target instanceof SpikeRockEntity) {
            return true;
        }
        return super.canPAZTarget(target);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose poseIn) {
        if (this.isMiniZombie()) {
            return EntityDimensions.scalable(0.3f, 1.0f);
        }
        return EntityDimensions.scalable(0.75f, 2.0f);
    }

    @Override
    protected void dropAllDeathLoot(ServerLevel level, DamageSource damageSource) {
        this.level().explode(this, getX(), getY(), getZ(), 2.5f, Level.ExplosionInteraction.NONE);
        EntityUtil.playSound(this, PvZSoundRegister.IMP_HAPPY.get());
        ImpEntity imp = (ImpEntity) ((EntityType<?>) PvZEntityRegister.IMP.get()).create(this.level(),EntitySpawnReason.MOB_SUMMONED);
        if (imp == null) {
            return;
        }
        EntityUtil.onEntitySpawn(this.level(), imp, this.blockPosition());
        super.dropAllDeathLoot(level, damageSource);
    }

    @Override
    public IZombieType getZombieType() {
        return RoofZombies.EDGAR_090547.get();
    }
}