package com.lying.pvz.common.level.entity.zombie.pool;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.EntityUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 雪橇车僵尸的个体单位。
 *
 * <p>本体 20 血。每 30 tick 若站在雪或冰上，获得 2 秒
 * {@link MobEffects#SPEED}（等级 0）与 {@link MobEffects#REGENERATION}（等级 3），
 * 模拟“雪地加成”。是 {@link BobsleTeamEntityPVZ} 死亡时的分裂产物。
 */
public class BobsleZombieEntity extends PVZZombieEntity {

	public BobsleZombieEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public void zombieTick() {
		super.zombieTick();
		if (!this.level().isClientSide()) {
			if (this.tickCount % 30 == 0 && (EntityUtil.isOnSnow(this) || EntityUtil.isOnIce(this))) {
				this.addEffect(new MobEffectInstance(MobEffects.SPEED, 40, 0, false, false));
				this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 3, false, false));
			}
		}
	}

	@Override
	public float getLife() {
		return 20;
	}

	@Override
	public IZombieType getZombieType() {
		return PoolZombies.BOBSLE_ZOMBIE.get();
	}
}