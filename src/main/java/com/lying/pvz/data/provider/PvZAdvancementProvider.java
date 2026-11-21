package com.lying.pvz.data.provider;

import com.lying.pvz.common.advancement.predicate.AmountPredicate;
import com.lying.pvz.common.advancement.predicate.StringPredicate;
import com.lying.pvz.common.advancement.trigger.*;
import com.lying.pvz.common.init.*;
import com.lying.pvz.common.level.item.component.SunStorage;
import com.lying.pvz.data.bootstrap.worldgen.biome.PvZBiome;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.advancements.*;
import net.minecraft.advancements.predicates.*;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.ConsumeItemTrigger;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.triggers.KilledTrigger;
import net.minecraft.advancements.triggers.PlayerHurtEntityTrigger;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class PvZAdvancementProvider extends AdvancementProvider {

    public PvZAdvancementProvider(List<AdvancementSubProvider.Factory> subProviders) {
        super(subProviders);
    }

    public static final class Generator extends AdvancementSubProvider {

        private static final Identifier BG = StringUtil.id("block/amethyst_block");

        public Generator(BootstrapContext<Advancement> output) {
            super(output);
        }

        @Override
        public void generate() {
            AdvancementHolder root = genRoot();
            AdvancementHolder essence = genEssence(root);
            AdvancementHolder origin = genOrigin(essence);
            AdvancementHolder almanac = genAlmanac(origin);
            AdvancementHolder firstPlant = genFirstPlant(origin);
            AdvancementHolder upgradePlant = genUpgradePlant(firstPlant);
            AdvancementHolder invasion = genInvasion(root);
            AdvancementHolder strangeHelp = genStrangeHelp(root);
            AdvancementHolder edgar05 = genEdgar05(strangeHelp);
            AdvancementHolder edgar17 = genEdgar17(edgar05);
            AdvancementHolder trophy = genTrophy(edgar05);
            AdvancementHolder crazyDave = genCrazyDave(essence);
            AdvancementHolder slotMachine = genSlotMachine(essence);
            AdvancementHolder tasteBrain = genTasteBrain(essence);
            AdvancementHolder richMan = genRichMan(essence);
            AdvancementHolder sunnyDays = genSunnyDays(origin);
            AdvancementHolder sunStorage = genSunStorage(origin);
            AdvancementHolder tooth = genTooth(origin);
            AdvancementHolder toweringWisdom = genToweringWisdom(origin);
            AdvancementHolder peaHoliday = genPeaHoliday(origin);
            AdvancementHolder disco = genDisco(firstPlant);
            AdvancementHolder explodonator = genExplodonator(firstPlant);
            AdvancementHolder frozen = genFrozen(firstPlant);
            AdvancementHolder goodMorning = genGoodMorning(firstPlant);
            AdvancementHolder popcorn = genPopcorn(firstPlant);
            AdvancementHolder spudow = genSpudow(firstPlant);
            AdvancementHolder superPlant = genSuperPlant(firstPlant);
            AdvancementHolder killGargantuar = genKillGargantuar(invasion);
            AdvancementHolder missionExpert = genMissionExpert(invasion);
            AdvancementHolder missionMaster = genMissionMaster(missionExpert);
            AdvancementHolder mustacheMode = genMustacheMode(invasion);
            AdvancementHolder yeti = genYeti(invasion);
            AdvancementHolder luckyDog = genLuckyDog(slotMachine);
             AdvancementHolder zenGarden      = genZenGarden(root);
        }

        // ---------- 通用 ----------

        private AdvancementHolder gen(AdvancementHolder parent, String path, ItemStackTemplate icon, String titleKey, String descKey,
                                      AdvancementType frame, Identifier bg, boolean toast, boolean announce, boolean hidden,
                                      AdvancementRewards rewards, AdvancementRequirements reqs, Consumer<Advancement.Builder> criterion) {
            Advancement.Builder b = Advancement.Builder.advancement();
            if (parent != null) b.parent(parent);

            Optional<ClientAsset.ResourceTexture> background = bg == null
                    ? Optional.empty()
                    : Optional.of(new ClientAsset.ResourceTexture(bg));
            b.display(new DisplayInfo(
                    icon,
                    Component.translatable(titleKey),
                    Component.translatable(descKey),
                    background,
                    frame, toast, announce, hidden));

            if (rewards != null) b.rewards(rewards);
            if (reqs != null) b.requirements(reqs);
            criterion.accept(b);
            return b.save(this.output, StringUtil.id(path));
        }

        private static ItemStackTemplate of(ItemLike item) {
            return new ItemStackTemplate(item.asItem());
        }

        private EntityPredicate.Builder entityOf(EntityType<?> type) {
            HolderGetter<EntityType<?>> entityTypes = this.output.lookup(Registries.ENTITY_TYPE);
            return EntityPredicate.Builder.entity().of(entityTypes, type);
        }

        private static ResourceKey<Level> overworld() {
            return Level.OVERWORLD;
        }

        // ---------- 具体 advancement ----------

        private AdvancementHolder genRoot() {
            return gen(null, "adventure/root",
                    of(PvZItemRegister.PEA.get()),
                    "advancement.pvz.root", "advancement.pvz.root.desc",
                    AdvancementType.TASK, BG, false, true, false, null, null,
                    b -> b.addCriterion("in_world",
                            PlayerTrigger.TriggerInstance.located(
                                    EntityPredicate.Builder.entity().located(
                                            LocationPredicate.Builder.location()
                                                    .setDimension(overworld())))));
        }

        private AdvancementHolder genEssence(AdvancementHolder parent) {
            return gen(parent, "adventure/essence",
                    of(PvZItemRegister.ORIGIN_ESSENCE.get()),
                    "advancement.pvz.essence", "advancement.pvz.essence.desc",
                    AdvancementType.GOAL, null, false, true, false, null, null,
                    b -> b.addCriterion("get_origin",
                            InventoryChangeTrigger.TriggerInstance.hasItems(PvZItemRegister.ORIGIN_ESSENCE.get())));
        }

        private AdvancementHolder genOrigin(AdvancementHolder parent) {
            return gen(parent, "adventure/origin",
                    of(PvZBlockRegister.ORIGIN_BLOCK.get()),
                    "advancement.pvz.origin", "advancement.pvz.origin.desc",
                    AdvancementType.GOAL, null, false, true, false, null, null,
                    b -> b.addCriterion("get_origin",
                            ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(
                                    this.output.lookup(Registries.BLOCK),
                                    PvZBlockRegister.ORIGIN_BLOCK.get())));
        }

        private AdvancementHolder genAlmanac(AdvancementHolder parent) {
            return gen(parent, "adventure/almanac",
                    of(PvZItemRegister.ALMANAC.get()),
                    "advancement.pvz.almanac", "advancement.pvz.almanac.desc",
                    AdvancementType.TASK, null, false, true, false, null, null,
                    b -> b.addCriterion("get_almanac",
                            InventoryChangeTrigger.TriggerInstance.hasItems(PvZItemRegister.ALMANAC.get())));
        }

        private AdvancementHolder genFirstPlant(AdvancementHolder parent) {
            return gen(parent, "adventure/first_plant",
                    of(PvZItemRegister.SUN_FLOWER_ENJOY_CARD.get()),
                    "advancement.pvz.first_plant", "advancement.pvz.first_plant.desc",
                    AdvancementType.GOAL, null, false, true, false, null, null,
                    b -> b.addCriterion("get_origin",
                            PvZTriggers.PLAYER_PLACE_PAZ.get().createCriterion(
                                    new PlayerPlacePAZTrigger.Instance(
                                            Optional.empty(),
                                            new StringPredicate("plant"),
                                            StringPredicate.ANY))));
        }

        private AdvancementHolder genUpgradePlant(AdvancementHolder parent) {
            return gen(parent, "adventure/upgrade_plant",
                    of(PvZItemRegister.TWIN_SUNFLOWER_ENJOY_CARD.get()),
                    "advancement.pvz.upgrade_plant", "advancement.pvz.upgrade_plant.desc",
                    AdvancementType.TASK, null, false, true, false, null, null,
                    b -> b.addCriterion("get_origin",
                            PvZTriggers.PLAYER_PLACE_PAZ.get().createCriterion(
                                    new PlayerPlacePAZTrigger.Instance(
                                            Optional.empty(),
                                            new StringPredicate("upgrade"),
                                            StringPredicate.ANY))));
        }

        private AdvancementHolder genInvasion(AdvancementHolder parent) {
            return gen(parent, "adventure/invasion",
                    new ItemStackTemplate(Items.STONE_SWORD),
                    "advancement.pvz.invasion", "advancement.pvz.invasion.desc",
                    AdvancementType.GOAL, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.INVASION.get().createCriterion(
                                    new InvasionTrigger.Instance(Optional.empty()))));
        }

        private AdvancementHolder genStrangeHelp(AdvancementHolder parent) {
            return gen(parent, "adventure/strange_help",
                    of(PvZItemRegister.CHALLENGE_ENVELOPE.get()),
                    "advancement.pvz.strange_help", "advancement.pvz.strange_help.desc",
                    AdvancementType.TASK, null, true, true, false,
                    AdvancementRewards.Builder.experience(100).build(), null,
                    b -> b.addCriterion("in_world",
                            PvZTriggers.CHALLENGE.get().createCriterion(
                                    new ChallengeTrigger.Instance(
                                            Optional.empty(),
                                            new StringPredicate("strange_help")))));
        }

        private AdvancementHolder genEdgar05(AdvancementHolder parent) {
            return gen(parent, "adventure/edgar_090505",
                    of(PvZItemRegister.CHALLENGE_ENVELOPE.get()),
                    "advancement.pvz.edgar_090505", "advancement.pvz.edgar_090505.desc",
                    AdvancementType.TASK, null, true, true, false,
                    AdvancementRewards.Builder.experience(100).build(), null,
                    b -> b.addCriterion("in_world",
                            PvZTriggers.CHALLENGE.get().createCriterion(
                                    new ChallengeTrigger.Instance(
                                            Optional.empty(),
                                            new StringPredicate("roof_night")))));
        }

        private AdvancementHolder genEdgar17(AdvancementHolder parent) {
            return gen(parent, "adventure/edgar_090517",
                    of(PvZItemRegister.CHALLENGE_ENVELOPE.get()),
                    "advancement.pvz.edgar_090517", "advancement.pvz.edgar_090517.desc",
                    AdvancementType.CHALLENGE, null, true, true, false,
                    AdvancementRewards.Builder.experience(500).build(), null,
                    b -> b.addCriterion("in_world",
                            PvZTriggers.CHALLENGE.get().createCriterion(
                                    new ChallengeTrigger.Instance(
                                            Optional.empty(),
                                            new StringPredicate("roof_again")))));
        }

        private AdvancementHolder genTrophy(AdvancementHolder parent) {
            return gen(parent, "adventure/trophy",
                    of(PvZBlockRegister.DIAMOND_SUNFLOWER_TROPHY),
                    "advancement.pvz.trophy", "advancement.pvz.trophy.desc",
                    AdvancementType.CHALLENGE, null, false, true, false, null, null,
                    b -> b.addCriterion("get_origin",
                            InventoryChangeTrigger.TriggerInstance.hasItems(
                                    PvZBlockRegister.DIAMOND_SUNFLOWER_TROPHY)));
        }

        private AdvancementHolder genCrazyDave(AdvancementHolder parent) {
            return gen(parent, "adventure/crazy_dave",
                    of(PvZItemRegister.CAR_KEY.get()),
                    "advancement.pvz.crazy_dave", "advancement.pvz.crazy_dave.desc",
                    AdvancementType.GOAL, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.TRADE.get().createCriterion(
                                    new PVZTradeTrigger.Instance(
                                            Optional.empty(),
                                            Optional.of(EntityPredicate.wrap(
                                                    entityOf(PvZEntityRegister.CRAZY_DAVE.get()).build())),
                                            AmountPredicate.ANY))));
        }

        private AdvancementHolder genSlotMachine(AdvancementHolder parent) {
            return gen(parent, "adventure/slot_machine",
                    of(PvZBlockRegister.SLOT_MACHINE.asItem()),
                    "advancement.pvz.slot_machine", "advancement.pvz.slot_machine.desc",
                    AdvancementType.GOAL, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.SLOT_MACHINE.get().createCriterion(
                                    new SlotMachineTrigger.Instance(
                                            Optional.empty(),
                                            new AmountPredicate(MinMaxBounds.Ints.atLeast(1)),
                                            StringPredicate.ANY))));
        }

        private AdvancementHolder genTasteBrain(AdvancementHolder parent) {
            return gen(parent, "adventure/taste_brain",
                    of(PvZItemRegister.FAKE_BRAIN.get()),
                    "advancement.pvz.taste_brain", "advancement.pvz.taste_brain.desc",
                    AdvancementType.TASK, null, false, true, false, null, null,
                    b -> b.addCriterion("get_almanac",
                            ConsumeItemTrigger.TriggerInstance.usedItem(this.output.lookup(Registries.ITEM),
                                    PvZItemRegister.FAKE_BRAIN.get())));
        }

        private AdvancementHolder genRichMan(AdvancementHolder parent) {
            return gen(parent, "adventure/rich_man",
                    new ItemStackTemplate(Items.GOLD_BLOCK),
                    "advancement.pvz.rich_man", "advancement.pvz.rich_man.desc",
                    AdvancementType.CHALLENGE, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.MONEY.get().createCriterion(
                                    new MoneyTrigger.Instance(
                                            Optional.empty(),
                                            new AmountPredicate(MinMaxBounds.Ints.atLeast(100000))))));
        }

        private AdvancementHolder genSunnyDays(AdvancementHolder parent) {
            return gen(parent, "adventure/sunny_days",
                    of(PvZItemRegister.SUN_STORAGE_SAPLING.get()),
                    "advancement.pvz.sunny_days", "advancement.pvz.sunny_days.desc",
                    AdvancementType.GOAL, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.SUN_AMOUNT.get().createCriterion(
                                    new SunAmountTrigger.Instance(
                                            Optional.empty(),
                                            new AmountPredicate(MinMaxBounds.Ints.atLeast(8000))))));
        }

        private AdvancementHolder genSunStorage(AdvancementHolder parent) {
            ItemStackTemplate icon = new ItemStackTemplate(
                    PvZItemRegister.LARGE_SUN_STORAGE_SAPLING.get().asItem(),
                    1, DataComponentPatch.builder().set(PvZDataComponents.SUN_STORAGE.get(),
                    new SunStorage(100000L, 100000L, false)).build());

            return gen(parent, "adventure/sun_storage",
                    icon,
                    "advancement.pvz.sun_storage", "advancement.pvz.sun_storage.desc",
                    AdvancementType.CHALLENGE, null, false, true, false, null, null,
                    b -> b.addCriterion("get_almanac",
                            InventoryChangeTrigger.TriggerInstance.hasItems(
                                    ItemPredicate.Builder.item()
                                            .of(this.output.lookup(Registries.ITEM),
                                                    PvZItemRegister.LARGE_SUN_STORAGE_SAPLING.get())
                                            .withComponents(new DataComponentMatchers(
                                                    DataComponentExactPredicate.builder()
                                                            .expect(PvZDataComponents.SUN_STORAGE.get(),
                                                                    new SunStorage(100000L, 100000L, false))
                                                            .build(),
                                                    Map.of()))
                                            .build())));
        }

        private AdvancementHolder genTooth(AdvancementHolder parent) {
            return gen(parent, "adventure/tooth",
                    of(PvZItemRegister.TARGET_ARROW.get()),
                    "advancement.pvz.tooth", "advancement.pvz.tooth.desc",
                    AdvancementType.GOAL, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(
                                    Optional.of(DamagePredicate.Builder.damageInstance()
                                            .type(DamageSourcePredicate.Builder.damageType()
                                                    .direct(entityOf(PvZEntityRegister.TARGET_ARROW.get())))
                                            .build()),
                                    Optional.empty())));
        }

        private AdvancementHolder genToweringWisdom(AdvancementHolder parent) {
            return gen(parent, "adventure/towering_wisdom",
                    new ItemStackTemplate(Items.OAK_SAPLING),
                    "advancement.pvz.towering_wisdom", "advancement.pvz.towering_wisdom.desc",
                    AdvancementType.CHALLENGE, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.TREE_LEVEL.get().createCriterion(
                                    new TreeLevelTrigger.Instance(
                                            Optional.empty(),
                                            new AmountPredicate(MinMaxBounds.Ints.atLeast(100))))));
        }

        private AdvancementHolder genPeaHoliday(AdvancementHolder parent) {
            return gen(parent, "adventure/pea_holiday",
                    of(PvZItemRegister.PEA_GUN.get()),
                    "advancement.pvz.pea_holiday", "advancement.pvz.pea_holiday.desc",
                    AdvancementType.GOAL, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntity(
                                    Optional.of(DamagePredicate.Builder.damageInstance()
                                            .type(DamageSourcePredicate.Builder.damageType()
                                                    .direct(entityOf(PvZEntityRegister.PEA.get())))
                                            .build()),
                                    Optional.empty())));
        }

        private AdvancementHolder genDisco(AdvancementHolder parent) {
            return gen(parent, "adventure/disco",
                    of(PvZItemRegister.HYPNO_SHROOM_ENJOY_CARD.get()),
                    "advancement.pvz.disco", "advancement.pvz.disco.desc",
                    AdvancementType.GOAL, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.CHARM_ZOMBIE.get().createCriterion(
                                    new CharmZombieTrigger.Instance(
                                            Optional.empty(),
                                            Optional.of(EntityPredicate.wrap(
                                                    entityOf(PvZEntityRegister.DANCING_ZOMBIE.get()).build()))))));
        }

        private AdvancementHolder genExplodonator(AdvancementHolder parent) {
            return gen(parent, "adventure/explodonator",
                    of(PvZItemRegister.CHERRY_BOMB_ENJOY_CARD.get()),
                    "advancement.pvz.explodonator", "advancement.pvz.explodonator.desc",
                    AdvancementType.TASK, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.ENTITY_EFFECT_AMOUNT.get().createCriterion(
                                    new EntityEffectAmountTrigger.Instance(
                                            Optional.empty(),
                                            Optional.of(EntityPredicate.wrap(
                                                    entityOf(PvZEntityRegister.CHERRY_BOMB.get()).build())),
                                            new AmountPredicate(MinMaxBounds.Ints.atLeast(15))))));
        }

        private AdvancementHolder genFrozen(AdvancementHolder parent) {
            return gen(parent, "adventure/frozen",
                    of(PvZItemRegister.ICE_SHROOM_ENJOY_CARD.get()),
                    "advancement.pvz.frozen", "advancement.pvz.frozen.desc",
                    AdvancementType.TASK, null, true, true, false, null,
                    AdvancementRequirements.anyOf(List.of("frozen1", "frozen2")),
                    b -> {
                        b.addCriterion("frozen1",
                                PvZTriggers.ENTITY_EFFECT_AMOUNT.get().createCriterion(
                                        new EntityEffectAmountTrigger.Instance(
                                                Optional.empty(),
                                                Optional.of(EntityPredicate.wrap(
                                                        entityOf(PvZEntityRegister.ICE_SHROOM.get()).build())),
                                                new AmountPredicate(MinMaxBounds.Ints.atLeast(25)))));
                        b.addCriterion("frozen2",
                                PvZTriggers.ENTITY_EFFECT_AMOUNT.get().createCriterion(
                                        new EntityEffectAmountTrigger.Instance(
                                                Optional.empty(),
                                                Optional.of(EntityPredicate.wrap(
                                                        entityOf(PvZEntityRegister.ICEBERG_LETTUCE.get()).build())),
                                                new AmountPredicate(MinMaxBounds.Ints.atLeast(20)))));
                    });
        }

        private AdvancementHolder genGoodMorning(AdvancementHolder parent) {
            return gen(parent, "adventure/good_morning",
                    of(PvZItemRegister.COFFEE_BEAN_ENJOY_CARD.get()),
                    "advancement.pvz.good_morning", "advancement.pvz.good_morning.desc",
                    AdvancementType.TASK, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.ENTITY_EFFECT_AMOUNT.get().createCriterion(
                                    new EntityEffectAmountTrigger.Instance(
                                            Optional.empty(),
                                            Optional.of(EntityPredicate.wrap(
                                                    entityOf(PvZEntityRegister.COFFEE_BEAN.get()).build())),
                                            new AmountPredicate(MinMaxBounds.Ints.atLeast(4))))));
        }

        private AdvancementHolder genPopcorn(AdvancementHolder parent) {
            return gen(parent, "adventure/popcorn",
                    of(PvZItemRegister.COB_CANNON_ENJOY_CARD.get()),
                    "advancement.pvz.popcorn", "advancement.pvz.popcorn.desc",
                    AdvancementType.TASK, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.ENTITY_EFFECT_AMOUNT.get().createCriterion(
                                    new EntityEffectAmountTrigger.Instance(
                                            Optional.empty(),
                                            Optional.of(EntityPredicate.wrap(
                                                    entityOf(PvZEntityRegister.COB_CANNON.get()).build())),
                                            new AmountPredicate(MinMaxBounds.Ints.atLeast(2))))));
        }

        private AdvancementHolder genSpudow(AdvancementHolder parent) {
            return gen(parent, "adventure/spudow",
                    of(PvZItemRegister.POTATO_MINE_ENJOY_CARD.get()),
                    "advancement.pvz.spudow", "advancement.pvz.spudow.desc",
                    AdvancementType.GOAL, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            KilledTrigger.TriggerInstance.playerKilledEntity(
                                    Optional.empty(),
                                    DamageSourcePredicate.Builder.damageType()
                                            .source(entityOf(PvZEntityRegister.POTATO_MINE.get())))));
        }

        private AdvancementHolder genSuperPlant(AdvancementHolder parent) {
            return gen(parent, "adventure/super_plant",
                    of(PvZItemRegister.ORIGIN_SWORD.get()),
                    "advancement.pvz.super_plant", "advancement.pvz.super_plant.desc",
                    AdvancementType.GOAL, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.PLANT_SUPER.get().createCriterion(
                                    new PlantSuperTrigger.Instance(Optional.empty(), Optional.empty()))));
        }

        private AdvancementHolder genKillGargantuar(AdvancementHolder parent) {
            return gen(parent, "adventure/kill_gargantuar",
                    of(PvZItemRegister.POLE.get()),
                    "advancement.pvz.kill_gargantuar", "advancement.pvz.kill_gargantuar.desc",
                    AdvancementType.TASK, null, true, true, false, null,
                    AdvancementRequirements.anyOf(List.of("zombie1", "zombie2")),
                    b -> {
                        b.addCriterion("zombie1",
                                KilledTrigger.TriggerInstance.playerKilledEntity(
                                        Optional.of(entityOf(PvZEntityRegister.GARGANTUAR.get()).build())));
                        b.addCriterion("zombie2",
                                KilledTrigger.TriggerInstance.playerKilledEntity(
                                        Optional.of(entityOf(PvZEntityRegister.GIGA_GARGANTUAR.get()).build())));
                    });
        }

        private AdvancementHolder genMissionExpert(AdvancementHolder parent) {
            return gen(parent, "adventure/mission_expert",
                    new ItemStackTemplate(Items.IRON_SWORD),
                    "advancement.pvz.mission_expert", "advancement.pvz.mission_expert.desc",
                    AdvancementType.GOAL, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.INVASION_MISSION.get().createCriterion(
                                    new InvasionMissionTrigger.Instance(
                                            Optional.empty(),
                                            new AmountPredicate(MinMaxBounds.Ints.atLeast(10))))));
        }

        private AdvancementHolder genMissionMaster(AdvancementHolder parent) {
            return gen(parent, "adventure/mission_master",
                    new ItemStackTemplate(Items.DIAMOND_SWORD),
                    "advancement.pvz.mission_master", "advancement.pvz.mission_master.desc",
                    AdvancementType.CHALLENGE, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.INVASION_MISSION.get().createCriterion(
                                    new InvasionMissionTrigger.Instance(
                                            Optional.empty(),
                                            new AmountPredicate(MinMaxBounds.Ints.atLeast(50))))));
        }

        private AdvancementHolder genMustacheMode(AdvancementHolder parent) {
            return gen(parent, "adventure/mustache_mode",
                    of(PvZItemRegister.ZOMBIE_DOLL.get()),
                    "advancement.pvz.mustache_mode", "advancement.pvz.mustache_mode.desc",
                    AdvancementType.TASK, null, true, true, false, null,
                    AdvancementRequirements.allOf(List.of("zombie1", "zombie2", "zombie3", "zombie4")),
                    b -> {
                        b.addCriterion("zombie1", mustacheKill(PvZEntityRegister.NORMAL_ZOMBIE.get()));
                        b.addCriterion("zombie2", mustacheKill(PvZEntityRegister.CONEHEAD_ZOMBIE.get()));
                        b.addCriterion("zombie3", mustacheKill(PvZEntityRegister.FLAG_ZOMBIE.get()));
                        b.addCriterion("zombie4", mustacheKill(PvZEntityRegister.BUCKETHEAD_ZOMBIE.get()));
                    });
        }

        private Criterion<KilledTrigger.TriggerInstance> mustacheKill(EntityType<?> type) {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("zombie_type", 2);
            HolderGetter<EntityType<?>> entityTypes = this.output.lookup(Registries.ENTITY_TYPE);
            return KilledTrigger.TriggerInstance.playerKilledEntity(
                    Optional.of(EntityPredicate.Builder.entity()
                            .of(entityTypes, type)
                            .nbt(new NbtPredicate(nbt))
                            .build()));
        }

        private AdvancementHolder genYeti(AdvancementHolder parent) {
            return gen(parent, "adventure/yeti",
                    new ItemStackTemplate(Items.SNOW_BLOCK),
                    "advancement.pvz.yeti", "advancement.pvz.yeti.desc",
                    AdvancementType.TASK, null, true, true, false, null, null,
                    b -> b.addCriterion("zombie1",
                            KilledTrigger.TriggerInstance.playerKilledEntity(
                                    Optional.of(entityOf(PvZEntityRegister.YETI_ZOMBIE.get()).build()))));
        }

        private AdvancementHolder genLuckyDog(AdvancementHolder parent) {
            return gen(parent, "adventure/lucky_dog",
                    of(PvZBlockRegister.SLOT_MACHINE),
                    "advancement.pvz.lucky_dog", "advancement.pvz.lucky_dog.desc",
                    AdvancementType.CHALLENGE, null, true, true, false, null, null,
                    b -> b.addCriterion("shot_arrow",
                            PvZTriggers.SLOT_MACHINE.get().createCriterion(
                                    new SlotMachineTrigger.Instance(
                                            Optional.empty(),
                                            new AmountPredicate(MinMaxBounds.Ints.atLeast(3)),
                                            new StringPredicate("jewel")))));
        }

        private AdvancementHolder genZenGarden(AdvancementHolder parent) {
            return gen(parent, "adventure/zen_garden",
                    of(PvZBlockRegister.NUT_SAPLING.get()),
                    "advancement.pvz.zen_garden", "advancement.pvz.zen_garden.desc",
                    AdvancementType.TASK, null, false, true, false, null, null,
                    b -> {
                        HolderGetter<Biome> biomeLookup = this.output.lookup(Registries.BIOME);
                        Holder<Biome> zenGarden = biomeLookup.getOrThrow(PvZBiome.ZEN_GARDEN);
                        b.addCriterion("reach",
                                PlayerTrigger.TriggerInstance.located(
                                        EntityPredicate.Builder.entity().located(
                                                LocationPredicate.Builder.location()
                                                        .setDimension(overworld())
                                                        .setBiomes(HolderSet.direct(zenGarden)))));
                    });
        }
    }
}