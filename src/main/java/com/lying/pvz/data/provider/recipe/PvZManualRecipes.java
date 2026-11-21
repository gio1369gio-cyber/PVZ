package com.lying.pvz.data.provider.recipe;

import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.function.Function;

/**
 * 手工 JSON 配方集中管理器。
 * 由 PvZRecipeProvider.buildRecipes 末尾调用。
 * 不生成 smelting/ 前缀配方；熔炼仍由 PvZRecipeProvider 负责。
 */
public final class PvZManualRecipes {

    private PvZManualRecipes() {
    }

    private static final TagKey<Item> C_INGOTS_IRON = ItemTags.create(StringUtil.idNeoforge("ingots/iron"));
    private static final TagKey<Item> C_INGOTS_GOLD = ItemTags.create(StringUtil.idNeoforge("ingots/gold"));
    private static final TagKey<Item> C_INGOTS_AMETHYST = ItemTags.create(StringUtil.idNeoforge("ingots/amethyst"));
    private static final TagKey<Item> C_COBBLESTONE = ItemTags.create(StringUtil.idNeoforge("cobblestones"));

    private static final TagKey<Item> CROPS_PEA = ItemTags.create(StringUtil.idNeoforge("crops/pea"));

    public static void register(RecipeOutput output, HolderGetter<Item> items,
                                Function<ItemLike, Criterion<?>> has) {
        registerShapeless(output, items, has);
        registerShaped(output, items, has);
        // registerGuideBook(output);
    }

    private static ResourceKey<Recipe<?>> key(String path) {
        return ResourceKey.create(Registries.RECIPE, StringUtil.id(path));
    }

    /* ==================== 无序合成 ==================== */

    private static void registerShapeless(RecipeOutput output, HolderGetter<Item> items,
                                          Function<ItemLike, Criterion<?>> has) {
        // amethyst_ingot.json
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, PvZItemRegister.AMETHYST_INGOT.get(), 9)
                .requires(PvZBlockRegister.AMETHYST_BLOCK.get())
                .unlockedBy("has_amethyst_block", has.apply(PvZBlockRegister.AMETHYST_BLOCK.get()))
                .save(output, key("amethyst_ingot"));

        // nut_sapling.json
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, PvZBlockRegister.NUT_SAPLING.get())
                .requires(PvZItemRegister.NUT.get())
                .requires(PvZItemRegister.ORIGIN_ESSENCE.get())
                .unlockedBy("has_origin_essence", has.apply(PvZItemRegister.ORIGIN_ESSENCE.get()))
                .save(output, key("nut_sapling"));

        // origin_essence.json
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, PvZItemRegister.ORIGIN_ESSENCE.get(), 4)
                .requires(PvZBlockRegister.ORIGIN_BLOCK.get())
                .unlockedBy("has_origin_block", has.apply(PvZBlockRegister.ORIGIN_BLOCK.get()))
                .save(output, key("origin_essence"));

        // origin_ingot.json
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, PvZItemRegister.ORIGIN_INGOT.get(), 2)
                .requires(PvZItemRegister.ORIGIN_ESSENCE.get())
                .requires(C_INGOTS_IRON)
                .requires(C_INGOTS_GOLD)
                .unlockedBy("has_origin_essence", has.apply(PvZItemRegister.ORIGIN_ESSENCE.get()))
                .save(output, key("origin_ingot"));

        // pea_soup.json
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, PvZItemRegister.PEA_SOUP.get())
                .requires(Items.BOWL)
                .requires(CROPS_PEA)
                .requires(CROPS_PEA)
                .unlockedBy("has_pea", has.apply(PvZItemRegister.PEA.get()))
                .save(output, key("pea_soup"));

        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, PvZItemRegister.ICE_CABBAGE.get())
                .requires(PvZItemRegister.CABBAGE)
                .requires(Items.ICE)
                .unlockedBy("has_cabbage", has.apply(PvZItemRegister.CABBAGE.get()))
                .save(output, key("ice_cabbage"));

        // food/meat/small_meat1.json -> porkchop x6
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, PvZItemRegister.SMALL_MEAT.get(), 6)
                .requires(Items.PORKCHOP)
                .unlockedBy("has_porkchop", has.apply(Items.PORKCHOP))
                .save(output, key("food/meat/small_meat1"));

        // food/meat/small_meat2.json -> mutton x4
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, PvZItemRegister.SMALL_MEAT.get(), 4)
                .requires(Items.MUTTON)
                .unlockedBy("has_mutton", has.apply(Items.MUTTON))
                .save(output, key("food/meat/small_meat2"));

        // food/meat/small_meat3.json -> rabbit x4
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, PvZItemRegister.SMALL_MEAT.get(), 4)
                .requires(Items.RABBIT)
                .unlockedBy("has_rabbit", has.apply(Items.RABBIT))
                .save(output, key("food/meat/small_meat3"));

        // food/meat/small_meat4.json -> beef x6
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, PvZItemRegister.SMALL_MEAT.get(), 6)
                .requires(Items.BEEF)
                .unlockedBy("has_beef", has.apply(Items.BEEF))
                .save(output, key("food/meat/small_meat4"));

        // food/meat/small_meat5.json -> chicken x4
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, PvZItemRegister.SMALL_MEAT.get(), 4)
                .requires(Items.CHICKEN)
                .unlockedBy("has_chicken", has.apply(Items.CHICKEN))
                .save(output, key("food/meat/small_meat5"));

        // food/meat/small_meat6.json -> rotten flesh x4
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, PvZItemRegister.SMALL_MEAT.get(), 4)
                .requires(Items.ROTTEN_FLESH)
                .unlockedBy("has_rotten_flesh", has.apply(Items.ROTTEN_FLESH))
                .save(output, key("food/meat/small_meat6"));

        // lunar_stone.json -> cobblestone + glowstone_dust
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, PvZBlockRegister.LUNAR_STONE.get())
                .requires(C_COBBLESTONE)
                .requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_glowstone_dust", has.apply(Items.GLOWSTONE_DUST))
                .save(output, key("lunar_stone"));

        // oak_sapling.json -> dead_bush + origin_essence
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, Items.OAK_SAPLING)
                .requires(Items.DEAD_BUSH)
                .requires(PvZItemRegister.ORIGIN_ESSENCE.get())
                .unlockedBy("has_origin_essence", has.apply(PvZItemRegister.ORIGIN_ESSENCE.get()))
                .save(output, key("oak_sapling"));

    }

    /* ==================== 有序合成 ==================== */

    private static void registerShaped(RecipeOutput output, HolderGetter<Item> items,
                                       Function<ItemLike, Criterion<?>> has) {
        // card_fusion_table.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZBlockRegister.CARD_FUSION_TABLE.get())
                .pattern("CCC")
                .pattern("XAY")
                .pattern("BBB")
                .define('C', PvZItemRegister.ORIGIN_INGOT.get())
                .define('X', PvZItemRegister.ORIGIN_ESSENCE.get())
                .define('A', Items.CRAFTING_TABLE)
                .define('Y', PvZItemRegister.LIGHT_ESSENCE.get())
                .define('B', Items.IRON_INGOT)
                .unlockedBy("has_origin_ingot", has.apply(PvZItemRegister.ORIGIN_INGOT.get()))
                .save(output, key("card_fusion_table"));

        // card_pack.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.CARD_PACK.get())
                .pattern("BBB")
                .pattern("XAX")
                .pattern("BBB")
                .define('B', Items.LEATHER)
                .define('X', PvZItemRegister.ORIGIN_INGOT.get())
                .define('A', C_INGOTS_AMETHYST)
                .unlockedBy("has_origin_ingot", has.apply(PvZItemRegister.ORIGIN_INGOT.get()))
                .save(output, key("card_pack"));

        // essence_altar.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZBlockRegister.ESSENCE_ALTAR.get())
                .pattern(" A ")
                .pattern("XCX")
                .pattern("BBB")
                .define('A', PvZBlockRegister.ORIGIN_BLOCK.get())
                .define('X', PvZItemRegister.ORIGIN_INGOT.get())
                .define('C', Items.GOLD_BLOCK)
                .define('B', Items.OBSIDIAN)
                .unlockedBy("has_origin_ingot", has.apply(PvZItemRegister.ORIGIN_INGOT.get()))
                .save(output, key("essence_altar"));

        // fire_cracker.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.FIRE_CRACKER.get(), 4)
                .pattern("XAX")
                .pattern("XBX")
                .pattern("XBX")
                .define('X', Items.BAMBOO)
                .define('A', Items.STRING)
                .define('B', Items.GUNPOWDER)
                .unlockedBy("has_bamboo", has.apply(Items.BAMBOO))
                .save(output, key("fire_cracker"));

        // flame_pea.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.FLAME_PEA.get(), 8)
                .pattern("XXX")
                .pattern("XBX")
                .pattern("XXX")
                .define('X', CROPS_PEA)
                .define('B', PvZItemRegister.FLAME_ESSENCE.get())
                .unlockedBy("has_flame_essence", has.apply(PvZItemRegister.FLAME_ESSENCE.get()))
                .save(output, key("flame_pea"));

        // fragment_splice.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZBlockRegister.FRAGMENT_SPLICE.get())
                .pattern("AAA")
                .pattern("PQP")
                .pattern("BBB")
                .define('A', Items.CRAFTING_TABLE)
                .define('P', PvZItemRegister.LIGHT_ESSENCE.get())
                .define('Q', PvZItemRegister.ORIGIN_ESSENCE.get())
                .define('B', Items.IRON_INGOT)
                .unlockedBy("has_origin_essence", has.apply(PvZItemRegister.ORIGIN_ESSENCE.get()))
                .save(output, key("fragment_splice"));

        // garden_compass.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.GARDEN_COMPASS.get())
                .pattern(" X ")
                .pattern("XYX")
                .pattern(" X ")
                .define('X', PvZItemRegister.ORIGIN_ESSENCE.get())
                .define('Y', Items.REDSTONE)
                .unlockedBy("has_origin_essence", has.apply(PvZItemRegister.ORIGIN_ESSENCE.get()))
                .save(output, key("garden_compass"));

        // origin_block.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZBlockRegister.ORIGIN_BLOCK.get())
                .pattern("XX")
                .pattern("XX")
                .define('X', PvZItemRegister.ORIGIN_ESSENCE.get())
                .unlockedBy("has_origin_essence", has.apply(PvZItemRegister.ORIGIN_ESSENCE.get()))
                .save(output, key("origin_block"));

        // origin_hoe.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.ORIGIN_HOE.get())
                .pattern("XX ")
                .pattern(" Y ")
                .pattern(" Y ")
                .define('X', PvZItemRegister.ORIGIN_INGOT.get())
                .define('Y', Items.STICK)
                .unlockedBy("has_origin_ingot", has.apply(PvZItemRegister.ORIGIN_INGOT.get()))
                .save(output, key("origin_hoe"));

        // origin_shovel.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.ORIGIN_SHOVEL.get())
                .pattern(" X ")
                .pattern(" Y ")
                .pattern(" Y ")
                .define('X', PvZItemRegister.ORIGIN_INGOT.get())
                .define('Y', Items.STICK)
                .unlockedBy("has_origin_ingot", has.apply(PvZItemRegister.ORIGIN_INGOT.get()))
                .save(output, key("origin_shovel"));

        // origin_axe.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.ORIGIN_AXE.get())
                .pattern("XX ")
                .pattern("XY ")
                .pattern(" Y ")
                .define('X', PvZItemRegister.ORIGIN_INGOT.get())
                .define('Y', Items.STICK)
                .unlockedBy("has_origin_ingot", has.apply(PvZItemRegister.ORIGIN_INGOT.get()))
                .save(output, key("origin_axe"));

        // origin_pickaxe.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.ORIGIN_PICKAXE.get())
                .pattern("XXX")
                .pattern(" Y ")
                .pattern(" Y ")
                .define('X', PvZItemRegister.ORIGIN_INGOT.get())
                .define('Y', Items.STICK)
                .unlockedBy("has_origin_ingot", has.apply(PvZItemRegister.ORIGIN_INGOT.get()))
                .save(output, key("origin_pickaxe"));

        // origin_sword.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.ORIGIN_SWORD.get())
                .pattern(" X ")
                .pattern(" X ")
                .pattern(" Y ")
                .define('X', PvZItemRegister.ORIGIN_INGOT.get())
                .define('Y', Items.STICK)
                .unlockedBy("has_origin_ingot", has.apply(PvZItemRegister.ORIGIN_INGOT.get()))
                .save(output, key("origin_sword"));

        // snow_pea.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.SNOW_PEA.get(), 8)
                .pattern("XXX")
                .pattern("XBX")
                .pattern("XXX")
                .define('X', CROPS_PEA)
                .define('B', PvZItemRegister.ICE_ESSENCE.get())
                .unlockedBy("has_ice_essence", has.apply(PvZItemRegister.ICE_ESSENCE.get()))
                .save(output, key("snow_pea"));

        // steel_ladder.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZBlockRegister.STEEL_LADDER.get(), 8)
                .pattern("X X")
                .pattern("XYX")
                .pattern("X X")
                .define('X', C_INGOTS_IRON)
                .define('Y', PvZItemRegister.ASSIST_ESSENCE.get())
                .unlockedBy("has_assist_essence", has.apply(PvZItemRegister.ASSIST_ESSENCE.get()))
                .save(output, key("steel_ladder"));

        // sun_converter.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZBlockRegister.SUN_CONVERTER.get())
                .pattern("ACA")
                .pattern("ABA")
                .pattern("XXX")
                .define('A', PvZItemRegister.LIGHT_ESSENCE.get())
                .define('C', PvZItemRegister.RESOURCE_COLLECTOR.get())
                .define('B', Items.HOPPER)
                .define('X', Items.IRON_BLOCK)
                .unlockedBy("has_resource_collector", has.apply(PvZItemRegister.RESOURCE_COLLECTOR.get()))
                .save(output, key("sun_converter"));

        // amethyst_block.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZBlockRegister.AMETHYST_BLOCK.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', PvZItemRegister.AMETHYST_INGOT.get())
                .unlockedBy("has_amethyst_ingot", has.apply(PvZItemRegister.AMETHYST_INGOT.get()))
                .save(output, key("amethyst_block"));

        // fake_brain.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.FAKE_BRAIN.get())
                .pattern("XXX")
                .pattern("XXX")
                .pattern("  X")
                .define('X', PvZItemRegister.SMALL_MEAT.get())
                .unlockedBy("has_small_meat", has.apply(PvZItemRegister.SMALL_MEAT.get()))
                .save(output, key("fake_brain"));

        // gold_sunflower_trophy.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZBlockRegister.GOLD_SUNFLOWER_TROPHY.get())
                .pattern(" X ")
                .pattern("XBX")
                .pattern(" X ")
                .define('B', Items.GOLD_INGOT)
                .define('X', PvZBlockRegister.SILVER_SUNFLOWER_TROPHY.get())
                .unlockedBy("has_silver_sunflower_trophy", has.apply(PvZBlockRegister.SILVER_SUNFLOWER_TROPHY.get()))
                .save(output, key("gold_sunflower_trophy"));

        // diamond_sunflower_trophy.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZBlockRegister.DIAMOND_SUNFLOWER_TROPHY.get())
                .pattern(" X ")
                .pattern("XBX")
                .pattern(" X ")
                .define('B', Items.DIAMOND)
                .define('X', PvZBlockRegister.GOLD_SUNFLOWER_TROPHY.get())
                .unlockedBy("has_gold_sunflower_trophy", has.apply(PvZBlockRegister.GOLD_SUNFLOWER_TROPHY.get()))
                .save(output, key("diamond_sunflower_trophy"));

        // food/chocolate.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.FOOD, PvZItemRegister.CHOCOLATE.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.COCOA_BEANS)
                .define('B', PvZItemRegister.CANDY.get())
                .unlockedBy("has_candy", has.apply(PvZItemRegister.CANDY.get()))
                .save(output, key("food/chocolate"));

        // food/small_sun_storage_sapling.json -> sun_storage_sapling2
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.SMALL_SUN_STORAGE_SAPLING.get())
                .pattern("CAC")
                .pattern("ABA")
                .pattern("CAC")
                .define('A', PvZItemRegister.LIGHT_ESSENCE.get())
                .define('B', ItemTags.SAPLINGS)
                .define('C', PvZItemRegister.ORIGIN_ESSENCE.get())
                .unlockedBy("has_origin_essence", has.apply(PvZItemRegister.ORIGIN_ESSENCE.get()))
                .save(output, key("food/small_sun_storage_sapling"));

        // food/sun_storage_sapling.json -> sun_storage_sapling
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.SUN_STORAGE_SAPLING.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', PvZItemRegister.LIGHT_ESSENCE.get())
                .define('B', PvZItemRegister.SMALL_SUN_STORAGE_SAPLING.get())
                .define('C', PvZItemRegister.ORIGIN_ESSENCE.get())
                .unlockedBy("has_small_sun_storage_sapling", has.apply(PvZItemRegister.SMALL_SUN_STORAGE_SAPLING.get()))
                .save(output, key("food/sun_storage_sapling"));

        // food/large_sun_storage_sapling.json -> sun_storage_sapling3
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.LARGE_SUN_STORAGE_SAPLING.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', PvZItemRegister.LIGHT_ESSENCE.get())
                .define('B', PvZItemRegister.SUN_STORAGE_SAPLING.get())
                .define('C', PvZItemRegister.ORIGIN_ESSENCE.get())
                .unlockedBy("has_sun_storage_sapling", has.apply(PvZItemRegister.SUN_STORAGE_SAPLING.get()))
                .save(output, key("food/large_sun_storage_sapling"));

        // pea_gun.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.PEA_GUN.get())
                .pattern("AXA")
                .pattern("CBC")
                .pattern("ACA")
                .define('X', CROPS_PEA)
                .define('A', PvZItemRegister.APPEASE_ESSENCE.get())
                .define('B', Items.DISPENSER)
                .define('C', PvZItemRegister.ORIGIN_INGOT.get())
                .unlockedBy("has_appease_essence", has.apply(PvZItemRegister.APPEASE_ESSENCE.get()))
                .save(output, key("pea_gun"));

        // resource_collector.json
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, PvZItemRegister.RESOURCE_COLLECTOR.get())
                .pattern("AXA")
                .pattern("XBX")
                .pattern("AXA")
                .define('X', Items.GOLD_INGOT)
                .define('A', PvZItemRegister.ASSIST_ESSENCE.get())
                .define('B', Items.DAYLIGHT_DETECTOR)
                .unlockedBy("has_assist_essence", has.apply(PvZItemRegister.ASSIST_ESSENCE.get()))
                .save(output, key("resource_collector"));
    }


//    private static void registerGuideBook(RecipeOutput output) {
//        ItemStack result = ItemModBook.forBook(StringUtil.id("pvz_guide"));
//
//        NonNullList<Ingredient> ingredients = NonNullList.create();
//        ingredients.add(Ingredient.of(Items.BOOK));
//        ingredients.add(Ingredient.of(ItemTags.SAPLINGS));
//        ingredients.add(Ingredient.of(ItemTags.SAPLINGS));
//        ingredients.add(Ingredient.of(ItemTags.SAPLINGS));
//        ingredients.add(Ingredient.of(ItemTags.SAPLINGS));
//
//        ShapelessRecipe recipe = new ShapelessRecipe(
//                "", CraftingBookCategory.MISC, result, ingredients);
//
//        output.accept(StringUtil.id("pvz_guide"), recipe, null);
//    }
}