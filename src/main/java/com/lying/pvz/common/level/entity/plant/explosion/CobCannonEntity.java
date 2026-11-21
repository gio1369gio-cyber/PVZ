package com.lying.pvz.common.level.entity.plant.explosion;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.IAlmanacEntry;
import com.lying.pvz.api.projectile.ShotSpec;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZRandomTargetGoal;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.projectile.BulletKinds;
import com.lying.pvz.common.projectile.BulletLauncher;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.enums.PAZAlmanacs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 玉米加农炮。
 *
 * <p>玉米粒通过 {@link #getPreCD()} 自动累积到 2 才可发射。玩家可骑乘操控，
 * 副手持空白手可发射锁定的目标 / 方块位置；无锁定则随机轰炸附近目标。
 * super 模式一次发射 {@link #getSuperCornNum()} 发。
 *
 * <p>新架构适配：
 * <ul>
 *   <li>玉米弹 → {@link com.lying.pvz.common.projectile.BulletKinds#CORN}（玉米粒数量走 {@code ShotSpec.drop}）</li>
 *   <li>目标射击 → {@code BulletLauncher.launch}；方块射击 → {@code BulletLauncher.launchAtPoint}</li>
 * </ul>
 */
public class CobCannonEntity extends PlantEntity {

	protected static final EntityDataAccessor<Integer> CORN_NUM =
			SynchedEntityData.defineId(CobCannonEntity.class, EntityDataSerializers.INT);
	protected Optional<LivingEntity> lockTarget = Optional.empty();
	protected Optional<BlockPos> lockPos = Optional.empty();
	protected int cornCnt = 0;
	protected final int MaxCornCnt = 16;
	protected int preTick = 0;
	private int climbTick = 0;

	public CobCannonEntity(EntityType<? extends PlantEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canCollideWithPlant = false;
		this.isImmuneToWeak = true;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(CORN_NUM, 1);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		final AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(0.25D);
		}
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(2, new PVZRandomTargetGoal(this, true, false, 10, 48, 16));
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if (this.level().isClientSide()) {
			return;
		}
		++this.preTick;
		if (this.getAttackTime() == 0 && this.preTick >= this.getPreCD()) {
			this.preTick = 0;
			this.setCornNum(Math.min(2, this.getCornNum() + 1));
		}
		if (this.getCornNum() >= 2 && !this.isPlayerRiding() && this.getTarget() != null) {
			this.setAttackTime(this.getAnimCD());
			this.setCornNum(this.getCornNum() - 1);
		}
		if (this.getAttackTime() > 0) {
			this.setAttackTime(this.getAttackTime() - 1);
			this.getPassengers().forEach(Entity::stopRiding);
			if (this.getAttackTime() == this.getAnimCD() / 2) {
				this.startAttack();
			}
		}
	}

	public void checkAndAttack() {
		if (this.getAttackTime() != 0 || !this.isPlayerRiding()) {
			return;
		}
		final Player player = (Player) this.getPassengers().get(0);
		final Vec3 look = player.getLookAngle();
		final Vec3 start = player.position().add(0, player.getEyeHeight(), 0);
		final double range = 60;
		Vec3 end = start.add(look.normalize().multiply(range, range, range));
		final ClipContext ray = new ClipContext(start, end,
				ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
		final HitResult result = this.level().clip(ray);
		if (result.getType() != HitResult.Type.MISS) {
			end = result.getLocation();
		}
		final HitResult entityRay = this.rayTraceEntities(this.level(), player, range, start, end);
		if (entityRay instanceof EntityHitResult ehr) {
			if (ehr.getEntity() instanceof LivingEntity living) {
				this.setAttackTime(this.getAnimCD());
				this.setCornNum(this.getCornNum() - 1);
				this.lockTarget = Optional.of(living);
			}
		} else if (result instanceof BlockHitResult bhr
				&& result.getType() == HitResult.Type.BLOCK) {
			this.setAttackTime(this.getAnimCD());
			this.setCornNum(this.getCornNum() - 1);
			this.lockPos = Optional.ofNullable(bhr.getBlockPos());
		}
	}

	protected void startAttack() {
		if (this.lockTarget.isPresent()) {
			this.shootCorn(this.lockTarget.get());
			this.lockTarget = Optional.empty();
			return;
		}
		if (this.lockPos.isPresent()) {
			this.shootCorn(this.lockPos.get());
			this.lockPos = Optional.empty();
			return;
		}
		final float range = 45F;
		final List<LivingEntity> list = EntityUtil.getViewableTargetableEntity(this,
				EntityUtil.getEntityAABB(this, range, range));
		final int num = this.isPlantInSuperMode() ? this.getSuperCornNum() : 1;
		for (int i = 0; i < num; ++i) {
			LivingEntity res = this;
			if (!list.isEmpty()) {
				res = list.get(this.getRandom().nextInt(list.size()));
			}
			this.shootCorn(res);
		}
	}

	protected void shootCorn(LivingEntity target) {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		final ShotSpec spec = new ShotSpec();
		this.configureCornShot(spec);
		BulletLauncher.launch(serverLevel, PvZEntityRegister.CORN.get(), this,
				this.getEntityGroupType(), BulletKinds.CORN, spec,
				new Vec3(this.getX(), this.getY() + 1.5D, this.getZ()), target, null);
	}

	protected void shootCorn(BlockPos pos) {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		final ShotSpec spec = new ShotSpec();
		this.configureCornShot(spec);
		BulletLauncher.launchAtPoint(serverLevel, PvZEntityRegister.CORN.get(), this,
				this.getEntityGroupType(), BulletKinds.CORN, spec,
				new Vec3(this.getX(), this.getY() + 1.5D, this.getZ()),
				new Vec3(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D));
	}

	private void configureCornShot(ShotSpec spec) {
		spec.baseDamage(this.getAttackDamage());
		if (this.cornCnt > 0) {
			spec.drop(new ItemStack(PvZItemRegister.POP_CORN.get(), this.cornCnt));
		}
		this.cornCnt = 0;
		EntityUtil.playSound(this, PvZSoundRegister.COB_LAUNCH.get());
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 vec3d) {
		if (player.isSecondaryUseActive() || EntityUtil.canTargetEntity(this, player)) {
			return InteractionResult.FAIL;
		}
		final ItemStack stack = player.getItemInHand(hand);
		if (this.getAttackTime() == 0 && stack.isEmpty()) {
			if (this.mountTo(player)) {
				return InteractionResult.SUCCESS;
			}
		} else if (stack.getItem() == PvZItemRegister.CORN.get()) {
			if (this.cornCnt < this.MaxCornCnt) {
				++this.cornCnt;
				stack.shrink(Math.min(stack.getCount(), this.MaxCornCnt - this.cornCnt));
			}
			return InteractionResult.CONSUME;
		}
		return super.interact(player, hand, vec3d);
	}


	/** 新架构：覆写 {@link #onSuperStart(boolean)}。 */
	@Override
	public void onSuperStart(boolean first) {
		super.onSuperStart(first);
		this.setAttackTime(this.getAnimCD());
	}

	@Override
	protected boolean shouldLockXZ() {
		return !this.isPlayerRiding();
	}

	@Nullable
	protected HitResult rayTraceEntities(Level world, Player player, double range,
										 Vec3 startVec, Vec3 endVec) {
		return ProjectileUtil.getEntityHitResult(world, (Entity) player, startVec, endVec,
				player.getBoundingBox().inflate(range),
				entity -> EntityUtil.isEntityValid(entity)
						&& entity instanceof LivingEntity
						&& !entity.is(this),
				0.0F);
	}

	public boolean isPlayerRiding() {
		for (Entity entity : this.getPassengers()) {
			if (entity instanceof Player) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canPlaceOuter() {
		return false;
	}

	private boolean isRidingPlayer(Player player) {
		return player.getVehicle() != null && player.getVehicle() == this;
	}

	@Override
	public void travel(Vec3 vec3) {
		if (!this.isAlive()) {
			return;
		}
		if (this.isVehicle() && this.isPlayerRiding()) {
			final Player player = (Player) this.getPassengers().get(0);
			if (player == null) {
				return;
			}
			this.setYRot(player.getYRot());
			this.yRotO = this.getYRot();
			this.setXRot(player.getXRot() * 0.5F);
			this.setRot(this.getYRot(), this.getXRot());
			this.yBodyRot = this.getYRot();
			this.setYHeadRot(this.yBodyRot);
			final float f = player.xxa * 0.5F;
			float f1 = player.zza;
			if (f1 <= 0.0F) {
				f1 *= 0.25F;
			}
			if (this.horizontalCollision) {
				if (++this.climbTick <= 8) {
					final Vec3 vec = this.getDeltaMovement();
					this.setDeltaMovement(vec.x, 0.2D, vec.z);
				}
			} else {
				this.climbTick = 0;
			}
			this.setSpeed(this.getSpeed() * 0.1F);
			final AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
			if (movementSpeed != null) {
				this.setSpeed((float) movementSpeed.getValue());
			}
			super.travel(new Vec3(f, vec3.y, f1));
			final double d2 = this.getX() - this.xo;
			final double d3 = this.getZ() - this.zo;
			float f4 = Mth.sqrt((float) (d2 * d2 + d3 * d3)) * 4.0F;
			if (f4 > 1.0F) {
				f4 = 1.0F;
			}
			this.walkAnimation.update(f4, 0.4F,1.0f);
		} else {
			super.travel(vec3);
		}
	}

	@Override
	public boolean shouldWilt() {
		return this.isInWater();
	}

	protected boolean mountTo(Player player) {
		if (this.level().isClientSide()) {
			return false;
		}
		this.setYRot(player.getYRot());
		this.setXRot(player.getXRot());
		player.startRiding(this);
		return true;
	}

	@Override
	public Vec3 getPassengerRidingPosition(Entity entity) {
		return super.getPassengerRidingPosition(entity).add(0, 0.2, 0);
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return !EntityUtil.canTargetEntity(this, passenger) && super.canAddPassenger(passenger);
	}

	@Override
	public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
		return false;
	}

	@Override
	public void addAlmanacEntries(List<Pair<IAlmanacEntry, Number>> list) {
		super.addAlmanacEntries(list);
		list.addAll(Arrays.asList(
				Pair.of(PAZAlmanacs.PREPARE_CD, this.getPreCD()),
				Pair.of(PAZAlmanacs.ATTACK_DAMAGE, this.getAttackDamage())
		));
	}

	public int getPreCD() {
		return 1000;
	}

	public int getSuperCornNum() {
		return 4;
	}

	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.NORMAL_BOMB_DAMAGE.get());
	}

	public int getAnimCD() {
		return 60;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(1.25F, 1F);
	}

	@Override
	public int getSuperTimeLength() {
		return 80;
	}

	/* ================================================================== */
	/* NBT（ValueInput / ValueOutput）                                      */
	/* ================================================================== */

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.preTick = input.getIntOr("cannon_pre_tick", 0);
		this.setCornNum(input.getIntOr("cannon_corn_num", 1));
		this.cornCnt = input.getIntOr("cannon_pop_corn_cnt", 0);
		final int targetId = input.getIntOr("cannon_lock_target", 0);
		if (targetId != 0) {
			final Entity entity = this.level().getEntity(targetId);
			if (entity instanceof LivingEntity living) {
				this.lockTarget = Optional.of(living);
			}
		}
		final Optional<Integer> lx = input.getInt("cannon_lock_pos_x");
		final Optional<Integer> ly = input.getInt("cannon_lock_pos_y");
		final Optional<Integer> lz = input.getInt("cannon_lock_pos_z");
		if (lx.isPresent() && ly.isPresent() && lz.isPresent()) {
			this.lockPos = Optional.of(new BlockPos(lx.get(), ly.get(), lz.get()));
		}
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("cannon_pre_tick", this.preTick);
		output.putInt("cannon_corn_num", this.getCornNum());
		output.putInt("cannon_pop_corn_cnt", this.cornCnt);
		if (this.lockTarget.isPresent()) {
			output.putInt("cannon_lock_target", this.lockTarget.get().getId());
		}
		if (this.lockPos.isPresent()) {
			output.putInt("cannon_lock_pos_x", this.lockPos.get().getX());
			output.putInt("cannon_lock_pos_y", this.lockPos.get().getY());
			output.putInt("cannon_lock_pos_z", this.lockPos.get().getZ());
		}
	}
	@Override
	public @Nullable LivingEntity getControllingPassenger() {
		Entity first = this.getFirstPassenger();
		return first instanceof Player player ? player : null;
	}
	public int getCornNum() {
		return this.entityData.get(CORN_NUM);
	}

	public void setCornNum(int num) {
		this.entityData.set(CORN_NUM, num);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.COB_CANNON.get();
	}
}