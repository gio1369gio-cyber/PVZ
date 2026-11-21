package com.lying.pvz.common.level.entity.zombie.zombotany;

import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import com.lying.pvz.utils.ZombieUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * 南瓜头僵尸。
 *
 * <p>本体 180 血，行走 {@link ZombieUtil#WALK_SLOW}。每次命中目标前
 * {@code heal(20)}：南瓜的“护甲”定位使其在长时间混战中靠持续自愈扛线。
 * 不发射弹药。
 */
public class PumpkinZombieEntity extends AbstractZombotanyEntity {

	public PumpkinZombieEntity(EntityType<? extends AbstractZombotanyEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (movementSpeed != null) {
			movementSpeed.setBaseValue(ZombieUtil.WALK_SLOW);
		}
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity entityIn) {
		this.heal(20);
		return super.doHurtTarget(level, entityIn);
	}

	@Override
	public float getLife() {
		return 180;
	}

	@Override
	public IZombieType getZombieType() {
		return Zombotanies.PUMPKIN_ZOMBIE.get();
	}
}