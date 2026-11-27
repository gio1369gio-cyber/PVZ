package com.lying.pvz.common.level.entity.zombie.roof;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.ai.goal.attack.PVZZombieAttackGoal;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.level.entity.plant.spear.SpikeRockEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * 巨人僵尸（Gargantuar）。
 *
 * <p>本体 300 血、护甲韧性 5、行走 {@link ZombieUtil#WALK_SUPER_SLOW}。
 * 核心机制：
 * <ul>
 *   <li><b>携带小鬼</b>：血量低于 50% 时投掷 {@link ImpEntity}，
 *       {@link #getThrowCD()} 内完成动画；投掷后 {@code hasImp = false}；</li>
 *   <li><b>碾压攻击</b>：{@link #getCrushCD()} tick 动画，
 *       动画 1/3 处对目标造成最大生命 2 倍伤害；</li>
 *   <li><b>目标选择</b>：额外允许 {@link SpikeRockEntity}（地刺王），
 *       用于清理地面防御。</li>
 * </ul>
 *
 * <p>不破坏植物方块、击退抗性拉满。掉落时 {@code body.setMaxLiveTick(DEATH_ANIM_CD)}
 * 让尸体持续 100 tick 供观看；{@code isSad} 决定尸体是否保留手部外观。
 */
public class GargantuarEntity extends PVZZombieEntity {

	private static final EntityDataAccessor<Boolean> HAS_IMP =
			SynchedEntityData.defineId(GargantuarEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> TOOL_TYPE =
			SynchedEntityData.defineId(GargantuarEntity.class, EntityDataSerializers.INT);
	public static final int DEATH_ANIM_CD = 100;
	public boolean isSad = false;

	public GargantuarEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.setIsWholeBody();
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
										EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
		if (!this.level().isClientSide()) {
			this.setToolType(GargantuarType.fromOrdinal(
					this.getRandom().nextInt(GargantuarType.values().length)));
		}
		return super.finalizeSpawn(level, difficulty, reason, spawnData);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HAS_IMP, true);
		builder.define(TOOL_TYPE, GargantuarType.DOLL.ordinal());
	}

	@Override
	protected void registerAttackGoals() {
		this.goalSelector.addGoal(3, new GargantuarMoveToTargetGoal(this, true));
		this.goalSelector.addGoal(2, new CrushAttackGoal(this));
		this.goalSelector.addGoal(1, new ThrowImpGoal(this));
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (attribute != null) {
			attribute.setBaseValue(ZombieUtil.WALK_SUPER_SLOW);
		}
	}

	public void throwImp(LivingEntity target) {
		EntityUtil.playSound(this, PvZSoundRegister.IMP_HAPPY.get());
		ImpEntity imp = PvZEntityRegister.IMP.get().create(this.level(), EntitySpawnReason.TRIGGERED);
		if (imp == null) {
			return;
		}
		imp.throwByGargantuar(this, target);
		EntityUtil.onEntitySpawn(this.level(), imp, this.blockPosition().offset(0, (int) this.getBbHeight(), 0));
		this.setHasImp(false);
	}

	@Override
	protected void setBodyStates(ZombieDropBodyEntity body) {
		super.setBodyStates(body);
		body.setMaxLiveTick(DEATH_ANIM_CD);
		body.setHandDefence(this.isSad);
	}

	@Override
	public boolean canPAZTarget(Entity target) {
		if (target instanceof SpikeRockEntity) {
			return true;
		}
		return super.canPAZTarget(target);
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity target) {
		EntityUtil.playSound(this, PvZSoundRegister.GROUND_SHAKE.get());
		if (!EntityUtil.isEntityValid(target)) {
			return false;
		}
		return super.doHurtTarget(level, target);
	}

	@Override
	protected PvZEntityDamageSource getZombieAttackDamageSource() {
		return PvZEntityDamageSource.causeCrushDamage(this);
	}

	@Override
	protected float getModifyAttackDamage(Entity entity, float f) {
		if (entity instanceof LivingEntity living) {
			return EntityUtil.getMaxHealthDamage(living, 2);
		}
		return f;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		if (this.isMiniZombie()) {
			return EntityDimensions.scalable(0.6F, 1.8F);
		}
		return EntityDimensions.scalable(0.8f, 4f);
	}

	public boolean canThrowImp() {
		return this.canNormalUpdate() && this.getHealth() / this.getMaxHealth() < 0.5F;
	}

	@Override
	public boolean canBreakPlantBlock() {
		return false;
	}

	@Override
	public float getKBValue() {
		return 1;
	}

	public int getCrushCD() {
		return 40;
	}

	public int getThrowCD() {
		return 30;
	}

	@Override
	public float getLife() {
		return 300;
	}

	@Override
	public float getArmorToughness() {
		return 5f;
	}

	@Override
	protected int getDeathTime() {
		return 40;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return PvZSoundRegister.GARGANTUAR_DEATH.get();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return PvZSoundRegister.GARGANTUAR_GROAN.get();
	}

	@Override
	public int getAmbientSoundInterval() {
		return 200;
	}

	@Override
	public IZombieType getZombieType() {
		return RoofZombies.GARGANTUAR.get();
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setHasImp(input.getBooleanOr("has_imp", true));
		this.setToolType(GargantuarType.fromOrdinal(
				input.getIntOr("weapon_type", GargantuarType.DOLL.ordinal())));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("has_imp", this.hasImp());
		output.putInt("weapon_type", this.getToolType().ordinal());
	}

	public void setHasImp(boolean has) {
		this.entityData.set(HAS_IMP, has);
	}

	public boolean hasImp() {
		return this.entityData.get(HAS_IMP);
	}

	public void setToolType(GargantuarType type) {
		this.entityData.set(TOOL_TYPE, type.ordinal());
	}

	public GargantuarType getToolType() {
		return GargantuarType.fromOrdinal(this.entityData.get(TOOL_TYPE));
	}

	public enum GargantuarType {
		POLE, SIGN, DOLL;

		public static GargantuarType fromOrdinal(int ordinal) {
			final GargantuarType[] values = values();
			return (ordinal >= 0 && ordinal < values.length) ? values[ordinal] : DOLL;
		}
	}

	private final class GargantuarMoveToTargetGoal extends PVZZombieAttackGoal {

		public GargantuarMoveToTargetGoal(GargantuarEntity creature, boolean useLongMemory) {
			super(creature, useLongMemory);
		}

		@Override
		public boolean canUse() {
			return this.zombie.getAttackTime() == 0 && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return this.zombie.getAttackTime() == 0 && super.canContinueToUse();
		}

		@Override
		protected void checkAndPerformAttack(LivingEntity target) {
			double dis = EntityUtil.getNearestDistance(this.attacker, target);
			double range = this.getAttackReachSqr(target);
			if (range >= dis && this.attackTick <= 0) {
				this.attackTick = this.zombie.getAttackCD();
				this.attacker.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT);
				this.zombie.setAttackTime(GargantuarEntity.this.getCrushCD());
			}
		}
	}

	private final class CrushAttackGoal extends Goal {

		private final GargantuarEntity attacker;

		public CrushAttackGoal(GargantuarEntity creature) {
			this.attacker = creature;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return this.attacker.getAttackTime() > 0;
		}

		@Override
		public boolean canContinueToUse() {
			return this.attacker.getAttackTime() > 0;
		}

		@Override
		public void tick() {
			if (this.attacker.canNormalUpdate() && this.attacker.getAttackTime() > 0) {
				this.attacker.setAttackTime(this.attacker.getAttackTime() - 1);
				if (this.attacker.getAttackTime() == this.attacker.getCrushCD() / 3
						&& this.attacker.level() instanceof ServerLevel serverLevel) {
					this.attacker.doHurtTarget(serverLevel, this.attacker.getTarget());
				}
			}
		}
	}

	private final class ThrowImpGoal extends Goal {

		private final GargantuarEntity attacker;

		public ThrowImpGoal(GargantuarEntity creature) {
			this.attacker = creature;
			this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return this.attacker.hasImp() && this.attacker.canThrowImp();
		}

		@Override
		public void start() {
			this.attacker.setAttackTime(-this.attacker.getThrowCD());
		}

		@Override
		public boolean canContinueToUse() {
			return this.attacker.getAttackTime() < 0;
		}

		@Override
		public void stop() {
			this.attacker.setAttackTime(0);
		}

		@Override
		public void tick() {
			if (this.attacker.canNormalUpdate() && this.attacker.getAttackTime() < 0) {
				this.attacker.setAttackTime(this.attacker.getAttackTime() + 1);
				if (this.attacker.hasImp()
						&& -this.attacker.getAttackTime() == this.attacker.getThrowCD() / 4) {
					this.attacker.throwImp(this.attacker.getTarget());
				}
			}
		}
	}
}