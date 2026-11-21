package com.lying.pvz.common.level.entity.projectile;

import com.lying.pvz.api.entity.IPAZEntity;
import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.api.entity.projectile.IPAZProjectile;
import com.lying.pvz.api.projectile.Mark;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.entity.zombie.roof.BungeeZombieEntity;
import com.lying.pvz.common.projectile.mark.MarkRegistry;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * 蹦极目标箭。
 *
 * <p>继承 {@link AbstractArrow} 走原版箭物理/渲染/拾取，同时实现 {@link IPAZProjectile}
 * 让“是否是弹药”的判定统一到接口层。
 *
 * <p>追踪逻辑：{@code tick} 中若 owner 是 {@link BungeeZombieEntity} 且持有
 * {@code stealTarget}，则每 tick 朝该目标修正朝向；否则 {@code discard}。
 *
 * <p>命中：owner 是玩家且目标是可被偷取实体时，生成 {@link BungeeZombieEntity}
 * 并附带 stealTarget；其余走原版箭命中逻辑。
 */
public class TargetArrowEntity extends AbstractArrow implements IPAZProjectile {

	private static final int DEFAULT_MAX_LIVE_TICK = 1200;

	private GroupType ownerGroup = GroupType.NEUTRALS;
	private int cachedMaxLiveTick = DEFAULT_MAX_LIVE_TICK;
	@Nullable
	private LivingEntity homingTarget;

	public TargetArrowEntity(EntityType<? extends AbstractArrow> type, Level worldIn) {
		super(type, worldIn);
	}

	public TargetArrowEntity(Level worldIn, LivingEntity owner) {
		super(PvZEntityRegister.TARGET_ARROW.get(), owner, worldIn,
				new ItemStack(PvZItemRegister.TARGET_ARROW.get()), null);
		// 标记箭：无重力直线飞行，不造成伤害。
		this.setNoGravity(true);
		this.setBaseDamage(0.0D);
	}

	/* ================================================================== */
	/* IPAZProjectile                                                      */
	/* ================================================================== */

	@Override
	public void shootFrom(IPAZEntity shooter) {
		this.ownerGroup = shooter.getEntityGroupType();
		if (shooter instanceof Entity entity) {
			this.setOwner(entity);
		}
	}

	@Override
	public void aimAt(LivingEntity target) {
		this.homingTarget = target;
	}

	@Override
	public void aimAt(Vec3 direction) {
		final Vec3 normalized = direction.normalize().scale(1.3D);
		this.lerpMotion(normalized);
	}
	@Override
	public GroupType getOwnerGroup() {
		return this.ownerGroup;
	}

	@Override
	public int getMaxLiveTick() {
		return this.cachedMaxLiveTick;
	}

	@Override
	public void setMaxLiveTick(int ticks) {
		this.cachedMaxLiveTick = ticks;
	}

	/* ================================================================== */
	/* tick                                                                */
	/* ================================================================== */

	@Override
	public void tick() {
		if (!this.level().isClientSide()) {
			final Entity owner = this.getOwner();
			if (!EntityUtil.isEntityValid(owner)) {
				this.discard();
				return;
			}
			if (owner instanceof BungeeZombieEntity bungee
					&& !EntityUtil.isEntityValid(bungee.getStealTarget())) {
				this.discard();
				return;
			}
		}
		super.tick();
	}
	/* ================================================================== */
	/* 命中                                                                */
	/* ================================================================== */

	@Override
	protected void onHitEntity(EntityHitResult result) {
		final Entity owner = this.getOwner();
		if (owner instanceof BungeeZombieEntity bungee) {
			// 只给 bungee 想偷的 stealTarget 打标记（不管箭先撞到的是谁），然后消失。
			if (!this.level().isClientSide()) {
				final LivingEntity stealTarget = bungee.getStealTarget();
				if (stealTarget instanceof Mark.Holder holder) {
					holder.addMark(new Mark.Key(MarkRegistry.BUNGEE_TARGET, owner.getUUID()), 1);
				}
			}
			this.discard();
			return;
		}

		if (result.getEntity() instanceof LivingEntity target
				&& owner instanceof Player) {
			if (!BungeeZombieEntity.canBungeeSteal(target)) {
				super.onHitEntity(result);
				return;
			}
			final BungeeZombieEntity zombie = PvZEntityRegister.BUNGEE_ZOMBIE.get().create(this.level(), EntitySpawnReason.MOB_SUMMONED);
			if (zombie == null) {
				super.onHitEntity(result);
				return;
			}
			zombie.setBungeeType(BungeeZombieEntity.BungeeTypes.HELP);
			zombie.setCharmed(true);
			zombie.setStealTarget(target);
			EntityUtil.onEntitySpawn(this.level(), zombie, this.blockPosition().above(20));
			super.onHitEntity(result);
			return;
		}
		super.onHitEntity(result);
	}

	/* ================================================================== */
	/* 尺寸 / 拾取                                                         */
	/* ================================================================== */

	@Override
	public Optional<UUID> getOwnerUUID() {
		final Entity owner = this.getOwner();
		return owner == null ? Optional.empty() : Optional.of(owner.getUUID());
	}
	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return EntityDimensions.scalable(0.5F, 0.5F);
	}


	@Override
	public byte getPierceLevel() {
		return 0;
	}

	/* ================================================================== */
	/* 存档                                                                */
	/* ================================================================== */

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("owner_group", this.ownerGroup.ordinal());
		output.putInt("max_live_tick", this.cachedMaxLiveTick);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		final GroupType[] groups = GroupType.values();
		final int ord = input.getIntOr("owner_group", GroupType.NEUTRALS.ordinal());
		this.ownerGroup = (ord >= 0 && ord < groups.length) ? groups[ord] : GroupType.NEUTRALS;
		this.cachedMaxLiveTick = input.getIntOr("max_live_tick", DEFAULT_MAX_LIVE_TICK);
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(PvZItemRegister.TARGET_ARROW.get());
	}

	/* ================================================================== */
	/* 网络                                                                */
	/* ================================================================== */

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
		return super.getAddEntityPacket(serverEntity);
	}
}
