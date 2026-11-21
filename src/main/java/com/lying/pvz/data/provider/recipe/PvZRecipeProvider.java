package com.lying.pvz.data.provider.recipe;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.init.PvZPotionRegister;
import com.lying.pvz.common.level.item.card.PlantCardItem;
import com.lying.pvz.common.tag.PvZItemTags;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;

public class PvZRecipeProvider extends RecipeProvider {

	public PvZRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput,
							 BootstrapContext<Advancement> advancementOutput) {
		super(recipeOutput, advancementOutput);
	}

	private static ResourceKey<Recipe<?>> key(String path) {
		return ResourceKey.create(Registries.RECIPE, StringUtil.id(path));
	}

	@Override
	protected void buildRecipes() {

		PVZAPI.lookup().ranks().forEach(this::registerTemplateCard);
		PVZAPI.lookup().plants().forEach(this::registerFragment);

		registerCommonCard(PvZItemRegister.PEA_SHOOTER_CARD.get(), PvZItemTags.PEAS);
		registerCommonCard(PvZItemRegister.SUN_FLOWER_CARD.get(), Items.SUNFLOWER);
		registerCommonCard(PvZItemRegister.WALL_NUT_CARD.get(), PvZItemTags.NUTS);
		registerCommonCard(PvZItemRegister.EXPLODE_O_NUT_CARD.get(), PvZItemTags.NUTS);
		registerCommonCard(PvZItemRegister.POTATO_MINE_CARD.get(), Items.POTATO);
		registerCommonCard(PvZItemRegister.SNOW_PEA_CARD.get(), PvZItemRegister.SNOW_PEA.get());
		registerCommonCard(PvZItemRegister.CHOMPER_CARD.get(), PvZBlockRegister.CHOMPER.get().asItem());

		registerCommonCard(PvZItemRegister.SUN_SHROOM_CARD.get(), PvZItemRegister.SPORE.get());
		registerCommonCard(PvZItemRegister.FUME_SHROOM_CARD.get(), PvZItemRegister.SPORE.get());
		registerCommonCard(PvZItemRegister.GRAVE_BUSTER_CARD.get(), Items.VINE);
		registerCommonCard(PvZItemRegister.HYPNO_SHROOM_CARD.get(), Items.RED_MUSHROOM);
		registerCommonCard(PvZItemRegister.SCAREDY_SHROOM_CARD.get(), PvZItemRegister.SPORE.get());
		registerCommonCard(PvZItemRegister.ICE_SHROOM_CARD.get(), Items.BROWN_MUSHROOM);
		registerCommonCard(PvZItemRegister.DOOM_SHROOM_CARD.get(), PvZItemRegister.SPORE.get());

		registerCommonCard(PvZItemRegister.LILY_PAD_CARD.get(), Items.LILY_PAD);
		registerCommonCard(PvZItemRegister.TANGLE_KELP_CARD.get(), Items.SEAGRASS);
		registerCommonCard(PvZItemRegister.JALAPENO_CARD.get(), PvZItemRegister.PEPPER.get());
		registerCommonCard(PvZItemRegister.SPIKE_WEED_CARD.get(), Items.SHORT_GRASS);
		registerCommonCard(PvZItemRegister.TORCH_WOOD_CARD.get(), ItemTags.LOGS);

		registerCommonCard(PvZItemRegister.PLANTERN_CARD.get(), PvZBlockRegister.LANTERN.get().asItem());
		registerCommonCard(PvZItemRegister.CACTUS_CARD.get(), Items.CACTUS);
		registerCommonCard(PvZItemRegister.BLOVER_CARD.get(), Items.SHORT_GRASS);
		registerCommonCard(PvZItemRegister.PUMPKIN_CARD.get(), Items.PUMPKIN);
		registerCommonCard(PvZItemRegister.MAGNET_SHROOM_CARD.get(), PvZItemRegister.SPORE.get());

		registerCommonCard(PvZItemRegister.CABBAGE_PULT_CARD.get(), PvZItemTags.CABBAGES);
		registerCommonCard(PvZItemRegister.FLOWER_POT_CARD.get(), Items.FLOWER_POT);
		registerCommonCard(PvZItemRegister.KERNEL_PULT_CARD.get(), PvZItemTags.CORNS);
		registerCommonCard(PvZItemRegister.COFFEE_BEAN_CARD.get(), Items.COCOA_BEANS);
		registerCommonCard(PvZItemRegister.UMBRELLA_LEAF_CARD.get(), Items.CARROT);
		registerCommonCard(PvZItemRegister.MARIGOLD_CARD.get(), Items.OXEYE_DAISY);
		registerCommonCard(PvZItemRegister.MELON_PULT_CARD.get(), Items.MELON);

		registerCommonCard(PvZItemRegister.BAMBOO_LORD_CARD.get(), Items.BAMBOO);
		registerCommonCard(PvZItemRegister.ICEBERG_LETTUCE_CARD.get(), PvZItemTags.CABBAGES);
		registerCommonCard(PvZItemRegister.BONK_CHOY_CARD.get(), PvZItemTags.CABBAGES);

		// ---- 新植物卡片（addon）----
		registerCommonCard(PvZItemRegister.BEE_SHOOTER_CARD.get(), Items.HONEYCOMB);
		registerCommonCard(PvZItemRegister.BLAZE_WART_CARD.get(), Items.NETHER_WART);
		registerCommonCard(PvZItemRegister.CHORUS_FRUIT_PULT_CARD.get(), Items.CHORUS_FRUIT);
		registerCommonCard(PvZItemRegister.FIRE_PEASHOOTER_CARD.get(), PvZItemRegister.FLAME_PEA.get());
		registerCommonCard(PvZItemRegister.FODDER_BUSH_CARD.get(), Items.SWEET_BERRIES);
		registerCommonCard(PvZItemRegister.GOO_PEA_SHOOTER_CARD.get(), PvZItemRegister.SPORE.get());
		registerCommonCard(PvZItemRegister.MAGNIFYING_GRASS_CARD.get(), Items.GLASS);
		registerCommonCard(PvZItemRegister.PRIMAL_PEA_SHOOTER_CARD.get(), PvZBlockRegister.PEA_BLOCK.get().asItem());
		registerCommonCard(PvZItemRegister.ROTATE_RADISH_CARD.get(), Items.CARROT);
		registerCommonCard(PvZItemRegister.SELF_IMITATER_CARD.get(), Items.BAKED_POTATO);
		registerCommonCard(PvZItemRegister.STEEL_PUMPKIN_CARD.get(), Items.PUMPKIN);
		registerCommonCard(PvZItemRegister.WATER_POT_CARD.get(), PvZBlockRegister.WATER_POT.get().asItem());
		registerCommonCard(PvZItemRegister.POT_GRASS_CARD.get(), PvZBlockRegister.POT_GRASS.get().asItem());

		registerStoneSmelting(PvZBlockRegister.AMETHYST_ORE.get(),
				PvZItemRegister.AMETHYST_INGOT.get(), 1.4F, 250, "amethyst_ingot");
		registerFoodSmelting(PvZItemRegister.FAKE_BRAIN.get(),
				PvZItemRegister.COOKED_BRAIN.get(), 0.4F, 200, "cooked_brain");


		registerFusion(Arrays.asList(
				PvZItemRegister.PEA_SHOOTER_CARD.get(),
				PvZItemRegister.PEA_SHOOTER_CARD.get()
		), PvZItemRegister.REPEATER_CARD.get());

		registerFusion(Arrays.asList(
				PvZItemRegister.PEA_SHOOTER_CARD.get(),
				PvZItemRegister.PEA_SHOOTER_CARD.get(),
				PvZItemRegister.PEA_SHOOTER_CARD.get()
		), PvZItemRegister.THREE_PEATER_CARD.get());

		registerFusion(Arrays.asList(
				PvZItemRegister.PEA_SHOOTER_CARD.get(),
				PvZItemRegister.REPEATER_CARD.get()
		), PvZItemRegister.SPLIT_PEA_CARD.get());

		registerFusion(Arrays.asList(
				PvZItemRegister.LILY_PAD_CARD.get(),
				PvZItemRegister.WALL_NUT_CARD.get()
		), PvZItemRegister.WATER_GUARD_CARD.get());

		registerFusion(Arrays.asList(
				PvZItemRegister.FUME_SHROOM_CARD.get(),
				PvZItemRegister.SCAREDY_SHROOM_CARD.get()
		), PvZItemRegister.PUFF_SHROOM_CARD.get());

		registerFusion(Arrays.asList(
				PvZItemRegister.WALL_NUT_CARD.get(),
				PvZItemRegister.WALL_NUT_CARD.get()
		), PvZItemRegister.TALL_NUT_CARD.get());

		registerFusion(Arrays.asList(
				PvZItemRegister.SCAREDY_SHROOM_CARD.get(),
				PvZItemRegister.TANGLE_KELP_CARD.get()
		), PvZItemRegister.SEA_SHROOM_CARD.get());

		// ---- 新植物卡片融合配方（addon）----
		registerFusion(Arrays.asList(
				PvZItemRegister.MELON_PULT_CARD.get(),
				PvZItemRegister.GOLD_MAGNET_CARD.get()
		), PvZItemRegister.GOLDEN_MELON_PULT_CARD.get());

		registerFusion(Arrays.asList(
				PvZItemRegister.CABBAGE_PULT_CARD.get(),
				PvZItemRegister.ICEBERG_LETTUCE_CARD.get()
		), PvZItemRegister.ICE_CABBAGE_PULT_CARD.get());

		registerFusion(Arrays.asList(
				PvZItemRegister.PEA_SHOOTER_CARD.get(),
				PvZItemRegister.PEA_SHOOTER_CARD.get(),
				PvZItemRegister.PEA_SHOOTER_CARD.get(),
				PvZItemRegister.PEA_SHOOTER_CARD.get(),
				PvZItemRegister.PEA_SHOOTER_CARD.get()
		), PvZItemRegister.PEAPOD_CARD.get());

		registerFusion(Arrays.asList(
				PvZItemRegister.SNOW_PEA_CARD.get(),
				PvZItemRegister.REPEATER_CARD.get()
		), PvZItemRegister.RE_ICEPEA_CARD.get());

		PvZManualRecipes.register(this.output, this.items, this::has);

		registerBrewingRecipes();


	}

	/**
	 * 酿造配方（26.3 起为数据驱动，位于 data/pvz/recipe/brewing/）。
	 * 沿用旧项目的配方链：
	 * 尴尬药水 + 巧克力 → 兴奋 I；兴奋 I + 萤石粉 → 兴奋 II；兴奋 I + 红石 → 兴奋 III。
	 */
	private void registerBrewingRecipes() {
		BrewingRecipeBuilder
				.brewingMix(Items.POTION, Potions.AWKWARD,
						PvZItemRegister.CHOCOLATE.get(), PvZPotionRegister.EXCITE_POTION_1)
				.save(this.output, key("brewing/excite_potion_1"));

		BrewingRecipeBuilder
				.brewingMix(Items.POTION, PvZPotionRegister.EXCITE_POTION_1,
						Items.GLOWSTONE_DUST, PvZPotionRegister.EXCITE_POTION_2)
				.save(this.output, key("brewing/excite_potion_2_glowstone"));

		BrewingRecipeBuilder
				.brewingMix(Items.POTION, PvZPotionRegister.EXCITE_POTION_1,
						Items.REDSTONE, PvZPotionRegister.EXCITE_POTION_3)
				.save(this.output, key("brewing/excite_potion_3_redstone"));
	}

	private HolderGetter<Recipe<?>> recipeLookup() {
		return this.output.lookup(Registries.RECIPE);
	}

	private void registerFusion(List<Item> list, Item result) {
		FusionRecipeBuilder builder = FusionRecipeBuilder.shapeless(this.items, recipeLookup(), result);
		list.forEach(builder::requires);
		if (result instanceof PlantCardItem card) {
			card.getType().getRank().getCardTag().ifPresent(builder::requires);
		}
		String name = BuiltInRegistries.ITEM.getKey(result).getPath();
		builder.save(this.output, key("card_fusion/" + name));
	}

	private void registerFragment(IPlantType type) {
		type.getSummonCard().ifPresent(card -> {
			if (type.getEnjoyCard().isPresent()) {
				type.getRank().getCardTag().ifPresent(rankTag ->
						FragmentRecipeBuilder.shaped(this.items, recipeLookup(), card)
								.pattern("AAAAA")
								.pattern("ABBBA")
								.pattern("ABCBA")
								.pattern("ABBBA")
								.pattern("AAAAA")
								.define('A', type.getEnjoyCard().get())
								.define('B', type.getEssence().getEssenceItem())
								.define('C', rankTag)
								.unlockedBy("has_essence", has(type.getEssence().getEssenceItem()))
								.save(this.output, key("fragment_splice/" + type.getID().getPath() + "_card")));
			}
		});
	}

	private void registerStoneSmelting(ItemLike input, ItemLike result,
									   float xp, int time, String name) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC,
						CookingBookCategory.MISC, result, xp, time)
				.unlockedBy("has_input", has(input))
				.save(this.output, key("smelting/" + name + "_from_smelting"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.MISC,
						CookingBookCategory.MISC, result, xp, time)
				.unlockedBy("has_input", has(input))
				.save(this.output, key("smelting/" + name + "_from_blasting"));
	}

	private void registerFoodSmelting(ItemLike input, ItemLike result,
									  float xp, int time, String name) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD,
						CookingBookCategory.FOOD, result, xp, time)
				.unlockedBy("has_input", has(input))
				.save(this.output, key("smelting/" + name));
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.FOOD,
						result, xp, time)
				.unlockedBy("has_input", has(input))
				.save(this.output, key("smelting/" + name + "_from_smoking"));
		SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(input), RecipeCategory.FOOD,
						result, xp, time)
				.unlockedBy("has_input", has(input))
				.save(this.output, key("smelting/" + name + "_from_campfire_cooking"));
	}

	private void registerCommonCard(PlantCardItem result, Item crop) {
		Item essence = result.getType().getEssence().getEssenceItem();
		result.getType().getRank().getCardTag().ifPresent(rankCard ->
				this.shaped(RecipeCategory.MISC, result)
						.pattern("AAA")
						.pattern("ABA")
						.pattern("ACA")
						.define('A', essence)
						.define('B', crop)
						.define('C', rankCard)
						.unlockedBy("has_essence", has(essence))
						.save(this.output, key("card/" + result.getType().getID().getPath() + "_card")));
	}

	private void registerCommonCard(PlantCardItem result, TagKey<Item> crop) {
		Item essence = result.getType().getEssence().getEssenceItem();
		result.getType().getRank().getCardTag().ifPresent(rankCard ->
				this.shaped(RecipeCategory.MISC, result)
						.pattern("AAA")
						.pattern("ABA")
						.pattern("ACA")
						.define('A', essence)
						.define('B', crop)
						.define('C', rankCard)
						.unlockedBy("has_essence", has(essence))
						.save(this.output, key("card/" + result.getType().getID().getPath() + "_card")));
	}

	private void registerTemplateCard(IRankType type) {
		Item rankCard = type.getTemplateCard();
		Item origin = PvZItemRegister.ORIGIN_ESSENCE.get();
		type.getMaterial().ifPresent(material ->
				this.shaped(RecipeCategory.MISC, rankCard)
						.pattern(" A ")
						.pattern("ABA")
						.pattern(" A ")
						.define('A', material)
						.define('B', origin)
						.unlockedBy("has_origin", has(origin))
						.save(this.output, key("card/template/" + type.getID().getPath() + "_card")));
	}
}