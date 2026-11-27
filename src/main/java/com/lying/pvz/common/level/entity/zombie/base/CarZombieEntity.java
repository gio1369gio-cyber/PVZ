package com.lying.pvz.common.level.entity.zombie.base;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class CarZombieEntity extends PVZZombieEntity {

	public CarZombieEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setIsWholeBody();
		this.canBeFrozen = false;
		this.canBeMini = false;
	}

	@Override
	public void zombieTick() {
		super.zombieTick();
		if (this.level().isClientSide() && this.isCarShaking()) {
			for (int i = 1; i <= 3; i++) {
				this.level().addParticle(ParticleTypes.SMOKE,
						this.getX(), this.getY(), this.getZ(),
						(this.getRandom().nextFloat() - 0.5) / 10, 0.05,
						(this.getRandom().nextFloat() - 0.5) / 10);
			}
		}
	}

	@Override
	protected void onRemoveWhenDeath() {
		if (!this.level().isClientSide()) {
			EntityUtil.playSound(this, PvZSoundRegister.CAR_EXPLOSION.get());
		} else {
			for (int i = 0; i < 4; ++i) {
				this.level().addParticle(ParticleTypes.EXPLOSION,
						this.getX(), this.getY(), this.getZ(), 0, 0, 0);
			}
		}
		super.onRemoveWhenDeath();
	}

	@Override
	public float getKBValue() {
		return 1;
	}

	public boolean isCarShaking() {
		return this.getHealth() <= this.getMaxHealth() / 4;
	}

	@Override
	public boolean canBreakPlantBlock() {
		return false;
	}

	@Override
	protected PvZEntityDamageSource getZombieAttackDamageSource() {
		return PvZEntityDamageSource.causeCrushDamage(this);
	}

	@Override
	protected float getModifyAttackDamage(Entity entity, float f) {
		if (entity instanceof LivingEntity living) {
			return EntityUtil.getMaxHealthDamage(living, 1.0F);
		}
		return f;
	}

	@Override
	protected int getDeathTime() {
		return 2;
	}
}