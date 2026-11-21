package com.lying.pvz.common.level.entity.bowling;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZParticleRegister;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.EntityGroupHander;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * 爆炸保龄球。
 *
 * <p>首次命中实体时由父类 {@code tickCollision()} 触发
 * {@link #changeDirection()}：2.5 格内可攻击目标各受 180 点爆炸伤害，
 * 同时把自身朝向切到 {@link BowlingFacing#BOMB}。切到 BOMB 之后
 * {@link #tickCollision()} 会在下一个 tick 生成爆炸粒子并 {@code discard()}。
 *
 * <p>伤害源为 {@link PvZEntityDamageSource#explode(Entity)}，
 * 命中音效 {@link PvZSoundRegister#CHERRY_BOMB}。
 */
public class ExplosionBowlingEntity extends AbstractBowlingProjectile {

	public ExplosionBowlingEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}

	public ExplosionBowlingEntity(EntityType<?> type, Level worldIn, Player entity) {
		super(type, worldIn);
		if (entity != null) {
			this.setOwnerUUID(entity.getUUID());
			this.ownerGroup = EntityGroupHander.getEntityGroupType(entity);
		}
	}

	@Override
	protected void tickCollision() {
		if (this.getBowlingFacing() == BowlingFacing.BOMB) {
			this.bomb();
			this.discard();
		} else {
			super.tickCollision();
		}
	}

	private void bomb() {
		// 爆炸粒子在服务端一次性生成多个（带随机偏移）并广播，避免只画 1 个几乎不可见。
		if (this.level() instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(PvZParticleRegister.RED_BOMB.get(),
					this.getX(), this.getY(), this.getZ(), 40, 0.6D, 0.6D, 0.6D, 0.2D);
		}
	}

	@Override
	protected void changeDirection() {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		final float len = 2.5F;
		final AABB aabb = EntityUtil.getEntityAABB(this, len, len);
		final LivingEntity owner = this.resolveOwner();
		// 爆炸是无差别范围伤害：不再按阵营过滤目标，范围内的所有生物（含动物、玩家）都会受伤，
		// 仅排除投掷者本人（owner）。
		serverLevel.getEntitiesOfClass(LivingEntity.class, aabb).stream()
				.filter(target -> target != owner && !target.is(this))
				.forEach(target -> target.hurtServer(serverLevel, PvZEntityDamageSource.explode(this), 180));
		EntityUtil.playSound(this, PvZSoundRegister.CHERRY_BOMB.get());
		this.setBowlingFacing(BowlingFacing.BOMB);
	}

	@Override
	protected void dealDamageTo(Entity entity) {
	}

	@Nullable
	private LivingEntity resolveOwner() {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return null;
		}
		final Optional<UUID> ownerId = this.getOwnerUUID();
		if (ownerId.isEmpty()) {
			return null;
		}
		final Entity entity = serverLevel.getEntity(ownerId.get());
		return entity instanceof LivingEntity living ? living : null;
	}
}