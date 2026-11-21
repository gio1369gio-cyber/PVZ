package com.lying.pvz.common.level.entity.misc;

import java.util.List;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.AbstractOwnerEntity;
import com.lying.pvz.utils.EntityUtil;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * 花园耙：放置后原地守候，探测到可攻击目标后播放挥击动画并对范围内目标造成重击，随后消失。
 * 玩家空手右键可以回收成物品。
 */
public class GardenRakeEntity extends AbstractOwnerEntity {

	private static final EntityDataAccessor<Integer> ATTACK_TIME =
			SynchedEntityData.defineId(GardenRakeEntity.class, EntityDataSerializers.INT);

	public GardenRakeEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
		this.setDeltaMovement(Vec3.ZERO);
	}

	public GardenRakeEntity(Level worldIn, LivingEntity livingEntityIn) {
		super(PvZEntityRegister.GARDEN_RAKE.get(), worldIn, livingEntityIn);
		this.setYRot(livingEntityIn.getDirection().toYRot());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ATTACK_TIME, 0);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickMove();
		if (!this.level().isClientSide()) {
			this.stopVerticalVelocityIfOnGround();

			if (this.isStartAttack()) {
				this.setAttackTime(this.getAttackTime() + 1);
				if (this.getAttackTime() >= this.getAnimTime()) {
					this.dealDamage();
				}
			} else {
				List<Entity> list = this.level().getEntitiesOfClass(Entity.class,
						this.getBoundingBox().inflate(0.2D),
						target -> EntityUtil.canTargetEntity(this, target));
				if (!list.isEmpty()) {
					this.onStartAttack();
				}
			}
		}
	}
	private void stopVerticalVelocityIfOnGround() {
		if (this.onGround() && this.getDeltaMovement().y < 0) {
			Vec3 v = this.getDeltaMovement();
			this.setDeltaMovement(v.x, 0, v.z);
		}
	}
	private void dealDamage() {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		this.level().getEntitiesOfClass(Entity.class,
						this.getBoundingBox().inflate(0.25D),
						target -> EntityUtil.canTargetEntity(this, target))
				.forEach(target -> target.hurtServer(serverLevel,
						PvZEntityDamageSource.normal(this), 180F));
		EntityUtil.playSound(this, PvZSoundRegister.SWING.get());
		this.discard();
	}

	protected void onStartAttack() {
		this.setAttackTime(1);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		if (!this.isStartAttack()
				&& hand == InteractionHand.MAIN_HAND
				&& player.getMainHandItem().isEmpty()) {
			if (!this.level().isClientSide()) {
				player.addItem(new ItemStack(PvZItemRegister.GARDEN_RAKE.get()));
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

	public boolean isStartAttack() {
		return this.getAttackTime() > 0;
	}

	public int getAnimTime() {
		return 10;
	}

	public void setPlacer(Player player) {
		this.setOwner(player);
		this.setYRot(player.getDirection().toYRot());
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.9F, 0.8F);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		input.getInt("rake_attack_time").ifPresent(this::setAttackTime);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("rake_attack_time", this.getAttackTime());
	}

	public int getAttackTime() {
		return this.entityData.get(ATTACK_TIME);
	}

	public void setAttackTime(int cd) {
		this.entityData.set(ATTACK_TIME, cd);
	}
}