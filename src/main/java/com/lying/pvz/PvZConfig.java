package com.lying.pvz;

import net.neoforged.neoforge.common.ModConfigSpec;

public class PvZConfig {

    public static Local LOCAL_CONFIG;
    public static Client CLIENT_CONFIG;

    public static class Local {

        public Local(ModConfigSpec.Builder builder) {
            /* Invasion Settings */
            builder.comment("Settings about invasion.").translation("config.pvz.invasion.title").push("Invasion Settings");
            {
                InvasionSettings.SafeDayLength = builder
                        .translation("config.pvz.invasion.safe_day")
                        .comment("If you set to 5, then the first 5 * 20 minutes of the world will not have any zombie invasion event.")
                        .defineInRange("SafeDayLength", 4, 0, 1000000);

                InvasionSettings.ScatterInvasions = builder
                        .translation("config.pvz.invasion.scatter_invasions")
                        .comment("If on, invasions of different players will happen in different days.")
                        .define("ScatterInvasions", false);

                InvasionSettings.InvasionIntervalLength = builder
                        .translation("config.pvz.invasion.interval")
                        .comment("The interval day length between each invasion.")
                        .defineInRange("InvasionIntervalLength", 2, 0, 1000000);

                InvasionSettings.ShowEventMessages = builder
                        .translation("config.pvz.invasion.show_message")
                        .comment("If true, you will receive detail message about each event when zombie invasion happened.")
                        .define("ShowEventMessages", true);

                InvasionSettings.EnableHugeWave = builder
                        .translation("config.pvz.invasion.enable_wave")
                        .comment("If true, players will be invaded by a huge wave of zombies in zombie invasion day")
                        .define("EnableHugeWave", true);

                InvasionSettings.MaxSpawnEachPlayer = builder
                        .translation("config.pvz.invasion.spawn_count")
                        .comment("how many amount of entity will spawn to player.")
                        .defineInRange("MaxSpawnCount", 30, 1, 1000);

                InvasionSettings.MaxSpawnRange = builder
                        .translation("config.pvz.invasion.spawn_range")
                        .comment("how far can entity spawn in invasion.")
                        .defineInRange("MaxSpawnRange", 50, 1, 100);
            }
            builder.pop();

            /* Challenge Settings */
            builder.comment("Settings about challenge").translation("config.pvz.challenge.title").push("Challenge Settings");
            {
                ChallengeSettings.EnableChallenge = builder
                        .comment("Turn to false, no new challenge will happen, and old challenges will be clear.")
                        .translation("config.pvz.enable_challenge")
                        .define("EnableChallenge", true);

                ChallengeSettings.ChallengeWaitTime = builder
                        .comment("how many ticks will challenge wait to remove, when there is no player in it.")
                        .translation("config.pvz.challenge_wait_time")
                        .defineInRange("ChallengeWaitTime", 400, 1, 1000000);

                ChallengeSettings.ChallengeRange = builder
                        .comment("how far will a player join the raid.")
                        .translation("config.pvz.challenge_range")
                        .defineInRange("ChallengeRange", 50, 1, 1000);
            }
            builder.pop();

            //World Settings.
            builder.comment("Settings about global rules.").translation("config.pvz.rule.title").push("Rule Settings");
            {
                RuleSettings.CanSpawnDefaultMonster = builder
                        .translation("config.pvz.rule.spawn_monster")
                        .comment("if turn to false, there will have no monster of other monsters spawn in overworld except pvz zombies.")
                        .define("CanSpawnDefaultMonster", true);

                RuleSettings.AllZombieGiveXP = builder
                        .translation("config.pvz.rule.all_zombie_give_xp")
                        .comment("if turn to false, only invasion zombies will give tree xp.")
                        .define("AllZombieGiveXP", false);

                RuleSettings.DespawnOwnedEntityRange = builder
                        .translation("config.pvz.rule.despawn_owned_entity_range")
                        .comment("plants summoned by players will be naturally despawned if the distance to the player's respawn anchor is farther than this number.",
                                "null respawn anchor (e.g. player never slept) disables despawn.",
                                "set to -1 to disable despawn entirely.")
                        .defineInRange("DespawnOwnedEntityRange", 1000, -1, 100000);

                RuleSettings.GiveBeginnerReward = builder
                        .translation("config.pvz.rule.beginner_reward")
                        .comment("If you set it true, you will get some basic plantcards when you first join world.")
                        .define("GiveBeginnerReward", true);

                RuleSettings.AllowNaturalTurnOrigin = builder
                        .translation("config.pvz.rule.turn_origin")
                        .comment("If turn to false, saplings no longer to grow to Origin Ore naturally, except there is a block above it.")
                        .define("AllowNaturalTurnOrigin", true);

                RuleSettings.TeamAttack = builder
                        .translation("config.pvz.rule.team_attack")
                        .comment("if turn to true, when plant's owner is in a team, the plant will attack the entity from other team(include players).")
                        .define("PlantAttackTeam", false);

                RuleSettings.LimitPlantCount = builder
                        .translation("config.pvz.rule.plant_count")
                        .comment("how many plants can you place in 30 x 30 area without increasing cost.")
                        .defineInRange("LimitPlantCount", 50, 10, 1000);

                RuleSettings.NeedUnlockToPlant = builder
                        .translation("config.pvz.rule.unlock_plant")
                        .comment("if on, plant needs to be unlocked before planting.")
                        .define("NeedUnlockToPlant", true);

                RuleSettings.KeepSunWhenDie = builder
                        .translation("config.pvz.rule.keep_sun")
                        .comment("if turn to true, player will keep its sun after death.")
                        .define("KeepSunWhenDie", false);

                RuleSettings.MaxDamageLimit = builder
                        .translation("config.pvz.rule.max_damage")
                        .comment("how many damage could plants and zombies deal to other living entity.")
                        .defineInRange("MaxDamageLimit", 20, 0, 100000);
            }
            builder.pop();

            //World Settings
            builder.comment("Settings about world.").translation("config.pvz.world.title").push("World Settings");
            {
                builder.comment("Settings about the biome gen.").translation("config.pvz.world.biome.title").push("Biome Settings");
                {
                    WorldSettings.EnableBiome = builder
                            .translation("config.pvz.world.enable_biome")
                            .comment("If true, pvz:zen_garden and pvz:night will be injected into overworld biome generation.",
                                    "Disable this when another mod's overworld biome mixin conflicts with ours,",
                                    "or when you do not want Biome to appear at all.")
                            .define("EnableZenGarden", true);
                }
                builder.pop();
            }
            builder.pop();

            //Entity Settings
            builder.comment("Settings about entities.").translation("config.pvz.entity.title").push("Entity Settings");
            {
                builder.comment("Settings about players.").translation("config.pvz.player.title").push("Player Settings");
                {
                    EntitySettings.PlayerInitialGroup = builder
                            .translation("config.pvz.player.initial_group")
                            .comment("Player Initial Group When they join the world for the first time(-2 means other monsters, -1 means zombies, 0 means neutral creatures, 1 means plants and 2 means other guards).")
                            .defineInRange("PlayerInitialGroup", 1, -2, 2);

                    EntitySettings.PlayerBaseSunAmount = builder
                            .translation("config.pvz.player.initial_sun")
                            .comment("players' base sun amount, it will increase when tree maxLevel increasing.")
                            .defineInRange("PlayerInitialSun", 800, 100, 10000);
                }
                builder.pop();

                builder.comment("Settings about zombies.").translation("config.pvz.zombie.title").push("Zombie Settings");
                {
                    EntitySettings.ZombieSetting.ZombieSuperChance = builder
                            .translation("config.pvz.zombie.super_chance")
                            .comment("the spawn chance of zombie with plant energy(the bigger,the more chance it spawn).")
                            .defineInRange("ZombieSuperChance", 1, 0, 40);

                    EntitySettings.ZombieSetting.ZombieSunChance = builder
                            .translation("config.pvz.zombie.sun_chance")
                            .comment("the spawn chance of zombie with sun layer(the bigger,the more chance it spawn).")
                            .defineInRange("ZombieSunChance", 1, 0, 40);

                    EntitySettings.DropChanceMultiper = builder
                            .translation("config.pvz.zombie.drop_multiper")
                            .comment("the drop chance of coin when zombie die(the bigger,the less chance it spawn).")
                            .defineInRange("ZombieDropMultiper", 10, 3, 100);

                    EntitySettings.ZombieSetting.EnableZombieDropHands = builder
                            .translation("config.pvz.zombie.drop_part")
                            .comment("enable zombies to drop hands and heads when they got hurt(turn false to disable).")
                            .define("EnableZombieDropParts", true);

                    EntitySettings.ZombieSetting.JackinboxBreak = builder
                            .translation("config.pvz.zombie.jack_break")
                            .comment("if on, jack-in-box zombie will break blocks nearby when explode.")
                            .define("JackInBoxZombieBreakBlocks", false);

                    EntitySettings.ZombieSetting.ImmuneDamage = builder
                            .translation("config.pvz.zombie.immune_damage")
                            .comment("if on, fire & explode damages will not hurt most of the zombies.")
                            .define("ImmuneToEnvironmentDamage", true);
                }
                builder.pop();

                builder.comment("Settings about plants.").translation("config.pvz.plant.title").push("Plant Settings");
                {
                    EntitySettings.PlantSetting.StrangeCatCount = builder
                            .translation("config.pvz.plant.strange_cat_count")
                            .comment("the max number StrangeCats can copy themselves in a range of 20 * 20.")
                            .defineInRange("StrangeCatCount", 10, 0, 100);

                    EntitySettings.PlantSetting.PlantBulletMerge = builder
                            .translation("config.pvz.plant.plant_bullet_merge")
                            .comment("whether two same bullets merge into one when coincide.")
                            .define("PlantBulletMerge", false);

                    EntitySettings.PlantSetting.PlantMinimumLiveTick = builder
                            .translation("config.pvz.plant.plant_minimum_live_tick")
                            .comment("plants won't despawn before living for this long.")
                            .defineInRange("PlantMinimumLiveTick", 2000, 0, 10000000);
                }
                builder.pop();

                builder.comment("The Max live time for Entity like sun.").translation("config.pvz.entity.live_time.title").push("EntityLiveTime");
                {
                    EntitySettings.EntityLiveTick.SunLiveTick = builder
                            .translation("config.pvz.entity.sun_live_tick")
                            .comment("how many ticks can the sun entity live.")
                            .defineInRange("SunLiveTick", 500, 1, 1200);

                    EntitySettings.EntityLiveTick.CoinLiveTick = builder
                            .translation("config.pvz.entity.coin_live_tick")
                            .comment("how many ticks can the coin entity live.")
                            .defineInRange("CoinLiveTick", 400, 1, 1200);

                    EntitySettings.EntityLiveTick.JewelLiveTick = builder
                            .translation("config.pvz.entity.jewel_live_tick")
                            .comment("how many ticks can the jewel entity live.")
                            .defineInRange("JewelLiveTick", 500, 1, 1200);

                    EntitySettings.EntityLiveTick.EnergyLiveTick = builder
                            .translation("config.pvz.entity.plant_food_live_tick")
                            .comment("how many ticks can the energy entity live.")
                            .defineInRange("PlantFoodLiveTick", 400, 1, 1200);

                    EntitySettings.EntityLiveTick.YetiLiveTick = builder
                            .translation("config.pvz.entity.yeti_live_tick")
                            .comment("how many ticks can yeti entity live(how long will it stay).")
                            .defineInRange("YetiLiveTick", 2400, 1, 1000000);

                    EntitySettings.EntityLiveTick.LawnMowerLiveTick = builder
                            .translation("config.pvz.entity.lawn_mower_live_tick")
                            .comment("how many ticks can lawnmower entity live.")
                            .defineInRange("LawnMowerLiveTick", 200, 1, 1000000);

                    EntitySettings.EntityLiveTick.ElementBallLiveTick = builder
                            .translation("config.pvz.entity.element_ball_live_tick")
                            .comment("how many ticks can element ball entity live.")
                            .defineInRange("ElementBallLiveTick", 600, 1, 1000000);
                }
                builder.pop();
            }
            builder.pop();

            //Block Settings
            builder.comment("Settings about blocks.").translation("config.pvz.block.title").push("Block Settings");
            {
                BlockSettings.OriginEffectChance = builder
                        .translation("config.pvz.block.origin_effect_chance")
                        .comment("The chance to get Essence Ore from Origin Block's effect")
                        .defineInRange("OriginEffectChance", 0.25, 0, 1);

                BlockSettings.SaplingTurnChance = builder
                        .translation("config.pvz.block.sapling_turn_chance")
                        .comment("The chance when sapling turn to origin ore")
                        .defineInRange("SaplingTurnChance", 0.15, 0, 1);

                BlockSettings.AmethystAngerChance = builder
                        .translation("config.pvz.block.amethyst_anger_chance")
                        .comment("The chance to anger nearby endermen when break amethyst ore")
                        .defineInRange("AmethystAngerChance", 0.4, 0, 1);

                builder.comment("Setting about break blocks.").translation("config.pvz.block.break.title").push("Break Block Setting");
                {
                    BlockSettings.PeaDropChance = builder
                            .translation("config.pvz.block.pea_drop_chance")
                            .comment("the drop chance of pea when you break grass")
                            .defineInRange("PeaDropChance", 0.05, 0, 1);

                    BlockSettings.CabbageDropChance = builder
                            .translation("config.pvz.block.cabbage_drop_chance")
                            .comment("the drop chance of cabbage when you break grass")
                            .defineInRange("CabbageDropChance", 0.025, 0, 1);
                }
                builder.pop();
            }
            builder.pop();

            //Item Settings
            builder.comment("Settings about items.").translation("config.pvz.item.title").push("Item Settings");
            {
                ItemSettings.JackBoxSurpriseChance = builder
                        .translation("config.pvz.item.jack_surprise_chance")
                        .comment("The chance when player got a surprise when use jack box.the bigger the value is,the lower chance you get.(more specificly 1/x)")
                        .defineInRange("JackBoxSurpriseChance", 10, 1, 1000000);
            }
            builder.pop();

            /* BHTPvZ Addon Settings —— 独立分组，不合并 */
//            builder.comment("Settings about BHTPvZ addon.")
//                    .translation("config.pvz.bhtpvz.title")
//                    .push("BHTPvZ");
//            {
//                this.BHTPvZ = new BHTPvZConfig.Local(builder);
//            }
//            builder.pop();
        }

        public InvasionSettings InvasionSettings = new InvasionSettings();
        public ChallengeSettings ChallengeSettings = new ChallengeSettings();
        public RuleSettings RuleSettings = new RuleSettings();
        public WorldSettings WorldSettings = new WorldSettings();
        public EntitySettings EntitySettings = new EntitySettings();
        public BlockSettings BlockSettings = new BlockSettings();
        public ItemSettings ItemSettings = new ItemSettings();
        //public BHTPvZConfig.Local BHTPvZ;



        public static class InvasionSettings {
            public ModConfigSpec.IntValue SafeDayLength;
            public ModConfigSpec.BooleanValue ScatterInvasions;
            public ModConfigSpec.IntValue InvasionIntervalLength;
            public ModConfigSpec.BooleanValue ShowEventMessages;
            public ModConfigSpec.BooleanValue EnableHugeWave;
            public ModConfigSpec.IntValue MaxSpawnEachPlayer;
            public ModConfigSpec.IntValue MaxSpawnRange;
        }

        public static class ChallengeSettings {
            public ModConfigSpec.BooleanValue EnableChallenge;
            public ModConfigSpec.IntValue ChallengeWaitTime;
            public ModConfigSpec.IntValue ChallengeRange;
        }

        public static class RuleSettings {
            public ModConfigSpec.BooleanValue CanSpawnDefaultMonster;
            public ModConfigSpec.BooleanValue AllZombieGiveXP;
            public ModConfigSpec.IntValue DespawnOwnedEntityRange;
            public ModConfigSpec.BooleanValue GiveBeginnerReward;
            public ModConfigSpec.BooleanValue AllowNaturalTurnOrigin;
            public ModConfigSpec.BooleanValue TeamAttack;
            public ModConfigSpec.IntValue LimitPlantCount;
            public ModConfigSpec.BooleanValue NeedUnlockToPlant;
            public ModConfigSpec.BooleanValue KeepSunWhenDie;
            public ModConfigSpec.IntValue MaxDamageLimit;
        }

        public static class WorldSettings {
            public ModConfigSpec.BooleanValue EnableBiome;
        }

        public static class EntitySettings {
            public EntityLiveTick EntityLiveTick = new EntityLiveTick();
            public ZombieSetting ZombieSetting = new ZombieSetting();
            public PlantSetting PlantSetting = new PlantSetting();

            public ModConfigSpec.IntValue PlayerInitialGroup;
            public ModConfigSpec.IntValue PlayerBaseSunAmount;
            public ModConfigSpec.IntValue DropChanceMultiper;

            public static class ZombieSetting {
                public ModConfigSpec.IntValue ZombieSuperChance;
                public ModConfigSpec.IntValue ZombieSunChance;
                public ModConfigSpec.BooleanValue EnableZombieDropHands;
                public ModConfigSpec.BooleanValue JackinboxBreak;
                public ModConfigSpec.BooleanValue ImmuneDamage;
            }

            public static class PlantSetting {
                public ModConfigSpec.IntValue StrangeCatCount;
                public ModConfigSpec.BooleanValue PlantBulletMerge;
                public ModConfigSpec.IntValue PlantMinimumLiveTick;
            }

            public static class EntityLiveTick {
                public ModConfigSpec.IntValue SunLiveTick;
                public ModConfigSpec.IntValue CoinLiveTick;
                public ModConfigSpec.IntValue JewelLiveTick;
                public ModConfigSpec.IntValue EnergyLiveTick;
                public ModConfigSpec.IntValue YetiLiveTick;
                public ModConfigSpec.IntValue LawnMowerLiveTick;
                public ModConfigSpec.IntValue ElementBallLiveTick;
            }
        }

        public static class BlockSettings {
            public ModConfigSpec.DoubleValue OriginEffectChance;
            public ModConfigSpec.DoubleValue SaplingTurnChance;
            public ModConfigSpec.DoubleValue PeaDropChance;
            public ModConfigSpec.DoubleValue CabbageDropChance;
            public ModConfigSpec.DoubleValue AmethystAngerChance;
        }

        public static class ItemSettings {
            public ModConfigSpec.IntValue JackBoxSurpriseChance;
        }
    }

    public static class Client {

        public Client(ModConfigSpec.Builder builder) {
            builder.comment("Overlay Settings").translation("config.pvz.overlay.title").push("Overlay Settings");
            {
                OverlaySettings.RenderSunBar = builder
                        .comment("turn to false to cancel the display of sun amount bar.")
                        .translation("config.pvz.overlay.sun_bar")
                        .define("RenderSunAmountBar", true);

                OverlaySettings.RenderMoneyBar = builder
                        .comment("turn to false to cancel the display of money bar.")
                        .translation("config.pvz.overlay.money_bar")
                        .define("RenderMoneyBar", true);

                OverlaySettings.RenderGemBar = builder
                        .comment("turn to false to cancel the display of gem bar.")
                        .translation("config.pvz.overlay.gem_bar")
                        .define("RenderGemBar", true);

                OverlaySettings.RenderTreeLevel = builder
                        .comment("turn to false to cancel the display of tree maxLevel bar.")
                        .translation("config.pvz.overlay.level_bar")
                        .define("RenderTreeLevelBar", true);

                OverlaySettings.RenderPlantFoodBar = builder
                        .comment("turn to false to cancel the display of plant food bar.")
                        .translation("config.pvz.overlay.energy_bar")
                        .define("RenderPlantFoodBar", true);

                OverlaySettings.RenderInvasionProgress = builder
                        .comment("turn to false to cancel the display of invasion progress.")
                        .translation("config.pvz.overlay.invasion_bar")
                        .define("RenderInvasionProgress", true);

                OverlaySettings.RenderFog = builder
                        .comment("turn to false to cancel the display of fog.")
                        .translation("config.pvz.overlay.fog")
                        .define("RenderFog", true);

                OverlaySettings.RenderCardSlots = builder
                        .comment("turn to false to cancel the display of summon card slots.")
                        .translation("config.pvz.overlay.card_slot")
                        .define("RenderCardSlots", true);
            }
            builder.pop();

            builder.comment("Other Render Settings").translation("config.pvz.client.other.title").push("Other Render Settings");
            {
                OtherSettings.ShowPVZMainMenu = builder
                        .translation("config.pvz.client.pvz_menu")
                        .comment("show pvz main menu")
                        .define("ShowPVZMainMenu", true);
            }
            builder.pop();
        }

        public OverlaySettings OverlaySettings = new OverlaySettings();
        public OtherSettings OtherSettings = new OtherSettings();

        public static class OverlaySettings {
            public ModConfigSpec.BooleanValue RenderSunBar;
            public ModConfigSpec.BooleanValue RenderMoneyBar;
            public ModConfigSpec.BooleanValue RenderGemBar;
            public ModConfigSpec.BooleanValue RenderTreeLevel;
            public ModConfigSpec.BooleanValue RenderPlantFoodBar;
            public ModConfigSpec.BooleanValue RenderInvasionProgress;
            public ModConfigSpec.BooleanValue RenderFog;
            public ModConfigSpec.BooleanValue RenderCardSlots;
        }

        public static class OtherSettings {
            public ModConfigSpec.BooleanValue ShowPVZMainMenu;
        }
    }
}