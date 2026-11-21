package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.EssenceTypes;
import com.lying.pvz.common.impl.RankTypes;
import com.lying.pvz.common.impl.plant.CustomPlants;
import com.lying.pvz.common.impl.plant.MemePlants;
import com.lying.pvz.common.impl.plant.OtherPlants;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.item.armor.PVZArmorMaterials;
import com.lying.pvz.common.level.item.blockitem.ScreenDoorItem;
import com.lying.pvz.common.level.item.bullet.FireCrackerItem;
import com.lying.pvz.common.level.item.card.ImitaterCardItem;
import com.lying.pvz.common.level.item.card.PlantCardItem;
import com.lying.pvz.common.level.item.display.AlmanacItem;
import com.lying.pvz.common.level.item.display.ChallengeEnvelopeItem;
import com.lying.pvz.common.level.item.material.EssenceItem;
import com.lying.pvz.common.level.item.material.TemplateCardItem;
import com.lying.pvz.common.level.item.misc.PvZConsumable;
import com.lying.pvz.common.level.item.misc.PvZToolMaterial;
import com.lying.pvz.common.level.item.spawn.EdgarDollItem;
import com.lying.pvz.common.level.item.spawn.GardenRakeItem;
import com.lying.pvz.common.level.item.spawn.LawnMowerItem;
import com.lying.pvz.common.level.item.tool.*;
import com.lying.pvz.common.level.item.tool.plant.*;
import com.lying.pvz.common.level.item.tool.zombie.*;
import com.lying.pvz.common.level.item.misc.PvZFoods;
import com.lying.pvz.common.level.item.PvZSpawnEggItem;
import com.lying.pvz.common.tag.PvZItemTags;
import com.lying.pvz.data.bootstrap.misc.PvZJukeboxSongs;
import com.lying.pvz.utils.enums.Colors;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class PvZItemRegister {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PvZMod.MOD_ID);

    /* ===== 种子与作物 ===== */
    public static final DeferredItem<Item> PEA = ITEMS.register("pea",
            id -> new BlockItem(PvZBlockRegister.PEA_PLANT.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> SPORE = ITEMS.register("spore",
            id -> new BlockItem(PvZBlockRegister.TOXIC_SHROOM.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> CABBAGE_SEEDS = ITEMS.register("cabbage_seeds",
            id -> new BlockItem(PvZBlockRegister.CABBAGE.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> CORN_SEEDS = ITEMS.register("corn_seeds",
            id -> new BlockItem(PvZBlockRegister.CORN.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<BlockItem> CHILI_SEEDS = ITEMS.register("chili_seeds",
            id -> new BlockItem(PvZBlockRegister.CHILI.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> ZOMBIE_FLAG = ITEMS.registerItem("zombie_flag", ZombieFlagItem::new,
            props -> props.stacksTo(1).durability(100));

    public static final DeferredItem<Item> SCREEN_DOOR = ITEMS.registerItem("screen_door", ScreenDoorItem::new,
            props -> props.stacksTo(1).equippableUnswappable(EquipmentSlot.OFFHAND)
                    .durability(1600).repairable(ItemTags.IRON_TOOL_MATERIALS)
                    .delayedComponent(DataComponents.BLOCKS_ATTACKS,
                            context -> new BlocksAttacks(0.25F, 1.0F,
                                    List.of(new BlocksAttacks.DamageReduction(145.0F, Optional.empty(), 0.0F, 1.0F)),
                                    new BlocksAttacks.ItemDamageFunction(4.0F, 1.0F, 1.0F),
                                    Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                                    Optional.of(SoundEvents.SHIELD_BLOCK),
                                    Optional.of(SoundEvents.SHIELD_BREAK))));

    public static final DeferredItem<Item> BOBSLE_CAR = ITEMS.registerItem("bobsle_car", BobsleCarItem::new,
            props -> props.stacksTo(1));

    public static final DeferredItem<Item> JACK_BOX = ITEMS.registerItem("jack_box", JackBoxItem::new,
            props -> props.stacksTo(1));

    public static final DeferredItem<Item> BALLOON = ITEMS.registerItem("balloon", BalloonItem::new,
            props -> props.stacksTo(1).durability(100));

    public static final DeferredItem<Item> TARGET_ARROW = ITEMS.registerItem("target_arrow", TargetArrowItem::new,
            props -> props);

    public static final DeferredItem<Item> ZOMBIE_DOLL = ITEMS.registerItem("zombie_doll", ZombieDollItem::new,
            props -> props.stacksTo(1));

    public static final DeferredItem<Item> TIME_KEY_TO_DEEP = ITEMS.registerItem("time_key_to_deep", TimeKeyItem::new,
            props -> props.stacksTo(1));

    public static final DeferredItem<Item> GARDEN_COMPASS = ITEMS.registerItem("garden_compass", GardenCompassItem::new,
            props ->  props.stacksTo(1) );

    public static final DeferredItem<Item> CARD_PACK = ITEMS.registerItem("card_pack", CardPackItem::new,
            props -> props.stacksTo(1).component(PvZDataComponents.CARD_CONTENTS.get(), ItemContainerContents.EMPTY));

    public static final DeferredItem<Item> PEA_GUN = ITEMS.registerItem("pea_gun", PeaGunItem::new, props -> props
            .stacksTo(1).durability(1200).enchantable(1).repairable(PvZItemTags.PEN_GUN_REPAIRABLE)
            .component(PvZDataComponents.CARD_CONTENTS.get(), ItemContainerContents.EMPTY));

    public static final DeferredItem<Item> RESOURCE_COLLECTOR = ITEMS.registerItem("resource_collector", ResourceCollectorItem::new,
            props -> props.stacksTo(1).durability(1200));

    public static final DeferredItem<Item> SUN_STORAGE_SAPLING = ITEMS.register("sun_storage_sapling",
            id -> new SunStorageSaplingItem(id,3000));
    public static final DeferredItem<Item> SMALL_SUN_STORAGE_SAPLING = ITEMS.register("sun_storage_sapling2",
            id -> new SunStorageSaplingItem(id,1000));
    public static final DeferredItem<Item> LARGE_SUN_STORAGE_SAPLING = ITEMS.register("sun_storage_sapling3",
            id -> new SunStorageSaplingItem(id,10000));
    public static final DeferredItem<Item> ONCE_SUN_STORAGE_SAPLING = ITEMS.register("sun_storage_sapling4",
            SunStorageSaplingItem::new);

    public static final DeferredItem<Item> HAMMER = ITEMS.register("hammer", id ->
            new Hammer(new Item.Properties().durability(300)
                    .repairable(ToolMaterial.WOOD.repairItems()).setId(ResourceKey.create(Registries.ITEM,id))));

    public static final DeferredItem<Item> ORIGIN_SWORD = ITEMS.registerItem("origin_sword", Item::new,
            props -> props.sword(PvZToolMaterial.ORIGIN,3F,-2.4F));

    public static final DeferredItem<Item> ORIGIN_SHOVEL = ITEMS.registerItem("origin_shovel", Item::new,
            props -> props.shovel(PvZToolMaterial.ORIGIN,1.5F,-3.0F));

    public static final DeferredItem<Item> ORIGIN_PICKAXE = ITEMS.registerItem("origin_pickaxe",Item::new,
            props -> props.pickaxe(PvZToolMaterial.ORIGIN, 1.0F, -2.8F));

    public static final DeferredItem<Item> ORIGIN_AXE = ITEMS.registerItem("origin_axe",Item::new,
            (props -> props.axe(PvZToolMaterial.ORIGIN, 6.0F, -3.1F)));

    public static final DeferredItem<Item> ORIGIN_HOE = ITEMS.registerItem("origin_hoe",Item::new,
            props -> props.hoe(PvZToolMaterial.ORIGIN, -2.0F, -1.0F));

    public static final DeferredItem<Item> WARNING_SIGN = ITEMS.registerItem("warning_sign",Item::new,
            props -> props.sword(ToolMaterial.IRON, 6, -2.4F));

    public static final DeferredItem<Item> POLE = ITEMS.registerItem("pole",Item::new,
            props -> props.axe(ToolMaterial.IRON, 8.0F, -3.0F));


    public static final DeferredItem<Item> NUT = ITEMS.registerItem("nut",Item::new);

    public static final DeferredItem<Item> SNOW_PEA = ITEMS.registerItem("snow_pea", Item::new);

    public static final DeferredItem<Item> FLAME_PEA = ITEMS.registerItem("flame_pea", Item::new);

    public static final DeferredItem<Item> SMALL_MEAT = ITEMS.registerItem("small_meat", Item::new);

    public static final DeferredItem<Item> FROZEN_MELON_SLICE = ITEMS.registerItem("frozen_melon_slice", Item::new);

    public static final DeferredItem<Item> TIME_SOURCE = ITEMS.registerItem("time_source", Item::new);
    
    /* ===== 锭 ===== */
    public static final DeferredItem<Item> ORIGIN_INGOT = ITEMS.registerItem("origin_ingot", Item::new);
    public static final DeferredItem<Item> AMETHYST_INGOT = ITEMS.registerItem("amethyst_ingot", Item::new);

    /* ===== 精华 ===== */
    public static final DeferredItem<Item> ORIGIN_ESSENCE = ITEMS.register("origin_essence", id-> new EssenceItem(id,EssenceTypes.ORIGIN));
    public static final DeferredItem<Item> APPEASE_ESSENCE = ITEMS.register("appease_essence", id -> new EssenceItem(id,EssenceTypes.APPEASE));
    public static final DeferredItem<Item> LIGHT_ESSENCE = ITEMS.register("light_essence", id -> new EssenceItem(id,EssenceTypes.LIGHT));
    public static final DeferredItem<Item> EXPLOSION_ESSENCE = ITEMS.register("explosion_essence", id -> new EssenceItem(id,EssenceTypes.EXPLOSION));
    public static final DeferredItem<Item> DEFENCE_ESSENCE = ITEMS.register("defence_essence", id -> new EssenceItem(id,EssenceTypes.DEFENCE));
    public static final DeferredItem<Item> ICE_ESSENCE = ITEMS.register("ice_essence", id -> new EssenceItem(id,EssenceTypes.ICE));
    public static final DeferredItem<Item> ENFORCE_ESSENCE = ITEMS.register("enforce_essence", id -> new EssenceItem(id,EssenceTypes.ENFORCE));
    public static final DeferredItem<Item> TOXIC_ESSENCE = ITEMS.register("toxic_essence", id -> new EssenceItem(id,EssenceTypes.TOXIC));
    public static final DeferredItem<Item> ASSIST_ESSENCE = ITEMS.register("assist_essence", id -> new EssenceItem(id,EssenceTypes.ASSIST));
    public static final DeferredItem<Item> MAGIC_ESSENCE = ITEMS.register("magic_essence", id -> new EssenceItem(id,EssenceTypes.MAGIC));
    public static final DeferredItem<Item> FLAME_ESSENCE = ITEMS.register("flame_essence", id -> new EssenceItem(id,EssenceTypes.FLAME));
    public static final DeferredItem<Item> SPEAR_ESSENCE = ITEMS.register("spear_essence", id -> new EssenceItem(id,EssenceTypes.SPEAR));
    public static final DeferredItem<Item> ARMA_ESSENCE = ITEMS.register("arma_essence", id -> new EssenceItem(id,EssenceTypes.ARMA));
    public static final DeferredItem<Item> ELECTRIC_ESSENCE = ITEMS.register("electric_essence", id -> new EssenceItem(id,EssenceTypes.ELECTRIC));
    public static final DeferredItem<Item> SHADOW_ESSENCE = ITEMS.register("shadow_essence", id -> new EssenceItem(id,EssenceTypes.SHADOW));

    
    /* ===== 卡片模板 ===== */
    public static final DeferredItem<Item> GRAY_CARD = ITEMS.register("gray_card", id -> new TemplateCardItem(id,RankTypes.GRAY));
    public static final DeferredItem<Item> WHITE_CARD = ITEMS.register("white_card", id -> new TemplateCardItem(id,RankTypes.WHITE));
    public static final DeferredItem<Item> GREEN_CARD = ITEMS.register("green_card", id -> new TemplateCardItem(id,RankTypes.GREEN));
    public static final DeferredItem<Item> BLUE_CARD = ITEMS.register("blue_card", id -> new TemplateCardItem(id,RankTypes.BLUE));
    public static final DeferredItem<Item> PURPLE_CARD = ITEMS.register("purple_card", id -> new TemplateCardItem(id,RankTypes.PURPLE));
    public static final DeferredItem<Item> GOLD_CARD = ITEMS.register("gold_card", id -> new TemplateCardItem(id,RankTypes.GOLD));
    public static final DeferredItem<Item> RED_CARD = ITEMS.register("red_card", id -> new TemplateCardItem(id,RankTypes.RED));
    public static final DeferredItem<Item> BLACK_CARD = ITEMS.register("black_card", id -> new TemplateCardItem(id,RankTypes.BLACK));
    public static final DeferredItem<Item> MEGA_CARD = ITEMS.register("mega_card", id -> new TemplateCardItem(id,RankTypes.MEGA));

   
    /* ===== 刷怪蛋 ===== */
    public static final DeferredHolder<Item, PvZSpawnEggItem> SUN_SPAWN_EGG = registerSpawnEgg("sun",PvZEntityRegister.SUN, Colors.YELLOW, Colors.WHITE);
    public static final DeferredHolder<Item, PvZSpawnEggItem> COIN_SPAWN_EGG = registerSpawnEgg("coin",PvZEntityRegister.COIN, Colors.ORANGE, Colors.GOLD);
    public static final DeferredHolder<Item, PvZSpawnEggItem> JEWEL_SPAWN_EGG = registerSpawnEgg("jewel",PvZEntityRegister.JEWEL, Colors.LITTLE_AQUA, Colors.BLUE);
    public static final DeferredHolder<Item, PvZSpawnEggItem> ENERGY_SPAWN_EGG = registerSpawnEgg("energy",PvZEntityRegister.ENERGY, Colors.GREEN, Colors.DARK_GREEN);
    public static final DeferredHolder<Item, PvZSpawnEggItem> FOODIE_ZOMBIE_SPAWN_EGG = registerSpawnEgg("foodie_zombie",PvZEntityRegister.FOODIE_ZOMBIE, Colors.ZOMBIE_SKIN, Colors.WHITE);
    public static final DeferredHolder<Item, PvZSpawnEggItem> CRAZY_DAVE_SPAWN_EGG = registerSpawnEgg("crazy_dave",PvZEntityRegister.CRAZY_DAVE, Colors.BROWN, Colors.SILVER);
    public static final DeferredHolder<Item, PvZSpawnEggItem> PANNEY_SPAWN_EGG = registerSpawnEgg("panney",PvZEntityRegister.PANNEY, Colors.BLUE, Colors.RED);
    public static final DeferredHolder<Item, PvZSpawnEggItem> SUN_DAVE_SPAWN_EGG = registerSpawnEgg("sun_dave",PvZEntityRegister.SUN_DAVE, Colors.BROWN, Colors.SILVER);
    public static final DeferredHolder<Item, PvZSpawnEggItem> NORMAL_ZOMBIE_SPAWN_EGG = registerSpawnEgg("normal_zombie",PvZEntityRegister.NORMAL_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> FLAG_ZOMBIE_SPAWN_EGG = registerSpawnEgg("flag_zombie",PvZEntityRegister.FLAG_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> CONEHEAD_ZOMBIE_SPAWN_EGG = registerSpawnEgg("conehead_zombie",PvZEntityRegister.CONEHEAD_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> POLE_ZOMBIE_SPAWN_EGG = registerSpawnEgg("pole_zombie",PvZEntityRegister.POLE_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> BUCKETHEAD_ZOMBIE_SPAWN_EGG = registerSpawnEgg("buckethead_zombie",PvZEntityRegister.BUCKETHEAD_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> NEWSPAPER_ZOMBIE_SPAWN_EGG = registerSpawnEgg("newspaper_zombie",PvZEntityRegister.NEWSPAPER_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> TOMB_STONE_SPAWN_EGG = registerSpawnEgg("tomb_stone",PvZEntityRegister.TOMB_STONE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> SCREENDOOR_ZOMBIE_SPAWN_EGG = registerSpawnEgg("screendoor_zombie",PvZEntityRegister.SCREENDOOR_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> FOOTBALL_ZOMBIE_SPAWN_EGG = registerSpawnEgg("football_zombie",PvZEntityRegister.FOOTBALL_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> DANCING_ZOMBIE_SPAWN_EGG = registerSpawnEgg("dancing_zombie",PvZEntityRegister.DANCING_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> BACKUP_DANCER_SPAWN_EGG = registerSpawnEgg("backup_dancer",PvZEntityRegister.BACKUP_DANCER, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> GIGA_FOOTBALL_ZOMBIE_SPAWN_EGG = registerSpawnEgg("giga_football_zombie",PvZEntityRegister.GIGA_FOOTBALL_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> OLD_ZOMBIE_SPAWN_EGG = registerSpawnEgg("old_zombie",PvZEntityRegister.OLD_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> SUNDAY_EDITION_ZOMBIE_SPAWN_EGG = registerSpawnEgg("sunday_edition_zombie",PvZEntityRegister.SUNDAY_EDITION_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> SNORKEL_ZOMBIE_SPAWN_EGG = registerSpawnEgg("snorkel_zombie",PvZEntityRegister.SNORKEL_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> ZOMBONI_SPAWN_EGG = registerSpawnEgg("zomboni",PvZEntityRegister.ZOMBONI, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> BOBSLE_TEAM_SPAWN_EGG = registerSpawnEgg("bobsle_team",PvZEntityRegister.BOBSLE_TEAM, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> BOBSLE_ZOMBIE_SPAWN_EGG = registerSpawnEgg("bobsle_zombie",PvZEntityRegister.BOBSLE_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> ZOMBIE_DOLPHIN_SPAWN_EGG = registerSpawnEgg("zombie_dolphin",PvZEntityRegister.ZOMBIE_DOLPHIN, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> DOLPHIN_RIDER_SPAWN_EGG = registerSpawnEgg("dolphin_rider",PvZEntityRegister.DOLPHIN_RIDER, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> DOLPHIN_RIDER_ZOMBIE_SPAWN_EGG = registerSpawnEgg("dolphin_rider_zombie",PvZEntityRegister.DOLPHIN_RIDER_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> LAVA_ZOMBIE_SPAWN_EGG = registerSpawnEgg("lava_zombie",PvZEntityRegister.LAVA_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> JACK_IN_BOX_ZOMBIE_SPAWN_EGG = registerSpawnEgg("jack_in_box_zombie",PvZEntityRegister.JACK_IN_BOX_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> BALLOON_ZOMBIE_SPAWN_EGG = registerSpawnEgg("balloon_zombie",PvZEntityRegister.BALLOON_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> DIGGER_ZOMBIE_SPAWN_EGG = registerSpawnEgg("digger_zombie",PvZEntityRegister.DIGGER_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> POGO_ZOMBIE_SPAWN_EGG = registerSpawnEgg("pogo_zombie",PvZEntityRegister.POGO_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> YETI_ZOMBIE_SPAWN_EGG = registerSpawnEgg("yeti_zombie",PvZEntityRegister.YETI_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> BUNGEE_ZOMBIE_SPAWN_EGG = registerSpawnEgg("bungee_zombie",PvZEntityRegister.BUNGEE_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> LADDER_ZOMBIE_SPAWN_EGG = registerSpawnEgg("ladder_zombie",PvZEntityRegister.LADDER_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> CATAPULT_ZOMBIE_SPAWN_EGG = registerSpawnEgg("catapult_zombie",PvZEntityRegister.CATAPULT_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> GARGANTUAR_SPAWN_EGG = registerSpawnEgg("gargantuar",PvZEntityRegister.GARGANTUAR, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> IMP_SPAWN_EGG = registerSpawnEgg("imp",PvZEntityRegister.IMP, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> GIGA_GARGANTUAR_SPAWN_EGG = registerSpawnEgg("giga_gargantuar",PvZEntityRegister.GIGA_GARGANTUAR, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> EDGAR_090505_SPAWN_EGG = registerSpawnEgg("edgar_090505",PvZEntityRegister.EDGAR_090505, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> EDGAR_090517_SPAWN_EGG = registerSpawnEgg("edgar_090517",PvZEntityRegister.EDGAR_090517, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> PEASHOOTER_ZOMBIE_SPAWN_EGG = registerSpawnEgg("peashooter_zombie",PvZEntityRegister.PEASHOOTER_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> WALLNUT_ZOMBIE_SPAWN_EGG = registerSpawnEgg("wallnut_zombie",PvZEntityRegister.WALLNUT_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> GATLINGPEA_ZOMBIE_SPAWN_EGG = registerSpawnEgg("gatlingpea_zombie",PvZEntityRegister.GATLINGPEA_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> TALLNUT_ZOMBIE_SPAWN_EGG = registerSpawnEgg("tallnut_zombie",PvZEntityRegister.TALLNUT_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> SQUASH_ZOMBIE_SPAWN_EGG = registerSpawnEgg("squash_zombie",PvZEntityRegister.SQUASH_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> JALAPENO_ZOMBIE_SPAWN_EGG = registerSpawnEgg("jalapeno_zombie",PvZEntityRegister.JALAPENO_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> PUMPKIN_ZOMBIE_SPAWN_EGG = registerSpawnEgg("pumpkin_zombie",PvZEntityRegister.PUMPKIN_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> TRICK_ZOMBIE_SPAWN_EGG = registerSpawnEgg("trick_zombie",PvZEntityRegister.TRICK_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> COFFIN_SPAWN_EGG = registerSpawnEgg("coffin",PvZEntityRegister.COFFIN, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> MOURNER_ZOMBIE_SPAWN_EGG = registerSpawnEgg("mourner_zombie",PvZEntityRegister.MOURNER_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> NOBLE_ZOMBIE_SPAWN_EGG = registerSpawnEgg("noble_zombie",PvZEntityRegister.NOBLE_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> RA_ZOMBIE_SPAWN_EGG = registerSpawnEgg("ra_zombie",PvZEntityRegister.RA_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> GIGA_TOMB_STONE_SPAWN_EGG = registerSpawnEgg("giga_tomb_stone",PvZEntityRegister.GIGA_TOMB_STONE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> EDGAR_090547_SPAWN_EGG = registerSpawnEgg("edgar_090547_spawn_egg", PvZEntityRegister.EDGAR_090547, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> FLOWER_POT_ZOMBIE_SPAWN_EGG = registerSpawnEgg("flower_pot_zombie_spawn_egg", PvZEntityRegister.FLOWER_POT_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> AIRBORNE_ZOMBIE_SPAWN_EGG = registerSpawnEgg("airborne_zombie_spawn_egg", PvZEntityRegister.AIRBORNE_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> MC_ZOMBIE_SPAWN_EGG = registerSpawnEgg("mc_zombie_spawn_egg", PvZEntityRegister.MC_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> STEEL_PUMPKIN_ZOMBIE_SPAWN_EGG = registerSpawnEgg("steel_pumpkin_zombie_spawn_egg", PvZEntityRegister.STEEL_PUMPKIN_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> TARGET_ARROW_ZOMBIE_SPAWN_EGG = registerSpawnEgg("target_arrow_zombie_spawn_egg", PvZEntityRegister.TARGET_ARROW_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> RED_EDGE_ZOMBIE_SPAWN_EGG = registerSpawnEgg("red_edge_zombie_spawn_egg", PvZEntityRegister.RED_EDGE_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> MJ_ZOMBIE_SPAWN_EGG = registerSpawnEgg("mj_zombie_spawn_egg", PvZEntityRegister.MJ_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> DANCER_BACKUP_ZOMBIE_SPAWN_EGG = registerSpawnEgg("dancer_backup_zombie_spawn_egg", PvZEntityRegister.DANCER_BACKUP_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> RED_FLOWER_ZOMBIE_SPAWN_EGG = registerSpawnEgg("sun_flower_zombie_spawn_egg", PvZEntityRegister.SUN_FLOWER_ZOMBIE, Colors.ZOMBIE_COLOR);
    public static final DeferredHolder<Item, PvZSpawnEggItem> CHOMPER_ZOMBIE_SPAWN_EGG = registerSpawnEgg("chomper_zombie_spawn_egg", PvZEntityRegister.CHOMPER_ZOMBIE, Colors.ZOMBIE_COLOR);


    /* ===== 食物===== */
    public static final DeferredItem<Item> FAKE_BRAIN = ITEMS.registerItem("fake_brain",
            Item::new, prop -> prop.food(PvZFoods.FAKE_BRAIN));
    public static final DeferredItem<Item> REAL_BRAIN = ITEMS.registerItem("real_brain",
            Item::new,prop -> prop.food(PvZFoods.REAL_BRAIN, PvZConsumable.REAL_BRAIN));
    public static final DeferredItem<Item> CANDY = ITEMS.registerItem("candy",
            Item::new,prop -> prop.food(PvZFoods.CANDY));
    public static final DeferredItem<Item> CHOCOLATE = ITEMS.registerItem("chocolate",
            Item::new,prop -> prop.food(PvZFoods.CHOCOLATE));
    public static final DeferredItem<Item> PEA_SOUP = ITEMS.registerItem("pea_soup",
            Item::new,prop -> prop.food(PvZFoods.PEA_SOUP).stacksTo(1));
    public static final DeferredItem<Item> COOKED_BRAIN = ITEMS.registerItem("cooked_brain",
            Item::new,prop -> prop.food(PvZFoods.COOKED_BRAIN));
    public static final DeferredItem<Item> CABBAGE = ITEMS.registerItem("cabbage",
            Item::new,prop -> prop.food(PvZFoods.CABBAGE));
    public static final DeferredItem<Item> ICE_CABBAGE = ITEMS.registerItem("ice_cabbage",
            Item::new,prop -> prop.food(PvZFoods.ICE_CABBAGE,PvZConsumable.ICE_CABBAGE));
    public static final DeferredItem<Item> CORN = ITEMS.registerItem("corn",
            Item::new,prop -> prop.food(PvZFoods.CORN));
    public static final DeferredItem<Item> POP_CORN = ITEMS.registerItem("pop_corn",
            Item::new,prop -> prop.food(PvZFoods.POP_CORN));
    public static final DeferredItem<Item> TACOS = ITEMS.registerItem("tacos",
            Item::new,prop -> prop.food(PvZFoods.TACOS,PvZConsumable.TACOS));
    public static final DeferredItem<Item> PEPPER = ITEMS.registerItem("pepper", Item::new,
            prop -> prop.food(PvZFoods.PEPPER,PvZConsumable.PEPPER));
    public static final DeferredItem<Item> GARLIC = ITEMS.register("garlic", id ->   new BlockItem(
            PvZBlockRegister.GARLIC.get(), new Item.Properties().food(PvZFoods.GARLIC,PvZConsumable.GARLIC)
            .setId(ResourceKey.create(Registries.ITEM,id))));

    public static final DeferredItem<Item> BOWLING_GLOVE = ITEMS.registerItem("bowling_glove",
            BowlingGloveItem::new, props -> props.stacksTo(1).rarity(Rarity.UNCOMMON).durability(666));

    public static final DeferredItem<Item> FIRE_CRACKER = ITEMS.registerItem("fire_cracker",
            FireCrackerItem::new,prop -> prop);

    public static final DeferredItem<Item> EDGAR_DOLL = ITEMS.registerItem("edgar_doll",
            EdgarDollItem::new, prop -> prop.stacksTo(1));

    public static final DeferredItem<Item> GARDEN_RAKE = ITEMS.registerItem("garden_rake",
            GardenRakeItem::new,prop -> prop);

    public static final DeferredItem<Item> LAWN_MOWER = ITEMS.registerItem("lawn_mower",
            LawnMowerItem::new,props -> props);

    public static final DeferredItem<Item> CAR_KEY = ITEMS.registerItem("car_key",
            Item::new,props -> props);

    public static final DeferredItem<Item> ALMANAC = ITEMS.registerItem("almanac",
            AlmanacItem::new,props -> props.stacksTo(1));

    /* ===== 挑战 ===== */
    public static final DeferredItem<Item> CHALLENGE_ENVELOPE = ITEMS.registerItem("challenge_envelope",
            ChallengeEnvelopeItem::new, props -> props.stacksTo(1));

    /* ===== 护甲===== */
    public static final DeferredItem<Item> CONE_HEAD = ITEMS.registerItem("cone_head", Item::new,
            prop -> prop.humanoidArmor(ArmorMaterials.LEATHER, ArmorType.HELMET)
                    .durability(ArmorType.HELMET.getDurability(1)));

    public static final DeferredItem<Item> BUCKET_HEAD = ITEMS.registerItem("bucket_head",Item::new,
           prop -> prop.humanoidArmor(ArmorMaterials.IRON, ArmorType.HELMET)
                   .durability(ArmorType.HELMET.getDurability(15)));


    public static final DeferredItem<Item> FOOTBALL_HELMET = ITEMS.registerItem("football_helmet",Item::new,
            prop -> prop.humanoidArmor(PVZArmorMaterials.FOOTBALL, ArmorType.HELMET)
                    .durability(ArmorType.HELMET.getDurability(50)));

    public static final DeferredItem<Item> FOOTBALL_CHESTPLATE = ITEMS.registerItem("football_chestplate",Item::new,
            prop -> prop.humanoidArmor(PVZArmorMaterials.FOOTBALL, ArmorType.CHESTPLATE)
                    .durability(ArmorType.CHESTPLATE.getDurability(50)));

    public static final DeferredItem<Item> FOOTBALL_LEGGINGS = ITEMS.registerItem("football_leggings",Item::new,
            prop -> prop.humanoidArmor(PVZArmorMaterials.FOOTBALL, ArmorType.LEGGINGS)
                    .durability(ArmorType.LEGGINGS.getDurability(50)));

    public static final DeferredItem<Item> FOOTBALL_BOOTS = ITEMS.registerItem("football_boots", Item::new,
            prop -> prop.humanoidArmor(PVZArmorMaterials.FOOTBALL, ArmorType.BOOTS)
                    .durability(ArmorType.BOOTS.getDurability(50)));

    public static final DeferredItem<Item> GIGA_HELMET = ITEMS.registerItem("giga_helmet",Item::new,
            prop -> prop.humanoidArmor(PVZArmorMaterials.GIGA, ArmorType.HELMET)
                    .durability(ArmorType.HELMET.getDurability(80)));

    public static final DeferredItem<Item> GIGA_CHESTPLATE = ITEMS.registerItem("giga_chestplate",Item::new,
            prop -> prop.humanoidArmor(PVZArmorMaterials.GIGA, ArmorType.CHESTPLATE)
                    .durability(ArmorType.CHESTPLATE.getDurability(80)));

    public static final DeferredItem<Item> GIGA_LEGGINGS = ITEMS.registerItem("giga_leggings",Item::new,
            prop -> prop.humanoidArmor(PVZArmorMaterials.GIGA, ArmorType.LEGGINGS)
                    .durability(ArmorType.LEGGINGS.getDurability(80)));

    public static final DeferredItem<Item> GIGA_BOOTS = ITEMS.registerItem("giga_boots", Item::new,
            prop -> prop.humanoidArmor(PVZArmorMaterials.GIGA, ArmorType.BOOTS)
                    .durability(ArmorType.BOOTS.getDurability(80)));


    /* ===== 音乐唱片===== */
    public static final DeferredItem<Item> ZOMBIE_ON_YOUR_LAWN = ITEMS.register("zombie_on_your_lawn",
            id -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, id))
                    .jukeboxPlayable(PvZJukeboxSongs.ZOMBIE_ON_YOUR_LAWN_SONG)));

    public static final DeferredItem<Item> ZEN_GARDEN = ITEMS.register("zen_garden",
            id -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, id))
                    .jukeboxPlayable(PvZJukeboxSongs.ZEN_GARDEN_SONG)));

    public static final DeferredItem<Item> POOL_DISC = ITEMS.register("pool_disc",
            id -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, id))
                    .jukeboxPlayable(PvZJukeboxSongs.POOL_DISC)));

    public static final DeferredItem<Item> BOWLING_MUSIC_DISC = ITEMS.register("bowling_music_disc",
            id -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, id))
                    .jukeboxPlayable(PvZJukeboxSongs.BOWLING_MUSIC_DISC)));

    /* ===== 植物卡片 ===== */
    public static final DeferredHolder<Item, PlantCardItem> PEA_SHOOTER_CARD = registerCard(PVZPlants.PEA_SHOOTER, false);
    public static final DeferredHolder<Item, PlantCardItem> PEA_SHOOTER_ENJOY_CARD = registerCard(PVZPlants.PEA_SHOOTER, true);
    public static final DeferredHolder<Item, PlantCardItem> SUN_FLOWER_CARD = registerCard(PVZPlants.SUN_FLOWER, false);
    public static final DeferredHolder<Item, PlantCardItem> SUN_FLOWER_ENJOY_CARD = registerCard(PVZPlants.SUN_FLOWER, true);
    public static final DeferredHolder<Item, PlantCardItem> CHERRY_BOMB_CARD = registerCard(PVZPlants.CHERRY_BOMB, false);
    public static final DeferredHolder<Item, PlantCardItem> CHERRY_BOMB_ENJOY_CARD = registerCard(PVZPlants.CHERRY_BOMB, true);
    public static final DeferredHolder<Item, PlantCardItem> WALL_NUT_CARD = registerCard(PVZPlants.WALL_NUT, false);
    public static final DeferredHolder<Item, PlantCardItem> WALL_NUT_ENJOY_CARD = registerCard(PVZPlants.WALL_NUT, true);
    public static final DeferredHolder<Item, PlantCardItem> POTATO_MINE_CARD = registerCard(PVZPlants.POTATO_MINE, false);
    public static final DeferredHolder<Item, PlantCardItem> POTATO_MINE_ENJOY_CARD = registerCard(PVZPlants.POTATO_MINE, true);
    public static final DeferredHolder<Item, PlantCardItem> SNOW_PEA_CARD = registerCard(PVZPlants.SNOW_PEA, false);
    public static final DeferredHolder<Item, PlantCardItem> SNOW_PEA_ENJOY_CARD = registerCard(PVZPlants.SNOW_PEA, true);
    public static final DeferredHolder<Item, PlantCardItem> CHOMPER_CARD = registerCard(PVZPlants.CHOMPER, false);
    public static final DeferredHolder<Item, PlantCardItem> CHOMPER_ENJOY_CARD = registerCard(PVZPlants.CHOMPER, true);
    public static final DeferredHolder<Item, PlantCardItem> REPEATER_CARD = registerCard(PVZPlants.REPEATER, false);
    public static final DeferredHolder<Item, PlantCardItem> REPEATER_ENJOY_CARD = registerCard(PVZPlants.REPEATER, true);
    public static final DeferredHolder<Item, PlantCardItem> PUFF_SHROOM_CARD = registerCard(PVZPlants.PUFF_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> PUFF_SHROOM_ENJOY_CARD = registerCard(PVZPlants.PUFF_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> SUN_SHROOM_CARD = registerCard(PVZPlants.SUN_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> SUN_SHROOM_ENJOY_CARD = registerCard(PVZPlants.SUN_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> FUME_SHROOM_CARD = registerCard(PVZPlants.FUME_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> FUME_SHROOM_ENJOY_CARD = registerCard(PVZPlants.FUME_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> GRAVE_BUSTER_CARD = registerCard(PVZPlants.GRAVE_BUSTER, false);
    public static final DeferredHolder<Item, PlantCardItem> GRAVE_BUSTER_ENJOY_CARD = registerCard(PVZPlants.GRAVE_BUSTER, true);
    public static final DeferredHolder<Item, PlantCardItem> HYPNO_SHROOM_CARD = registerCard(PVZPlants.HYPNO_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> HYPNO_SHROOM_ENJOY_CARD = registerCard(PVZPlants.HYPNO_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> SCAREDY_SHROOM_CARD = registerCard(PVZPlants.SCAREDY_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> SCAREDY_SHROOM_ENJOY_CARD = registerCard(PVZPlants.SCAREDY_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> ICE_SHROOM_CARD = registerCard(PVZPlants.ICE_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> ICE_SHROOM_ENJOY_CARD = registerCard(PVZPlants.ICE_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> DOOM_SHROOM_CARD = registerCard(PVZPlants.DOOM_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> DOOM_SHROOM_ENJOY_CARD = registerCard(PVZPlants.DOOM_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> LILY_PAD_CARD = registerCard(PVZPlants.LILY_PAD, false);
    public static final DeferredHolder<Item, PlantCardItem> LILY_PAD_ENJOY_CARD = registerCard(PVZPlants.LILY_PAD, true);
    public static final DeferredHolder<Item, PlantCardItem> SQUASH_CARD = registerCard(PVZPlants.SQUASH, false);
    public static final DeferredHolder<Item, PlantCardItem> SQUASH_ENJOY_CARD = registerCard(PVZPlants.SQUASH, true);
    public static final DeferredHolder<Item, PlantCardItem> THREE_PEATER_CARD = registerCard(PVZPlants.THREE_PEATER, false);
    public static final DeferredHolder<Item, PlantCardItem> THREE_PEATER_ENJOY_CARD = registerCard(PVZPlants.THREE_PEATER, true);
    public static final DeferredHolder<Item, PlantCardItem> TANGLE_KELP_CARD = registerCard(PVZPlants.TANGLE_KELP, false);
    public static final DeferredHolder<Item, PlantCardItem> TANGLE_KELP_ENJOY_CARD = registerCard(PVZPlants.TANGLE_KELP, true);
    public static final DeferredHolder<Item, PlantCardItem> JALAPENO_CARD = registerCard(PVZPlants.JALAPENO, false);
    public static final DeferredHolder<Item, PlantCardItem> JALAPENO_ENJOY_CARD = registerCard(PVZPlants.JALAPENO, true);
    public static final DeferredHolder<Item, PlantCardItem> SPIKE_WEED_CARD = registerCard(PVZPlants.SPIKE_WEED, false);
    public static final DeferredHolder<Item, PlantCardItem> SPIKE_WEED_ENJOY_CARD = registerCard(PVZPlants.SPIKE_WEED, true);
    public static final DeferredHolder<Item, PlantCardItem> TORCH_WOOD_CARD = registerCard(PVZPlants.TORCH_WOOD, false);
    public static final DeferredHolder<Item, PlantCardItem> TORCH_WOOD_ENJOY_CARD = registerCard(PVZPlants.TORCH_WOOD, true);
    public static final DeferredHolder<Item, PlantCardItem> TALL_NUT_CARD = registerCard(PVZPlants.TALL_NUT, false);
    public static final DeferredHolder<Item, PlantCardItem> TALL_NUT_ENJOY_CARD = registerCard(PVZPlants.TALL_NUT, true);
    public static final DeferredHolder<Item, PlantCardItem> SEA_SHROOM_CARD = registerCard(PVZPlants.SEA_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> SEA_SHROOM_ENJOY_CARD = registerCard(PVZPlants.SEA_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> PLANTERN_CARD = registerCard(PVZPlants.PLANTERN, false);
    public static final DeferredHolder<Item, PlantCardItem> PLANTERN_ENJOY_CARD = registerCard(PVZPlants.PLANTERN, true);
    public static final DeferredHolder<Item, PlantCardItem> CACTUS_CARD = registerCard(PVZPlants.CACTUS, false);
    public static final DeferredHolder<Item, PlantCardItem> CACTUS_ENJOY_CARD = registerCard(PVZPlants.CACTUS, true);
    public static final DeferredHolder<Item, PlantCardItem> BLOVER_CARD = registerCard(PVZPlants.BLOVER, false);
    public static final DeferredHolder<Item, PlantCardItem> BLOVER_ENJOY_CARD = registerCard(PVZPlants.BLOVER, true);
    public static final DeferredHolder<Item, PlantCardItem> SPLIT_PEA_CARD = registerCard(PVZPlants.SPLIT_PEA, false);
    public static final DeferredHolder<Item, PlantCardItem> SPLIT_PEA_ENJOY_CARD = registerCard(PVZPlants.SPLIT_PEA, true);
    public static final DeferredHolder<Item, PlantCardItem> STAR_FRUIT_CARD = registerCard(PVZPlants.STAR_FRUIT, false);
    public static final DeferredHolder<Item, PlantCardItem> STAR_FRUIT_ENJOY_CARD = registerCard(PVZPlants.STAR_FRUIT, true);
    public static final DeferredHolder<Item, PlantCardItem> PUMPKIN_CARD = registerCard(PVZPlants.PUMPKIN, false);
    public static final DeferredHolder<Item, PlantCardItem> PUMPKIN_ENJOY_CARD = registerCard(PVZPlants.PUMPKIN, true);
    public static final DeferredHolder<Item, PlantCardItem> MAGNET_SHROOM_CARD = registerCard(PVZPlants.MAGNET_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> MAGNET_SHROOM_ENJOY_CARD = registerCard(PVZPlants.MAGNET_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> CABBAGE_PULT_CARD = registerCard(PVZPlants.CABBAGE_PULT, false);
    public static final DeferredHolder<Item, PlantCardItem> CABBAGE_PULT_ENJOY_CARD = registerCard(PVZPlants.CABBAGE_PULT, true);
    public static final DeferredHolder<Item, PlantCardItem> FLOWER_POT_CARD = registerCard(PVZPlants.FLOWER_POT, false);
    public static final DeferredHolder<Item, PlantCardItem> FLOWER_POT_ENJOY_CARD = registerCard(PVZPlants.FLOWER_POT, true);
    public static final DeferredHolder<Item, PlantCardItem> KERNEL_PULT_CARD = registerCard(PVZPlants.KERNEL_PULT, false);
    public static final DeferredHolder<Item, PlantCardItem> KERNEL_PULT_ENJOY_CARD = registerCard(PVZPlants.KERNEL_PULT, true);
    public static final DeferredHolder<Item, PlantCardItem> COFFEE_BEAN_CARD = registerCard(PVZPlants.COFFEE_BEAN, false);
    public static final DeferredHolder<Item, PlantCardItem> COFFEE_BEAN_ENJOY_CARD = registerCard(PVZPlants.COFFEE_BEAN, true);
    public static final DeferredHolder<Item, PlantCardItem> GARLIC_CARD = registerCard(PVZPlants.GARLIC, false);
    public static final DeferredHolder<Item, PlantCardItem> GARLIC_ENJOY_CARD = registerCard(PVZPlants.GARLIC, true);
    public static final DeferredHolder<Item, PlantCardItem> UMBRELLA_LEAF_CARD = registerCard(PVZPlants.UMBRELLA_LEAF, false);
    public static final DeferredHolder<Item, PlantCardItem> UMBRELLA_LEAF_ENJOY_CARD = registerCard(PVZPlants.UMBRELLA_LEAF, true);
    public static final DeferredHolder<Item, PlantCardItem> MARIGOLD_CARD = registerCard(PVZPlants.MARIGOLD, false);
    public static final DeferredHolder<Item, PlantCardItem> MARIGOLD_ENJOY_CARD = registerCard(PVZPlants.MARIGOLD, true);
    public static final DeferredHolder<Item, PlantCardItem> MELON_PULT_CARD = registerCard(PVZPlants.MELON_PULT, false);
    public static final DeferredHolder<Item, PlantCardItem> MELON_PULT_ENJOY_CARD = registerCard(PVZPlants.MELON_PULT, true);
    public static final DeferredHolder<Item, PlantCardItem> GATLING_PEA_CARD = registerCard(PVZPlants.GATLING_PEA, false);
    public static final DeferredHolder<Item, PlantCardItem> GATLING_PEA_ENJOY_CARD = registerCard(PVZPlants.GATLING_PEA, true);
    public static final DeferredHolder<Item, PlantCardItem> TWIN_SUNFLOWER_CARD = registerCard(PVZPlants.TWIN_SUNFLOWER, false);
    public static final DeferredHolder<Item, PlantCardItem> TWIN_SUNFLOWER_ENJOY_CARD = registerCard(PVZPlants.TWIN_SUNFLOWER, true);
    public static final DeferredHolder<Item, PlantCardItem> GLOOM_SHROOM_CARD = registerCard(PVZPlants.GLOOM_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> GLOOM_SHROOM_ENJOY_CARD = registerCard(PVZPlants.GLOOM_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> CAT_TAIL_CARD = registerCard(PVZPlants.CAT_TAIL, false);
    public static final DeferredHolder<Item, PlantCardItem> CAT_TAIL_ENJOY_CARD = registerCard(PVZPlants.CAT_TAIL, true);
    public static final DeferredHolder<Item, PlantCardItem> WINTER_MELON_CARD = registerCard(PVZPlants.WINTER_MELON, false);
    public static final DeferredHolder<Item, PlantCardItem> WINTER_MELON_ENJOY_CARD = registerCard(PVZPlants.WINTER_MELON, true);
    public static final DeferredHolder<Item, PlantCardItem> GOLD_MAGNET_CARD = registerCard(PVZPlants.GOLD_MAGNET, false);
    public static final DeferredHolder<Item, PlantCardItem> GOLD_MAGNET_ENJOY_CARD = registerCard(PVZPlants.GOLD_MAGNET, true);
    public static final DeferredHolder<Item, PlantCardItem> SPIKE_ROCK_CARD = registerCard(PVZPlants.SPIKE_ROCK, false);
    public static final DeferredHolder<Item, PlantCardItem> SPIKE_ROCK_ENJOY_CARD = registerCard(PVZPlants.SPIKE_ROCK, true);
    public static final DeferredHolder<Item, PlantCardItem> COB_CANNON_CARD = registerCard(PVZPlants.COB_CANNON, false);
    public static final DeferredHolder<Item, PlantCardItem> COB_CANNON_ENJOY_CARD = registerCard(PVZPlants.COB_CANNON, true);
   public static final DeferredHolder<Item, PlantCardItem> WATER_GUARD_CARD = registerCard(CustomPlants.WATER_GUARD, false);
    public static final DeferredHolder<Item, PlantCardItem> WATER_GUARD_ENJOY_CARD = registerCard(CustomPlants.WATER_GUARD, true);
    public static final DeferredHolder<Item, PlantCardItem> STRANGE_CAT_CARD = registerCard(MemePlants.STRANGE_CAT, false);
    public static final DeferredHolder<Item, PlantCardItem> STRANGE_CAT_ENJOY_CARD = registerCard(MemePlants.STRANGE_CAT, true);
    public static final DeferredHolder<Item, PlantCardItem> GOLD_LEAF_CARD = registerCard(OtherPlants.GOLD_LEAF, false);
    public static final DeferredHolder<Item, PlantCardItem> GOLD_LEAF_ENJOY_CARD = registerCard(OtherPlants.GOLD_LEAF, true);
    public static final DeferredHolder<Item, PlantCardItem> ANGEL_STAR_FRUIT_CARD = registerCard(OtherPlants.ANGEL_STAR_FRUIT, false);
    public static final DeferredHolder<Item, PlantCardItem> ANGEL_STAR_FRUIT_ENJOY_CARD = registerCard(OtherPlants.ANGEL_STAR_FRUIT, true);
    public static final DeferredHolder<Item, PlantCardItem> BUTTER_PULT_CARD = registerCard(CustomPlants.BUTTER_PULT, false);
    public static final DeferredHolder<Item, PlantCardItem> BUTTER_PULT_ENJOY_CARD = registerCard(CustomPlants.BUTTER_PULT, true);
    public static final DeferredHolder<Item, PlantCardItem> BAMBOO_LORD_CARD = registerCard(OtherPlants.BAMBOO_LORD, false);
    public static final DeferredHolder<Item, PlantCardItem> BAMBOO_LORD_ENJOY_CARD = registerCard(OtherPlants.BAMBOO_LORD, true);
    public static final DeferredHolder<Item, PlantCardItem> ICEBERG_LETTUCE_CARD = registerCard(OtherPlants.ICEBERG_LETTUCE, false);
    public static final DeferredHolder<Item, PlantCardItem> ICEBERG_LETTUCE_ENJOY_CARD = registerCard(OtherPlants.ICEBERG_LETTUCE, true);
    public static final DeferredHolder<Item, PlantCardItem> BONK_CHOY_CARD = registerCard(OtherPlants.BONK_CHOY, false);
    public static final DeferredHolder<Item, PlantCardItem> BONK_CHOY_ENJOY_CARD = registerCard(OtherPlants.BONK_CHOY,true);
    public static final DeferredHolder<Item, PlantCardItem> STEEL_PUMPKIN_CARD = registerCard(PVZPlants.STEEL_PUMPKIN, false);
    public static final DeferredHolder<Item, PlantCardItem> STEEL_PUMPKIN_ENJOY_CARD = registerCard(PVZPlants.STEEL_PUMPKIN, true);
    public static final DeferredHolder<Item, PlantCardItem> ICE_CABBAGE_PULT_CARD = registerCard(PVZPlants.ICE_CABBAGE_PULT, false);
    public static final DeferredHolder<Item, PlantCardItem> ICE_CABBAGE_PULT_ENJOY_CARD = registerCard(PVZPlants.ICE_CABBAGE_PULT, true);
    public static final DeferredHolder<Item, PlantCardItem> FIRE_PEASHOOTER_CARD = registerCard(PVZPlants.FIRE_PEASHOOTER, false);
    public static final DeferredHolder<Item, PlantCardItem> FIRE_PEASHOOTER_ENJOY_CARD = registerCard(PVZPlants.FIRE_PEASHOOTER, true);
    public static final DeferredHolder<Item, PlantCardItem> CHORUS_FRUIT_PULT_CARD = registerCard(PVZPlants.CHORUS_FRUIT_PULT, false);
    public static final DeferredHolder<Item, PlantCardItem> CHORUS_FRUIT_PULT_ENJOY_CARD = registerCard(PVZPlants.CHORUS_FRUIT_PULT, true);
    public static final DeferredHolder<Item, PlantCardItem> ROTATE_RADISH_CARD = registerCard(PVZPlants.ROTATE_RADISH, false);
    public static final DeferredHolder<Item, PlantCardItem> ROTATE_RADISH_ENJOY_CARD = registerCard(PVZPlants.ROTATE_RADISH, true);
    public static final DeferredHolder<Item, PlantCardItem> BURST_KERNEL_PULT_CARD = registerCard(PVZPlants.BURST_KERNEL_PULT, false);
    public static final DeferredHolder<Item, PlantCardItem> BURST_KERNEL_PULT_ENJOY_CARD = registerCard(PVZPlants.BURST_KERNEL_PULT, true);
    public static final DeferredHolder<Item, PlantCardItem> BLAZE_WART_CARD = registerCard(PVZPlants.BLAZE_WART, false);
    public static final DeferredHolder<Item, PlantCardItem> BLAZE_WART_ENJOY_CARD = registerCard(PVZPlants.BLAZE_WART, true);
    public static final DeferredHolder<Item, PlantCardItem> GRASS_CARP_CARD = registerCard(PVZPlants.GRASS_CARP, false);
    public static final DeferredHolder<Item, PlantCardItem> GRASS_CARP_ENJOY_CARD = registerCard(PVZPlants.GRASS_CARP, true);
    public static final DeferredHolder<Item, PlantCardItem> SELF_IMITATER_CARD = registerCard(PVZPlants.SELF_IMITATER, false);
    public static final DeferredHolder<Item, PlantCardItem> SELF_IMITATER_ENJOY_CARD = registerCard(PVZPlants.SELF_IMITATER, true);
    public static final DeferredHolder<Item, PlantCardItem> RE_ICEPEA_CARD = registerCard(PVZPlants.RE_ICEPEA, false);
    public static final DeferredHolder<Item, PlantCardItem> RE_ICEPEA_ENJOY_CARD = registerCard(PVZPlants.RE_ICEPEA, true);
    public static final DeferredHolder<Item, PlantCardItem> PEAPOD_CARD = registerCard(PVZPlants.PEA_POD, false);
    public static final DeferredHolder<Item, PlantCardItem> PEAPOD_ENJOY_CARD = registerCard(PVZPlants.PEA_POD, true);
    public static final DeferredHolder<Item, PlantCardItem> SCULK_SHROOM_CARD = registerCard(PVZPlants.SCULK_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> SCULK_SHROOM_ENJOY_CARD = registerCard(PVZPlants.SCULK_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> ORIGIN_SHROOM_CARD = registerCard(PVZPlants.ORIGIN_SHROOM, false);
    public static final DeferredHolder<Item, PlantCardItem> ORIGIN_SHROOM_ENJOY_CARD = registerCard(PVZPlants.ORIGIN_SHROOM, true);
    public static final DeferredHolder<Item, PlantCardItem> BEE_SHOOTER_CARD = registerCard(PVZPlants.BEE_SHOOTER, false);
    public static final DeferredHolder<Item, PlantCardItem> BEE_SHOOTER_ENJOY_CARD = registerCard(PVZPlants.BEE_SHOOTER, true);
    public static final DeferredHolder<Item, PlantCardItem> FODDER_BUSH_CARD = registerCard(PVZPlants.FODDER_BUSH, false);
    public static final DeferredHolder<Item, PlantCardItem> FODDER_BUSH_ENJOY_CARD = registerCard(PVZPlants.FODDER_BUSH, true);
    public static final DeferredHolder<Item, PlantCardItem> GOLDEN_MELON_PULT_CARD = registerCard(PVZPlants.GOLDEN_MELON_PULT, false);
    public static final DeferredHolder<Item, PlantCardItem> GOLDEN_MELON_PULT_ENJOY_CARD = registerCard(PVZPlants.GOLDEN_MELON_PULT, true);
    public static final DeferredHolder<Item, PlantCardItem> PRIMAL_PEA_SHOOTER_CARD = registerCard(PVZPlants.PRIMAL_PEA_SHOOTER, false);
    public static final DeferredHolder<Item, PlantCardItem> PRIMAL_PEA_SHOOTER_ENJOY_CARD = registerCard(PVZPlants.PRIMAL_PEA_SHOOTER, true);
    public static final DeferredHolder<Item, PlantCardItem> GOO_PEA_SHOOTER_CARD = registerCard(PVZPlants.GOO_PEA_SHOOTER, false);
    public static final DeferredHolder<Item, PlantCardItem> GOO_PEA_SHOOTER_ENJOY_CARD = registerCard(PVZPlants.GOO_PEA_SHOOTER, true);
    public static final DeferredHolder<Item, PlantCardItem> MAGNIFYING_GRASS_CARD = registerCard(PVZPlants.MAGNIFYING_GRASS, false);
    public static final DeferredHolder<Item, PlantCardItem> MAGNIFYING_GRASS_ENJOY_CARD = registerCard(PVZPlants.MAGNIFYING_GRASS, true);
    public static final DeferredHolder<Item, PlantCardItem> WATER_POT_CARD = registerCard(PVZPlants.WATER_POT, false);
    public static final DeferredHolder<Item, PlantCardItem> WATER_POT_ENJOY_CARD = registerCard(PVZPlants.WATER_POT, true);
    public static final DeferredHolder<Item, PlantCardItem> POT_GRASS_CARD = registerCard(PVZPlants.POT_GRASS, false);
    public static final DeferredHolder<Item, PlantCardItem> POT_GRASS_ENJOY_CARD = registerCard(PVZPlants.POT_GRASS, true);
    public static final DeferredHolder<Item, ImitaterCardItem> IMITATER_CARD = ITEMS.register("imitater_card", id -> new ImitaterCardItem(id,false));
    public static final DeferredHolder<Item,ImitaterCardItem> IMITATER_ENJOY_CARD = ITEMS.register("imitater_enjoy_card", id -> new ImitaterCardItem(id,true));


    /* ===== 辅助方法 ===== */

    private static DeferredHolder<Item, PvZSpawnEggItem> registerSpawnEgg(String name,
                                                                          Supplier<? extends EntityType<?>> entityType, int color1, int color2) {
        return ITEMS.register(name + "_spawn_egg",
                id -> new PvZSpawnEggItem(entityType, color1, color2, new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM,id))));
    }

    private static DeferredHolder<Item, PvZSpawnEggItem> registerSpawnEgg(String name,
                                                                          Supplier<? extends EntityType<?>> entityType, Pair<Integer, Integer> color) {
        return ITEMS.register(name + "_spawn_egg",
                id -> new PvZSpawnEggItem(entityType, color.getFirst(), color.getSecond(), new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM,id))));
    }

    private static DeferredHolder<Item, PlantCardItem> registerCard( DeferredHolder<IPlantType, ? extends IPlantType>  plant, boolean is) {
        String name = plant.key().identifier().getPath();
        if (is) name = name + "_enjoy";
        name = name + "_card";
        return ITEMS.register(name, id -> new PlantCardItem(id,plant, is));
    }
}
