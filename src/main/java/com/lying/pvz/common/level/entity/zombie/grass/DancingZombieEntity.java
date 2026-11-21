package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.WorldUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * 舞王僵尸。
 *
 * <p>核心机制：
 * <ul>
 *   <li><b>召唤伴舞</b>：{@code SummonDancerGoal} 在 {@code restTick > 0} 且有
 *       空位时 5% 概率触发，{@code summonTime} 归零时 {@link #summonEmptyDancers()}
 *       在四个象限偏移位置各召唤一只 {@link BackupDancerEntity}，最多
 *       {@link #getMaxSummonCnt()} 只；</li>
 *   <li><b>跳舞</b>：{@code ZombieDanceGoal} 驱动 {@code attackTime} 计数，
 *       舞蹈期间移动速度置 0，结束后随机 60~300 tick 的休息期；</li>
 *   <li><b>伴舞跟随</b>：伴舞在 {@link BackupDancerEntity} 侧读取 {@code dancing_owner}，
 *       每 tick 同步朝向与攻击时间，实现“同一节奏”。</li>
 * </ul>
 *
 * <p>本体 50 血，护甲韧性 10；行走 {@link ZombieUtil#WALK_LITTLE_SLOW}。
 * 同时锁定 {@code canCollideWithZombie = false}，避免与伴舞互相推挤。
 */
public class DancingZombieEntity extends ZombieEntity {

	private static final EntityDataAccessor<Integer> SUMMON_TIME =
			SynchedEntityData.defineId(DancingZombieEntity.class, EntityDataSerializers.INT);
	public static final int MAX_DANCER_NUM = 4;
	private static final float[][] POS_OFFSET = new float[][]{{2, 0}, {-2, 0}, {0, 2}, {0, -2}};
	public static final int SUMMON_CD = 10;
	public static final int DANCE_CD = 100;
	private static final int MIN_REST_CD = 60;
	private static final int MAX_REST_CD = 300;
	private final List<Optional<BackupDancerEntity>> Dancers = new ArrayList<>(MAX_DANCER_NUM);
	private int summonCnt = 0;
	private int restTick = 0;

	public DancingZombieEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canCollideWithZombie = false;
		this.setRestTick();
		for (int i = 0; i < MAX_DANCER_NUM; ++i) {
			this.Dancers.add(Optional.empty());
		}
		this.clearDancers();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SUMMON_TIME, 0);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new ZombieDanceGoal(this));
		this.goalSelector.addGoal(0, new SummonDancerGoal(this));
	}

	public void tickDance() {
		for (int i = 0; i < MAX_DANCER_NUM; ++i) {
			this.Dancers.get(i).ifPresent(dancer -> {
			});
		}
	}

	public void summonEmptyDancers() {
		for (int i = 0; i < MAX_DANCER_NUM; ++i) {
			if (!this.Dancers.get(i).isPresent() && this.summonCnt < this.getMaxSummonCnt()) {
				BackupDancerEntity dancer = PvZEntityRegister.BACKUP_DANCER.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
				if (dancer == null) {
					return;
				}
				BlockPos pos = WorldUtil.getSuitableHeightPos(this.level(),
						this.blockPosition().offset((int) POS_OFFSET[i][0], 0, (int) POS_OFFSET[i][1]));
				dancer.setDancingOwner(this);
				dancer.setZombieRising();
				ZombieUtil.copySummonZombieData(this, dancer);
				this.setDancer(i, dancer);
				++this.summonCnt;
				EntityUtil.onEntitySpawn(this.level(), dancer, pos);
				this.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 40, 5, false, false));
			}
		}
	}

	protected void setRestTick() {
		this.restTick = MathUtil.getRandomMinMax(this.getRandom(), MIN_REST_CD, MAX_REST_CD);
	}

	protected void updateSpeed() {
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(this.getAttackTime() > 0 ? 0 : this.getWalkSpeed());
		}
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_LITTLE_SLOW;
	}

	protected boolean hasEmptyPlace() {
		for (int i = 0; i < MAX_DANCER_NUM; ++i) {
			if (!this.Dancers.get(i).isPresent()) {
				return true;
			} else {
				if (!EntityUtil.isEntityValid(this.Dancers.get(i).get())) {
					this.Dancers.set(i, Optional.empty());
					return true;
				}
			}
		}
		return false;
	}

	private void clearDancers() {
		for (int i = 0; i < MAX_DANCER_NUM; ++i) {
			this.Dancers.set(i, Optional.empty());
		}
	}

	private void setDancer(int pos, Entity entity) {
		if (EntityUtil.isEntityValid(entity) && entity instanceof BackupDancerEntity) {
			this.Dancers.set(pos, Optional.ofNullable((BackupDancerEntity) entity));
		}
	}

	public boolean isDancing() {
		return this.getAttackTime() > 0;
	}

	public int getMaxSummonCnt() {
		return 20;
	}

	@Override
	public float getLife() {
		return 50;
	}

	@Override
	public float getArmorToughness() {
		return 10;
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setSummonTime(input.getIntOr("zombie_summon_tick", 0));
		this.restTick = input.getIntOr("zombie_rest_tick", 0);
		input.read("dancer_ids", CompoundTagCodecsHolder.DANCER_IDS).ifPresent(nbt -> {
			for (int i = 0; i < MAX_DANCER_NUM; ++i) {
				final String key = "dancer_" + i;
				if (nbt.contains(key)) {
					this.setDancer(i, this.level().getEntity(nbt.getIntOr(key, 0)));
				}
			}
		});
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("zombie_summon_tick", this.getSummonTime());
		output.putInt("zombie_rest_tick", this.restTick);
		net.minecraft.nbt.CompoundTag nbt = new net.minecraft.nbt.CompoundTag();
		for (int i = 0; i < MAX_DANCER_NUM; ++i) {
			if (!this.Dancers.get(i).isPresent()) {
				continue;
			}
			BackupDancerEntity dancer = this.Dancers.get(i).get();
			if (EntityUtil.isEntityValid(dancer)) {
				nbt.putInt("dancer_" + i, dancer.getId());
			}
		}
		output.store("dancer_ids", net.minecraft.nbt.CompoundTag.CODEC, nbt);
	}

	public int getSummonTime() {
		return this.entityData.get(SUMMON_TIME);
	}

	public void setSummonTime(int time) {
		this.entityData.set(SUMMON_TIME, time);
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.DANCING_ZOMBIE.get();
	}

	static class ZombieDanceGoal extends Goal {

		private final DancingZombieEntity dancer;

		public ZombieDanceGoal(DancingZombieEntity dancer) {
			this.dancer = dancer;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		@Override
		public boolean canUse() {
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
			this.dancer.tickDance();
			this.dancer.setAttackTime(Math.max(0, tick - 1));
		}

		@Override
		public void stop() {
			this.dancer.updateSpeed();
		}
	}

	static class SummonDancerGoal extends Goal {

		private final DancingZombieEntity dancer;

		public SummonDancerGoal(DancingZombieEntity dancer) {
			this.dancer = dancer;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			if (this.dancer.summonCnt >= this.dancer.getMaxSummonCnt()) {
				return false;
			}
			if (this.dancer.getSummonTime() > 0) {
				return true;
			}
			if (this.dancer.restTick > 0 && this.dancer.hasEmptyPlace()
					&& this.dancer.getRandom().nextFloat() < 0.05F) {
				this.dancer.setSummonTime(SUMMON_CD);
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
			return this.dancer.getSummonTime() > 0;
		}

		@Override
		public void tick() {
			int tick = this.dancer.getSummonTime();
			if (tick == 5) {
				this.dancer.summonEmptyDancers();
			}
			this.dancer.setSummonTime(Math.max(0, tick - 1));
		}

		@Override
		public void stop() {
			this.dancer.updateSpeed();
		}
	}

	private static final class CompoundTagCodecsHolder {
		private static final com.mojang.serialization.Codec<net.minecraft.nbt.CompoundTag> DANCER_IDS =
				net.minecraft.nbt.CompoundTag.CODEC;
	}
}