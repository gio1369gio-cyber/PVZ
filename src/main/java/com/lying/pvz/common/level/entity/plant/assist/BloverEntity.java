package com.lying.pvz.common.level.entity.plant.assist;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.pool.BalloonZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 三叶草（Blover）。
 *
 * <p>存在 tick=5 时执行 {@link #blow()}：命中天空中实体并施加水平吹飞。
 * 生命周期上限 60 tick，超时 {@code discard}。
 */
public class BloverEntity extends PlantEntity {

	public BloverEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (!this.level().isClientSide()) {
			if (this.getExistTick() == 5) {
				this.blow();
			} else if (this.getExistTick() > 60) {
				this.discard();
			}
		}
	}

	public void blow() {
		if (this.level().isClientSide()) {
			return;
		}
		final float len = this.getBlowRange();
		EntityUtil.getWholeTargetableEntities(this, EntityUtil.getEntityAABB(this, len, len))
				.forEach(target -> {
					if (!EntityUtil.isEntityInSky(target)) {
						return;
					}
					target.hurt(PvZEntityDamageSource.normal(this).setMustHurt(),
							this.getAttackDamage());
					final Vec3 speed = target.getDeltaMovement();
					final double lvl = this.getForceLevel() * 2.5F;
					final Vec3 delta = MathUtil.getHorizontalNormalizedVec(
							this.position(), target.position()).scale(lvl);
					target.setDeltaMovement(speed.x + delta.x, speed.y, speed.z + delta.z);
				});
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage()),
				Pair.of(PAZAlmanacs.ATTACK_RANGE, this.getBlowRange())
		));
	}

	@Override
	public boolean canPAZTarget(Entity entity) {
		if (entity instanceof BalloonZombieEntity) {
			return true;
		}
		return super.canPAZTarget(entity);
	}

	@Override
	public Optional<SoundEvent> getSpawnSound() {
		return Optional.ofNullable(PvZSoundRegister.BLOVER.get());
	}

	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.BLOW_STRENGTH.get());
	}

	public int getForceLevel() {
		return 2;
	}

	public float getBlowRange() {
		return 30;
	}

	public int getReadyTime() {
		return 40;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.5F, 1.5F);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.BLOVER.get();
	}
}