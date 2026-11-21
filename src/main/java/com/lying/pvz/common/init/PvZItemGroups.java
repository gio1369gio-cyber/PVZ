package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.common.enchantment.EnchantmentRegister;
import com.lying.pvz.common.level.item.display.ChallengeEnvelopeItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class PvZItemGroups {

	public static final DeferredRegister<CreativeModeTab> TABS =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PvZMod.MOD_ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PVZ_MISC =
			TABS.register("pvz_misc", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.pvz.pvz_misc"))
					.icon(() -> new ItemStack(PvZItemRegister.PEA.get()))
					.displayItems((params, output) -> {
								// 种子 / 材料
								output.accept(PvZItemRegister.PEA.get());
								output.accept(PvZItemRegister.NUT.get());
								output.accept(PvZItemRegister.SPORE.get());
								output.accept(PvZItemRegister.CABBAGE_SEEDS.get());
								output.accept(PvZItemRegister.CORN_SEEDS.get());
								output.accept(PvZItemRegister.CHILI_SEEDS.get());
								output.accept(PvZItemRegister.GARLIC.get());
								output.accept(PvZItemRegister.SNOW_PEA.get());
								output.accept(PvZItemRegister.FLAME_PEA.get());
								output.accept(PvZItemRegister.SMALL_MEAT.get());
								output.accept(PvZItemRegister.FROZEN_MELON_SLICE.get());
								// 精华
								output.accept(PvZItemRegister.ORIGIN_ESSENCE.get());
								output.accept(PvZItemRegister.APPEASE_ESSENCE.get());
								output.accept(PvZItemRegister.LIGHT_ESSENCE.get());
								output.accept(PvZItemRegister.EXPLOSION_ESSENCE.get());
								output.accept(PvZItemRegister.DEFENCE_ESSENCE.get());
								output.accept(PvZItemRegister.ICE_ESSENCE.get());
								output.accept(PvZItemRegister.ENFORCE_ESSENCE.get());
								output.accept(PvZItemRegister.TOXIC_ESSENCE.get());
								output.accept(PvZItemRegister.ASSIST_ESSENCE.get());
								output.accept(PvZItemRegister.MAGIC_ESSENCE.get());
								output.accept(PvZItemRegister.FLAME_ESSENCE.get());
								output.accept(PvZItemRegister.SPEAR_ESSENCE.get());
								output.accept(PvZItemRegister.ARMA_ESSENCE.get());
								output.accept(PvZItemRegister.ELECTRIC_ESSENCE.get());
								output.accept(PvZItemRegister.SHADOW_ESSENCE.get());
								// 锭
								output.accept(PvZItemRegister.ORIGIN_INGOT.get());
								output.accept(PvZItemRegister.AMETHYST_INGOT.get());
								// 卡片模板
								output.accept(PvZItemRegister.GRAY_CARD.get());
								output.accept(PvZItemRegister.WHITE_CARD.get());
								output.accept(PvZItemRegister.GREEN_CARD.get());
								output.accept(PvZItemRegister.BLUE_CARD.get());
								output.accept(PvZItemRegister.PURPLE_CARD.get());
								output.accept(PvZItemRegister.GOLD_CARD.get());
								output.accept(PvZItemRegister.RED_CARD.get());
								output.accept(PvZItemRegister.BLACK_CARD.get());
								output.accept(PvZItemRegister.MEGA_CARD.get());
								// 杂项
								output.accept(PvZItemRegister.TIME_SOURCE.get());
								// 刷怪蛋
								List.of(
										PvZItemRegister.SUN_SPAWN_EGG, PvZItemRegister.COIN_SPAWN_EGG,
										PvZItemRegister.JEWEL_SPAWN_EGG, PvZItemRegister.ENERGY_SPAWN_EGG,
										PvZItemRegister.FOODIE_ZOMBIE_SPAWN_EGG, PvZItemRegister.CRAZY_DAVE_SPAWN_EGG,
										PvZItemRegister.PANNEY_SPAWN_EGG, PvZItemRegister.SUN_DAVE_SPAWN_EGG,
										PvZItemRegister.NORMAL_ZOMBIE_SPAWN_EGG, PvZItemRegister.FLAG_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.CONEHEAD_ZOMBIE_SPAWN_EGG, PvZItemRegister.POLE_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.BUCKETHEAD_ZOMBIE_SPAWN_EGG, PvZItemRegister.NEWSPAPER_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.TOMB_STONE_SPAWN_EGG, PvZItemRegister.SCREENDOOR_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.FOOTBALL_ZOMBIE_SPAWN_EGG, PvZItemRegister.DANCING_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.BACKUP_DANCER_SPAWN_EGG, PvZItemRegister.GIGA_FOOTBALL_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.OLD_ZOMBIE_SPAWN_EGG, PvZItemRegister.SUNDAY_EDITION_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.SNORKEL_ZOMBIE_SPAWN_EGG, PvZItemRegister.ZOMBONI_SPAWN_EGG,
										PvZItemRegister.BOBSLE_TEAM_SPAWN_EGG, PvZItemRegister.BOBSLE_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.ZOMBIE_DOLPHIN_SPAWN_EGG, PvZItemRegister.DOLPHIN_RIDER_SPAWN_EGG,
										PvZItemRegister.DOLPHIN_RIDER_ZOMBIE_SPAWN_EGG, PvZItemRegister.LAVA_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.JACK_IN_BOX_ZOMBIE_SPAWN_EGG, PvZItemRegister.BALLOON_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.DIGGER_ZOMBIE_SPAWN_EGG, PvZItemRegister.POGO_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.YETI_ZOMBIE_SPAWN_EGG, PvZItemRegister.BUNGEE_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.LADDER_ZOMBIE_SPAWN_EGG, PvZItemRegister.CATAPULT_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.GARGANTUAR_SPAWN_EGG, PvZItemRegister.IMP_SPAWN_EGG,
										PvZItemRegister.GIGA_GARGANTUAR_SPAWN_EGG, PvZItemRegister.EDGAR_090505_SPAWN_EGG,
										PvZItemRegister.EDGAR_090517_SPAWN_EGG, PvZItemRegister.PEASHOOTER_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.WALLNUT_ZOMBIE_SPAWN_EGG, PvZItemRegister.GATLINGPEA_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.TALLNUT_ZOMBIE_SPAWN_EGG, PvZItemRegister.SQUASH_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.JALAPENO_ZOMBIE_SPAWN_EGG, PvZItemRegister.PUMPKIN_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.TRICK_ZOMBIE_SPAWN_EGG, PvZItemRegister.COFFIN_SPAWN_EGG,
										PvZItemRegister.MOURNER_ZOMBIE_SPAWN_EGG, PvZItemRegister.NOBLE_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.RA_ZOMBIE_SPAWN_EGG, PvZItemRegister.GIGA_TOMB_STONE_SPAWN_EGG,
										PvZItemRegister.EDGAR_090547_SPAWN_EGG, PvZItemRegister.FLOWER_POT_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.AIRBORNE_ZOMBIE_SPAWN_EGG, PvZItemRegister.MC_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.STEEL_PUMPKIN_ZOMBIE_SPAWN_EGG, PvZItemRegister.TARGET_ARROW_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.RED_EDGE_ZOMBIE_SPAWN_EGG, PvZItemRegister.MJ_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.DANCER_BACKUP_ZOMBIE_SPAWN_EGG, PvZItemRegister.RED_FLOWER_ZOMBIE_SPAWN_EGG,
										PvZItemRegister.CHOMPER_ZOMBIE_SPAWN_EGG
								).forEach(item -> output.accept(item.get()));

						HolderLookup.RegistryLookup<Enchantment> enchantmentLookup =
								params.holders().lookupOrThrow(Registries.ENCHANTMENT);

						List.of(
								EnchantmentRegister.ENERGY_TRANSFER,
								EnchantmentRegister.BREAK_OUT,
								EnchantmentRegister.CHARM,
								EnchantmentRegister.SUN_SHOVEL,
								EnchantmentRegister.SOILLESS_PLANT,
								EnchantmentRegister.SUN_MENDING,
								EnchantmentRegister.DENSELY_PLANT,
								EnchantmentRegister.IMMEDIATE_CD,
								EnchantmentRegister.RANGE_REACH,
								EnchantmentRegister.CARD_HEAL
						).forEach(key -> enchantmentLookup.get(key).ifPresent(holder -> {
							for (int lv = 1; lv <= holder.value().getMaxLevel(); lv++) {
								ItemEnchantments.Mutable mutable =
										new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
								mutable.set(holder, lv);
								ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
								book.set(DataComponents.STORED_ENCHANTMENTS, mutable.toImmutable());
								output.accept(book);
							}}));
							}).build());


	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PVZ_BLOCKS =
			TABS.register("pvz_blocks", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.pvz.pvz_blocks"))
					.icon(() -> new ItemStack(PvZBlockRegister.ORIGIN_ORE.get().asItem()))
					.displayItems((params, output) -> {
						// 矿石
						output.accept(PvZBlockRegister.ORIGIN_ORE.get().asItem());
						output.accept(PvZBlockRegister.APPEASE_ORE.get().asItem());
						output.accept(PvZBlockRegister.LIGHT_ORE.get().asItem());
						output.accept(PvZBlockRegister.EXPLOSION_ORE.get().asItem());
						output.accept(PvZBlockRegister.DEFENCE_ORE.get().asItem());
						output.accept(PvZBlockRegister.ICE_ORE.get().asItem());
						output.accept(PvZBlockRegister.ENFORCE_ORE.get().asItem());
						output.accept(PvZBlockRegister.TOXIC_ORE.get().asItem());
						output.accept(PvZBlockRegister.ASSIST_ORE.get().asItem());
						output.accept(PvZBlockRegister.MAGIC_ORE.get().asItem());
						output.accept(PvZBlockRegister.FLAME_ORE.get().asItem());
						output.accept(PvZBlockRegister.SPEAR_ORE.get().asItem());
						output.accept(PvZBlockRegister.ARMA_ORE.get().asItem());
						output.accept(PvZBlockRegister.ELECTRIC_ORE.get().asItem());
						output.accept(PvZBlockRegister.SHADOW_ORE.get().asItem());
						output.accept(PvZBlockRegister.AMETHYST_ORE.get().asItem());
						// 方块
						output.accept(PvZBlockRegister.AMETHYST_BLOCK.get().asItem());
						output.accept(PvZBlockRegister.ORIGIN_BLOCK.get().asItem());
						output.accept(PvZBlockRegister.BUTTER_BLOCK.get().asItem());
						output.accept(PvZBlockRegister.LUNAR_STONE.get().asItem());
						output.accept(PvZBlockRegister.FROZEN_MELON.get().asItem());
						output.accept(PvZBlockRegister.STEEL_LADDER.get().asItem());
						// 植物
						output.accept(PvZBlockRegister.NUT_LEAVES.get().asItem());
						output.accept(PvZBlockRegister.NUT_LOG.get().asItem());
						output.accept(PvZBlockRegister.NUT_WOOD.get().asItem());
						output.accept(PvZBlockRegister.STRIPPED_NUT_LOG.get().asItem());
						output.accept(PvZBlockRegister.STRIPPED_NUT_WOOD.get().asItem());
						output.accept(PvZBlockRegister.NUT_SAPLING.get().asItem());
						output.accept(PvZBlockRegister.CHOMPER.get().asItem());
						output.accept(PvZBlockRegister.LILY_PAD.get().asItem());
						output.accept(PvZBlockRegister.POT_GRASS.get().asItem());
						output.accept(PvZBlockRegister.WATER_POT.get().asItem());
						output.accept(PvZBlockRegister.QUESTION_MARK_POT.get().asItem());
						output.accept(PvZBlockRegister.PLANT_POT.get().asItem());
						// 特殊 / 机器
						output.accept(PvZBlockRegister.LANTERN.get().asItem());
						output.accept(PvZBlockRegister.FLOWER_POT.get().asItem());
						output.accept(PvZBlockRegister.GOLD_TILE1.get().asItem());
						output.accept(PvZBlockRegister.GOLD_TILE2.get().asItem());
						output.accept(PvZBlockRegister.GOLD_TILE3.get().asItem());
						output.accept(PvZBlockRegister.SILVER_SUNFLOWER_TROPHY.get().asItem());
						output.accept(PvZBlockRegister.GOLD_SUNFLOWER_TROPHY.get().asItem());
						output.accept(PvZBlockRegister.DIAMOND_SUNFLOWER_TROPHY.get().asItem());
						output.accept(PvZBlockRegister.SUN_CONVERTER.get().asItem());
						output.accept(PvZBlockRegister.FRAGMENT_SPLICE.get().asItem());
						output.accept(PvZBlockRegister.SLOT_MACHINE.get().asItem());
						output.accept(PvZBlockRegister.ESSENCE_ALTAR.get().asItem());
						output.accept(PvZBlockRegister.CARD_FUSION_TABLE.get().asItem());
					})
					.build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PVZ_PLANT_CARD =
			TABS.register("pvz_plant_card", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.pvz.pvz_plant_card"))
					.icon(() -> new ItemStack(PvZItemRegister.PEA_SHOOTER_CARD.get()))
					.displayItems((params, output) -> {
						List.of(
								PvZItemRegister.PEA_SHOOTER_CARD, PvZItemRegister.PEA_SHOOTER_ENJOY_CARD,
								PvZItemRegister.SUN_FLOWER_CARD, PvZItemRegister.SUN_FLOWER_ENJOY_CARD,
								PvZItemRegister.CHERRY_BOMB_CARD, PvZItemRegister.CHERRY_BOMB_ENJOY_CARD,
								PvZItemRegister.WALL_NUT_CARD, PvZItemRegister.WALL_NUT_ENJOY_CARD,
								PvZItemRegister.POTATO_MINE_CARD, PvZItemRegister.POTATO_MINE_ENJOY_CARD,
								PvZItemRegister.SNOW_PEA_CARD, PvZItemRegister.SNOW_PEA_ENJOY_CARD,
								PvZItemRegister.CHOMPER_CARD, PvZItemRegister.CHOMPER_ENJOY_CARD,
								PvZItemRegister.REPEATER_CARD, PvZItemRegister.REPEATER_ENJOY_CARD,
								PvZItemRegister.PUFF_SHROOM_CARD, PvZItemRegister.PUFF_SHROOM_ENJOY_CARD,
								PvZItemRegister.SUN_SHROOM_CARD, PvZItemRegister.SUN_SHROOM_ENJOY_CARD,
								PvZItemRegister.FUME_SHROOM_CARD, PvZItemRegister.FUME_SHROOM_ENJOY_CARD,
								PvZItemRegister.GRAVE_BUSTER_CARD, PvZItemRegister.GRAVE_BUSTER_ENJOY_CARD,
								PvZItemRegister.HYPNO_SHROOM_CARD, PvZItemRegister.HYPNO_SHROOM_ENJOY_CARD,
								PvZItemRegister.SCAREDY_SHROOM_CARD, PvZItemRegister.SCAREDY_SHROOM_ENJOY_CARD,
								PvZItemRegister.ICE_SHROOM_CARD, PvZItemRegister.ICE_SHROOM_ENJOY_CARD,
								PvZItemRegister.DOOM_SHROOM_CARD, PvZItemRegister.DOOM_SHROOM_ENJOY_CARD,
								PvZItemRegister.LILY_PAD_CARD, PvZItemRegister.LILY_PAD_ENJOY_CARD,
								PvZItemRegister.SQUASH_CARD, PvZItemRegister.SQUASH_ENJOY_CARD,
								PvZItemRegister.THREE_PEATER_CARD, PvZItemRegister.THREE_PEATER_ENJOY_CARD,
								PvZItemRegister.TANGLE_KELP_CARD, PvZItemRegister.TANGLE_KELP_ENJOY_CARD,
								PvZItemRegister.JALAPENO_CARD, PvZItemRegister.JALAPENO_ENJOY_CARD,
								PvZItemRegister.SPIKE_WEED_CARD, PvZItemRegister.SPIKE_WEED_ENJOY_CARD,
								PvZItemRegister.TORCH_WOOD_CARD, PvZItemRegister.TORCH_WOOD_ENJOY_CARD,
								PvZItemRegister.TALL_NUT_CARD, PvZItemRegister.TALL_NUT_ENJOY_CARD,
								PvZItemRegister.SEA_SHROOM_CARD, PvZItemRegister.SEA_SHROOM_ENJOY_CARD,
								PvZItemRegister.PLANTERN_CARD, PvZItemRegister.PLANTERN_ENJOY_CARD,
								PvZItemRegister.CACTUS_CARD, PvZItemRegister.CACTUS_ENJOY_CARD,
								PvZItemRegister.BLOVER_CARD, PvZItemRegister.BLOVER_ENJOY_CARD,
								PvZItemRegister.SPLIT_PEA_CARD, PvZItemRegister.SPLIT_PEA_ENJOY_CARD,
								PvZItemRegister.STAR_FRUIT_CARD, PvZItemRegister.STAR_FRUIT_ENJOY_CARD,
								PvZItemRegister.PUMPKIN_CARD, PvZItemRegister.PUMPKIN_ENJOY_CARD,
								PvZItemRegister.MAGNET_SHROOM_CARD, PvZItemRegister.MAGNET_SHROOM_ENJOY_CARD,
								PvZItemRegister.CABBAGE_PULT_CARD, PvZItemRegister.CABBAGE_PULT_ENJOY_CARD,
								PvZItemRegister.FLOWER_POT_CARD, PvZItemRegister.FLOWER_POT_ENJOY_CARD,
								PvZItemRegister.KERNEL_PULT_CARD, PvZItemRegister.KERNEL_PULT_ENJOY_CARD,
								PvZItemRegister.COFFEE_BEAN_CARD, PvZItemRegister.COFFEE_BEAN_ENJOY_CARD,
								PvZItemRegister.GARLIC_CARD, PvZItemRegister.GARLIC_ENJOY_CARD,
								PvZItemRegister.UMBRELLA_LEAF_CARD, PvZItemRegister.UMBRELLA_LEAF_ENJOY_CARD,
								PvZItemRegister.MARIGOLD_CARD, PvZItemRegister.MARIGOLD_ENJOY_CARD,
								PvZItemRegister.MELON_PULT_CARD, PvZItemRegister.MELON_PULT_ENJOY_CARD,
								PvZItemRegister.GATLING_PEA_CARD, PvZItemRegister.GATLING_PEA_ENJOY_CARD,
								PvZItemRegister.TWIN_SUNFLOWER_CARD, PvZItemRegister.TWIN_SUNFLOWER_ENJOY_CARD,
								PvZItemRegister.GLOOM_SHROOM_CARD, PvZItemRegister.GLOOM_SHROOM_ENJOY_CARD,
								PvZItemRegister.CAT_TAIL_CARD, PvZItemRegister.CAT_TAIL_ENJOY_CARD,
								PvZItemRegister.WINTER_MELON_CARD, PvZItemRegister.WINTER_MELON_ENJOY_CARD,
								PvZItemRegister.GOLD_MAGNET_CARD, PvZItemRegister.GOLD_MAGNET_ENJOY_CARD,
								PvZItemRegister.SPIKE_ROCK_CARD, PvZItemRegister.SPIKE_ROCK_ENJOY_CARD,
								PvZItemRegister.COB_CANNON_CARD, PvZItemRegister.COB_CANNON_ENJOY_CARD,
								PvZItemRegister.IMITATER_CARD, PvZItemRegister.IMITATER_ENJOY_CARD,
								PvZItemRegister.WATER_GUARD_CARD, PvZItemRegister.WATER_GUARD_ENJOY_CARD,
								PvZItemRegister.STRANGE_CAT_CARD, PvZItemRegister.STRANGE_CAT_ENJOY_CARD,
								PvZItemRegister.GOLD_LEAF_CARD, PvZItemRegister.GOLD_LEAF_ENJOY_CARD,
								PvZItemRegister.ANGEL_STAR_FRUIT_CARD, PvZItemRegister.ANGEL_STAR_FRUIT_ENJOY_CARD,
								PvZItemRegister.BUTTER_PULT_CARD, PvZItemRegister.BUTTER_PULT_ENJOY_CARD,
								PvZItemRegister.BAMBOO_LORD_CARD, PvZItemRegister.BAMBOO_LORD_ENJOY_CARD,
								PvZItemRegister.ICEBERG_LETTUCE_CARD, PvZItemRegister.ICEBERG_LETTUCE_ENJOY_CARD,
								PvZItemRegister.BONK_CHOY_CARD, PvZItemRegister.BONK_CHOY_ENJOY_CARD,
								PvZItemRegister.STEEL_PUMPKIN_CARD, PvZItemRegister.STEEL_PUMPKIN_ENJOY_CARD,
								PvZItemRegister.ICE_CABBAGE_PULT_CARD, PvZItemRegister.ICE_CABBAGE_PULT_ENJOY_CARD,
								PvZItemRegister.FIRE_PEASHOOTER_CARD, PvZItemRegister.FIRE_PEASHOOTER_ENJOY_CARD,
								PvZItemRegister.WATER_POT_CARD, PvZItemRegister.WATER_POT_ENJOY_CARD,
								PvZItemRegister.CHORUS_FRUIT_PULT_CARD, PvZItemRegister.CHORUS_FRUIT_PULT_ENJOY_CARD,
								PvZItemRegister.ROTATE_RADISH_CARD, PvZItemRegister.ROTATE_RADISH_ENJOY_CARD,
								PvZItemRegister.BURST_KERNEL_PULT_CARD, PvZItemRegister.BURST_KERNEL_PULT_ENJOY_CARD,
								PvZItemRegister.BLAZE_WART_CARD, PvZItemRegister.BLAZE_WART_ENJOY_CARD,
								PvZItemRegister.GRASS_CARP_CARD, PvZItemRegister.GRASS_CARP_ENJOY_CARD,
								PvZItemRegister.POT_GRASS_CARD, PvZItemRegister.POT_GRASS_ENJOY_CARD,
								PvZItemRegister.SELF_IMITATER_CARD, PvZItemRegister.SELF_IMITATER_ENJOY_CARD,
								PvZItemRegister.RE_ICEPEA_CARD, PvZItemRegister.RE_ICEPEA_ENJOY_CARD,
								PvZItemRegister.PEAPOD_CARD, PvZItemRegister.PEAPOD_ENJOY_CARD,
								PvZItemRegister.SCULK_SHROOM_CARD, PvZItemRegister.SCULK_SHROOM_ENJOY_CARD,
								PvZItemRegister.ORIGIN_SHROOM_CARD, PvZItemRegister.ORIGIN_SHROOM_ENJOY_CARD,
								PvZItemRegister.BEE_SHOOTER_CARD, PvZItemRegister.BEE_SHOOTER_ENJOY_CARD,
								PvZItemRegister.FODDER_BUSH_CARD, PvZItemRegister.FODDER_BUSH_ENJOY_CARD,
								PvZItemRegister.GOLDEN_MELON_PULT_CARD, PvZItemRegister.GOLDEN_MELON_PULT_ENJOY_CARD,
								PvZItemRegister.PRIMAL_PEA_SHOOTER_CARD, PvZItemRegister.PRIMAL_PEA_SHOOTER_ENJOY_CARD,
								PvZItemRegister.GOO_PEA_SHOOTER_CARD, PvZItemRegister.GOO_PEA_SHOOTER_ENJOY_CARD,
								PvZItemRegister.MAGNIFYING_GRASS_CARD, PvZItemRegister.MAGNIFYING_GRASS_ENJOY_CARD
						).forEach(item -> output.accept(item.get()));
					})
					.build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PVZ_USEFUL =
			TABS.register("pvz_useful", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.pvz.pvz_useful"))
					.icon(() -> new ItemStack(PvZItemRegister.PEA_GUN.get()))
					.displayItems((params, output) -> {
						// 工具 / 武器
						output.accept(PvZItemRegister.ZOMBIE_FLAG.get());
						output.accept(PvZItemRegister.SCREEN_DOOR.get());
						output.accept(PvZItemRegister.BOBSLE_CAR.get());
						output.accept(PvZItemRegister.JACK_BOX.get());
						output.accept(PvZItemRegister.BALLOON.get());
						output.accept(PvZItemRegister.TARGET_ARROW.get());
						output.accept(PvZItemRegister.WARNING_SIGN.get());
						output.accept(PvZItemRegister.POLE.get());
						output.accept(PvZItemRegister.HAMMER.get());
						output.accept(PvZItemRegister.ORIGIN_SWORD.get());
						output.accept(PvZItemRegister.ORIGIN_SHOVEL.get());
						output.accept(PvZItemRegister.ORIGIN_PICKAXE.get());
						output.accept(PvZItemRegister.ORIGIN_AXE.get());
						output.accept(PvZItemRegister.ORIGIN_HOE.get());
						output.accept(PvZItemRegister.PEA_GUN.get());
						output.accept(PvZItemRegister.BOWLING_GLOVE.get());
						output.accept(PvZItemRegister.RESOURCE_COLLECTOR.get());
						output.accept(PvZItemRegister.SUN_STORAGE_SAPLING.get());
						output.accept(PvZItemRegister.SMALL_SUN_STORAGE_SAPLING.get());
						output.accept(PvZItemRegister.LARGE_SUN_STORAGE_SAPLING.get());
						output.accept(PvZItemRegister.ONCE_SUN_STORAGE_SAPLING.get());
						output.accept(PvZItemRegister.FIRE_CRACKER.get());
						output.accept(PvZItemRegister.ALMANAC.get());
						output.accept(PvZItemRegister.GARDEN_COMPASS.get());
						output.accept(PvZItemRegister.CARD_PACK.get());
						output.accept(PvZItemRegister.CAR_KEY.get());
						output.accept(PvZItemRegister.LAWN_MOWER.get());
						output.accept(PvZItemRegister.GARDEN_RAKE.get());
						output.accept(PvZItemRegister.ZOMBIE_DOLL.get());
						output.accept(PvZItemRegister.EDGAR_DOLL.get());
						output.accept(PvZItemRegister.TIME_KEY_TO_DEEP.get());
						// 护甲
						output.accept(PvZItemRegister.CONE_HEAD.get());
						output.accept(PvZItemRegister.BUCKET_HEAD.get());
						output.accept(PvZItemRegister.FOOTBALL_HELMET.get());
						output.accept(PvZItemRegister.FOOTBALL_CHESTPLATE.get());
						output.accept(PvZItemRegister.FOOTBALL_LEGGINGS.get());
						output.accept(PvZItemRegister.FOOTBALL_BOOTS.get());
						output.accept(PvZItemRegister.GIGA_HELMET.get());
						output.accept(PvZItemRegister.GIGA_CHESTPLATE.get());
						output.accept(PvZItemRegister.GIGA_LEGGINGS.get());
						output.accept(PvZItemRegister.GIGA_BOOTS.get());
						// 音乐唱片
						output.accept(PvZItemRegister.ZOMBIE_ON_YOUR_LAWN.get());
						output.accept(PvZItemRegister.ZEN_GARDEN.get());
						output.accept(PvZItemRegister.POOL_DISC.get());
						output.accept(PvZItemRegister.BOWLING_MUSIC_DISC.get());
					})
					.build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PVZ_ENVELOPE =
			TABS.register("pvz_envelope", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.pvz.pvz_envelope"))
					.icon(() -> new ItemStack(PvZItemRegister.CHALLENGE_ENVELOPE.get()))
					.displayItems((params, output) -> {
						output.accept(PvZItemRegister.CHALLENGE_ENVELOPE.get());
						PVZAPI.raid().definitions().forEach((id, def) -> {
							output.accept(ChallengeEnvelopeItem.getChallengeEnvelope(id));
						});
					})
					.build());


	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PVZ_FOOD =
			TABS.register("pvz_food", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.pvz.pvz_food"))
					.icon(() -> new ItemStack(PvZItemRegister.REAL_BRAIN.get()))
					.displayItems((params, output) -> {
						// 脑类
						output.accept(PvZItemRegister.FAKE_BRAIN.get());
						output.accept(PvZItemRegister.REAL_BRAIN.get());
						output.accept(PvZItemRegister.COOKED_BRAIN.get());
						// 甜食
						output.accept(PvZItemRegister.CANDY.get());
						output.accept(PvZItemRegister.CHOCOLATE.get());
						// 汤
						output.accept(PvZItemRegister.PEA_SOUP.get());
						// 作物食品
						output.accept(PvZItemRegister.CABBAGE.get());
						output.accept(PvZItemRegister.ICE_CABBAGE.get());
						output.accept(PvZItemRegister.PEPPER.get());
						output.accept(PvZItemRegister.CORN.get());
						output.accept(PvZItemRegister.POP_CORN.get());
						output.accept(PvZItemRegister.TACOS.get());
						output.accept(PvZItemRegister.GARLIC.get());
					})
					.build());


}