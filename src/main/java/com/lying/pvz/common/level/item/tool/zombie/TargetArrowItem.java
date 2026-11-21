package com.lying.pvz.common.level.item.tool.zombie;

import com.lying.pvz.common.level.entity.projectile.TargetArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class TargetArrowItem extends ArrowItem {

	public TargetArrowItem(Properties props) {
		super(props);
	}

	@Override
	public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter,
                                     @Nullable ItemStack weapon) {

        return new TargetArrowEntity(level, shooter);
	}

	@Override
	public boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity livingEntity) {
		return false;
	}

}