package com.lying.pvz.common.level.entity.misc;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.AbstractOwnerEntity;
import com.lying.pvz.common.level.entity.plant.enforce.ChomperEntity;
import com.lying.pvz.utils.EntityUtil;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 小咀嚼者：大咀嚼者攻击时的视觉效果实体，短暂存在后对周围目标造成一次啃咬伤害。
 * 本身不可交互、不受伤害、无碰撞、无重力。
 */
public class SmallChomperEntity extends AbstractOwnerEntity {

	private static final int MAX_LIFE_TICK = 20;

	private int lifeTick;

	public SmallChomperEntity(EntityType<?> entityTypeIn, Level worldIn) {
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

	protected void performAttack(ServerLevel serverLevel) {
		final Entity owner = this.getOwner();
		if (owner != null) {
			for (LivingEntity target : EntityUtil.getTargetableLivings(
					this, EntityUtil.getEntityAABB(this, 1.5F, 2F))) {
				target.hurtServer(serverLevel,
						PvZEntityDamageSource.eat(this, owner), getAttackDamage(owner));
			}
		}
		EntityUtil.playSound(this, PvZSoundRegister.BIG_CHOMP.get());
	}

	private float getAttackDamage(Entity owner) {
		if (owner instanceof ChomperEntity chomper) {
			return chomper.getAttackDamage();
		}
		return 40;
	}

	public int getTick() {
		return this.lifeTick;
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return EntityDimensions.fixed(0.4f, 0.5f);
	}
}