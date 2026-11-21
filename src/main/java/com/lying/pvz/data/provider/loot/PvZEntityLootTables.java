package com.lying.pvz.data.provider.loot;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.misc.PvZLoot;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

/**
 * PVZ 实体战利品表生成器。
 *
 */
public class PvZEntityLootTables implements LootTableSubProvider {

	private final LootTableSubProvider.Context output;
	private final HolderGetter<Enchantment> enchantments;

	public PvZEntityLootTables(LootTableSubProvider.Context output) {
		this.output = output;
		this.enchantments = output.lookup(Registries.ENCHANTMENT);
	}

	@Override
	public void run() {
		this.output.accept(PvZLoot.NORMAL_ZOMBIE, getZombieLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.ZOMBIE_DOLL.get()))
						.add(LootItem.lootTableItem(Items.BAMBOO))
						.add(LootItem.lootTableItem(PvZItemRegister.ALMANAC.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.001F, 0.01F))
				));

		this.output.accept(PvZLoot.FLAG_ZOMBIE, getZombieLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.ZOMBIE_FLAG.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

		this.output.accept(PvZLoot.CONEHEAD_ZOMBIE, getZombieLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.CONE_HEAD.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

		this.output.accept(PvZLoot.BUCKETHEAD_ZOMBIE, getZombieLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.BUCKET_HEAD.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

		this.output.accept(PvZLoot.SCREENDOOR_ZOMBIE, getZombieLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.SCREEN_DOOR.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

		this.output.accept(PvZLoot.FOOTBALL_ZOMBIE, getZombieLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.FOOTBALL_HELMET.get()))
						.add(LootItem.lootTableItem(PvZItemRegister.FOOTBALL_CHESTPLATE.get()))
						.add(LootItem.lootTableItem(PvZItemRegister.FOOTBALL_LEGGINGS.get()))
						.add(LootItem.lootTableItem(PvZItemRegister.FOOTBALL_BOOTS.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.01F, 0.01F))
				));

		this.output.accept(PvZLoot.GIGA_FOOTBALL_ZOMBIE, getZombieLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.GIGA_HELMET.get()))
						.add(LootItem.lootTableItem(PvZItemRegister.GIGA_CHESTPLATE.get()))
						.add(LootItem.lootTableItem(PvZItemRegister.GIGA_LEGGINGS.get()))
						.add(LootItem.lootTableItem(PvZItemRegister.GIGA_BOOTS.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.01F, 0.01F))
				));

		this.output.accept(PvZLoot.NOBLE_ZOMBIE, getZombieLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.TIME_KEY_TO_DEEP.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 1F, 0.01F)))
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(Items.MYCELIUM))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.08F, 0.01F))
				));

		this.output.accept(PvZLoot.COFFIN, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(Items.MYCELIUM))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.08F, 0.01F))
				));

		this.output.accept(PvZLoot.MOURNER_ZOMBIE, getZombieLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.SPORE.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.2F, 0.01F))
				));

		this.output.accept(PvZLoot.BOBSLE_TEAM, getZombieLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.BOBSLE_CAR.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.05F, 0.01F))
				));

		this.output.accept(PvZLoot.ZOMBIE_DOLPHIN, getRottenFleshLootTable(this.enchantments));

		this.output.accept(PvZLoot.FOODIE_ZOMBIE, getRottenFleshLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.REAL_BRAIN.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.2F, 0.01F))
				));

		this.output.accept(PvZLoot.LAVA_ZOMBIE, getRottenFleshLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.PEPPER.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.02F, 0.01F))
				));

		this.output.accept(PvZLoot.PUMPKIN_ZOMBIE, getRottenFleshLootTable(this.enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.CANDY.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.1F, 0.01F))
				).withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.PUMPKIN_ENJOY_CARD.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

		this.output.accept(PvZLoot.TRICK_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.CANDY.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.1F, 0.01F))
				));

		this.output.accept(PvZLoot.JACK_IN_BOX_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.JACK_BOX.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.01F, 0.01F))
				));

		this.output.accept(PvZLoot.BALLOON_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.BALLOON.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

		this.output.accept(PvZLoot.DIGGER_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.ORIGIN_PICKAXE.get()))
						.add(LootItem.lootTableItem(Items.DIAMOND_PICKAXE))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

		this.output.accept(PvZLoot.RA_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.RESOURCE_COLLECTOR.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.001F, 0.01F))
				));

		this.output.accept(PvZLoot.BUNGEE_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.TARGET_ARROW.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.01F, 0.01F))
				));

		this.output.accept(PvZLoot.LADDER_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZBlockRegister.STEEL_LADDER.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.1F, 0.01F))
				));

		this.output.accept(PvZLoot.GARGANTUAR, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.POLE.get()))
						.add(LootItem.lootTableItem(PvZItemRegister.ZOMBIE_DOLL.get()))
						.add(LootItem.lootTableItem(PvZItemRegister.WARNING_SIGN.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.01F, 0.01F))
				));

		this.output.accept(PvZLoot.GIGA_GARGANTUAR, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.POLE.get()))
						.add(LootItem.lootTableItem(PvZItemRegister.ZOMBIE_DOLL.get()))
						.add(LootItem.lootTableItem(PvZItemRegister.WARNING_SIGN.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.08F, 0.01F))
				));

		this.output.accept(PvZLoot.EDGAR_090505, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZBlockRegister.SILVER_SUNFLOWER_TROPHY.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 1F, 0.01F))
				).withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.ZOMBIE_ON_YOUR_LAWN.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 1F, 0.01F))
				).withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.TIME_SOURCE.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.6F, 0.01F))
				));

		this.output.accept(PvZLoot.EDGAR_090517, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZBlockRegister.GOLD_SUNFLOWER_TROPHY.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 1F, 0.01F))
				).withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.ZOMBIE_ON_YOUR_LAWN.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 1F, 0.01F))
				).withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.TIME_SOURCE.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.6F, 0.01F))
				));

		this.output.accept(PvZLoot.PEASHOOTER_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.PEA_SHOOTER_ENJOY_CARD.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

		this.output.accept(PvZLoot.WALLNUT_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.WALL_NUT_ENJOY_CARD.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

		this.output.accept(PvZLoot.GATLINGPEA_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.GATLING_PEA_ENJOY_CARD.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.004F, 0.01F))
				));

		this.output.accept(PvZLoot.TALLNUT_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.TALL_NUT_ENJOY_CARD.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

		this.output.accept(PvZLoot.SQUASH_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.SQUASH_ENJOY_CARD.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.008F, 0.01F))
				));

		this.output.accept(PvZLoot.JALAPENO_ZOMBIE, getLootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.JALAPENO_ENJOY_CARD.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.005F, 0.01F))
				));

	}

	private static LootTable.Builder getLootTable() {
		return LootTable.lootTable();
	}

	private static LootTable.Builder getRottenFleshLootTable(HolderGetter<Enchantment> enchantments) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH))
						.apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 2)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments, ContextFloatProviders.between(0.0F, 1.0F)))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(enchantments, 0.15F, 0.01F)));
	}

	private static LootTable.Builder getZombieLootTable(HolderGetter<Enchantment> enchantments) {
		return getRottenFleshLootTable(enchantments)
				.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.CORN_SEEDS.get()))
						.add(LootItem.lootTableItem(Items.SUNFLOWER))
						.add(LootItem.lootTableItem(PvZItemRegister.NUT.get()))
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(enchantments, 0.01F, 0.01F)));
	}
}