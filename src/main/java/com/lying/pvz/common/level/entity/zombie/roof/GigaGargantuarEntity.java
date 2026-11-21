package com.lying.pvz.common.level.entity.zombie.roof;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.utils.EntityUtil;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

/**
 * 巨人僵尸（GIGA 变异版）。
 *
 * <p>继承 {@link GargantuarEntity}，本体 600 血、行走
 * {@link ZombieUtil#WALK_SLOW}（略快于普通巨人）。差异点：
 * <ul>
 *   <li>攻击时先执行 { #groundShack(Entity)}，4 格内其他可攻击目标
 *       各受 50% 最大生命伤害并播放粒子；</li>
 *   <li>碾压伤害倍率提升到 4 倍最大生命；</li>
 *   <li>投掷小鬼时一次抛出 3 只（{@code throwImp} 循环调用父类）。</li>
 * </ul>
 */
public class GigaGargantuarEntity extends GargantuarEntity {

	public GigaGargantuarEntity(EntityType<? extends GargantuarEntity> type, Level worldIn) {
		super(type, worldIn);
		this.isSad = true;
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity target) {
		this.groundShack(level, target);
		return super.doHurtTarget(level, target);
	}

	private void groundShack(ServerLevel level, Entity entity) {
		final float range = 4;
		EntityUtil.getTargetableLivings(this, EntityUtil.getEntityAABB(this, range, range))
				.forEach(target -> {
					if (!target.is(entity)) {
						target.hurtServer(level, this.getZombieAttackDamageSource(),
								EntityUtil.getMaxHealthDamage(target, 0.5F));
						for (int i = 0; i < 5; ++i) {
							EntityUtil.spawnParticle(target, 6);
						}
					}
				});
	}

	@Override
	protected float getModifyAttackDamage(Entity entity, float f) {
		if (entity instanceof LivingEntity living) {
			return EntityUtil.getMaxHealthDamage(living, 4);
		}
		return f;
	}

	@Override
	public void throwImp(LivingEntity target) {
		for (int i = 0; i < 3; ++i) {
			super.throwImp(target);
		}
	}

	@Override
	public float getLife() {
		return 600;
	}

	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_SLOW;
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose poseIn) {
		if (this.isMiniZombie()) {
			return EntityDimensions.scalable(0.7F, 2F);
		}
		return super.getDefaultDimensions(poseIn);
	}

	@Override
	public IZombieType getZombieType() {
		return RoofZombies.GIGA_GARGANTUAR.get();
	}
}