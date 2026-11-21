package com.lying.pvz.common.level.entity.zombie.other;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.CustomZombies;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.common.misc.PvZLoot;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * 捣蛋僵尸。
 *
 * <p>本体 6 血，行走 {@link ZombieUtil#WALK_LITTLE_FAST}、攻击 {@link ZombieUtil#HUGE_LOW}。
 * 特色：
 * <ul>
 *   <li><b>受击分裂</b>：{@link #hurtServer} 在单次伤害 ≥1.1、40 tick 冷却外、1/5 概率下，
 *       在自身周围随机位置召唤另一只捣蛋僵尸（复用 {@code ZombieUtil.copySummonZombieData}）；</li>
 *   <li><b>糖果收服</b>：{ #interactAt} 手持 {@code CANDY} 时有 1/3 概率消耗 1 个糖果、
 *       翻转魅惑状态并返回 {@link InteractionResult#CONSUME}；</li>
 *   <li><b>死亡自爆</b>：{@link #dropAllDeathLoot} 在未寒冷、未被魅惑时 1/5 概率原地爆炸
 *       （{@link Level.ExplosionInteraction#NONE}，不破坏方块）。</li>
 * </ul>
 *
 * <p>{@link #ignoreExplosion(Explosion)} 恒返回 true：捣蛋僵尸自身免疫爆炸伤害，
 * 避免自爆连锁时互相击杀。掉落表沿用 {@link PvZLoot#TRICK_ZOMBIE}。
 */
public class TrickZombieEntity extends ZombieEntity {

	public static final int EXPLOSION_CHANCE = 5;
	public static final int SUMMON_CHACNE = 5;
	private int lastSummonTick = 0;
	private final int summonGap = 40;

	public TrickZombieEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_LITTLE_FAST);
		}
		AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
		if (attackDamage != null) {
			attackDamage.setBaseValue(ZombieUtil.HUGE_LOW);
		}
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (amount >= 1.1F && !this.level().isClientSide()
				&& this.tickCount - this.lastSummonTick >= this.summonGap
				&& this.getRandom().nextInt(SUMMON_CHACNE) == 0) {
			this.lastSummonTick = this.tickCount;
			TrickZombieEntity zombie = PvZEntityRegister.TRICK_ZOMBIE.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
			BlockPos pos = this.blockPosition().offset(
					this.getRandom().nextInt(5) - 2,
					this.getRandom().nextInt(2),
					this.getRandom().nextInt(5) - 2);
			if (zombie != null) {
				ZombieUtil.copySummonZombieData(this, zombie);
				EntityUtil.onEntitySpawn(this.level(), zombie, pos);
			}
		}
		return super.hurtServer(level, source, amount);
	}

	@Override
	public InteractionResult interact(Player player,  InteractionHand hand,Vec3 vec3d) {
		if (!this.level().isClientSide()
				&& player.getItemInHand(hand).getItem() == PvZItemRegister.CANDY.get()
				&& !this.isCharmed()) {
			if (this.getRandom().nextInt(3) == 0) {
				player.getItemInHand(hand).shrink(1);
				this.setCharmed(true);
				return InteractionResult.CONSUME;
			}
		}
		return super.interact(player, hand, vec3d);
	}

	@Override
	protected void dropAllDeathLoot(ServerLevel level, DamageSource damageSourceIn) {
		if (!this.hasEffect(PvZEffectRegister.COLD_EFFECT) && !this.isCharmed()) {
			if (this.getRandom().nextInt(EXPLOSION_CHANCE) == 0) {
				this.level().explode(this, this.getX(), this.getY(), this.getZ(),
						0.5f, Level.ExplosionInteraction.NONE);
			}
		}
		super.dropAllDeathLoot(level, damageSourceIn);
	}

	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		if (this.isMiniZombie()) {
			return EntityDimensions.scalable(0.3F, 0.5F);
		}
		return EntityDimensions.scalable(0.6F, 1.2F);
	}

	@Override
	public float getLife() {
		return 6;
	}


	@Override
	public IZombieType getZombieType() {
		return CustomZombies.TRICK_ZOMBIE.get();
	}
}