package com.lying.pvz.data.provider.loot;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class PvZLootTableProvider extends LootTableProvider {

	public PvZLootTableProvider() {
		super(Set.of(), List.of(
				new SubProviderEntry(PvZChestLootTables::new,   LootContextParamSets.CHEST),
				new SubProviderEntry(PvZEntityLootTables::new,  LootContextParamSets.ENTITY),
				new SubProviderEntry(PvZBlockLootTables::new,   LootContextParamSets.BLOCK)
		));
	}
}