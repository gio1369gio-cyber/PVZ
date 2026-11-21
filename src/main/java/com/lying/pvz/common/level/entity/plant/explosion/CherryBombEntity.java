package com.lying.pvz.common.level.entity.plant.explosion;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.init.PvZTriggers;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantBomberEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * 樱桃炸弹。爆炸范围 4.5 格、伤害 {@link SkillTypes#NORMAL_BOMB_DAMAGE}，
 * 顺带清除范围内梯子，并触发 {@code ENTITY_EFFECT_AMOUNT} 成就计数。
 */
public class CherryBombEntity extends PlantBomberEntity {

	public CherryBombEntity(EntityType<? extends PlantBomberEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void startBomb(boolean server) {
		if (server) {
			int deathCnt = 0;
			final float range = this.getExplodeRange();
			final AABB aabb = EntityUtil.getEntityAABB(this, range, range);
			for (Entity target : EntityUtil.getWholeTargetableEntitiesIgnoreFog(this, aabb)) {
				target.hurt(PvZEntityDamageSource.explode(this), this.getExplodeDamage());
				if (!EntityUtil.isEntityValid(target)) {
					++deathCnt;
				}
			}
			PlantEntity.clearLadders(this, aabb);
			EntityUtil.playSound(this, PvZSoundRegister.CHERRY_BOMB.get());
			final Player owner = EntityUtil.getEntityOwner(this.level(), this);
			if (owner instanceof ServerPlayer sp) {
				PvZTriggers.ENTITY_EFFECT_AMOUNT.get().trigger(sp, this, deathCnt);
			}
		} else {
			for (int i = 0; i < 5; ++i) {
				this.level().addParticle(PvZParticleRegister.RED_BOMB.get(),
						this.getX(), this.getY(), this.getZ(), 0, 0, 0);
			}
		}
	}

	@Override
	public float getExplodeDamage() {
		return this.getSkillValue(SkillTypes.NORMAL_BOMB_DAMAGE.get());
	}

	@Override
	public float getExplodeRange() {
		return 4.5F;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.fixed(0.9F, 1F);
	}

	@Override
	public int getReadyTime() {
		return 30;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.CHERRY_BOMB.get();
	}
}