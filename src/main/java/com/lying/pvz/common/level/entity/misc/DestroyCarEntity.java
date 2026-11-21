package com.lying.pvz.common.level.entity.misc;

import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.level.entity.AbstractOwnerEntity;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.MathUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 破坏车：从高空向目标抛物线飞行，落地或 100 tick 后消失，
 * 每 15 tick 对周围目标造成致命伤害。
 */
public class DestroyCarEntity extends AbstractOwnerEntity {

	protected final float height = 20;

	public DestroyCarEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			if (this.tickCount >= 100 || this.onGround()) {
				this.discard();
			}
		}
		this.tickMove();
		this.tickCollision();
	}

	public void shootPultBullet(LivingEntity target) {
		if (target == null) {
			return;
		}
		double g = this.getGravityVelocity();
		double t1 = Mth.sqrt((float) (2 * height / g));
		double t2 = 0;
		if (this.getY() + height - target.getY() - target.getBbHeight() >= 0) {
			t2 = Mth.sqrt((float) (2 * (this.getY() + height - target.getY() - target.getBbHeight()) / g));
		}
		double dx = target.getX() + target.getDeltaMovement().x * (t1 + t2) - this.getX();
		double dz = target.getZ() + target.getDeltaMovement().z * (t1 + t2) - this.getZ();
		double dxz = Mth.sqrt((float) (dx * dx + dz * dz));
		double vxz = dxz / (t1 + t2);
		double vy = g * t1;
		this.setDeltaMovement(vxz * dx / dxz, vy + MathUtil.getRandomFloat(this.random) / 10, vxz * dz / dxz);
	}

	private void tickCollision() {
		if (this.level() instanceof ServerLevel serverLevel && this.tickCount % 15 == 0) {
			EntityUtil.getTargetableEntities(this.getOwnerOrSelf(), this.getBoundingBox().inflate(1F))
					.forEach(target -> {
						if (target instanceof LivingEntity living) {
							target.hurtServer(serverLevel,
									PvZEntityDamageSource.causeDeadlyDamage(this, this.getOwner()),
									EntityUtil.getCurrentMaxHealth(living));
						}
					});
		}
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return EntityDimensions.scalable(2F, 2F);
	}
}