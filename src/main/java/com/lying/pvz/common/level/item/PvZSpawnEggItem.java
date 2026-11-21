package com.lying.pvz.common.level.item;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PvZSpawnEggItem extends Item {

	protected static final List<PvZSpawnEggItem> PVZ_EGGS = new ArrayList<>();

	private final Supplier<? extends EntityType<?>> entityTypeSupplier;
	private final int backgroundColor;
	private final int highlightColor;

	public PvZSpawnEggItem(Supplier<? extends EntityType<?>> entityTypeSupplier,
						   int primaryColorIn, int secondaryColorIn, Properties builder) {
		super(builder);
		this.entityTypeSupplier = entityTypeSupplier;
		this.backgroundColor = primaryColorIn;
		this.highlightColor = secondaryColorIn;
		PVZ_EGGS.add(this);
	}

	public EntityType<?> getType() {
		return this.entityTypeSupplier.get();
	}

	public int getColor(int tintIndex) {
		return tintIndex == 0 ? this.backgroundColor : this.highlightColor;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.SUCCESS;
		}
		ItemStack stack = context.getItemInHand();
		EntityType<?> type = this.getType();
		Entity entity = type.spawn(serverLevel, stack, context.getPlayer(),
				context.getClickedPos().relative(context.getClickedFace()),
				EntitySpawnReason.SPAWN_ITEM_USE, false, false);
		if (entity == null) {
			return InteractionResult.PASS;
		}
		if (context.getPlayer() != null && context.getPlayer().isCreative()) {
			// 创造模式不消耗
		} else {
			stack.shrink(1);
		}
		return InteractionResult.CONSUME;
	}

}