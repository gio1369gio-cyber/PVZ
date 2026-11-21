package com.lying.pvz.client;

import com.lying.pvz.client.model.armor.BucketHeadModel;
import com.lying.pvz.client.model.armor.ConeHeadModel;
import com.lying.pvz.client.model.armor.FootballArmorModel;
import com.lying.pvz.client.model.armor.GigaArmorModel;
import com.lying.pvz.client.model.entity.blockentity.OriginModel;
import com.lying.pvz.client.model.entity.bullet.*;
import com.lying.pvz.client.model.entity.creature.FoodieZombieModel;
import com.lying.pvz.client.model.entity.misc.*;
import com.lying.pvz.client.model.entity.npc.*;
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
import com.lying.pvz.client.model.entity.zombie.custom.LavaZombieModel;
import com.lying.pvz.client.model.entity.zombie.custom.MCZombieModel;
import com.lying.pvz.client.model.entity.zombie.custom.RedEdgeZombieModel;
import com.lying.pvz.client.model.entity.zombie.custom.TargetArrowZombieModel;
import com.lying.pvz.client.model.entity.zombie.grass.*;
import com.lying.pvz.client.model.entity.zombie.other.*;
import com.lying.pvz.client.model.entity.zombie.pool.*;
import com.lying.pvz.client.model.entity.zombie.roof.*;
import com.lying.pvz.client.model.entity.zombie.zombotany.*;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class PvZModelLayer {
    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        registerArmorLayers(event);
        registerBulletLayers(event);
        registerCreatureLayers(event);
        registerPlantLayers(event);
        registerZombieLayers(event);
        registerMiscLayers(event);
        registerBlockEntityAndComponentLayers(event);
    }


    private static void registerArmorLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BucketHeadModel.BUCKET_HEAD_LAYER, BucketHeadModel::createBodyLayer);
        event.registerLayerDefinition(ConeHeadModel.CONE_HEAD_LAYER, ConeHeadModel::createBodyLayer);
        event.registerLayerDefinition(FootballArmorModel.FOOTBALL_HEAD_LAYER, FootballArmorModel::createBodyLayer);
        event.registerLayerDefinition(FootballArmorModel.FOOTBALL_CHEST_LAYER, FootballArmorModel::createBodyLayer);
        event.registerLayerDefinition(GigaArmorModel.GIGA_HEAD_LAYER, GigaArmorModel::createBodyLayer);
        event.registerLayerDefinition(GigaArmorModel.GIGA_CHEST_LAYER, GigaArmorModel::createBodyLayer);
    }

    private static void registerBulletLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BallModel.LAYER_LOCATION, BallModel::createBodyLayer);
        event.registerLayerDefinition(BeeEntityModel.LAYER_LOCATION, BeeEntityModel::createBodyLayer);
        event.registerLayerDefinition(CornModel.LAYER_LOCATION, CornModel::createBodyLayer);
        event.registerLayerDefinition(KernelModel.LAYER_LOCATION, KernelModel::createBodyLayer);
        event.registerLayerDefinition(StarModel.LAYER_LOCATION, StarModel::createBodyLayer);
        event.registerLayerDefinition(TargetArrowModel.LAYER_LOCATION, TargetArrowModel::createBodyLayer);
        event.registerLayerDefinition(ThornModel.LAYER_LOCATION, ThornModel::createBodyLayer);
    }

    private static void registerCreatureLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FoodieZombieModel.LAYER_LOCATION, FoodieZombieModel::createBodyLayer);
        event.registerLayerDefinition(CrazyDaveModel.LAYER_LOCATION, CrazyDaveModel::createBodyLayer);
        event.registerLayerDefinition(PennyModel.LAYER_LOCATION, PennyModel::createBodyLayer);
        event.registerLayerDefinition(SunDaveModel.LAYER_LOCATION, SunDaveModel::createBodyLayer);
    }

    private static void registerPlantLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PeaShooterModel.LAYER_LOCATION, PeaShooterModel::createBodyLayer);
        event.registerLayerDefinition(SunFlowerModel.LAYER_LOCATION, SunFlowerModel::createBodyLayer);
        event.registerLayerDefinition(CherryBombModel.LAYER_LOCATION, CherryBombModel::createBodyLayer);
        event.registerLayerDefinition(WallNutModel.LAYER_LOCATION, WallNutModel::createBodyLayer);
        event.registerLayerDefinition(PotatoMineModel.LAYER_LOCATION, PotatoMineModel::createBodyLayer);
        event.registerLayerDefinition(SnowPeaModel.LAYER_LOCATION, SnowPeaModel::createBodyLayer);
        event.registerLayerDefinition(ChomperModel.LAYER_LOCATION, ChomperModel::createBodyLayer);
        event.registerLayerDefinition(RepeaterModel.LAYER_LOCATION, RepeaterModel::createBodyLayer);
        event.registerLayerDefinition(PuffShroomModel.LAYER_LOCATION, PuffShroomModel::createBodyLayer);
        event.registerLayerDefinition(SunShroomModel.LAYER_LOCATION, SunShroomModel::createBodyLayer);
        event.registerLayerDefinition(FumeShroomModel.LAYER_LOCATION, FumeShroomModel::createBodyLayer);
        event.registerLayerDefinition(GraveBusterModel.LAYER_LOCATION, GraveBusterModel::createBodyLayer);
        event.registerLayerDefinition(HypnoShroomModel.LAYER_LOCATION, HypnoShroomModel::createBodyLayer);
        event.registerLayerDefinition(ScaredyShroomModel.LAYER_LOCATION, ScaredyShroomModel::createBodyLayer);
        event.registerLayerDefinition(IceShroomModel.LAYER_LOCATION, IceShroomModel::createBodyLayer);
        event.registerLayerDefinition(DoomShroomModel.LAYER_LOCATION, DoomShroomModel::createBodyLayer);
        event.registerLayerDefinition(LilyPadModel.LAYER_LOCATION, LilyPadModel::createBodyLayer);
        event.registerLayerDefinition(SquashModel.LAYER_LOCATION, SquashModel::createBodyLayer);
        event.registerLayerDefinition(ThreePeaterModel.LAYER_LOCATION, ThreePeaterModel::createBodyLayer);
        event.registerLayerDefinition(TangleKelpModel.LAYER_LOCATION, TangleKelpModel::createBodyLayer);
        event.registerLayerDefinition(JalapenoModel.LAYER_LOCATION, JalapenoModel::createBodyLayer);
        event.registerLayerDefinition(SpikeWeedModel.LAYER_LOCATION, SpikeWeedModel::createBodyLayer);
        event.registerLayerDefinition(TorchWoodModel.LAYER_LOCATION, TorchWoodModel::createBodyLayer);
        event.registerLayerDefinition(TallNutModel.LAYER_LOCATION, TallNutModel::createBodyLayer);
        event.registerLayerDefinition(SeaShroomModel.LAYER_LOCATION, SeaShroomModel::createBodyLayer);
        event.registerLayerDefinition(PlanternModel.LAYER_LOCATION, PlanternModel::createBodyLayer);
        event.registerLayerDefinition(CactusModel.LAYER_LOCATION, CactusModel::createBodyLayer);
        event.registerLayerDefinition(BloverModel.LAYER_LOCATION, BloverModel::createBodyLayer);
        event.registerLayerDefinition(SplitPeaModel.LAYER_LOCATION, SplitPeaModel::createBodyLayer);
        event.registerLayerDefinition(StarFruitModel.LAYER_LOCATION, StarFruitModel::createBodyLayer);
        event.registerLayerDefinition(MagnetShroomModel.LAYER_LOCATION, MagnetShroomModel::createBodyLayer);
        event.registerLayerDefinition(CabbagePultModel.LAYER_LOCATION, CabbagePultModel::createBodyLayer);
        event.registerLayerDefinition(FlowerPotModel.LAYER_LOCATION, FlowerPotModel::createBodyLayer);
        event.registerLayerDefinition(KernelPultModel.LAYER_LOCATION, KernelPultModel::createBodyLayer);
        event.registerLayerDefinition(CoffeeBeanModel.LAYER_LOCATION, CoffeeBeanModel::createBodyLayer);
        event.registerLayerDefinition(GarlicModel.LAYER_LOCATION, GarlicModel::createBodyLayer);
        event.registerLayerDefinition(UmbrellaLeafModel.LAYER_LOCATION, UmbrellaLeafModel::createBodyLayer);
        event.registerLayerDefinition(MariGoldModel.LAYER_LOCATION, MariGoldModel::createBodyLayer);
        event.registerLayerDefinition(MelonPultModel.LAYER_LOCATION, MelonPultModel::createBodyLayer);
        event.registerLayerDefinition(GatlingPeaModel.LAYER_LOCATION, GatlingPeaModel::createBodyLayer);
        event.registerLayerDefinition(TwinSunFlowerModel.LAYER_LOCATION, TwinSunFlowerModel::createBodyLayer);
        event.registerLayerDefinition(GloomShroomModel.LAYER_LOCATION, GloomShroomModel::createBodyLayer);
        event.registerLayerDefinition(CatTailModel.LAYER_LOCATION, CatTailModel::createBodyLayer);
        event.registerLayerDefinition(WinterMelonModel.LAYER_LOCATION, WinterMelonModel::createBodyLayer);
        event.registerLayerDefinition(GoldMagnetModel.LAYER_LOCATION, GoldMagnetModel::createBodyLayer);
        event.registerLayerDefinition(SpikeRockModel.LAYER_LOCATION, SpikeRockModel::createBodyLayer);
        event.registerLayerDefinition(CobCannonModel.LAYER_LOCATION, CobCannonModel::createBodyLayer);
        event.registerLayerDefinition(ImitaterModel.LAYER_LOCATION, ImitaterModel::createBodyLayer);
        event.registerLayerDefinition(WaterGuardModel.LAYER_LOCATION, WaterGuardModel::createBodyLayer);
        event.registerLayerDefinition(StrangeCatModel.LAYER_LOCATION, StrangeCatModel::createBodyLayer);
        event.registerLayerDefinition(IcebergLettuceModel.LAYER_LOCATION, IcebergLettuceModel::createBodyLayer);
        event.registerLayerDefinition(BonkChoyModel.LAYER_LOCATION, BonkChoyModel::createBodyLayer);
        event.registerLayerDefinition(GoldLeafModel.LAYER_LOCATION, GoldLeafModel::createBodyLayer);
        event.registerLayerDefinition(AngelStarFruitModel.LAYER_LOCATION, AngelStarFruitModel::createBodyLayer);
        event.registerLayerDefinition(BambooLordModel.LAYER_LOCATION, BambooLordModel::createBodyLayer);

        event.registerLayerDefinition(SteelPumpkinModel.LAYER_LOCATION, SteelPumpkinModel::createBodyLayer);
        event.registerLayerDefinition(IceCabbagePultModel.LAYER_LOCATION, IceCabbagePultModel::createBodyLayer);
        event.registerLayerDefinition(FirePeashooterModel.LAYER_LOCATION, FirePeashooterModel::createBodyLayer);
        event.registerLayerDefinition(ChorusFruitPultModel.LAYER_LOCATION, ChorusFruitPultModel::createBodyLayer);
        event.registerLayerDefinition(RotateRadishModel.LAYER_LOCATION,RotateRadishModel::createBodyLayer);
        event.registerLayerDefinition(BurstKernelPultModel.LAYER_LOCATION, BurstKernelPultModel::createBodyLayer);
        event.registerLayerDefinition(BlazeWartModel.LAYER_LOCATION, BlazeWartModel::createBodyLayer);
        event.registerLayerDefinition(GrassCarpModel.LAYER_LOCATION, GrassCarpModel::createBodyLayer);
        event.registerLayerDefinition(SelfImitaterModel.LAYER_LOCATION, SelfImitaterModel::createBodyLayer);
        event.registerLayerDefinition(PeaPodModel.LAYER_LOCATION, PeaPodModel::createBodyLayer);
        event.registerLayerDefinition(SculkShroomModel.LAYER_LOCATION, SculkShroomModel::createBodyLayer);
        event.registerLayerDefinition(OriginShroomModel.LAYER_LOCATION, OriginShroomModel::createBodyLayer);
        event.registerLayerDefinition(BeeShooterModel.LAYER_LOCATION, BeeShooterModel::createBodyLayer);
        event.registerLayerDefinition(FodderBushModel.LAYER_LOCATION, FodderBushModel::createBodyLayer);
        event.registerLayerDefinition(GoldenMelonPultModel.LAYER_LOCATION, GoldenMelonPultModel::createBodyLayer);
        event.registerLayerDefinition(PrimalPeaShooterModel.LAYER_LOCATION, PrimalPeaShooterModel::createBodyLayer);
        event.registerLayerDefinition(GooPeaShooterModel.LAYER_LOCATION, GooPeaShooterModel::createBodyLayer);
        event.registerLayerDefinition(MagnifyingGrassModel.LAYER_LOCATION,MagnifyingGrassModel::createBodyLayer);
    }

    private static void registerZombieLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NormalZombieModel.LAYER_LOCATION, NormalZombieModel::createBodyLayer);
        event.registerLayerDefinition(FlagZombieModel.LAYER_LOCATION, FlagZombieModel::createBodyLayer);
        event.registerLayerDefinition(ConeHeadZombieModel.LAYER_LOCATION, ConeHeadZombieModel::createBodyLayer);
        event.registerLayerDefinition(PoleZombieModel.LAYER_LOCATION, PoleZombieModel::createBodyLayer);
        event.registerLayerDefinition(BucketHeadZombieModel.LAYER_LOCATION, BucketHeadZombieModel::createBodyLayer);
        event.registerLayerDefinition(NewspaperZombieModel.LAYER_LOCATION, NewspaperZombieModel::createBodyLayer);
        event.registerLayerDefinition(ScreenDoorZombieModel.LAYER_LOCATION, ScreenDoorZombieModel::createBodyLayer);
        event.registerLayerDefinition(FootballZombieModel.LAYER_LOCATION, FootballZombieModel::createBodyLayer);
        event.registerLayerDefinition(DancingZombieModel.LAYER_LOCATION, DancingZombieModel::createBodyLayer);
        event.registerLayerDefinition(BackupDancerModel.LAYER_LOCATION, BackupDancerModel::createBodyLayer);
        event.registerLayerDefinition(GigaFootballZombieModel.LAYER_LOCATION, GigaFootballZombieModel::createBodyLayer);
        event.registerLayerDefinition(OldZombieModel.LAYER_LOCATION, OldZombieModel::createBodyLayer);
        event.registerLayerDefinition(SundayEditionZombieModel.LAYER_LOCATION, SundayEditionZombieModel::createBodyLayer);
        event.registerLayerDefinition(SnorkelZombieModel.LAYER_LOCATION, SnorkelZombieModel::createBodyLayer);
        event.registerLayerDefinition(ZomboniModel.LAYER_LOCATION, ZomboniModel::createBodyLayer);
        event.registerLayerDefinition(BobsleTeamModel.LAYER_LOCATION, BobsleTeamModel::createBodyLayer);
        event.registerLayerDefinition(BobsleZombieModel.LAYER_LOCATION, BobsleZombieModel::createBodyLayer);
        event.registerLayerDefinition(DolphinRiderZombieModel.LAYER_LOCATION, DolphinRiderZombieModel::createBodyLayer);
        event.registerLayerDefinition(JackInBoxZombieModel.LAYER_LOCATION, JackInBoxZombieModel::createBodyLayer);
        event.registerLayerDefinition(BalloonZombieModel.LAYER_LOCATION, BalloonZombieModel::createBodyLayer);
        event.registerLayerDefinition(DiggerZombieModel.LAYER_LOCATION, DiggerZombieModel::createBodyLayer);
        event.registerLayerDefinition(PogoZombieModel.LAYER_LOCATION, PogoZombieModel::createBodyLayer);
        event.registerLayerDefinition(YetiZombieModel.LAYER_LOCATION, YetiZombieModel::createBodyLayer);
        event.registerLayerDefinition(LadderZombieModel.LAYER_LOCATION, LadderZombieModel::createBodyLayer);
        event.registerLayerDefinition(CatapultZombieModel.LAYER_LOCATION, CatapultZombieModel::createBodyLayer);
        event.registerLayerDefinition(GargantuarModel.LAYER_LOCATION, GargantuarModel::createBodyLayer);
        event.registerLayerDefinition(ImpModel.LAYER_LOCATION, ImpModel::createBodyLayer);
        event.registerLayerDefinition(EdgarRobotModel.LAYER_LOCATION, EdgarRobotModel::createBodyLayer);
        event.registerLayerDefinition(LavaZombieModel.LAYER_LOCATION, LavaZombieModel::createBodyLayer);
        event.registerLayerDefinition(MournerZombieModel.LAYER_LOCATION, MournerZombieModel::createBodyLayer);
        event.registerLayerDefinition(NobleZombieModel.LAYER_LOCATION, NobleZombieModel::createBodyLayer);
        event.registerLayerDefinition(TrickZombieModel.LAYER_LOCATION, TrickZombieModel::createBodyLayer);
        event.registerLayerDefinition(PeaShooterZombieModel.LAYER_LOCATION, PeaShooterZombieModel::createBodyLayer);
        event.registerLayerDefinition(WallNutZombieModel.LAYER_LOCATION, WallNutZombieModel::createBodyLayer);
        event.registerLayerDefinition(GatlingPeaZombieModel.LAYER_LOCATION, GatlingPeaZombieModel::createBodyLayer);
        event.registerLayerDefinition(SquashZombieModel.LAYER_LOCATION, SquashZombieModel::createBodyLayer);
        event.registerLayerDefinition(JalapenoZombieModel.LAYER_LOCATION, JalapenoZombieModel::createBodyLayer);
        event.registerLayerDefinition(TallNutZombieModel.LAYER_LOCATION, TallNutZombieModel::createBodyLayer);
        event.registerLayerDefinition(PumpkinZombieModel.LAYER_LOCATION, PumpkinZombieModel::createBodyLayer);
        event.registerLayerDefinition(RaZombieModel.LAYER_LOCATION, RaZombieModel::createBodyLayer);
        event.registerLayerDefinition(TombStoneModel.LAYER_LOCATION, TombStoneModel::createBodyLayer);
        event.registerLayerDefinition(BungeeZombieModel.LAYER_LOCATION, BungeeZombieModel::createBodyLayer);
        event.registerLayerDefinition(CoffinModel.LAYER_LOCATION, CoffinModel::createBodyLayer);
        event.registerLayerDefinition(DolphinRiderModel.LAYER_LOCATION, DolphinRiderModel::createBodyLayer);
        event.registerLayerDefinition(ZombieDolphinModel.LAYER_LOCATION, ZombieDolphinModel::createBodyLayer);
        event.registerLayerDefinition(SunFlowerZombieModel.LAYER_LOCATION, SunFlowerZombieModel::createBodyLayer);
        event.registerLayerDefinition(ChomperZombieModel.LAYER_LOCATION, ChomperZombieModel::createBodyLayer);
        event.registerLayerDefinition(SteelPumpkinZombieModel.LAYER_LOCATION, SteelPumpkinZombieModel::createBodyLayer);
        event.registerLayerDefinition(AirborneZombieModel.LAYER_LOCATION, AirborneZombieModel::createBodyLayer);
        event.registerLayerDefinition(Edgar090547Model.LAYER_LOCATION, Edgar090547Model::createBodyLayer);
        event.registerLayerDefinition(MJZombieModel.LAYER_LOCATION, MJZombieModel::createBodyLayer);
        event.registerLayerDefinition(MCZombieModel.LAYER_LOCATION,MCZombieModel::createBodyLayer);
        event.registerLayerDefinition(DancerBackupModel.LAYER_LOCATION, DancerBackupModel::createBodyLayer);
        event.registerLayerDefinition(RedEdgeZombieModel.LAYER_LOCATION, RedEdgeZombieModel::createBodyLayer);
        event.registerLayerDefinition(TargetArrowZombieModel.LAYER_LOCATION, TargetArrowZombieModel::createBodyLayer);
        event.registerLayerDefinition(FlowerPotZombieModel.LAYER_LOCATION, FlowerPotZombieModel::createBodyLayer);
    }

    private static void registerMiscLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BobsleCarModel.LAYER_LOCATION, BobsleCarModel::createBodyLayer);
        event.registerLayerDefinition(FireCrackersModel.LAYER_LOCATION, FireCrackersModel::createBodyLayer);
        event.registerLayerDefinition(GardenRakeModel.LAYER_LOCATION, GardenRakeModel::createBodyLayer);
        event.registerLayerDefinition(GiftBoxModel.LAYER_LOCATION, GiftBoxModel::createBodyLayer);
        event.registerLayerDefinition(LawnMowerModel.LAYER_LOCATION, LawnMowerModel::createBodyLayer);
        event.registerLayerDefinition(SmallChomperModel.LAYER_LOCATION, SmallChomperModel::createBodyLayer);
        event.registerLayerDefinition(ZombieHandModel.LAYER_LOCATION, ZombieHandModel::createBodyLayer);
        event.registerLayerDefinition(DropModel.LAYER_LOCATION, DropModel::createBodyLayer);
        event.registerLayerDefinition(DuckyTubeModel.LAYER_LOCATION, DuckyTubeModel::createBodyLayer);
        event.registerLayerDefinition(LadderModel.LAYER_LOCATION, LadderModel::createBodyLayer);
    }

    private static void registerBlockEntityAndComponentLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OriginModel.LAYER_LOCATION, OriginModel::createBodyLayer);
        event.registerLayerDefinition(WallNutModel.ARMOR_LAYER_LOCATION, WallNutModel.WallNutArmorModel::createBodyLayer);
        event.registerLayerDefinition(PumpkinModel.ARMOR_LAYER_LOCATION, PumpkinModel.PumpkinArmorModel::createBodyLayer);
        event.registerLayerDefinition(PumpkinModel.LAYER_LOCATION, PumpkinModel::createBodyLayer);
        event.registerLayerDefinition(SteelPumpkinModel.SteelPumpkinArmorModel.ARMOR_LAYER_LOCATION,
                SteelPumpkinModel.SteelPumpkinArmorModel::createArmorLayer);
    }
}
