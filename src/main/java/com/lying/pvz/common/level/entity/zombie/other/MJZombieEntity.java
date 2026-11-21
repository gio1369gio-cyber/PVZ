package com.lying.pvz.common.level.entity.zombie.other;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.OtherZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.WorldUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class MJZombieEntity extends ZombieEntity {
    private static final EntityDataAccessor<Integer> SUMMON_TIME;

    private static final float[][] POS_OFFSET;

    private final List<Optional<DancerBackupEntity>> Dancers = new ArrayList<>(4);
    private int summonCnt = 0;
    private int restTick = 0;

    public MJZombieEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
        super(type, worldIn);
        this.canCollideWithZombie = false;
        this.setRestTick();

        for (int i = 0; i < 4; ++i) {
            this.Dancers.add(Optional.empty());
        }

        this.clearDancers();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SUMMON_TIME, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new ZombieDanceGoal(this));
        this.goalSelector.addGoal(0, new SummonDancerGoal(this));
    }

    public void tickDance() {
        for (int i = 0; i < 4; ++i) {
            this.Dancers.get(i).ifPresent((dancer) -> {
            });
        }
    }

    public void summonEmptyDancers() {
        for (int i = 0; i < 4; ++i) {
            if (this.Dancers.get(i).isEmpty() && this.summonCnt < this.getMaxSummonCnt()) {
                DancerBackupEntity dancer =  PvZEntityRegister.DANCER_BACKUP_ZOMBIE.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
                BlockPos pos = WorldUtil.getSuitableHeightPos(this.level(), this.blockPosition().offset((int) POS_OFFSET[i][0], (int) 0.0, (int) POS_OFFSET[i][1]));
                if (dancer != null) {
                    dancer.setDancingOwner(this);
                    dancer.setZombieRising();

                    ZombieUtil.copySummonZombieData(this, dancer);

                    this.setDancer(i, dancer);
                    ++this.summonCnt;
                    EntityUtil.onEntitySpawn(this.level(), dancer, pos);
                }
                this.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 40, 5, false, false));
            }
        }
    }

    protected void setRestTick() {
        this.restTick = MathUtil.getRandomMinMax(this.random, 60, 300);
    }

    protected void updateSpeed() {
        AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute != null) {
            attribute.setBaseValue(this.getAttackTime() > 0 ? 0.0 : (double) this.getWalkSpeed());
        }
    }

    public float getWalkSpeed() {
        return 0.19F;
    }

    protected boolean hasEmptyPlace() {
        for (int i = 0; i < 4; ++i) {
            if (this.Dancers.get(i).isEmpty()) {
                return true;
            }

            if (!EntityUtil.isEntityValid(this.Dancers.get(i).orElse(null))) {
                this.Dancers.set(i, Optional.empty());
                return true;
            }
        }

        return false;
    }

    private void clearDancers() {
        for (int i = 0; i < 4; ++i) {
            this.Dancers.set(i, Optional.empty());
        }
    }

    private void setDancer(int pos, Entity entity) {
        if (EntityUtil.isEntityValid(entity) && entity instanceof DancerBackupEntity dancer) {
            this.Dancers.set(pos, Optional.of(dancer));
        }
    }

    public int getMaxSummonCnt() {
        return 25;
    }

    @Override
    public float getLife() {
        return 50.0F;
    }

    public float getArmorToughness() {
        return 15;
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setSummonTime(input.getIntOr("zombie_summon_tick", this.getSummonTime()));
        this.restTick = input.getIntOr("zombie_rest_tick", this.restTick);

        final ValueInput ids = input.childOrEmpty("dancer_ids");
        for (int i = 0; i < 4; ++i) {
            final int id = ids.getIntOr("dancer_" + i, -1);
            if (id >= 0) {
                final Entity entity = this.level().getEntity(id);
                this.setDancer(i, entity);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("zombie_summon_tick", this.getSummonTime());
        output.putInt("zombie_rest_tick", this.restTick);
        final ValueOutput ids = output.child("dancer_ids");
        for (int i = 0; i < 4; ++i) {
            final int index = i;
            this.Dancers.get(i).ifPresent(dancer -> {
                if (EntityUtil.isEntityValid(dancer)) {
                    ids.putInt("dancer_" + index, dancer.getId());
                }
            });
        }
    }


    public int getSummonTime() {
        return this.entityData.get(SUMMON_TIME);
    }

    public void setSummonTime(int time) {
        this.entityData.set(SUMMON_TIME, time);
    }

    static {
        SUMMON_TIME = SynchedEntityData.defineId(MJZombieEntity.class, EntityDataSerializers.INT);
        POS_OFFSET = new float[][]{{2.0F, 0.0F}, {-2.0F, 0.0F}, {0.0F, 2.0F}, {0.0F, -2.0F}};
    }

    static class SummonDancerGoal extends Goal {
        private final MJZombieEntity dancer;

        public SummonDancerGoal(MJZombieEntity dancer) {
            this.dancer = dancer;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.dancer.summonCnt >= this.dancer.getMaxSummonCnt()) {
                return false;
            } else if (this.dancer.getSummonTime() > 0) {
                return true;
            } else if (this.dancer.restTick > 0 && this.dancer.hasEmptyPlace() && this.dancer.getRandom().nextFloat() < 0.05F) {
                this.dancer.setSummonTime(10);
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
            return this.dancer.getSummonTime() > 0;
        }

        @Override
        public void tick() {
            int tick = this.dancer.getSummonTime();
            if (tick == 5) {
                this.dancer.summonEmptyDancers();
            }

            this.dancer.setSummonTime(Math.max(0, tick - 1));
        }

        @Override
        public void stop() {
            this.dancer.updateSpeed();
        }
    }

    static class ZombieDanceGoal extends Goal {
        private final MJZombieEntity dancer;

        public ZombieDanceGoal(MJZombieEntity dancer) {
            this.dancer = dancer;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.dancer.getAttackTime() > 0) {
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

            this.dancer.tickDance();
            this.dancer.setAttackTime(Math.max(0, tick - 1));
        }

        @Override
        public void stop() {
            this.dancer.updateSpeed();
        }
    }

    @Override
    public IZombieType getZombieType() {
        return OtherZombies.MJ_ZOMBIE.get();
    }
}