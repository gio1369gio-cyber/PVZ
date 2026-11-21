package com.lying.pvz.common.level.entity.effect;

import com.lying.pvz.common.level.entity.PVZBaseEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class AbstractEffectEntity extends PVZBaseEntity {

	private static final EntityDataAccessor<Integer> EXIST_TICK =
			SynchedEntityData.defineId(AbstractEffectEntity.class, EntityDataSerializers.INT);

	protected int maxEffectTick;

	public AbstractEffectEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(EXIST_TICK, 0);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			this.setExistTick(this.getExistTick() + 1);
			if (this.getExistTick() > this.maxEffectTick) {
				this.discard();
			}
		}
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		double d0 = this.getBoundingBox().getSize() * 4.0D;
		if (Double.isNaN(d0)) {
			d0 = 4.0D;
		}
		d0 = d0 * 256.0D;
		return distance < d0 * d0;
	}

	// ----------------- 存档迁移 -----------------

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("effect_exist_tick", this.getExistTick());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setExistTick(input.getIntOr("effect_exist_tick", this.getExistTick()));
	}

	public int getExistTick() {
		return this.entityData.get(EXIST_TICK);
	}

	public void setExistTick(int tick) {
		this.entityData.set(EXIST_TICK, tick);
	}
}