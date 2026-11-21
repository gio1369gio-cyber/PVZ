package com.lying.pvz.common.level.entity.plant.fire;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.damage.PvZEntityDamageSource;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.entity.misc.ElementBallEntity;
import com.lying.pvz.common.level.entity.misc.ElementBallEntity.ElementTypes;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.base.PlantBomberEntity;
import com.lying.pvz.common.level.entity.zombie.zombotany.JalapenoZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.EventHooks;

/**
 * 火爆辣椒。
 *
 * <p>爆炸范围 20 格，沿 X/Z 轴两条窄带 + 中心区域造成火焰伤害；
 * 顺带清除雪层/雪块（{@code EventHooks.canEntityGrief} 允许时），并清除 40 格内
 * {@link ElementTypes#ICE} 系元素球。
 */
public class JalapenoEntity extends PlantBomberEntity {

	public JalapenoEntity(EntityType<? extends PlantBomberEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void startBomb(boolean server) {
		final float range = this.getExplodeRange();
		if (server) {
			fireMob(this, range, 1F);
			fireMob(this, 1F, range);
			ElementBallEntity.killElementBalls(this, 40, ElementTypes.ICE);
			EntityUtil.playSound(this, PvZSoundRegister.JALAPENO.get());
		}
		clearSnowAndSpawnFlame(this, (int) range);
	}

	public static void fireMob(LivingEntity entity, float dx, float dz) {
		final AABB aabb = new AABB(
				entity.position().add(dx, 1, dz),
				entity.position().add(-dx, -1, -dz));
		for (Entity target : EntityUtil.getWholeTargetableEntities(entity, aabb)) {
			float damage = 0;
			if (entity instanceof JalapenoEntity jalapeno) {
				damage = jalapeno.getExplodeDamage();
			} else if (entity instanceof JalapenoZombieEntity) {
				if (target instanceof LivingEntity living) {
					damage = EntityUtil.getMaxHealthDamage(living, 2);
				} else {
					damage = 100F;
				}
			}
			target.hurt(PvZEntityDamageSource.causeFlameDamage(entity, entity).setExplosion(), damage);
		}
		PlantEntity.clearLadders(entity, aabb);
	}

	public static void clearSnowAndSpawnFlame(LivingEntity entity, int range) {
		// 1. 粒子放到服务端广播，客户端所有人可见
		if (entity.level() instanceof ServerLevel serverLevel) {
			for (int i = -range; i <= range; ++i) {
				spawnFlame(serverLevel, entity, i, 0);
				spawnFlame(serverLevel, entity, 0, i);
			}
		}

		// 2. 雪只清一次（服务端 + 允许破坏）
		if (entity.level() instanceof ServerLevel serverLevel
				&& EventHooks.canEntityGrief(serverLevel, entity)) {
			for (int i = -range; i <= range; ++i) {
				clearSnow(entity, i, 0);
				clearSnow(entity, 0, i);
			}
			final int half = range / 2;
			for (int i = -half; i <= half; ++i) {
				for (int j = -half; j <= half; ++j) {
					clearSnow(entity, i, j);
				}
			}
		}
	}

	private static void spawnFlame(ServerLevel level, LivingEntity entity, int dx, int dz) {
		level.sendParticles(
				ParticleTypes.FLAME,
				entity.getX() + dx,
				entity.getY() + 0.5,
				entity.getZ() + dz,
				20,        // count
				0.1, 0.1, 0.1,  // 随机偏移范围
				0.1        // 速度
		);
	}

	private static void clearSnow(LivingEntity entity, int dx, int dz) {
		if (entity.level().isClientSide()) {
			return;
		}
		final BlockPos pos = entity.blockPosition().offset(dx, 0, dz);
		if (entity.level().getBlockState(pos).is(BlockTags.SNOW)) {
			entity.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}

	@Override
	public float getExplodeRange() {
		return 20;
	}

	@Override
	public float getExplodeDamage() {
		return this.getSkillValue(SkillTypes.NORMAL_BOMB_DAMAGE.get());
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		return EntityDimensions.scalable(0.7F, 1.5F);
	}

	@Override
	public int getReadyTime() {
		return 20;
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.JALAPENO.get();
	}
}