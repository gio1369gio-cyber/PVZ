package com.lying.pvz.common.level.entity.bowling;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.init.PvZTriggers;
import com.lying.pvz.common.level.entity.drop.CoinEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * 坚果保龄球。
 *
 * <p>每次命中目标时：
 * <ul>
 *   <li>{@code hitCount} 自增，并据当前连击数在目标周围生成
 *       {@code hitCount - 1} 枚金币；</li>
 *   <li>对目标造成 30 点普通伤害，伤害源携带 {@code setCount(hitCount)}，
 *       用于区分连击阶段（音效、动画）。</li>
 * </ul>
 *
 * <p>若 owner 是 {@link ServerPlayer}，同时触发
 * {@link PvZTriggers#ENTITY_EFFECT_AMOUNT} 进度（连击数作为参数）。
 * 命中音效 {@link PvZSoundRegister#BOWLING_HIT}。
 */
public class WallNutBowlingEntity extends AbstractBowlingProjectile {

	public WallNutBowlingEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}

	public WallNutBowlingEntity(EntityType<?> type, Level worldIn, Player entity) {
		super(type, worldIn);
		if (entity != null) {
			this.setOwnerUUID(entity.getUUID());
			this.ownerGroup = com.lying.pvz.common.level.entity.EntityGroupHander.getEntityGroupType(entity);
		}
	}

	@Override
	protected void dealDamageTo(Entity entity) {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		++this.hitCount;
		for (int i = 1; i < this.hitCount; ++i) {
			CoinEntity coin = PvZEntityRegister.COIN.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
			if (coin == null) {
				continue;
			}
			coin.setAmount(1);
			EntityUtil.onEntityRandomPosSpawn(this.level(), coin, this.blockPosition(), 1);
		}
		entity.hurtServer(serverLevel,
				PvZEntityDamageSource.normal(this, this.resolveOwner()).setCount(this.hitCount), 30);
		EntityUtil.playSound(this, PvZSoundRegister.BOWLING_HIT.get());
		Entity owner = this.resolveOwnerEntity();
		if (owner instanceof ServerPlayer player) {
			PvZTriggers.ENTITY_EFFECT_AMOUNT.get().trigger(player, this, this.hitCount);
		}
	}

	@Nullable
	private LivingEntity resolveOwner() {
		final Entity entity = this.resolveOwnerEntity();
		return entity instanceof LivingEntity living ? living : null;
	}

	@Nullable
	private Entity resolveOwnerEntity() {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return null;
		}
		final Optional<UUID> ownerId = this.getOwnerUUID();
		if (ownerId.isEmpty()) {
			return null;
		}
		return serverLevel.getEntity(ownerId.get());
	}
}