package com.lying.pvz.common.level.entity.zombie.roof;


import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.ICanAttract;
import com.lying.pvz.api.entity.ability.ICanPushBack;
import com.lying.pvz.api.projectile.Mark;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZRandomTargetGoal;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.level.entity.AbstractPAZEntity;
import com.lying.pvz.common.level.entity.projectile.TargetArrowEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.common.projectile.mark.MarkRegistry;
import com.lying.pvz.common.tag.PvZEntityTypeTags;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

/**
 * 蹦极僵尸。
 *
 * <p>三种工作模式由 {@link BungeeTypes} 与 {@link BungeeStates} 协同驱动：
 * <ul>
 *   <li><b>STEAL</b>：瞄准目标后下放、抓取、回收到初始位置，可被击退打断；</li>
 *   <li><b>HELP</b>：玩家使用蹦极箭触发，抓取 {@code stealTarget} 后回收；</li>
 *   <li><b>SUMMON</b>：为 BOSS 召唤而存在，把召唤物带入战场后 {@code discard()}。</li>
 * </ul>
 *
 * <p>除 {@code PUSH_BACK}（被击退回收动画）外均可被选中、受伤害；
 * 避免"卡住后植物不攻击"与刷怪蛋生成的蹦极无法被集火。
 *
 * <p>本体 45 血、无重力、可使用碰撞箱之外的空间移动；
 * 禁止迷你、禁止被 Bungee 二次偷取、禁止被吸引。
 */
public class BungeeZombieEntity extends ZombieEntity implements ICanPushBack {

	private static final EntityDataAccessor<Integer> BUNGEE_STATE =
			SynchedEntityData.defineId(BungeeZombieEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> BUNGEE_TYPE =
			SynchedEntityData.defineId(BungeeZombieEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<BlockPos> ORIGIN_POS =
			SynchedEntityData.defineId(BungeeZombieEntity.class, EntityDataSerializers.BLOCK_POS);
	protected EntityType<?> entityType;
	private BlockPos stealPos;
	private LivingEntity stealTarget;
	/** 等待标记命中的 tick；超时后放弃本次偷取。 */
	private int markWaitTick;
	/** 本次下降是否已射出目标箭（EdgarRobot 直接进 DOWN 时也需要补射）。 */
	private boolean arrowShot;

	public BungeeZombieEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canBeMini = false;
		this.canBeStealByBungee = false;
		this.setImmuneAllEffects();
		this.setIsWholeBody();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(BUNGEE_STATE, BungeeStates.WAIT.ordinal());
		builder.define(BUNGEE_TYPE, BungeeTypes.STEAL.ordinal());
		builder.define(ORIGIN_POS, BlockPos.ZERO);
	}

	@Override
	public void finalizeSpawn(net.minecraft.nbt.CompoundTag tag) {
		super.finalizeSpawn(tag);
		this.setOriginPos(this.blockPosition());
	}

	@Override
	public void tick() {
		this.noPhysics = true;
		super.tick();
	}

	@Override
	protected void registerGoals() {
		this.registerTargetGoals();
	}

	@Override
	protected void registerTargetGoals() {
		this.targetSelector.addGoal(0,
				new BungeeRandomTargetGoal(this, true, true,
						ZombieUtil.CLOSE_TARGET_RANGE, ZombieUtil.HIGH_TARGET_HEIGHT));
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			if (this.tickCount > 1000 && this.tickCount % 80 == 0
					&& this.canDespawn && this.getBungeeState() == BungeeStates.WAIT) {
				this.discard();
			}
			if (this.getBungeeState() == BungeeStates.PUSH_BACK) {
				this.setAttackTime(this.getAttackTime() - 1);
				this.setDeltaMovement(new Vec3(0, 1.2, 0));
				if (this.getAttackTime() <= -60) {
					if (this.getBungeeType() == BungeeTypes.SUMMON
							&& EntityUtil.isEntityValid(this.getStealTarget())) {
						this.getStealTarget().discard();
					}
					this.discard();
				}
				return;
			}
			if (this.getBungeeType() == BungeeTypes.STEAL) {
				this.tickSteal();
			} else if (this.getBungeeType() == BungeeTypes.HELP) {
				this.tickHelp();
			} else if (this.getBungeeType() == BungeeTypes.SUMMON) {
				this.tickSummon();
			}
		}
	}

	protected void tickHelp() {
		if (!this.isSuitableTarget(this.getStealTarget())) {
			if (this.getAttackTime() >= 0) {
				this.setAttackTime(-1);
			}
			this.setBungeeState(BungeeStates.UP);
		}
		if (this.getBungeeState() == BungeeStates.WAIT) {
			if (this.isSuitableTarget(this.getStealTarget())) {
				this.setBungeeState(BungeeStates.DOWN);
				EntityUtil.playSound(this.getStealTarget(), PvZSoundRegister.BUNGEE_SCREAM.get());
			}
		} else if (this.getBungeeState() == BungeeStates.DOWN) {
			if (this.isNearToTarget()) {
				this.setAttackTime(this.getStayTime());
				if (this.getStealTarget() != null) {
					this.getStealTarget().startRiding(this);
				}
				this.setBungeeState(BungeeStates.CATCH);
				EntityUtil.playSound(this, PvZSoundRegister.DRAG.get());
			} else {
				this.moveToTarget();
			}
		} else if (this.getBungeeState() == BungeeStates.CATCH) {
			this.setDeltaMovement(Vec3.ZERO);
			if (this.getAttackTime() > 0) {
				this.setAttackTime(this.getAttackTime() - 1);
			}
			if (this.getAttackTime() == 0) {
				this.setBungeeState(BungeeStates.UP);
			}
		} else if (this.getBungeeState() == BungeeStates.UP) {
			this.setAttackTime(this.getAttackTime() - 1);
			this.moveBackToOrigin();
			if (this.getStealTarget() != null && this.getAttackTime() < -100) {
				this.dealDamageAndRemove();
				return;
			}
		}
	}

	protected void tickSteal() {
		if (this.getBungeeState() == BungeeStates.WAIT) {
			final LivingEntity target = this.getTarget();
			if (target != null && this.isSuitableTarget(target)) {
				if (this.getOriginPos().getY() - target.getY() <= 15) {
					this.setOriginPos(target.blockPosition().above(20));
					this.setBungeeState(BungeeStates.BACK_WAIT);
					return;
				}
				this.setBungeeState(BungeeStates.DOWN);
				this.setStealTarget(target);
				this.markWaitTick = 0;
				this.arrowShot = false;
				EntityUtil.playSound(target, PvZSoundRegister.BUNGEE_SCREAM.get());
			}
			return;
		}

		final LivingEntity stealTarget = this.getStealTarget();
		if (stealTarget == null || !this.isSuitableTarget(stealTarget)) {
			this.setBungeeState(BungeeStates.BACK_WAIT);
			this.setStealTarget(null);
			return;
		}

		if (this.getBungeeState() == BungeeStates.BACK_WAIT) {
			this.moveBackToOrigin();
			if (this.distanceToSqr(this.getOriginPos().getX(),
					this.getOriginPos().getY(), this.getOriginPos().getZ()) <= 2) {
				this.setBungeeState(BungeeStates.WAIT);
				this.setDeltaMovement(Vec3.ZERO);
			}
		} else if (this.getBungeeState() == BungeeStates.DOWN) {
			if (!this.arrowShot) {
				this.shootArrowToTarget();
				this.arrowShot = true;
			}
			if (this.isNearToTarget() && this.isMarkedByMe(stealTarget)) {
				this.setAttackTime(this.getStayTime());
				this.stealPos = stealTarget.blockPosition();
				stealTarget.startRiding(this);
				this.setBungeeState(BungeeStates.CATCH);
				EntityUtil.playSound(this, PvZSoundRegister.DRAG.get());
			} else if (this.isNearToTarget()) {
				// 已到目标身边但箭还没命中：等标记，超时则放弃。
				this.setDeltaMovement(Vec3.ZERO);
				if (++this.markWaitTick > 80) {
					this.setBungeeState(BungeeStates.BACK_WAIT);
					this.setStealTarget(null);
					this.markWaitTick = 0;
				}
			} else {
				this.moveToTarget();
			}
		} else if (this.getBungeeState() == BungeeStates.CATCH) {
			this.setDeltaMovement(Vec3.ZERO);
			if (this.getAttackTime() > 0) {
				this.setAttackTime(this.getAttackTime() - 1);
			}
			stealTarget.startRiding(this);
			if (this.getAttackTime() == 0) {
				this.setBungeeState(BungeeStates.UP);
			}
		} else if (this.getBungeeState() == BungeeStates.UP) {
			this.setAttackTime(this.getAttackTime() - 1);
			this.moveBackToOrigin();
			stealTarget.startRiding(this);
			if (this.getAttackTime() < -100) {
				this.dealDamageAndRemove();
				return;
			}
		}
	}

	protected void tickSummon() {
		if (this.getBungeeState() != BungeeStates.WAIT
				&& !EntityUtil.isEntityValid(this.getStealTarget())) {
			this.setBungeeState(BungeeStates.UP);
		}
		if (this.getBungeeState() == BungeeStates.WAIT) {
			if (this.getPassengers().isEmpty()) {
				this.summonZombie();
				this.setBungeeState(BungeeStates.DOWN);
				EntityUtil.playSound(this.getStealTarget(), PvZSoundRegister.BUNGEE_SCREAM.get());
			}
		} else if (this.getBungeeState() == BungeeStates.DOWN) {
			if (this.level().getBlockState(this.blockPosition().below()).getBlock() != Blocks.AIR) {
				if (this.getStealTarget() != null) {
					this.getStealTarget().stopRiding();
				}
				this.setAttackTime(0);
				this.setBungeeState(BungeeStates.UP);
			} else {
				if (this.getStealTarget() != null) {
					this.getStealTarget().startRiding(this);
				}
				this.setDeltaMovement(new Vec3(0, -0.5D, 0));
			}
		} else if (this.getBungeeState() == BungeeStates.UP) {
			this.moveBackToOrigin();
			this.setAttackTime(this.getAttackTime() - 1);
			if (this.getAttackTime() < -60) {
				this.discard();
				return;
			}
		}
	}

	@Override
	public void pushBack() {
		this.setBungeeState(BungeeStates.PUSH_BACK);
		if (this.getBungeeType() == BungeeTypes.STEAL && this.getStealTarget() != null) {
			this.getStealTarget().stopRiding();
			if (this.stealPos != null) {
				this.getStealTarget().setPos(this.stealPos.getX(), this.stealPos.getY(), this.stealPos.getZ());
			}
			this.setStealTarget(null);
		}
	}

	@Override
	public boolean canPAZTarget(Entity target) {
		return canBungeeSteal(target);
	}

	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		return super.canBeTargetBy(living) && this.getBungeeState() != BungeeStates.PUSH_BACK;
	}

	@Override
	protected boolean isZombieInvulnerableTo(DamageSource source) {
		return super.isZombieInvulnerableTo(source) || this.getBungeeState() == BungeeStates.PUSH_BACK;
	}

	public void setSummonType(EntityType<?> entityType) {
		this.entityType = entityType;
	}

	public void summonZombie() {
		if (this.entityType == null) {
			this.entityType = BuiltInRegistries.ENTITY_TYPE
					.getRandomElementOf(PvZEntityTypeTags.BUNGEE_SPAWNS, this.getRandom())
					.<EntityType<?>>map(Holder::value)
					.orElse(null);
		}
		if (this.entityType == null) {
			return;
		}
		Entity entity = this.entityType.create(this.level(), EntitySpawnReason.TRIGGERED);
		if (entity instanceof LivingEntity living) {
			EntityUtil.onEntitySpawn(this.level(), entity, this.blockPosition());
			entity.startRiding(this);
			this.setStealTarget(living);
		}
	}

	private void dealDamageAndRemove() {
		this.discard();
	}

	public int getStayTime() {
		return 80;
	}

	@Override
	public boolean canBeAttractedBy(ICanAttract defender) {
		return false;
	}

	private void moveToTarget() {
		if (!EntityUtil.isEntityValid(this.getStealTarget())) {
			return;
		}
		Vec3 vec = this.getStealTarget().position().subtract(this.position()).normalize();
		final double speed = 0.55D;
		this.setDeltaMovement(vec.scale(speed));
	}

	private void moveBackToOrigin() {
		Vec3 vec = new Vec3(
				this.getOriginPos().getX() - this.getX(),
				this.getOriginPos().getY() - this.getY(),
				this.getOriginPos().getZ() - this.getZ()).normalize();
		final double speed = 0.4D;
		this.setDeltaMovement(vec.scale(speed));
	}

	private boolean isNearToTarget() {
		return EntityUtil.isEntityValid(this.getStealTarget())
				&& this.distanceToSqr(this.getStealTarget()) <= 2;
	}

	/** 只有自己射出的目标箭命中了，才允许抓取；非 PAZ 实体（如玩家）沿用旧行为。 */
	private boolean isMarkedByMe(@Nullable LivingEntity target) {
		if (!(target instanceof Mark.Holder holder)) {
			return true;
		}
		return holder.hasMark(new Mark.Key(MarkRegistry.BUNGEE_TARGET, this.getUUID()));
	}

	private void shootArrowToTarget() {
		TargetArrowEntity arrow = new TargetArrowEntity(this.level(), this);
		arrow.setPos(this.getX(), this.getY(), this.getZ());
		if(this.getStealTarget() == null) {return;}
		// 无重力直线箭，瞄目标眼睛。
		Vec3 dir = this.getStealTarget().getEyePosition()
				.subtract(arrow.position())
				.normalize();
		arrow.shoot(dir.x, dir.y, dir.z, 1.5F, 0.0F);
		this.level().addFreshEntity(arrow);
	}

	public static boolean canBungeeSteal(Entity target) {
		if (!(target instanceof LivingEntity living) || EntityUtil.isEntityBoss(living)
				|| isHoldingZombieDoll(living)) {
			return false;
		}
		if (target instanceof AbstractPAZEntity paz) {
			return paz.canBeStealByBungee();
		}
		return EntityUtil.getCurrentMaxHealth(living) <= ConfigUtil.getLimitDamage();
	}

	protected boolean isSuitableTarget(LivingEntity target) {
		if (!EntityUtil.isEntityValid(target) || isHoldingZombieDoll(target)) {
			return false;
		}
		if (target.getVehicle() instanceof BungeeZombieEntity) {
			return target.getVehicle().is(this);
		}
		return true;
	}

	private static boolean isHoldingZombieDoll(LivingEntity target) {
		return target.getMainHandItem().getItem().equals(PvZItemRegister.ZOMBIE_DOLL.get())
				|| target.getOffhandItem().getItem().equals(PvZItemRegister.ZOMBIE_DOLL.get());
	}

	@Override
	public float getLife() {
		return 45;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(1.2F, 2F);
	}

	@Override
	public IZombieType getZombieType() {
		return RoofZombies.BUNGEE_ZOMBIE.get();
	}

	@Override
	public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
		return true;
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setBungeeState(BungeeStates.fromOrdinal(input.getIntOr("bungee_state", BungeeStates.WAIT.ordinal())));
		this.setBungeeType(BungeeTypes.fromOrdinal(input.getIntOr("bungee_type", BungeeTypes.STEAL.ordinal())));
		final int stealTargetId = input.getIntOr("steal_target", 0);
		if (stealTargetId != 0) {
			Entity entity = this.level().getEntity(stealTargetId);
			this.stealTarget = entity instanceof LivingEntity living ? living : null;
		}
		final String summonTypeId = input.getStringOr("summon_type", "");
		if (!summonTypeId.isEmpty()) {
			final Identifier id = Identifier.tryParse(summonTypeId);
			if (id != null) {
				this.entityType = BuiltInRegistries.ENTITY_TYPE.getValue(id);
			}
		}
		this.stealPos = new BlockPos(
				input.getIntOr("steal_pos_x", 0),
				input.getIntOr("steal_pos_y", 0),
				input.getIntOr("steal_pos_z", 0));
		this.setOriginPos(new BlockPos(
				input.getIntOr("origin_pos_x", 0),
				input.getIntOr("origin_pos_y", 0),
				input.getIntOr("origin_pos_z", 0)));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("bungee_state", this.getBungeeState().ordinal());
		output.putInt("bungee_type", this.getBungeeType().ordinal());
		if (this.stealTarget != null) {
			output.putInt("steal_target", this.stealTarget.getId());
		}
		if (this.entityType != null) {
			output.putString("summon_type", BuiltInRegistries.ENTITY_TYPE.getKey(this.entityType).toString());
		}
		if (this.stealPos != null) {
			output.putInt("steal_pos_x", this.stealPos.getX());
			output.putInt("steal_pos_y", this.stealPos.getY());
			output.putInt("steal_pos_z", this.stealPos.getZ());
		}
		output.putInt("origin_pos_x", this.getOriginPos().getX());
		output.putInt("origin_pos_y", this.getOriginPos().getY());
		output.putInt("origin_pos_z", this.getOriginPos().getZ());
	}

	public void setBungeeState(BungeeStates state) {
		this.entityData.set(BUNGEE_STATE, state.ordinal());
	}

	public void setBungeeType(BungeeTypes type) {
		this.entityData.set(BUNGEE_TYPE, type.ordinal());
	}

	public BungeeStates getBungeeState() {
		return BungeeStates.fromOrdinal(this.entityData.get(BUNGEE_STATE));
	}

	public BungeeTypes getBungeeType() {
		return BungeeTypes.fromOrdinal(this.entityData.get(BUNGEE_TYPE));
	}

	public BlockPos getOriginPos() {
		return this.entityData.get(ORIGIN_POS);
	}

	public void setOriginPos(BlockPos pos) {
		this.entityData.set(ORIGIN_POS, pos);
	}

	@Nullable
	public LivingEntity getStealTarget() {
		return this.stealTarget;
	}

	public void setStealTarget(LivingEntity target) {
		this.stealTarget = target;
	}

	public enum BungeeStates {
		WAIT, BACK_WAIT, DOWN, CATCH, UP, PUSH_BACK;

		public static BungeeStates fromOrdinal(int ordinal) {
			final BungeeStates[] values = values();
			return (ordinal >= 0 && ordinal < values.length) ? values[ordinal] : WAIT;
		}
	}

	public enum BungeeTypes {
		STEAL, HELP, SUMMON;

		public static BungeeTypes fromOrdinal(int ordinal) {
			final BungeeTypes[] values = values();
			return (ordinal >= 0 && ordinal < values.length) ? values[ordinal] : STEAL;
		}
	}

	static class BungeeRandomTargetGoal extends PVZRandomTargetGoal {

		private final BungeeZombieEntity zombie;

		public BungeeRandomTargetGoal(BungeeZombieEntity mobIn, boolean checkSight,
									  boolean mustReach, float w, float h) {
			super(mobIn, checkSight, mustReach, w, h);
			this.zombie = mobIn;
		}

		@Override
		public boolean canContinueToUse() {
			return this.zombie.isSuitableTarget(this.zombie.getTarget());
		}
	}
}