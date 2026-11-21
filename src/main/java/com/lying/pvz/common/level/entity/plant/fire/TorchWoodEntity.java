package com.lying.pvz.common.level.entity.plant.fire;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.projectile.BulletTransformer;
import com.lying.pvz.api.projectile.IBullet;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.projectile.BulletTransformers;
import com.lying.pvz.common.projectile.BulletTriggers;
import com.lying.pvz.common.projectile.BulletVisuals;
import com.lying.pvz.utils.WorldUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

/**
 * 火炬树桩。
 *
 * <p>注册为 {@link BulletTransformer}：黄火把发射 {@link BulletTriggers#HEAT}，
 * 蓝火把发射 {@link BulletTriggers#BLUE_HEAT}。豌豆 Kind 的
 * {@code VariantTable} 决定变成 FIRE / BLUE_FIRE，并同步切换材质与伤害倍率。
 *
 * <p>触发方式从"每 tick 半径扫描"改为"子弹本 tick 位移段与火炬树 AABB 相交"，
 * 每个 {@code (transformer, provider)} 每发子弹只触发一次。
 */
public class TorchWoodEntity extends PlantEntity {

	private static final EntityDataAccessor<Integer> FLAME_TYPE =
			SynchedEntityData.defineId(TorchWoodEntity.class, EntityDataSerializers.INT);

	static {
		BulletTransformers.register(
				provider -> provider instanceof TorchWoodEntity,
				new HeatTransformer());
	}

	public TorchWoodEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(FLAME_TYPE, FlameTypes.YELLOW.ordinal());
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (!this.level().isClientSide()) {
			return;
		}
		ParticleOptions particle = PvZParticleRegister.YELLOW_FLAME.get();
		if (this.getFlameType() == FlameTypes.BLUE) {
			particle = PvZParticleRegister.BLUE_FLAME.get();
		}
		WorldUtil.spawnRandomSpeedParticle(this.level(), particle,
				this.position().add(0, 1.2F, 0), 0.1F);
	}

	/**
	 * 黄火 → {@link BulletTriggers#HEAT}，蓝火 → {@link BulletTriggers#BLUE_HEAT}。
	 * 只对声明了 {@link BulletVisuals#HEATABLE} 的 Kind 生效。
	 */
	private static final class HeatTransformer implements BulletTransformer {

		private static final Identifier ID = StringUtil.id("torchwood_heat");

		@Override
		public Identifier id() {
			return ID;
		}

		@Override
		public boolean applies(IBullet bullet, Entity provider) {
			return provider instanceof TorchWoodEntity
					&& bullet.kind().transformerTags().contains(BulletVisuals.HEATABLE);
		}

		@Override
		public Result transform(IBullet bullet, Entity provider) {
			final boolean blue = ((TorchWoodEntity) provider).getFlameType() == FlameTypes.BLUE;
			final Identifier trigger = blue ? BulletTriggers.BLUE_HEAT : BulletTriggers.HEAT;
			final int next = bullet.kind().nextVariant(bullet.variant(), trigger);
			return next != bullet.variant()
					? new Result(next, 0.0F, 1.0F)
					: Result.none();
		}
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		this.setFlameType(FlameTypes.BLUE);
	}

	@Override
	public boolean canStartSuperMode() {
		return super.canStartSuperMode() && this.getFlameType() == FlameTypes.YELLOW;
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.add(Pair.of(PAZAlmanacs.HEAT_PEA_RANGE, this.getHeatRange()));
	}

	public float getHeatRange() {
		return this.getSkillValue(SkillTypes.HEAT_PEA_RANGE.get());
	}

	@Override
	protected float getLife() {
		return this.getSkillValue(SkillTypes.WOOD_MORE_LIFE.get());
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.fixed(0.95F, 1.5F);
	}

	@Override
	public int getSuperTimeLength() {
		return 0;
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		final FlameTypes[] values = FlameTypes.values();
		final int ord = input.getIntOr("flame_type", FlameTypes.YELLOW.ordinal());
		this.setFlameType((ord >= 0 && ord < values.length) ? values[ord] : FlameTypes.YELLOW);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("flame_type", this.getFlameType().ordinal());
	}

	public FlameTypes getFlameType() {
		final FlameTypes[] values = FlameTypes.values();
		final int ord = this.entityData.get(FLAME_TYPE);
		return (ord >= 0 && ord < values.length) ? values[ord] : FlameTypes.YELLOW;
	}

	public void setFlameType(FlameTypes type) {
		this.entityData.set(FLAME_TYPE, type.ordinal());
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.TORCH_WOOD.get();
	}

	public enum FlameTypes {
		YELLOW,
		BLUE,
		PURPLE
	}
}