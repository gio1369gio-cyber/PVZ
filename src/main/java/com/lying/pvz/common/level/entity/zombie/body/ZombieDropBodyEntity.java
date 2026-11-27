package com.lying.pvz.common.level.entity.zombie.body;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.api.entity.ability.BodyType;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.level.entity.PVZBaseEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * 僵尸掉落身体部件实体。
 *
 * <p>由 {@link PVZZombieEntity} 在断肢（{@code onLostPart}）、死亡（{@code onFallBody}）
 * 时生成，本身不参与 AI：服务端 tick 只处理重力、地面摩擦与存活计时，
 * {@code max_exist_tick} 到后 {@link #discard()}。
 *
 * <p>渲染由 {@code IZombieModel#tickPartAnim / renderBody} 通过 {@link IBodyEntity}
 * 完成，本类不持有模型。
 *
 * <p><b>类型</b>：{@link #getZombieType()} 读取 {@code ZOMBIE_TYPE}（Identifier 字符串），
 * 解析失败回退 {@link GrassZombies#NORMAL_ZOMBIE}。{@code BODY_TYPE} 存
 * {@link BodyType#ordinal()}，读取时做越界保护。
 *
 * <p><b>状态</b>：{@code BODY_STATE} 位掩码，bit {@code HAS_HAND_DEFENCE} 表示
 * 掉落时手持防御，bit {@code MINI_BODY} 表示迷你部件。
 */
public class ZombieDropBodyEntity extends PVZBaseEntity implements IBodyEntity {

	private static final EntityDataAccessor<String> ZOMBIE_TYPE =
			SynchedEntityData.defineId(ZombieDropBodyEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> BODY_TYPE =
			SynchedEntityData.defineId(ZombieDropBodyEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> BODY_STATE =
			SynchedEntityData.defineId(ZombieDropBodyEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ANIM_TIME =
			SynchedEntityData.defineId(ZombieDropBodyEntity.class, EntityDataSerializers.INT);
	public static final int MAX_EXIST_TICK = 60;
	private static final int HAS_HAND_DEFENCE = 0;
	private static final int MINI_BODY = 1;
	public final int HEAD_ROT;
	private int max_exist_tick;
	private float friction = 0.3F;

	public ZombieDropBodyEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
		HEAD_ROT = this.random.nextInt(60) - 30;
		this.max_exist_tick = MAX_EXIST_TICK;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ZOMBIE_TYPE, "");
		builder.define(BODY_TYPE, BodyType.HAND.ordinal());
		builder.define(BODY_STATE, 0);
		builder.define(ANIM_TIME, 0);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickMove();
		if (!this.level().isClientSide()) {
			if (this.getAnimTime() >= this.max_exist_tick) {
				this.discard();
			} else {
				this.setAnimTime(this.getAnimTime() + 1);
			}
			if (this.onGround()) {
				this.setDeltaMovement(this.getDeltaMovement().scale(this.friction));
			}
		}
	}

	public void droppedByOwner(PVZZombieEntity zombie, DamageSource source, BodyType type) {
		this.updateInfo(zombie, type);
		switch (type) {
			case HAND -> {
				final float j = 2 * 3.14159f * this.getYRot() / 360;
				final float dis = 0.6F;
				this.setPos(zombie.position().x - Math.sin(j) * dis,
						zombie.position().y + zombie.getEyeHeight(),
						zombie.position().z + Math.cos(j) * dis);
			}
			case HEAD -> this.hitUp(zombie, source, 0.3D);
			case BODY -> {
				this.setPos(zombie.position().x, zombie.position().y, zombie.position().z);
				this.setDeltaMovement(zombie.getDeltaMovement());
			}
			default -> {
			}
		}
	}

	public void specialDropBody(PVZZombieEntity zombie, DamageSource source, BodyType type) {
		this.updateInfo(zombie, type);
		final IZombieType zombieType = zombie.getZombieType();
		if (zombieType == PoolZombies.ZOMBONI.get() || zombieType == RoofZombies.CATAPULT_ZOMBIE.get()) {
			this.hitUp(zombie, source, 0.5D, 0.5D, 0.5D);
		}
	}

	private void hitUp(PVZZombieEntity zombie, DamageSource source, double speed) {
		this.hitUp(zombie, source, speed, speed, speed);
	}

	private void hitUp(PVZZombieEntity zombie, DamageSource source, double speed, double speedH, double speedV) {
		this.setPos(zombie.position().x, zombie.position().y + zombie.getEyeHeight(), zombie.position().z);
		final double speedX = (this.random.nextDouble() - 0.5D) * speedH;
		final double speedZ = (this.random.nextDouble() - 0.5D) * speedH;
		final double speedY = this.random.nextDouble() * speedV;
		Optional.ofNullable(source.getSourcePosition()).ifPresent(vec -> {
			final Vec3 v = this.position().subtract(vec);
			this.setDeltaMovement(v.normalize().scale(speed).add(speedX, speedY, speedZ));
		});
	}

	public void updateInfo(PVZZombieEntity zombie, BodyType type) {
		this.setZombieType(zombie.getZombieType());
		this.setMini(zombie.isMiniZombie());
		this.setBodyType(type);
	}


	@Override
	public void lerpMotion(Vec3 movement) {
		this.setDeltaMovement(movement);
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			final float f = Mth.sqrt((float) (movement.x * movement.x + movement.z * movement.z));
			final float newXRot = (float) (Mth.atan2(movement.y, f) * (double) (180F / (float) Math.PI));
			final float newYRot = (float) (Mth.atan2(movement.x, movement.z) * (double) (180F / (float) Math.PI));
			this.setXRot(newXRot);
			this.setYRot(newYRot);
			this.xRotO = newXRot;
			this.yRotO = newYRot;
			this.snapTo(this.getX(), this.getY(), this.getZ(), newYRot, newXRot);
		}
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return EntityDimensions.scalable(0.5F, 0.5F);
	}

	public void setMaxLiveTick(int tick) {
		this.max_exist_tick = tick;
	}

	public void setFriction(float f) {
		this.friction = f;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		this.setAnimTime(input.getIntOr("body_anim_tick", 0));
		final String typeId = input.getStringOr("body_zombie_type", "");
		if (!typeId.isEmpty()) {
			this.entityData.set(ZOMBIE_TYPE, typeId);
		}
		this.setBodyState(input.getIntOr("body_part_state", 0));
		final BodyType[] bodyTypes = BodyType.values();
		final int ord = input.getIntOr("body_part_type", BodyType.HAND.ordinal());
		this.setBodyType((ord >= 0 && ord < bodyTypes.length) ? bodyTypes[ord] : BodyType.HAND);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		output.putInt("body_anim_tick", this.getAnimTime());
		output.putString("body_zombie_type", this.getZombieType().getID().toString());
		output.putInt("body_part_state", this.getBodyState());
		output.putInt("body_part_type", this.getBodyType().ordinal());
	}

	@Override
	public IZombieType getZombieType() {
		final String id = this.entityData.get(ZOMBIE_TYPE);
		if (id.isEmpty()) {
			return GrassZombies.NORMAL_ZOMBIE.get();
		}
		final Identifier identifier = Identifier.tryParse(id);
		if (identifier == null) {
			return GrassZombies.NORMAL_ZOMBIE.get();
		}
		return PVZAPI.lookup().zombie(identifier).orElse(GrassZombies.NORMAL_ZOMBIE.get());
	}

	public void setZombieType(IZombieType type) {
		this.entityData.set(ZOMBIE_TYPE, type == null ? "" : type.getID().toString());
	}

	@Override
	public BodyType getBodyType() {
		final BodyType[] values = BodyType.values();
		final int ord = this.entityData.get(BODY_TYPE);
		return (ord >= 0 && ord < values.length) ? values[ord] : BodyType.HAND;
	}

	public void setBodyType(BodyType type) {
		this.entityData.set(BODY_TYPE, type.ordinal());
	}

	public int getBodyState() {
		return this.entityData.get(BODY_STATE);
	}

	public void setBodyState(int state) {
		this.entityData.set(BODY_STATE, state);
	}

	@Override
	public int getAnimTime() {
		return this.entityData.get(ANIM_TIME);
	}

	public void setAnimTime(int tick) {
		this.entityData.set(ANIM_TIME, tick);
	}

	public void setHandDefence(boolean flag) {
		final int mask = 1 << HAS_HAND_DEFENCE;
		this.setBodyState(flag ? (this.getBodyState() | mask) : (this.getBodyState() & ~mask));
	}

	@Override
	public boolean hasHandDefence() {
		return (this.getBodyState() & (1 << HAS_HAND_DEFENCE)) != 0;
	}

	public void setMini(boolean flag) {
		final int mask = 1 << MINI_BODY;
		this.setBodyState(flag ? (this.getBodyState() | mask) : (this.getBodyState() & ~mask));
	}

	@Override
	public boolean isMini() {
		return (this.getBodyState() & (1 << MINI_BODY)) != 0;
	}
}