package com.lying.pvz.common.level.entity.drop;

import com.lying.pvz.api.entity.ability.ICollectible;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class DropEntity extends Mob implements ICollectible {

    private static final EntityDataAccessor<Integer> AMOUNT =
            SynchedEntityData.defineId(DropEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STATE  =
            SynchedEntityData.defineId(DropEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIVE_TIME =
            SynchedEntityData.defineId(DropEntity.class, EntityDataSerializers.INT);
    protected int liveTime = 0;

    public DropEntity(EntityType<? extends Mob> type, Level worldIn) {
        super(type, worldIn);
        this.setPermanentlyInvulnerable(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AMOUNT, 1);
        builder.define(STATE, DropStates.NORMAL.ordinal());
        builder.define(LIVE_TIME, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new FloatGoal(this));
    }


    @Override
    public @org.jspecify.annotations.Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @org.jspecify.annotations.Nullable SpawnGroupData groupData) {
       this.onDropped();
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }


    @Override
    public void tick() {
        super.tick();
        this.noPhysics = this.getDropState() != DropStates.NORMAL;

        if (!level().isClientSide()) {
            if (this.getDropState() == DropStates.NORMAL) {
                ++ this.liveTime;
                this.entityData.set(LIVE_TIME, this.liveTime);
            }
            if (this.liveTime >= this.getMaxLiveTick()) {
                this.discard();
            }
        }
    }

    @Override
    public void playerTouch(Player entityIn) {
        if (this.canCollectBy(entityIn)) {
            this.onCollect(entityIn);
        }
    }

    @Override
    public boolean canCollectBy(LivingEntity living) {
        return living instanceof Player && EntityUtil.isEntityValid(this)
                && this.getDropState() != DropStates.STEAL;
    }

    @Override
    public void onCollect(LivingEntity living) {
        if (living instanceof Player player) {
            this.onCollectedByPlayer(player);
        }
        this.discard();
    }

    /** run when collect the drop. */
    public abstract void onCollectedByPlayer(Player player);

    /** drop live tick,read from config file */
    protected abstract int getMaxLiveTick();

    /** called when first join to world. */
    protected void onDropped() {
    }

    @Override
    protected void doPush(Entity entityIn) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (AMOUNT.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void playBlockFallSound() {
    }



    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.liveTime = input.getIntOr("live_time", 0);
        this.entityData.set(LIVE_TIME, this.liveTime);
        this.setAmount(input.getIntOr("drop_amount", 1));
        int rawState = input.getIntOr("drop_state", DropStates.NORMAL.ordinal());
        DropStates[] states = DropStates.values();
        if (rawState >= 0 && rawState < states.length) {
            this.setDropState(states[rawState]);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("live_time", this.liveTime);
        output.putInt("drop_amount", this.getAmount());
        output.putInt("drop_state", this.getDropState().ordinal());
    }


    public int getAmount() {
        return this.entityData.get(AMOUNT);
    }

    public void setAmount(int num) {
        int clamped = Math.max(1, num);
        this.entityData.set(AMOUNT, clamped);
        if (!this.level().isClientSide()) {
            this.refreshDimensions();
        }
    }

    public DropStates getDropState() {
        return DropStates.values()[this.entityData.get(STATE)];
    }

    public void setDropState(DropStates state) {
        this.entityData.set(STATE, state.ordinal());
    }

    /** synced live tick, safe to read on both sides. */
    public int getLiveTime() {
        return this.entityData.get(LIVE_TIME);
    }

    public enum DropStates {
        NORMAL,
        ABSORB,
        STEAL
    }
}