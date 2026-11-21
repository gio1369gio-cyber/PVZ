package com.lying.pvz.common.level.entity.zombie.base;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * BOSS 僵尸基类（EDGAR 系列等）。
 *
 * <p>与普通 {@link ZombieEntity} 的差异：
 * <ul>
 *   <li><b>BossBar</b>：持有 {@link ServerBossEvent}，血量按
 *       {@code getCurrentHealth / getCurrentMaxHealth} 实时刷新；玩家进出视野用
 *       {@link #startSeenByPlayer}/{@link #stopSeenByPlayer} 挂载/卸载。</li>
 *   <li><b>免伤窗口</b>：出生后 {@link #spawnImmuneCD} tick 内无敌（{@link #isZombieInvulnerableTo}）。</li>
 *   <li><b>脱战自愈</b>：无目标累计 {@link #noTargetTick} ≥ 40 tick 时每 tick {@code heal(1)}。</li>
 *   <li><b>范围清场</b>：{@link #kickEnemiesNearby()} 在 {@code tickCount % 10 == 0} 时对
 *       {@code kickRange} 内可攻击目标造成伤害（植物直接置 0 血；生物受攻击属性伤害并击飞）。</li>
 *   <li><b>碾压推挤</b>：{@link #doPush} 每 5 tick 对可攻击的推挤目标按血量百分比造成挤压伤害。</li>
 *   <li><b>禁止跨维度与自然消失</b>： 返回 false，
 *       {@link #removeWhenFarAway} 返回 false，{@link #canBeRemove} 置 false。</li>
 *   <li><b>能力关闭</b>：禁用魅惑/冰冻/迷你/隐身/Bungee 偷取；召唤僵尸 {@code canCollideWithZombie}
 *       仍为 false，避免同类互推。</li>
 * </ul>
 *
 * <p>子类通过 {@link #onBossSummon} 召唤附属僵尸（复制 {@code ZombieUtil#copySummonZombieData}），
 * 具体召唤逻辑（Bungee/元素球/投车）在 EDGAR 系列子类实现。
 */
public abstract class AbstractBossZombieEntity extends ZombieEntity {

	protected final ServerBossEvent bossInfo = new ServerBossEvent(this.uuid,
			this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
	protected int refreshCountCD = 30;
	protected int spawnImmuneCD = 100;
	protected float kickRange = 0;
	protected int maxZombieSurround = 40;
	protected int maxPlantSurround = 50;
	protected int nearbyPlantCount = 0;
	protected int nearbyZombieCount = 0;
	private int noTargetTick = 0;

	public AbstractBossZombieEntity(EntityType<? extends ZombieEntity> type, Level worldIn) {
		super(type, worldIn);
		this.bossInfo.setDarkenScreen(true);
		this.canCollideWithZombie = false;
		this.canBeCharm = false;
		this.setImmuneAllEffects();
		this.canBeMini = false;
		this.canBeInvisible = false;
		this.canBeStealByBungee = false;
		this.canBeRemove = false;
	}

	@Override
	protected VariantType rollVariantType() {
		return VariantType.NORMAL;
	}

	@Override
	public void zombieTick() {
		super.zombieTick();
		final float percent = (float) (this.getCurrentHealth() / this.getCurrentMaxHealth());
		this.bossInfo.setProgress(percent);
		if (!this.level().isClientSide()) {
			this.checkAndHeal(percent);
			if (this.tickCount % this.refreshCountCD == 0) {
				this.nearbyPlantCount = this.getNearbyPlantCount();
				this.nearbyZombieCount = this.getNearbyZombieCount();
			}
			this.kickEnemiesNearby();
		}
	}

	public void kickEnemiesNearby() {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		if (this.tickCount % 10 != 0 || this.kickRange <= 0) {
			return;
		}
		this.level().getEntitiesOfClass(LivingEntity.class,
				EntityUtil.getEntityAABB(this, this.kickRange, this.getBbHeight() + 1),
				target -> EntityUtil.canAttackEntity(target, this)
		).forEach(target -> {
			if (target instanceof PlantEntity) {
				target.kill(serverLevel);
			} else {
				target.hurtServer(serverLevel, PvZEntityDamageSource.normal(this),
						(float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
				target.setDeltaMovement(
						target.position().add(0, target.getEyeHeight(), 0)
								.subtract(this.position()).normalize().scale(2F)
				);
			}
		});
	}

	public void checkAndHeal(float percent) {
		if (this.getTarget() == null) {
			if (++this.noTargetTick >= 40) {
				this.heal(1);
			}
		}
	}

	@Override
	protected boolean isZombieInvulnerableTo(DamageSource source) {
		return super.isZombieInvulnerableTo(source) || this.tickCount <= this.spawnImmuneCD;
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	protected boolean shouldCollideWithEntity(LivingEntity target) {
		return EntityUtil.canTargetEntity(this, target);
	}

	@Override
	protected void doPush(Entity entityIn) {
		super.doPush(entityIn);
		if (this.level() instanceof ServerLevel serverLevel
				&& entityIn instanceof LivingEntity living
				&& EntityUtil.canTargetEntity(this, entityIn)
				&& this.tickCount % 5 == 0) {
			entityIn.hurtServer(serverLevel, PvZEntityDamageSource.causeCrushDamage(this),
					EntityUtil.getMaxHealthDamage(living, 0.5F));
		}
	}

	public void onBossSummon(ZombieEntity zombie, BlockPos pos) {
		com.lying.pvz.utils.ZombieUtil.copySummonZombieData(this, zombie);
		EntityUtil.onEntitySpawn(this.level(), zombie, pos);
	}

	protected int getNearbyPlantCount() {
		final float range = 50;
		return this.level().getEntitiesOfClass(PlantEntity.class,
				EntityUtil.getEntityAABB(this, range, range),
				plant -> EntityUtil.canTargetEntity(this, plant)
		).size();
	}

	protected int getNearbyZombieCount() {
		final float range = 40;
		return this.level().getEntitiesOfClass(ZombieEntity.class,
				EntityUtil.getEntityAABB(this, range, range),
				zombie -> !EntityUtil.canTargetEntity(this, zombie)
		).size();
	}

	@Override
	public float getKBValue() {
		return 1;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}


	@Override
	public boolean canTeleport(Level from, Level to) {
		return false;
	}
	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}
}