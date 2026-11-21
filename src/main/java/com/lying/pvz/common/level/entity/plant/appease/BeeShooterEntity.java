package com.lying.pvz.common.level.entity.plant.appease;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.projectile.BulletKind;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.utils.MathUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * 蜜蜂射手。
 *
 * <p>发射追踪制导弹 {@link com.lying.pvz.common.projectile.BulletKinds#BEE}，
 * 复用荆棘运动的制导模式（变体 2 → 制导），命中即消耗。
 */
public class BeeShooterEntity extends PeaShooterEntity {
    public BeeShooterEntity(EntityType<? extends PeaShooterEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public void shootBullet() {
        if (this.isPlantInSuperMode()) {
            int cnt = this.getSuperShootCount();
            for (int i = 0; i < cnt; ++i) {
                float offset = MathUtil.getRandomFloat(this.getRandom()) / 3.0F;
                float offsetH = MathUtil.getRandomFloat(this.getRandom()) / 3.0F;
                this.performShoot(SHOOT_OFFSET, offset, offsetH,
                        this.getExistTick() % 10 == 0, FORWARD_SHOOT_ANGLE);
            }
        } else {
            this.performShoot(SHOOT_OFFSET, 0.0, 0.0,
                    this.getAttackTime() == 1, FORWARD_SHOOT_ANGLE);
        }
    }

    @Override
    protected @Nullable BulletKind getBulletKind() {
        return BulletKinds.BEE;
    }

    @Override
    protected @Nullable EntityType<? extends BulletEntity> getBulletType() {
        return PvZEntityRegister.BEE.get();
    }

    @Override
    protected void configureShot(ShotSpec spec) {
        super.configureShot(spec);
        // 荆棘运动 mode = variant / 2，变体 2 → 制导（追踪）
        spec.variant(2);
    }

    @Override
    public int getShootCD() {
        return 60;
    }

    @Override
    public int getSuperShootCount() {
        return 1;
    }

    @Override
    public int getSuperTimeLength() {
        return 40;
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.BEE_SHOOTER.get();
    }
}
