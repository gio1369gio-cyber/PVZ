package com.lying.pvz.common.level.entity.plant.fire;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.appease.PeaShooterEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.MathUtil;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 火焰豌豆。
 *
 * <p>发射 {@link com.lying.pvz.common.projectile.BulletKinds#PEA} 的火焰变体
 * （{@link com.lying.pvz.common.projectile.BulletKinds#PEA_FIRE}）。
 */
public class FirePeashooterEntity extends PeaShooterEntity {
    public FirePeashooterEntity(EntityType<? extends PeaShooterEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public void shootBullet() {
        if (this.isPlantInSuperMode()) {
            int cnt = this.getSuperShootCount();
            for (int i = 0; i < cnt; ++i) {
                float offset = MathUtil.getRandomFloat(this.getRandom()) / 3.0F;
                float offsetH = MathUtil.getRandomFloat(this.getRandom()) / 3.0F;
                this.performShoot(0.2, offset, offsetH, this.getExistTick() % 10 == 0, 0.0);
            }
        } else {
            this.performShoot(0.2, 0.0, 0.0, this.getAttackTime() == 1, 0.0);
        }
    }

    @Override
    protected void configureShot(ShotSpec spec) {
        super.configureShot(spec);
        spec.variant(BulletKinds.PEA_FIRE);
    }

    public int getSuperShootCount() {
        return MathUtil.getRandomMinMax(this.getRandom(), 1, 2);
    }

    @Override
    public void startShootAttack() {
        this.setAttackTime(1);
    }

    @Override
    public int getSuperTimeLength() {
        return 100;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose poseIn) {
        return EntityDimensions.scalable(0.7F, 1.3F);
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.FIRE_PEASHOOTER.get();
    }
}
