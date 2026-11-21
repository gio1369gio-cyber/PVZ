package com.lying.pvz.common.level.entity.misc;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.AbstractOwnerEntity;
import com.lying.pvz.common.level.entity.plant.explosion.BambooLordEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 鞭炮：倒计时引线燃尽，对范围内目标造成固定伤害并把它们向上弹飞。
 */
public class FireCrackersEntity extends AbstractOwnerEntity {

	private static final EntityDataAccessor<Integer> FUSE =
			SynchedEntityData.defineId(FireCrackersEntity.class, EntityDataSerializers.INT);

	public FireCrackersEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
	}

	public FireCrackersEntity(Level worldIn, LivingEntity living) {
		super(PvZEntityRegister.FIRE_CRACKERS.get(), worldIn, living);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(FUSE, 80);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickMove();
		if (!this.level().isClientSide()) {
			if (this.getFuse() > 0) {
				this.setFuse(this.getFuse() - 1);
				if (this.getFuse() <= 0) {
					this.explode();
					this.discard();
				}
			}
		}
	}

	protected void explode() {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		final float range = 2F;
		EntityUtil.playSound(this, SoundEvents.GENERIC_EXPLODE.value());
		EntityUtil.getTargetableEntities(this, EntityUtil.getEntityAABB(this, range, range)).forEach(target -> {
			target.hurtServer(serverLevel,
					PvZEntityDamageSource.explode(this, this.getOwner()), 50F);
			target.setDeltaMovement(target.getDeltaMovement().add(0, BambooLordEntity.UP_SPEED, 0));
		});
		for (int i = 0; i < 2; ++i) {
			EntityUtil.spawnParticle(this, 5);
		}
	}

	protected boolean isMovementNoisy() {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		input.getInt("fuse_tick").ifPresent(this::setFuse);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("fuse_tick", this.getFuse());
	}

	public void setFuse(int tick) {
		this.entityData.set(FUSE, tick);
	}

	public int getFuse() {
		return this.entityData.get(FUSE);
	}
}