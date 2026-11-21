package com.lying.pvz.common.level.item.material;

import com.lying.pvz.api.content.IRankType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class TemplateCardItem extends Item {
	public final Supplier<IRankType> Rank;
	public TemplateCardItem(Identifier id, Supplier<IRankType> rank) {
		super(new Properties().setId(ResourceKey.create(Registries.ITEM, id)));
		this.Rank = rank;
	}

}