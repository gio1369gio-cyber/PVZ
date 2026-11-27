package com.lying.pvz.data.provider;

import com.lying.pvz.PvZConfig;
import com.lying.pvz.PvZMod;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZEffectRegister;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class PvZLangProvider extends LanguageProvider {
    public PvZLangProvider(PackOutput output) {
        super(output, PvZMod.MOD_ID, "en_us");
    }
    @Override
    protected void addTranslations() {
        /* ==================== 创造模式标签 ==================== */
        add("itemGroup.pvz.pvz_misc", "PVZ Misc");
        add("itemGroup.pvz.pvz_blocks", "PVZ Blocks");
        add("itemGroup.pvz.pvz_plant_card", "PVZ Plant Cards");
        add("itemGroup.pvz.pvz_useful", "PVZ Useful Items");
        add("itemGroup.pvz.pvz_food", "PVZ Food");
        add("itemGroup.pvz.pvz_envelope","PVZ Challenge");

        /* ==================== 属性 ==================== */
        add("attribute.name.pvz.inner_defence_hp", "Inner Defence HP");
        add("attribute.name.pvz.outer_defence_hp", "Outer Defence HP");
        /* ==================== 方块 ==================== */

        addBlock(PvZBlockRegister.LILY_PAD, "Lily Pad");
        addBlock(PvZBlockRegister.NUT_LEAVES, "Nut Leaves");
        addBlock(PvZBlockRegister.NUT_WOOD, "Nut Wood");
        addBlock(PvZBlockRegister.STRIPPED_NUT_WOOD, "Stripped Nut Wood");
        addBlock(PvZBlockRegister.STRIPPED_NUT_LOG, "Stripped Nut Log");
        addBlock(PvZBlockRegister.NUT_LOG, "Nut Log");
        addBlock(PvZBlockRegister.NUT_SAPLING, "Nut Sapling");
        addBlock(PvZBlockRegister.CHOMPER, "Chomper");

        addBlock(PvZBlockRegister.ORIGIN_BLOCK, "Origin Block");
        addBlock(PvZBlockRegister.BUTTER_BLOCK, "Butter Block");
        addBlock(PvZBlockRegister.STEEL_LADDER, "Steel Ladder");
        addBlock(PvZBlockRegister.AMETHYST_BLOCK, "Amethyst Block");
        addBlock(PvZBlockRegister.LUNAR_STONE, "Lunar Stone");
        addBlock(PvZBlockRegister.FROZEN_MELON, "Frozen Melon");

        addBlock(PvZBlockRegister.ORIGIN_ORE, "Origin Ore");
        addBlock(PvZBlockRegister.APPEASE_ORE, "Appease Ore");
        addBlock(PvZBlockRegister.LIGHT_ORE, "Light Ore");
        addBlock(PvZBlockRegister.EXPLOSION_ORE, "Explosion Ore");
        addBlock(PvZBlockRegister.DEFENCE_ORE, "Defence Ore");
        addBlock(PvZBlockRegister.ICE_ORE, "Ice Ore");
        addBlock(PvZBlockRegister.ENFORCE_ORE, "Enforce Ore");
        addBlock(PvZBlockRegister.TOXIC_ORE, "Toxic Ore");
        addBlock(PvZBlockRegister.ASSIST_ORE, "Assist Ore");
        addBlock(PvZBlockRegister.MAGIC_ORE, "Magic Ore");
        addBlock(PvZBlockRegister.FLAME_ORE, "Flame Ore");
        addBlock(PvZBlockRegister.SPEAR_ORE, "Spear Ore");
        addBlock(PvZBlockRegister.ARMA_ORE, "Arma Ore");
        addBlock(PvZBlockRegister.ELECTRIC_ORE, "Electric Ore");
        addBlock(PvZBlockRegister.SHADOW_ORE, "Shadow Ore");
        addBlock(PvZBlockRegister.AMETHYST_ORE, "Amethyst Ore");

        addBlock(PvZBlockRegister.PEA_PLANT, "Pea Plant");
        addBlock(PvZBlockRegister.TOXIC_SHROOM, "Toxic Shroom");
        addBlock(PvZBlockRegister.CABBAGE, "Cabbage Crop");
        addBlock(PvZBlockRegister.CORN, "Corn Crop");
        addBlock(PvZBlockRegister.CHILI,"Chili Crop");
        addBlock(PvZBlockRegister.GARLIC,"Garlic");

        addBlock(PvZBlockRegister.LANTERN, "Lantern");
        addBlock(PvZBlockRegister.FLOWER_POT, "Flower Pot");
        addBlock(PvZBlockRegister.GOLD_TILE1, "Gold Tile 1");
        addBlock(PvZBlockRegister.GOLD_TILE2, "Gold Tile 2");
        addBlock(PvZBlockRegister.GOLD_TILE3, "Gold Tile 3");
        addBlock(PvZBlockRegister.SILVER_SUNFLOWER_TROPHY, "Silver Sunflower Trophy");
        addBlock(PvZBlockRegister.GOLD_SUNFLOWER_TROPHY, "Gold Sunflower Trophy");
        addBlock(PvZBlockRegister.DIAMOND_SUNFLOWER_TROPHY, "Diamond Sunflower Trophy");
        addBlock(PvZBlockRegister.QUESTION_MARK_POT,"Question Mark Pot");
        addBlock(PvZBlockRegister.PLANT_POT,"Plant Pot");
        addBlock(PvZBlockRegister.SCREEN_DOOR,"Screen Door");
        addBlock(PvZBlockRegister.WATER_POT,"Water Pot");
        addBlock(PvZBlockRegister.POT_GRASS,"Pot Grass");

        addBlock(PvZBlockRegister.SLOT_MACHINE, "Slot Machine");
        addBlock(PvZBlockRegister.SUN_CONVERTER, "Sun Converter");
        addBlock(PvZBlockRegister.FRAGMENT_SPLICE, "Fragment Splice");
        addBlock(PvZBlockRegister.ESSENCE_ALTAR, "Essence Altar");
        addBlock(PvZBlockRegister.CARD_FUSION_TABLE, "Card Fusion Table");

        /* ==================== 物品：种子 / 材料 ==================== */
        addItem(PvZItemRegister.PEA, "Pea");
        addItem(PvZItemRegister.SPORE, "Spore");
        addItem(PvZItemRegister.CABBAGE_SEEDS, "Cabbage Seeds");
        addItem(PvZItemRegister.CORN_SEEDS, "Corn Seeds");
        addItem(PvZItemRegister.CHILI_SEEDS,"Chili Seeds");
        addItem(PvZItemRegister.GARLIC,"Garlic");

        addItem(PvZItemRegister.NUT, "Nut");
        addItem(PvZItemRegister.SNOW_PEA, "Snow Pea");
        addItem(PvZItemRegister.FLAME_PEA, "Flame Pea");
        addItem(PvZItemRegister.SMALL_MEAT, "Small Meat");
        addItem(PvZItemRegister.PEPPER, "Pepper");
        addItem(PvZItemRegister.FROZEN_MELON_SLICE, "Frozen Melon Slice");
        addItem(PvZItemRegister.TIME_SOURCE, "Time Source");

        addItem(PvZItemRegister.ORIGIN_INGOT, "Origin Ingot");
        addItem(PvZItemRegister.AMETHYST_INGOT, "Amethyst Ingot");

        /* ==================== 物品：精华 ==================== */
        addItem(PvZItemRegister.ORIGIN_ESSENCE, "Origin Essence");
        addItem(PvZItemRegister.APPEASE_ESSENCE, "Appease Essence");
        addItem(PvZItemRegister.LIGHT_ESSENCE, "Light Essence");
        addItem(PvZItemRegister.EXPLOSION_ESSENCE, "Explosion Essence");
        addItem(PvZItemRegister.DEFENCE_ESSENCE, "Defence Essence");
        addItem(PvZItemRegister.ICE_ESSENCE, "Ice Essence");
        addItem(PvZItemRegister.ENFORCE_ESSENCE, "Enforce Essence");
        addItem(PvZItemRegister.TOXIC_ESSENCE, "Toxic Essence");
        addItem(PvZItemRegister.ASSIST_ESSENCE, "Assist Essence");
        addItem(PvZItemRegister.MAGIC_ESSENCE, "Magic Essence");
        addItem(PvZItemRegister.FLAME_ESSENCE, "Flame Essence");
        addItem(PvZItemRegister.SPEAR_ESSENCE, "Spear Essence");
        addItem(PvZItemRegister.ARMA_ESSENCE, "Arma Essence");
        addItem(PvZItemRegister.ELECTRIC_ESSENCE, "Electric Essence");
        addItem(PvZItemRegister.SHADOW_ESSENCE, "Shadow Essence");

        /* ==================== 物品：卡片模板 ==================== */
        addItem(PvZItemRegister.GRAY_CARD, "Gray Card");
        addItem(PvZItemRegister.WHITE_CARD, "White Card");
        addItem(PvZItemRegister.GREEN_CARD, "Green Card");
        addItem(PvZItemRegister.BLUE_CARD, "Blue Card");
        addItem(PvZItemRegister.PURPLE_CARD, "Purple Card");
        addItem(PvZItemRegister.GOLD_CARD, "Gold Card");
        addItem(PvZItemRegister.RED_CARD, "Red Card");
        addItem(PvZItemRegister.BLACK_CARD, "Black Card");
        addItem(PvZItemRegister.MEGA_CARD, "Mega Card");

        /* ==================== 物品：工具 / 杂项 ==================== */
        addItem(PvZItemRegister.ZOMBIE_FLAG, "Zombie Flag");
        addItem(PvZItemRegister.SCREEN_DOOR, "Screen Door");
        addItem(PvZItemRegister.BOBSLE_CAR, "Bobsle Car");
        addItem(PvZItemRegister.JACK_BOX, "Jack-in-the-Box");
        addItem(PvZItemRegister.BALLOON, "Balloon");
        addItem(PvZItemRegister.TARGET_ARROW, "Target Arrow");
        addItem(PvZItemRegister.ZOMBIE_DOLL, "Zombie Doll");
        addItem(PvZItemRegister.TIME_KEY_TO_DEEP, "Time Key to Deep");
        addItem(PvZItemRegister.GARDEN_COMPASS, "Garden Compass");
        addItem(PvZItemRegister.CARD_PACK, "Card Pack");
        addItem(PvZItemRegister.PEA_GUN, "Pea Gun");
        addItem(PvZItemRegister.RESOURCE_COLLECTOR, "Resource Collector");

        addItem(PvZItemRegister.SUN_STORAGE_SAPLING, "Sun Storage Sapling");
        addItem(PvZItemRegister.SMALL_SUN_STORAGE_SAPLING, "Small Sun Storage Sapling");
        addItem(PvZItemRegister.LARGE_SUN_STORAGE_SAPLING, "Large Sun Storage Sapling");
        addItem(PvZItemRegister.ONCE_SUN_STORAGE_SAPLING, "Once Sun Storage Sapling");

        addItem(PvZItemRegister.HAMMER,"hammer");
        addItem(PvZItemRegister.ORIGIN_SWORD, "Origin Sword");
        addItem(PvZItemRegister.ORIGIN_SHOVEL, "Origin Shovel");
        addItem(PvZItemRegister.ORIGIN_PICKAXE, "Origin Pickaxe");
        addItem(PvZItemRegister.ORIGIN_AXE, "Origin Axe");
        addItem(PvZItemRegister.ORIGIN_HOE, "Origin Hoe");
        addItem(PvZItemRegister.WARNING_SIGN, "Warning Sign");
        addItem(PvZItemRegister.POLE, "Pole");

        addItem(PvZItemRegister.BOWLING_GLOVE, "Bowling Glove");
        addItem(PvZItemRegister.FIRE_CRACKER, "Fire Cracker");
        addItem(PvZItemRegister.EDGAR_DOLL, "Edgar Doll");
        addItem(PvZItemRegister.GARDEN_RAKE, "Garden Rake");
        addItem(PvZItemRegister.LAWN_MOWER, "Lawn Mower");
        addItem(PvZItemRegister.CAR_KEY, "Car Key");
        addItem(PvZItemRegister.ALMANAC, "Almanac");
        addItem(PvZItemRegister.CHALLENGE_ENVELOPE, "Challenge Envelope");

        /* ==================== 物品：食物 ==================== */
        addItem(PvZItemRegister.FAKE_BRAIN, "Fake Brain");
        addItem(PvZItemRegister.REAL_BRAIN, "Real Brain");
        addItem(PvZItemRegister.CANDY, "Candy");
        addItem(PvZItemRegister.CHOCOLATE, "Chocolate");
        addItem(PvZItemRegister.PEA_SOUP, "Pea Soup");
        addItem(PvZItemRegister.COOKED_BRAIN, "Cooked Brain");
        addItem(PvZItemRegister.CABBAGE, "Cabbage");
        addItem(PvZItemRegister.ICE_CABBAGE, "Ice Cabbage");
        addItem(PvZItemRegister.CORN, "Corn");
        addItem(PvZItemRegister.POP_CORN, "Pop Corn");
        addItem(PvZItemRegister.TACOS, "Tacos");

        /* ==================== 物品：护甲 ==================== */
        addItem(PvZItemRegister.CONE_HEAD, "Cone Head");
        addItem(PvZItemRegister.BUCKET_HEAD, "Bucket Head");
        addItem(PvZItemRegister.FOOTBALL_HELMET, "Football Helmet");
        addItem(PvZItemRegister.FOOTBALL_CHESTPLATE, "Football Chestplate");
        addItem(PvZItemRegister.FOOTBALL_LEGGINGS, "Football Leggings");
        addItem(PvZItemRegister.FOOTBALL_BOOTS, "Football Boots");
        addItem(PvZItemRegister.GIGA_HELMET, "Giga Helmet");
        addItem(PvZItemRegister.GIGA_CHESTPLATE, "Giga Chestplate");
        addItem(PvZItemRegister.GIGA_LEGGINGS, "Giga Leggings");
        addItem(PvZItemRegister.GIGA_BOOTS, "Giga Boots");

        /* ==================== 物品：音乐唱片 ==================== */
        addItem(PvZItemRegister.ZOMBIE_ON_YOUR_LAWN, "Music Disc - Zombie on Your Lawn");
        addItem(PvZItemRegister.ZEN_GARDEN, "Music Disc - Zen Garden");
        addItem(PvZItemRegister.POOL_DISC, "Pool Music Disc");
        addItem(PvZItemRegister.BOWLING_MUSIC_DISC, "Bowling Music Disc");
        add("item.pvz.music_disc_zombie_on_your_lawn.desc","Zombie on Your Lawn");
        add("item.pvz.music_disc_zen_garden.desc","Zen Garden");
        add("item.pvz.music_disc_pool_disc.desc", "Pool");
        add("item.pvz.music_disc_bowling_music_disc.desc", "Bowling");

        /* ==================== 刷怪蛋 ==================== */
        addItem(PvZItemRegister.SUN_SPAWN_EGG, "Sun Spawn Egg");
        addItem(PvZItemRegister.COIN_SPAWN_EGG, "Coin Spawn Egg");
        addItem(PvZItemRegister.JEWEL_SPAWN_EGG, "Jewel Spawn Egg");
        addItem(PvZItemRegister.ENERGY_SPAWN_EGG, "Energy Spawn Egg");
        addItem(PvZItemRegister.FOODIE_ZOMBIE_SPAWN_EGG, "Foodie Zombie Spawn Egg");
        addItem(PvZItemRegister.CRAZY_DAVE_SPAWN_EGG, "Crazy Dave Spawn Egg");
        addItem(PvZItemRegister.PANNEY_SPAWN_EGG, "Penny Spawn Egg");
        addItem(PvZItemRegister.SUN_DAVE_SPAWN_EGG, "Sun Dave Spawn Egg");

        addItem(PvZItemRegister.NORMAL_ZOMBIE_SPAWN_EGG, "Normal Zombie Spawn Egg");
        addItem(PvZItemRegister.FLAG_ZOMBIE_SPAWN_EGG, "Flag Zombie Spawn Egg");
        addItem(PvZItemRegister.CONEHEAD_ZOMBIE_SPAWN_EGG, "Conehead Zombie Spawn Egg");
        addItem(PvZItemRegister.POLE_ZOMBIE_SPAWN_EGG, "Pole Zombie Spawn Egg");
        addItem(PvZItemRegister.BUCKETHEAD_ZOMBIE_SPAWN_EGG, "Buckethead Zombie Spawn Egg");
        addItem(PvZItemRegister.NEWSPAPER_ZOMBIE_SPAWN_EGG, "Newspaper Zombie Spawn Egg");
        addItem(PvZItemRegister.TOMB_STONE_SPAWN_EGG, "Tomb Stone Spawn Egg");
        addItem(PvZItemRegister.SCREENDOOR_ZOMBIE_SPAWN_EGG, "Screen Door Zombie Spawn Egg");
        addItem(PvZItemRegister.FOOTBALL_ZOMBIE_SPAWN_EGG, "Football Zombie Spawn Egg");
        addItem(PvZItemRegister.DANCING_ZOMBIE_SPAWN_EGG, "Dancing Zombie Spawn Egg");
        addItem(PvZItemRegister.BACKUP_DANCER_SPAWN_EGG, "Backup Dancer Spawn Egg");
        addItem(PvZItemRegister.GIGA_FOOTBALL_ZOMBIE_SPAWN_EGG, "Giga Football Zombie Spawn Egg");
        addItem(PvZItemRegister.OLD_ZOMBIE_SPAWN_EGG, "Old Zombie Spawn Egg");
        addItem(PvZItemRegister.SUNDAY_EDITION_ZOMBIE_SPAWN_EGG, "Sunday Edition Zombie Spawn Egg");
        addItem(PvZItemRegister.SNORKEL_ZOMBIE_SPAWN_EGG, "Snorkel Zombie Spawn Egg");
        addItem(PvZItemRegister.ZOMBONI_SPAWN_EGG, "Zomboni Spawn Egg");
        addItem(PvZItemRegister.BOBSLE_TEAM_SPAWN_EGG, "Bobsle Team Spawn Egg");
        addItem(PvZItemRegister.BOBSLE_ZOMBIE_SPAWN_EGG, "Bobsle Zombie Spawn Egg");
        addItem(PvZItemRegister.ZOMBIE_DOLPHIN_SPAWN_EGG, "Zombie Dolphin Spawn Egg");
        addItem(PvZItemRegister.DOLPHIN_RIDER_SPAWN_EGG, "Dolphin Rider Spawn Egg");
        addItem(PvZItemRegister.DOLPHIN_RIDER_ZOMBIE_SPAWN_EGG, "Dolphin Rider Zombie Spawn Egg");
        addItem(PvZItemRegister.LAVA_ZOMBIE_SPAWN_EGG, "Lava Zombie Spawn Egg");
        addItem(PvZItemRegister.JACK_IN_BOX_ZOMBIE_SPAWN_EGG, "Jack-in-the-Box Zombie Spawn Egg");
        addItem(PvZItemRegister.BALLOON_ZOMBIE_SPAWN_EGG, "Balloon Zombie Spawn Egg");
        addItem(PvZItemRegister.DIGGER_ZOMBIE_SPAWN_EGG, "Digger Zombie Spawn Egg");
        addItem(PvZItemRegister.POGO_ZOMBIE_SPAWN_EGG, "Pogo Zombie Spawn Egg");
        addItem(PvZItemRegister.YETI_ZOMBIE_SPAWN_EGG, "Yeti Zombie Spawn Egg");
        addItem(PvZItemRegister.BUNGEE_ZOMBIE_SPAWN_EGG, "Bungee Zombie Spawn Egg");
        addItem(PvZItemRegister.LADDER_ZOMBIE_SPAWN_EGG, "Ladder Zombie Spawn Egg");
        addItem(PvZItemRegister.CATAPULT_ZOMBIE_SPAWN_EGG, "Catapult Zombie Spawn Egg");
        addItem(PvZItemRegister.GARGANTUAR_SPAWN_EGG, "Gargantuar Spawn Egg");
        addItem(PvZItemRegister.IMP_SPAWN_EGG, "Imp Spawn Egg");
        addItem(PvZItemRegister.GIGA_GARGANTUAR_SPAWN_EGG, "Giga Gargantuar Spawn Egg");
        addItem(PvZItemRegister.EDGAR_090505_SPAWN_EGG, "Edgar 090505 Spawn Egg");
        addItem(PvZItemRegister.EDGAR_090517_SPAWN_EGG, "Edgar 090517 Spawn Egg");
        addItem(PvZItemRegister.PEASHOOTER_ZOMBIE_SPAWN_EGG, "Peashooter Zombie Spawn Egg");
        addItem(PvZItemRegister.WALLNUT_ZOMBIE_SPAWN_EGG, "Wall-nut Zombie Spawn Egg");
        addItem(PvZItemRegister.GATLINGPEA_ZOMBIE_SPAWN_EGG, "Gatling Pea Zombie Spawn Egg");
        addItem(PvZItemRegister.TALLNUT_ZOMBIE_SPAWN_EGG, "Tall-nut Zombie Spawn Egg");
        addItem(PvZItemRegister.SQUASH_ZOMBIE_SPAWN_EGG, "Squash Zombie Spawn Egg");
        addItem(PvZItemRegister.JALAPENO_ZOMBIE_SPAWN_EGG, "Jalapeno Zombie Spawn Egg");
        addItem(PvZItemRegister.PUMPKIN_ZOMBIE_SPAWN_EGG, "Pumpkin Zombie Spawn Egg");
        addItem(PvZItemRegister.TRICK_ZOMBIE_SPAWN_EGG, "Trick Zombie Spawn Egg");
        addItem(PvZItemRegister.COFFIN_SPAWN_EGG, "Coffin Spawn Egg");
        addItem(PvZItemRegister.MOURNER_ZOMBIE_SPAWN_EGG, "Mourner Zombie Spawn Egg");
        addItem(PvZItemRegister.NOBLE_ZOMBIE_SPAWN_EGG, "Noble Zombie Spawn Egg");
        addItem(PvZItemRegister.RA_ZOMBIE_SPAWN_EGG, "Ra Zombie Spawn Egg");
        addItem(PvZItemRegister.GIGA_TOMB_STONE_SPAWN_EGG, "Giga Tomb Stone Spawn Egg");
        addItem(PvZItemRegister.RED_FLOWER_ZOMBIE_SPAWN_EGG, "Sunflower Zombie Spawn Egg");
        addItem(PvZItemRegister.CHOMPER_ZOMBIE_SPAWN_EGG, "Chomper Zombie Spawn Egg");
        addItem(PvZItemRegister.STEEL_PUMPKIN_ZOMBIE_SPAWN_EGG, "Steel Pumpkin Zombie Spawn Egg");
        addItem(PvZItemRegister.FLOWER_POT_ZOMBIE_SPAWN_EGG, "Flower Pot Zombie Spawn Egg");
        addItem(PvZItemRegister.EDGAR_090547_SPAWN_EGG, "Edgar 090547 Spawn Egg");
        addItem(PvZItemRegister.AIRBORNE_ZOMBIE_SPAWN_EGG, "Airborne Zombie Spawn Egg");
        addItem(PvZItemRegister.MC_ZOMBIE_SPAWN_EGG, "MC Zombie Spawn Egg");
        addItem(PvZItemRegister.TARGET_ARROW_ZOMBIE_SPAWN_EGG, "Target Arrow Zombie Spawn Egg");
        addItem(PvZItemRegister.RED_EDGE_ZOMBIE_SPAWN_EGG, "Red Edge Zombie Spawn Egg");
        addItem(PvZItemRegister.MJ_ZOMBIE_SPAWN_EGG, "MJ Zombie Spawn Egg");
        addItem(PvZItemRegister.DANCER_BACKUP_ZOMBIE_SPAWN_EGG, "Dancer Backup Zombie Spawn Egg");

        /* ==================== 植物卡 ==================== */
        addItem(PvZItemRegister.PEA_SHOOTER_CARD, "Peashooter Card");
        addItem(PvZItemRegister.PEA_SHOOTER_ENJOY_CARD, "Peashooter (Enjoy) Card");
        addItem(PvZItemRegister.SUN_FLOWER_CARD, "Sunflower Card");
        addItem(PvZItemRegister.SUN_FLOWER_ENJOY_CARD, "Sunflower (Enjoy) Card");
        addItem(PvZItemRegister.CHERRY_BOMB_CARD, "Cherry Bomb Card");
        addItem(PvZItemRegister.CHERRY_BOMB_ENJOY_CARD, "Cherry Bomb (Enjoy) Card");
        addItem(PvZItemRegister.WALL_NUT_CARD, "Wall-nut Card");
        addItem(PvZItemRegister.WALL_NUT_ENJOY_CARD, "Wall-nut (Enjoy) Card");
        addItem(PvZItemRegister.POTATO_MINE_CARD, "Potato Mine Card");
        addItem(PvZItemRegister.POTATO_MINE_ENJOY_CARD, "Potato Mine (Enjoy) Card");
        addItem(PvZItemRegister.SNOW_PEA_CARD, "Snow Pea Card");
        addItem(PvZItemRegister.SNOW_PEA_ENJOY_CARD, "Snow Pea (Enjoy) Card");
        addItem(PvZItemRegister.CHOMPER_CARD, "Chomper Card");
        addItem(PvZItemRegister.CHOMPER_ENJOY_CARD, "Chomper (Enjoy) Card");
        addItem(PvZItemRegister.REPEATER_CARD, "Repeater Card");
        addItem(PvZItemRegister.REPEATER_ENJOY_CARD, "Repeater (Enjoy) Card");
        addItem(PvZItemRegister.PUFF_SHROOM_CARD, "Puff-shroom Card");
        addItem(PvZItemRegister.PUFF_SHROOM_ENJOY_CARD, "Puff-shroom (Enjoy) Card");
        addItem(PvZItemRegister.SUN_SHROOM_CARD, "Sun-shroom Card");
        addItem(PvZItemRegister.SUN_SHROOM_ENJOY_CARD, "Sun-shroom (Enjoy) Card");
        addItem(PvZItemRegister.FUME_SHROOM_CARD, "Fume-shroom Card");
        addItem(PvZItemRegister.FUME_SHROOM_ENJOY_CARD, "Fume-shroom (Enjoy) Card");
        addItem(PvZItemRegister.GRAVE_BUSTER_CARD, "Grave Buster Card");
        addItem(PvZItemRegister.GRAVE_BUSTER_ENJOY_CARD, "Grave Buster (Enjoy) Card");
        addItem(PvZItemRegister.HYPNO_SHROOM_CARD, "Hypno-shroom Card");
        addItem(PvZItemRegister.HYPNO_SHROOM_ENJOY_CARD, "Hypno-shroom (Enjoy) Card");
        addItem(PvZItemRegister.SCAREDY_SHROOM_CARD, "Scaredy-shroom Card");
        addItem(PvZItemRegister.SCAREDY_SHROOM_ENJOY_CARD, "Scaredy-shroom (Enjoy) Card");
        addItem(PvZItemRegister.ICE_SHROOM_CARD, "Ice-shroom Card");
        addItem(PvZItemRegister.ICE_SHROOM_ENJOY_CARD, "Ice-shroom (Enjoy) Card");
        addItem(PvZItemRegister.DOOM_SHROOM_CARD, "Doom-shroom Card");
        addItem(PvZItemRegister.DOOM_SHROOM_ENJOY_CARD, "Doom-shroom (Enjoy) Card");
        addItem(PvZItemRegister.LILY_PAD_CARD, "Lily Pad Card");
        addItem(PvZItemRegister.LILY_PAD_ENJOY_CARD, "Lily Pad (Enjoy) Card");
        addItem(PvZItemRegister.SQUASH_CARD, "Squash Card");
        addItem(PvZItemRegister.SQUASH_ENJOY_CARD, "Squash (Enjoy) Card");
        addItem(PvZItemRegister.THREE_PEATER_CARD, "Threepeater Card");
        addItem(PvZItemRegister.THREE_PEATER_ENJOY_CARD, "Threepeater (Enjoy) Card");
        addItem(PvZItemRegister.TANGLE_KELP_CARD, "Tangle Kelp Card");
        addItem(PvZItemRegister.TANGLE_KELP_ENJOY_CARD, "Tangle Kelp (Enjoy) Card");
        addItem(PvZItemRegister.JALAPENO_CARD, "Jalapeno Card");
        addItem(PvZItemRegister.JALAPENO_ENJOY_CARD, "Jalapeno (Enjoy) Card");
        addItem(PvZItemRegister.SPIKE_WEED_CARD, "Spikeweed Card");
        addItem(PvZItemRegister.SPIKE_WEED_ENJOY_CARD, "Spikeweed (Enjoy) Card");
        addItem(PvZItemRegister.TORCH_WOOD_CARD, "Torchwood Card");
        addItem(PvZItemRegister.TORCH_WOOD_ENJOY_CARD, "Torchwood (Enjoy) Card");
        addItem(PvZItemRegister.TALL_NUT_CARD, "Tall-nut Card");
        addItem(PvZItemRegister.TALL_NUT_ENJOY_CARD, "Tall-nut (Enjoy) Card");
        addItem(PvZItemRegister.SEA_SHROOM_CARD, "Sea-shroom Card");
        addItem(PvZItemRegister.SEA_SHROOM_ENJOY_CARD, "Sea-shroom (Enjoy) Card");
        addItem(PvZItemRegister.PLANTERN_CARD, "Plantern Card");
        addItem(PvZItemRegister.PLANTERN_ENJOY_CARD, "Plantern (Enjoy) Card");
        addItem(PvZItemRegister.CACTUS_CARD, "Cactus Card");
        addItem(PvZItemRegister.CACTUS_ENJOY_CARD, "Cactus (Enjoy) Card");
        addItem(PvZItemRegister.BLOVER_CARD, "Blover Card");
        addItem(PvZItemRegister.BLOVER_ENJOY_CARD, "Blover (Enjoy) Card");
        addItem(PvZItemRegister.SPLIT_PEA_CARD, "Split Pea Card");
        addItem(PvZItemRegister.SPLIT_PEA_ENJOY_CARD, "Split Pea (Enjoy) Card");
        addItem(PvZItemRegister.STAR_FRUIT_CARD, "Starfruit Card");
        addItem(PvZItemRegister.STAR_FRUIT_ENJOY_CARD, "Starfruit (Enjoy) Card");
        addItem(PvZItemRegister.PUMPKIN_CARD, "Pumpkin Card");
        addItem(PvZItemRegister.PUMPKIN_ENJOY_CARD, "Pumpkin (Enjoy) Card");
        addItem(PvZItemRegister.MAGNET_SHROOM_CARD, "Magnet-shroom Card");
        addItem(PvZItemRegister.MAGNET_SHROOM_ENJOY_CARD, "Magnet-shroom (Enjoy) Card");
        addItem(PvZItemRegister.CABBAGE_PULT_CARD, "Cabbage-pult Card");
        addItem(PvZItemRegister.CABBAGE_PULT_ENJOY_CARD, "Cabbage-pult (Enjoy) Card");
        addItem(PvZItemRegister.FLOWER_POT_CARD, "Flower Pot Card");
        addItem(PvZItemRegister.FLOWER_POT_ENJOY_CARD, "Flower Pot (Enjoy) Card");
        addItem(PvZItemRegister.KERNEL_PULT_CARD, "Kernel-pult Card");
        addItem(PvZItemRegister.KERNEL_PULT_ENJOY_CARD, "Kernel-pult (Enjoy) Card");
        addItem(PvZItemRegister.COFFEE_BEAN_CARD, "Coffee Bean Card");
        addItem(PvZItemRegister.COFFEE_BEAN_ENJOY_CARD, "Coffee Bean (Enjoy) Card");
        addItem(PvZItemRegister.GARLIC_CARD, "Garlic Card");
        addItem(PvZItemRegister.GARLIC_ENJOY_CARD, "Garlic (Enjoy) Card");
        addItem(PvZItemRegister.UMBRELLA_LEAF_CARD, "Umbrella Leaf Card");
        addItem(PvZItemRegister.UMBRELLA_LEAF_ENJOY_CARD, "Umbrella Leaf (Enjoy) Card");
        addItem(PvZItemRegister.MARIGOLD_CARD, "Marigold Card");
        addItem(PvZItemRegister.MARIGOLD_ENJOY_CARD, "Marigold (Enjoy) Card");
        addItem(PvZItemRegister.MELON_PULT_CARD, "Melon-pult Card");
        addItem(PvZItemRegister.MELON_PULT_ENJOY_CARD, "Melon-pult (Enjoy) Card");
        addItem(PvZItemRegister.GATLING_PEA_CARD, "Gatling Pea Card");
        addItem(PvZItemRegister.GATLING_PEA_ENJOY_CARD, "Gatling Pea (Enjoy) Card");
        addItem(PvZItemRegister.TWIN_SUNFLOWER_CARD, "Twin Sunflower Card");
        addItem(PvZItemRegister.TWIN_SUNFLOWER_ENJOY_CARD, "Twin Sunflower (Enjoy) Card");
        addItem(PvZItemRegister.GLOOM_SHROOM_CARD, "Gloom-shroom Card");
        addItem(PvZItemRegister.GLOOM_SHROOM_ENJOY_CARD, "Gloom-shroom (Enjoy) Card");
        addItem(PvZItemRegister.CAT_TAIL_CARD, "Cattail Card");
        addItem(PvZItemRegister.CAT_TAIL_ENJOY_CARD, "Cattail (Enjoy) Card");
        addItem(PvZItemRegister.WINTER_MELON_CARD, "Winter Melon Card");
        addItem(PvZItemRegister.WINTER_MELON_ENJOY_CARD, "Winter Melon (Enjoy) Card");
        addItem(PvZItemRegister.GOLD_MAGNET_CARD, "Gold Magnet Card");
        addItem(PvZItemRegister.GOLD_MAGNET_ENJOY_CARD, "Gold Magnet (Enjoy) Card");
        addItem(PvZItemRegister.SPIKE_ROCK_CARD, "Spikerock Card");
        addItem(PvZItemRegister.SPIKE_ROCK_ENJOY_CARD, "Spikerock (Enjoy) Card");
        addItem(PvZItemRegister.COB_CANNON_CARD, "Cob Cannon Card");
        addItem(PvZItemRegister.COB_CANNON_ENJOY_CARD, "Cob Cannon (Enjoy) Card");
        addItem(PvZItemRegister.WATER_GUARD_CARD, "Water Guard Card");
        addItem(PvZItemRegister.WATER_GUARD_ENJOY_CARD, "Water Guard (Enjoy) Card");
        addItem(PvZItemRegister.STRANGE_CAT_CARD, "Strange Cat Card");
        addItem(PvZItemRegister.STRANGE_CAT_ENJOY_CARD, "Strange Cat (Enjoy) Card");
        addItem(PvZItemRegister.GOLD_LEAF_CARD, "Gold Leaf Card");
        addItem(PvZItemRegister.GOLD_LEAF_ENJOY_CARD, "Gold Leaf (Enjoy) Card");
        addItem(PvZItemRegister.ANGEL_STAR_FRUIT_CARD, "Angel Starfruit Card");
        addItem(PvZItemRegister.ANGEL_STAR_FRUIT_ENJOY_CARD, "Angel Starfruit (Enjoy) Card");
        addItem(PvZItemRegister.BUTTER_PULT_CARD, "Butter-pult Card");
        addItem(PvZItemRegister.BUTTER_PULT_ENJOY_CARD, "Butter-pult (Enjoy) Card");
        addItem(PvZItemRegister.BAMBOO_LORD_CARD, "Bamboo Lord Card");
        addItem(PvZItemRegister.BAMBOO_LORD_ENJOY_CARD, "Bamboo Lord (Enjoy) Card");
        addItem(PvZItemRegister.ICEBERG_LETTUCE_CARD, "Iceberg Lettuce Card");
        addItem(PvZItemRegister.ICEBERG_LETTUCE_ENJOY_CARD, "Iceberg Lettuce (Enjoy) Card");
        addItem(PvZItemRegister.BONK_CHOY_CARD, "Bonk Choy Card");
        addItem(PvZItemRegister.BONK_CHOY_ENJOY_CARD, "Bonk Choy (Enjoy) Card");
        addItem(PvZItemRegister.IMITATER_CARD, "Imitater Card");
        addItem(PvZItemRegister.IMITATER_ENJOY_CARD, "Imitater (Enjoy) Card");
        addItem(PvZItemRegister.EXPLODE_O_NUT_CARD,"Explode Nut Card");
        addItem(PvZItemRegister.EXPLODE_O_NUT_ENJOY_CARD,"Explode Nut (Enjoy) Card");
        addItem(PvZItemRegister.GIANT_NUT_CARD,"Giant Nut Card");
        addItem(PvZItemRegister.GIANT_NUT_ENJOY_CARD,"Giant Nut (Enjoy) Card");
        addItem(PvZItemRegister.STEEL_PUMPKIN_CARD, "Steel Pumpkin Card");
        addItem(PvZItemRegister.STEEL_PUMPKIN_ENJOY_CARD, "Steel Pumpkin (Enjoy) Card");
        addItem(PvZItemRegister.ICE_CABBAGE_PULT_CARD, "Ice Cabbage-pult Card");
        addItem(PvZItemRegister.ICE_CABBAGE_PULT_ENJOY_CARD, "Ice Cabbage-pult (Enjoy) Card");
        addItem(PvZItemRegister.FIRE_PEASHOOTER_CARD, "Fire Peashooter Card");
        addItem(PvZItemRegister.FIRE_PEASHOOTER_ENJOY_CARD, "Fire Peashooter (Enjoy) Card");
        addItem(PvZItemRegister.CHORUS_FRUIT_PULT_CARD, "Chorus Fruit-pult Card");
        addItem(PvZItemRegister.CHORUS_FRUIT_PULT_ENJOY_CARD, "Chorus Fruit-pult (Enjoy) Card");
        addItem(PvZItemRegister.ROTATE_RADISH_CARD, "Rotate Radish Card");
        addItem(PvZItemRegister.ROTATE_RADISH_ENJOY_CARD, "Rotate Radish (Enjoy) Card");
        addItem(PvZItemRegister.BURST_KERNEL_PULT_CARD, "Burst Kernel-pult Card");
        addItem(PvZItemRegister.BURST_KERNEL_PULT_ENJOY_CARD, "Burst Kernel-pult (Enjoy) Card");
        addItem(PvZItemRegister.BLAZE_WART_CARD, "Blaze Wart Card");
        addItem(PvZItemRegister.BLAZE_WART_ENJOY_CARD, "Blaze Wart (Enjoy) Card");
        addItem(PvZItemRegister.GRASS_CARP_CARD, "Grass Carp Card");
        addItem(PvZItemRegister.GRASS_CARP_ENJOY_CARD, "Grass Carp (Enjoy) Card");
        addItem(PvZItemRegister.SELF_IMITATER_CARD, "Self Imitater Card");
        addItem(PvZItemRegister.SELF_IMITATER_ENJOY_CARD, "Self Imitater (Enjoy) Card");
        addItem(PvZItemRegister.RE_ICEPEA_CARD, "Re-icepea Card");
        addItem(PvZItemRegister.RE_ICEPEA_ENJOY_CARD, "Re-icepea (Enjoy) Card");
        addItem(PvZItemRegister.PEAPOD_CARD, "Peapod Card");
        addItem(PvZItemRegister.PEAPOD_ENJOY_CARD, "Peapod (Enjoy) Card");
        addItem(PvZItemRegister.SCULK_SHROOM_CARD, "Sculk Shroom Card");
        addItem(PvZItemRegister.SCULK_SHROOM_ENJOY_CARD, "Sculk Shroom (Enjoy) Card");
        addItem(PvZItemRegister.ORIGIN_SHROOM_CARD, "Origin Shroom Card");
        addItem(PvZItemRegister.ORIGIN_SHROOM_ENJOY_CARD, "Origin Shroom (Enjoy) Card");
        addItem(PvZItemRegister.BEE_SHOOTER_CARD, "Bee Shooter Card");
        addItem(PvZItemRegister.BEE_SHOOTER_ENJOY_CARD, "Bee Shooter (Enjoy) Card");
        addItem(PvZItemRegister.FODDER_BUSH_CARD, "Fodder Bush Card");
        addItem(PvZItemRegister.FODDER_BUSH_ENJOY_CARD, "Fodder Bush (Enjoy) Card");
        addItem(PvZItemRegister.GOLDEN_MELON_PULT_CARD, "Golden Melon-pult Card");
        addItem(PvZItemRegister.GOLDEN_MELON_PULT_ENJOY_CARD, "Golden Melon-pult (Enjoy) Card");
        addItem(PvZItemRegister.PRIMAL_PEA_SHOOTER_CARD, "Primal Peashooter Card");
        addItem(PvZItemRegister.PRIMAL_PEA_SHOOTER_ENJOY_CARD, "Primal Peashooter (Enjoy) Card");
        addItem(PvZItemRegister.GOO_PEA_SHOOTER_CARD, "Goo Peashooter Card");
        addItem(PvZItemRegister.GOO_PEA_SHOOTER_ENJOY_CARD, "Goo Peashooter (Enjoy) Card");
        addItem(PvZItemRegister.MAGNIFYING_GRASS_CARD, "Magnifying Grass Card");
        addItem(PvZItemRegister.MAGNIFYING_GRASS_ENJOY_CARD, "Magnifying Grass (Enjoy) Card");
        addItem(PvZItemRegister.WATER_POT_CARD, "Water Pot Card");
        addItem(PvZItemRegister.WATER_POT_ENJOY_CARD, "Water Pot (Enjoy) Card");
        addItem(PvZItemRegister.POT_GRASS_CARD, "Pot Grass Card");
        addItem(PvZItemRegister.POT_GRASS_ENJOY_CARD, "Pot Grass (Enjoy) Card");

        /* ==================== 实体 ==================== */
        addEntityType(PvZEntityRegister.SUN, "Sun");
        addEntityType(PvZEntityRegister.COIN, "Coin");
        addEntityType(PvZEntityRegister.JEWEL, "Jewel");
        addEntityType(PvZEntityRegister.ENERGY, "Energy");
        addEntityType(PvZEntityRegister.GIFT_BOX, "Gift Box");

        addEntityType(PvZEntityRegister.PEA, "Pea Bullet");
        addEntityType(PvZEntityRegister.SPORE, "Spore Bullet");
        addEntityType(PvZEntityRegister.FUME, "Fume Bullet");
        addEntityType(PvZEntityRegister.STAR, "Star Bullet");
        addEntityType(PvZEntityRegister.THORN, "Thorn Bullet");
        addEntityType(PvZEntityRegister.METAL, "Metal Bullet");
        addEntityType(PvZEntityRegister.FIRE_CRACKER, "Fire Cracker Bullet");
        addEntityType(PvZEntityRegister.CABBAGE, "Cabbage Bullet");
        addEntityType(PvZEntityRegister.KERNEL, "Kernel Bullet");
        addEntityType(PvZEntityRegister.BUTTER, "Butter Bullet");
        addEntityType(PvZEntityRegister.CORN, "Corn Bullet");
        addEntityType(PvZEntityRegister.MELON, "Melon Bullet");
        addEntityType(PvZEntityRegister.BALL, "Ball Bullet");
        addEntityType(PvZEntityRegister.NUT, "Nut Bullet");
        addEntityType(PvZEntityRegister.POTATO, "Potato Bullet");
        addEntityType(PvZEntityRegister.TARGET_ARROW, "Target Arrow");

        addEntityType(PvZEntityRegister.ORIGIN_EFFECT, "Origin Effect");
        addEntityType(PvZEntityRegister.DOOM_FIXER, "Doom Fixer");

        addEntityType(PvZEntityRegister.SMALL_CHOMPER, "Small Chomper");
        addEntityType(PvZEntityRegister.BOBSLE_CAR, "Bobsle Car");
        addEntityType(PvZEntityRegister.ZOMBIE_PART, "Zombie Part");
        addEntityType(PvZEntityRegister.ZOMBIE_HAND, "Zombie Hand");
        addEntityType(PvZEntityRegister.WALL_NUT_BOWLING, "Wall Nut Bowling");
        addEntityType(PvZEntityRegister.EXPLOSION_BOWLING, "Explosion Bowling");
        addEntityType(PvZEntityRegister.GIANT_NUT_BOWLING, "Giant Nut Bowling");
        addEntityType(PvZEntityRegister.LAWN_MOWER, "Lawn Mower");
        addEntityType(PvZEntityRegister.FIRE_CRACKERS, "Fire Crackers");
        addEntityType(PvZEntityRegister.ELEMENT_BALL, "Element Ball");
        addEntityType(PvZEntityRegister.DESTROY_CAR, "Destroy Car");
        addEntityType(PvZEntityRegister.GARDEN_RAKE, "Garden Rake");
        addEntityType(PvZEntityRegister.ZOMBIE_DROP_BODY, "Zombie Drop Body");

        addEntityType(PvZEntityRegister.FOODIE_ZOMBIE, "Foodie Zombie");

        addEntityType(PvZEntityRegister.CRAZY_DAVE, "Crazy Dave");
        addEntityType(PvZEntityRegister.PANNEY, "Penny");
        addEntityType(PvZEntityRegister.SUN_DAVE, "Sun Dave");

        addEntityType(PvZEntityRegister.NORMAL_ZOMBIE, "Normal Zombie");
        addEntityType(PvZEntityRegister.FLAG_ZOMBIE, "Flag Zombie");
        addEntityType(PvZEntityRegister.CONEHEAD_ZOMBIE, "Conehead Zombie");
        addEntityType(PvZEntityRegister.POLE_ZOMBIE, "Pole Zombie");
        addEntityType(PvZEntityRegister.BUCKETHEAD_ZOMBIE, "Buckethead Zombie");
        addEntityType(PvZEntityRegister.SNORKEL_ZOMBIE, "Snorkel Zombie");
        addEntityType(PvZEntityRegister.ZOMBONI, "Zomboni");
        addEntityType(PvZEntityRegister.BOBSLE_TEAM, "Bobsle Team");
        addEntityType(PvZEntityRegister.BOBSLE_ZOMBIE, "Bobsle Zombie");
        addEntityType(PvZEntityRegister.ZOMBIE_DOLPHIN, "Zombie Dolphin");
        addEntityType(PvZEntityRegister.DOLPHIN_RIDER, "Dolphin Rider");
        addEntityType(PvZEntityRegister.DOLPHIN_RIDER_ZOMBIE, "Dolphin Rider Zombie");
        addEntityType(PvZEntityRegister.LAVA_ZOMBIE, "Lava Zombie");
        addEntityType(PvZEntityRegister.NEWSPAPER_ZOMBIE, "Newspaper Zombie");
        addEntityType(PvZEntityRegister.TOMB_STONE, "Tomb Stone");
        addEntityType(PvZEntityRegister.SCREENDOOR_ZOMBIE, "Screen Door Zombie");
        addEntityType(PvZEntityRegister.FOOTBALL_ZOMBIE, "Football Zombie");
        addEntityType(PvZEntityRegister.DANCING_ZOMBIE, "Dancing Zombie");
        addEntityType(PvZEntityRegister.BACKUP_DANCER, "Backup Dancer");
        addEntityType(PvZEntityRegister.GIGA_FOOTBALL_ZOMBIE, "Giga Football Zombie");
        addEntityType(PvZEntityRegister.PUMPKIN_ZOMBIE, "Pumpkin Zombie");
        addEntityType(PvZEntityRegister.TRICK_ZOMBIE, "Trick Zombie");
        addEntityType(PvZEntityRegister.COFFIN, "Coffin");
        addEntityType(PvZEntityRegister.MOURNER_ZOMBIE, "Mourner Zombie");
        addEntityType(PvZEntityRegister.NOBLE_ZOMBIE, "Noble Zombie");
        addEntityType(PvZEntityRegister.OLD_ZOMBIE, "Old Zombie");
        addEntityType(PvZEntityRegister.SUNDAY_EDITION_ZOMBIE, "Sunday Edition Zombie");
        addEntityType(PvZEntityRegister.JACK_IN_BOX_ZOMBIE, "Jack-in-the-Box Zombie");
        addEntityType(PvZEntityRegister.POGO_ZOMBIE, "Pogo Zombie");
        addEntityType(PvZEntityRegister.YETI_ZOMBIE, "Yeti Zombie");
        addEntityType(PvZEntityRegister.DIGGER_ZOMBIE, "Digger Zombie");
        addEntityType(PvZEntityRegister.BALLOON_ZOMBIE, "Balloon Zombie");
        addEntityType(PvZEntityRegister.RA_ZOMBIE, "Ra Zombie");
        addEntityType(PvZEntityRegister.BUNGEE_ZOMBIE, "Bungee Zombie");
        addEntityType(PvZEntityRegister.LADDER_ZOMBIE, "Ladder Zombie");
        addEntityType(PvZEntityRegister.CATAPULT_ZOMBIE, "Catapult Zombie");
        addEntityType(PvZEntityRegister.GARGANTUAR, "Gargantuar");
        addEntityType(PvZEntityRegister.IMP, "Imp");
        addEntityType(PvZEntityRegister.GIGA_GARGANTUAR, "Giga Gargantuar");
        addEntityType(PvZEntityRegister.EDGAR_090505, "Edgar 090505");
        addEntityType(PvZEntityRegister.EDGAR_090517, "Edgar 090517");
        addEntityType(PvZEntityRegister.SPHINX_INATOR, "Sphinx Terminator");
        addEntityType(PvZEntityRegister.ZOMBOT_MISSILE, "Zombot Missile");
        addEntityType(PvZEntityRegister.PLANK_WALKER, "Plank Walker");
        addEntityType(PvZEntityRegister.WAR_WAGON, "War Wagon");
        addEntityType(PvZEntityRegister.TOMORROW_TRON, "Tomorrow Tron");
        addEntityType(PvZEntityRegister.PEASHOOTER_ZOMBIE, "Peashooter Zombie");
        addEntityType(PvZEntityRegister.GATLINGPEA_ZOMBIE, "Gatling Pea Zombie");
        addEntityType(PvZEntityRegister.SQUASH_ZOMBIE, "Squash Zombie");
        addEntityType(PvZEntityRegister.JALAPENO_ZOMBIE, "Jalapeno Zombie");
        addEntityType(PvZEntityRegister.WALLNUT_ZOMBIE, "Wall-nut Zombie");
        addEntityType(PvZEntityRegister.TALLNUT_ZOMBIE, "Tall-nut Zombie");
        addEntityType(PvZEntityRegister.GIGA_TOMB_STONE, "Giga Tomb Stone");

        addEntityType(PvZEntityRegister.PEA_SHOOTER, "Peashooter");
        addEntityType(PvZEntityRegister.SUN_FLOWER, "Sunflower");
        addEntityType(PvZEntityRegister.CHERRY_BOMB, "Cherry Bomb");
        addEntityType(PvZEntityRegister.WALL_NUT, "Wall-nut");
        addEntityType(PvZEntityRegister.EXPLODE_O_NUT, "Explode Nut");
        addEntityType(PvZEntityRegister.GIANT_NUT, "Giant Nut");
        addEntityType(PvZEntityRegister.POTATO_MINE, "Potato Mine");
        addEntityType(PvZEntityRegister.SNOW_PEA, "Snow Pea");
        addEntityType(PvZEntityRegister.CHOMPER, "Chomper");
        addEntityType(PvZEntityRegister.REPEATER, "Repeater");
        addEntityType(PvZEntityRegister.LILY_PAD, "Lily Pad");
        addEntityType(PvZEntityRegister.SQUASH, "Squash");
        addEntityType(PvZEntityRegister.THREE_PEATER, "Threepeater");
        addEntityType(PvZEntityRegister.TANGLE_KELP, "Tangle Kelp");
        addEntityType(PvZEntityRegister.JALAPENO, "Jalapeno");
        addEntityType(PvZEntityRegister.SPIKE_WEED, "Spikeweed");
        addEntityType(PvZEntityRegister.TORCH_WOOD, "Torchwood");
        addEntityType(PvZEntityRegister.TALL_NUT, "Tall-nut");
        addEntityType(PvZEntityRegister.PUFF_SHROOM, "Puff-shroom");
        addEntityType(PvZEntityRegister.SUN_SHROOM, "Sun-shroom");
        addEntityType(PvZEntityRegister.FUME_SHROOM, "Fume-shroom");
        addEntityType(PvZEntityRegister.GRAVE_BUSTER, "Grave Buster");
        addEntityType(PvZEntityRegister.HYPNO_SHROOM, "Hypno-shroom");
        addEntityType(PvZEntityRegister.SCAREDY_SHROOM, "Scaredy-shroom");
        addEntityType(PvZEntityRegister.ICE_SHROOM, "Ice-shroom");
        addEntityType(PvZEntityRegister.DOOM_SHROOM, "Doom-shroom");
        addEntityType(PvZEntityRegister.SEA_SHROOM, "Sea-shroom");
        addEntityType(PvZEntityRegister.SPLIT_PEA, "Split Pea");
        addEntityType(PvZEntityRegister.COFFEE_BEAN, "Coffee Bean");
        addEntityType(PvZEntityRegister.MARIGOLD, "Marigold");
        addEntityType(PvZEntityRegister.GATLING_PEA, "Gatling Pea");
        addEntityType(PvZEntityRegister.TWIN_SUNFLOWER, "Twin Sunflower");
        addEntityType(PvZEntityRegister.WATER_GUARD, "Water Guard");
        addEntityType(PvZEntityRegister.PUMPKIN, "Pumpkin");
        addEntityType(PvZEntityRegister.PLANTERN, "Plantern");
        addEntityType(PvZEntityRegister.MAGNET_SHROOM, "Magnet-shroom");
        addEntityType(PvZEntityRegister.CAT_TAIL, "Cattail");
        addEntityType(PvZEntityRegister.STRANGE_CAT, "Strange Cat");
        addEntityType(PvZEntityRegister.STAR_FRUIT, "Starfruit");
        addEntityType(PvZEntityRegister.ANGEL_STAR_FRUIT, "Angel Starfruit");
        addEntityType(PvZEntityRegister.CACTUS, "Cactus");
        addEntityType(PvZEntityRegister.BLOVER, "Blover");
        addEntityType(PvZEntityRegister.GLOOM_SHROOM, "Gloom-shroom");
        addEntityType(PvZEntityRegister.GOLD_MAGNET, "Gold Magnet");
        addEntityType(PvZEntityRegister.GOLD_LEAF, "Gold Leaf");
        addEntityType(PvZEntityRegister.FLOWER_POT, "Flower Pot");
        addEntityType(PvZEntityRegister.CABBAGE_PULT, "Cabbage-pult");
        addEntityType(PvZEntityRegister.KERNEL_PULT, "Kernel-pult");
        addEntityType(PvZEntityRegister.BUTTER_PULT, "Butter-pult");
        addEntityType(PvZEntityRegister.GARLIC, "Garlic");
        addEntityType(PvZEntityRegister.UMBRELLA_LEAF, "Umbrella Leaf");
        addEntityType(PvZEntityRegister.MELON_PULT, "Melon-pult");
        addEntityType(PvZEntityRegister.WINTER_MELON, "Winter Melon");
        addEntityType(PvZEntityRegister.BAMBOO_LORD, "Bamboo Lord");
        addEntityType(PvZEntityRegister.ICEBERG_LETTUCE, "Iceberg Lettuce");
        addEntityType(PvZEntityRegister.SPIKE_ROCK, "Spikerock");
        addEntityType(PvZEntityRegister.BONK_CHOY, "Bonk Choy");
        addEntityType(PvZEntityRegister.IMITATER, "Imitater");
        addEntityType(PvZEntityRegister.COB_CANNON, "Cob Cannon");
        addEntityType(PvZEntityRegister.ORIGIN_SHROOM,"Origin Shroom");
        addEntityType(PvZEntityRegister.SUN_FLOWER_ZOMBIE, "Sunflower Zombie");
        addEntityType(PvZEntityRegister.CHOMPER_ZOMBIE, "Chomper Zombie");
        addEntityType(PvZEntityRegister.STEEL_PUMPKIN_ZOMBIE, "Steel Pumpkin Zombie");
        addEntityType(PvZEntityRegister.FLOWER_POT_ZOMBIE, "Flower Pot Zombie");
        addEntityType(PvZEntityRegister.EDGAR_090547, "Edgar 090547");
        addEntityType(PvZEntityRegister.AIRBORNE_ZOMBIE, "Airborne Zombie");
        addEntityType(PvZEntityRegister.MC_ZOMBIE, "MC Zombie");
        addEntityType(PvZEntityRegister.TARGET_ARROW_ZOMBIE, "Target Arrow Zombie");
        addEntityType(PvZEntityRegister.RED_EDGE_ZOMBIE, "Red Edge Zombie");
        addEntityType(PvZEntityRegister.MJ_ZOMBIE, "MJ Zombie");
        addEntityType(PvZEntityRegister.DANCER_BACKUP_ZOMBIE, "Dancer Backup Zombie");
        addEntityType(PvZEntityRegister.RED_SUN, "Red Sun");
        addEntityType(PvZEntityRegister.BEE, "Bee");

        addEntityType(PvZEntityRegister.STEEL_PUMPKIN, "Steel Pumpkin");
        addEntityType(PvZEntityRegister.ICE_CABBAGE_PULT, "Ice Cabbage-pult");
        addEntityType(PvZEntityRegister.FIRE_PEASHOOTER, "Fire Peashooter");
        addEntityType(PvZEntityRegister.CHORUS_FRUIT_PULT, "Chorus Fruit-pult");
        addEntityType(PvZEntityRegister.ROTATE_RADISH, "Rotate Radish");
        addEntityType(PvZEntityRegister.BURST_KERNEL_PULT, "Burst Kernel-pult");
        addEntityType(PvZEntityRegister.BLAZE_WART, "Blaze Wart");
        addEntityType(PvZEntityRegister.GRASS_CARP, "Grass Carp");
        addEntityType(PvZEntityRegister.SELF_IMITATER, "Self Imitater");
        addEntityType(PvZEntityRegister.RE_ICEPEA, "Re-icepea");
        addEntityType(PvZEntityRegister.SCULK_SHROOM, "Sculk Shroom");
        addEntityType(PvZEntityRegister.BEE_SHOOTER, "Bee Shooter");
        addEntityType(PvZEntityRegister.FODDER_BUSH, "Fodder Bush");
        addEntityType(PvZEntityRegister.GOLDEN_MELON_PULT, "Golden Melon-pult");
        addEntityType(PvZEntityRegister.PRIMAL_PEA_SHOOTER, "Primal Peashooter");
        addEntityType(PvZEntityRegister.GOO_PEA_SHOOTER, "Goo Peashooter");
        addEntityType(PvZEntityRegister.MAGNIFYING_GRASS, "Magnifying Grass");
        addEntityType(PvZEntityRegister.PEA_POD, "Peapod");
        /* ==================== 效果 ==================== */
        addEffect(PvZEffectRegister.COLD_EFFECT, "Cold");
        addEffect(PvZEffectRegister.FROZEN_EFFECT, "Frozen");
        addEffect(PvZEffectRegister.EXCITE_EFFECT, "Excitement");
        addEffect(PvZEffectRegister.LIGHT_EYE_EFFECT, "Light Eye");
        addEffect(PvZEffectRegister.BUTTER_EFFECT, "Buttered");
        addEffect(PvZEffectRegister.ENERGETIC_EFFECT, "Energetic");
        addEffect(PvZEffectRegister.HALITOSIS, "Halitosis");
        addEffect(PvZEffectRegister.GOO_POISON, "Goo Poison");
        /* ==================== 群系 ==================== */
        addBiome(PvZBiome.ZEN_GARDEN,"Zen garden");
        addBiome(PvZBiome.ANCIENT_EGYPT,"Ancient Egypt");
        addBiome(PvZBiome.PIRATE_HARBOR,"Pirate Harbor");
        addBiome(PvZBiome.WILD_WEST,"Wild West");
        addBiome(PvZBiome.FUTURE_WORLD,"Future World");
        /* ==================== 药水 ==================== */
        addPotion("excite_potion_1", "Excitement");
        addPotion("excite_potion_2", "Greater Excitement");
        addPotion("excite_potion_3", "Long Excitement");
        addPotion("light_eye_potion_1", "Light Eye");
        addPotion("light_eye_potion_2", "Long Light Eye");


        add("item.pvz.lily_pad", "Lily Pad");
        add("item.pvz.slot_machine", "Slot Machine");
        add("entity.pvz.pot_grass", "Pot Grass");
        add("entity.pvz.water_pot", "Water Pot");

        /* ==================== 按键绑定 ==================== */
        add("key.pvz.show_overlay", "Show PVZ Overlay");
        add("key.pvz.left_toggle", "Toggle Left Card Slot");
        add("key.pvz.right_toggle", "Toggle Right Card Slot");
        add("key.categories.pvz.pvz", "Plants vs Zombies");

        /* ==================== GUI ==================== */
        add("gui.pvz.dave_shop.title", "Crazy Dave's Shop");
        add("gui.pvz.sun_shop.title", "Sun Shop");
        add("gui.pvz.penny_shop.title", "Penny's Shop");
        add("gui.pvz.dave_shop.buy", "Buy");
        add("gui.pvz.shop.left_time", "Refreshes in %s ticks");
        add("gui.pvz.shop.more_energy", "Max Energy +1");
        add("gui.pvz.shop.more_slot", "Card Slot +1");
        add("gui.pvz.shop.money_1", "1,000 Money");
        add("gui.pvz.shop.money_2", "2,000 Money");
        add("gui.pvz.shop.money_3", "3,000 Money");
        add("gui.pvz.shop.money_4", "4,000 Money");
        add("gui.pvz.shop.money_5", "5,000 Money");
        add("gui.pvz.shop.money_10", "10,000 Money");

        add("gui.pvz.almanac.show", "Almanac");
        add("gui.pvz.card_pack.show", "Card Pack");
        add("gui.pvz.pea_gun.show", "Pea Gun");
        add("gui.pvz.imitater.show", "Imitater");
        add("gui.pvz.card_fusion", "Card Fusion");
        add("gui.pvz.fragment_splice", "Splice");
        add("gui.pvz.card_fusion_table.tip1", "The fusion table needs the combined power of essence and sun to run");
        add("gui.pvz.card_fusion_table.tip2", "Put a Sun Storage Sapling on the left to supply sun");
        add("gui.pvz.card_fusion_table.tip3", "Put any type of Essence on the right to supply essence");
        add("gui.pvz.fragment_splice.tip1", "Requires a Sun Storage Sapling filled with sun");
        add("gui.pvz.fragment_splice.tip2", "Place 16 Enjoy Cards of that plant on the outer ring");
        add("gui.pvz.fragment_splice.tip3", "Place 8 Essences of that plant on the middle ring");
        add("gui.pvz.fragment_splice.tip4", "Place 1 card template of that plant in the center");
        add("gui.pvz.pea_gun.tip1", "Sneak + use to open bullet menu");
        add("gui.pvz.pea_gun.tip2", "Hold right-click to fire");
        add("gui.pvz.sun_converter.tip1", "Collects nearby sun into the container");
        add("gui.pvz.sun_converter.tip2", "Accepts Sun Storage Saplings and damaged items");
        add("gui.pvz.slot_machine.slow", "Slow Spin");
        add("gui.pvz.slot_machine.fast", "Fast Spin");
        add("gui.pvz.slot_machine.tip1", "Cost: 1 Lottery Ticket");
        add("gui.pvz.slot_machine.tip2", "Sun: %s / %s");
        add("gui.pvz.slot_machine.tip3", "Remaining chances: %s");
        add("gui.pvz.challenge_info.wave", "Wave %s (Duration %s ticks)");
        add("gui.pvz.challenge_info.spawn", "  @%s ticks, x%s %s");

        /* ==================== Tooltip ==================== */
        add("tooltip.pvz.card_sun_cost", "Sun Cost: %s");
        add("tooltip.pvz.card_cd", "Cooldown: %s");
        add("tooltip.pvz.card_required_level", "Requires Tree Level %s");
        add("tooltip.pvz.imitater_card.empty", "Empty (place a plant card inside)");
        add("tooltip.pvz.imitater_card.full", "Imitating: %s");
        add("tooltip.pvz.challenge_envelope1", "Right-click the top of a block to start the challenge");
        add("tooltip.pvz.challenge_envelope2", "Right-click the air with main hand to view challenge details");
        add("tooltip.pvz.challenge_envelope.empty", "No challenge bound");
        add("tooltip.pvz.challenge_envelope.unloaded", "Challenge not loaded");
        add("tooltip.pvz.sun_storage_sapling.use", "Right-click to release sun");
        add("tooltip.pvz.sun_storage_sapling.amount", "Sun: %s");
        add("tooltip.pvz.sun_storage_sapling.once", "Single-use");
        add("tooltip.pvz.time_source", "A fragment of frozen time");
        add("tooltip.pvz.time_key_use", "In Development :Right-click in an unlocked PVZ biome to travel; click Penny in the overworld to enter");
        add("tooltip.pvz.wait_for_update", "Content coming soon");
        add("tooltip.pvz.edgar_doll", "Use in a PVZ biome to summon a boss. Reusable.");
        add("msg.pvz.edgar_doll.wrong_biome", "The boss can only be summoned in a PVZ biome");
        add("msg.pvz.edgar_doll.already", "A boss is already present nearby");
        add("msg.pvz.edgar_doll.success", "The boss has been summoned! Defeat it to advance");
        add("msg.pvz.biome_unlock.success", "Unlocked next region: %s");
        add("msg.pvz.biome_locked", "This region is still locked — leave or unlock it to stop the curse");
        add("msg.pvz.time_key.got_doll", "Received an Edgar Doll — use it in a biome to summon the boss");
        add("msg.pvz.time_key.no_target", "No unlocked regions available");
        add("msg.pvz.time_key.biome_locked", "This region is locked — right-click only works in unlocked regions");
        add("msg.pvz.time_key.dimension_missing", "The PVZ dimension is not available yet");
        add("msg.pvz.time_key.locate_fail", "Could not locate that region. Try again closer to the center.");
        add("tooltip.pvz.bowling_glove.empty", "Empty - right-click a bowling plant to load");
        add("tooltip.pvz.bowling_glove.full", "Loaded: %s");
        add("tooltip.pvz.pea_gun", "Hold right-click to fire");
        add("tooltip.pvz.resource_collector.use", "Right-click to pick up · Sneak to collect in range");
        add("tooltip.pvz.resource_collector.info", "Range: %s blocks / area %s blocks");
        add("tooltip.pvz.balloon", "Grants Slow Falling");
        add("tooltip.pvz.sun_cost", "Costs %s Sun");
        add("tooltip.pvz.jack_box", "A surprise could be inside...");
        add("tooltip.pvz.zombie_flag", "A burst of zombie speed");
        add("tooltip.pvz.origin_shovel","Right click plant to shovel it without consuming durability");
        add("tooltip.pvz.origin_sword","Right click plants to use plant food");
        add("tooltip.pvz.hammer.use","Right hammer zombies or pot");
        add("tooltip.pvz.slot_machine.random","Random");
        add("tooltip.pvz.zombie_doll","Holding this doll can prevent it from being stolen by bungee zombies.");

        add("command.pvz.invasion_active", "This player is already in the invasion");
        add("command.pvz.invasion_cancelled", "The current invasion has been canceled");
        add("command.pvz.invasion_scheduled", "Invasion of %1$s scheduled, starting in %2$s ticks");
        add("command.pvz.no_invasion", "There are currently no ongoing intrusions");
        add("command.pvz.player_offline", "Player is offline:%1$s");
        add("command.pvz.resource_modified", "The %2$s of %1$s has been adjusted to %3$s");
        add("command.pvz.unknown_invasion", "Unknown Intrusion:%1$s");

        /* ==================== 提示 ==================== */
        add("help.pvz.ground", "This block cannot hold a plant");
        add("help.pvz.water", "This plant needs water");
        add("help.pvz.upgrade", "This card requires an existing plant to upgrade");
        add("help.pvz.outer", "Outer plants cannot be placed directly");
        add("help.pvz.outer_full", "This plant already has an outer layer");
        add("help.pvz.heal_wrong_plant", "This card cannot heal that plant");
        add("help.pvz.upgrade_wrong_plant", "This card cannot upgrade that plant");
        add("help.pvz.cd", "Still cooling down");
        add("help.pvz.lock", "Locked - level up the tree first");
        add("help.pvz.sun", "Not enough sun (need %s)");
        add("help.pvz.multiple_sun", "Not enough sun for multiple cards");
        add("help.pvz.empty_content", "Card is empty");
        add("help.pvz.invalid_content", "Invalid card content");
        add("help.pvz.broken", "Item is broken");
        add("help.pvz.no_bullet", "No bullets");
        add("help.pvz.no_shoot_mode", "No shoot mode selected");
        add("help.pvz.challenge_not_top", "Must right-click on top of a block");
        add("help.pvz.no_challenge", "No challenge bound to this envelope");
        add("help.pvz.full_challenge", "Too close to an existing challenge");
        add("help.pvz.wrong_challenge", "Cannot start the challenge here");
        add("help.pvz.bowling_glove.empty", "No bowling plant loaded");
        add("help.pvz.bowling_glove.fail", "Target is not a bowling plant");
        add("help.pvz.is_unlocked", " unlocked!");

        /* ==================== 卡片 / 分类 ==================== */
        add("pvz.card.slot_locked", "Card moved out - slot is locked by tree level");
        add("pvz.category.all", "All");
        add("pvz.category.plants", "Plants");
        add("pvz.category.zombies", "Zombies");

        /* ==================== 挑战 ==================== */
        add("challenge.pvz.title", "Challenge");
        add("challenge.pvz.win_title", "Victory!");
        add("challenge.pvz.loss_title", "Failed");
        add("challenge.pvz.unlock", "%s unlocked");
        add("challenge.pvz.round", "Wave %s");
        add("challenge.pvz.too_far_away", "You are too far from the challenge");

        /* ==================== 入侵 ==================== */
        add("invasion.pvz.start", "A zombie invasion is coming!");
        add("invasion.pvz.mission.finish", "Mission stage %s complete");
        add("invasion.pvz.mission.kill.name", "Slay Zombies");
        add("invasion.pvz.mission.instant_kill.name", "Multi-kill");
        add("invasion.pvz.mission.collect_sun.name", "Collect Sun");
        add("invasion.pvz.safe_days","safe days %s");
        add("invasion.pvz.huge_wave","A huge wave is coming");
        add("invasion.pvz.timeout", "Invasion timed out. Remaining zombies cleared. %s safe days restored.");

        /* ==================== 死亡信息 ==================== */
        add("death.attack.pvz.plant_wilt", "%1$s withered away");
        add("death.attack.pvz.plant_wilt.player", "%1$s was withered by %2$s");
        add("death.attack.pvz.plant_wilt.item", "%1$s was withered by %2$s using %3$s");
        add("death.attack.pvz.chomper_plant", "%1$s was eaten");
        add("death.attack.pvz.chomper_plant.player", "%1$s was eaten by %2$s");
        add("death.attack.pvz.chomper_plant.item", "%1$s was eaten by %2$s using %3$s");
        add("death.attack.pvz.pea", "%1$s was shot by %2$s");
        add("death.attack.pvz.pea.player", "%1$s was shot by %2$s");
        add("death.attack.pvz.pea.item", "%1$s was shot by %2$s using %3$s");
        add("death.attack.pvz.snow_pea", "%1$s was frozen to death by %2$s");
        add("death.attack.pvz.snow_pea.player", "%1$s was frozen to death by %2$s");
        add("death.attack.pvz.snow_pea.item", "%1$s was frozen to death by %2$s using %3$s");
        add("death.attack.pvz.flame_pea", "%1$s was burned by %2$s");
        add("death.attack.pvz.flame_pea.player", "%1$s was burned by %2$s");
        add("death.attack.pvz.flame_pea.item", "%1$s was burned by %2$s using %3$s");
        add("death.attack.pvz.spore", "%1$s was hit by %2$s");
        add("death.attack.pvz.spore.player", "%1$s was hit by %2$s");
        add("death.attack.pvz.spore.item", "%1$s was hit by %2$s using %3$s");
        add("death.attack.pvz.fume", "%1$s was fumigated by %2$s");
        add("death.attack.pvz.fume.player", "%1$s was fumigated by %2$s");
        add("death.attack.pvz.fume.item", "%1$s was fumigated by %2$s using %3$s");
        add("death.attack.pvz.star", "%1$s was struck by %2$s");
        add("death.attack.pvz.star.player", "%1$s was struck by %2$s");
        add("death.attack.pvz.star.item", "%1$s was struck by %2$s using %3$s");
        add("death.attack.pvz.metal", "%1$s was shot by %2$s");
        add("death.attack.pvz.metal.player", "%1$s was shot by %2$s");
        add("death.attack.pvz.metal.item", "%1$s was shot by %2$s using %3$s");
        add("death.attack.pvz.cabbage", "%1$s was hit by %2$s");
        add("death.attack.pvz.cabbage.player", "%1$s was hit by %2$s");
        add("death.attack.pvz.cabbage.item", "%1$s was hit by %2$s using %3$s");
        add("death.attack.pvz.kernel", "%1$s was hit by %2$s");
        add("death.attack.pvz.kernel.player", "%1$s was hit by %2$s");
        add("death.attack.pvz.kernel.item", "%1$s was hit by %2$s using %3$s");
        add("death.attack.pvz.corn", "%1$s was blasted by %2$s");
        add("death.attack.pvz.corn.player", "%1$s was blasted by %2$s");
        add("death.attack.pvz.corn.item", "%1$s was blasted by %2$s using %3$s");
        add("death.attack.pvz.butter", "%1$s was hit by %2$s");
        add("death.attack.pvz.butter.player", "%1$s was hit by %2$s");
        add("death.attack.pvz.butter.item", "%1$s was hit by %2$s using %3$s");
        add("death.attack.pvz.melon", "%1$s was splattered by %2$s");
        add("death.attack.pvz.melon.player", "%1$s was splattered by %2$s");
        add("death.attack.pvz.melon.item", "%1$s was splattered by %2$s using %3$s");
        add("death.attack.pvz.winter_melon", "%1$s was frozen by %2$s");
        add("death.attack.pvz.winter_melon.player", "%1$s was frozen by %2$s");
        add("death.attack.pvz.winter_melon.item", "%1$s was frozen by %2$s using %3$s");
        add("death.attack.pvz.ball", "%1$s was hit by %2$s");
        add("death.attack.pvz.ball.player", "%1$s was hit by %2$s");
        add("death.attack.pvz.ball.item", "%1$s was hit by %2$s using %3$s");
        add("death.attack.pvz.normal", "%1$s was killed by %2$s");
        add("death.attack.pvz.normal.player", "%1$s was killed by %2$s");
        add("death.attack.pvz.normal.item", "%1$s was killed by %2$s using %3$s");
        add("death.attack.pvz.eat", "%1$s was eaten by %2$s");
        add("death.attack.pvz.eat.player", "%1$s was eaten by %2$s");
        add("death.attack.pvz.eat.item", "%1$s was eaten by %2$s using %3$s");
        add("death.attack.pvz.explosion", "%1$s was blown up by %2$s");
        add("death.attack.pvz.explosion.player", "%1$s was blown up by %2$s");
        add("death.attack.pvz.explosion.item", "%1$s was blown up by %2$s using %3$s");
        add("death.attack.pvz.deadly", "%1$s was destroyed by %2$s");
        add("death.attack.pvz.deadly.player", "%1$s was destroyed by %2$s");
        add("death.attack.pvz.deadly.item", "%1$s was destroyed by %2$s using %3$s");
        add("death.attack.pvz.crush", "%1$s was crushed by %2$s");
        add("death.attack.pvz.crush.player", "%1$s was crushed by %2$s");
        add("death.attack.pvz.crush.item", "%1$s was crushed by %2$s using %3$s");
        add("death.attack.pvz.thorn", "%1$s was pricked to death by %2$s");
        add("death.attack.pvz.thorn.player", "%1$s was pricked to death by %2$s");
        add("death.attack.pvz.thorn.item", "%1$s was pricked to death by %2$s using %3$s");
        add("death.attack.pvz.ice", "%1$s was frozen by %2$s");
        add("death.attack.pvz.ice.player", "%1$s was frozen by %2$s");
        add("death.attack.pvz.ice.item", "%1$s was frozen by %2$s using %3$s");
        add("death.attack.pvz.flame", "%1$s was burned by %2$s");
        add("death.attack.pvz.flame.player", "%1$s was burned by %2$s");
        add("death.attack.pvz.flame.item", "%1$s was burned by %2$s using %3$s");

        /* ==================== 附魔 ==================== */
        add("enchantment.pvz.energy_transfer", "Energy Transfer");
        add("enchantment.pvz.energy_transfer.desc", "Right-click a plant with a sword to spend plant food and trigger its super ability.");
        add("enchantment.pvz.range_reach", "Range Reach");
        add("enchantment.pvz.sun_mending", "Sun Mending");
        add("enchantment.pvz.sun_mending.desc", "Collect sun to repair the durability of your equipment.");
        add("enchantment.pvz.sun_shovel", "Sun Shovel");
        add("enchantment.pvz.sun_shovel.desc", "Shoveling a plant refunds sun based on its planting cost.");
        add("enchantment.pvz.bandage", "Bandage");
        add("enchantment.pvz.bandage.desc", "Use plant cards to heal your summoned creatures.");
        add("enchantment.pvz.charm", "Charm");
        add("enchantment.pvz.charm.desc", "Summoned creatures become charmed.");
        add("enchantment.pvz.immediate_cd", "Immediate Cooldown");
        add("enchantment.pvz.immediate_cd.desc", "Plant cards have a chance to skip most of their cooldown.");
        add("enchantment.pvz.break_out", "Break Out");
        add("enchantment.pvz.break_out.desc", "Summoned plants have a chance to unleash their super ability.");
        add("enchantment.pvz.densely_plant", "Densely Plant");
        add("enchantment.pvz.densely_plant.desc", "Raises the plant placement limit.");
        add("enchantment.pvz.soilless_plant", "Soilless Plant");
        add("enchantment.pvz.soilless_plant.desc", "Lets plants be placed independently on any land.");
        add("enchantment.pvz.level_up.desc", "Summoned creatures have a chance to gain a few random levels.");

        /* ==================== 图鉴 ==================== */
        add("almanac.pvz.health", "Health");
        add("almanac.pvz.sun_cost", "Sun Cost");
        add("almanac.pvz.cool_down", "Cooldown");
        add("almanac.pvz.bullet_damage", "Bullet Damage");
        add("almanac.pvz.shoot_cd", "Shoot Interval");
        add("almanac.pvz.shoot_range", "Shoot Range");
        add("almanac.pvz.cold_level", "Cold Level");
        add("almanac.pvz.cold_time", "Cold Duration");
        add("almanac.pvz.gen_cd", "Produce Interval");
        add("almanac.pvz.gen_sun_amount", "Sun Produced");
        add("almanac.pvz.small_gen_sun_amount", "Small Sun Produced");
        add("almanac.pvz.armor", "Armor");
        add("almanac.pvz.armor_toughness", "Armor Toughness");
        add("almanac.pvz.explode_damage", "Explosion Damage");
        add("almanac.pvz.explode_range", "Explosion Range");
        add("almanac.pvz.prepare_cd", "Prepare Time");
        add("almanac.pvz.attack_damage", "Attack Damage");
        add("almanac.pvz.rest_time", "Rest Time");
        add("almanac.pvz.work_time", "Work Time");
        add("almanac.pvz.frozen_level", "Freeze Level");
        add("almanac.pvz.frozen_time", "Freeze Duration");
        add("almanac.pvz.again_chance", "Retry Chance");
        add("almanac.pvz.spike_count", "Spike Count");
        add("almanac.pvz.attack_cd", "Attack Interval");
        add("almanac.pvz.heat_pea_range", "Flame Pea Range");
        add("almanac.pvz.attack_range", "Attack Range");
        add("almanac.pvz.work_cd", "Work Interval");
        add("almanac.pvz.work_range", "Work Range");
        add("almanac.pvz.awake_time", "Awake Time");
        add("almanac.pvz.effect_time", "Effect Duration");

        /* ==================== 资源 ==================== */
        add("resource.pvz.tree_lvl", "Tree Level");
        add("resource.pvz.tree_xp", "Tree XP");
        add("resource.pvz.sun_num", "Sun");
        add("resource.pvz.max_energy_num", "Max Energy");
        add("resource.pvz.energy_num", "Energy");
        add("resource.pvz.money", "Money");
        add("resource.pvz.gem_num", "Gems");
        add("resource.pvz.lottery_chance", "Lottery Tickets");
        add("resource.pvz.group_type", "Group");
        add("resource.pvz.slot_num", "Card Slots");
        add("resource.pvz.mission_finish_time", "Missions Completed");
        add("resource.pvz.no_fog_tick", "Fog Timer");

        /* ==================== 冷却等级 ==================== */
        add("misc.pvz.cd.default", "Default");
        add("misc.pvz.cd.super_fast", "Super Fast");
        add("misc.pvz.cd.huge_fast", "Huge Fast");
        add("misc.pvz.cd.very_fast", "Very Fast");
        add("misc.pvz.cd.fast", "Fast");
        add("misc.pvz.cd.little_fast", "Little Fast");
        add("misc.pvz.cd.normal", "Normal");
        add("misc.pvz.cd.little_slow", "Little Slow");
        add("misc.pvz.cd.slow", "Slow");
        add("misc.pvz.cd.very_slow", "Very Slow");
        add("misc.pvz.cd.huge_slow", "Huge Slow");
        add("misc.pvz.cd.super_slow", "Super Slow");

        /* ==================== 卡片品质 ==================== */
        add("misc.pvz.rank.gray", "Gray");
        add("misc.pvz.rank.white", "White");
        add("misc.pvz.rank.green", "Green");
        add("misc.pvz.rank.blue", "Blue");
        add("misc.pvz.rank.purple", "Purple");
        add("misc.pvz.rank.gold", "Gold");
        add("misc.pvz.rank.red", "Red");
        add("misc.pvz.rank.black", "Black");
        add("misc.pvz.rank.mega", "Mega");

        /* ==================== 精华 ==================== */
        add("misc.pvz.essence.origin", "Origin Essence");
        add("misc.pvz.essence.appease", "Appease Essence");
        add("misc.pvz.essence.light", "Light Essence");
        add("misc.pvz.essence.explosion", "Explosion Essence");
        add("misc.pvz.essence.defence", "Defence Essence");
        add("misc.pvz.essence.ice", "Ice Essence");
        add("misc.pvz.essence.enforce", "Enforce Essence");
        add("misc.pvz.essence.toxic", "Toxic Essence");
        add("misc.pvz.essence.assist", "Assist Essence");
        add("misc.pvz.essence.magic", "Magic Essence");
        add("misc.pvz.essence.flame", "Flame Essence");
        add("misc.pvz.essence.spear", "Spear Essence");
        add("misc.pvz.essence.arma", "Arma Essence");
        add("misc.pvz.essence.electric", "Electric Essence");
        add("misc.pvz.essence.shadow", "Shadow Essence");

        /* ==================== 技能 ==================== */
        add("skill.pvz.fast_cd", "Fast Cooldown");
        add("skill.pvz.fast_cd.desc", "Reduces the card's cooldown.");
        add("skill.pvz.less_sun", "Sun Discount");
        add("skill.pvz.less_sun.desc", "Reduces the card's sun cost.");
        add("skill.pvz.plant_more_life", "Extra Plant Health");
        add("skill.pvz.plant_more_life.desc", "Increases the plant's max health.");
        add("skill.pvz.pea_damage", "Pea Damage");
        add("skill.pvz.pea_damage.desc", "Increases pea damage.");
        add("skill.pvz.normal_bomb_damage", "Bomb Damage");
        add("skill.pvz.normal_bomb_damage.desc", "Increases bomb damage.");
        add("skill.pvz.nut_more_life", "Nut Extra Health");
        add("skill.pvz.nut_more_life.desc", "Increases wall-nut health.");
        add("skill.pvz.mine_fast_prepare", "Faster Arming");
        add("skill.pvz.mine_fast_prepare.desc", "Shortens the mine arming time.");
        add("skill.pvz.normal_enhance_strength", "Enhance Strength");
        add("skill.pvz.normal_enhance_strength.desc", "Increases general damage.");
        add("skill.pvz.spore_damage", "Spore Damage");
        add("skill.pvz.spore_damage.desc", "Increases spore damage.");
        add("skill.pvz.high_explode_damage", "High Explosion Damage");
        add("skill.pvz.high_explode_damage.desc", "Increases large explosion damage.");
        add("skill.pvz.squash_again", "Squash Recharge");
        add("skill.pvz.squash_again.desc", "Chance to reuse the squash attack.");
        add("skill.pvz.spike_damage", "Spike Damage");
        add("skill.pvz.spike_damage.desc", "Increases spike damage.");
        add("skill.pvz.more_spike", "More Spikes");
        add("skill.pvz.more_spike.desc", "Adds extra spike rows.");
        add("skill.pvz.heat_pea_range", "Flame Range");
        add("skill.pvz.heat_pea_range.desc", "Increases the flame pea conversion range.");
        add("skill.pvz.wood_more_life", "Torchwood Health");
        add("skill.pvz.wood_more_life.desc", "Increases torchwood health.");
        add("skill.pvz.more_light_range", "Light Range");
        add("skill.pvz.more_light_range.desc", "Increases the light radius.");
        add("skill.pvz.night_vision", "Night Vision");
        add("skill.pvz.night_vision.desc", "Grants night vision in dark areas.");
        add("skill.pvz.more_thorn_damage", "Thorn Damage");
        add("skill.pvz.more_thorn_damage.desc", "Increases thorn reflection damage.");
        add("skill.pvz.blow_strength", "Blow Strength");
        add("skill.pvz.blow_strength.desc", "Increases blover push strength.");
        add("skill.pvz.split_double_chance", "Split Double Chance");
        add("skill.pvz.split_double_chance.desc", "Chance for split pea to fire twice.");
        add("skill.pvz.more_star_damage", "Star Damage");
        add("skill.pvz.more_star_damage.desc", "Increases star projectile damage.");
        add("skill.pvz.less_work_cd", "Faster Work");
        add("skill.pvz.less_work_cd.desc", "Reduces producer work cooldown.");
        add("skill.pvz.more_cabbage_damage", "Cabbage Damage");
        add("skill.pvz.more_cabbage_damage.desc", "Increases cabbage projectile damage.");
        add("skill.pvz.more_kernel_damage", "Kernel Damage");
        add("skill.pvz.more_kernel_damage.desc", "Increases kernel projectile damage.");
        add("skill.pvz.more_garlic_life", "Garlic Health");
        add("skill.pvz.more_garlic_life.desc", "Increases garlic health.");
        add("skill.pvz.more_melon_damage", "Melon Damage");
        add("skill.pvz.more_melon_damage.desc", "Increases melon projectile damage.");
        add("skill.pvz.more_swing_damage", "Swing Damage");
        add("skill.pvz.more_swing_damage.desc", "Increases melee swing damage.");
        add("skill.pvz.advance_gold", "Advance Gold");
        add("skill.pvz.advance_gold.desc", "Increases gold generation.");
        add("skill.pvz.ten_stars", "Ten Stars");
        add("skill.pvz.ten_stars.desc", "Chance to fire extra stars.");
        add("skill.pvz.small_bomb_damage", "Small Bomb Damage");
        add("skill.pvz.small_bomb_damage.desc", "Increases small bomb damage.");
        add("skill.pvz.more_guard_life", "Guard Health");
        add("skill.pvz.more_guard_life.desc", "Increases guard plant health.");
        add("skill.pvz.more_butter_damage", "Butter Damage");
        add("skill.pvz.more_butter_damage.desc", "Increases butter projectile damage.");
        add("skill.pvz.zombie_fast_move", "Zombie Fast Move");
        add("skill.pvz.zombie_fast_move.desc", "Zombified plants move faster.");
        add("skill.pvz.high_eat_damage", "High Eat Damage");
        add("skill.pvz.high_eat_damage.desc", "Increases eating damage.");
        add("skill.pvz.tough_body", "Tough Body");
        add("skill.pvz.tough_body.desc", "Grants armor to the zombie.");
        add("skill.pvz.wart_heal_cd", "Faster Healing");
        add("skill.pvz.wart_heal_cd.desc", "The Blaze Wart heals faster.");
        add("skill.pvz.imitater_chance", "Imitate Training");
        add("skill.pvz.imitater_chance.desc", "The Imitater gains more imitation chances.");
        add("skill.pvz.light_beam_damage", "Shoot Training");
        add("skill.pvz.light_beam_damage.desc", "Higher light beam damage.");
        add("skill.pvz.magnifying_grass_damage", "Light Focus");
        add("skill.pvz.magnifying_grass_damage.desc", "Increases the Magnifying Grass's super damage.");
        add("skill.pvz.grass_carp_damage", "More Healing");
        add("skill.pvz.grass_carp_damage.desc", "The plant heals more.");
        add("skill.pvz.grass_carp_heal_rage", "Wider Range");
        add("skill.pvz.grass_carp_heal_rage.desc", "The plant heals in a wider range.");
        add("skill.pvz.more_chorus_fruit_damage", "Pult Training");
        add("skill.pvz.more_chorus_fruit_damage.desc", "The plant deals higher damage.");
        add("skill.pvz.more_stone_pea_damage", "Shoot Training");
        add("skill.pvz.more_stone_pea_damage.desc", "The plant deals higher damage.");
        add("skill.pvz.more_golden_melon_damage", "Pult Training");
        add("skill.pvz.more_golden_melon_damage.desc", "The plant deals higher damage.");
        /* ==================== 配置：分组标题（level，无 ConfigValue，手写） ==================== */
        add("config.pvz.invasion.title", "Invasion Settings");
        add("config.pvz.invasion.title.tooltip", "Settings about invasion.");
        add("config.pvz.challenge.title", "Challenge Settings");
        add("config.pvz.challenge.title.tooltip", "Settings about challenge");
        add("config.pvz.rule.title", "Rule Settings");
        add("config.pvz.rule.title.tooltip", "Settings about global rules.");
        add("config.pvz.world.title", "World Settings");
        add("config.pvz.world.title.tooltip", "Settings about world.");
        add("config.pvz.world.biome.title", "Biome Settings");
        add("config.pvz.world.biome.title.tooltip", "Settings about the biome gen.");
        add("config.pvz.world.structure.title", "Structure Settings");
        add("config.pvz.world.structure.title.tooltip", "Settings about the structure gen.");
        add("config.pvz.world.ore.title", "Ore Settings");
        add("config.pvz.world.ore.title.tooltip", "Settings about the ore gen.");
        add("config.pvz.world.spawn_weight.title", "Entity Spawn Weights");
        add("config.pvz.world.spawn_weight.title.tooltip", "The Spawn Weight of entity.");
        add("config.pvz.entity.title", "Entity Settings");
        add("config.pvz.entity.title.tooltip", "Settings about entities.");
        add("config.pvz.player.title", "Player Settings");
        add("config.pvz.player.title.tooltip", "Settings about players.");
        add("config.pvz.zombie.title", "Zombie Settings");
        add("config.pvz.zombie.title.tooltip", "Settings about zombies.");
        add("config.pvz.plant.title", "Plant Settings");
        add("config.pvz.plant.title.tooltip", "Settings about plants.");
        add("config.pvz.entity.live_time.title", "Entity Live Time");
        add("config.pvz.entity.live_time.title.tooltip", "The Max live time for Entity like sun.");
        add("config.pvz.block.title", "Block Settings");
        add("config.pvz.block.title.tooltip", "Settings about blocks.");
        add("config.pvz.block.break.title", "Break Block Settings");
        add("config.pvz.block.break.title.tooltip", "Setting about break blocks.");
        add("config.pvz.item.title", "Item Settings");
        add("config.pvz.item.title.tooltip", "Settings about items.");
        add("config.pvz.overlay.title", "Overlay Settings");
        add("config.pvz.overlay.title.tooltip", "Overlay Settings");
        add("config.pvz.overlay.position", "Resource Bar Position");
        add("config.pvz.overlay.position.tooltip", "Drag to adjust the resource bar position");
        add("config.pvz.overlay.position.button", "Adjust Position");
        add("config.pvz.overlay.position.hint", "Drag the rectangle to reposition the resource bar. Press ESC to save");
        add("config.pvz.client.other.title", "Other Render Settings");
        add("config.pvz.client.other.title.tooltip", "Other Render Settings");
        /* ==================== 配置：值（label + tooltip 由 addConfigValue 自动生成） ==================== */
        var local = PvZConfig.LOCAL_CONFIG;
        var client = PvZConfig.CLIENT_CONFIG;
        /* Invasion */
        addConfigValue(local.InvasionSettings.SafeDayLength, "Safe Day Length");
        addConfigValue(local.InvasionSettings.ScatterInvasions, "Scatter Invasions");
        addConfigValue(local.InvasionSettings.InvasionIntervalLength, "Invasion Interval");
        addConfigValue(local.InvasionSettings.ShowEventMessages, "Show Event Messages");
        addConfigValue(local.InvasionSettings.EnableHugeWave, "Enable Huge Wave");
        addConfigValue(local.InvasionSettings.MaxSpawnEachPlayer, "Max Spawn Count");
        addConfigValue(local.InvasionSettings.MaxSpawnRange, "Max Spawn Range");
        addConfigValue(local.InvasionSettings.OvertimeGraceDays, "Overtime Grace");

        /* Challenge */
        addConfigValue(local.ChallengeSettings.EnableChallenge, "Enable Challenge");
        addConfigValue(local.ChallengeSettings.ChallengeWaitTime, "Challenge Wait Time");
        addConfigValue(local.ChallengeSettings.ChallengeRange, "Challenge Range");

        /* Rule */
        addConfigValue(local.RuleSettings.CanSpawnDefaultMonster, "Allow Default Monsters");
        addConfigValue(local.RuleSettings.AllZombieGiveXP, "All Zombies Give XP");
        addConfigValue(local.RuleSettings.DespawnOwnedEntityRange, "Despawn Owned Entity Range");
        addConfigValue(local.RuleSettings.GiveBeginnerReward, "Beginner Reward");
        addConfigValue(local.RuleSettings.AllowNaturalTurnOrigin, "Allow Natural Turn Origin");
        addConfigValue(local.RuleSettings.TeamAttack, "Plant Attack Team");
        addConfigValue(local.RuleSettings.LimitPlantCount, "Plant Count Limit");
        addConfigValue(local.RuleSettings.NeedUnlockToPlant, "Need Unlock To Plant");
        addConfigValue(local.RuleSettings.KeepSunWhenDie, "Keep Sun On Death");
        addConfigValue(local.RuleSettings.MaxDamageLimit, "Max Damage Limit");

        /* World */
        addConfigValue(local.WorldSettings.EnableBiome, "Enable Zen Garden Biome");

        /* Entity */
        addConfigValue(local.EntitySettings.PlayerInitialGroup, "Player Initial Group");
        addConfigValue(local.EntitySettings.PlayerBaseSunAmount, "Initial Sun");
        addConfigValue(local.EntitySettings.ZombieSetting.ZombieSuperChance, "Zombie Super Chance");
        addConfigValue(local.EntitySettings.ZombieSetting.ZombieSunChance, "Zombie Sun Chance");
        addConfigValue(local.EntitySettings.DropChanceMultiper, "Zombie Drop Multiplier");
        addConfigValue(local.EntitySettings.ZombieSetting.EnableZombieDropHands, "Enable Zombie Drop Parts");
        addConfigValue(local.EntitySettings.ZombieSetting.JackinboxBreak, "Jack-in-the-Box Break Blocks");
        addConfigValue(local.EntitySettings.ZombieSetting.ImmuneDamage, "Immune to Environmental Damage");
        addConfigValue(local.EntitySettings.PlantSetting.StrangeCatCount, "Strange Cat Limit");
        addConfigValue(local.EntitySettings.PlantSetting.PlantBulletMerge, "Plant Bullet Merge");
        addConfigValue(local.EntitySettings.PlantSetting.PlantMinimumLiveTick, "Plant Minimum Live Tick");
        addConfigValue(local.EntitySettings.EntityLiveTick.SunLiveTick, "Sun Live Tick");
        addConfigValue(local.EntitySettings.EntityLiveTick.CoinLiveTick, "Coin Live Tick");
        addConfigValue(local.EntitySettings.EntityLiveTick.JewelLiveTick, "Jewel Live Tick");
        addConfigValue(local.EntitySettings.EntityLiveTick.EnergyLiveTick, "Plant Food Live Tick");
        addConfigValue(local.EntitySettings.EntityLiveTick.YetiLiveTick, "Yeti Live Tick");
        addConfigValue(local.EntitySettings.EntityLiveTick.LawnMowerLiveTick, "Lawn Mower Live Tick");
        addConfigValue(local.EntitySettings.EntityLiveTick.ElementBallLiveTick, "Element Ball Live Tick");

        /* Block / Item */
        addConfigValue(local.BlockSettings.OriginEffectChance, "Origin Effect Chance");
        addConfigValue(local.BlockSettings.SaplingTurnChance, "Sapling Turn Chance");
        addConfigValue(local.BlockSettings.AmethystAngerChance, "Amethyst Anger Chance");
        addConfigValue(local.BlockSettings.PeaDropChance, "Pea Drop Chance");
        addConfigValue(local.BlockSettings.CabbageDropChance, "Cabbage Drop Chance");
        addConfigValue(local.ItemSettings.JackBoxSurpriseChance, "Jack-in-the-Box Surprise 1/N");

        /* Client */
        addConfigValue(client.OverlaySettings.RenderSunBar, "Show Sun Bar");
        addConfigValue(client.OverlaySettings.RenderMoneyBar, "Show Money Bar");
        addConfigValue(client.OverlaySettings.RenderGemBar, "Show Gem Bar");
        addConfigValue(client.OverlaySettings.RenderTreeLevel, "Show Tree Level Bar");
        addConfigValue(client.OverlaySettings.RenderPlantFoodBar, "Show Plant Food Bar");
        addConfigValue(client.OverlaySettings.RenderInvasionProgress, "Show Invasion Progress");
        addConfigValue(client.OverlaySettings.RenderFog, "Show Fog");
        addConfigValue(client.OverlaySettings.RenderCardSlots, "Show Card Slots");
        addConfigValue(client.OtherSettings.ShowPVZMainMenu, "Show PVZ Main Menu");

        /*Sound*/
        add("sound.pvz.advancement", "Advancement");
        add("sound.pvz.zombie_siren", "Zombie Siren");
        add("sound.pvz.plant_siren", "Plant Siren");
        add("sound.pvz.balloon_inflate", "Balloon Inflate");
        add("sound.pvz.balloon_pop", "Balloon Pop");
        add("sound.pvz.basketball", "Basketball");
        add("sound.pvz.big_chomp", "Big Chomp");
        add("sound.pvz.blover", "Blover");
        add("sound.pvz.pole", "Pole");
        add("sound.pvz.pole_jump", "Pole Jump");
        add("sound.pvz.hammer_bonk", "Hammer Bonk");
        add("sound.pvz.edgar_shoot", "Edgar Shoot");
        add("sound.pvz.edgar_explosion", "Edgar Explosion");
        add("sound.pvz.bowling", "Bowling");
        add("sound.pvz.bowling_hit", "Bowling Hit");
        add("sound.pvz.bug_spray", "Bug Spray");
        add("sound.pvz.bungee_scream", "Bungee Scream");
        add("sound.pvz.butter", "Butter");
        add("sound.pvz.no", "No");
        add("sound.pvz.cherry_bomb", "Cherry Bomb");
        add("sound.pvz.jewel_drop", "Jewel Drop");
        add("sound.pvz.chomp", "Chomp");
        add("sound.pvz.cob_launch", "Cob Launch");
        add("sound.pvz.coin_pick", "Coin Pickup");
        add("sound.pvz.crazy_dave", "Crazy Dave");
        add("sound.pvz.crazy_say", "Crazy Dave Says");
        add("sound.pvz.dave_hurt", "Dave Hurt");
        add("sound.pvz.dave_happy", "Dave Happy");
        add("sound.pvz.dance_music", "Dance Music");
        add("sound.pvz.jewel_pick", "Jewel Pickup");
        add("sound.pvz.dirt_rise", "Dirt Rise");
        add("sound.pvz.dolphin_say", "Dolphin Call");
        add("sound.pvz.dolphin_jump", "Dolphin Jump");
        add("sound.pvz.edgar_laugh", "Edgar Laugh");
        add("sound.pvz.doom_shroom", "Doom Shroom");
        add("sound.pvz.coin_drop", "Coin Drop");
        add("sound.pvz.car_explosion", "Car Explosion");
        add("sound.pvz.fertilizer", "Fertilizer");
        add("sound.pvz.final_win", "Final Win");
        add("sound.pvz.final_wave", "Final Wave");
        add("sound.pvz.flame_hit", "Flame Hit");
        add("sound.pvz.drag", "Drag");
        add("sound.pvz.frozen", "Frozen");
        add("sound.pvz.fume", "Fume");
        add("sound.pvz.ground_shake", "Ground Shake");
        add("sound.pvz.gargantuar_death", "Gargantuar Death");
        add("sound.pvz.zombie_groan", "Zombie Groan");
        add("sound.pvz.plant_death", "Plant Death");
        add("sound.pvz.huge_wave", "Huge Wave");
        add("sound.pvz.robot_move", "Robot Move");
        add("sound.pvz.imp_happy", "Imp Happy");
        add("sound.pvz.jack_music", "Jack-in-the-Box Music");
        add("sound.pvz.jack_surprise", "Jack-in-the-Box Surprise");
        add("sound.pvz.jalapeno", "Jalapeno");
        add("sound.pvz.steel_ladder", "Steel Ladder");
        add("sound.pvz.lawn_mower", "Lawn Mower");
        add("sound.pvz.gargantuar_groan", "Gargantuar Groan");
        add("sound.pvz.lose_music", "Lose Music");
        add("sound.pvz.magnet", "Magnet");
        add("sound.pvz.melon_hit", "Melon Hit");
        add("sound.pvz.hypno", "Hypno");
        add("sound.pvz.zombie_angry", "Zombie Angry");
        add("sound.pvz.change_paper", "Change Paper");
        add("sound.pvz.paper_broken", "Paper Broken");
        add("sound.pvz.phonograph", "Phonograph");
        add("sound.pvz.place_plant_ground", "Plant Placed on Ground");
        add("sound.pvz.place_plant_water", "Plant Placed in Water");
        add("sound.pvz.plastic_hit", "Plastic Hit");
        add("sound.pvz.pogo", "Pogo");
        add("sound.pvz.sun_pick", "Sun Pickup");
        add("sound.pvz.pool_cleaner", "Pool Cleaner");
        add("sound.pvz.potato_mine", "Potato Mine");
        add("sound.pvz.prize_drop", "Prize Drop");
        add("sound.pvz.puff", "Puff");
        add("sound.pvz.ready", "Ready");
        add("sound.pvz.dave_scream", "Dave Scream");
        add("sound.pvz.metal_hit", "Metal Hit");
        add("sound.pvz.shovel", "Shovel");
        add("sound.pvz.slot_machine", "Slot Machine");
        add("sound.pvz.slurp", "Slurp");
        add("sound.pvz.snow_shoot", "Snow Shoot");
        add("sound.pvz.splat", "Splat");
        add("sound.pvz.squash_hmm", "Squash Hmm");
        add("sound.pvz.swing", "Swing");
        add("sound.pvz.pult_throw", "Pult Throw");
        add("sound.pvz.vase_breaking", "Vase Breaking");
        add("sound.pvz.wake_up", "Wake Up");
        add("sound.pvz.win_music", "Win Music");
        add("sound.pvz.plant_grow", "Plant Grow");
        add("sound.pvz.plantern", "Plantern");
        add("sound.pvz.yuck", "Yuck");
        add("sound.pvz.car", "Car");
        add("sound.pvz.bruh", "Bruh");
        add("sound.pvz.penny_say", "Penny Says");
        add("sound.pvz.buzzer", "Buzzer");
        add("sound.pvz.pool", "Pool");
        add("sound.pvz.roof", "Roof");
        add("sound.pvz.bowling_music", "Bowling Music");

        /* ==================== 进度：核心主线 (Adventure) ==================== */
        add("advancement.pvz.almanac","Almanac");
        add("advancement.pvz.almanac.desc","Get Almanac");

        add("advancement.pvz.root", "Plants vs. Zombies");
        add("advancement.pvz.root.desc", "Welcome to the neighborhood! Beware of the zombies.");

        add("advancement.pvz.essence", "Origin Essence");
        add("advancement.pvz.essence.desc", "Obtain a piece of Origin Essence.");

        add("advancement.pvz.origin", "The Origin");
        add("advancement.pvz.origin.desc", "Place down the Origin Block to start your garden.");

        add("advancement.pvz.strange_help", "Strange Help");
        add("advancement.pvz.strange_help.desc", "Receive a mysterious challenge envelope.");

        add("advancement.pvz.first_plant", "First Sprout");
        add("advancement.pvz.first_plant.desc", "Plant your very first plant. Let the gardening begin!");

        /* ==================== 进度：种植与植物玩法 ==================== */
        add("advancement.pvz.upgrade_plant", "Evolution");
        add("advancement.pvz.upgrade_plant.desc", "Plant an upgraded plant.");

        add("advancement.pvz.super_plant", "Super Plant");
        add("advancement.pvz.super_plant.desc", "Place down a super-powered plant.");

        add("advancement.pvz.spudow", "Spudow!");
        add("advancement.pvz.spudow.desc", "Take down a zombie with a Potato Mine.");

        add("advancement.pvz.explodonator", "Explodonator");
        add("advancement.pvz.explodonator.desc", "Blow up at least 15 zombies with a single Cherry Bomb.");

        add("advancement.pvz.frozen", "Ice Ice Baby");
        add("advancement.pvz.frozen.desc", "Freeze a large group of zombies with Ice-shrooms or Iceberg Lettuces.");

        add("advancement.pvz.good_morning", "Good Morning!");
        add("advancement.pvz.good_morning.desc", "Use Coffee Beans to wake up 4 nocturnal plants.");

        add("advancement.pvz.popcorn", "Popcorn");
        add("advancement.pvz.popcorn.desc", "Unleash the power of the Cob Cannon.");

        add("advancement.pvz.disco", "Disco Inferno");
        add("advancement.pvz.disco.desc", "Charm a Dancing Zombie using a Hypno-shroom.");

        add("advancement.pvz.pea_holiday", "Pea Holiday");
        add("advancement.pvz.pea_holiday.desc", "Hit an entity with a pea shot by a Pea Gun.");

        /* ==================== 进度：经济与收集 ==================== */
        add("advancement.pvz.sunny_days", "Sunny Days");
        add("advancement.pvz.sunny_days.desc", "Accumulate 8,000 sun.");

        add("advancement.pvz.sun_storage", "Sun Bank");
        add("advancement.pvz.sun_storage.desc", "Store a staggering 100,000 sun in a Sun Storage.");

        add("advancement.pvz.rich_man", "Rich Man");
        add("advancement.pvz.rich_man.desc", "Amass a fortune of 100,000 coins.");

        add("advancement.pvz.towering_wisdom", "Towering Wisdom");
        add("advancement.pvz.towering_wisdom.desc", "Reach Tree Level 100.");

        add("advancement.pvz.slot_machine", "Slot Machine");
        add("advancement.pvz.slot_machine.desc", "Try your luck at the slot machine.");

        add("advancement.pvz.lucky_dog", "Lucky Dog");
        add("advancement.pvz.lucky_dog.desc", "Get 3 matching jewels on the slot machine.");

        add("advancement.pvz.trophy", "Diamond Trophy");
        add("advancement.pvz.trophy.desc", "Obtain the Diamond Sunflower Trophy.");

        add("advancement.pvz.taste_brain", "Taste the Brain");
        add("advancement.pvz.taste_brain.desc", "Eat a fake brain. Are you a zombie?");

        add("advancement.pvz.zen_garden", "Zen Garden");
        add("advancement.pvz.zen_garden.desc", "Find the peaceful Zen Garden biome.");

        /* ==================== 进度：战斗与入侵 ==================== */
        add("advancement.pvz.invasion", "Zombies on your Lawn!");
        add("advancement.pvz.invasion.desc", "Trigger a zombie invasion.");

        add("advancement.pvz.mission_expert", "Mission Expert");
        add("advancement.pvz.mission_expert.desc", "Complete 10 invasion missions.");

        add("advancement.pvz.mission_master", "Mission Master");
        add("advancement.pvz.mission_master.desc", "Complete 50 invasion missions.");

        add("advancement.pvz.kill_gargantuar", "Giant Slayer");
        add("advancement.pvz.kill_gargantuar.desc", "Defeat a Gargantuar or Giga-Gargantuar.");

        add("advancement.pvz.yeti", "Abominable Snowman");
        add("advancement.pvz.yeti.desc", "Kill a Yeti Zombie.");

        add("advancement.pvz.mustache_mode", "Mustache Mode");
        add("advancement.pvz.mustache_mode.desc", "Defeat Normal, Conehead, Flag, and Buckethead zombies with mustaches.");

        add("advancement.pvz.tooth", "Target Practice");
        add("advancement.pvz.tooth.desc", "Hit an entity with a Target Arrow.");

        /* ==================== 进度：特殊挑战与戴夫 ==================== */
        add("advancement.pvz.crazy_dave", "Crazy Dave's Deal");
        add("advancement.pvz.crazy_dave.desc", "Trade with Crazy Dave and get his car keys.");

        add("advancement.pvz.edgar_090505", "Edgar's Secret");
        add("advancement.pvz.edgar_090505.desc", "Complete the challenge on the Roof (Night).");

        add("advancement.pvz.edgar_090517", "Roof Again");
        add("advancement.pvz.edgar_090517.desc", "Complete the ultimate challenge on the Roof.");


        /*JADE*/
        add("tooltip.pvz.owner","Owner");
        add( "tooltip.pvz.defence","Defence HP");
        /*JEI*/
        add("config.jade.plugin_pvz.show_owner", "Show Owner");
        add("config.jade.plugin_pvz.entity_provider", "Entity Provider");
        add("config.jade.plugin_pvz.show_defence_health", "Show Defence Health");
        add("config.jade.plugin_pvz.show_skills", "Show Skills");

        /* ==================== 挑战：名称 + 信件文本 ==================== */
        addChallenge("strange_help", "Strange Help",
                "<<Plants vs. Zombies Mod Help>>",
                "When the zombies appear, do not be afraid",
                "You only need to stand there quietly",
                "and silently wait for the zombies to approach",
                "When the zombies eat your brain, you win!",
                "--This part of the help is a gift from the zombies--");
        addChallenge("grass_day", "Day Lawn",
                "Hello",
                "",
                "we are about to launch an all-out assault",
                "",
                "",
                "--Sincerely, the Zombies--");
        addChallenge("grass_night", "Night Lawn",
                "Hello",
                "",
                "we would love to drop by your place for a midnight snack.",
                "How does ice cream with brains sound?",
                "",
                "--Sincerely, the Zombies--");
        addChallenge("grass_add", "Extra Lawn",
                "An extra challenge by pvz", "", "", "", "", "");
        addChallenge("pool_day", "Day Pool",
                "Hello",
                "we heard you are throwing a pool party,",
                "and we are sure it must be great fun,",
                "so we will be right there",
                "",
                "--Sincerely, the Zombies--");
        addChallenge("pool_night", "Night Pool",
                "Hello",
                "this is your mother.",
                "Please open your front door,",
                "take down the defences on the lawn,",
                "and come try some meat wraps",
                "--Mom (not a zombie)--");
        addChallenge("pool_add", "Extra Pool",
                "An extra challenge by pvz", "", "", "", "", "");
        addChallenge("newspaper", "Newspaper (Extra: The Prince's Passing)",
                "Tonight's News",
                "The King's dearly beloved brother has passed away",
                "As a member of the royal family, the King grants a special farewell rite",
                "So long as four zombies carry the coffin along the way,",
                "he may earn a chance of rebirth in another world",
                "--Abyss Daily--");
        addChallenge("zombie_is_coming", "Newspaper (Headline: The Prince Arrives)",
                "Tonight's News",
                "The Prince's coffin is expected to arrive tonight,",
                "and word of his successful rebirth will be sent back.",
                "The surface entrance will then reopen,",
                "so let us wish the Prince and his retinue every success.",
                "--Abyss Daily--");
        addChallenge("brain_eating_movement", "Newspaper (Extra: The Brain-Eating Movement)",
                "Tonight's News",
                "A zombie uprising broke out in the capital of our Abyss Dynasty last night,",
                "demanding the right for common zombies to travel to the surface.",
                "If surface passage is wrested from the nobles,",
                "brain-eating will become far easier for the zombies of the Abyss.",
                "--Abyss Daily--");
        addChallenge("jump_jump_jump", "Jump! Jump! Jump!",
                "", "", "Welcome to the Jumping Party~", "", "", "");
        addChallenge("surrounded", "Surrounded",
                "", "", "Have you, all by yourself, surrounded us?", "", "", "");
        addChallenge("roof_day", "Day Roof",
                "Foolish player",
                "",
                "we are preparing to launch another all-out assault",
                "Do not be ungrateful",
                "",
                "--Sincerely, the Zombies--");
        addChallenge("roof_night", "Night Roof",
                "Foolish player",
                "you still have not met our righteous demands.",
                "We suggest you take our sincere advice,",
                "otherwise we will be forced to take extraordinary action.",
                "Hand over your house and everything in it at once!",
                "--Edgar 090505--");
        addChallenge("roof_again", "Roof Revenge",
                "Foolish player",
                "",
                "I told you I would be back!",
                "",
                "",
                "--Edgar 090517--");
        addChallenge("roof_extra", "Extra Roof",
                "",
                "Corporal,",
                "those routed zombies are regrouping with other units on the roof,",
                "be ready to respond.",
                "",
                "");
        addChallenge("game_over", "Notice",
                "Showtime: 22:00 to 08:00 the next day",
                "\"Curtain Call\" is the longest opera in our theatre's history.",
                "If something urgent arises and you must leave mid-performance,",
                "the front desk will refund your ticket in proportion to the time remaining.",
                "After the show, please leave in an orderly fashion.",
                "--Abyss Theatre--");
        addChallenge("zombie_barracks", "Zombie Army",
                "My main force arrives tonight.",
                "When the moon rises to the treetops of the Wall-nuts,",
                "wait beneath the red mushroom tree by the river.",
                "This message is relayed by an Imp,",
                "so mind whether the area around you is safe.",
                "--Left by 05--");
        addChallenge("zombie_boss_twins", "Double Revenge",
                "Hopeless player,",
                "",
                "...I do not think anything more needs to be said.",
                "",
                "",
                "--Edgar 181022--");
        addChallenge("column", "Overwhelming",
                "", "Look,", "they stand like pillars", "", "", "");
        addChallenge("dance", "Dance Party",
                "A new hit from the zombie world today,",
                "",
                "\"Brave Zombies\"",
                "",
                "Broadcasting live from the Abyss",
                "");
        addChallenge("gym", "Gym",
                "", "Do you like my muscles,", "Miss Hypno-shroom?", "", "", "");
        addChallenge("mushroom_garden", "Night Mushroom Garden",
                "The zombies invaded your mushroom garden in force during the night,",
                "and now they are going to stew brains with mushrooms.",
                "Do not let them succeed",
                "",
                "",
                "");
        addChallenge("halloween", "Halloween Challenge",
                "Trick or treat!",
                "No candy?",
                "No matter, brains will do just as well",
                "",
                "",
                "");
        addChallenge("origin_flame", "Operation Origin",
                "Today the Abyss Zombie City sends out its elite Red Blade squad",
                "to hunt down Dave and his companions.",
                "If they succeed, the zombies of our city will feast on brains unhindered",
                "At the squad's request, this operation is named \"Operation Origin\"",
                "",
                "");
        addChallenge("sharp_edge", "Shredding Blades",
                "Today the plant positions were attacked by the Red Blade squad,",
                "",
                "and the fighting was brutal,",
                "",
                "but the plant forces fought back, found a breakthrough, and are about to break out.",
                "...");
        addChallenge("steel_and_fire", "Steel and Fire",
                "", "", "The air around begins to grow hot and dry", "", "", "");

        addChallenge("coffin", "Coffin",
                "", "", "What will happen later?", "", "", "------Rest in peace");


    }

    /**
     * 挑战键统一入口：name 为挑战标题，msg1~msg6 为信封内的六行文本。
     */
    private void addChallenge(String id, String name, String msg1, String msg2, String msg3,
                              String msg4, String msg5, String msg6) {
        String prefix = "challenge.pvz." + id;
        add(prefix + ".name", name);
        add(prefix + ".msg1", msg1);
        add(prefix + ".msg2", msg2);
        add(prefix + ".msg3", msg3);
        add(prefix + ".msg4", msg4);
        add(prefix + ".msg5", msg5);
        add(prefix + ".msg6", msg6);
    }
    private void addPotion(String id, String effectName) {
        add("item.minecraft.potion.effect." + id, "Potion of " + effectName);
        add("item.minecraft.splash_potion.effect." + id, "Splash Potion of " + effectName);
        add("item.minecraft.lingering_potion.effect." + id, "Lingering Potion of " + effectName);
        add("item.minecraft.tipped_arrow.effect." + id, "Arrow of " + effectName);
    }
}
