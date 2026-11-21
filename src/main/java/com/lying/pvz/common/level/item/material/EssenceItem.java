package com.lying.pvz.common.level.item.material;
import com.lying.pvz.api.content.IEssenceType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class EssenceItem extends Item {
	public final Supplier<IEssenceType> essence;
	public EssenceItem(Identifier id , Supplier<IEssenceType> essence) {
		super(new Properties().setId(ResourceKey.create(Registries.ITEM, id)));
		this.essence = essence;
	}

}