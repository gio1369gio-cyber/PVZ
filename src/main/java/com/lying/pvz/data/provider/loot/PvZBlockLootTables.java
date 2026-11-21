package com.lying.pvz.data.provider.loot;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PvZBlockLootTables extends BlockLootSubProvider {

	private final Set<Block> knownBlocks = new HashSet<>();

	public PvZBlockLootTables(LootTableSubProvider.Context context) {
		super(Set.of(), FeatureFlags.DEFAULT_FLAGS, context);
	}

	@Override
	protected void generate() {

		final Set<Block> noLootBlocks = new HashSet<>(Arrays.asList(
				PvZBlockRegister.LILY_PAD.get(),
				PvZBlockRegister.FLOWER_POT.get(),
				PvZBlockRegister.SLOT_MACHINE.get(),
				PvZBlockRegister.QUESTION_MARK_POT.get(),
				PvZBlockRegister.PLANT_POT.get(),
				PvZBlockRegister.WATER_POT.get(),
				PvZBlockRegister.POT_GRASS.get()
		));

		// 矿石
		Arrays.asList(
						PvZBlockRegister.ORIGIN_ORE.get(),     PvZBlockRegister.APPEASE_ORE.get(),
						PvZBlockRegister.LIGHT_ORE.get(),      PvZBlockRegister.EXPLOSION_ORE.get(),
						PvZBlockRegister.DEFENCE_ORE.get(),    PvZBlockRegister.ICE_ORE.get(),
						PvZBlockRegister.ENFORCE_ORE.get(),    PvZBlockRegister.TOXIC_ORE.get(),
						PvZBlockRegister.ASSIST_ORE.get(),     PvZBlockRegister.MAGIC_ORE.get(),
						PvZBlockRegister.FLAME_ORE.get(),      PvZBlockRegister.SPEAR_ORE.get(),
						PvZBlockRegister.ARMA_ORE.get(),       PvZBlockRegister.ELECTRIC_ORE.get(),
						PvZBlockRegister.SHADOW_ORE.get())
				.forEach(object -> this.add(object,
						block -> createOreDrop(block, object.getEssence().getEssenceItem())));

		// 作物
		this.add(PvZBlockRegister.CABBAGE.get(),
				createCropDrops(
						PvZBlockRegister.CABBAGE.get(),
						PvZItemRegister.CABBAGE.get(),
						PvZBlockRegister.CABBAGE.get().asItem(),
						getAgeBuilder(PvZBlockRegister.CABBAGE.get(), 3)));

		this.add(PvZBlockRegister.CORN.get(),
				createCropDrops(
						PvZBlockRegister.CORN.get(),
						PvZItemRegister.CORN.get(),
						PvZBlockRegister.CORN.get().asItem(),
						getAgeBuilder(PvZBlockRegister.CORN.get(), 7)));

		this.add(PvZBlockRegister.CHILI.get(),
				createCropDrops(
						PvZBlockRegister.CHILI.get(),
						PvZItemRegister.PEPPER.get(),
						PvZBlockRegister.CHILI.get().asItem(),
						getAgeBuilder(PvZBlockRegister.CHILI.get(), 3)));

		this.add(PvZBlockRegister.GARLIC.get(),
				createCropDrops(
						PvZBlockRegister.GARLIC.get(),
						PvZItemRegister.GARLIC.get(),
						PvZBlockRegister.GARLIC.get().asItem(),
						getAgeBuilder(PvZBlockRegister.GARLIC.get(), 3)));



		// 树叶：坚果树叶额外掉落坚果
		this.add(PvZBlockRegister.NUT_LEAVES.get(), (leaves) -> {
			var fortune = this.enchantments.getOrThrow(Enchantments.FORTUNE);
			// 无剪/无精准采集：用 AnyOfCondition 承接两个 Holder<LootItemCondition>
			LootItemCondition.Builder noShearsOrSilkTouch =
					new AnyOfCondition.Builder(new LootItemCondition.Builder[0])
							.or(this.hasShears())
							.or(this.hasSilkTouch())
							.invert();
			return createLeavesDrops(leaves, PvZBlockRegister.NUT_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES)
					.withPool(LootPool.lootPool()
							.setRolls(ContextIntProviders.exactly(1))
							.when(noShearsOrSilkTouch)
							.add(applyExplosionCondition(leaves, LootItem.lootTableItem(PvZItemRegister.NUT.get()))
									.when(BonusLevelTableCondition.bonusLevelFlatChance(fortune,
											0.01F, 0.015F, 0.02F, 0.025F, 0.03F))));
		});

		// 金砖
		this.dropOther(PvZBlockRegister.GOLD_TILE1.get(), Blocks.GOLD_BLOCK);
		this.dropOther(PvZBlockRegister.GOLD_TILE2.get(), Blocks.GOLD_BLOCK);
		this.dropOther(PvZBlockRegister.GOLD_TILE3.get(), Blocks.GOLD_BLOCK);


		this.add(PvZBlockRegister.SCREEN_DOOR.get(),
				block -> LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
						.add(LootItem.lootTableItem(PvZItemRegister.SCREEN_DOOR.get()))));


		BuiltInRegistries.BLOCK.forEach(block -> {
			Identifier id = BuiltInRegistries.BLOCK.getKey(block);
			if (id != null
					&& id.getNamespace().equals(PvZMod.MOD_ID)
					&& !noLootBlocks.contains(block)
					&& !this.knownBlocks.contains(block)) {
				this.dropSelf(block);
			}
		});
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return this.knownBlocks;
	}

	@Override
	protected void add(Block block, LootTable.Builder builder) {
		super.add(block, builder);
		this.knownBlocks.add(block);
	}

	private LootItemCondition.Builder getAgeBuilder(Block block, int age) {
		return MatchBlock.blockMatches(
				this.blocks, block,
				StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, age));
	}
}