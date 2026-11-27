package com.lying.pvz.common.level.entity.misc;

import com.lying.pvz.api.entity.ability.GroupType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.AbstractOwnerEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.WorldUtil;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 僵尸之手：僵尸召唤的范围攻击视觉实体，短暂存在后对周围目标造成伤害并将其压入地下。
 * 本身不可交互、无碰撞、无重力。
 */
public class ZombieHandEntity extends AbstractOwnerEntity {

	private static final int MAX_LIFE_TICK = 40;

	private int lifeTick;

	public ZombieHandEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
		this.setPermanentlyInvulnerable(true);
		this.noPhysics = true;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.lifeTick < MAX_LIFE_TICK) {
			++this.lifeTick;
			return;
		}
		if (this.level() instanceof ServerLevel serverLevel) {
			this.performAttack(serverLevel);
			this.discard();
		}
	}

	public static void spawnRangeZombieHands(Level world, PVZZombieEntity zombie, int range) {
		for (int i = -range; i <= range; ++i) {
			for (int j = -range; j <= range; ++j) {
				final ZombieHandEntity hand = PvZEntityRegister.ZOMBIE_HAND.get()
						.create(world, EntitySpawnReason.MOB_SUMMONED);
				if (hand == null) {
					continue;
				}
				hand.summonByOwner(zombie);
				EntityUtil.onEntitySpawn(world, hand,
						WorldUtil.getSuitableHeightPos(world, zombie.blockPosition().offset(i, 0, j)));
			}
		}
	}

	protected void performAttack(ServerLevel serverLevel) {
		EntityUtil.getTargetableLivings(this, EntityUtil.getEntityAABB(this, 0.5f, 1f)).forEach(target -> {
			target.hurtServer(serverLevel,
					PvZEntityDamageSource.normal(this, this.getOwnerOrSelf()),
					this.getAttackDamage(target));
			target.setPos(target.getX(), target.getY() - 3, target.getZ());
		});
	}

	@Override
	public GroupType getInitialEntityGroup() {
		return GroupType.ZOMBIES;
	}

	private float getAttackDamage(LivingEntity target) {
		return 10;
	}

	public int getTick() {
		return this.lifeTick;
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return EntityDimensions.fixed(0.4f, 0.5f);
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}
}