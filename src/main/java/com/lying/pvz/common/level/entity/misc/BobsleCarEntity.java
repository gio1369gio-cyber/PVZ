package com.lying.pvz.common.level.entity.misc;

import javax.annotation.Nullable;

import com.lying.pvz.client.ClientUtil;
import com.lying.pvz.common.init.PvZItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LilyPadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 保龄球车：可被玩家驾驶或自动收集小生物的多乘客雪地载具。
 * 雪地上拥有额外滑行，落水会丢弃，摔落或受击过量会碎裂并掉落物品。
 */
public class BobsleCarEntity extends Entity {

	private static final EntityDataAccessor<Integer> TIME_SINCE_HIT =
			SynchedEntityData.defineId(BobsleCarEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Float> DAMAGE_TAKEN =
			SynchedEntityData.defineId(BobsleCarEntity.class, EntityDataSerializers.FLOAT);
	private static final int MAX_PASSENGER_SIZE = 4;
	private static final float SNOW_SMOOTH = 0.991f;
	private static final float MAX_MOVE_SPEED = 0.042f;
	private static final float MAX_ROTATION = 1f;

	private Status status;
	private float momentum;
	private float deltaRotation;
	private float boatGlide;

	public BobsleCarEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
		this.refreshDimensions();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(TIME_SINCE_HIT, 0);
		builder.define(DAMAGE_TAKEN, 0.0F);
	}

	@Override
	public void tick() {
		super.tick();
		this.status = this.getCarStatus();
		if (this.status == Status.IN_WATER) {
			this.ejectPassengers();
			this.discard();
		}
		if (this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}
		if (this.getDamageTaken() > 0.0F) {
			this.setDamageTaken(this.getDamageTaken() - 1.0F);
		}

		if (this.canSimulateMovement()) {
			this.updateMotion();
			if (this.level().isClientSide()) {
				this.updateControls();
			}
			this.move(MoverType.SELF, this.getDeltaMovement());
		} else {
			this.setDeltaMovement(Vec3.ZERO);
		}

		this.applyEffectsFromBlocks();

		if (!this.level().isClientSide()) {
			for (Entity entity : this.level().getEntities(this,
					this.getBoundingBox().inflate(0.2D, -0.01D, 0.2D),
					EntitySelector.pushableBy(this))) {
				if (!entity.isPassenger()) {
					if (checkCanRideOn(entity)) {
						entity.startRiding(this);
					} else {
						this.push(entity);
					}
				}
			}
		}
	}

	private boolean checkCanRideOn(Entity entity) {
		return !(this.getControllingPassenger() instanceof Player)
				&& this.getPassengers().size() < MAX_PASSENGER_SIZE
				&& !entity.isPassenger()
				&& entity.getBbWidth() < this.getBbWidth()
				&& entity instanceof LivingEntity
				&& !(entity instanceof WaterAnimal)
				&& !(entity instanceof Player);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.FAIL;
		}
		if (!this.level().isClientSide()) {
			player.startRiding(this);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

	protected void updateControls() {

		if (!this.isRidingPlayer(ClientUtil.getClientPlayer())) {
			return;
		}
		float f = 0;
		boolean left = ClientUtil.getMinecraft().options.keyLeft.isDown();
		boolean right = ClientUtil.getMinecraft().options.keyRight.isDown();
		boolean forward = ClientUtil.getMinecraft().options.keyUp.isDown();
		boolean back = ClientUtil.getMinecraft().options.keyDown.isDown();
		if (left) {
			this.deltaRotation -= MAX_ROTATION;
		}
		if (right) {
			this.deltaRotation += MAX_ROTATION;
		}
		if (!right && !left) {
			this.deltaRotation = 0;
		}
		this.setYRot(this.getYRot() + this.deltaRotation);
		if (right != left && !forward && !back) {
			f += MAX_MOVE_SPEED / 10f;
		}
		if (forward) {
			f += MAX_MOVE_SPEED;
		}
		if (back) {
			f -= MAX_MOVE_SPEED / 3;
		}
		this.setDeltaMovement(this.getDeltaMovement().add(
				Mth.sin(-this.getYRot() * ((float) Math.PI / 180F)) * f,
				0.0D,
				Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * f));
	}


	private boolean isRidingPlayer(Player player) {
		return player.getVehicle() != null && player.getVehicle() == this;
	}

	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Nullable
	@Override
	public LivingEntity getControllingPassenger() {
		Entity first = this.getFirstPassenger();
		return first instanceof LivingEntity living ? living : null;
	}

	@Override
	public boolean isPushable() {
		return true;
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return this.getPassengers().size() < MAX_PASSENGER_SIZE;
	}

	@Override
	public boolean dismountsUnderwater() {
		return true;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
		return super.getAddEntityPacket(entity);
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (this.isInvulnerableToBase(source)) {
			return false;
		}
		if (this.isRemoved()) {
			return true;
		}
		if (source.getEntity() != null
				&& source.getDirectEntity() != source.getEntity()
				&& this.hasPassenger(source.getEntity())) {
			return false;
		}
		this.setTimeSinceHit(10);
		this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
		this.syncVelocity = true;

		boolean creative = source.getEntity() instanceof Player player && player.isCreative();
		if (creative || this.getDamageTaken() > 40.0F) {
			if (!creative && (Boolean) level.getGameRules().get(GameRules.ENTITY_DROPS)) {
				this.spawnAtLocation(level, PvZItemRegister.BOBSLE_CAR.get());
			}
			this.discard();
		}
		return true;
	}

	@Override
	public void animateHurt(float yaw) {
		this.setTimeSinceHit(10);
		this.setDamageTaken(this.getDamageTaken() * 11.0F);
	}

	public Direction getMotionDirection() {
		return this.getDirection().getClockWise();
	}

	private Status getCarStatus() {
		if (this.isInWater()) {
			return Status.IN_WATER;
		}
		float f = this.getCarGlide();
		if (f == SNOW_SMOOTH) {
			this.boatGlide = f;
			return Status.ON_SNOW;
		}
		if (f > 0.0F) {
			this.boatGlide = f;
			return Status.ON_LAND;
		}
		return Status.IN_AIR;
	}

	public float getCarGlide() {
		BlockState at = this.level().getBlockState(this.blockPosition());
		BlockState below = this.level().getBlockState(this.blockPosition().below());
		if (at.is(Blocks.SNOW) || below.is(Blocks.SNOW_BLOCK)) {
			return SNOW_SMOOTH;
		}
		AABB box = this.getBoundingBox();
		AABB boxLow = new AABB(box.minX, box.minY - 0.001D,
				box.minZ, box.maxX, box.minY, box.maxZ);
		int i = Mth.floor(boxLow.minX) - 1;
		int j = Mth.ceil(boxLow.maxX) + 1;
		int k = Mth.floor(boxLow.minY) - 1;
		int l = Mth.ceil(boxLow.maxY) + 1;
		int i1 = Mth.floor(boxLow.minZ) - 1;
		int j1 = Mth.ceil(boxLow.maxZ) + 1;
		VoxelShape shape = Shapes.create(boxLow);
		float f = 0.0F;
		int count = 0;
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		for (int l1 = i; l1 < j; ++l1) {
			for (int i2 = i1; i2 < j1; ++i2) {
				int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
				if (j2 != 2) {
					for (int k2 = k; k2 < l; ++k2) {
						if (j2 <= 0 || k2 != k && k2 != l - 1) {
							mutable.set(l1, k2, i2);
							BlockState state = this.level().getBlockState(mutable);
							if (!(state.getBlock() instanceof LilyPadBlock)
									&& Shapes.joinIsNotEmpty(
									state.getCollisionShape(this.level(), mutable).move(l1, k2, i2),
									shape, BooleanOp.AND)) {
								f += state.getFriction(this.level(), mutable, this);
								++count;
							}
						}
					}
				}
			}
		}
		return count == 0 ? 0.0F : f / (float) count;
	}

	private void updateMotion() {
		double gravity = -0.04;
		this.momentum = 0.05F;
		if (this.status == Status.IN_AIR) {
			this.momentum = 0.9F;
		} else if (this.status == Status.ON_LAND || this.status == Status.ON_SNOW) {
			this.momentum = this.boatGlide;
			if (this.getControllingPassenger() instanceof Player) {
				this.boatGlide /= 2.0F;
			}
		}

		Vec3 vec3d = this.getDeltaMovement();
		this.setDeltaMovement(vec3d.x * (double) this.momentum, vec3d.y + gravity, vec3d.z * (double) this.momentum);
		this.deltaRotation *= this.momentum;
	}

	@Override
	protected void positionRider(Entity passenger, MoveFunction callback) {
		if (!this.hasPassenger(passenger)) {
			return;
		}
		float f = 0.0F;
		float f1 = (float) (this.isRemoved() ? 0.01F : 0.2D);
		if (this.getPassengers().size() > 1) {
			int i = this.getPassengers().indexOf(passenger);
			f = 0.2F - 0.7f * i;
			if (passenger instanceof Animal) {
				f = (float) ((double) f + 0.2D);
			}
		}

		Vec3 vec3d = new Vec3(f, 0.0D, 0.0D)
				.yRot(-this.getYRot() * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
		callback.accept(passenger, this.getX() + vec3d.x, this.getY() + (double) f1, this.getZ() + vec3d.z);
		passenger.setYRot(passenger.getYRot() + this.deltaRotation);
		passenger.setYHeadRot(passenger.getYHeadRot() + this.deltaRotation);
		this.applyYawToEntity(passenger);
		if (passenger instanceof Animal && this.getPassengers().size() > 1) {
			int j = passenger.getId() % 2 == 0 ? 90 : 270;
			passenger.setYBodyRot(((Animal) passenger).yBodyRot + (float) j);
			passenger.setYHeadRot(passenger.getYHeadRot() + (float) j);
		}
	}

	protected void applyYawToEntity(Entity entityToUpdate) {
		entityToUpdate.setYBodyRot(this.getYRot());
		float f = Mth.wrapDegrees(entityToUpdate.getYRot() - this.getYRot());
		float f1 = Mth.clamp(f, -105.0F, 105.0F);
		entityToUpdate.yRotO += f1 - f;
		entityToUpdate.setYRot(entityToUpdate.getYRot() + f1 - f);
		entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
	}

	@Override
	public void onPassengerTurned(Entity entityToUpdate) {
		this.applyYawToEntity(entityToUpdate);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
		if (this.isPassenger()) {
			return;
		}
		if (onGroundIn) {
			if (this.fallDistance > 3.0F) {
				if (this.status != Status.ON_LAND && this.status != Status.ON_SNOW) {
					this.fallDistance = 0.0F;
					return;
				}
				this.causeFallDamage(this.fallDistance, 1.0F, this.damageSources().fall());
				if (!this.level().isClientSide() && !this.isRemoved()) {
					this.discard();
					if (this.level() instanceof ServerLevel serverLevel
							&& (Boolean) serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)) {
						this.spawnAtLocation(serverLevel, PvZItemRegister.BOBSLE_CAR.get());
					}
				}
			}
			this.fallDistance = 0.0F;
		} else if (!this.level().getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && y < 0.0D) {
			this.fallDistance = (float) ((double) this.fallDistance - y);
		}
	}

	public void setDamageTaken(float damageTaken) {
		this.entityData.set(DAMAGE_TAKEN, damageTaken);
	}

	public float getDamageTaken() {
		return this.entityData.get(DAMAGE_TAKEN);
	}

	public void setTimeSinceHit(int timeSinceHit) {
		this.entityData.set(TIME_SINCE_HIT, timeSinceHit);
	}

	public int getTimeSinceHit() {
		return this.entityData.get(TIME_SINCE_HIT);
	}

	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return EntityDimensions.scalable(1.25f, 1.4f);
	}

	public static enum Status {
		IN_WATER, ON_SNOW, ON_LAND, IN_AIR
	}
}