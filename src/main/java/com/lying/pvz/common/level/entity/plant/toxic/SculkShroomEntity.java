package com.lying.pvz.common.level.entity.plant.toxic;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 音爆菇。
 *
 * <p>继承 {@link FumeShroomEntity} 的穿透弹 {@link com.lying.pvz.common.projectile.BulletKinds#FUME}，
 * 以自身为中心向 8 个方向发射音波。
 */
public class SculkShroomEntity extends FumeShroomEntity {

    public SculkShroomEntity(EntityType<? extends FumeShroomEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public void shootBullet() {
        float now = this.getYHeadRot();
        for (int i = 0; i < 8; ++i) {
            this.shootByAngle(now, 0.25F);
            now += 45.0F;
        }
        EntityUtil.playSound(this, SoundEvents.GENERIC_EXPLODE.value());
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
    public int getShootCD() {
        return 60;
    }

    @Override
    public float getAttackDamage() {
        return this.getSkillValue(SkillTypes.SPORE_DAMAGE.get()) * 1.5F;
    }

    @Override
    public float getShootRange() {
        return 24.0F;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose poseIn) {
        return EntityDimensions.scalable(0.9F, 0.75F);
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.SCULK_SHROOM.get();
    }
}
