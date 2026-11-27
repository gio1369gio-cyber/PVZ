package com.lying.pvz.common.misc;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class PvZLoot {

	// chest
	public static final ResourceKey<LootTable> DAVE_VILLA_CHEST = getChestLootTable("dave_villa_chest");
	public static final ResourceKey<LootTable> BUCKET_HOUSE_CHEST = getChestLootTable("bucket_house_chest");
	public static final ResourceKey<LootTable> DOLPHIN_HOUSE_CHEST = getChestLootTable("dolphin_house_chest");
	public static final ResourceKey<LootTable> GRAVE_YARD_CHEST = getChestLootTable("grave_yard_chest");
	public static final ResourceKey<LootTable> YETI_HOUSE_CHEST = getChestLootTable("yeti_house_chest");
	public static final ResourceKey<LootTable> SUN_TEMPLE_CHEST = getChestLootTable("sun_temple_chest");
	// entity
	public static final ResourceKey<LootTable> NORMAL_ZOMBIE = getEntityLootTable("normal_zombie");
	public static final ResourceKey<LootTable> FLAG_ZOMBIE = getEntityLootTable("flag_zombie");
	public static final ResourceKey<LootTable> CONEHEAD_ZOMBIE = getEntityLootTable("conehead_zombie");
	public static final ResourceKey<LootTable> BUCKETHEAD_ZOMBIE = getEntityLootTable("buckethead_zombie");
	public static final ResourceKey<LootTable> SCREENDOOR_ZOMBIE = getEntityLootTable("screendoor_zombie");
	public static final ResourceKey<LootTable> FOOTBALL_ZOMBIE = getEntityLootTable("football_zombie");
	public static final ResourceKey<LootTable> GIGA_FOOTBALL_ZOMBIE = getEntityLootTable("giga_football_zombie");
	public static final ResourceKey<LootTable> NOBLE_ZOMBIE = getEntityLootTable("noble_zombie");
	public static final ResourceKey<LootTable> MOURNER_ZOMBIE = getEntityLootTable("mourner_zombie");
	public static final ResourceKey<LootTable> BOBSLE_TEAM = getEntityLootTable("bobsle_team");
	public static final ResourceKey<LootTable> ZOMBIE_DOLPHIN = getEntityLootTable("zombie_dolphin");
	public static final ResourceKey<LootTable> FOODIE_ZOMBIE = getEntityLootTable("foodie_zombie");
	public static final ResourceKey<LootTable> LAVA_ZOMBIE = getEntityLootTable("lava_zombie");
	public static final ResourceKey<LootTable> PUMPKIN_ZOMBIE = getEntityLootTable("pumpkin_zombie");
	public static final ResourceKey<LootTable> TRICK_ZOMBIE = getEntityLootTable("trick_zombie");
	public static final ResourceKey<LootTable> JACK_IN_BOX_ZOMBIE = getEntityLootTable("jack_in_box_zombie");
	public static final ResourceKey<LootTable> BALLOON_ZOMBIE = getEntityLootTable("balloon_zombie");
	public static final ResourceKey<LootTable> DIGGER_ZOMBIE = getEntityLootTable("digger_zombie");
	public static final ResourceKey<LootTable> BUNGEE_ZOMBIE = getEntityLootTable("bungee_zombie");
	public static final ResourceKey<LootTable> LADDER_ZOMBIE = getEntityLootTable("ladder_zombie");
	public static final ResourceKey<LootTable> GARGANTUAR = getEntityLootTable("gargantuar");
	public static final ResourceKey<LootTable> GIGA_GARGANTUAR = getEntityLootTable("giga_gargantuar");
	public static final ResourceKey<LootTable> EDGAR_090505 = getEntityLootTable("edgar_090505");
	public static final ResourceKey<LootTable> EDGAR_090517 = getEntityLootTable("edgar_090517");
	// PvZ2 四足机甲 BOSS（实体注册名 == 战利品表名，自动绑定）
	public static final ResourceKey<LootTable> SPHINX_INATOR = getEntityLootTable("sphinx_inator");
	public static final ResourceKey<LootTable> PLANK_WALKER = getEntityLootTable("plank_walker");
	public static final ResourceKey<LootTable> WAR_WAGON = getEntityLootTable("war_wagon");
	public static final ResourceKey<LootTable> TOMORROW_TRON = getEntityLootTable("tomorrow_tron");
	public static final ResourceKey<LootTable> PEASHOOTER_ZOMBIE = getEntityLootTable("peashooter_zombie");
	public static final ResourceKey<LootTable> WALLNUT_ZOMBIE = getEntityLootTable("wallnut_zombie");
	public static final ResourceKey<LootTable> GATLINGPEA_ZOMBIE = getEntityLootTable("gatlingpea_zombie");
	public static final ResourceKey<LootTable> TALLNUT_ZOMBIE = getEntityLootTable("tallnut_zombie");
	public static final ResourceKey<LootTable> SQUASH_ZOMBIE = getEntityLootTable("squash_zombie");
	public static final ResourceKey<LootTable> JALAPENO_ZOMBIE = getEntityLootTable("jalapeno_zombie");
	public static final ResourceKey<LootTable> RA_ZOMBIE = getEntityLootTable("ra_zombie");
	public static final ResourceKey<LootTable> COFFIN = getEntityLootTable("coffin");

	public static ResourceKey<LootTable> getChestLootTable(String name) {
		return ResourceKey.create(Registries.LOOT_TABLE, StringUtil.id("chests/" + name));
	}

	public static ResourceKey<LootTable> getEntityLootTable(String name) {
		return ResourceKey.create(Registries.LOOT_TABLE, StringUtil.id("entities/" + name));
	}

	public static ResourceKey<LootTable> getFishingLootTable(String name) {
		return ResourceKey.create(Registries.LOOT_TABLE, StringUtil.id("gameplay/fishing/" + name));
	}
}