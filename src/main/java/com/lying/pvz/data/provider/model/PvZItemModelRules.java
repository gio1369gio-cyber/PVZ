package com.lying.pvz.data.provider.model;

import com.lying.pvz.api.card.PvZCardComponents;
import com.lying.pvz.client.model.baked.BowlingGloveItemModel;
import com.lying.pvz.client.render.renderer.itemstack.BowlingGloveRenderer;
import com.lying.pvz.client.render.renderer.itemstack.ImitaterCardRenderer;
import com.lying.pvz.client.render.state.entity.bowling.BowlingRenderState;
import com.lying.pvz.common.init.PvZDataComponents;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.item.PvZEggTint;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SpecialModelWrapper;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class PvZItemModelRules {

    private static final String MOD_ID = "pvz";

    private PvZItemModelRules() {}
    /**
     * 手写 Blockbench / 复杂 overrides 模型，跳过生成。
     */
    private static final Set<String> HANDWRITTEN = Set.copyOf(List.of(
            "balloon",
            "garden_compass",
            "jack_box",
            "pea_gun",
            "pole",
            "resource_collector",
            "warning_sign",
            "zombie_flag"
    ));

    public static void registerHandwrittenItems(ItemModelGenerators itemModels) {
        for (String name : HANDWRITTEN) {
            Item item = BuiltInRegistries.ITEM.getValue(pvz(name));
            if (item == Items.AIR) continue;
            // garden_compass 使用 range_dispatch 数据驱动模型（32 帧角度指针），
            // 由 GardenCompassAngleProperty 驱动，不能走 plainModel。
            if ("garden_compass".equals(name)) {
                itemModels.itemModelOutput.accept(item, gardenCompassModel());
                continue;
            }
            // 只登记"这个 item 用哪个模型"，不写 json
            itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(pvz("item/" + name)));
        }
    }

    /**
     * 花园指南针的 range_dispatch 模型：按 {@code pvz:garden_compass_angle}（0~1）映射到
     * 32 帧指针模型 {@code item/garden_compass/00~31}。角度×32 后按阈值选择帧，
     * 顺序与 1.21.1 的 overrides 表一致（16 为起点，向 17…31→00…15 循环）。
     */
    private static ItemModel.Unbaked gardenCompassModel() {
        // 帧序号按角度 0~1 循环：从 16 开始
        int[] frames = new int[32];
        for (int i = 0; i < 32; i++) {
            frames[i] = (16 + i) % 32;
        }
        List<net.minecraft.client.renderer.item.RangeSelectItemModel.Entry> entries =
                new java.util.ArrayList<>(32);
        // threshold 必须用 i（0~31），而非 i+0.5：
        // RangeSelectItemModel 取「最后一个 threshold <= value」的 entry，
        // value = property * 32 ∈ [0, 32]。threshold 从 0 起才能覆盖 [0, 0.5) 区间，
        // 否则指南针指向正北（value≈0）时无 entry 匹配，落到 null fallback → 错误材质。
        for (int i = 0; i < 32; i++) {
            String id = String.format("%02d", frames[i]);
            entries.add(new net.minecraft.client.renderer.item.RangeSelectItemModel.Entry(
                    i, ItemModelUtils.plainModel(pvz("item/garden_compass/" + id))));
        }
        return ItemModelUtils.rangeSelect(
                com.lying.pvz.client.properties.GardenCompassAngleProperty.INSTANCE,
                32.0F, entries);
    }
    public static void registerBowingItems(ItemModelGenerators itemModels) {
        itemModels.itemModelOutput.accept(PvZItemRegister.BOWLING_GLOVE.get(),
                new BowlingGloveItemModel.Unbaked(StringUtil.id("item/bowling_glove"),
                        new BowlingGloveRenderer.Unbaked()));
    }

    public static void registerScreenDoorItem(ItemModelGenerators itemModels) {
        Item item = PvZItemRegister.SCREEN_DOOR.get();
        Identifier normal = ModelLocationUtils.getModelLocation(item);
        Identifier blocking = ModelLocationUtils.getModelLocation(item,"_blocking");
        itemModels.itemModelOutput.accept(item,
                ItemModelUtils.conditional(
                        ItemModelUtils.isUsingItem(),
                        ItemModelUtils.plainModel(blocking),
                        ItemModelUtils.plainModel(normal)));
    }

    public static void registerImitaterItems(ItemModelGenerators itemModels) {
        Material base    = new Material(pvz("item/misc/gold_card"));
        Material overlay = new Material(pvz("screenshot/plant/imitater"));
        Identifier modelId = CARD_BASE.create(
                PvZItemRegister.IMITATER_CARD.get(),
                TextureMapping.layered(base, overlay),
                itemModels.modelOutput);

        itemModels.itemModelOutput.accept(PvZItemRegister.IMITATER_CARD.get(),
                ItemModelUtils.conditional(
                        ItemModelUtils.hasComponent(PvZCardComponents.IMITATED_CARD.get()),
                        ItemModelUtils.specialModel(modelId, new ImitaterCardRenderer.Unbaked()),
                        // 无内层卡时 → 画普通底卡
                        ItemModelUtils.plainModel(modelId)
                ));
    }

    /**
     * 仅 JSON 存在、注册表不存在：记录，不生成、不补注册。
     */
    private static final Set<String> ONLY_IN_JSON = Set.of(
            "blue_flame_pea",
            "guild_book",
            "plant_enjoy_card",
            "steel_axe",
            "steel_hoe",
            "steel_pickaxe",
            "steel_shovel",
            "steel_sword",
            "steel_ingot",
            "steel_block",
            "strange_help",
            "zomboss_doll"

    );
    /** 卡片基础模板：parent = pvz:item/card_base，两个纹理槽 */
    private static final ModelTemplate CARD_BASE = new ModelTemplate(
            Optional.of(Identifier.fromNamespaceAndPath(MOD_ID, "item/card_base")),
            Optional.empty(),
            TextureSlot.LAYER0, TextureSlot.LAYER1);

    /* ===== A. 普通物品：item/generated ===== */
    public static void registerBasicItems(ItemModelGenerators itemModels) {
        List<DeferredHolder<Item, ? extends Item>> items = List.of(
                PvZItemRegister.ALMANAC,
                PvZItemRegister.AMETHYST_INGOT,
                PvZItemRegister.APPEASE_ESSENCE,
                PvZItemRegister.ARMA_ESSENCE,
                PvZItemRegister.ASSIST_ESSENCE,
                PvZItemRegister.BLACK_CARD,
                PvZItemRegister.BLUE_CARD,
                PvZItemRegister.BOBSLE_CAR,
                PvZItemRegister.BOWLING_MUSIC_DISC,
                PvZItemRegister.BUCKET_HEAD,
                PvZItemRegister.CABBAGE,
                PvZItemRegister.CABBAGE_SEEDS,
                PvZItemRegister.ICE_CABBAGE,
                PvZItemRegister.CANDY,
                PvZItemRegister.CAR_KEY,
                PvZItemRegister.CARD_PACK,
                PvZItemRegister.CHALLENGE_ENVELOPE,
                PvZItemRegister.CHOCOLATE,
                PvZItemRegister.CONE_HEAD,
                PvZItemRegister.COOKED_BRAIN,
                PvZItemRegister.CORN,
                PvZItemRegister.CORN_SEEDS,
                PvZItemRegister.DEFENCE_ESSENCE,
                PvZItemRegister.EDGAR_DOLL,
                PvZItemRegister.ELECTRIC_ESSENCE,
                PvZItemRegister.ENFORCE_ESSENCE,
                PvZItemRegister.EXPLOSION_ESSENCE,
                PvZItemRegister.FAKE_BRAIN,
                PvZItemRegister.FIRE_CRACKER,
                PvZItemRegister.FLAME_ESSENCE,
                PvZItemRegister.FLAME_PEA,
                PvZItemRegister.FOOTBALL_BOOTS,
                PvZItemRegister.FOOTBALL_CHESTPLATE,
                PvZItemRegister.FOOTBALL_HELMET,
                PvZItemRegister.FOOTBALL_LEGGINGS,
                PvZItemRegister.FROZEN_MELON_SLICE,
                PvZItemRegister.GARDEN_RAKE,
                PvZItemRegister.GIGA_BOOTS,
                PvZItemRegister.GIGA_CHESTPLATE,
                PvZItemRegister.GIGA_HELMET,
                PvZItemRegister.GIGA_LEGGINGS,
                PvZItemRegister.GOLD_CARD,
                PvZItemRegister.GRAY_CARD,
                PvZItemRegister.GREEN_CARD,
                PvZItemRegister.ICE_ESSENCE,
                PvZItemRegister.LAWN_MOWER,
                PvZItemRegister.LIGHT_ESSENCE,
                PvZItemRegister.MAGIC_ESSENCE,
                PvZItemRegister.MEGA_CARD,
                PvZItemRegister.NUT,
                PvZItemRegister.ORIGIN_ESSENCE,
                PvZItemRegister.ORIGIN_INGOT,
                PvZItemRegister.PEA_SOUP,
                PvZItemRegister.PEPPER,
                PvZItemRegister.CHILI_SEEDS,
                PvZItemRegister.GARLIC,
                PvZItemRegister.POOL_DISC,
                PvZItemRegister.POP_CORN,
                PvZItemRegister.PURPLE_CARD,
                PvZItemRegister.REAL_BRAIN,
                PvZItemRegister.RED_CARD,
                PvZItemRegister.SHADOW_ESSENCE,
                PvZItemRegister.SMALL_MEAT,
                PvZItemRegister.SNOW_PEA,
                PvZItemRegister.SPEAR_ESSENCE,
                PvZItemRegister.PEA,
                PvZItemRegister.SPORE,
                PvZItemRegister.TACOS,
                PvZItemRegister.TARGET_ARROW,
                PvZItemRegister.TIME_KEY_TO_DEEP,
                PvZItemRegister.TIME_SOURCE,
                PvZItemRegister.TOXIC_ESSENCE,
                PvZItemRegister.WHITE_CARD,
                PvZItemRegister.ZEN_GARDEN,
                PvZItemRegister.ZOMBIE_DOLL,
                PvZItemRegister.ZOMBIE_ON_YOUR_LAWN
        );
        for (DeferredHolder<Item, ? extends Item> holder : items) {
            itemModels.generateFlatItem(holder.get(), ModelTemplates.FLAT_ITEM);
        }

    }

    /* ===== B. 手持物品：item/handheld ===== */
    public static void registerHandheldItems(ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(PvZItemRegister.ORIGIN_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(PvZItemRegister.ORIGIN_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(PvZItemRegister.ORIGIN_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(PvZItemRegister.ORIGIN_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(PvZItemRegister.ORIGIN_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(PvZItemRegister.HAMMER.get(),ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    /* ===== C.刷怪蛋===== */
    public static void registerSpawnEggs(ItemModelGenerators itemModels) {
        List<DeferredHolder<Item, ? extends Item>> eggs = List.of(
                PvZItemRegister.BACKUP_DANCER_SPAWN_EGG,
                PvZItemRegister.BALLOON_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.BOBSLE_TEAM_SPAWN_EGG,
                PvZItemRegister.BOBSLE_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.BUCKETHEAD_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.BUNGEE_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.CATAPULT_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.COFFIN_SPAWN_EGG,
                PvZItemRegister.COIN_SPAWN_EGG,
                PvZItemRegister.CONEHEAD_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.CRAZY_DAVE_SPAWN_EGG,
                PvZItemRegister.DANCING_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.DIGGER_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.DOLPHIN_RIDER_SPAWN_EGG,
                PvZItemRegister.DOLPHIN_RIDER_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.EDGAR_090505_SPAWN_EGG,
                PvZItemRegister.EDGAR_090517_SPAWN_EGG,
                PvZItemRegister.ENERGY_SPAWN_EGG,
                PvZItemRegister.FLAG_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.FOODIE_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.FOOTBALL_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.GARGANTUAR_SPAWN_EGG,
                PvZItemRegister.GATLINGPEA_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.GIGA_FOOTBALL_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.GIGA_GARGANTUAR_SPAWN_EGG,
                PvZItemRegister.GIGA_TOMB_STONE_SPAWN_EGG,
                PvZItemRegister.IMP_SPAWN_EGG,
                PvZItemRegister.JACK_IN_BOX_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.JALAPENO_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.JEWEL_SPAWN_EGG,
                PvZItemRegister.LADDER_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.LAVA_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.MOURNER_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.NEWSPAPER_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.NOBLE_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.NORMAL_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.OLD_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.PANNEY_SPAWN_EGG,
                PvZItemRegister.PEASHOOTER_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.POGO_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.POLE_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.PUMPKIN_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.RA_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.SCREENDOOR_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.SNORKEL_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.SQUASH_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.SUN_DAVE_SPAWN_EGG,
                PvZItemRegister.SUN_SPAWN_EGG,
                PvZItemRegister.SUNDAY_EDITION_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.TALLNUT_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.TOMB_STONE_SPAWN_EGG,
                PvZItemRegister.TRICK_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.WALLNUT_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.YETI_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.ZOMBIE_DOLPHIN_SPAWN_EGG,
                PvZItemRegister.ZOMBONI_SPAWN_EGG,
                PvZItemRegister.EDGAR_090547_SPAWN_EGG,
                PvZItemRegister.FLOWER_POT_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.AIRBORNE_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.MC_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.STEEL_PUMPKIN_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.TARGET_ARROW_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.RED_EDGE_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.MJ_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.DANCER_BACKUP_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.RED_FLOWER_ZOMBIE_SPAWN_EGG,
                PvZItemRegister.CHOMPER_ZOMBIE_SPAWN_EGG
        );
        Identifier sharedModel = pvz("item/spawn_egg");
        ModelTemplates.TWO_LAYERED_ITEM.create(
                sharedModel,
                TextureMapping.layered(
                        new Material(pvz("item/spawn_egg")),
                        new Material(pvz("item/spawn_egg_overlay"))),
                itemModels.modelOutput);

        for (DeferredHolder<Item, ? extends Item> holder : eggs) {
            itemModels.itemModelOutput.accept(holder.get(),
                    ItemModelUtils.tintedModel(sharedModel, new ItemTintSource[]{
                            new PvZEggTint(false),
                            new PvZEggTint(true)
                    }));
        }
    }

    /* ===== D. 植物卡片：parent = pvz:item/card_base，两层纹理 ===== */
    public static void registerPlantCards(ItemModelGenerators itemModels) {
        plantCard(itemModels,PvZItemRegister.ANGEL_STAR_FRUIT_CARD, "purple", "angel_star_fruit");
        plantCard(itemModels,PvZItemRegister.ANGEL_STAR_FRUIT_ENJOY_CARD, "mega", "angel_star_fruit");
        plantCard(itemModels,PvZItemRegister.BAMBOO_LORD_CARD, "blue", "bamboo_lord");
        plantCard(itemModels,PvZItemRegister.BAMBOO_LORD_ENJOY_CARD, "mega", "bamboo_lord");
        plantCard(itemModels,PvZItemRegister.BLOVER_CARD, "green", "blover");
        plantCard(itemModels,PvZItemRegister.BLOVER_ENJOY_CARD, "mega", "blover");
        plantCard(itemModels,PvZItemRegister.BONK_CHOY_CARD, "purple", "bonk_choy");
        plantCard(itemModels,PvZItemRegister.BONK_CHOY_ENJOY_CARD, "mega", "bonk_choy");
        plantCard(itemModels,PvZItemRegister.BUTTER_PULT_CARD, "red", "butter_pult");
        plantCard(itemModels,PvZItemRegister.BUTTER_PULT_ENJOY_CARD, "mega", "butter_pult");
        plantCard(itemModels,PvZItemRegister.CABBAGE_PULT_CARD, "green", "cabbage_pult");
        plantCard(itemModels,PvZItemRegister.CABBAGE_PULT_ENJOY_CARD, "mega", "cabbage_pult");
        plantCard(itemModels,PvZItemRegister.CACTUS_CARD, "green", "cactus");
        plantCard(itemModels,PvZItemRegister.CACTUS_ENJOY_CARD, "mega", "cactus");
        plantCard(itemModels,PvZItemRegister.CAT_TAIL_CARD, "red", "cat_tail");
        plantCard(itemModels,PvZItemRegister.CAT_TAIL_ENJOY_CARD, "mega", "cat_tail");
        plantCard(itemModels,PvZItemRegister.CHERRY_BOMB_CARD, "blue", "cherry_bomb");
        plantCard(itemModels,PvZItemRegister.CHERRY_BOMB_ENJOY_CARD, "mega", "cherry_bomb");
        plantCard(itemModels,PvZItemRegister.CHOMPER_CARD, "blue", "chomper");
        plantCard(itemModels,PvZItemRegister.CHOMPER_ENJOY_CARD, "mega", "chomper");
        plantCard(itemModels,PvZItemRegister.COB_CANNON_CARD, "red", "cob_cannon");
        plantCard(itemModels,PvZItemRegister.COB_CANNON_ENJOY_CARD, "mega", "cob_cannon");
        plantCard(itemModels,PvZItemRegister.COFFEE_BEAN_CARD, "green", "coffee_bean");
        plantCard(itemModels,PvZItemRegister.COFFEE_BEAN_ENJOY_CARD, "mega", "coffee_bean");
        plantCard(itemModels,PvZItemRegister.DOOM_SHROOM_CARD, "red", "doom_shroom");
        plantCard(itemModels,PvZItemRegister.DOOM_SHROOM_ENJOY_CARD, "mega", "doom_shroom");
        plantCard(itemModels,PvZItemRegister.FLOWER_POT_CARD, "green", "flower_pot");
        plantCard(itemModels,PvZItemRegister.FLOWER_POT_ENJOY_CARD, "mega", "flower_pot");
        plantCard(itemModels,PvZItemRegister.FUME_SHROOM_CARD, "blue", "fume_shroom");
        plantCard(itemModels,PvZItemRegister.FUME_SHROOM_ENJOY_CARD, "mega", "fume_shroom");
        plantCard(itemModels,PvZItemRegister.GARLIC_CARD, "green", "garlic");
        plantCard(itemModels,PvZItemRegister.GARLIC_ENJOY_CARD, "mega", "garlic");
        plantCard(itemModels,PvZItemRegister.GATLING_PEA_CARD, "gold", "gatling_pea");
        plantCard(itemModels,PvZItemRegister.GATLING_PEA_ENJOY_CARD, "mega", "gatling_pea");
        plantCard(itemModels,PvZItemRegister.GLOOM_SHROOM_CARD, "gold", "gloom_shroom");
        plantCard(itemModels,PvZItemRegister.GLOOM_SHROOM_ENJOY_CARD, "mega", "gloom_shroom");
        plantCard(itemModels,PvZItemRegister.GOLD_LEAF_CARD, "purple", "gold_leaf");
        plantCard(itemModels,PvZItemRegister.GOLD_LEAF_ENJOY_CARD, "mega", "gold_leaf");
        plantCard(itemModels,PvZItemRegister.GOLD_MAGNET_CARD, "white", "gold_magnet");
        plantCard(itemModels,PvZItemRegister.GOLD_MAGNET_ENJOY_CARD, "mega", "gold_magnet");
        plantCard(itemModels,PvZItemRegister.GRAVE_BUSTER_CARD, "white", "grave_buster");
        plantCard(itemModels,PvZItemRegister.GRAVE_BUSTER_ENJOY_CARD, "mega", "grave_buster");
        plantCard(itemModels,PvZItemRegister.HYPNO_SHROOM_CARD, "blue", "hypno_shroom");
        plantCard(itemModels,PvZItemRegister.HYPNO_SHROOM_ENJOY_CARD, "mega", "hypno_shroom");
        plantCard(itemModels,PvZItemRegister.ICE_SHROOM_CARD, "purple", "ice_shroom");
        plantCard(itemModels,PvZItemRegister.ICE_SHROOM_ENJOY_CARD, "mega", "ice_shroom");
        plantCard(itemModels,PvZItemRegister.ICEBERG_LETTUCE_CARD, "gray", "iceberg_lettuce");
        plantCard(itemModels,PvZItemRegister.ICEBERG_LETTUCE_ENJOY_CARD, "mega", "iceberg_lettuce");
        plantCard(itemModels,PvZItemRegister.IMITATER_ENJOY_CARD, "mega", "imitater");
        plantCard(itemModels,PvZItemRegister.JALAPENO_CARD, "purple", "jalapeno");
        plantCard(itemModels,PvZItemRegister.JALAPENO_ENJOY_CARD, "mega", "jalapeno");
        plantCard(itemModels,PvZItemRegister.KERNEL_PULT_CARD, "blue", "kernel_pult");
        plantCard(itemModels,PvZItemRegister.KERNEL_PULT_ENJOY_CARD, "mega", "kernel_pult");
        plantCard(itemModels,PvZItemRegister.LILY_PAD_CARD, "gray", "lily_pad");
        plantCard(itemModels,PvZItemRegister.LILY_PAD_ENJOY_CARD, "mega", "lily_pad");
        plantCard(itemModels,PvZItemRegister.MAGNET_SHROOM_CARD, "blue", "magnet_shroom");
        plantCard(itemModels,PvZItemRegister.MAGNET_SHROOM_ENJOY_CARD, "mega", "magnet_shroom");
        plantCard(itemModels,PvZItemRegister.MARIGOLD_CARD, "purple", "marigold");
        plantCard(itemModels,PvZItemRegister.MARIGOLD_ENJOY_CARD, "mega", "marigold");
        plantCard(itemModels,PvZItemRegister.MELON_PULT_CARD, "gold", "melon_pult");
        plantCard(itemModels,PvZItemRegister.MELON_PULT_ENJOY_CARD, "mega", "melon_pult");
        plantCard(itemModels,PvZItemRegister.PEA_SHOOTER_CARD, "white", "pea_shooter");
        plantCard(itemModels,PvZItemRegister.PEA_SHOOTER_ENJOY_CARD, "mega", "pea_shooter");
        plantCard(itemModels,PvZItemRegister.PLANTERN_CARD, "gray", "plantern");
        plantCard(itemModels,PvZItemRegister.PLANTERN_ENJOY_CARD, "mega", "plantern");
        plantCard(itemModels,PvZItemRegister.POTATO_MINE_CARD, "white", "potato_mine");
        plantCard(itemModels,PvZItemRegister.POTATO_MINE_ENJOY_CARD, "mega", "potato_mine");
        plantCard(itemModels,PvZItemRegister.PUFF_SHROOM_CARD, "green", "puff_shroom");
        plantCard(itemModels,PvZItemRegister.PUFF_SHROOM_ENJOY_CARD, "mega", "puff_shroom");
        plantCard(itemModels,PvZItemRegister.PUMPKIN_CARD, "blue", "pumpkin");
        plantCard(itemModels,PvZItemRegister.PUMPKIN_ENJOY_CARD, "mega", "pumpkin");
        plantCard(itemModels,PvZItemRegister.REPEATER_CARD, "blue", "repeater");
        plantCard(itemModels,PvZItemRegister.REPEATER_ENJOY_CARD, "mega", "repeater");
        plantCard(itemModels,PvZItemRegister.SCAREDY_SHROOM_CARD, "white", "scaredy_shroom");
        plantCard(itemModels,PvZItemRegister.SCAREDY_SHROOM_ENJOY_CARD, "mega", "scaredy_shroom");
        plantCard(itemModels,PvZItemRegister.SEA_SHROOM_CARD, "white", "sea_shroom");
        plantCard(itemModels,PvZItemRegister.SEA_SHROOM_ENJOY_CARD, "mega", "sea_shroom");
        plantCard(itemModels,PvZItemRegister.SNOW_PEA_CARD, "green", "snow_pea");
        plantCard(itemModels,PvZItemRegister.SNOW_PEA_ENJOY_CARD, "mega", "snow_pea");
        plantCard(itemModels,PvZItemRegister.SPIKE_ROCK_CARD, "purple", "spike_rock");
        plantCard(itemModels,PvZItemRegister.SPIKE_ROCK_ENJOY_CARD, "mega", "spike_rock");
        plantCard(itemModels,PvZItemRegister.SPIKE_WEED_CARD, "white", "spike_weed");
        plantCard(itemModels,PvZItemRegister.SPIKE_WEED_ENJOY_CARD, "mega", "spike_weed");
        plantCard(itemModels,PvZItemRegister.SPLIT_PEA_CARD, "green", "split_pea");
        plantCard(itemModels,PvZItemRegister.SPLIT_PEA_ENJOY_CARD, "mega", "split_pea");
        plantCard(itemModels,PvZItemRegister.SQUASH_CARD, "green", "squash");
        plantCard(itemModels,PvZItemRegister.SQUASH_ENJOY_CARD, "mega", "squash");
        plantCard(itemModels,PvZItemRegister.STAR_FRUIT_CARD, "green", "star_fruit");
        plantCard(itemModels,PvZItemRegister.STAR_FRUIT_ENJOY_CARD, "mega", "star_fruit");
        plantCard(itemModels,PvZItemRegister.STRANGE_CAT_CARD, "purple", "strange_cat");
        plantCard(itemModels,PvZItemRegister.STRANGE_CAT_ENJOY_CARD, "mega", "strange_cat");
        plantCard(itemModels,PvZItemRegister.SUN_FLOWER_CARD, "white", "sun_flower");
        plantCard(itemModels,PvZItemRegister.SUN_FLOWER_ENJOY_CARD, "mega", "sun_flower");
        plantCard(itemModels,PvZItemRegister.SUN_SHROOM_CARD, "blue", "sun_shroom");
        plantCard(itemModels,PvZItemRegister.SUN_SHROOM_ENJOY_CARD, "mega", "sun_shroom");
        plantCard(itemModels,PvZItemRegister.TALL_NUT_CARD, "purple", "tall_nut");
        plantCard(itemModels,PvZItemRegister.TALL_NUT_ENJOY_CARD, "mega", "tall_nut");
        plantCard(itemModels,PvZItemRegister.TANGLE_KELP_CARD, "white", "tangle_kelp");
        plantCard(itemModels,PvZItemRegister.TANGLE_KELP_ENJOY_CARD, "mega", "tangle_kelp");
        plantCard(itemModels,PvZItemRegister.THREE_PEATER_CARD, "green", "three_peater");
        plantCard(itemModels,PvZItemRegister.THREE_PEATER_ENJOY_CARD, "mega", "three_peater");
        plantCard(itemModels,PvZItemRegister.TORCH_WOOD_CARD, "gold", "torch_wood");
        plantCard(itemModels,PvZItemRegister.TORCH_WOOD_ENJOY_CARD, "mega", "torch_wood");
        plantCard(itemModels,PvZItemRegister.TWIN_SUNFLOWER_CARD, "gold", "twin_sunflower");
        plantCard(itemModels,PvZItemRegister.TWIN_SUNFLOWER_ENJOY_CARD, "mega", "twin_sunflower");
        plantCard(itemModels,PvZItemRegister.UMBRELLA_LEAF_CARD, "green", "umbrella_leaf");
        plantCard(itemModels,PvZItemRegister.UMBRELLA_LEAF_ENJOY_CARD, "mega", "umbrella_leaf");
        plantCard(itemModels,PvZItemRegister.WALL_NUT_CARD, "white", "wall_nut");
        plantCard(itemModels,PvZItemRegister.WALL_NUT_ENJOY_CARD, "mega", "wall_nut");
        plantCard(itemModels,PvZItemRegister.EXPLODE_O_NUT_CARD, "red", "explode_o_nut");
        plantCard(itemModels,PvZItemRegister.EXPLODE_O_NUT_ENJOY_CARD, "mega", "explode_o_nut");
        plantCard(itemModels,PvZItemRegister.GIANT_NUT_CARD, "blue", "giant_nut");
        plantCard(itemModels,PvZItemRegister.GIANT_NUT_ENJOY_CARD, "mega", "giant_nut");
        plantCard(itemModels,PvZItemRegister.WATER_GUARD_CARD, "gray", "water_guard");
        plantCard(itemModels,PvZItemRegister.WATER_GUARD_ENJOY_CARD, "mega", "water_guard");
        plantCard(itemModels,PvZItemRegister.WINTER_MELON_CARD, "red", "winter_melon");
        plantCard(itemModels,PvZItemRegister.WINTER_MELON_ENJOY_CARD, "mega", "winter_melon");
        plantCard(itemModels,PvZItemRegister.STEEL_PUMPKIN_CARD, "blue", "steel_pumpkin");
        plantCard(itemModels,PvZItemRegister.STEEL_PUMPKIN_ENJOY_CARD, "mega", "steel_pumpkin");
        plantCard(itemModels,PvZItemRegister.ICE_CABBAGE_PULT_CARD, "green", "ice_cabbage_pult");
        plantCard(itemModels,PvZItemRegister.ICE_CABBAGE_PULT_ENJOY_CARD, "mega", "ice_cabbage_pult");
        plantCard(itemModels,PvZItemRegister.FIRE_PEASHOOTER_CARD, "green", "flame_pea");
        plantCard(itemModels,PvZItemRegister.FIRE_PEASHOOTER_ENJOY_CARD, "mega", "flame_pea");
        plantCard(itemModels,PvZItemRegister.WATER_POT_CARD, "green", "water_pot");
        plantCard(itemModels,PvZItemRegister.WATER_POT_ENJOY_CARD, "mega", "water_pot");
        plantCard(itemModels,PvZItemRegister.CHORUS_FRUIT_PULT_CARD, "purple", "chorus_fruit_pult");
        plantCard(itemModels,PvZItemRegister.CHORUS_FRUIT_PULT_ENJOY_CARD, "mega", "chorus_fruit_pult");
        plantCard(itemModels,PvZItemRegister.ROTATE_RADISH_CARD, "blue", "rotate_radish");
        plantCard(itemModels,PvZItemRegister.ROTATE_RADISH_ENJOY_CARD, "mega", "rotate_radish");
        plantCard(itemModels,PvZItemRegister.BURST_KERNEL_PULT_CARD, "green", "caramel_kernel_pult");
        plantCard(itemModels,PvZItemRegister.BURST_KERNEL_PULT_ENJOY_CARD, "mega", "caramel_kernel_pult");
        plantCard(itemModels,PvZItemRegister.BLAZE_WART_CARD, "red", "blaze_wart");
        plantCard(itemModels,PvZItemRegister.BLAZE_WART_ENJOY_CARD, "mega", "blaze_wart");
        plantCard(itemModels,PvZItemRegister.GRASS_CARP_CARD, "purple", "grass_carp");
        plantCard(itemModels,PvZItemRegister.GRASS_CARP_ENJOY_CARD, "mega", "grass_carp");
        plantCard(itemModels,PvZItemRegister.POT_GRASS_CARD, "green", "pot_grass");
        plantCard(itemModels,PvZItemRegister.POT_GRASS_ENJOY_CARD, "mega", "pot_grass");
        plantCard(itemModels,PvZItemRegister.SELF_IMITATER_CARD, "purple", "self_imitater");
        plantCard(itemModels,PvZItemRegister.SELF_IMITATER_ENJOY_CARD, "mega", "self_imitater");
        plantCard(itemModels,PvZItemRegister.RE_ICEPEA_CARD, "blue", "re_icepea");
        plantCard(itemModels,PvZItemRegister.RE_ICEPEA_ENJOY_CARD, "mega", "re_icepea");
        plantCard(itemModels,PvZItemRegister.PEAPOD_CARD, "green", "peapod");
        plantCard(itemModels,PvZItemRegister.PEAPOD_ENJOY_CARD, "mega", "peapod");
        plantCard(itemModels,PvZItemRegister.SCULK_SHROOM_CARD, "black", "sculk_shroom");
        plantCard(itemModels,PvZItemRegister.SCULK_SHROOM_ENJOY_CARD, "mega", "sculk_shroom");
        plantCard(itemModels,PvZItemRegister.ORIGIN_SHROOM_CARD, "green", "origin_shroom");
        plantCard(itemModels,PvZItemRegister.ORIGIN_SHROOM_ENJOY_CARD, "mega", "origin_shroom");
        plantCard(itemModels,PvZItemRegister.BEE_SHOOTER_CARD, "green", "bee_shooter");
        plantCard(itemModels,PvZItemRegister.BEE_SHOOTER_ENJOY_CARD, "mega", "bee_shooter");
        plantCard(itemModels,PvZItemRegister.FODDER_BUSH_CARD, "white", "fodder_bush");
        plantCard(itemModels,PvZItemRegister.FODDER_BUSH_ENJOY_CARD, "mega", "fodder_bush");
        plantCard(itemModels,PvZItemRegister.GOLDEN_MELON_PULT_CARD, "gold", "golden_melon_pult");
        plantCard(itemModels,PvZItemRegister.GOLDEN_MELON_PULT_ENJOY_CARD, "mega", "golden_melon_pult");
        plantCard(itemModels,PvZItemRegister.PRIMAL_PEA_SHOOTER_CARD, "blue", "primal_pea_shooter");
        plantCard(itemModels,PvZItemRegister.PRIMAL_PEA_SHOOTER_ENJOY_CARD, "mega", "primal_pea_shooter");
        plantCard(itemModels,PvZItemRegister.GOO_PEA_SHOOTER_CARD, "blue", "goo_pea_shooter");
        plantCard(itemModels,PvZItemRegister.GOO_PEA_SHOOTER_ENJOY_CARD, "mega", "goo_pea_shooter");
        plantCard(itemModels,PvZItemRegister.MAGNIFYING_GRASS_CARD, "blue", "magnifying_grass");
        plantCard(itemModels,PvZItemRegister.MAGNIFYING_GRASS_ENJOY_CARD, "mega", "magnifying_grass");
    }

    private static void plantCard(ItemModelGenerators itemModels, DeferredHolder<Item, ? extends Item> item, String card, String plant) {
        Material base = new Material(pvz("item/misc/" + card + "_card"));
        Material overlay = new Material(pvz("screenshot/plant/" + plant));
        Identifier modelId = CARD_BASE.create(item.get(), TextureMapping.layered(base, overlay), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item.get(), ItemModelUtils.plainModel(modelId));
    }

    /* ===== E. sun storage saplings：共用纹理 ===== */
    public static void registerSunStorageSaplings(ItemModelGenerators itemModels) {
        Identifier textureId = pvz("item/sun_storage_sapling");
        String[] names = {"sun_storage_sapling", "sun_storage_sapling2",
                "sun_storage_sapling3", "sun_storage_sapling4"};
        for (String name : names) {
            Item item = BuiltInRegistries.ITEM.getValue(pvz(name));
            if (item == Items.AIR) continue;
            itemModels.generateFlatItem(item, textureId, ModelTemplates.FLAT_ITEM);
        }
    }

    /* ===== F. garden compass frames：只写 model json，不注册 item ===== */
    public static void registerGardenCompassFrames(ItemModelGenerators itemModels) {
        for (int i = 0; i < 32; i++) {
            String id = String.format("%02d", i);
            Identifier modelId = pvz("item/garden_compass/" + id);
            Material texture = new Material(pvz("item/garden_compass/garden_compass_" + id));
            ModelTemplates.FLAT_ITEM.create(modelId, TextureMapping.layer0(texture), itemModels.modelOutput);
        }
    }

    private static Identifier pvz(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}