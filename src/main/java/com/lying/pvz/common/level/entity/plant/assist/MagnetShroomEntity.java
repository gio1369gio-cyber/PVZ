package com.lying.pvz.common.level.entity.plant.assist;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.entity.ability.IHasMetal;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZNearestTargetGoal;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZTargetGoal;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.common.projectile.BulletLauncher;
import com.lying.pvz.utils.AlgorithmUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 磁力菇（Magnet Shroom）。
 *
 * <p>普通模式：吸附单个 {@link IHasMetal} 目标的金属，生成
 * {@link com.lying.pvz.common.projectile.BulletKinds#METAL}（ABSORB 状态）并设置工作 CD。
 * super 模式：一次性吸附范围内最多 {@link #getSuperDragCnt()} 个目标，
 * 生成的金属弹直接以 BULLET 状态出现。
 *
 * <p>新架构 {@link MetalTypes} 仅含 {@code EMPTY/LADDER/SCREEN_DOOR}，
 * 无 {@code getMetalItem}；{@link #getMetalRenderItem()} 改为从 {@link PvZItemRegister}
 * 按金属类型映射。若新架构未注册对应物品，返回 {@code ItemStack.EMPTY}。
 */
public class MagnetShroomEntity extends PlantEntity {

	private static final EntityDataAccessor<Integer> METAL_TYPE =
			SynchedEntityData.defineId(MagnetShroomEntity.class, EntityDataSerializers.INT);

	public MagnetShroomEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(METAL_TYPE, MetalTypes.EMPTY.ordinal());
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new PVZNearestTargetGoal(
				this, true, false, this.getAbsorbRange(), this.getAbsorbRange()));
		this.targetSelector.addGoal(1, new TargetLadderGoal(
				this, true, false, this.getAbsorbRange()));
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		if (this.getAttackTime() > 0) {
			this.setAttackTime(this.getAttackTime() - 1);
		} else if (this.getAttackTime() == 0) {
			this.setMetalType(MetalTypes.EMPTY);
		}
		if (this.isPlantSleeping()) {
			this.setTarget(null);
			return;
		}
		final LivingEntity target = this.getTarget();
		if (EntityUtil.isEntityValid(target) && this.canPAZTarget(target) && this.isMagnetActive()) {
			this.dragMetal(target);
		}
	}

	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		final float range = this.getAbsorbRange();
		int cnt = this.getSuperDragCnt();
		EntityUtil.playSound(this, PvZSoundRegister.MAGNET.get());
		for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class,
				EntityUtil.getEntityAABB(this, range, range), this::canPAZTarget)) {
			if (!(target instanceof IHasMetal metalTarget)) {
				continue;
			}
			final MetalTypes type = metalTarget.getMetalType();
			metalTarget.decreaseMetal();

			if (!(this.level() instanceof ServerLevel serverLevel)) {
				continue;
			}
			final ShotSpec spec = new ShotSpec()
					.baseDamage(this.getAttackDamage())
					.variant(type.ordinal());
			final BulletEntity metal = BulletLauncher.launchRaw(serverLevel,
					PvZEntityRegister.METAL.get(), this, this.getEntityGroupType(),
					BulletKinds.METAL, spec,
					new Vec3(target.getX(), target.getEyeY(), target.getZ()), Vec3.ZERO);
			if (metal != null) {
				metal.setState(1); // BULLET
			}

			if (--cnt == 0) {
				return;
			}
		}
	}

	public void dragMetal(LivingEntity target) {
		if (!(target instanceof IHasMetal metalTarget) || !metalTarget.hasMetal()) {
			return;   // 无金属目标不抓
		}
		final MetalTypes type = metalTarget.getMetalType();
		if (type == MetalTypes.EMPTY) {
			return;   // 防御：hasMetal 为 true 但类型为空
		}
		metalTarget.decreaseMetal();
		this.setAttackTime(this.getWorkCD());

		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		final ShotSpec spec = new ShotSpec()
				.baseDamage(this.getAttackDamage())
				.variant(type.ordinal());
		final BulletEntity metal = BulletLauncher.launchRaw(serverLevel,
				PvZEntityRegister.METAL.get(), this, this.getEntityGroupType(),
				BulletKinds.METAL, spec,
				new Vec3(target.getX(), target.getEyeY(), target.getZ()), Vec3.ZERO);
		if (metal != null) {
			metal.setState(0); // ABSORB
		}

		EntityUtil.playSound(this, PvZSoundRegister.MAGNET.get());
	}

	public int getAttackDamage() {
		return 100;
	}

	public int getSuperDragCnt() {
		return 4;
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.WORK_CD, this.getWorkCD()),
				Pair.of(PAZAlmanacs.WORK_RANGE, this.getAbsorbRange())
		));
	}

	public float getAbsorbRange() {
		return 15;
	}

	public int getWorkCD() {
		return (int) this.getSkillValue(SkillTypes.LESS_WORK_CD.get());
	}

	public boolean isMagnetActive() {
		return this.getAttackTime() == 0;
	}

	/**
	 * 旧实现走 {@code MetalTypes.getMetalItem}。新架构 {@link MetalTypes} 无此方法，
	 * 改为按类型从 {@link PvZItemRegister} 取。若新架构未注册对应物品，返回空。
	 */
	public ItemStack getMetalRenderItem() {
		if (this.getMetalType() == MetalTypes.EMPTY) {
			return ItemStack.EMPTY;
		}
		return new ItemStack(MetalTypes.getMetalItem(this.getMetalType()));
	}

	@Override
	public boolean canPAZTarget(Entity entity) {
		return entity instanceof IHasMetal metal && metal.hasMetal();
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("metal_type", this.getMetalType().ordinal());
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		final MetalTypes[] values = MetalTypes.values();
		final int ord = input.getIntOr("metal_type", MetalTypes.EMPTY.ordinal());
		this.setMetalType((ord >= 0 && ord < values.length) ? values[ord] : MetalTypes.EMPTY);
	}

	public MetalTypes getMetalType() {
		final MetalTypes[] values = MetalTypes.values();
		final int ord = this.entityData.get(METAL_TYPE);
		return (ord >= 0 && ord < values.length) ? values[ord] : MetalTypes.EMPTY;
	}

	public void setMetalType(MetalTypes type) {
		this.entityData.set(METAL_TYPE, type.ordinal());
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.5F, 1.3F);
	}

	@Override
	public int getSuperTimeLength() {
		return 60;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.MAGNET_SHROOM.get();
	}

	/* ================================================================== */
	/* AI: 搜索带梯子（金属）的植物                                         */
	/* ================================================================== */

	private static class TargetLadderGoal extends PVZTargetGoal {

		protected final AlgorithmUtil.EntitySorter sorter;

		public TargetLadderGoal(MagnetShroomEntity mobIn, boolean checkSight,
								boolean mustReach, float range) {
			super(mobIn, checkSight, mustReach, range, range);
			this.sorter = new AlgorithmUtil.EntitySorter(mob);
		}

		@Override
		public boolean canUse() {
			if (this.mob instanceof MagnetShroomEntity shroom && shroom.isPlantSleeping()) {
				return false;
			}
			if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0) {
				return false;
			}
			final List<LivingEntity> list1 = new ArrayList<>();
			this.mob.level().getEntitiesOfClass(PlantEntity.class, this.getAABB())
					.forEach(plant -> {
						if (!EntityUtil.canTargetEntity(this.mob, plant) && this.checkSenses(plant)) {
							if (plant.hasMetal()) {
								list1.add(plant);
							}
						}
					});
			if (list1.isEmpty()) {
				return false;
			}
			Collections.sort(list1, this.sorter);
			this.targetMob = list1.get(0);
			return true;
		}

		@Override
		public boolean canContinueToUse() {
			LivingEntity entity = this.mob.getTarget();
			if (entity == null) {
				entity = this.targetMob;
			}
			if (entity == null || !entity.isAlive()) {
				return false;
			}
			if (!EntityUtil.canTargetEntity(this.mob, entity) && this.checkSenses(entity)
					&& entity instanceof IHasMetal metal && metal.hasMetal()) {
				this.mob.setTarget(entity);
				return true;
			}
			return false;
		}
	}
}