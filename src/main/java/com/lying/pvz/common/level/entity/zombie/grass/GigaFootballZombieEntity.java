package com.lying.pvz.common.level.entity.zombie.grass;


import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.utils.EffectUtil;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * 巨型橄榄球僵尸。
 *
 * <p>继承 {@link FootballZombieEntity}，本体 100 血 + 内层头盔 300 血。
 * 额外机制为“冲刺”：
 * <ul>
 *   <li>未被冲刺命中、且攻击计时归零时进入 {@link #isRushing()}，
 *       加速药水 {@code MOVEMENT_SPEED} 等级 1，持续到命中或断开；</li>
 *   <li>{@link #doPush} 命中可攻击目标时按 30% 最大生命造成碾压伤害，
 *       并立即退出冲刺（{@code updateRush(false)}）重置冷却；</li>
 *   <li>冲刺期间禁用寒冷/黄油（{@link #canBeCold()} / {@link #canBeButtered()}），
 *       避免被控打断；</li>
 *   <li>冲刺冷却为 200~600 tick 随机，写入 {@code attackTime}。</li>
 * </ul>
 *
 * <p>碰撞推挤范围 {@code getCollideWidthOffset = 0.4}，比普通僵尸更宽，
 * 便于冲刺时清扫更多目标。
 *
 * <p>{@code GIGA_HELMET} 在新 {@link MetalTypes} 中不存在，头盔按内层防御
 * 处理，{@link #getMetalType()} 返回 {@link MetalTypes#EMPTY}。
 */
public class GigaFootballZombieEntity extends FootballZombieEntity {

	protected boolean isRushing = false;
	private final int minRushCD = 200;
	private final int maxRushCD = 600;

	public GigaFootballZombieEntity(EntityType<? extends FootballZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.canCollideWithZombie = false;
	}

	@Override
	public void normalZombieTick() {
		super.normalZombieTick();
		if (!this.level().isClientSide()) {
			if (!this.isRushing && this.getAttackTime() == 0) {
				this.updateRush(true);
			}
			this.setAttackTime(Math.max(0, this.getAttackTime() - 1));
		}
	}

	protected void updateRush(boolean is) {
		this.isRushing = is;
		if (!is) {
			this.setAttackTime(MathUtil.getRandomMinMax(this.getRandom(), minRushCD, maxRushCD));
		} else {
			this.addEffect(EffectUtil.effect(MobEffects.SPEED, 1000000, 1));
		}
	}

	@Override
	protected boolean shouldCollideWithEntity(LivingEntity target) {
		return EntityUtil.canTargetEntity(this, target);
	}

	@Override
	protected void doPush(Entity target) {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		if (this.isRushing() && target instanceof LivingEntity living) {
			living.hurtServer(serverLevel, PvZEntityDamageSource.causeCrushDamage(this),
					EntityUtil.getMaxHealthDamage(living, 0.3F));
			this.updateRush(false);
		}
	}

	@Override
	protected double getCollideWidthOffset() {
		return 0.4D;
	}

	@Override
	public float getInnerLife() {
		return 300;
	}

	@Override
	public float getEatDamage() {
		return ZombieUtil.LITTLE_LOW;
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_FAST;
	}

	@Override
	public MetalTypes getMetalType() {
		return MetalTypes.GIGA_HELMET;
	}

	@Override
	public boolean canBeCold() {
		return !this.isRushing();
	}

	@Override
	public boolean canBeButtered() {
		return !this.isRushing();
	}

	public boolean isRushing() {
		return this.isRushing;
	}

	@Override
	public float getLife() {
		return 100;
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		this.isRushing = input.getBooleanOr("is_zombie_rushing", false);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putBoolean("is_zombie_rushing", this.isRushing);
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.GIGA_FOOTBALL_ZOMBIE.get();
	}
}