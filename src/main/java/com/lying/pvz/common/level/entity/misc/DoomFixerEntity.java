package com.lying.pvz.common.level.entity.misc;


import com.lying.pvz.common.level.entity.effect.OriginEffectEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class DoomFixerEntity extends OriginEffectEntity {

	public DoomFixerEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
		this.maxEffectTick = 40;
	}
}