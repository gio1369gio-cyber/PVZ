package com.lying.pvz.common.init;


import com.lying.pvz.common.level.entity.bowling.ExplosionBowlingEntity;
import com.lying.pvz.common.level.entity.bowling.GiantNutBowlingEntity;
import com.lying.pvz.common.level.entity.bowling.WallNutBowlingEntity;
import com.lying.pvz.common.level.entity.creature.FoodieZombieEntity;
import com.lying.pvz.common.level.entity.drop.*;
import com.lying.pvz.common.level.entity.effect.OriginEffectEntity;
import com.lying.pvz.common.level.entity.misc.*;
import com.lying.pvz.common.level.entity.npc.CrazyDaveEntity;
import com.lying.pvz.common.level.entity.npc.PennyEntity;
import com.lying.pvz.common.level.entity.npc.SunDaveEntity;
import com.lying.pvz.common.level.entity.plant.PlantEntity;
import com.lying.pvz.common.level.entity.plant.appease.*;
import com.lying.pvz.common.level.entity.plant.arma.*;
import com.lying.pvz.common.level.entity.plant.assist.*;
import com.lying.pvz.common.level.entity.plant.defence.*;
import com.lying.pvz.common.level.entity.plant.electric.MagnifyingGrassEntity;
import com.lying.pvz.common.level.entity.plant.enforce.*;
import com.lying.pvz.common.level.entity.plant.explosion.*;
import com.lying.pvz.common.level.entity.plant.fire.BlazeWartEntity;
import com.lying.pvz.common.level.entity.plant.fire.FirePeashooterEntity;
import com.lying.pvz.common.level.entity.plant.fire.JalapenoEntity;
import com.lying.pvz.common.level.entity.plant.fire.TorchWoodEntity;
import com.lying.pvz.common.level.entity.plant.ice.*;
import com.lying.pvz.common.level.entity.plant.light.*;
import com.lying.pvz.common.level.entity.plant.magic.*;
import com.lying.pvz.common.level.entity.plant.spear.CactusEntity;
import com.lying.pvz.common.level.entity.plant.spear.CatTailEntity;
import com.lying.pvz.common.level.entity.plant.spear.SpikeRockEntity;
import com.lying.pvz.common.level.entity.plant.spear.SpikeWeedEntity;
import com.lying.pvz.common.level.entity.plant.toxic.*;
import com.lying.pvz.common.level.entity.projectile.TargetArrowEntity;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.common.level.entity.zombie.boss.*;
import com.lying.pvz.common.level.entity.zombie.custom.*;
import com.lying.pvz.common.projectile.BulletEntity;
import com.lying.pvz.common.level.entity.zombie.body.ZombieDropBodyEntity;
import com.lying.pvz.common.level.entity.zombie.grass.*;
import com.lying.pvz.common.level.entity.zombie.other.*;
import com.lying.pvz.common.level.entity.zombie.part.PVZZombiePartEntity;
import com.lying.pvz.common.level.entity.zombie.pool.*;
import com.lying.pvz.common.level.entity.zombie.roof.*;
import com.lying.pvz.common.level.entity.zombie.zombotany.*;
import com.lying.pvz.common.misc.PvZEnumProxy;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.PvZMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class PvZEntityRegister {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, PvZMod.MOD_ID);

    // ===== drops =====
    public static final DeferredHolder<EntityType<?>, EntityType<SunEntity>> SUN = registerEntityType(SunEntity::new, "sun", MobCategory.AMBIENT);
    public static final DeferredHolder<EntityType<?>, EntityType<CoinEntity>> COIN = registerEntityType(CoinEntity::new, "coin", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<JewelEntity>> JEWEL = registerEntityType(JewelEntity::new, "jewel", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<EnergyEntity>> ENERGY = registerEntityType(EnergyEntity::new, "energy", MobCategory.MISC, 0.9f, 2f);
    public static final DeferredHolder<EntityType<?>, EntityType<GiftBoxEntity>> GIFT_BOX = registerEntityType(GiftBoxEntity::new, "gift_box", MobCategory.MISC, 0.9f, 1f);
    public static final DeferredHolder<EntityType<?>, EntityType<RedSunEntity>> RED_SUN = registerEntityType(RedSunEntity::new, "red_sun",MobCategory.MISC);//红色阳光

    // ===== bullets =====
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> PEA = registerProjectileEntityType(BulletEntity::new, "pea", 0.25F, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> SPORE = registerProjectileEntityType(BulletEntity::new, "spore", 0.25F, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> FUME = registerProjectileEntityType(BulletEntity::new, "fume", 0.25F, 0.25F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> STAR = registerProjectileEntityType(BulletEntity::new, "star", 0.2F, 0.2F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> THORN = registerProjectileEntityType(BulletEntity::new, "thorn", 0.3F, 0.3F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> METAL = registerProjectileEntityType(BulletEntity::new, "metal", 0.3F, 0.3F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> FIRE_CRACKER = registerProjectileEntityType(BulletEntity::new, "fire_cracker", 0.3F, 0.3F, true);

    /* ------------------------------------------------------------------ */
    /* 投掷弹                                                              */
    /* ------------------------------------------------------------------ */
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> CABBAGE = registerProjectileEntityType(BulletEntity::new, "cabbage", 0.4F, 0.4F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> KERNEL = registerProjectileEntityType(BulletEntity::new, "kernel", 0.4F, 0.4F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> BUTTER = registerProjectileEntityType(BulletEntity::new, "butter", 0.5F, 0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> CORN = registerProjectileEntityType(BulletEntity::new, "corn", 0.5F, 0.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> MELON = registerProjectileEntityType(BulletEntity::new, "melon", 0.8F, 0.8F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> BALL = registerProjectileEntityType(BulletEntity::new, "ball", 0.4F, 0.4F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> NUT = registerProjectileEntityType(BulletEntity::new, "nut", 0.4F, 0.4F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> POTATO = registerProjectileEntityType(BulletEntity::new, "potato", 0.4F, 0.4F);

    /* ------------------------------------------------------------------ */
    /* 特殊弹药                                                            */
    /* ------------------------------------------------------------------ */
    public static final DeferredHolder<EntityType<?>, EntityType<TargetArrowEntity>> TARGET_ARROW = registerProjectileEntityType(TargetArrowEntity::new, "target_arrow", 0.5F, 0.5F);

    /* ------------------------------------------------------------------ */
    /* 新植物弹药（BulletKind 对应 EntityType）                             */
    /* ------------------------------------------------------------------ */
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> BEE = registerProjectileEntityType(BulletEntity::new, "bee", 0.4F, 0.4F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> STONE_PEA = registerProjectileEntityType(BulletEntity::new, "stone_pea", 0.4F, 0.4F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> LIGHT_BEAM = registerProjectileEntityType(BulletEntity::new, "light_beam", 0.2F, 0.2F);
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> CHORUS_FRUIT = registerProjectileEntityType(BulletEntity::new, "chorus_fruit", 0.6F, 0.6F);


    // ===== effects =====
    public static final DeferredHolder<EntityType<?>, EntityType<OriginEffectEntity>> ORIGIN_EFFECT = registerEntityType(OriginEffectEntity::new, "origin_effect", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<DoomFixerEntity>> DOOM_FIXER = registerEntityType(DoomFixerEntity::new, "doom_fixer", MobCategory.MISC);

    // ===== misc =====
    public static final DeferredHolder<EntityType<?>, EntityType<SmallChomperEntity>> SMALL_CHOMPER = registerEntityType(SmallChomperEntity::new, "small_chomper", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<BobsleCarEntity>> BOBSLE_CAR = registerEntityType(BobsleCarEntity::new, "bobsle_car", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<PVZZombiePartEntity>> ZOMBIE_PART = registerEntityType(PVZZombiePartEntity::new, "zombie_part", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<ZombieHandEntity>> ZOMBIE_HAND = registerEntityType(ZombieHandEntity::new, "zombie_hand", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<WallNutBowlingEntity>> WALL_NUT_BOWLING = registerEntityType(WallNutBowlingEntity::new, "wall_nut_bowling", MobCategory.MISC, 1.0F, 1.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<ExplosionBowlingEntity>> EXPLOSION_BOWLING = registerEntityType(ExplosionBowlingEntity::new, "explosion_bowling", MobCategory.MISC, 1.0F, 1.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<GiantNutBowlingEntity>> GIANT_NUT_BOWLING = registerEntityType(GiantNutBowlingEntity::new, "giant_nut_bowling", MobCategory.MISC, 2.5F, 2.5F);
    public static final DeferredHolder<EntityType<?>, EntityType<LawnMowerEntity>> LAWN_MOWER = registerEntityType(LawnMowerEntity::new, "lawn_mower", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<FireCrackersEntity>> FIRE_CRACKERS = registerEntityType(FireCrackersEntity::new, "fire_crackers", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<ElementBallEntity>> ELEMENT_BALL = registerEntityType(ElementBallEntity::new, "element_ball", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<DestroyCarEntity>> DESTROY_CAR = registerEntityType(DestroyCarEntity::new, "destroy_car", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<GardenRakeEntity>> GARDEN_RAKE = registerEntityType(GardenRakeEntity::new, "garden_rake", MobCategory.MISC);
    public static final DeferredHolder<EntityType<?>, EntityType<ZombieDropBodyEntity>> ZOMBIE_DROP_BODY = registerEntityType(ZombieDropBodyEntity::new, "zombie_drop_body", MobCategory.MISC);

    // ===== animals =====
    public static final DeferredHolder<EntityType<?>, EntityType<FoodieZombieEntity>> FOODIE_ZOMBIE = registerEntityType(FoodieZombieEntity::new, "foodie_zombie", MobCategory.WATER_CREATURE);

    // ===== npc =====
    public static final DeferredHolder<EntityType<?>, EntityType<CrazyDaveEntity>> CRAZY_DAVE = registerEntityType(CrazyDaveEntity::new, "crazy_dave", MobCategory.CREATURE);
    public static final DeferredHolder<EntityType<?>, EntityType<PennyEntity>> PANNEY = registerEntityType(PennyEntity::new, "panney", MobCategory.CREATURE);
    public static final DeferredHolder<EntityType<?>, EntityType<SunDaveEntity>> SUN_DAVE = registerEntityType(SunDaveEntity::new, "sun_dave", MobCategory.CREATURE);

    // ===== zombies =====
    public static final DeferredHolder<EntityType<?>, EntityType<NormalZombieEntity>> NORMAL_ZOMBIE = registerZombieEntityType(NormalZombieEntity::new, "normal_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<FlagZombieEntity>> FLAG_ZOMBIE = registerZombieEntityType(FlagZombieEntity::new, "flag_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<ConeHeadZombieEntity>> CONEHEAD_ZOMBIE = registerZombieEntityType(ConeHeadZombieEntity::new, "conehead_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<PoleZombieEntity>> POLE_ZOMBIE = registerZombieEntityType(PoleZombieEntity::new, "pole_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<BucketHeadZombieEntity>> BUCKETHEAD_ZOMBIE = registerZombieEntityType(BucketHeadZombieEntity::new, "buckethead_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<SnorkelZombieEntity>> SNORKEL_ZOMBIE = registerZombieEntityType(SnorkelZombieEntity::new, "snorkel_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<ZomboniEntity>> ZOMBONI = registerZombieEntityType(ZomboniEntity::new, "zomboni");
    public static final DeferredHolder<EntityType<?>, EntityType<BobsleTeamEntityPVZ>> BOBSLE_TEAM = registerZombieEntityType(BobsleTeamEntityPVZ::new, "bobsle_team");
    public static final DeferredHolder<EntityType<?>, EntityType<BobsleZombieEntity>> BOBSLE_ZOMBIE = registerZombieEntityType(BobsleZombieEntity::new, "bobsle_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<ZombieDolphinEntity>> ZOMBIE_DOLPHIN = registerZombieEntityType(ZombieDolphinEntity::new, "zombie_dolphin");
    public static final DeferredHolder<EntityType<?>, EntityType<DolphinRiderEntity>> DOLPHIN_RIDER = registerZombieEntityType(DolphinRiderEntity::new, "dolphin_rider");
    public static final DeferredHolder<EntityType<?>, EntityType<DolphinRiderZombieEntity>> DOLPHIN_RIDER_ZOMBIE = registerZombieEntityType(DolphinRiderZombieEntity::new, "dolphin_rider_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<LavaZombieEntity>> LAVA_ZOMBIE = registerZombieEntityType(LavaZombieEntity::new, "lava_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<NewspaperZombieEntity>> NEWSPAPER_ZOMBIE = registerZombieEntityType(NewspaperZombieEntity::new, "newspaper_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<TombStoneEntity>> TOMB_STONE = registerZombieEntityType(TombStoneEntity::new, "tomb_stone");
    public static final DeferredHolder<EntityType<?>, EntityType<ScreenDoorZombieEntity>> SCREENDOOR_ZOMBIE = registerZombieEntityType(ScreenDoorZombieEntity::new, "screendoor_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<FootballZombieEntity>> FOOTBALL_ZOMBIE = registerZombieEntityType(FootballZombieEntity::new, "football_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<DancingZombieEntity>> DANCING_ZOMBIE = registerZombieEntityType(DancingZombieEntity::new, "dancing_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<BackupDancerEntity>> BACKUP_DANCER = registerZombieEntityType(BackupDancerEntity::new, "backup_dancer");
    public static final DeferredHolder<EntityType<?>, EntityType<GigaFootballZombieEntity>> GIGA_FOOTBALL_ZOMBIE = registerZombieEntityType(GigaFootballZombieEntity::new, "giga_football_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<PumpkinZombieEntity>> PUMPKIN_ZOMBIE = registerZombieEntityType(PumpkinZombieEntity::new, "pumpkin_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<TrickZombieEntity>> TRICK_ZOMBIE = registerZombieEntityType(TrickZombieEntity::new, "trick_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<CoffinEntity>> COFFIN = registerZombieEntityType(CoffinEntity::new, "coffin");
    public static final DeferredHolder<EntityType<?>, EntityType<MournerZombieEntity>> MOURNER_ZOMBIE = registerZombieEntityType(MournerZombieEntity::new, "mourner_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<NobleZombieEntity>> NOBLE_ZOMBIE = registerZombieEntityType(NobleZombieEntity::new, "noble_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<OldZombieEntity>> OLD_ZOMBIE = registerZombieEntityType(OldZombieEntity::new, "old_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<SundayEditionZombieEntity>> SUNDAY_EDITION_ZOMBIE = registerZombieEntityType(SundayEditionZombieEntity::new, "sunday_edition_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<JackInBoxZombieEntity>> JACK_IN_BOX_ZOMBIE = registerZombieEntityType(JackInBoxZombieEntity::new, "jack_in_box_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<PogoZombieEntity>> POGO_ZOMBIE = registerZombieEntityType(PogoZombieEntity::new, "pogo_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<YetiZombieEntity>> YETI_ZOMBIE = registerZombieEntityType(YetiZombieEntity::new, "yeti_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<DiggerZombieEntity>> DIGGER_ZOMBIE = registerZombieEntityType(DiggerZombieEntity::new, "digger_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<BalloonZombieEntity>> BALLOON_ZOMBIE = registerZombieEntityType(BalloonZombieEntity::new, "balloon_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<RaZombieEntity>> RA_ZOMBIE = registerZombieEntityType(RaZombieEntity::new, "ra_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<BungeeZombieEntity>> BUNGEE_ZOMBIE = registerZombieEntityType(BungeeZombieEntity::new, "bungee_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<LadderZombieEntity>> LADDER_ZOMBIE = registerZombieEntityType(LadderZombieEntity::new, "ladder_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<CatapultZombieEntity>> CATAPULT_ZOMBIE = registerZombieEntityType(CatapultZombieEntity::new, "catapult_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<GargantuarEntity>> GARGANTUAR = registerZombieEntityType(GargantuarEntity::new, "gargantuar");
    public static final DeferredHolder<EntityType<?>, EntityType<ImpEntity>> IMP = registerZombieEntityType(ImpEntity::new, "imp");
    public static final DeferredHolder<EntityType<?>, EntityType<GigaGargantuarEntity>> GIGA_GARGANTUAR = registerZombieEntityType(GigaGargantuarEntity::new, "giga_gargantuar");
    public static final DeferredHolder<EntityType<?>, EntityType<Edgar090505Entity>> EDGAR_090505 = registerZombieEntityType(Edgar090505Entity::new, "edgar_090505");

    public static final DeferredHolder<EntityType<?>, EntityType<SphinxInatorEntity>> SPHINX_INATOR =
            registerZombieEntityType(SphinxInatorEntity::new, "sphinx_inator");
    public static final DeferredHolder<EntityType<?>, EntityType<ZombotMissileEntity>> ZOMBOT_MISSILE =
            registerProjectileEntityType(ZombotMissileEntity::new, "zombot_missile", 0.6F, 0.6F);

    // ===== PvZ2 四足机甲 BOSS =====
    public static final DeferredHolder<EntityType<?>, EntityType<PlankWalkerEntity>> PLANK_WALKER =
            registerZombieEntityType(PlankWalkerEntity::new, "plank_walker");
    public static final DeferredHolder<EntityType<?>, EntityType<WarWagonEntity>> WAR_WAGON =
            registerZombieEntityType(WarWagonEntity::new, "war_wagon");
    public static final DeferredHolder<EntityType<?>, EntityType<TomorrowTronEntity>> TOMORROW_TRON =
            registerZombieEntityType(TomorrowTronEntity::new, "tomorrow_tron");

    public static final DeferredHolder<EntityType<?>, EntityType<Edgar090517Entity>> EDGAR_090517 = registerZombieEntityType(Edgar090517Entity::new, "edgar_090517");
    public static final DeferredHolder<EntityType<?>, EntityType<PeaShooterZombieEntity>> PEASHOOTER_ZOMBIE = registerZombieEntityType(PeaShooterZombieEntity::new, "peashooter_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<GatlingPeaZombieEntity>> GATLINGPEA_ZOMBIE = registerZombieEntityType(GatlingPeaZombieEntity::new, "gatlingpea_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<SquashZombieEntity>> SQUASH_ZOMBIE = registerZombieEntityType(SquashZombieEntity::new, "squash_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<JalapenoZombieEntity>> JALAPENO_ZOMBIE = registerZombieEntityType(JalapenoZombieEntity::new, "jalapeno_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<WallNutZombieEntity>> WALLNUT_ZOMBIE = registerZombieEntityType(WallNutZombieEntity::new, "wallnut_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<TallNutZombieEntity>> TALLNUT_ZOMBIE = registerZombieEntityType(TallNutZombieEntity::new, "tallnut_zombie");
    public static final DeferredHolder<EntityType<?>, EntityType<GigaTombStoneEntity>> GIGA_TOMB_STONE = registerZombieEntityType(GigaTombStoneEntity::new, "giga_tomb_stone");
    public static final DeferredHolder<EntityType<?>, EntityType<Edgar090547Entity>> EDGAR_090547 = registerZombieEntityType(Edgar090547Entity::new, "edgar_090547"); // 埃德加-090547
    public static final DeferredHolder<EntityType<?>, EntityType<FlowerPotZombieEntity>> FLOWER_POT_ZOMBIE = registerZombieEntityType(FlowerPotZombieEntity::new, "flower_pot_zombie"); // 花盆僵尸
    public static final DeferredHolder<EntityType<?>, EntityType<AirborneZombieEntity>> AIRBORNE_ZOMBIE = registerZombieEntityType(AirborneZombieEntity::new, "airborne_zombie"); // 空降僵尸
    public static final DeferredHolder<EntityType<?>, EntityType<MCZombieEntity>> MC_ZOMBIE = registerZombieEntityType(MCZombieEntity::new, "mc_zombie"); // MC僵尸
    public static final DeferredHolder<EntityType<?>, EntityType<SteelPumpkinZombieEntity>> STEEL_PUMPKIN_ZOMBIE = registerZombieEntityType(SteelPumpkinZombieEntity::new, "steel_pumpkin_zombie"); // 钢南瓜僵尸
    public static final DeferredHolder<EntityType<?>, EntityType<TargetArrowZombieEntity>> TARGET_ARROW_ZOMBIE = registerZombieEntityType(TargetArrowZombieEntity::new, "target_arrow_zombie"); // 箭靶僵尸
    public static final DeferredHolder<EntityType<?>, EntityType<RedEdgeZombieEntity>> RED_EDGE_ZOMBIE = registerZombieEntityType(RedEdgeZombieEntity::new, "red_edge_zombie"); // 红刀僵尸
    public static final DeferredHolder<EntityType<?>, EntityType<MJZombieEntity>> MJ_ZOMBIE = registerZombieEntityType(MJZombieEntity::new, "mj_zombie"); // 舞王僵尸
    public static final DeferredHolder<EntityType<?>, EntityType<DancerBackupEntity>> DANCER_BACKUP_ZOMBIE = registerZombieEntityType(DancerBackupEntity::new, "dancer_backup_zombie"); // 伴舞僵尸
    public static final DeferredHolder<EntityType<?>, EntityType<SunFlowerZombieEntity>> SUN_FLOWER_ZOMBIE = registerZombieEntityType(SunFlowerZombieEntity::new, "sun_flower_zombie"); // 太阳花僵尸
    public static final DeferredHolder<EntityType<?>, EntityType<ChomperZombieEntity>> CHOMPER_ZOMBIE = registerZombieEntityType(ChomperZombieEntity::new, "chomper_zombie"); // 大嘴花僵尸


    // ===== plants =====
    public static final DeferredHolder<EntityType<?>, EntityType<PeaShooterEntity>> PEA_SHOOTER = registerPlantEntityType(PeaShooterEntity::new, "pea_shooter");
    public static final DeferredHolder<EntityType<?>, EntityType<SunFlowerEntity>> SUN_FLOWER = registerPlantEntityType(SunFlowerEntity::new, "sun_flower");
    public static final DeferredHolder<EntityType<?>, EntityType<CherryBombEntity>> CHERRY_BOMB = registerPlantEntityType(CherryBombEntity::new, "cherry_bomb");
    public static final DeferredHolder<EntityType<?>, EntityType<WallNutEntity>> WALL_NUT = registerPlantEntityType(WallNutEntity::new, "wall_nut");
    public static final DeferredHolder<EntityType<?>, EntityType<ExplodeONutEntity>> EXPLODE_O_NUT = registerPlantEntityType(ExplodeONutEntity::new, "explode_o_nut");
    public static final DeferredHolder<EntityType<?>, EntityType<GiantNutEntity>> GIANT_NUT = registerPlantEntityType(GiantNutEntity::new, "giant_nut");
    public static final DeferredHolder<EntityType<?>, EntityType<PotatoMineEntity>> POTATO_MINE = registerPlantEntityType(PotatoMineEntity::new, "potato_mine");
    public static final DeferredHolder<EntityType<?>, EntityType<SnowPeaEntity>> SNOW_PEA = registerPlantEntityType(SnowPeaEntity::new, "snow_pea");
    public static final DeferredHolder<EntityType<?>, EntityType<ChomperEntity>> CHOMPER = registerPlantEntityType(ChomperEntity::new, "chomper");
    public static final DeferredHolder<EntityType<?>, EntityType<RepeaterEntity>> REPEATER = registerPlantEntityType(RepeaterEntity::new, "repeater");
    public static final DeferredHolder<EntityType<?>, EntityType<LilyPadEntity>> LILY_PAD = registerPlantEntityType(LilyPadEntity::new, "lily_pad");
    public static final DeferredHolder<EntityType<?>, EntityType<SquashEntity>> SQUASH = registerPlantEntityType(SquashEntity::new, "squash");
    public static final DeferredHolder<EntityType<?>, EntityType<ThreePeaterEntity>> THREE_PEATER = registerPlantEntityType(ThreePeaterEntity::new, "three_peater");
    public static final DeferredHolder<EntityType<?>, EntityType<TangleKelpEntity>> TANGLE_KELP = registerPlantEntityType(TangleKelpEntity::new, "tangle_kelp");
    public static final DeferredHolder<EntityType<?>, EntityType<JalapenoEntity>> JALAPENO = registerPlantEntityType(JalapenoEntity::new, "jalapeno");
    public static final DeferredHolder<EntityType<?>, EntityType<SpikeWeedEntity>> SPIKE_WEED = registerPlantEntityType(SpikeWeedEntity::new, "spike_weed");
    public static final DeferredHolder<EntityType<?>, EntityType<TorchWoodEntity>> TORCH_WOOD = registerPlantEntityType(TorchWoodEntity::new, "torch_wood");
    public static final DeferredHolder<EntityType<?>, EntityType<TallNutEntity>> TALL_NUT = registerPlantEntityType(TallNutEntity::new, "tall_nut");
    public static final DeferredHolder<EntityType<?>, EntityType<PuffShroomEntity>> PUFF_SHROOM = registerPlantEntityType(PuffShroomEntity::new, "puff_shroom");
    public static final DeferredHolder<EntityType<?>, EntityType<SunShroomEntity>> SUN_SHROOM = registerPlantEntityType(SunShroomEntity::new, "sun_shroom");
    public static final DeferredHolder<EntityType<?>, EntityType<FumeShroomEntity>> FUME_SHROOM = registerPlantEntityType(FumeShroomEntity::new, "fume_shroom");
    public static final DeferredHolder<EntityType<?>, EntityType<GraveBusterEntity>> GRAVE_BUSTER = registerPlantEntityType(GraveBusterEntity::new, "grave_buster");
    public static final DeferredHolder<EntityType<?>, EntityType<HypnoShroomEntity>> HYPNO_SHROOM = registerPlantEntityType(HypnoShroomEntity::new, "hypno_shroom");
    public static final DeferredHolder<EntityType<?>, EntityType<ScaredyShroomEntity>> SCAREDY_SHROOM = registerPlantEntityType(ScaredyShroomEntity::new, "scaredy_shroom");
    public static final DeferredHolder<EntityType<?>, EntityType<IceShroomEntity>> ICE_SHROOM = registerPlantEntityType(IceShroomEntity::new, "ice_shroom");
    public static final DeferredHolder<EntityType<?>, EntityType<DoomShroomEntity>> DOOM_SHROOM = registerPlantEntityType(DoomShroomEntity::new, "doom_shroom");
    public static final DeferredHolder<EntityType<?>, EntityType<SeaShroomEntity>> SEA_SHROOM = registerPlantEntityType(SeaShroomEntity::new, "sea_shroom");
    public static final DeferredHolder<EntityType<?>, EntityType<SplitPeaEntity>> SPLIT_PEA = registerPlantEntityType(SplitPeaEntity::new, "split_pea");
    public static final DeferredHolder<EntityType<?>, EntityType<CoffeeBeanEntity>> COFFEE_BEAN = registerPlantEntityType(CoffeeBeanEntity::new, "coffee_bean");
    public static final DeferredHolder<EntityType<?>, EntityType<MariGoldEntity>> MARIGOLD = registerPlantEntityType(MariGoldEntity::new, "marigold");
    public static final DeferredHolder<EntityType<?>, EntityType<GatlingPeaEntity>> GATLING_PEA = registerPlantEntityType(GatlingPeaEntity::new, "gatling_pea");
    public static final DeferredHolder<EntityType<?>, EntityType<TwinSunFlowerEntity>> TWIN_SUNFLOWER = registerPlantEntityType(TwinSunFlowerEntity::new, "twin_sunflower");
    public static final DeferredHolder<EntityType<?>, EntityType<WaterGuardEntity>> WATER_GUARD = registerPlantEntityType(WaterGuardEntity::new, "water_guard");
    public static final DeferredHolder<EntityType<?>, EntityType<PumpkinEntity>> PUMPKIN = registerPlantEntityType(PumpkinEntity::new, "pumpkin");
    public static final DeferredHolder<EntityType<?>, EntityType<PlanternEntity>> PLANTERN = registerPlantEntityType(PlanternEntity::new, "plantern");
    public static final DeferredHolder<EntityType<?>, EntityType<MagnetShroomEntity>> MAGNET_SHROOM = registerPlantEntityType(MagnetShroomEntity::new, "magnet_shroom");
    public static final DeferredHolder<EntityType<?>, EntityType<CatTailEntity>> CAT_TAIL = registerPlantEntityType(CatTailEntity::new, "cat_tail");
    public static final DeferredHolder<EntityType<?>, EntityType<StrangeCatEntity>> STRANGE_CAT = registerPlantEntityType(StrangeCatEntity::new, "strange_cat");
    public static final DeferredHolder<EntityType<?>, EntityType<StarFruitEntity>> STAR_FRUIT = registerPlantEntityType(StarFruitEntity::new, "star_fruit");
    public static final DeferredHolder<EntityType<?>, EntityType<AngelStarFruitEntity>> ANGEL_STAR_FRUIT = registerPlantEntityType(AngelStarFruitEntity::new, "angel_star_fruit");
    public static final DeferredHolder<EntityType<?>, EntityType<CactusEntity>> CACTUS = registerPlantEntityType(CactusEntity::new, "cactus");
    public static final DeferredHolder<EntityType<?>, EntityType<BloverEntity>> BLOVER = registerPlantEntityType(BloverEntity::new, "blover");
    public static final DeferredHolder<EntityType<?>, EntityType<GloomShroomEntity>> GLOOM_SHROOM = registerPlantEntityType(GloomShroomEntity::new, "gloom_shroom");
    public static final DeferredHolder<EntityType<?>, EntityType<GoldMagnetEntity>> GOLD_MAGNET = registerPlantEntityType(GoldMagnetEntity::new, "gold_magnet");
    public static final DeferredHolder<EntityType<?>, EntityType<GoldLeafEntity>> GOLD_LEAF = registerPlantEntityType(GoldLeafEntity::new, "gold_leaf");
    public static final DeferredHolder<EntityType<?>, EntityType<FlowerPotEntity>> FLOWER_POT = registerPlantEntityType(FlowerPotEntity::new, "flower_pot");
    public static final DeferredHolder<EntityType<?>, EntityType<CabbagePultEntity>> CABBAGE_PULT = registerPlantEntityType(CabbagePultEntity::new, "cabbage_pult");
    public static final DeferredHolder<EntityType<?>, EntityType<KernelPultEntity>> KERNEL_PULT = registerPlantEntityType(KernelPultEntity::new, "kernel_pult");
    public static final DeferredHolder<EntityType<?>, EntityType<ButterPultEntity>> BUTTER_PULT = registerPlantEntityType(ButterPultEntity::new, "butter_pult");
    public static final DeferredHolder<EntityType<?>, EntityType<GarlicEntity>> GARLIC = registerPlantEntityType(GarlicEntity::new, "garlic");
    public static final DeferredHolder<EntityType<?>, EntityType<UmbrellaLeafEntity>> UMBRELLA_LEAF = registerPlantEntityType(UmbrellaLeafEntity::new, "umbrella_leaf");
    public static final DeferredHolder<EntityType<?>, EntityType<MelonPultEntity>> MELON_PULT = registerPlantEntityType(MelonPultEntity::new, "melon_pult");
    public static final DeferredHolder<EntityType<?>, EntityType<WinterMelonEntity>> WINTER_MELON = registerPlantEntityType(WinterMelonEntity::new, "winter_melon");
    public static final DeferredHolder<EntityType<?>, EntityType<BambooLordEntity>> BAMBOO_LORD = registerPlantEntityType(BambooLordEntity::new, "bamboo_lord");
    public static final DeferredHolder<EntityType<?>, EntityType<IcebergLettuceEntity>> ICEBERG_LETTUCE = registerPlantEntityType(IcebergLettuceEntity::new, "iceberg_lettuce");
    public static final DeferredHolder<EntityType<?>, EntityType<SpikeRockEntity>> SPIKE_ROCK = registerPlantEntityType(SpikeRockEntity::new, "spike_rock");
    public static final DeferredHolder<EntityType<?>, EntityType<BonkChoyEntity>> BONK_CHOY = registerPlantEntityType(BonkChoyEntity::new, "bonk_choy");
    public static final DeferredHolder<EntityType<?>, EntityType<ImitaterEntity>> IMITATER = registerPlantEntityType(ImitaterEntity::new, "imitater");
    public static final DeferredHolder<EntityType<?>, EntityType<CobCannonEntity>> COB_CANNON = registerPlantEntityType(CobCannonEntity::new, "cob_cannon");
    public static final DeferredHolder<EntityType<?>, EntityType<SteelPumpkinEntity>> STEEL_PUMPKIN = registerPlantEntityType(SteelPumpkinEntity::new, "steel_pumpkin"); // 钢南瓜
    public static final DeferredHolder<EntityType<?>, EntityType<IceCabbagePultEntity>> ICE_CABBAGE_PULT = registerPlantEntityType(IceCabbagePultEntity::new, "ice_cabbage_pult"); // 冰卷心菜投手
    public static final DeferredHolder<EntityType<?>, EntityType<FirePeashooterEntity>> FIRE_PEASHOOTER = registerPlantEntityType(FirePeashooterEntity::new, "fire_peashooter"); // 火焰豌豆
    public static final DeferredHolder<EntityType<?>, EntityType<ChorusFruitPultEntity>> CHORUS_FRUIT_PULT = registerPlantEntityType(ChorusFruitPultEntity::new, "chorus_fruit_pult"); // 紫颂果投手
    public static final DeferredHolder<EntityType<?>, EntityType<RotateRadishEntity>> ROTATE_RADISH = registerPlantEntityType(RotateRadishEntity::new, "rotate_radish"); // 转转萝卜
    public static final DeferredHolder<EntityType<?>, EntityType<BurstKernelPultEntity>> BURST_KERNEL_PULT = registerPlantEntityType(BurstKernelPultEntity::new, "burst_kernel_pult"); // 爆裂玉米投手
    public static final DeferredHolder<EntityType<?>, EntityType<BlazeWartEntity>> BLAZE_WART = registerPlantEntityType(BlazeWartEntity::new, "blaze_wart"); // 烈焰庞
    public static final DeferredHolder<EntityType<?>, EntityType<GrassCarpEntity>> GRASS_CARP = registerPlantEntityType(GrassCarpEntity::new, "grass_carp");//草鱼
    public static final DeferredHolder<EntityType<?>, EntityType<SelfImitaterEntity>> SELF_IMITATER = registerPlantEntityType(SelfImitaterEntity::new, "self_imitater");//自己模仿者
    public static final DeferredHolder<EntityType<?>, EntityType<ReIcePeaEntity>> RE_ICEPEA = registerPlantEntityType(ReIcePeaEntity::new, "re_icepea");//双发寒冰
    public static final DeferredHolder<EntityType<?>, EntityType<PeaPodEntity>> PEA_POD = registerPlantEntityType(PeaPodEntity::new, "peapod");//豌豆荚
    public static final DeferredHolder<EntityType<?>, EntityType<SculkShroomEntity>> SCULK_SHROOM = registerPlantEntityType(SculkShroomEntity::new, "sculk_shroom");//音爆菇
    public static final DeferredHolder<EntityType<?>, EntityType<OriginShroomEntity>> ORIGIN_SHROOM = registerPlantEntityType(OriginShroomEntity::new, "origin_shroom");//原始蘑菇
    public static final DeferredHolder<EntityType<?>, EntityType<BeeShooterEntity>> BEE_SHOOTER = registerPlantEntityType(BeeShooterEntity::new, "bee_shooter");//蜜蜂射手
    public static final DeferredHolder<EntityType<?>, EntityType<FodderBushEntity>> FODDER_BUSH = registerPlantEntityType(FodderBushEntity::new, "fodder_bush");//炮灰灌
    public static final DeferredHolder<EntityType<?>, EntityType<GoldenMelonPultEntity>> GOLDEN_MELON_PULT = registerPlantEntityType(GoldenMelonPultEntity::new, "golden_melon_pult");//金西瓜
    public static final DeferredHolder<EntityType<?>, EntityType<PrimalPeaShooterEntity>> PRIMAL_PEA_SHOOTER = registerPlantEntityType(PrimalPeaShooterEntity::new, "primal_pea_shooter");//原始豌豆
    public static final DeferredHolder<EntityType<?>, EntityType<GooPeaShooterEntity>> GOO_PEA_SHOOTER = registerPlantEntityType(GooPeaShooterEntity::new, "goo_pea_shooter");//毒液豌豆
    public static final DeferredHolder<EntityType<?>, EntityType<MagnifyingGrassEntity>> MAGNIFYING_GRASS = registerPlantEntityType(MagnifyingGrassEntity::new, "magnifying_grass");//棱镜草


    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerProjectileEntityType(
            EntityFactory<T> factory, String name, float width, float height) {
        return registerProjectileEntityType(factory, name, width, height, false);
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerProjectileEntityType(
            EntityFactory<T> factory, String name, float width, float height, boolean fireImmune) {
        final ResourceKey<EntityType<?>> key =
                ResourceKey.create(Registries.ENTITY_TYPE, StringUtil.id(name));
        return ENTITY_TYPES.register(name, () -> {
            final EntityType.Builder<T> builder = EntityType.Builder
                    .<T>of(factory, MobCategory.MISC)
                    .sized(width, height)
                    .clientTrackingRange(4)
                    .updateInterval(2);
            if (fireImmune) {
                builder.fireImmune();
            }
            return builder.build(key);
        });
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntityType(EntityFactory<T> factory, String name, MobCategory classification) {
        final ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, StringUtil.id(name));
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, classification).build(key));
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntityType(EntityFactory<T> factory, String name, MobCategory classification, float w, float h) {
        final ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, StringUtil.id(name));
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, classification).sized(w, h).build(key));
    }

    private static <T extends PVZZombieEntity> DeferredHolder<EntityType<?>, EntityType<T>> registerZombieEntityType(
            EntityFactory<T> factory, String name) {
        final ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, StringUtil.id(name));
        return ENTITY_TYPES.register(name, () ->
                EntityType.Builder.of(factory, PvZEnumProxy.PVZ_ZOMBIE_PROXY.getValue())
                        .fireImmune()
                        .build(key));
    }

    private static <T extends PlantEntity> DeferredHolder<EntityType<?>, EntityType<T>> registerPlantEntityType(
            EntityFactory<T> factory, String name) {
        final ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, StringUtil.id(name));
        return ENTITY_TYPES.register(name, () ->
                EntityType.Builder.of(factory, PvZEnumProxy.PVZ_PLANT_PROXY.getValue())
                        .build(key));
    }
}