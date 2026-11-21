package com.lying.pvz.common.level.entity.plant.arma;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantPultEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

/**
 * 爆裂玉米投手。
 *
 * <p>按当前弹种抛射 {@link com.lying.pvz.common.projectile.BulletKinds#KERNEL}（普通玉米）或
 * {@link com.lying.pvz.common.projectile.BulletKinds#CORN}（爆裂/爆米花，溅射）。
 */
public class BurstKernelPultEntity extends PlantPultEntity {

    private static final EntityDataAccessor<Integer> CURRENT_BULLET =
            SynchedEntityData.defineId(BurstKernelPultEntity.class, EntityDataSerializers.INT);

    private BurstKernelPultEntity upgradeEntity;

    public BurstKernelPultEntity(EntityType<? extends PlantPultEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CURRENT_BULLET, CornTypes.POP_CORN.ordinal());
    }

    @Override
    public void onPlantUpgrade(PlantEntity plantEntity) {
        super.onPlantUpgrade(plantEntity);
        if (this.upgradeEntity != null) {
            this.upgradeEntity.isRemoved();
        }
    }

    @Override
    public boolean canBeUpgrade(Player player) {
        this.upgradeEntity = this.getNearByPult(player);
        return super.canBeUpgrade(player) && EntityUtil.isEntityValid(this.upgradeEntity);
    }

    private BurstKernelPultEntity getNearByPult(Player player) {
        float range = 1.5F;
        List<BurstKernelPultEntity> list = this.level().getEntitiesOfClass(BurstKernelPultEntity.class,
                EntityUtil.getEntityAABB(this, range, range),
                pult -> !pult.is(this)
                        && pult.getPlantType() == PVZPlants.BURST_KERNEL_PULT.get()
                        && !EntityUtil.checkCanEntityBeAttack(pult, player));
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void startPultAttack() {
        super.startPultAttack();
        this.changeBullet();
    }

    protected void changeBullet() {
        if (this.isPlantInSuperMode() && !this.isSuperOut) {
            this.setCurrentBullet(CornTypes.POP_CORN);
        } else if (this.getRandom().nextInt(2) == 0) {
            this.setCurrentBullet(CornTypes.POP_CORN);
        } else {
            this.setCurrentBullet(this.getRandom().nextInt(6) == 0
                    ? CornTypes.BURST_CORN : CornTypes.CORN);
        }
    }

    @Override
    public void performPult(LivingEntity target1) {
        super.performPult(target1);
        this.setCurrentBullet(CornTypes.POP_CORN);
    }

    @Override
    protected BulletKind getBulletKind() {
        return this.getCurrentBullet() == CornTypes.CORN ? BulletKinds.KERNEL : BulletKinds.CORN;
    }

    @Override
    protected EntityType<? extends BulletEntity> getBulletType() {
        return this.getBulletKind() == BulletKinds.KERNEL
                ? PvZEntityRegister.KERNEL.get()
                : PvZEntityRegister.CORN.get();
    }

    @Override
    protected void configureShot(ShotSpec spec) {
        spec.baseDamage(this.isPlantInSuperMode() ? this.getSuperDamage() : this.getAttackDamage());
    }

    public float getSuperDamage() {
        return 2.0F * this.getAttackDamage();
    }

    @Override
    public float getAttackDamage() {
        return 5;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return EntityDimensions.scalable(0.7F, 0.8F);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        int idx = input.getIntOr("current_bullet_type", -1);
        if (idx >= 0 && idx < CornTypes.values().length) {
            this.setCurrentBullet(CornTypes.values()[idx]);
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("current_bullet_type", this.getCurrentBullet().ordinal());
    }

    public void setCurrentBullet(CornTypes type) {
        this.entityData.set(CURRENT_BULLET, type.ordinal());
    }

    public CornTypes getCurrentBullet() {
        final CornTypes[] values = CornTypes.values();
        final int ord = this.entityData.get(CURRENT_BULLET);
        return (ord >= 0 && ord < values.length) ? values[ord] : CornTypes.POP_CORN;
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.BURST_KERNEL_PULT.get();
    }

    public enum CornTypes {
        CORN,
        BURST_CORN,
        POP_CORN
    }
}
