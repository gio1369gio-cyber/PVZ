package com.lying.pvz.client;

import com.lying.pvz.client.render.renderer.blockentity.EssenceAltarBlockEntityRenderer;
import com.lying.pvz.client.render.renderer.blockentity.SunConverterBlockEntityRenderer;
import com.lying.pvz.client.render.renderer.blockentity.SunFlowerTrophyBlockEntityRenderer;
import com.lying.pvz.client.render.renderer.entity.bullet.*;
import com.lying.pvz.client.render.renderer.entity.creature.FoodieZombieRender;
import com.lying.pvz.client.render.renderer.entity.effects.DoomFixerRender;
import com.lying.pvz.client.render.renderer.entity.effects.OriginEffectRender;
import com.lying.pvz.client.render.renderer.entity.misc.*;
import com.lying.pvz.client.render.renderer.entity.misc.bowling.ExplosionBowlingRender;
import com.lying.pvz.client.render.renderer.entity.misc.bowling.GiantNutBowlingRender;
import com.lying.pvz.client.render.renderer.entity.misc.bowling.WallNutBowlingRender;
import com.lying.pvz.client.render.renderer.entity.misc.drop.*;
import com.lying.pvz.client.render.renderer.entity.npc.CrazyDaveRender;
import com.lying.pvz.client.render.renderer.entity.npc.PennyRender;
import com.lying.pvz.client.render.renderer.entity.npc.SunDaveRender;
import com.lying.pvz.client.render.renderer.entity.plant.appease.*;
import com.lying.pvz.client.render.renderer.entity.plant.arma.*;
import com.lying.pvz.client.render.renderer.entity.plant.assist.*;
import com.lying.pvz.client.render.renderer.entity.plant.defence.*;
import com.lying.pvz.client.render.renderer.entity.plant.electric.MagnifyingGrassRender;
import com.lying.pvz.client.render.renderer.entity.plant.enforce.*;
import com.lying.pvz.client.render.renderer.entity.plant.explosion.*;
import com.lying.pvz.client.render.renderer.entity.plant.fire.BlazeWartRender;
import com.lying.pvz.client.render.renderer.entity.plant.fire.FirePeashooterRender;
import com.lying.pvz.client.render.renderer.entity.plant.fire.JalapenoRender;
import com.lying.pvz.client.render.renderer.entity.plant.fire.TorchWoodRender;
import com.lying.pvz.client.render.renderer.entity.plant.ice.*;
import com.lying.pvz.client.render.renderer.entity.plant.light.*;
import com.lying.pvz.client.render.renderer.entity.plant.magic.*;
import com.lying.pvz.client.render.renderer.entity.plant.spear.CactusRender;
import com.lying.pvz.client.render.renderer.entity.plant.spear.CatTailRender;
import com.lying.pvz.client.render.renderer.entity.plant.spear.SpikeRockRender;
import com.lying.pvz.client.render.renderer.entity.plant.spear.SpikeWeedRender;
import com.lying.pvz.client.render.renderer.entity.plant.toxic.*;
import com.lying.pvz.client.render.renderer.entity.zombie.body.ZombieBodyRender;
import com.lying.pvz.client.render.renderer.entity.zombie.custom.*;
import com.lying.pvz.client.render.renderer.entity.zombie.grass.*;
import com.lying.pvz.client.render.renderer.entity.zombie.other.*;
import com.lying.pvz.client.render.renderer.entity.zombie.pool.*;
import com.lying.pvz.client.render.renderer.entity.zombie.roof.*;
import com.lying.pvz.client.render.renderer.entity.zombie.zombotany.*;
import com.lying.pvz.common.init.PvZBlockEntityRegister;
import com.lying.pvz.common.init.PvZEntityRegister;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class PvZEntityRenderer {
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        registerBlockEntityRenderers(event);
        registerEntityRenderers(event);
    }


    private static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(PvZBlockEntityRegister.SUN_CONVERTER.get(), SunConverterBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(PvZBlockEntityRegister.SUNFLOWER_TROPHY.get(), SunFlowerTrophyBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(PvZBlockEntityRegister.ESSENCE_ALTAR.get(), EssenceAltarBlockEntityRenderer::new);
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        registerDrops(event);
        registerBullets(event);
        registerEffects(event);
        registerMisc(event);
        registerAnimals(event);
        registerNpcs(event);
        registerZombies(event);
        registerPlants(event);
    }

    private static void registerDrops(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PvZEntityRegister.SUN.get(), SunRender::new);
        event.registerEntityRenderer(PvZEntityRegister.COIN.get(), CoinRender::new);
        event.registerEntityRenderer(PvZEntityRegister.JEWEL.get(), JewelRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ENERGY.get(), EnergyRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GIFT_BOX.get(), GiftBoxRender::new);
        event.registerEntityRenderer(PvZEntityRegister.RED_SUN.get(), RedSunRender::new);
    }

    private static void registerBullets(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PvZEntityRegister.PEA.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.POTATO.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.SPORE.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.FUME.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.METAL.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.THORN.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.STAR.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.CABBAGE.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.KERNEL.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.BUTTER.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.TARGET_ARROW.get(), TargetArrowRender::new);
        event.registerEntityRenderer(PvZEntityRegister.MELON.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.FIRE_CRACKER.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.BALL.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.CORN.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.NUT.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.BEE.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.STONE_PEA.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.LIGHT_BEAM.get(), BulletRenderer::new);
        event.registerEntityRenderer(PvZEntityRegister.CHORUS_FRUIT.get(), BulletRenderer::new);
    }

    private static void registerEffects(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PvZEntityRegister.ORIGIN_EFFECT.get(), OriginEffectRender::new);
        event.registerEntityRenderer(PvZEntityRegister.DOOM_FIXER.get(), DoomFixerRender::new);
    }

    private static void registerMisc(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PvZEntityRegister.SMALL_CHOMPER.get(), SmallChomperRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BOBSLE_CAR.get(), BobsleCarRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ZOMBIE_PART.get(), EmptyRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ZOMBIE_HAND.get(), ZombieHandRender::new);
        event.registerEntityRenderer(PvZEntityRegister.WALL_NUT_BOWLING.get(), WallNutBowlingRender::new);
        event.registerEntityRenderer(PvZEntityRegister.EXPLOSION_BOWLING.get(), ExplosionBowlingRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GIANT_NUT_BOWLING.get(), GiantNutBowlingRender::new);
        event.registerEntityRenderer(PvZEntityRegister.LAWN_MOWER.get(), LawnMowerRender::new);
        event.registerEntityRenderer(PvZEntityRegister.FIRE_CRACKERS.get(), FireCrackersRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ELEMENT_BALL.get(), ElementBallRender::new);
        event.registerEntityRenderer(PvZEntityRegister.DESTROY_CAR.get(), DestroyCarRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GARDEN_RAKE.get(), GardenRakeRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ZOMBIE_DROP_BODY.get(), ZombieBodyRender::new);
    }

    private static void registerAnimals(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PvZEntityRegister.FOODIE_ZOMBIE.get(), FoodieZombieRender::new);
    }

    private static void registerNpcs(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PvZEntityRegister.CRAZY_DAVE.get(), CrazyDaveRender::new);
        event.registerEntityRenderer(PvZEntityRegister.PANNEY.get(), PennyRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SUN_DAVE.get(), SunDaveRender::new);
    }

    private static void registerZombies(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PvZEntityRegister.NORMAL_ZOMBIE.get(), NormalZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.FLAG_ZOMBIE.get(), FlagZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.CONEHEAD_ZOMBIE.get(), ConeHeadZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.POLE_ZOMBIE.get(), PoleZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BUCKETHEAD_ZOMBIE.get(), BucketHeadZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SNORKEL_ZOMBIE.get(), SnorkelZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ZOMBONI.get(), ZomboniRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BOBSLE_TEAM.get(), BobsleTeamRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BOBSLE_ZOMBIE.get(), BobsleZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ZOMBIE_DOLPHIN.get(), ZombieDolphinRender::new);
        event.registerEntityRenderer(PvZEntityRegister.DOLPHIN_RIDER.get(), DolphinRiderRender::new);
        event.registerEntityRenderer(PvZEntityRegister.DOLPHIN_RIDER_ZOMBIE.get(), DolphinRiderZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.LAVA_ZOMBIE.get(), LavaZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.NEWSPAPER_ZOMBIE.get(), NewspaperZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.TOMB_STONE.get(), TombStoneRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SCREENDOOR_ZOMBIE.get(), ScreenDoorZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.FOOTBALL_ZOMBIE.get(), FootballZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GIGA_FOOTBALL_ZOMBIE.get(), GigaFootballZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.DANCING_ZOMBIE.get(), DancingZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BACKUP_DANCER.get(), BackupDancerRender::new);
        event.registerEntityRenderer(PvZEntityRegister.PUMPKIN_ZOMBIE.get(), PumpkinZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.TRICK_ZOMBIE.get(), TrickZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.COFFIN.get(), CoffinRender::new);
        event.registerEntityRenderer(PvZEntityRegister.MOURNER_ZOMBIE.get(), MournerZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.NOBLE_ZOMBIE.get(), NobleZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.OLD_ZOMBIE.get(), OldZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SUNDAY_EDITION_ZOMBIE.get(), SundayEditionZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.JACK_IN_BOX_ZOMBIE.get(), JackInBoxZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.POGO_ZOMBIE.get(), PogoZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.YETI_ZOMBIE.get(), YetiZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.DIGGER_ZOMBIE.get(), DiggerZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BALLOON_ZOMBIE.get(), BalloonZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.RA_ZOMBIE.get(), RaZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BUNGEE_ZOMBIE.get(), BungeeZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.LADDER_ZOMBIE.get(), LadderZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.CATAPULT_ZOMBIE.get(), CatapultZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GARGANTUAR.get(), GargantuarRender::new);
        event.registerEntityRenderer(PvZEntityRegister.IMP.get(), ImpRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GIGA_GARGANTUAR.get(), GigaGargantuarRender::new);
        event.registerEntityRenderer(PvZEntityRegister.EDGAR_090505.get(), EdgarRobotRender::new);
        event.registerEntityRenderer(PvZEntityRegister.PEASHOOTER_ZOMBIE.get(), PeaShooterZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GATLINGPEA_ZOMBIE.get(), GatlingPeaZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SQUASH_ZOMBIE.get(), SquashZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.JALAPENO_ZOMBIE.get(), JalapenoZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.WALLNUT_ZOMBIE.get(), WallNutZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.TALLNUT_ZOMBIE.get(), TallNutZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GIGA_TOMB_STONE.get(), GigaTombStoneRender::new);
        event.registerEntityRenderer(PvZEntityRegister.EDGAR_090517.get(), EdgarRobotRender::new);
        event.registerEntityRenderer(PvZEntityRegister.AIRBORNE_ZOMBIE.get(), AirborneZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.EDGAR_090547.get(), Edgar090547Render::new);
        event.registerEntityRenderer(PvZEntityRegister.SPHINX_TERMINATOR.get(), com.lying.pvz.client.render.renderer.entity.zombie.boss.SphinxTerminatorRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SPHINX_MISSILE.get(), com.lying.pvz.client.render.renderer.entity.zombie.boss.SphinxMissileRender::new);
        event.registerEntityRenderer(PvZEntityRegister.RED_EDGE_ZOMBIE.get(), RedEdgeZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.DANCER_BACKUP_ZOMBIE.get(), DancerBackupRender::new);
        event.registerEntityRenderer(PvZEntityRegister.MJ_ZOMBIE.get(), MJZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.MC_ZOMBIE.get(), MCZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.FLOWER_POT_ZOMBIE.get(), FlowerPotZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.STEEL_PUMPKIN_ZOMBIE.get(), SteelPumpkinZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.CHOMPER_ZOMBIE.get(), ChomperZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SUN_FLOWER_ZOMBIE.get(), SunFlowerZombieRender::new);
        event.registerEntityRenderer(PvZEntityRegister.TARGET_ARROW_ZOMBIE.get(), TargetArrowZombieRender::new);
    }

    private static void registerPlants(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PvZEntityRegister.PEA_SHOOTER.get(), PeaShooterRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SUN_FLOWER.get(), SunFlowerRender::new);
        event.registerEntityRenderer(PvZEntityRegister.CHERRY_BOMB.get(), CherryBombRender::new);
        event.registerEntityRenderer(PvZEntityRegister.WALL_NUT.get(), WallNutRender::new);
        event.registerEntityRenderer(PvZEntityRegister.EXPLODE_O_NUT.get(), ExplodeNutRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GIANT_NUT.get(),TallNutRender::new);
        event.registerEntityRenderer(PvZEntityRegister.POTATO_MINE.get(), PotatoMineRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SNOW_PEA.get(), SnowPeaRender::new);
        event.registerEntityRenderer(PvZEntityRegister.CHOMPER.get(), ChomperRender::new);
        event.registerEntityRenderer(PvZEntityRegister.REPEATER.get(), RepeaterRender::new);
        event.registerEntityRenderer(PvZEntityRegister.LILY_PAD.get(), LilyPadRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SQUASH.get(), SquashRender::new);
        event.registerEntityRenderer(PvZEntityRegister.THREE_PEATER.get(), ThreePeaterRender::new);
        event.registerEntityRenderer(PvZEntityRegister.TANGLE_KELP.get(), TangleKelpRender::new);
        event.registerEntityRenderer(PvZEntityRegister.JALAPENO.get(), JalapenoRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SPIKE_WEED.get(), SpikeWeedRender::new);
        event.registerEntityRenderer(PvZEntityRegister.TORCH_WOOD.get(), TorchWoodRender::new);
        event.registerEntityRenderer(PvZEntityRegister.TALL_NUT.get(), TallNutRender::new);

        event.registerEntityRenderer(PvZEntityRegister.PUFF_SHROOM.get(), PuffShroomRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SUN_SHROOM.get(), SunShroomRender::new);
        event.registerEntityRenderer(PvZEntityRegister.FUME_SHROOM.get(), FumeShroomRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GRAVE_BUSTER.get(), GraveBusterRender::new);
        event.registerEntityRenderer(PvZEntityRegister.HYPNO_SHROOM.get(), HypnoShroomRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SCAREDY_SHROOM.get(), ScaredyShroomRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ICE_SHROOM.get(), IceShroomRender::new);
        event.registerEntityRenderer(PvZEntityRegister.DOOM_SHROOM.get(), DoomShroomRender::new);

        event.registerEntityRenderer(PvZEntityRegister.SEA_SHROOM.get(), SeaShroomRender::new);
        event.registerEntityRenderer(PvZEntityRegister.PLANTERN.get(), PlanternRender::new);
        event.registerEntityRenderer(PvZEntityRegister.CACTUS.get(), CactusRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BLOVER.get(), BloverRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SPLIT_PEA.get(), SplitPeaRender::new);
        event.registerEntityRenderer(PvZEntityRegister.STAR_FRUIT.get(), StarFruitRender::new);
        event.registerEntityRenderer(PvZEntityRegister.PUMPKIN.get(), PumpkinRender::new);
        event.registerEntityRenderer(PvZEntityRegister.MAGNET_SHROOM.get(), MagnetShroomRender::new);

        event.registerEntityRenderer(PvZEntityRegister.CABBAGE_PULT.get(), CabbagePultRender::new);
        event.registerEntityRenderer(PvZEntityRegister.FLOWER_POT.get(), FlowerPotRender::new);
        event.registerEntityRenderer(PvZEntityRegister.KERNEL_PULT.get(), KernelPultRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GARLIC.get(), GarlicRender::new);
        event.registerEntityRenderer(PvZEntityRegister.COFFEE_BEAN.get(), CoffeeBeanRender::new);
        event.registerEntityRenderer(PvZEntityRegister.UMBRELLA_LEAF.get(), UmbrellaLeafRender::new);
        event.registerEntityRenderer(PvZEntityRegister.MARIGOLD.get(), MariGoldRender::new);
        event.registerEntityRenderer(PvZEntityRegister.MELON_PULT.get(), MelonPultRender::new);

        event.registerEntityRenderer(PvZEntityRegister.GATLING_PEA.get(), GatlingPeaRender::new);
        event.registerEntityRenderer(PvZEntityRegister.TWIN_SUNFLOWER.get(), TwinSunFlowerRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GLOOM_SHROOM.get(), GloomShroomRender::new);
        event.registerEntityRenderer(PvZEntityRegister.CAT_TAIL.get(), CatTailRender::new);
        event.registerEntityRenderer(PvZEntityRegister.WINTER_MELON.get(), WinterMelonRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GOLD_MAGNET.get(), GoldMagnetRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SPIKE_ROCK.get(), SpikeRockRender::new);
        event.registerEntityRenderer(PvZEntityRegister.COB_CANNON.get(), CobCannonRender::new);
        event.registerEntityRenderer(PvZEntityRegister.IMITATER.get(), ImitaterRender::new);

        event.registerEntityRenderer(PvZEntityRegister.WATER_GUARD.get(), WaterGuardRender::new);
        event.registerEntityRenderer(PvZEntityRegister.STRANGE_CAT.get(), StrangeCatRender::new);

        event.registerEntityRenderer(PvZEntityRegister.ANGEL_STAR_FRUIT.get(), AngelStarFruitRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GOLD_LEAF.get(), GoldLeafRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BUTTER_PULT.get(), ButterPultRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BAMBOO_LORD.get(), BambooLordRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ICEBERG_LETTUCE.get(), IcebergLettuceRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BONK_CHOY.get(), BonkChoyRender::new);

        event.registerEntityRenderer(PvZEntityRegister.STEEL_PUMPKIN.get(), SteelPumpkinRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ICE_CABBAGE_PULT.get(), IceCabbagePultRender::new);
        event.registerEntityRenderer(PvZEntityRegister.FIRE_PEASHOOTER.get(), FirePeashooterRender::new);
        event.registerEntityRenderer(PvZEntityRegister.CHORUS_FRUIT_PULT.get(), ChorusFruitPultRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ROTATE_RADISH.get(), RotateRadishRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BURST_KERNEL_PULT.get(),BurstKernelPultRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BLAZE_WART.get(), BlazeWartRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GRASS_CARP.get(), GrassCrapRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SELF_IMITATER.get(), SelfImitaterRender::new);
        event.registerEntityRenderer(PvZEntityRegister.RE_ICEPEA.get(),SnowPeaRender::new);
        event.registerEntityRenderer(PvZEntityRegister.PEA_POD.get(), PeaPodRender::new);
        event.registerEntityRenderer(PvZEntityRegister.SCULK_SHROOM.get(),SculkShroomRender::new);
        event.registerEntityRenderer(PvZEntityRegister.ORIGIN_SHROOM.get(), OriginShroomRender::new);
        event.registerEntityRenderer(PvZEntityRegister.BEE_SHOOTER.get(), BeeShooterRender::new);
        event.registerEntityRenderer(PvZEntityRegister.FODDER_BUSH.get(), FodderBushRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GOLDEN_MELON_PULT.get(), GoldenMelonPultRender::new);
        event.registerEntityRenderer(PvZEntityRegister.PRIMAL_PEA_SHOOTER.get(), PrimalPeaShooterRender::new);
        event.registerEntityRenderer(PvZEntityRegister.GOO_PEA_SHOOTER.get(), GooPeaShooterRender::new);
        event.registerEntityRenderer(PvZEntityRegister.MAGNIFYING_GRASS.get(), MagnifyingGrassRender::new);
    }
}
