package com.lying.pvz.common.level.entity.zombie.other;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.CustomZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.misc.ZombieHandEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.base.AbstractBossZombieEntity;
import com.lying.pvz.common.misc.PvZLoot;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.WorldUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 贵族僵尸（棺材僵尸的最终形态）。
 *
 * <p>BOSS 型召唤者，本体 1000 血 + 内层 1000 血，行走 {@link ZombieUtil#WALK_VERY_SLOW}、
 * 攻击 {@link ZombieUtil#LITTLE_HIGH}。核心机制：
 * <ul>
 *   <li><b>召唤哀悼者</b>：{@code summonTick} 每轮随机 300~600 tick，
 *       到 0 时 {@link #checkAndSummonMournerZombie()} 按附近僵尸数比例召唤；
 *       达到 {@code maxZombieSurround} 上限则改为召唤僵尸手；</li>
 *   <li><b>睡眠攻击</b>：{@code attackTime} 每轮随机 360~1000 tick，
 *       到 0 时 {@link #checkAndSleepPlant()} 让 50 格内随机一棵植物周围 3 格内的植物
 *       全部 {@code sleepTime = 2400}；</li>
 *   <li><b>传送</b>：{@code TP_TICK} 按 {@link #getTpCD()} 计时（血量越低越快），
 *       到点后 {@link #teleportToPos} 向上寻找可站立点并 {@code randomTeleport}；</li>
 *   <li><b>减伤</b>：{@link #hurtServer} 按伤害量分段衰减（≥10 伤害 → 1/3，否则 1/2）。</li>
 * </ul>
 *
 * <p>{@link #checkAndHeal(float)} 在血量低于 1/2 或 1/6 时持续小量自愈。
 * 生成时播放 {@link PvZSoundRegister#DIRT_RISE} 并生成 6 只僵尸手，死亡掉落表沿用
 * {@link PvZLoot#NOBLE_ZOMBIE}。
 */
public class NobleZombieEntity extends AbstractBossZombieEntity {

	private static final EntityDataAccessor<Integer> TP_TICK =
			SynchedEntityData.defineId(NobleZombieEntity.class, EntityDataSerializers.INT);
	private int summonTick;
	private final int minSummonTick = 300;
	private final int maxSummonTick = 600;
	private final int minTpCD = 400;
	private final int maxTpCD = 850;
	private final int minSleepAttackCD = 360;
	private final int maxSleepAttackCD = 1000;

	public NobleZombieEntity(EntityType<? extends AbstractBossZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setAttackTime(this.maxSleepAttackCD / 5);
		this.summonTick = this.maxSummonTick / 3;
		this.setTpTick(-this.maxTpCD / 2);
		this.xpReward = 1000;
		this.setIsWholeBody();
		this.canBeFrozen = true;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(TP_TICK, 0);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_VERY_SLOW);
		}
		AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
		if (attackDamage != null) {
			attackDamage.setBaseValue(ZombieUtil.LITTLE_HIGH);
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn,
										EntitySpawnReason reason, @Nullable SpawnGroupData spawnDataIn) {
		if (!this.level().isClientSide()) {
			EntityUtil.playSound(this, PvZSoundRegister.DIRT_RISE.get());
			ZombieHandEntity.spawnRangeZombieHands(this.level(), this, 6);
		}
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
	}

	@Override
	public void zombieTick() {
		super.zombieTick();
		if (!this.level().isClientSide() && !this.canNormalUpdate()) {
			this.setTpTick(-this.maxTpCD);
		}
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			if (this.summonTick > 0) {
				--this.summonTick;
			} else {
				this.summonTick = MathUtil.getRandomMinMax(this.getRandom(), this.minSummonTick, this.maxSummonTick);
				this.checkAndSummonMournerZombie();
			}
			if (this.getAttackTime() > 0) {
				this.setAttackTime(this.getAttackTime() - 1);
			} else {
				this.setAttackTime(MathUtil.getRandomMinMax(this.getRandom(),
						this.minSleepAttackCD, this.maxSleepAttackCD));
				this.checkAndSleepPlant();
			}
		} else {
			if (this.getAttackTime() < 20) {
				this.level().addParticle(ParticleTypes.NOTE, this.getX(), this.getY() + 2f, this.getZ(), 0, 0, 0);
			}
		}
		if (!this.level().isClientSide()) {
			if (this.getTpTick() < 0) {
				this.setTpTick(this.getTpTick() + 1);
			} else if (this.getTpTick() == 0) {
				if (this.getRandom().nextInt(5) == 0) {
					this.setTpTick(1);
				}
			} else {
				if (this.getTpTick() >= this.getTpCD()) {
					this.checkAndTeleport();
				}
				this.setTpTick(this.getTpTick() + 1);
			}
		} else {
			if (this.getTpTick() > 0) {
				for (int i = 0; i < 9; ++i) {
					this.level().addParticle(ParticleTypes.PORTAL,
							this.getX() + (this.getRandom().nextInt(5) - 2),
							this.getY() + (this.getRandom().nextInt(3)),
							this.getZ() + (this.getRandom().nextInt(5) - 2), 0, 0, 0);
				}
			}
		}
	}

	private void checkAndTeleport() {
		final BlockPos teleportPos = this.getTarget() == null
				? this.blockPosition() : this.getTarget().blockPosition();
		this.setTpTick(-this.getRandom().nextInt(this.maxTpCD - this.minTpCD + 1) - this.minTpCD);
		this.teleportToPos(teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());
		ZombieHandEntity.spawnRangeZombieHands(this.level(), this, 2);
	}

	protected void checkAndSummonMournerZombie() {
		if (this.nearbyZombieCount >= this.maxZombieSurround) {
			this.checkAndSummonZombieHand();
			return;
		}
		final int max = Math.min(this.maxZombieSurround - this.nearbyZombieCount, 3);
		for (int i = 0; i < MathUtil.getRandomMinMax(this.getRandom(), 1 + max / 2, max + 1); ++i) {
			MournerZombieEntity zombie = PvZEntityRegister.MOURNER_ZOMBIE.get().create(this.level(),EntitySpawnReason.MOB_SUMMONED);
			this.onBossSummon(zombie, this.getTarget() == null
					? this.blockPosition() : this.getTarget().blockPosition());
		}
	}

	private void checkAndSleepPlant() {
		final float range = 50;
		List<PlantEntity> list = this.level().getEntitiesOfClass(PlantEntity.class,
				EntityUtil.getEntityAABB(this, range, range),
				plant -> EntityUtil.canTargetEntity(this, plant));
		if (list.isEmpty()) {
			return;
		}
		int pos = this.getRandom().nextInt(list.size());
		final PlantEntity plant = list.get(pos);
		final float radius = 3F;
		for (PlantEntity target : this.level().getEntitiesOfClass(PlantEntity.class,
				EntityUtil.getEntityAABB(plant, radius, radius),
				p -> EntityUtil.canTargetEntity(this, p))) {
			target.sleepTime = 2400;
		}
	}

	private void checkAndSummonZombieHand() {
		int cnt = this.getHandSummonNum();
		List<LivingEntity> list = EntityUtil.getTargetableLivings(this,
				EntityUtil.getEntityAABB(this, 50, 50));
		for (int i = 0; i < list.size(); ++i) {
			if (i + cnt >= list.size() || this.getRandom().nextInt(5) == 0) {
				ZombieHandEntity hand = PvZEntityRegister.ZOMBIE_HAND.get().create(this.level(),EntitySpawnReason.MOB_SUMMONED);
				if (hand == null) {
					return;
				}
				hand.summonByOwner(this);
				EntityUtil.onEntitySpawn(this.level(), hand,
						WorldUtil.getSuitableHeightPos(this.level(), list.get(i).blockPosition()));
				if (--cnt <= 0) {
					break;
				}
			}
		}
	}

	@Override
	public void checkAndHeal(float percent) {
		if (percent < 1f / 2) {
			this.heal(0.4f);
		} else if (percent < 1f / 6) {
			this.heal(0.8f);
		}
	}

	public void teleportToPos(double x, double y, double z) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(x, y, z);
		while (mutable.getY() > this.level().getMinY()
				&& this.level().getBlockState(mutable).getCollisionShape(this.level(), mutable).isEmpty()) {
			mutable.move(Direction.DOWN);
		}
		BlockState state = this.level().getBlockState(mutable);
		if (!state.getCollisionShape(this.level(), mutable).isEmpty()) {
			if (this.randomTeleport(x, y, z, true,BlockTags.DANGEROUS_FOR_TELEPORTATION)) {
				this.level().playSound(null, this.xo, this.yo, this.zo,
						SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
				this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (amount >= 10) {
			amount /= 3f;
		} else {
			amount /= 2f;
		}
		return super.hurtServer(level, source, amount);
	}

	@Override
	public float getLife() {
		return 1000;
	}

	@Override
	public float getInnerLife() {
		return 1000;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.8F, 1.9F);
	}

	protected int getTpCD() {
		final float percent = this.bossInfo.getProgress();
		if (percent < 1f / 3) {
			return 60;
		} else if (percent < 2f / 3) {
			return 80;
		}
		return 100;
	}

	protected int getHandSummonNum() {
		final float percent = this.bossInfo.getProgress();
		return percent < 1f / 3 ? this.nearbyPlantCount / 5 + 3
				: percent < 2f / 3 ? this.nearbyPlantCount / 8 + 2
				: this.nearbyPlantCount / 10 + 1;
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.summonTick = input.getIntOr("summon_zombie_tick", 0);
		this.setTpTick(input.getIntOr("zombie_tp_tick", 0));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("summon_zombie_tick", this.summonTick);
		output.putInt("zombie_tp_tick", this.getTpTick());
	}

	public int getTpTick() {
		return this.entityData.get(TP_TICK);
	}

	public void setTpTick(int tick) {
		this.entityData.set(TP_TICK, tick);
	}

	@Override
	public IZombieType getZombieType() {
		return CustomZombies.NOBLE_ZOMBIE.get();
	}
}