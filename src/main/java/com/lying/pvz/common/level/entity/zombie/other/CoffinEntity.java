package com.lying.pvz.common.level.entity.zombie.other;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.CustomZombies;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.misc.ZombieHandEntity;
import com.lying.pvz.common.level.entity.zombie.base.AbstractBossZombieEntity;
import com.lying.pvz.common.attachment.challenge.RaiderData;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

/**
 * 棺材僵尸。
 *
 * <p>BOSS 型召唤载体，本体 1000 血，碰撞箱 2×2，行走 {@link ZombieUtil#WALK_SLOW}、
 * 攻击 {@link ZombieUtil#LITTLE_LOW}。核心机制为“护柩者阶段”：
 * <ul>
 *   <li>{@link #zombieTick()} 每 tick 根据 {@link #bossInfo} 进度检查 4 名护柩者，
 *       当进度跌破 {@code (5-i)/6} 阈值时置位对应 bit 并召唤 {@link MournerZombieEntity}；</li>
 *   <li>{@link #isGuardGone(int)} / {@link #setGuardStateById(int, int)} 用
 *       {@code GUARD_STATE} 位掩码记录 4 名护柩者的出场状态；</li>
 *   <li>死亡时（{@link #onRemoveWhenDeath()}）召唤 {@link NobleZombieEntity} 并
 *       继承 {@link RaiderData#setChallengeID} 的挑战归属。</li>
 * </ul>
 *
 * <p>生成时在 {@link #finalizeSpawn} 中播放 {@link PvZSoundRegister#DIRT_RISE} 并
 * 生成一圈僵尸手。{@link #getDeathTime()} 为 1 tick，死亡即移除，避免长时间尸体残留。
 */
public class CoffinEntity extends AbstractBossZombieEntity {

	private static final EntityDataAccessor<Integer> GUARD_STATE = SynchedEntityData.defineId(CoffinEntity.class, EntityDataSerializers.INT);

	public CoffinEntity(EntityType<? extends AbstractBossZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setIsWholeBody();
		this.kickRange = 3;
	}

	@Override
	protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(GUARD_STATE, 0);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
		if (attackDamage != null) {
			attackDamage.setBaseValue(ZombieUtil.LITTLE_LOW);
		}
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_SLOW);
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn,
										EntitySpawnReason reason, @Nullable SpawnGroupData spawnDataIn) {
		if (!this.level().isClientSide()) {
			EntityUtil.playSound(this, PvZSoundRegister.DIRT_RISE.get());
			ZombieHandEntity.spawnRangeZombieHands(this.level(), this, 3);
		}
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
	}

	@Override
	public void zombieTick() {
		super.zombieTick();
		if (!this.level().isClientSide() && this.isAlive()) {
			final float progress = this.bossInfo.getProgress();
			for (int i = 0; i < 4; i++) {
				if (this.isGuardGone(i)) {
					continue;
				}
				if (progress < (5 - i) * 1.0f / 6f) {
					this.setGuardStateById(i, 1);
					MournerZombieEntity zombie = PvZEntityRegister.MOURNER_ZOMBIE.get().create(this.level(),EntitySpawnReason.MOB_SUMMONED);
					if (zombie == null) {
						return;
					}
					zombie.setRightShake((i & 1) == 0);
					this.onBossSummon(zombie,
							this.getTarget() == null ? this.blockPosition() : this.getTarget().blockPosition());
				}
			}
		}
	}

	@Override
	protected void onRemoveWhenDeath() {
		if (!this.level().isClientSide()) {
			NobleZombieEntity boss = PvZEntityRegister.NOBLE_ZOMBIE.get().create(this.level(),EntitySpawnReason.MOB_SUMMONED);
			if (boss == null) {
				return;
			}
			this.onBossSummon(boss, this.blockPosition().above());
			RaiderData x = this.getData(PvZAttachments.CHALLENGE_DATA);
			RaiderData y = boss.getData(PvZAttachments.CHALLENGE_DATA);
			y.setChallengeID(x.getChallengeID());
		}
	}

	@Override
	protected int getDeathTime() {
		return 1;
	}

	@Override
	protected double getCollideWidthOffset() {
		return 0.5;
	}

	@Override
	public float getLife() {
		return 1000;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(2F, 2F);
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setGuardState(input.getIntOr("guard_state", 0));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("guard_state", this.getGuardState());
	}

	public int getGuardState() {
		return this.entityData.get(GUARD_STATE);
	}

	public void setGuardState(int state) {
		this.entityData.set(GUARD_STATE, state);
	}

	public boolean isGuardGone(int id) {
		return ((this.getGuardState() >> id) & 1) == 1;
	}

	public void setGuardStateById(int id, int is) {
		int state = this.getGuardState();
		if (((is ^ (state >> id)) & 1) == 1) {
			state ^= (1 << id);
		}
		this.entityData.set(GUARD_STATE, state);
	}


	@Override
	public IZombieType getZombieType() {
		return CustomZombies.COFFIN.get();
	}
}