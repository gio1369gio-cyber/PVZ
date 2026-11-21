package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * 高坚果僵尸。
 *
 * <p>本体 320 血，行走 {@link ZombieUtil#WALK_LITTLE_SLOW}。纯肉盾型，
 * 不发射弹药。血量与移动速度的搭配使其成为僵尸植物中最“厚”的一档。
 */
public class TallNutZombieEntity extends AbstractZombotanyEntity {

	public TallNutZombieEntity(EntityType<? extends AbstractZombotanyEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_LITTLE_SLOW);
		}
	}

	@Override
	public float getLife() {
		return 320;
	}

	@Override
	public IZombieType getZombieType() {
		return Zombotanies.TALLNUT_ZOMBIE.get();
	}
}