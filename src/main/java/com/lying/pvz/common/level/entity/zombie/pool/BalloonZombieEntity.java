package com.lying.pvz.common.level.entity.zombie.pool;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.ICanAttract;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;

/**
 * 气球僵尸。
 *
 * <p>气球状态由 {@code HAS_BALLOON} 控制：
 * <ul>
 *   <li>有气球时无重力，使用 {@link FlyingMoveControl} + {@link FlyingPathNavigation}，
 *       可以越过植物、不可被魅惑以外的控制（寒冷/冰冻/黄油）影响，不可被吸引；</li>
 *   <li>气球被刺或抛物线/抚慰类伤害击破（{@link #canHitBalloon}）后，
 *       切换为地面 AI 与重力，恢复为普通僵尸。</li>
 * </ul>
 *
 * <p>飞行伤害免疫：有气球时对 {@link DamageTypeTags#IS_PROJECTILE} 与
 * {@link PvZEntityDamageSource#isAppease()} / {@code isParabola()} 免疫
 * （见 {@link #isZombieInvulnerableTo}）。本体 23 血，行走
 * {@link ZombieUtil#WALK_LITTLE_SLOW}。
 */
public class BalloonZombieEntity extends ZombieEntity {

	private static final EntityDataAccessor<Boolean> HAS_BALLOON =
			SynchedEntityData.defineId(BalloonZombieEntity.class, EntityDataSerializers.BOOLEAN);

	private final MoveControl flyController = new FlyingMoveControl(this, 360, true);
	private final MoveControl groundController = new MoveControl(this);
	private PathNavigation flyNavigator;
	private PathNavigation groundNavigator;

	public BalloonZombieEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HAS_BALLOON, true);
	}

	@Override
	protected void registerGoals() {
		this.flyNavigator = new FlyingPathNavigation(this, this.level());
		this.groundNavigator = new GroundPathNavigation(this, this.level());
		super.registerGoals();
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance flyingSpeed = this.getAttribute(Attributes.FLYING_SPEED);
		if (flyingSpeed != null) {
			flyingSpeed.setBaseValue(ZombieUtil.FLY_FAST);
		}
		this.setNoGravity(this.hasBalloon());
		this.moveControl = this.hasBalloon() ? this.flyController : this.groundController;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
		super.onSyncedDataUpdated(data);
		if (data.equals(HAS_BALLOON)) {
			this.setNoGravity(this.hasBalloon());
			this.moveControl = this.hasBalloon() ? this.flyController : this.groundController;
		}
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (this.hasBalloon() && this.canHitBalloon(source)) {
			this.onBalloonExplode();
			amount = 0;
		}
		return super.hurtServer(level, source, amount);
	}

	private boolean canHitBalloon(DamageSource source) {
		if (source.getDirectEntity() instanceof Arrow) {
			return true;
		}
		if (source instanceof PvZEntityDamageSource pds) {
			return pds.isThornDamage();
		}
		return false;
	}

	public void onBalloonExplode() {
		if (!this.level().isClientSide()) {
			EntityUtil.playSound(this, PvZSoundRegister.BALLOON_POP.get());
		}
		this.setBalloon(false);
	}

	@Override
	protected boolean isZombieInvulnerableTo(DamageSource source) {
		if (this.hasBalloon() && isProjectileDamage(source)) {
			return true;
		}
		return super.isZombieInvulnerableTo(source);
	}

	private static boolean isProjectileDamage(DamageSource source) {
		return source.is(DamageTypeTags.IS_PROJECTILE)
				|| (source instanceof PvZEntityDamageSource pds
				&& (pds.isAppease() || pds.isParabola()));
	}

	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		if (living instanceof PlantEntity && this.hasBalloon()) {
			return false;
		}
		return super.canBeTargetBy(living);
	}

	@Override
	public boolean isClimbing() {
		return !this.hasBalloon() && super.isClimbing();
	}

	@Override
	public boolean canBeAttractedBy(ICanAttract defender) {
		return !this.hasBalloon();
	}

	@Override
	public float getLife() {
		return 23;
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_LITTLE_SLOW;
	}

	@Override
	public boolean canBeButtered() {
		return !this.hasBalloon();
	}

	@Override
	public boolean canBeFrozen() {
		return !this.hasBalloon();
	}

	@Override
	public boolean canBeCold() {
		return !this.hasBalloon();
	}

	@Override
	public PathNavigation getNavigation() {
		if (this.hasBalloon()) {
			if (!(this.navigation instanceof FlyingPathNavigation)) {
				this.navigation = this.flyNavigator;
			}
		} else {
			if (!(this.navigation instanceof GroundPathNavigation)) {
				this.navigation = this.groundNavigator;
			}
		}
		return super.getNavigation();
	}

	@Override
	public Optional<SoundEvent> getSpawnSound() {
		return Optional.ofNullable(PvZSoundRegister.BALLOON_INFLATE.get());
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setBalloon(input.getBooleanOr("has_balloon", true));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("has_balloon", this.hasBalloon());
	}

	public void setBalloon(boolean has) {
		this.entityData.set(HAS_BALLOON, has);
	}

	public boolean hasBalloon() {
		return this.entityData.get(HAS_BALLOON);
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.BALLOON_ZOMBIE.get();
	}
}