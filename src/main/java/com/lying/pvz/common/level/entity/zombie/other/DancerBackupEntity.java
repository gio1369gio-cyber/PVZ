package com.lying.pvz.common.level.entity.zombie.other;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.OtherZombies;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.EnumSet;

public class DancerBackupEntity extends ZombieEntity {

    protected MJZombieEntity owner;

    private int restTick = 0;

    public DancerBackupEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
        super(type, worldIn);
        this.canCollideWithZombie = false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new ZombieDanceGoal(this));
    }

    @Override
    public void normalZombieTick() {
        super.normalZombieTick();
        if (!this.level().isClientSide()) {
            this.updateSpeed();
            if (this.needFollow() && EntityUtil.isEntityValid(this.owner)) {
                this.setAttackTime(owner.getAttackTime());
                this.setYRot(owner.getYRot());
                this.setXRot(owner.getXRot());
                this.yHeadRot = owner.getYHeadRot();
            }
        }
    }

    @Override
    public float getLife() {
        return 20.0F;
    }

    public boolean needFollow() {
        return EntityUtil.isEntityValid(this.owner);
    }

    public void setDancingOwner(MJZombieEntity dancer) {
        this.owner = dancer;
    }

    protected void setRestTick() {
        this.restTick = MathUtil.getRandomMinMax(this.random, 60, 300);
    }

    protected void updateSpeed() {
        AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance != null) {
            attributeInstance.setBaseValue(this.getAttackTime() > 0 ? 0.0 : 0.1899999976158142);
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.restTick = input.getIntOr("zombie_rest_tick", this.restTick);

        int ownerId = input.getIntOr("dancing_owner", -1);
        if (ownerId > 0) {
            Entity entity = this.level().getEntity(ownerId);
            if (EntityUtil.isEntityValid(entity) && entity instanceof MJZombieEntity mj) {
                this.owner = mj;
            }
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("zombie_rest_tick", this.restTick);
        if (EntityUtil.isEntityValid(this.owner)) {
            output.putInt("dancing_owner", owner.getId());
        }
    }

    static class ZombieDanceGoal extends Goal {
        private final DancerBackupEntity dancer;

        public ZombieDanceGoal(DancerBackupEntity dancer) {
            this.dancer = dancer;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.dancer.needFollow()) {
                return false;
            } else if (this.dancer.getAttackTime() > 0) {
                return true;
            } else if (this.dancer.restTick > 0) {
                --this.dancer.restTick;
                return false;
            } else if (this.dancer.getRandom().nextFloat() < 0.05F) {
                this.dancer.setAttackTime(100);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            this.dancer.updateSpeed();
        }

        @Override
        public boolean canContinueToUse() {
            return this.dancer.getAttackTime() > 0;
        }

        @Override
        public void tick() {
            int tick = this.dancer.getAttackTime();
            if (tick == 1) {
                this.dancer.setRestTick();
            }

            this.dancer.setAttackTime(Math.max(0, tick - 1));
        }

        @Override
        public void stop() {
            this.dancer.updateSpeed();
        }
    }

    @Override
    public IZombieType getZombieType() {
        return OtherZombies.DANCER_BACKUP_ZOMBIE.get();
    }
}