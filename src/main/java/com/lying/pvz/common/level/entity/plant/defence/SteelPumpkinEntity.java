package com.lying.pvz.common.level.entity.plant.defence;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.plant.IPlantEntity;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.PlantInfo;
import com.lying.pvz.common.level.entity.plant.base.PlantDefenderEntity;
import com.lying.pvz.utils.AlgorithmUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * 钢南瓜（防御型植物）。可让体形更小的植物骑乘其上；骑乘后实体变为非实心可让植物套入，
 * 带金属（超级外层）时回血并提供减伤；外层防御生命由 {@link SteelPumpkinInfo} 统一管理。
 */
public class SteelPumpkinEntity extends PlantDefenderEntity {

	private static final EntityDataAccessor<Boolean> IS_SOLID =
			SynchedEntityData.defineId(SteelPumpkinEntity.class, EntityDataSerializers.BOOLEAN);

	public SteelPumpkinEntity(EntityType<? extends PlantDefenderEntity> type, Level level) {
		super(type, level);
		this.canCollideWithPlant = false;
		this.isImmuneToWeak = true;
	}

	@Override
	public float getLife() {
		return 500F;
	}

	@Override
	public float getSuperLife() {
		return 800F;
	}

	@Override
	protected EntityDimensions getDefaultDimensions(Pose pose) {
		return EntityDimensions.fixed(0.95F, 0.8F);
	}

	@Override
	public void normalPlantTick() {
		if (!this.level().isClientSide()) {
			if (this.hasMetal()) {
				this.decreaseMetal();
				this.heal(275);
				this.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 1000, 5));
			}
			List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
			if (!list.isEmpty()) {
				if (this.random.nextInt(4) == 0) {
					for (Entity entity : list) {
						if (!entity.hasPassenger(this) && entity instanceof PlantEntity) {
							if (this.getPassengers().isEmpty() && !entity.isPassenger()
									&& entity.getBbWidth() < this.getBbWidth() && entity instanceof PlantEntity) {
								entity.startRiding(this);
								this.hasPlant();
								((PlantEntity) entity).addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 2000, 5));
							}
						} else {
							this.setSolid(true);
						}
					}
				}
			}
		}
		super.normalPlantTick();
	}

	@Override
	protected void positionRider(Entity entity, MoveFunction callback) {
		if (this.hasPassenger(entity)) {
			float f = 0.0F;
			if (this.getPassengers().size() > 1) {
				int i = this.getPassengers().indexOf(entity);
				if (i == 0) {
					f = 0.2F;
				} else {
					f = -0.6F;
				}
				if (entity instanceof PlantEntity) {
					f = (float) ((double) f + 0.2);
				}
			}
			Vec3 vec3 = (new Vec3(f, 0.0, 0.0)).yRot(-this.getYRot() * 0.017453292F - 1.5707964F);
			entity.setPos(this.getX() + vec3.x, this.getY(), this.getZ() + vec3.z);
		}
	}

	@Override
	public SoundEvent getHurtSound(DamageSource damageSource) {
		return PvZSoundRegister.METAL_HIT.get();
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setSolid(input.getBooleanOr("is_solid", this.isSolid()));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("is_solid", this.isSolid());
	}

	public void setSolid(boolean has) {
		this.entityData.set(IS_SOLID, has);
	}

	public boolean isSolid() {
		return this.entityData.get(IS_SOLID);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(IS_SOLID, true);
	}

	public void hasPlant() {
		if (!this.level().isClientSide()) {
			PvZSoundRegister.METAL_HIT.get();
		}
		this.setSolid(false);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.STEEL_PUMPKIN.get();
	}

	/** 外层（南瓜/钢南瓜套）信息：负责放置、超级、回血时维护外层防御生命。 */
	public static class SteelPumpkinInfo extends PlantInfo {
		public static final float NORMAL_PUMPKIN_LIFE = 800.0F;
		public static final float EXTRA_PUMPKIN_LIFE = 100.0F;
		public static final float SUPER_PUMPKIN_LIFE = 1200.0F;

		public SteelPumpkinInfo() {
		}

		@Override
		public void onSuper(IPlantEntity plantEntity) {
			super.onSuper(plantEntity);
			if (plantEntity instanceof PlantEntity plant) {
				AlgorithmUtil.setSteelPumpkin(plant, true);
			}
			plantEntity.setOuterDefenceLife(SUPER_PUMPKIN_LIFE);
		}

		@Override
		public void placeOn(IPlantEntity plantEntity, int sunCost) {
			super.placeOn(plantEntity, sunCost);
			if (plantEntity instanceof PlantEntity plant) {
				AlgorithmUtil.setSteelPumpkin(plant, true);
			} else {
				plantEntity.setPumpkin(true);
			}
			plantEntity.setOuterDefenceLife(NORMAL_PUMPKIN_LIFE + EXTRA_PUMPKIN_LIFE);
		}

		@Override
		public void onHeal(IPlantEntity plantEntity, float percent) {
			final float normalMax = NORMAL_PUMPKIN_LIFE + EXTRA_PUMPKIN_LIFE;
			float max = plantEntity.getOuterDefenceLife() > normalMax ? SUPER_PUMPKIN_LIFE : normalMax;
			plantEntity.setOuterDefenceLife(Mth.clamp(plantEntity.getOuterDefenceLife() * (1.0F + percent), 0.0F, max));
		}
	}
}