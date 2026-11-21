package com.lying.pvz.common.level.entity.plant.assist;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;

/**
 * 草鱼（水生植物）。
 *
 * <p>无弹药；周期性治疗周围友方植物，多只相邻时会互相吞噬。
 */
public class GrassCarpEntity extends PlantEntity {
    private static final int EFFECT_CD = 200;

    public GrassCarpEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new FloatGoal(this));
    }

    @Override
    public void normalPlantTick() {
        super.normalPlantTick();

        if (this.level() instanceof ServerLevel && this.getExistTick() % EFFECT_CD == 0) {
            this.giveHealToFriendly();
        }
    }

    private void giveHealToFriendly() {
        final float range = this.getEffectRange();
        EntityUtil.getFriendlyLivings(this, EntityUtil.getEntityAABB(this, range, range)).forEach(entity -> {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(this.getAttackDamage());
            }
            if (entity instanceof GrassCarpEntity && entity != this) {
                if (this.level()instanceof ServerLevel serverLevel) {
                    if (serverLevel != null) {
                        entity.kill(serverLevel);
                    }
                }
            }
        });
    }

    public float getAttackDamage() {
        return this.getSkillValue(SkillTypes.GRASS_CARP_DAMAGE.get());
    }

    public float getEffectRange() {
        return this.getSkillValue(SkillTypes.GRASS_CARP_HEAL_RANGE.get());
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return EntityDimensions.scalable(0.8F, 0.8F);
    }

    @Override
    public IPlantType getPlantType() {
        return PVZPlants.GRASS_CARP.get();
    }
}