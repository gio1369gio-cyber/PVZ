package com.lying.pvz.common.level.entity.zombie.roof;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.ICanAttract;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * 小鬼僵尸（IMP）。
 *
 * <p>由 {@link GargantuarEntity} 投掷入场，本体 10 血、行走
 * {@link ZombieUtil#WALK_FAST}。生成时按 {@link #finalizeSpawn} 概率获得随机
 * 增益（速度 / 力量 / 跳跃 / 生命 / 吸收各 10%）。核心机制：
 * <ul>
 *   <li><b>投掷入场</b>：{@link #throwByGargantuar(GargantuarEntity, LivingEntity)}
 *       记录 {@code isFalling = true}，{@code protectTick = 60} 免疫 3 秒；</li>
 *   <li><b>落地复位</b>：{@link #zombieTick()} 落地后清 {@code isFalling}；</li>
 *   <li><b>空中免疫</b>：{@link #canNormalUpdate()} / {@link #isZombieInvulnerableTo}
 *       在 {@code isFalling} 或 {@code protectTick > 0} 时返回 false / true。</li>
 * </ul>
 */
public class ImpEntity extends ZombieEntity {

	protected boolean isFalling = false;
	protected int protectTick = 0;

	public ImpEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
										EntitySpawnReason reason, @Nullable SpawnGroupData spawnData) {
		if (!this.level().isClientSide()) {
			final int now = this.getRandom().nextInt(10);
			switch (now) {
				case 0 -> this.addEffect(new MobEffectInstance(MobEffects.SPEED, 600, 1));
				case 1 -> this.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 600, 1));
				case 2 -> this.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 600, 1));
				case 3 -> this.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 600, 1));
				case 4 -> this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 1));
				default -> {
				}
			}
		}
		return super.finalizeSpawn(level, difficulty, reason, spawnData);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_FAST);
		}
	}

	@Override
	public void zombieTick() {
		super.zombieTick();
		if (!this.level().isClientSide()) {
			if (this.onGround() && this.isFalling) {
				this.isFalling = false;
			}
			if (this.protectTick > 0) {
				--this.protectTick;
			}
		}
	}

	@Override
	public boolean canNormalUpdate() {
		return super.canNormalUpdate() && !this.isFalling;
	}

	@Override
	protected boolean isZombieInvulnerableTo(DamageSource source) {
		return super.isZombieInvulnerableTo(source) || this.protectTick > 0;
	}

	public void throwByGargantuar(GargantuarEntity entity, LivingEntity target) {
		Vec3 vec = new Vec3(entity.getRandom().nextFloat() - 0.5,
				entity.getRandom().nextFloat() / 4,
				entity.getRandom().nextFloat() - 0.5).normalize();
		if (target != null) {
			double speed = 2F;
			vec = target.position().subtract(entity.position()).normalize().scale(speed);
		} else {
			double speed = 0.5F;
			vec = vec.scale(speed);
		}
		this.setDeltaMovement(vec);
		ZombieUtil.copySummonZombieData(entity, this);
		this.isFalling = true;
		this.protectTick = 60;
	}

	@Override
	public boolean canBeAttractedBy(ICanAttract defender) {
		return !this.isFalling && super.canBeAttractedBy(defender);
	}

	@Override
	public float getLife() {
		return 10;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		if (this.isMiniZombie()) {
			return EntityDimensions.scalable(0.2F, 0.45F);
		}
		return EntityDimensions.scalable(0.6F, 1.2F);
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.protectTick = input.getIntOr("protect_tick", 0);
		this.isFalling = input.getBooleanOr("fall_from_throw", false);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("protect_tick", this.protectTick);
		output.putBoolean("fall_from_throw", this.isFalling);
	}

	@Override
	public IZombieType getZombieType() {
		return RoofZombies.IMP.get();
	}
}