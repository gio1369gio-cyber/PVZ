package com.lying.pvz.data.provider.loot;

import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.misc.PvZLoot;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

/**
 * PVZ 箱子战利品表生成器。
 *
 * <p>遵循 1.21.5+ 的 {@link LootTableSubProvider} 契约：
 * 构造接收 {@link LootTableSubProvider.Context}，落盘入口为 {@link LootTableSubProvider.Context#accept}，
 * 附魔 lookup 由 {@code Context#lookup(Registries.ENCHANTMENT)} 派生。
 */
public class PvZChestLootTables implements LootTableSubProvider {

	private final LootTableSubProvider.Context output;
	private final HolderGetter<Enchantment> enchantments;

	public PvZChestLootTables(LootTableSubProvider.Context output) {
		this.output = output;
		this.enchantments = output.lookup(Registries.ENCHANTMENT);
	}

	@Override
	public void run() {
		this.output.accept(PvZLoot.DAVE_VILLA_CHEST, LootTable.lootTable()
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.between(3, 5))
								.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 5))))
								.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 7))))
								.add(LootItem.lootTableItem(Items.EMERALD).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.WHEAT).setWeight(20).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 6))))
								.add(LootItem.lootTableItem(PvZItemRegister.PEA.get()).setWeight(20).apply(SetItemCountFunction.setCount(ContextIntProviders.between(4, 10))))
								.add(LootItem.lootTableItem(Items.PORKCHOP).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 5))))
								.add(LootItem.lootTableItem(PvZItemRegister.PEA_SHOOTER_ENJOY_CARD.get()).setWeight(10))
								.add(LootItem.lootTableItem(PvZItemRegister.SUN_FLOWER_ENJOY_CARD.get()).setWeight(10))
								.add(LootItem.lootTableItem(PvZItemRegister.CHERRY_BOMB_ENJOY_CARD.get()).setWeight(5))
								.add(LootItem.lootTableItem(PvZItemRegister.WALL_NUT_ENJOY_CARD.get()).setWeight(10))
								.add(LootItem.lootTableItem(PvZItemRegister.POTATO_MINE_ENJOY_CARD.get()).setWeight(8))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(20).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.enchantments)))
								.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20))
								.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
								.add(LootItem.lootTableItem(PvZItemRegister.GRASS_CARP_ENJOY_CARD.get()).setWeight(4))
								.add(LootItem.lootTableItem(PvZItemRegister.SCULK_SHROOM_ENJOY_CARD.get()).setWeight(8))
								.add(EmptyLootItem.emptyItem().setWeight(15)))
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(3))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 4))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8)))))
				// 原 BHT: .withPool(BHTLoot.pool3())
				// 原 BHT: .withPool(BHTLoot.pool4())
		);

		this.output.accept(PvZLoot.BUCKET_HOUSE_CHEST, LootTable.lootTable()
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.between(2, 4))
								.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 5))))
								.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 7))))
								.add(LootItem.lootTableItem(Items.EMERALD).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.BONE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(4, 6))))
								.add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(3, 7))))
								.add(LootItem.lootTableItem(PvZItemRegister.PEA_SHOOTER_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.SUN_FLOWER_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.CHERRY_BOMB_ENJOY_CARD.get()).setWeight(12).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.WALL_NUT_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.POTATO_MINE_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.SNOW_PEA_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.CHOMPER_ENJOY_CARD.get()).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.REPEATER_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(20).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.enchantments)))
								.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20))
								.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
								.add(LootItem.lootTableItem(PvZItemRegister.GRASS_CARP_ENJOY_CARD.get()).setWeight(4))
								.add(EmptyLootItem.emptyItem().setWeight(15)))
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(4))
								.add(LootItem.lootTableItem(Items.BONE).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8)))))
				// 原 BHT: .withPool(BHTLoot.pool3())
				// 原 BHT: .withPool(BHTLoot.pool4())
		);

		this.output.accept(PvZLoot.DOLPHIN_HOUSE_CHEST, LootTable.lootTable()
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.between(2, 4))
								.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 5))))
								.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 7))))
								.add(LootItem.lootTableItem(Items.EMERALD).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.BONE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(4, 6))))
								.add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(3, 7))))
								.add(LootItem.lootTableItem(PvZItemRegister.LILY_PAD_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.SQUASH_ENJOY_CARD.get()).setWeight(12).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.THREE_PEATER_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.TANGLE_KELP_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.JALAPENO_ENJOY_CARD.get()).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.SPIKE_WEED_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.TORCH_WOOD_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.TALL_NUT_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(20).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.enchantments)))
								.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20))
								.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
								.add(LootItem.lootTableItem(PvZItemRegister.GRASS_CARP_ENJOY_CARD.get()).setWeight(8))
								.add(EmptyLootItem.emptyItem().setWeight(15)))
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(4))
								.add(LootItem.lootTableItem(Items.BONE).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8)))))
				// 原 BHT: .withPool(BHTLoot.pool3())
				// 原 BHT: .withPool(BHTLoot.pool4())
		);

		this.output.accept(PvZLoot.GRAVE_YARD_CHEST, LootTable.lootTable()
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.between(2, 4))
								.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 5))))
								.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(20).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 7))))
								.add(LootItem.lootTableItem(Items.EMERALD).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.BONE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(4, 6))))
								.add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(3, 7))))
								.add(LootItem.lootTableItem(PvZItemRegister.PUFF_SHROOM_ENJOY_CARD.get()).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.SUN_SHROOM_ENJOY_CARD.get()).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.FUME_SHROOM_ENJOY_CARD.get()).setWeight(12).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.GRAVE_BUSTER_ENJOY_CARD.get()).setWeight(6).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.HYPNO_SHROOM_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.SCAREDY_SHROOM_ENJOY_CARD.get()).setWeight(12).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.ICE_SHROOM_ENJOY_CARD.get()).setWeight(6).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.DOOM_SHROOM_ENJOY_CARD.get()).setWeight(12).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(Items.WITHER_ROSE).setWeight(2).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(20).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.enchantments)))
								.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20))
								.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
								.add(LootItem.lootTableItem(PvZItemRegister.GRASS_CARP_ENJOY_CARD.get()).setWeight(8))
								.add(EmptyLootItem.emptyItem().setWeight(15)))
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(4))
								.add(LootItem.lootTableItem(Items.BONE).setWeight(20).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Blocks.GRAVEL).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8)))))
				// 原 BHT: .withPool(BHTLoot.pool3())
				// 原 BHT: .withPool(BHTLoot.pool4())
		);

		this.output.accept(PvZLoot.SUN_TEMPLE_CHEST, LootTable.lootTable()
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.between(2, 4))
								.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 5))))
								.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(40).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 7))))
								.add(LootItem.lootTableItem(Items.EMERALD).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.BONE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(4, 6))))
								.add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(3, 7))))
								.add(LootItem.lootTableItem(PvZItemRegister.CABBAGE_PULT_ENJOY_CARD.get()).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.FLOWER_POT_ENJOY_CARD.get()).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.KERNEL_PULT_ENJOY_CARD.get()).setWeight(12).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.COFFEE_BEAN_ENJOY_CARD.get()).setWeight(6).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.UMBRELLA_LEAF_ENJOY_CARD.get()).setWeight(8).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.GARLIC_ENJOY_CARD.get()).setWeight(12).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.MARIGOLD_ENJOY_CARD.get()).setWeight(6).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.MELON_PULT_ENJOY_CARD.get()).setWeight(12).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.SMALL_SUN_STORAGE_SAPLING.get()).setWeight(2).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(20).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.enchantments)))
								.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20))
								.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
								.add(EmptyLootItem.emptyItem().setWeight(15)))
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(4))
								.add(LootItem.lootTableItem(Items.BONE).setWeight(20).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Blocks.GRAVEL).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8)))))
				// 原 BHT: .withPool(BHTLoot.pool3())
				// 原 BHT: .withPool(BHTLoot.pool4())
		);

		this.output.accept(PvZLoot.YETI_HOUSE_CHEST, LootTable.lootTable()
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.between(2, 4))
								.add(LootItem.lootTableItem(Items.DIAMOND).setWeight(5).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 5))))
								.add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(2, 7))))
								.add(LootItem.lootTableItem(Items.BLUE_ICE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(Items.APPLE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 4))))
								.add(LootItem.lootTableItem(Items.SNOWBALL).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(4, 8))))
								.add(LootItem.lootTableItem(Items.BONE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(4, 6))))
								.add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 3))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(3, 7))))
								.add(LootItem.lootTableItem(PvZItemRegister.SEA_SHROOM_ENJOY_CARD.get()).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.PLANTERN_ENJOY_CARD.get()).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.CACTUS_ENJOY_CARD.get()).setWeight(6).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.BLOVER_ENJOY_CARD.get()).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.SPLIT_PEA_ENJOY_CARD.get()).setWeight(6).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.STAR_FRUIT_ENJOY_CARD.get()).setWeight(12).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.PUMPKIN_ENJOY_CARD.get()).setWeight(6).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(PvZItemRegister.MAGNET_SHROOM_ENJOY_CARD.get()).setWeight(15).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(Items.WITHER_ROSE).setWeight(2).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2))))
								.add(LootItem.lootTableItem(Items.BOOK).setWeight(20).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.enchantments)))
								.add(LootItem.lootTableItem(Items.GOLDEN_APPLE).setWeight(20))
								.add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
								.add(EmptyLootItem.emptyItem().setWeight(15)))
						.withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(4))
								.add(LootItem.lootTableItem(Items.BONE).setWeight(20).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Items.STRING).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8))))
								.add(LootItem.lootTableItem(Blocks.GRAVEL).setWeight(10).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 8)))))
				// 原 BHT: .withPool(BHTLoot.pool3())
				// 原 BHT: .withPool(BHTLoot.pool4())
		);
	}
}