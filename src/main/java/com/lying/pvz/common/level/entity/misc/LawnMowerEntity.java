package com.lying.pvz.common.level.entity.misc;

import java.util.List;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.AbstractOwnerEntity;
import com.lying.pvz.utils.EntityUtil;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * 割草机：放置在草坪边界，探测到可攻击目标后沿着朝向高速推进并清除沿途目标。
 * 落水或超过配置时限后消失；玩家空手右键可以回收成物品。
 */
public class LawnMowerEntity extends AbstractOwnerEntity {

	private static final EntityDataAccessor<Boolean> START_RUN =
			SynchedEntityData.defineId(LawnMowerEntity.class, EntityDataSerializers.BOOLEAN);

	public LawnMowerEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
		this.setDeltaMovement(Vec3.ZERO);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(START_RUN, false);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickMove();
		if (!this.level().isClientSide()) {
			if (this.isInWater()
					|| this.tickCount >= PvZConfig.LOCAL_CONFIG.EntitySettings
					.EntityLiveTick.LawnMowerLiveTick.get()) {
				this.discard();
				return;
			}
			if (this.isStartRun()) {
				this.level().getEntitiesOfClass(Entity.class,
								this.getBoundingBox().inflate(0.5D),
								e -> EntityUtil.canTargetEntity(this.getOwnerOrSelf(), e))
						.forEach(this::checkAndRemoveEntity);
				double angle = this.getYRot() * Math.PI / 180;
				double speed = 0.4D;
				this.setDeltaMovement(-Math.sin(angle) * speed,
						this.getDeltaMovement().y,
						Math.cos(angle) * speed);
			} else {
				List<Entity> list = this.level().getEntitiesOfClass(Entity.class,
						this.getBoundingBox().inflate(1),
						t -> EntityUtil.canTargetEntity(this.getOwnerOrSelf(), t));
				if (!list.isEmpty()) {
					this.onStartRun(list.get(0));
				}
			}
			if (this.onGround() && this.getDeltaMovement().y < 0) {
				Vec3 v = this.getDeltaMovement();
				this.setDeltaMovement(v.x, 0, v.z);
			}
		}
		if (this.tickCount <= 5) {
			BlockPos pos = this.blockPosition();
			this.setPos(pos.getX() + 0.5D, this.getY(), pos.getZ() + 0.5D);
		}
	}

	public void checkAndRemoveEntity(Entity target) {
		if (EntityUtil.canEntityBeRemoved(target)) {
			target.discard();
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		if (!this.isStartRun()
				&& hand == InteractionHand.MAIN_HAND
				&& player.getMainHandItem().isEmpty()) {
			if (!this.level().isClientSide()) {
				player.addItem(new ItemStack(PvZItemRegister.LAWN_MOWER.get()));
				this.discard();
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	private void onStartRun(Entity target) {
		this.lookAt(EntityAnchorArgument.Anchor.FEET, target.position());
		this.setStartRun(true);
		EntityUtil.playSound(this, PvZSoundRegister.LAWN_MOWER.get());
	}

	public void setPlacer(Player player) {
		this.setOwner(player);
		this.setYRot(player.getDirection().toYRot());
	}

	@Override
	public void lerpMotion(Vec3 movement) {
		double x = movement.x;
		double y = movement.y;
		double z = movement.z;
		this.setDeltaMovement(x, y, z);
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			float f = Mth.sqrt((float) (x * x + z * z));
			this.setYRot((float) (Mth.atan2(x, z) * (180F / (float) Math.PI)));
			this.setXRot((float) (Mth.atan2(y, f) * (180F / (float) Math.PI)));
			this.yRotO = this.getYRot();
			this.xRotO = this.getXRot();
			this.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
		}
	}

	@Override
	public float maxUpStep() {
		return 0.5F;
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 0.8F);
	}



	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setStartRun(input.getBooleanOr("start_running", this.isStartRun()));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("start_running", this.isStartRun());
	}

	public void setStartRun(boolean is) {
		this.entityData.set(START_RUN, is);
	}

	public boolean isStartRun() {
		return this.entityData.get(START_RUN);
	}
}