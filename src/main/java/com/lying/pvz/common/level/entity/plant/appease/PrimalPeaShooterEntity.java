package com.lying.pvz.common.level.entity.plant.appease;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 原始豌豆（石豌豆）。
 *
 * <p>发射 {@link com.lying.pvz.common.projectile.BulletKinds#STONE_PEA}，
 * 命中减速；大招切换为岩浆变体（火焰伤害并减速）。
 */
public class PrimalPeaShooterEntity extends PeaShooterEntity {

    public PrimalPeaShooterEntity(EntityType<? extends PeaShooterEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected @Nullable BulletKind getBulletKind() {
        return BulletKinds.STONE_PEA;
    }

    @Override
    protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
        return PvZEntityRegister.STONE_PEA.get();
    }

    @Override
    protected void configureShot(ShotSpec spec) {
        super.configureShot(spec);
        spec.variant(this.getStoneVariant());
    }

    @Override
    public void shootBullet() {
        if (this.isPlantInSuperMode()) {
            if (this.tickCount % 10 == 0) {
                this.performShoot(SHOOT_OFFSET, 0.0, -0.15, true, FORWARD_SHOOT_ANGLE);
            }
        } else {
            this.performShoot(SHOOT_OFFSET, 0.0, -0.15,
                    this.getAttackTime() == 1, FORWARD_SHOOT_ANGLE);
        }
    }

    @Override
    public int getSuperTimeLength() {
        return 40;
    }

    @Override
    public void startShootAttack() {
        this.setAttackTime(1);
    }

    /** 变体：0 普通石豌豆，1 火焰（大招）。 */
    protected int getStoneVariant() {
        return this.isPlantInSuperMode() ? 1 : 0;
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.PRIMAL_PEA_SHOOTER.get();
    }
}
