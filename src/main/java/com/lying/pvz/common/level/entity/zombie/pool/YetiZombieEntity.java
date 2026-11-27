package com.lying.pvz.common.level.entity.zombie.pool;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.drop.JewelEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 雪人僵尸（YETI）。
 *
 * <p>本体 135 血，行走 {@link ZombieUtil#WALK_LITTLE_FAST}、攻击
 * {@link ZombieUtil#LITTLE_LOW}，禁用冻结。
 *
 * <p>生存周期：
 * <ul>
 *   <li>{@code live_tick} 超过 {@link PvZConfig} 配置的一半时，
 *       首次获得 {@link MobEffects#INVISIBILITY}（持续 2000 tick，等级 2），
 *       随后每 tick {@code heal(0.5)}；</li>
 *   <li>超过上限后自动 {@code discard()}，即“生命周期自然耗尽”。</li>
 * </ul>
 *
 * <p>单次伤害 ≥100 时除以 10（防止一击致死）。死亡时在四角生成 4 颗
 * {JewelEntity}，掉落宝石奖励。
 */
public class YetiZombieEntity extends PVZZombieEntity {

	private final int DROP_JEWEL_NUM = 4;
	private final double[] DD = new double[]{-0.5D, 0.5D, 0.5D, -0.5D};
	private int live_tick = 0;
	private boolean hasInvis = false;

	public YetiZombieEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canBeFrozen = false;
	}

	@Override
	public float getLife() {
		return 135;
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
			movementSpeed.setBaseValue(ZombieUtil.WALK_LITTLE_FAST);
		}
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			++this.live_tick;
			int maxLiveTick = this.getYetiMaxLiveTick();
			if (this.live_tick >= maxLiveTick) {
				this.discard();
			} else if (this.live_tick > maxLiveTick / 2) {
				if (!this.hasInvis) {
					this.hasInvis = true;
					this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2000, 2, false, true));
				}
				this.heal(0.5f);
			}
		}
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		if (this.isMiniZombie()) {
			return EntityDimensions.scalable(0.6F, 1.2F);
		}
		return EntityDimensions.scalable(1f, 2.6f);
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (amount >= 100) {
			amount /= 10;
		}
		return super.hurtServer(level, source, amount);
	}

	@Override
	protected void spawnSpecialDrops() {
		for (int i = 0; i < this.DROP_JEWEL_NUM; ++i) {
			JewelEntity jewel = PvZEntityRegister.JEWEL.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
			if (jewel ==null) return;
			EntityUtil.onEntitySpawn(this.level(), jewel,
					this.blockPosition().offset((int) this.DD[i], (int) this.getRandom().nextDouble(),
							(int) this.DD[(i + 1) % this.DROP_JEWEL_NUM]));
		}
	}

	public int getYetiMaxLiveTick() {
		return PvZConfig.LOCAL_CONFIG.EntitySettings.EntityLiveTick.YetiLiveTick.get();
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("yeti_live_tick", this.live_tick);
		output.putBoolean("yeti_invis", this.hasInvis);
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.live_tick = input.getIntOr("yeti_live_tick", 0);
		this.hasInvis = input.getBooleanOr("yeti_invis", false);
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.YETI_ZOMBIE.get();
	}
}