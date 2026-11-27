package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.EnumSet;
import java.util.Optional;

/**
 * 伴舞僵尸。
 *
 * <p>由 {@link DancingZombieEntity} 召唤，通过 {@link #setDancingOwner} 记录宿主。
 * 行为分两种：
 * <ul>
 *   <li>宿主有效（{@link #needFollow()} 为 true）：每 tick 同步宿主的
 *       {@code attackTime} 与朝向，自身不跳舞；</li>
 *   <li>宿主失效：进入独立的跳舞循环（{@code ZombieDanceGoal}），与宿主节奏解耦。</li>
 * </ul>
 *
 * <p>本体 20 血。移动速度在跳舞时为 0，否则 {@link ZombieUtil#WALK_LITTLE_SLOW}。
 * 与宿主一致，{@code canCollideWithZombie = false}，避免舞群互推。
 */
public class BackupDancerEntity extends PVZZombieEntity {

	public static final int DANCE_CD = DancingZombieEntity.DANCE_CD;
	protected Optional<DancingZombieEntity> owner = Optional.empty();
	private static final int MIN_REST_CD = 60;
	private static final int MAX_REST_CD = 300;
	private int restTick = 0;

	public BackupDancerEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canCollideWithZombie = false;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new ZombieDanceGoal(this));
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			this.updateSpeed();
			if (this.needFollow()) {
				this.owner.ifPresent(dancer -> {
					this.setAttackTime(dancer.getAttackTime());
					this.setYRot(dancer.getYRot());
					this.setXRot(dancer.getXRot());
					this.setYHeadRot(dancer.getYHeadRot());
				});
			}
		}
	}

	@Override
	public float getLife() {
		return 20;
	}

	public boolean needFollow() {
		return this.owner.isPresent() && EntityUtil.isEntityValid(this.owner.get());
	}

	public void setDancingOwner(DancingZombieEntity dancer) {
		this.owner = Optional.ofNullable(dancer);
	}

	protected void setRestTick() {
		this.restTick = MathUtil.getRandomMinMax(this.getRandom(), MIN_REST_CD, MAX_REST_CD);
	}

	protected void updateSpeed() {
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(this.getAttackTime() > 0 ? 0 : ZombieUtil.WALK_LITTLE_SLOW);
		}
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.restTick = input.getIntOr("zombie_rest_tick", 0);
		final int ownerId = input.getIntOr("dancing_owner", 0);
		if (ownerId != 0) {
			Entity entity = this.level().getEntity(ownerId);
			if (EntityUtil.isEntityValid(entity) && entity instanceof DancingZombieEntity) {
				this.owner = Optional.of((DancingZombieEntity) entity);
			}
		}
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("zombie_rest_tick", this.restTick);
		this.owner.ifPresent(dancer -> output.putInt("dancing_owner", dancer.getId()));
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.BACKUP_DANCER.get();
	}

	static class ZombieDanceGoal extends Goal {

		private final BackupDancerEntity dancer;

		public ZombieDanceGoal(BackupDancerEntity dancer) {
			this.dancer = dancer;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			if (this.dancer.needFollow()) {
				return false;
			}
			if (this.dancer.getAttackTime() > 0) {
				return true;
			}
			if (this.dancer.restTick > 0) {
				--this.dancer.restTick;
				return false;
			}
			if (this.dancer.getRandom().nextFloat() < 0.05F) {
				this.dancer.setAttackTime(DANCE_CD);
				return true;
			}
			return false;
		}

		@Override
		public void start() {
			this.dancer.updateSpeed();
		}

		@Override
		public boolean canContinueToUse() {
			return this.dancer.getAttackTime() > 0;
		}

		@Override
		public void tick() {
			int tick = this.dancer.getAttackTime();
			if (tick == 1) {
				this.dancer.setRestTick();
			}
			this.dancer.setAttackTime(Math.max(0, tick - 1));
		}

		@Override
		public void stop() {
			this.dancer.updateSpeed();
		}
	}
}