package com.lying.pvz.common.level.entity.plant.arma;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.base.PlantPultEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 紫颂果投手。
 *
 * <p>抛射 {@link com.lying.pvz.common.projectile.BulletKinds#CHORUS_FRUIT}（溅射，半额）。
 */
public class ChorusFruitPultEntity extends PlantPultEntity {

    private static final EntityDataAccessor<Integer> CURRENT_BULLET =
            SynchedEntityData.defineId(ChorusFruitPultEntity.class, EntityDataSerializers.INT);

    public ChorusFruitPultEntity(EntityType<? extends PlantPultEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CURRENT_BULLET, ChorusFruitTypes.POPPED.ordinal());
    }

    @Override
    public int getPultCD() {
        return 100;
    }

    @Override
    public void startPultAttack() {
        super.startPultAttack();
        this.changeBullet();
    }

    protected void changeBullet() {
        if (this.isPlantInSuperMode() && !this.isSuperOut) {
            this.setCurrentBullet(ChorusFruitTypes.POPPED);
        } else {
            this.setCurrentBullet(this.getRandom().nextInt(2) == 0
                    ? ChorusFruitTypes.NORMAL : ChorusFruitTypes.POPPED);
        }
    }

    @Override
    public void performPult(LivingEntity target1) {
        super.performPult(target1);
        this.setCurrentBullet(ChorusFruitTypes.POPPED);
    }

    @Override
    protected BulletKind getBulletKind() {
        return BulletKinds.CHORUS_FRUIT;
    }

    @Override
    protected EntityType<? extends BulletEntity> getBulletType() {
        return PvZEntityRegister.CHORUS_FRUIT.get();
    }

    @Override
    protected void configureShot(ShotSpec spec) {
        spec.baseDamage(this.isPlantInSuperMode() ? this.getSuperDamage() : this.getAttackDamage());
    }

    @Override
    public float getSuperDamage() {
        return 2.0F * this.getAttackDamage();
    }

    @Override
    public float getAttackDamage() {
        return this.getSkillValue(SkillTypes.MORE_CHORUS_FRUIT_DAMAGE.get());
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return EntityDimensions.scalable(0.75F, 0.8F);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        int idx = input.getIntOr("current_bullet_type", -1);
        if (idx >= 0 && idx < ChorusFruitTypes.values().length) {
            this.setCurrentBullet(ChorusFruitTypes.values()[idx]);
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("current_bullet_type", this.getCurrentBullet().ordinal());
    }

    public void setCurrentBullet(ChorusFruitTypes type) {
        this.entityData.set(CURRENT_BULLET, type.ordinal());
    }

    public ChorusFruitTypes getCurrentBullet() {
        final ChorusFruitTypes[] values = ChorusFruitTypes.values();
        final int ord = this.entityData.get(CURRENT_BULLET);
        return (ord >= 0 && ord < values.length) ? values[ord] : ChorusFruitTypes.POPPED;
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.CHORUS_FRUIT_PULT.get();
    }

    public enum ChorusFruitTypes {
        NORMAL,
        POPPED
    }
}