package com.lying.pvz.common.level.entity.misc;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.entity.AbstractOwnerEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.fire.JalapenoEntity;
import com.lying.pvz.common.level.entity.plant.ice.IceShroomEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.item.card.PlantCardItem;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.PlayerUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 元素球：由玩家发射的冰 / 火抛射物，可直接命中、也可自动追踪。
 * 冰克火、火克冰，命中对应克制植物时不造成伤害；命中僵尸或生物时按最大血量造成伤害。
 */
public class ElementBallEntity extends AbstractOwnerEntity {

	private static final EntityDataAccessor<Integer> ELEMENTS =
			SynchedEntityData.defineId(ElementBallEntity.class, EntityDataSerializers.INT);
	private static final int SEARCH_CD = 20;
	private static final float SEARCH_RANGE = 50;

	protected Entity target;
	protected boolean isAutoBall = false;
	protected float speed = 0.25F;
	private int removeTick = 0;

	public ElementBallEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ELEMENTS, ElementTypes.FLAME.ordinal());
	}

	public static void killElementBalls(LivingEntity attacker, float range, ElementTypes type) {
		attacker.level().getEntitiesOfClass(ElementBallEntity.class,
						EntityUtil.getEntityAABB(attacker, range, range),
						ball -> ball.getElementBallType() == type
								&& EntityUtil.checkCanEntityBeAttack(attacker, ball))
				.forEach(ball -> ball.onKilledByPlants(attacker));
	}

	public void onKilledByPlants(LivingEntity entity) {
		if (entity instanceof PlantEntity plant) {
			plant.getOwnerUUID().ifPresent(uuid -> {
				Player player = this.level().getPlayerByUUID(uuid);
				if (player != null) {
					PlantCardItem item = (this.getElementBallType() == ElementTypes.FLAME
							? PvZItemRegister.ICE_SHROOM_CARD.get()
							: PvZItemRegister.JALAPENO_CARD.get());
					PlayerUtil.setPAZCardCD(player, item.getType(), 160);
				}
			});
		}
		this.discard();
	}

	@Override
	public void tick() {
		this.noPhysics = true;
		super.tick();
		this.tickMove();
		if (!this.level().isClientSide()) {
			this.tickCollision();
			if (this.tickCount >= PvZConfig.LOCAL_CONFIG.EntitySettings.EntityLiveTick.ElementBallLiveTick.get()) {
				this.discard();
				return;
			}
			if (this.isAutoBall) {
				if (EntityUtil.isEntityValid(this.target)) {
					this.setDeltaMovement(this.target.position().subtract(this.position()).normalize().scale(this.speed));
				} else {
					this.findTarget();
				}
			}
			if (this.getOwner() == null) {
				++this.removeTick;
				if (this.removeTick >= 15) {
					this.discard();
				}
			} else {
				this.removeTick = 0;
			}
		}
	}

	public void shoot(LivingEntity target) {
		if (target != null) {
			if (this.isAutoBall) {
				this.target = target;
			} else {
				this.setDeltaMovement(target.position().subtract(this.position()).normalize().scale(this.speed));
			}
		} else if (this.getOwner() != null) {
			this.setDeltaMovement(this.getOwner().getLookAngle().normalize().scale(this.speed));
		}
	}

	@Override
	protected void tickMove() {
		if (!this.canSimulateMovement()) {
			return;
		}
		Vec3 motion = this.getDeltaMovement();
		this.setDeltaMovement(motion.scale(this.isAutoBall ? 0.9 : 1));
		this.move(MoverType.SELF, this.getDeltaMovement());
	}

	private void findTarget() {
		if (this.random.nextInt(SEARCH_CD) == 0) {
			final List<LivingEntity> list = EntityUtil
					.getTargetableLivings(this, EntityUtil.getEntityAABB(this, SEARCH_RANGE, SEARCH_RANGE))
					.stream().collect(Collectors.toList());
			if (!list.isEmpty()) {
				this.target = list.get(0);
			}
		}
	}

	private void tickCollision() {
		if (this.level() instanceof ServerLevel serverLevel && this.tickCount % 10 == 0) {
			EntityUtil.getTargetableEntities(this, this.getBoundingBox().inflate(1F)).forEach(target -> {
				if (target instanceof PlantEntity plant) {
					if (target instanceof JalapenoEntity && this.getElementBallType() == ElementTypes.ICE) {
						return;
					}
					if (target instanceof IceShroomEntity && this.getElementBallType() == ElementTypes.FLAME) {
						return;
					}
					target.hurtServer(serverLevel, this.getAttackSource(),
							EntityUtil.getCurrentMaxHealth(plant));
				} else if (target instanceof PVZZombieEntity zombie) {
					target.hurtServer(serverLevel, this.getAttackSource(),
							EntityUtil.getCurrentMaxHealth(zombie));
				} else {
					target.hurtServer(serverLevel, this.getAttackSource(), 5);
					target.setDeltaMovement(target.position().subtract(this.position())
							.normalize().scale(this.speed));
				}
			});
		}
	}

	private PvZEntityDamageSource getAttackSource() {
		if (this.getElementBallType() == ElementTypes.FLAME) {
			return PvZEntityDamageSource.causeFlameDamage(this, this.getOwner());
		}
		return PvZEntityDamageSource.causeIceDamage(this, this.getOwner());
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setAuto(boolean auto) {
		this.isAutoBall = auto;
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return EntityDimensions.scalable(3F, 3F);
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);

		input.getInt("element_ball_type").ifPresent(ord -> {
			ElementTypes[] types = ElementTypes.values();
			if (ord >= 0 && ord < types.length) {
				this.setElementBallType(types[ord]);
			}
		});
		input.getInt("element_target").ifPresent(id -> this.target = this.level().getEntity(id));
		this.speed = input.getFloatOr("element_speed", this.speed);
		this.isAutoBall = input.getBooleanOr("auto_ball", this.isAutoBall);
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);

		output.putInt("element_ball_type", this.getElementBallType().ordinal());
		if (this.target != null) {
			output.putInt("element_target", this.target.getId());
		}
		output.putFloat("element_speed", this.speed);
		output.putBoolean("auto_ball", this.isAutoBall);
	}

	public void setElementBallType(ElementTypes type) {
		this.entityData.set(ELEMENTS, type.ordinal());
	}

	public ElementTypes getElementBallType() {
		return ElementTypes.values()[this.entityData.get(ELEMENTS)];
	}

	public enum ElementTypes {
		FLAME,
		ICE,
	}
}