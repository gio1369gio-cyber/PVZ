package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 机枪豌豆僵尸。
 *
 * <p>继承 {@link PeaShooterZombieEntity}，将每轮发射数
 * {@link #getShootNum()} 提升到 4，本体 30 血。其余行为（冷却、
 * 目标选择、弹药）继承父类。
 */
public class GatlingPeaZombieEntity extends PeaShooterZombieEntity {

	public GatlingPeaZombieEntity(EntityType<? extends PeaShooterZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected int getShootNum() {
		return 4;
	}

	@Override
	public float getLife() {
		return 30;
	}

	@Override
	public IZombieType getZombieType() {
		return Zombotanies.GATLINGPEA_ZOMBIE.get();
	}
}