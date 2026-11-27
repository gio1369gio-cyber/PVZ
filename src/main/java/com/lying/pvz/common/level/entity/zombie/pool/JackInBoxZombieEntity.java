package com.lying.pvz.common.level.entity.zombie.pool;


import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.IHasMetal;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.common.attachment.player.PlayerMeta;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.init.PvZAttachments;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.common.attachment.player.PlayerDataManager;
import com.lying.pvz.utils.ConfigUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 玩偶盒僵尸。
 *
 * <p>本体 40 血，行走 {@link ZombieUtil#WALK_FAST}。核心机制为“盒中炸弹”：
 * <ul>
 *   <li>{@code attackTime} 为负数时表示“倒计时”，初始随机 300~3000；
 *       靠近目标 50 格且 {@code attackTime == -1} 时切换为
 *       {@link #JACK_EXPLODE_CD}（引爆倒计时）；</li>
 *   <li>{@code attackTime == 1} 时 {@link #doJackExplode()}：3.5 格内植物按最大
 *       生命全额伤害、难度影响爆炸半径、{@link ConfigUtil#jackinboxBreak()}
 *       控制是否破坏方块，爆炸后 {@code discard()}</li>
 *   <li>引爆期间（{@link #isInExplosion()}）不可被选为目标，免疫伤害；</li>
 *   <li>{@link PlayJackBoxGoal} 在持有盒子时定期向 150 格内玩家播放
 *       {@link PvZSoundRegister#JACK_SURPRISE}，通过 {@link PlayerDataManager}
 *       的 {@code playSoundTick} 节流。</li>
 * </ul>
 *
 * <p>{@code JACK_BOX} 在新 {@link MetalTypes} 中不存在，按内层携带物处理，
 * {@link #getMetalType()} 返回 {@link MetalTypes#EMPTY}。
 */
public class JackInBoxZombieEntity extends PVZZombieEntity implements IHasMetal {

	private static final EntityDataAccessor<Boolean> HAS_BOX =
			SynchedEntityData.defineId(JackInBoxZombieEntity.class, EntityDataSerializers.BOOLEAN);
	public static final int JACK_EXPLODE_CD = 30;
	private final int MinExplodeTime = 300;
	private final int MaxExplodeTime = 3000;

	public JackInBoxZombieEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setExplosionTime();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HAS_BOX, true);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new PlayJackBoxGoal(this));
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide() && this.hasBox()) {
			final int tick = this.getAttackTime();
			if (tick < 0) {
				if (tick == -1 && this.canJackExplode()) {
					this.setAttackTime(JACK_EXPLODE_CD);
					EntityUtil.playSound(this, PvZSoundRegister.JACK_SURPRISE.get());
				} else {
					this.setAttackTime(Math.min(tick + 1, -1));
				}
			} else {
				if (tick == 1) {
					this.doJackExplode();
				}
				this.setAttackTime(Math.max(tick - 1, 0));
			}
		}
		if (this.level().isClientSide() && this.hasBox() && this.getAttackTime() == 3) {
			for (int i = 0; i < 2; ++i) {
				this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER,
						this.getX(), this.getY(), this.getZ(), 0, 0, 0);
			}
		}
	}

	private void doJackExplode() {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		final float range = 3.5F;
		EntityUtil.getWholeTargetableEntities(this, EntityUtil.getEntityAABB(this, range, range)).forEach(target -> {
			final PvZEntityDamageSource source = PvZEntityDamageSource.explode(this);
			if (target instanceof PlantEntity && target instanceof LivingEntity living) {
				living.hurtServer(serverLevel, source, EntityUtil.getMaxHealthDamage(living));
			}
		});
		EntityUtil.playSound(this, PvZSoundRegister.CAR_EXPLOSION.get());
		final Level.ExplosionInteraction mode = ConfigUtil.jackinboxBreak()
				? Level.ExplosionInteraction.MOB
				: Level.ExplosionInteraction.NONE;
		final float strength = this.level().getDifficulty() == Difficulty.HARD ? 2.4F
				: this.level().getDifficulty() == Difficulty.NORMAL ? 2F : 1.6F;
		this.level().explode(this, this.getX(), this.getY(), this.getZ(), strength, mode);
		this.discard();
	}

	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		return super.canBeTargetBy(living) && !this.isInExplosion();
	}

	@Override
	protected boolean isZombieInvulnerableTo(DamageSource source) {
		return super.isZombieInvulnerableTo(source) || this.isInExplosion();
	}

	protected void playJackBoxSound() {
		this.level().players().stream()
				.filter(player -> this.distanceToSqr(player) <= 150)
				.forEach(player -> {
					PlayerMeta data = player.getData(PvZAttachments.PLAYER_META);
					if (data.playSoundTick == 0) {
						PlayerUtil.playClientSound(player, PvZSoundRegister.JACK_SURPRISE.get());
						data.playSoundTick = 300;
					}
				});
	}

	@Override
	public boolean canLostHand() {
		return super.canLostHand() && !this.hasBox();
	}

	@Override
	protected void setBodyStates(ZombieDropBodyEntity body) {
		super.setBodyStates(body);
		body.setHandDefence(this.hasBox());
	}

	private boolean canJackExplode() {
		if (this.getTarget() == null) {
			return false;
		}
		return this.distanceToSqr(this.getTarget()) <= 50;
	}

	@Override
	public int getAmbientSoundInterval() {
		return 200;
	}

	private void setExplosionTime() {
		this.setAttackTime(-MathUtil.getRandomMinMax(this.getRandom(), MinExplodeTime, MaxExplodeTime));
	}

	@Override
	public float getLife() {
		return 40;
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_FAST;
	}

	public boolean isInExplosion() {
		return this.getAttackTime() > 0;
	}

	@Override
	public boolean hasMetal() {
		return this.hasBox();
	}

	@Override
	public void decreaseMetal() {
		this.setBox(false);
	}

	@Override
	public void increaseMetal() {
		this.setBox(true);
	}

	@Override
	public MetalTypes getMetalType() {
		return MetalTypes.JACK_BOX;
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setBox(input.getBooleanOr("has_jack_box", true));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("has_jack_box", this.hasBox());
	}

	public void setBox(boolean has) {
		this.entityData.set(HAS_BOX, has);
	}

	public boolean hasBox() {
		return this.entityData.get(HAS_BOX);
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.JACK_IN_BOX_ZOMBIE.get();
	}

	static class PlayJackBoxGoal extends Goal {

		private final JackInBoxZombieEntity zombie;
		private final int PlayCD = 50;
		private int delayTick = 20;

		public PlayJackBoxGoal(JackInBoxZombieEntity zombie) {
			this.zombie = zombie;
		}

		@Override
		public boolean canUse() {
			if (--this.delayTick > 0 || !this.zombie.hasBox()) {
				return false;
			}
			return this.zombie.getRandom().nextFloat() < 0.2F;
		}

		@Override
		public void start() {
		}

		@Override
		public boolean canContinueToUse() {
			return this.zombie.hasBox();
		}

		@Override
		public void tick() {
			if (this.zombie.tickCount % this.PlayCD == 0) {
				this.zombie.playJackBoxSound();
			}
		}

		@Override
		public void stop() {
			this.delayTick = 20;
		}
	}
}