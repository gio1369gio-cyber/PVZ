package com.lying.pvz.common.level.entity.bowling;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * 大坚果保龄球。
 *
 * <p>体积 2.5×2.5，体型远大于普通保龄球；命中单个实体造成 200 点普通伤害。
 * {@link #hitEntities} 用于避免同一目标被反复命中（父类
 * {@code AbstractPAZProjectile} 已提供 {@code IntOpenHashSet}）。
 *
 * <p>{@link #changeDirection()} 为空实现：大坚果不会因撞击而改变方向，
 * 只会把命中记录交给父类 {@code tickCollision()} 处理。
 */
public class GiantNutBowlingEntity extends AbstractBowlingProjectile {

	public GiantNutBowlingEntity(EntityType<?> type, Level worldIn) {
		super(type, worldIn);
	}

	public GiantNutBowlingEntity(EntityType<?> type, Level worldIn, Player entity) {
		super(type, worldIn);
		if (entity != null) {
			this.setOwnerUUID(entity.getUUID());
			this.ownerGroup = com.lying.pvz.common.level.entity.EntityGroupHander.getEntityGroupType(entity);
		}
	}

	@Override
	protected void dealDamageTo(Entity entity) {
		if (this.hitEntities.contains(entity.getId())) {
			return;
		}
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		entity.hurtServer(serverLevel, PvZEntityDamageSource.normal(this, this.resolveOwner()), 200);
		this.hitEntities.add(entity.getId());
		EntityUtil.playSound(this, PvZSoundRegister.BOWLING_HIT.get());
	}


	@Override
	protected void changeDirection() {
	}

	@Nullable
	private net.minecraft.world.entity.LivingEntity resolveOwner() {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return null;
		}
		final Optional<UUID> ownerId = this.getOwnerUUID();
		if (ownerId.isEmpty()) {
			return null;
		}
		final Entity entity = serverLevel.getEntity(ownerId.get());
		return entity instanceof net.minecraft.world.entity.LivingEntity living ? living : null;
	}
}