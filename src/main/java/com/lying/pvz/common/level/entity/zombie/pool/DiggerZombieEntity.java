package com.lying.pvz.common.level.entity.zombie.pool;


import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.ICanAttract;
import com.lying.pvz.api.entity.ability.IHasMetal;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.level.entity.ai.goal.target.PVZRandomTargetGoal;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.appease.SplitPeaEntity;
import com.lying.pvz.common.level.entity.plant.arma.MelonPultEntity;
import com.lying.pvz.common.level.entity.plant.ice.WinterMelonEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.WorldUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 矿工僵尸。
 *
 * <p>本体 40 血。{@code HAS_PICKAXE} 控制两个阶段：
 * <ul>
 *   <li><b>有镐</b>：攻击 {@link ZombieUtil#LOW}、行走 {@link ZombieUtil#WALK_LITTLE_FAST}，
 *       靠近目标 8 格内时 {@code attackTime} 累加（挖掘动作），
 *       期间不可被植物选中，且对投射物免疫（见 {@link #isZombieInvulnerableTo}）；</li>
 *   <li><b>无镐</b>：攻击 {@link ZombieUtil#HUGE_LOW}、行走
 *       {@link ZombieUtil#WALK_LITTLE_SLOW}，正常受击。</li>
 * </ul>
 *
 * <p>{@link #isNotDigging()} 等价于 {@code attackTime == MAX_OUT_TIME}：
 * 完全钻出地面时不再有钻地保护。可通过 {@code TALL_NUT} 类吸引物
 * （{@link #canBeAttractedBy}）强制落地，触发 {@code setPickaxe(false)}。
 *
 * <p>{@code IRON_PICKAXE} 在新 {@link MetalTypes} 中不存在，按内层携带物处理，
 * {@link #getMetalType()} 返回 {@link MetalTypes#EMPTY}。
 */
public class DiggerZombieEntity extends ZombieEntity implements IHasMetal {

	private static final EntityDataAccessor<Boolean> HAS_PICKAXE =
			SynchedEntityData.defineId(DiggerZombieEntity.class, EntityDataSerializers.BOOLEAN);
	public static final int MAX_OUT_TIME = 30;

	public DiggerZombieEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HAS_PICKAXE, true);
	}

	@Override
	protected void registerTargetGoals() {
		this.targetSelector.addGoal(0, new PVZRandomTargetGoal(this, true, true,
				ZombieUtil.NORMAL_TARGET_RANGE, ZombieUtil.NORMAL_TARGET_HEIGHT));
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			LivingEntity target = this.getTarget();
			if (this.hasPickaxe()) {
				if (target != null && this.distanceToSqr(target) <= 8) {
					this.setAttackTime(Mth.clamp(this.getAttackTime() + 1, 0, MAX_OUT_TIME));
				} else {
					this.setAttackTime(Mth.clamp(this.getAttackTime() - 1, 0, MAX_OUT_TIME));
				}
			} else {
				this.setAttackTime(Mth.clamp(this.getAttackTime() + 1, 0, MAX_OUT_TIME));
			}
		} else {
			if (!this.isNotDigging()) {
				WorldUtil.spawnRandomSpeedParticle(this.level(), PvZParticleRegister.DIRT_BURST_OUT.get(),
						this.position(), 0.1F);
			}
		}
		this.refreshDimensions();
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
		super.onSyncedDataUpdated(data);
		if (data.equals(HAS_PICKAXE)) {
			this.updateAttributes(this.hasPickaxe());
		}
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		this.updateAttributes(this.hasPickaxe());
	}

	private void updateAttributes(boolean has) {
		AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
		if (attackDamage != null) {
			attackDamage.setBaseValue(has ? ZombieUtil.LOW : ZombieUtil.HUGE_LOW);
		}
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(has ? ZombieUtil.WALK_LITTLE_FAST : ZombieUtil.WALK_LITTLE_SLOW);
		}
	}

	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		if (living instanceof PlantEntity && !this.isNotDigging()) {
			return false;
		}
		return super.canBeTargetBy(living);
	}

	@Override
	public boolean canBeAttractedBy(ICanAttract defender) {
		return !this.hasPickaxe();
	}

	@Override
	protected boolean isZombieInvulnerableTo(DamageSource source) {
		if (!this.isNotDigging() && isProjectileDamage(source)) {
			return !(source.getEntity() instanceof SplitPeaEntity
					|| source.getEntity() instanceof Player
					|| source.getEntity() instanceof MelonPultEntity
					|| source.getEntity() instanceof WinterMelonEntity);
		}
		return super.isZombieInvulnerableTo(source);
	}

	private static boolean isProjectileDamage(DamageSource source) {
		return source.is(DamageTypeTags.IS_PROJECTILE)
				|| (source instanceof PvZEntityDamageSource pds
				&& (pds.isAppease() || pds.isParabola()));
	}

	public boolean isNotDigging() {
		return this.getAttackTime() == MAX_OUT_TIME;
	}

	@Override
	public float getLife() {
		return 40;
	}

	@Override
	public boolean hasMetal() {
		return this.hasPickaxe();
	}

	@Override
	public void decreaseMetal() {
		this.setPickaxe(false);
	}

	@Override
	public void increaseMetal() {
		this.setPickaxe(true);
	}

	@Override
	public MetalTypes getMetalType() {
		return MetalTypes.IRON_PICKAXE;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		if (this.isMiniZombie()) {
			return EntityDimensions.scalable(0.4f, this.getAttackTime() * 0.02F + 0.1F);
		}
		return EntityDimensions.scalable(0.8f, this.getAttackTime() * 0.06F + 0.2F);
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.setPickaxe(input.getBooleanOr("digger_has_pickaxe", true));
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("digger_has_pickaxe", this.hasPickaxe());
	}

	public void setPickaxe(boolean has) {
		this.entityData.set(HAS_PICKAXE, has);
	}

	public boolean hasPickaxe() {
		return this.entityData.get(HAS_PICKAXE);
	}


	@Override
	public IZombieType getZombieType() {
		return PoolZombies.DIGGER_ZOMBIE.get();
	}
}