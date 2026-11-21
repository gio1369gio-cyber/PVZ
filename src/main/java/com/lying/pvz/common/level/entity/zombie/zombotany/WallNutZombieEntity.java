package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * 坚果僵尸。
 *
 * <p>本体 160 血，不覆写属性，走父类默认行走速度。纯肉盾型，
 * 不发射弹药。
 */
public class WallNutZombieEntity extends AbstractZombotanyEntity {

	public WallNutZombieEntity(EntityType<? extends AbstractZombotanyEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getLife() {
		return 160;
	}

	@Override
	public IZombieType getZombieType() {
		return Zombotanies.WALLNUT_ZOMBIE.get();
	}
}