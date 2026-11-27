package com.lying.pvz.common.level.entity.zombie.grass;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * 普通僵尸。
 *
 * <p>最基础的僵尸实现：血量 20、行走 {@link ZombieUtil#WALK_NORMAL}。
 * {@link #rollVariantType()} 在父类 SUPER/SUN 判定之后追加 {@code BEARD} 变体：
 * 概率 1/200，命中时把攻击属性提升到 {@link ZombieUtil#NORMAL_DAMAGE}。
 *
 * <p>水下减速 0.91，为泳池里最慢的一档。
 */
public class NormalZombieEntity extends PVZZombieEntity {

	public NormalZombieEntity(EntityType<? extends PVZZombieEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected VariantType rollVariantType() {
		VariantType type = super.rollVariantType();
		if (type == VariantType.NORMAL) {
			if (this.getRandom().nextInt(200) == 0) {
				AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
				if (attackDamage != null) {
					attackDamage.setBaseValue(ZombieUtil.NORMAL_DAMAGE);
				}
				return VariantType.BEARD;
			}
		}
		return type;
	}

	@Override
	public float getLife() {
		return 20;
	}

	@Override
	protected float getWaterSlowDown() {
		return 0.91F;
	}

	@Override
	public IZombieType getZombieType() {
		return GrassZombies.NORMAL_ZOMBIE.get();
	}
}