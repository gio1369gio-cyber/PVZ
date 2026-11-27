package com.lying.pvz.common.level.entity.zombie.roof;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class AirborneZombieEntity extends PVZZombieEntity {
    private static final EntityDataAccessor<Boolean> HAS_LINE = SynchedEntityData.defineId(AirborneZombieEntity.class, EntityDataSerializers.BOOLEAN);
    private final MoveControl<? extends Mob> FlyController = new FlyingMoveControl<>(this, 360, true);
    private final MoveControl<? extends Mob> GroundController = new MoveControl<>(this);
    private PathNavigation FlyNavigator;
    private PathNavigation GroundNavigator;

    public AirborneZombieEntity(EntityType<? extends PVZZombieEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_LINE, true);
    }

    @Override
    protected void registerGoals() {
        this.FlyNavigator = new FlyingPathNavigation(this, level());
        this.GroundNavigator = new GroundPathNavigation(this, level());
        super.registerGoals();
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        AttributeInstance flySpeed = this.getAttribute(Attributes.FLYING_SPEED);
        if (flySpeed != null) {
            flySpeed.setBaseValue(ZombieUtil.FLY_FAST);
        }

        this.setNoGravity(this.hasLine());
        this.moveControl = this.hasLine() ? FlyController : GroundController;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataParameter) {
        super.onSyncedDataUpdated(dataParameter);
        if (dataParameter.equals(HAS_LINE)) {
            this.setNoGravity(this.hasLine());
            this.moveControl = this.hasLine() ? FlyController : GroundController;
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.hasLine() && this.canHitLine(source)) {
            this.onLineExplode();
            amount = 0;
        }
        return super.hurtServer(level, source, amount);
    }

    private boolean canHitLine(DamageSource source) {
        if (source instanceof PvZEntityDamageSource pvzSource) {
            return pvzSource.isIceDamage();
        }
        return false;
    }

    public void onLineExplode() {
        if (!level().isClientSide()) {
            EntityUtil.playSound(this, SoundEvents.WOOL_FALL);
        }
        this.setLine(false);
    }

    @Override
    public boolean isClimbing() {
        return super.isClimbing() && !this.hasLine();
    }


    @Override
    public float getLife() {
        return 30;
    }

    @Override
    public float getWalkSpeed() {
        return ZombieUtil.WALK_LITTLE_SLOW;
    }

    @Override
    public boolean canBeButtered() {
        return !this.hasLine();
    }

    @Override
    public PathNavigation getNavigation() {
        if (this.hasLine()) {
            if (!(this.navigation instanceof FlyingPathNavigation)) {
                this.navigation = this.FlyNavigator;
            }
        } else {
            if (!(this.navigation instanceof GroundPathNavigation)) {
                this.navigation = this.GroundNavigator;
            }
        }
        return super.getNavigation();
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setLine(input.getBooleanOr("has_line", this.hasLine()));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("has_line", this.hasLine());
    }

    public void setLine(boolean has) {
        this.entityData.set(HAS_LINE, has);
    }

    public boolean hasLine() {
        return this.entityData.get(HAS_LINE);
    }

    @Override
    public IZombieType getZombieType() {
        return RoofZombies.AIRBORNE_ZOMBIE.get();
    }
}
