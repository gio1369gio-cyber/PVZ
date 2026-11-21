package com.lying.pvz.client.registry;

import com.lying.pvz.client.model.entity.plant.appease.*;
import com.lying.pvz.client.model.entity.plant.arma.*;
import com.lying.pvz.client.model.entity.plant.assist.*;
import com.lying.pvz.client.model.entity.plant.defence.*;
import com.lying.pvz.client.model.entity.plant.electric.MagnifyingGrassModel;
import com.lying.pvz.client.model.entity.plant.enforce.*;
import com.lying.pvz.client.model.entity.plant.explosion.*;
import com.lying.pvz.client.model.entity.plant.fire.BlazeWartModel;
import com.lying.pvz.client.model.entity.plant.fire.FirePeashooterModel;
import com.lying.pvz.client.model.entity.plant.fire.JalapenoModel;
import com.lying.pvz.client.model.entity.plant.fire.TorchWoodModel;
import com.lying.pvz.client.model.entity.plant.ice.*;
import com.lying.pvz.client.model.entity.plant.light.*;
import com.lying.pvz.client.model.entity.plant.magic.*;
import com.lying.pvz.client.model.entity.plant.spear.*;
import com.lying.pvz.client.model.entity.plant.toxic.*;
import com.lying.pvz.client.model.entity.zombie.boss.SphinxTerminatorModel;
import com.lying.pvz.client.model.entity.zombie.custom.LavaZombieModel;
import com.lying.pvz.client.model.entity.zombie.custom.MCZombieModel;
import com.lying.pvz.client.model.entity.zombie.custom.RedEdgeZombieModel;
import com.lying.pvz.client.model.entity.zombie.custom.TargetArrowZombieModel;
import com.lying.pvz.client.model.entity.zombie.grass.*;
import com.lying.pvz.client.model.entity.zombie.other.*;
import com.lying.pvz.client.model.entity.zombie.pool.*;
import com.lying.pvz.client.model.entity.zombie.roof.*;
import com.lying.pvz.client.model.entity.zombie.zombotany.*;
import com.lying.pvz.common.impl.plant.CustomPlants;
import com.lying.pvz.common.impl.plant.MemePlants;
import com.lying.pvz.common.impl.plant.OtherPlants;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.impl.zombie.CustomZombies;
import com.lying.pvz.common.impl.zombie.GrassZombies;
import com.lying.pvz.common.impl.zombie.OtherZombies;
import com.lying.pvz.common.impl.zombie.PoolZombies;
import com.lying.pvz.common.impl.zombie.RoofZombies;
import com.lying.pvz.common.impl.zombie.Zombotanies;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

/**
 * 主模组的模型 / 贴图 / 缩放注册入口。
 *
 * <p>由 {@link com.lying.pvz.client.PVZClientSetup} 在 {@code FMLClientSetupEvent} 中调用
 * {@link #registerAll()}。
 *
 * <p><b>贴图路径</b>：{@code texture("plant/xxx")} → {@code pvz:textures/plant/xxx.png}。
 * 若你的资源实际按 essence 分子目录（如 {@code plant/light/sun_flower}），
 * 只需调整每行的 {@code path} 入参即可。
 *
 * <p><b>⚠ LAYER 缺失</b>（需在 {@code PvZModelLayer} 补注册，否则 {@code bakeLayer} 会 NPE）：
 * <ul>
 *   <li>{@code ButterPultModel.LAYER_LOCATION}</li>
 *   <li>{@code GigaGargantuarModel.LAYER_LOCATION}（本文件先用 {@code GargantuarModel} 复用）</li>
 *   <li>{@code GigaTombStoneModel.LAYER_LOCATION}（本文件先用 {@code TombStoneModel} 复用）</li>
 * </ul>
 */
public final class PVZModelRegistrations {

    private PVZModelRegistrations() {
    }

    public static void registerAll() {
        registerPlants();
        registerZombies();
    }

    /* ================================================================== */
    /* 植物                                                                */
    /* ================================================================== */

    private static void registerPlants() {
        // ---- appease ----------------------------------------------------
        ClientModelRegistry.registerPlant(OtherPlants.ANGEL_STAR_FRUIT.get().getID(), 0.8F,
                texture("plant/appease/angel_star_fruit"), AngelStarFruitModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.PEA_SHOOTER.get().getID(), 1.0F,
                texture("plant/appease/pea_shooter"), PeaShooterModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.GATLING_PEA.get().getID(), 1.0F,
                texture("plant/appease/gatling_pea"), GatlingPeaModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.REPEATER.get().getID(), 1.0F,
                texture("plant/appease/repeater"), RepeaterModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.SPLIT_PEA.get().getID(), 1.0F,
                texture("plant/appease/split_pea"), SplitPeaModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.STAR_FRUIT.get().getID(), 0.82F,
                texture("plant/appease/star_fruit"), StarFruitModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.THREE_PEATER.get().getID(), 0.98F,
                texture("plant/appease/three_peater"), ThreePeaterModel::new);

        // ---- arma -------------------------------------------------------
        ClientModelRegistry.registerPlant(CustomPlants.BUTTER_PULT.get().getID(), 0.9F,
                texture("plant/arma/butter_pult"), KernelPultModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.CABBAGE_PULT.get().getID(), 1.0F,
                texture("plant/arma/cabbage_pult"), CabbagePultModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.KERNEL_PULT.get().getID(), 0.9F,
                texture("plant/arma/kernel_pult"), KernelPultModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.MELON_PULT.get().getID(), 0.9F,
                texture("plant/arma/melon_pult"), MelonPultModel::new);

        // ---- assist -----------------------------------------------------
        ClientModelRegistry.registerPlant(PVZPlants.BLOVER.get().getID(), 1.2F,
                texture("plant/assist/blover"), BloverModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.FLOWER_POT.get().getID(), 0.85F,
                texture("plant/assist/flower_pot"), FlowerPotModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.GOLD_MAGNET.get().getID(), 1.2F,
                texture("plant/assist/gold_magnet"), GoldMagnetModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.GRAVE_BUSTER.get().getID(), 1.0F,
                texture("plant/assist/grave_buster"), GraveBusterModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.LILY_PAD.get().getID(), 1.0F,
                texture("plant/assist/lily_pad"), LilyPadModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.MAGNET_SHROOM.get().getID(), 1.2F,
                texture("plant/assist/magnet_shroom"), root -> new MagnetShroomModel(root, RenderTypes::entityTranslucentCull));
        ClientModelRegistry.registerPlant(PVZPlants.MARIGOLD.get().getID(), 0.5F,
                texture("plant/assist/marigold"), MariGoldModel::new);

        // ---- defence ----------------------------------------------------
        ClientModelRegistry.registerPlant(PVZPlants.WALL_NUT.get().getID(), 1.0F,
                texture("plant/defence/wall_nut"), WallNutModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.EXPLODE_O_NUT.get().getID(), 1.0F,
                texture("plant/defence/explode_nut"), WallNutModel::new);

        ClientModelRegistry.registerPlant(PVZPlants.GIANT_NUT.get().getID(), 0.55F,
                texture("plant/defence/tall_nut"), TallNutModel::new);

        ClientModelRegistry.registerPlant(PVZPlants.GARLIC.get().getID(), 0.9F,
                texture("plant/defence/garlic"), GarlicModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.PUMPKIN.get().getID(), 1.0F,
                texture("plant/defence/pumpkin"), PumpkinModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.TALL_NUT.get().getID(), 0.4F,
                texture("plant/defence/tall_nut"), TallNutModel::new);
        ClientModelRegistry.registerPlant(CustomPlants.WATER_GUARD.get().getID(), 0.8F,
                texture("plant/defence/water_guard"), WaterGuardModel::new);

        // ---- enforce ----------------------------------------------------
        ClientModelRegistry.registerPlant(OtherPlants.BONK_CHOY.get().getID(), 0.9F,
                texture("plant/enforce/bonk_choy"), BonkChoyModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.CHOMPER.get().getID(), 0.85F,
                texture("plant/enforce/chomper"), ChomperModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.SQUASH.get().getID(), 0.5F,
                texture("plant/enforce/squash"), SquashModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.TANGLE_KELP.get().getID(), 1.0F,
                texture("plant/enforce/tangle_kelp"), TangleKelpModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.UMBRELLA_LEAF.get().getID(), 0.8F,
                texture("plant/enforce/umbrella_leaf"), UmbrellaLeafModel::new);

        // ---- explosion --------------------------------------------------
        ClientModelRegistry.registerPlant(OtherPlants.BAMBOO_LORD.get().getID(), 1.0F,
                texture("plant/explosion/bamboo_lord"), BambooLordModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.CHERRY_BOMB.get().getID(), 0.5F,
                texture("plant/explosion/cherry_bomb"), CherryBombModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.COB_CANNON.get().getID(), 1.1F,
                texture("plant/explosion/cob_cannon"), CobCannonModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.DOOM_SHROOM.get().getID(), 1.0F,
                texture("plant/explosion/doom_shroom"), DoomShroomModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.POTATO_MINE.get().getID(), 1.0F,
                texture("plant/explosion/potato_mine"), PotatoMineModel::new);

        // ---- flame ------------------------------------------------------
        ClientModelRegistry.registerPlant(PVZPlants.JALAPENO.get().getID(), 1.0F,
                texture("plant/flame/jalapeno"), JalapenoModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.TORCH_WOOD.get().getID(), 1.0F,
                texture("plant/flame/torch_wood"), TorchWoodModel::new);

        // ---- ice --------------------------------------------------------
        ClientModelRegistry.registerPlant(PVZPlants.ICE_SHROOM.get().getID(), 1.0F,
                texture("plant/ice/ice_shroom"), IceShroomModel::new);
        ClientModelRegistry.registerPlant(OtherPlants.ICEBERG_LETTUCE.get().getID(), 1.0F,
                texture("plant/ice/iceberg_lettuce"), IcebergLettuceModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.SNOW_PEA.get().getID(), 1.0F,
                texture("plant/ice/snow_pea"), SnowPeaModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.WINTER_MELON.get().getID(), 0.9F,
                texture("plant/ice/winter_melon"), WinterMelonModel::new);

        // ---- light ------------------------------------------------------
        ClientModelRegistry.registerPlant(OtherPlants.GOLD_LEAF.get().getID(), 0.8F,
                texture("plant/light/gold_leaf"), GoldLeafModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.PLANTERN.get().getID(), 1.0F,
                texture("plant/light/plantern"),  root -> new PlanternModel(root, RenderTypes::entityTranslucentCull));
        ClientModelRegistry.registerPlant(PVZPlants.SUN_FLOWER.get().getID(), 1.0F,
                texture("plant/light/sun_flower"), SunFlowerModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.SUN_SHROOM.get().getID(), 0.4F,
                texture("plant/light/sun_shroom"), SunShroomModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.TWIN_SUNFLOWER.get().getID(), 0.45F,
                texture("plant/light/twin_sunflower"), TwinSunFlowerModel::new);

        // ---- magic ------------------------------------------------------
        ClientModelRegistry.registerPlant(PVZPlants.COFFEE_BEAN.get().getID(), 1.0F,
                texture("plant/magic/coffee_bean"), CoffeeBeanModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.HYPNO_SHROOM.get().getID(), 1.0F,
                texture("plant/magic/hypno_shroom"), HypnoShroomModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.IMITATER.get().getID(), 0.9F,
                texture("plant/magic/imitater"), ImitaterModel::new);
        ClientModelRegistry.registerPlant(MemePlants.STRANGE_CAT.get().getID(), 0.18F,
                texture("plant/magic/strange_cat"), StrangeCatModel::new);

        // ---- spear ------------------------------------------------------
        ClientModelRegistry.registerPlant(PVZPlants.CACTUS.get().getID(), 1.0F,
                texture("plant/spear/cactus"), CactusModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.CAT_TAIL.get().getID(), 1.0F,
                texture("plant/spear/cat_tail"), CatTailModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.SPIKE_ROCK.get().getID(), 0.5F,
                texture("plant/spear/spike_rock"), SpikeRockModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.SPIKE_WEED.get().getID(), 1.0F,
                texture("plant/spear/spike_weed"), SpikeWeedModel::new);

        // ---- toxic ------------------------------------------------------
        ClientModelRegistry.registerPlant(PVZPlants.FUME_SHROOM.get().getID(), 0.9F,
                texture("plant/toxic/fume_shroom"), FumeShroomModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.GLOOM_SHROOM.get().getID(), 1.0F,
                texture("plant/toxic/gloom_shroom"), GloomShroomModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.PUFF_SHROOM.get().getID(), 0.6F,
                texture("plant/toxic/puff_shroom"), PuffShroomModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.SCAREDY_SHROOM.get().getID(), 0.7F,
                texture("plant/toxic/scaredy_shroom"), ScaredyShroomModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.SEA_SHROOM.get().getID(), 0.6F,
                texture("plant/toxic/sea_shroom"), SeaShroomModel::new);

        // ---- 新植物（addon）--------------------------------------------
        ClientModelRegistry.registerPlant(PVZPlants.BEE_SHOOTER.get().getID(), 1.0F,
                texture("plant/appease/bee_shooter"), BeeShooterModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.PEA_POD.get().getID(), 1.1F,
                texture("plant/appease/peapod"), PeaPodModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.PRIMAL_PEA_SHOOTER.get().getID(), 1.0F,
                texture("plant/appease/primal_pea_shooter"), PrimalPeaShooterModel::new);

        ClientModelRegistry.registerPlant(PVZPlants.BURST_KERNEL_PULT.get().getID(), 0.95F,
                texture("plant/arma/burst_kernel_pult"), BurstKernelPultModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.CHORUS_FRUIT_PULT.get().getID(), 0.95F,
                texture("plant/arma/chorus_fruit_pult"), ChorusFruitPultModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.GOLDEN_MELON_PULT.get().getID(), 1.0F,
                texture("plant/arma/golden_melon_pult"), GoldenMelonPultModel::new);

        ClientModelRegistry.registerPlant(PVZPlants.FODDER_BUSH.get().getID(), 1.0F,
                texture("plant/assist/fodder_bush"), FodderBushModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.GRASS_CARP.get().getID(), 1.4F,
                texture("plant/assist/grass_carp"), GrassCarpModel::new);

        ClientModelRegistry.registerPlant(PVZPlants.STEEL_PUMPKIN.get().getID(), 1.0F,
                texture("plant/defence/steel_pumpkin"), SteelPumpkinModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.SELF_IMITATER.get().getID(), 1.1F,
                texture("plant/defence/self_imitater"), SelfImitaterModel::new);

        ClientModelRegistry.registerPlant(PVZPlants.MAGNIFYING_GRASS.get().getID(), 1.3F,
                texture("plant/electric/magnifying_grass"), MagnifyingGrassModel::new);

        ClientModelRegistry.registerPlant(PVZPlants.ROTATE_RADISH.get().getID(), 0.95F,
                texture("plant/enforce/rotate_radish"), RotateRadishModel::new);

        ClientModelRegistry.registerPlant(PVZPlants.BLAZE_WART.get().getID(), 0.95F,
                texture("plant/flame/blaze_wart"), BlazeWartModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.FIRE_PEASHOOTER.get().getID(), 1.0F,
                texture("plant/flame/fire_peashooter"), FirePeashooterModel::new);

        ClientModelRegistry.registerPlant(PVZPlants.ICE_CABBAGE_PULT.get().getID(), 0.95F,
                texture("plant/ice/ice_cabbage_pult"), IceCabbagePultModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.RE_ICEPEA.get().getID(), 1.1F,
                texture("plant/ice/re_icepea"), SnowPeaModel::new);

        ClientModelRegistry.registerPlant(PVZPlants.GOO_PEA_SHOOTER.get().getID(), 1.0F,
                texture("plant/toxic/goo_pea_shooter"), GooPeaShooterModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.ORIGIN_SHROOM.get().getID(), 0.9F,
                texture("plant/toxic/origin_shroom"), OriginShroomModel::new);
        ClientModelRegistry.registerPlant(PVZPlants.SCULK_SHROOM.get().getID(), 1.4F,
                texture("plant/toxic/sculk_shroom"), SculkShroomModel::new);
    }

    /* ================================================================== */
    /* 僵尸                                                                */
    /* ================================================================== */

    private static void registerZombies() {

        // ---- custom（全部 0.5F，与旧值一致）----------------------------
        ClientModelRegistry.registerZombie(CustomZombies.COFFIN.get().getID(), 0.5F,
                texture("zombie/custom/coffin"),
                set -> new CoffinModel(set.bakeLayer(CoffinModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(CustomZombies.GIGA_TOMBSTONE.get().getID(), 0.5F,
                texture("zombie/custom/giga_tomb_stone"),
                set -> new TombStoneModel(set.bakeLayer(TombStoneModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(CustomZombies.LAVA_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/custom/lava_zombie"),
                set -> new LavaZombieModel(set.bakeLayer(LavaZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(CustomZombies.MOURNER_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/custom/mourner_zombie"),
                set -> new MournerZombieModel(set.bakeLayer(MournerZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(CustomZombies.NOBLE_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/custom/noble_zombie"),
                set -> new NobleZombieModel(set.bakeLayer(NobleZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(CustomZombies.TRICK_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/custom/trick_zombie"),
                set -> new TrickZombieModel(set.bakeLayer(TrickZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(CustomZombies.MC_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/custom/mc_zombie"),
                set -> new MCZombieModel(set.bakeLayer(MCZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(CustomZombies.RED_EDGE_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/custom/red_edge_zombie"),
                set -> new RedEdgeZombieModel(set.bakeLayer(RedEdgeZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(CustomZombies.TARGET_ARROW_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/custom/target_arrow_zombie"),
                set -> new TargetArrowZombieModel(set.bakeLayer(TargetArrowZombieModel.LAYER_LOCATION)));

        // ---- grass（全部 0.5F，与旧值一致）-----------------------------
        ClientModelRegistry.registerZombie(GrassZombies.BACKUP_DANCER.get().getID(), 0.5F,
                texture("zombie/grass/backup_dancer"),
                set -> new BackupDancerModel(set.bakeLayer(BackupDancerModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.BUCKETHEAD_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/buckethead_zombie"),
                set -> new BucketHeadZombieModel(set.bakeLayer(BucketHeadZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.CONEHEAD_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/conehead_zombie"),
                set -> new ConeHeadZombieModel(set.bakeLayer(ConeHeadZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.DANCING_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/dancing_zombie"),
                set -> new DancingZombieModel(set.bakeLayer(DancingZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.FLAG_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/flag_zombie"),
                set -> new FlagZombieModel(set.bakeLayer(FlagZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.FOOTBALL_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/football_zombie"),
                set -> new FootballZombieModel(set.bakeLayer(FootballZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.GIGA_FOOTBALL_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/giga_football_zombie"),
                set -> new GigaFootballZombieModel(set.bakeLayer(GigaFootballZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.NEWSPAPER_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/newspaper_zombie"),
                set -> new NewspaperZombieModel(set.bakeLayer(NewspaperZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.NORMAL_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/normal_zombie"),
                set -> new NormalZombieModel(set.bakeLayer(NormalZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.OLD_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/old_zombie"),
                set -> new OldZombieModel(set.bakeLayer(OldZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.POLE_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/pole_zombie"),
                set -> new PoleZombieModel(set.bakeLayer(PoleZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.SCREENDOOR_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/screendoor_zombie"),
                set -> new ScreenDoorZombieModel(set.bakeLayer(ScreenDoorZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.SUNDAY_EDITION_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/grass/sunday_edition_zombie"),
                set -> new SundayEditionZombieModel(set.bakeLayer(SundayEditionZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(GrassZombies.TOMB_STONE.get().getID(), 0.5F,
                texture("zombie/grass/tomb_stone"),
                set -> new TombStoneModel(set.bakeLayer(TombStoneModel.LAYER_LOCATION)));

        // ---- other（全部 0.5F）----------------------------------------
        ClientModelRegistry.registerZombie(OtherZombies.RA_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/other/ra_zombie"),
                set -> new RaZombieModel(set.bakeLayer(RaZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(OtherZombies.DANCER_BACKUP_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/other/dancer_backup_zombie"),
                set -> new DancerBackupModel(set.bakeLayer(DancerBackupModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(OtherZombies.FLOWER_POT_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/other/flower_pot_zombie"),
                set -> new FlowerPotZombieModel(set.bakeLayer(FlowerPotZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(OtherZombies.MJ_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/other/mj_zombie"),
                set -> new MJZombieModel(set.bakeLayer(MJZombieModel.LAYER_LOCATION)));

        // ---- pool（全部 0.5F，与旧值一致）------------------------------
        ClientModelRegistry.registerZombie(PoolZombies.BALLOON_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/pool/balloon_zombie"),
                set -> new BalloonZombieModel(set.bakeLayer(BalloonZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.BOBSLE_TEAM.get().getID(), 0.5F,
                texture("zombie/pool/bobsle_team"),
                set -> new BobsleTeamModel(set.bakeLayer(BobsleTeamModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.BOBSLE_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/pool/bobsle_zombie"),
                set -> new BobsleZombieModel(set.bakeLayer(BobsleZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.DIGGER_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/pool/digger_zombie"),
                set -> new DiggerZombieModel(set.bakeLayer(DiggerZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.DOLPHIN_RIDER.get().getID(), 0.5F,
                texture("zombie/pool/dolphin_rider"),
                set -> new DolphinRiderModel(set.bakeLayer(DolphinRiderModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.DOLPHIN_RIDER_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/pool/dolphin_rider_zombie"),
                set -> new DolphinRiderZombieModel(set.bakeLayer(DolphinRiderZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.JACK_IN_BOX_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/pool/jack_in_box_zombie"),
                set -> new JackInBoxZombieModel(set.bakeLayer(JackInBoxZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.POGO_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/pool/pogo_zombie"),
                set -> new PogoZombieModel(set.bakeLayer(PogoZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.SNORKEL_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/pool/snorkel_zombie"),
                set -> new SnorkelZombieModel(set.bakeLayer(SnorkelZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.YETI_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/pool/yeti_zombie"),
                set -> new YetiZombieModel(set.bakeLayer(YetiZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.ZOMBIE_DOLPHIN.get().getID(), 0.5F,
                texture("zombie/pool/zombie_dolphin"),
                set -> new ZombieDolphinModel(set.bakeLayer(ZombieDolphinModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(PoolZombies.ZOMBONI.get().getID(), 0.5F,
                texture("zombie/pool/zomboni"),
                set -> new ZomboniModel(set.bakeLayer(ZomboniModel.LAYER_LOCATION)));

        // ---- roof（4 处非 0.5 的旧值需恢复）---------------------------
        ClientModelRegistry.registerZombie(RoofZombies.BUNGEE_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/roof/bungee_zombie"),
                set -> new BungeeZombieModel(set.bakeLayer(BungeeZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(RoofZombies.CATAPULT_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/roof/catapult_zombie"),
                set -> new CatapultZombieModel(set.bakeLayer(CatapultZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(RoofZombies.EDGAR_090505.get().getID(), 1.6F,
                texture("zombie/roof/edgar_090505"),
                set -> new EdgarRobotModel(set.bakeLayer(EdgarRobotModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(RoofZombies.EDGAR_090517.get().getID(), 2.0F,
                texture("zombie/roof/edgar_090517"),
                set -> new EdgarRobotModel(set.bakeLayer(EdgarRobotModel.LAYER_LOCATION)));
        
        ClientModelRegistry.registerZombie(RoofZombies.SPHINX_TERMINATOR.get().getID(), 2.2F,
                texture("zombie/boss/sphinx_terminator"),
                set -> new SphinxTerminatorModel(set.bakeLayer(SphinxTerminatorModel.LAYER_LOCATION)));

        ClientModelRegistry.registerZombie(RoofZombies.GARGANTUAR.get().getID(), 0.75F,
                texture("zombie/roof/gargantuar"),
                set -> new GargantuarModel(set.bakeLayer(GargantuarModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(RoofZombies.GIGA_GARGANTUAR.get().getID(), 0.8F,
                texture("zombie/roof/giga_gargantuar"),
                set -> new GargantuarModel(set.bakeLayer(GargantuarModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(RoofZombies.IMP.get().getID(), 0.5F,
                texture("zombie/roof/imp"),
                set -> new ImpModel(set.bakeLayer(ImpModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(RoofZombies.LADDER_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/roof/ladder_zombie"),
                set -> new LadderZombieModel(set.bakeLayer(LadderZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(RoofZombies.AIRBORNE_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/roof/airborne_zombie"),
                set -> new AirborneZombieModel(set.bakeLayer(AirborneZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(RoofZombies.EDGAR_090547.get().getID(), 1.0F,
                texture("zombie/roof/edgar_090547"),
                set -> new Edgar090547Model(set.bakeLayer(Edgar090547Model.LAYER_LOCATION)));

        // ---- zombotany（全部 0.5F，与旧值一致）------------------------
        ClientModelRegistry.registerZombie(Zombotanies.GATLINGPEA_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/zombotany/gatlingpea_zombie"),
                set -> new GatlingPeaZombieModel(set.bakeLayer(GatlingPeaZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(Zombotanies.JALAPENO_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/zombotany/jalapeno_zombie"),
                set -> new JalapenoZombieModel(set.bakeLayer(JalapenoZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(Zombotanies.PEASHOOTER_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/zombotany/peashooter_zombie"),
                set -> new PeaShooterZombieModel(set.bakeLayer(PeaShooterZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(Zombotanies.PUMPKIN_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/zombotany/pumpkin_zombie"),
                set -> new PumpkinZombieModel(set.bakeLayer(PumpkinZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(Zombotanies.SQUASH_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/zombotany/squash_zombie"),
                set -> new SquashZombieModel(set.bakeLayer(SquashZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(Zombotanies.TALLNUT_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/zombotany/tallnut_zombie"),
                set -> new TallNutZombieModel(set.bakeLayer(TallNutZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(Zombotanies.WALLNUT_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/zombotany/wallnut_zombie"),
                set -> new WallNutZombieModel(set.bakeLayer(WallNutZombieModel.LAYER_LOCATION)));

        ClientModelRegistry.registerZombie(Zombotanies.SUN_FLOWER_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/zombotany/sun_flower_zombie"),
                set -> new SunFlowerZombieModel(set.bakeLayer(SunFlowerZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(Zombotanies.STEEL_PUMPKIN_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/zombotany/steel_pumpkin_zombie"),
                set -> new SteelPumpkinZombieModel(set.bakeLayer(SteelPumpkinZombieModel.LAYER_LOCATION)));
        ClientModelRegistry.registerZombie(Zombotanies.CHOMPER_ZOMBIE.get().getID(), 0.5F,
                texture("zombie/zombotany/chomper_zombie"),
                set -> new ChomperZombieModel(set.bakeLayer(ChomperZombieModel.LAYER_LOCATION)));

    }

    /* ================================================================== */
    /* 工具                                                                */
    /* ================================================================== */

    /**
     * 构造贴图路径。
     *
     * <p>调用方传 {@code plant/pea_shooter} 形式，返回
     * {@code pvz:textures/plant/pea_shooter.png}。
     */
    private static Identifier texture(String path) {
        return StringUtil.id("textures/entity/" + path + ".png");
    }
}