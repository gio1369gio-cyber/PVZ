package com.lying.pvz.common.level.entity.plant.defence;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * 爆炸坚果（保龄球专用植物类型）。继承 {@link WallNutEntity} 提供坚果形态，
 * 爆炸逻辑由 {@code ExplosionBowlingEntity} 承载，这里只替换植物类型 ID。
 */
public class ExplodeONutEntity extends WallNutEntity {

    /** 生命值低于该值时触发爆炸。 */
    private static final float EXPLODE_THRESHOLD = 30F;
    /** 爆炸范围（格）。 */
    private static final float EXPLODE_RANGE = 2.5F;
    /** 爆炸伤害。 */
    private static final float EXPLODE_DAMAGE = 180F;

    public ExplodeONutEntity(EntityType<? extends WallNutEntity> type, Level worldIn) {
        super(type, worldIn);
    }
    @Override
    protected void normalPlantTick() {
        super.normalPlantTick();
        if (!this.level().isClientSide() && this.getHealth() <= EXPLODE_THRESHOLD) {
            this.explode();
        }
    }

    /** 爆炸：对周围实体造成伤害并生成粒子，不破坏方块。 */
    private void explode() {
        final AABB aabb = EntityUtil.getEntityAABB(this, EXPLODE_RANGE, EXPLODE_RANGE);
        for (Entity target : EntityUtil.getWholeTargetableEntitiesIgnoreFog(this, aabb)) {
            target.hurt(PvZEntityDamageSource.explode(this), EXPLODE_DAMAGE);
        }
        EntityUtil.playSound(this, PvZSoundRegister.CHERRY_BOMB.get());
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(PvZParticleRegister.RED_BOMB.get(),
                    this.getX(), this.getY(), this.getZ(), 5, 0.3D, 0.3D, 0.3D, 0D);
        }
        this.discard();
    }
    @Override
    public IPlantType getPlantType() {
        return PVZPlants.EXPLODE_O_NUT.get();
    }
}
