package com.lying.pvz.common.level.entity.effect;

import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class OriginEffectEntity extends AbstractEffectEntity {

	private static final EntityDataAccessor<Float> LENGTH =
			SynchedEntityData.defineId(OriginEffectEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> COLOR =
			SynchedEntityData.defineId(OriginEffectEntity.class, EntityDataSerializers.INT);

	public OriginEffectEntity(EntityType<?> type, Level world) {
		super(type, world);
		this.maxEffectTick = 100;
	}

	public static void create(Level world, BlockPos pos, int color) {
		OriginEffectEntity effectEntity = PvZEntityRegister.ORIGIN_EFFECT.get()
				.create(world, EntitySpawnReason.TRIGGERED);
		if (effectEntity == null) {
			return;
		}
		final boolean exist = EntityUtil.hasNearBy(world, pos, 5, e -> e instanceof OriginEffectEntity);
		if (!exist) { // avoid overlapped.
			EntityUtil.onEntitySpawn(world, effectEntity, pos);
			effectEntity.setColor(color);
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(LENGTH, 1F);
		builder.define(COLOR, 255);
	}

	// ----------------- 存档迁移 -----------------

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setColor(input.getIntOr("effect_color", this.getColor()));
		this.setLength(input.getFloatOr("effect_length", this.getLength()));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("effect_color", this.getColor());
		output.putFloat("effect_length", this.getLength());
	}

	public void setColor(int color) {
		this.entityData.set(COLOR, color);
	}

	public int getColor() {
		return this.entityData.get(COLOR);
	}

	public void setLength(float l) {
		this.entityData.set(LENGTH, l);
	}

	public float getLength() {
		return this.entityData.get(LENGTH);
	}
}