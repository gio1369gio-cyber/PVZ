package com.lying.pvz.common.impl.plant;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.common.impl.CoolDowns;
import com.lying.pvz.common.impl.EssenceTypes;
import com.lying.pvz.common.impl.PVZRegistryBootstrap;
import com.lying.pvz.common.impl.Placements;
import com.lying.pvz.common.impl.RankTypes;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.type.PlantTypeBuilder;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.level.entity.plant.defence.PumpkinEntity;
import com.lying.pvz.common.level.entity.plant.defence.SteelPumpkinEntity;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 主模组内置的植物类型。
 *
 * <p>通过 {@link PVZRegistryBootstrap#PLANTS} 注册到 {@code pvz:plant_type} registry。
 * 主模组需要在 mod 构造期调用一次 {@link #init()} 触发类加载。
 *
 * <p><b>客户端渲染</b>：模型 / 贴图 / 缩放等由客户端按 {@link IPlantType#getID()} 绑定，
 * 不在本类。
 */
public final class PVZPlants {

    /** 绝大多数植物通用的基础技能：减阳光、加生命、减冷却。 */
    private static final List<Supplier<ISkillType>> COMMON_SKILLS = List.of(
            SkillTypes.LESS_SUN, SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD
    );

    /* ------------------------------------------------------------------ */
    /* grass day                                                           */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<IPlantType, IPlantType> PEA_SHOOTER = plant("pea_shooter", b -> b
            .sunCost(100).requiredLevel(1)
            .coolDown(CoolDowns.SUPER_FAST).rank(RankTypes.WHITE).essence(EssenceTypes.APPEASE)
            .entityType(PvZEntityRegister.PEA_SHOOTER::get)
            .summonCard(PvZItemRegister.PEA_SHOOTER_CARD)
            .enjoyCard(PvZItemRegister.PEA_SHOOTER_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.PEA_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> CHERRY_BOMB = plant("cherry_bomb", b -> b
            .sunCost(150).requiredLevel(30)
            .coolDown(CoolDowns.HUGE_SLOW).rank(RankTypes.GOLD).essence(EssenceTypes.EXPLOSION)
            .entityType(PvZEntityRegister.CHERRY_BOMB::get)
            .summonCard(PvZItemRegister.CHERRY_BOMB_CARD)
            .enjoyCard(PvZItemRegister.CHERRY_BOMB_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.NORMAL_BOMB_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> WALL_NUT = plant("wall_nut", b -> b
            .sunCost(50).requiredLevel(1)
            .coolDown(CoolDowns.SLOW).rank(RankTypes.WHITE).essence(EssenceTypes.DEFENCE)
            .entityType(PvZEntityRegister.WALL_NUT::get)
            .summonCard(PvZItemRegister.WALL_NUT_CARD)
            .enjoyCard(PvZItemRegister.WALL_NUT_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.NUT_MORE_LIFE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> EXPLODE_O_NUT = plant("explode_o_nut", b -> b
            .sunCost(125).requiredLevel(20)
            .coolDown(CoolDowns.HUGE_SLOW).rank(RankTypes.GOLD).essence(EssenceTypes.EXPLOSION)
            .entityType(PvZEntityRegister.EXPLODE_O_NUT::get)
            .summonCard(PvZItemRegister.EXPLODE_O_NUT_CARD)
            .enjoyCard(PvZItemRegister.EXPLODE_O_NUT_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.NUT_MORE_LIFE)
    );

    public static final DeferredHolder<IPlantType, IPlantType> GIANT_NUT = plant("giant_nut", b -> b
            .sunCost(75).requiredLevel(10)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.BLUE).essence(EssenceTypes.DEFENCE)
            .entityType(PvZEntityRegister.GIANT_NUT::get)
            .summonCard(PvZItemRegister.GIANT_NUT_CARD)
            .enjoyCard(PvZItemRegister.GIANT_NUT_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.NUT_MORE_LIFE)
    );

    public static final DeferredHolder<IPlantType, IPlantType> POTATO_MINE = plant("potato_mine", b -> b
            .sunCost(25).requiredLevel(1)
            .coolDown(CoolDowns.LITTLE_SLOW).rank(RankTypes.WHITE).essence(EssenceTypes.EXPLOSION)
            .entityType(PvZEntityRegister.POTATO_MINE::get)
            .summonCard(PvZItemRegister.POTATO_MINE_CARD)
            .enjoyCard(PvZItemRegister.POTATO_MINE_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD,
                            SkillTypes.NORMAL_BOMB_DAMAGE, SkillTypes.MINE_FAST_PREPARE)
            .placement(Placements.STABLE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> SNOW_PEA = plant("snow_pea", b -> b
            .sunCost(175).requiredLevel(12)
            .coolDown(CoolDowns.SUPER_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.ICE)
            .entityType(PvZEntityRegister.SNOW_PEA::get)
            .summonCard(PvZItemRegister.SNOW_PEA_CARD)
            .enjoyCard(PvZItemRegister.SNOW_PEA_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.PEA_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> CHOMPER = plant("chomper", b -> b
            .sunCost(150).requiredLevel(24)
            .coolDown(CoolDowns.NORMAL).rank(RankTypes.GOLD).essence(EssenceTypes.ENFORCE)
            .entityType(PvZEntityRegister.CHOMPER::get)
            .summonCard(PvZItemRegister.CHOMPER_CARD)
            .enjoyCard(PvZItemRegister.CHOMPER_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.NORMAL_ENHANCE_STRENGTH)
            );

    /* ------------------------------------------------------------------ */
    /* grass night                                                         */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<IPlantType, IPlantType> PUFF_SHROOM = plant("puff_shroom", b -> b
            .sunCost(10).requiredLevel(21)
            .coolDown(CoolDowns.SUPER_FAST).rank(RankTypes.WHITE).essence(EssenceTypes.TOXIC)
            .entityType(PvZEntityRegister.PUFF_SHROOM::get)
            .summonCard(PvZItemRegister.PUFF_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.PUFF_SHROOM_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.SPORE_DAMAGE)
            .isShroomPlant()
            );

    public static final DeferredHolder<IPlantType, IPlantType> SUN_SHROOM = plant("sun_shroom", b -> b
            .sunCost(25).requiredLevel(21)
            .coolDown(CoolDowns.FAST).rank(RankTypes.GOLD).essence(EssenceTypes.LIGHT)
            .entityType(PvZEntityRegister.SUN_SHROOM::get)
            .summonCard(PvZItemRegister.SUN_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.SUN_SHROOM_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD)
            .isShroomPlant()
            );

    public static final DeferredHolder<IPlantType, IPlantType> GRAVE_BUSTER = plant("grave_buster", b -> b
            .sunCost(75).requiredLevel(7)
            .coolDown(CoolDowns.SUPER_FAST).rank(RankTypes.WHITE).essence(EssenceTypes.ASSIST)
            .entityType(PvZEntityRegister.GRAVE_BUSTER::get)
            .summonCard(PvZItemRegister.GRAVE_BUSTER_CARD)
            .enjoyCard(PvZItemRegister.GRAVE_BUSTER_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD)
            );

    public static final DeferredHolder<IPlantType, IPlantType> HYPNO_SHROOM = plant("hypno_shroom", b -> b
            .sunCost(75).requiredLevel(32)
            .coolDown(CoolDowns.LITTLE_SLOW).rank(RankTypes.GOLD).essence(EssenceTypes.MAGIC)
            .entityType(PvZEntityRegister.HYPNO_SHROOM::get)
            .summonCard(PvZItemRegister.HYPNO_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.HYPNO_SHROOM_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD)
            .isShroomPlant()
            );

    public static final DeferredHolder<IPlantType, IPlantType> SCAREDY_SHROOM = plant("scaredy_shroom", b -> b
            .sunCost(25).requiredLevel(29)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.WHITE).essence(EssenceTypes.TOXIC)
            .entityType(PvZEntityRegister.SCAREDY_SHROOM::get)
            .summonCard(PvZItemRegister.SCAREDY_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.SCAREDY_SHROOM_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.SPORE_DAMAGE)
            .isShroomPlant()
            );

    public static final DeferredHolder<IPlantType, IPlantType> ICE_SHROOM = plant("ice_shroom", b -> b
            .sunCost(75).requiredLevel(54)
            .coolDown(CoolDowns.SLOW).rank(RankTypes.BLUE).essence(EssenceTypes.ICE)
            .entityType(PvZEntityRegister.ICE_SHROOM::get)
            .summonCard(PvZItemRegister.ICE_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.ICE_SHROOM_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD)
            .isShroomPlant()
            );

    public static final DeferredHolder<IPlantType, IPlantType> DOOM_SHROOM = plant("doom_shroom", b -> b
            .sunCost(225).requiredLevel(55)
            .coolDown(CoolDowns.SUPER_SLOW).rank(RankTypes.BLACK).essence(EssenceTypes.EXPLOSION)
            .entityType(PvZEntityRegister.DOOM_SHROOM::get)
            .summonCard(PvZItemRegister.DOOM_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.DOOM_SHROOM_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.HIGH_EXPLODE_DAMAGE)
            .isShroomPlant()
            );

    /* ------------------------------------------------------------------ */
    /* pool day                                                            */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<IPlantType, IPlantType> SQUASH = plant("squash", b -> b
            .sunCost(50).requiredLevel(37)
            .coolDown(CoolDowns.LITTLE_SLOW).rank(RankTypes.GREEN).essence(EssenceTypes.ENFORCE)
            .entityType(PvZEntityRegister.SQUASH::get)
            .summonCard(PvZItemRegister.SQUASH_CARD)
            .enjoyCard(PvZItemRegister.SQUASH_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.NORMAL_ENHANCE_STRENGTH)
            );

    public static final DeferredHolder<IPlantType, IPlantType> THREE_PEATER = plant("three_peater", b -> b
            .sunCost(325).requiredLevel(33)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.APPEASE)
            .entityType(PvZEntityRegister.THREE_PEATER::get)
            .summonCard(PvZItemRegister.THREE_PEATER_CARD)
            .enjoyCard(PvZItemRegister.THREE_PEATER_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.PEA_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> TANGLE_KELP = plant("tangle_kelp", b -> b
            .sunCost(25).requiredLevel(7)
            .coolDown(CoolDowns.LITTLE_FAST).rank(RankTypes.WHITE).essence(EssenceTypes.ENFORCE)
            .entityType(PvZEntityRegister.TANGLE_KELP::get)
            .summonCard(PvZItemRegister.TANGLE_KELP_CARD)
            .enjoyCard(PvZItemRegister.TANGLE_KELP_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.NORMAL_ENHANCE_STRENGTH)
            .isWaterPlant()
            );

    public static final DeferredHolder<IPlantType, IPlantType> JALAPENO = plant("jalapeno", b -> b
            .sunCost(175).requiredLevel(45)
            .coolDown(CoolDowns.HUGE_SLOW).rank(RankTypes.BLUE).essence(EssenceTypes.FLAME)
            .entityType(PvZEntityRegister.JALAPENO::get)
            .summonCard(PvZItemRegister.JALAPENO_CARD)
            .enjoyCard(PvZItemRegister.JALAPENO_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.NORMAL_BOMB_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> TORCH_WOOD = plant("torch_wood", b -> b
            .sunCost(200).requiredLevel(35)
            .coolDown(CoolDowns.NORMAL).rank(RankTypes.PURPLE).essence(EssenceTypes.FLAME)
            .entityType(PvZEntityRegister.TORCH_WOOD::get)
            .summonCard(PvZItemRegister.TORCH_WOOD_CARD)
            .enjoyCard(PvZItemRegister.TORCH_WOOD_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.WOOD_MORE_LIFE,
                            SkillTypes.HEAT_PEA_RANGE, SkillTypes.LESS_SUN)
            );

    public static final DeferredHolder<IPlantType, IPlantType> TALL_NUT = plant("tall_nut", b -> b
            .sunCost(125).requiredLevel(25)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.BLUE).essence(EssenceTypes.DEFENCE)
            .entityType(PvZEntityRegister.TALL_NUT::get)
            .summonCard(PvZItemRegister.TALL_NUT_CARD)
            .enjoyCard(PvZItemRegister.TALL_NUT_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.NUT_MORE_LIFE)
            );

    /* ------------------------------------------------------------------ */
    /* pool night                                                          */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<IPlantType, IPlantType> SEA_SHROOM = plant("sea_shroom", b -> b
            .sunCost(10).requiredLevel(20)
            .coolDown(CoolDowns.SUPER_FAST).rank(RankTypes.WHITE).essence(EssenceTypes.TOXIC)
            .entityType(PvZEntityRegister.SEA_SHROOM::get)
            .summonCard(PvZItemRegister.SEA_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.SEA_SHROOM_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.SPORE_DAMAGE)
            .isShroomPlant()
            .isWaterPlant()   //trait 同时含 SHROOM + WATER；placement 推导按 WATER 优先 → NONE，与旧版行为一致
            );

    public static final DeferredHolder<IPlantType, IPlantType> PLANTERN = plant("plantern", b -> b
            .sunCost(75).requiredLevel(5)
            .coolDown(CoolDowns.FAST).rank(RankTypes.WHITE).essence(EssenceTypes.LIGHT)
            .entityType(PvZEntityRegister.PLANTERN::get)
            .summonCard(PvZItemRegister.PLANTERN_CARD)
            .enjoyCard(PvZItemRegister.PLANTERN_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD,
                            SkillTypes.MORE_LIGHT_RANGE, SkillTypes.NIGHT_VISION)
            );

    public static final DeferredHolder<IPlantType, IPlantType> CACTUS = plant("cactus", b -> b
            .sunCost(125).requiredLevel(8)
            .coolDown(CoolDowns.HUGE_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.SPEAR)
            .entityType(PvZEntityRegister.CACTUS::get)
            .summonCard(PvZItemRegister.CACTUS_CARD)
            .enjoyCard(PvZItemRegister.CACTUS_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.MORE_THORN_DAMAGE)
            .placement(Placements.SAND)
            );

    public static final DeferredHolder<IPlantType, IPlantType> BLOVER = plant("blover", b -> b
            .sunCost(50).requiredLevel(19)
            .coolDown(CoolDowns.FAST).rank(RankTypes.GREEN).essence(EssenceTypes.ASSIST)
            .entityType(PvZEntityRegister.BLOVER::get)
            .summonCard(PvZItemRegister.BLOVER_CARD)
            .enjoyCard(PvZItemRegister.BLOVER_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.BLOW_STRENGTH)
            );

    public static final DeferredHolder<IPlantType, IPlantType> SPLIT_PEA = plant("split_pea", b -> b
            .sunCost(125).requiredLevel(20)
            .coolDown(CoolDowns.HUGE_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.APPEASE)
            .entityType(PvZEntityRegister.SPLIT_PEA::get)
            .summonCard(PvZItemRegister.SPLIT_PEA_CARD)
            .enjoyCard(PvZItemRegister.SPLIT_PEA_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD,
                            SkillTypes.PEA_DAMAGE, SkillTypes.SPLIT_DOUBLE_CHANCE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> STAR_FRUIT = plant("star_fruit", b -> b
            .sunCost(150).requiredLevel(31)
            .coolDown(CoolDowns.FAST).rank(RankTypes.GREEN).essence(EssenceTypes.APPEASE)
            .entityType(PvZEntityRegister.STAR_FRUIT::get)
            .summonCard(PvZItemRegister.STAR_FRUIT_CARD)
            .enjoyCard(PvZItemRegister.STAR_FRUIT_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.MORE_STAR_DAMAGE)
            );

    /* ------------------------------------------------------------------ */
    /* grass day — 含升级关系                                              */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<IPlantType, IPlantType> SUN_FLOWER = plant("sun_flower", b -> b
            .sunCost(50).requiredLevel(1)
            .coolDown(CoolDowns.NORMAL).rank(RankTypes.WHITE).essence(EssenceTypes.LIGHT)
            .entityType(PvZEntityRegister.SUN_FLOWER::get)
            .summonCard(PvZItemRegister.SUN_FLOWER_CARD)
            .enjoyCard(PvZItemRegister.SUN_FLOWER_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD)
            .upgradeTo(PVZPlants.TWIN_SUNFLOWER)
            );

    public static final DeferredHolder<IPlantType, IPlantType> REPEATER = plant("repeater", b -> b
            .sunCost(225).requiredLevel(15)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.GOLD).essence(EssenceTypes.APPEASE)
            .entityType(PvZEntityRegister.REPEATER::get)
            .summonCard(PvZItemRegister.REPEATER_CARD)
            .enjoyCard(PvZItemRegister.REPEATER_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.PEA_DAMAGE)
            .upgradeTo(PVZPlants.GATLING_PEA)
            );

    /* ------------------------------------------------------------------ */
    /* grass night                                                         */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<IPlantType, IPlantType> FUME_SHROOM = plant("fume_shroom", b -> b
            .sunCost(100).requiredLevel(22)
            .coolDown(CoolDowns.LITTLE_FAST).rank(RankTypes.GOLD).essence(EssenceTypes.TOXIC)
            .entityType(PvZEntityRegister.FUME_SHROOM::get)
            .summonCard(PvZItemRegister.FUME_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.FUME_SHROOM_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.SPORE_DAMAGE)
            .isShroomPlant()
            .upgradeTo(PVZPlants.GLOOM_SHROOM)
            );

    /* ------------------------------------------------------------------ */
    /* pool day                                                            */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<IPlantType, IPlantType> LILY_PAD = plant("lily_pad", b -> b
            .sunCost(25).requiredLevel(11)
            .coolDown(CoolDowns.SUPER_FAST).rank(RankTypes.WHITE).essence(EssenceTypes.ASSIST)
            .entityType(PvZEntityRegister.LILY_PAD::get)
            .summonCard(PvZItemRegister.LILY_PAD_CARD)
            .enjoyCard(PvZItemRegister.LILY_PAD_ENJOY_CARD)
            .plantBlock(PvZBlockRegister.LILY_PAD::get)
            .skill(SkillTypes.FAST_CD)
            .isWaterPlant()
            .upgradeTo(PVZPlants.CAT_TAIL)
            );

    public static final DeferredHolder<IPlantType, IPlantType> SPIKE_WEED = plant("spike_weed", b -> b
            .sunCost(100).requiredLevel(17)
            .coolDown(CoolDowns.LITTLE_FAST).rank(RankTypes.WHITE).essence(EssenceTypes.SPEAR)
            .entityType(PvZEntityRegister.SPIKE_WEED::get)
            .summonCard(PvZItemRegister.SPIKE_WEED_CARD)
            .enjoyCard(PvZItemRegister.SPIKE_WEED_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.SPIKE_DAMAGE)
            .placement(Placements.STABLE)
            .upgradeTo(PVZPlants.SPIKE_ROCK)
            );

    /* ------------------------------------------------------------------ */
    /* pool night                                                          */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<IPlantType, IPlantType> PUMPKIN = plant("pumpkin", b -> b
            .sunCost(125).requiredLevel(30)
            .coolDown(CoolDowns.SLOW).rank(RankTypes.GOLD).essence(EssenceTypes.DEFENCE)
            .entityType(PvZEntityRegister.PUMPKIN::get)
            .summonCard(PvZItemRegister.PUMPKIN_CARD)
            .enjoyCard(PvZItemRegister.PUMPKIN_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD).upgradeTo(PVZPlants.STEEL_PUMPKIN)
            .outerPlant(PumpkinEntity.PumpkinInfo::new)
            );

    public static final DeferredHolder<IPlantType, IPlantType> MAGNET_SHROOM = plant("magnet_shroom", b -> b
            .sunCost(100).requiredLevel(41)
            .coolDown(CoolDowns.LITTLE_FAST).rank(RankTypes.GOLD).essence(EssenceTypes.ASSIST)
            .entityType(PvZEntityRegister.MAGNET_SHROOM::get)
            .summonCard(PvZItemRegister.MAGNET_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.MAGNET_SHROOM_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.LESS_WORK_CD)
            .isShroomPlant()
            .upgradeTo(PVZPlants.GOLD_MAGNET)
            );

    /* ------------------------------------------------------------------ */
    /* roof                                                                */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<IPlantType, IPlantType> CABBAGE_PULT = plant("cabbage_pult", b -> b
            .sunCost(100).requiredLevel(10)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.ARMA)
            .entityType(PvZEntityRegister.CABBAGE_PULT::get)
            .summonCard(PvZItemRegister.CABBAGE_PULT_CARD)
            .enjoyCard(PvZItemRegister.CABBAGE_PULT_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.MORE_CABBAGE_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> FLOWER_POT = plant("flower_pot", b -> b
            .sunCost(25).requiredLevel(3)
            .coolDown(CoolDowns.HUGE_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.ASSIST)
            .entityType(PvZEntityRegister.FLOWER_POT::get)
            .summonCard(PvZItemRegister.FLOWER_POT_CARD)
            .enjoyCard(PvZItemRegister.FLOWER_POT_ENJOY_CARD)
            .plantBlock(PvZBlockRegister.FLOWER_POT)
            .skill(SkillTypes.FAST_CD)
            .placement(Placements.ANY)
            );

    public static final DeferredHolder<IPlantType, IPlantType> KERNEL_PULT = plant("kernel_pult", b -> b
            .sunCost(125).requiredLevel(18)
            .coolDown(CoolDowns.FAST).rank(RankTypes.GOLD).essence(EssenceTypes.ARMA)
            .entityType(PvZEntityRegister.KERNEL_PULT::get)
            .summonCard(PvZItemRegister.KERNEL_PULT_CARD)
            .enjoyCard(PvZItemRegister.KERNEL_PULT_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.MORE_KERNEL_DAMAGE)
            .upgradeTo(PVZPlants.COB_CANNON)
            );

    public static final DeferredHolder<IPlantType, IPlantType> COFFEE_BEAN = plant("coffee_bean", b -> b
            .sunCost(75).requiredLevel(38)
            .coolDown(CoolDowns.FAST).rank(RankTypes.GREEN).essence(EssenceTypes.MAGIC)
            .entityType(PvZEntityRegister.COFFEE_BEAN::get)
            .summonCard(PvZItemRegister.COFFEE_BEAN_CARD)
            .enjoyCard(PvZItemRegister.COFFEE_BEAN_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD)
            );

    public static final DeferredHolder<IPlantType, IPlantType> GARLIC = plant("garlic", b -> b
            .sunCost(50).requiredLevel(16)
            .coolDown(CoolDowns.FAST).rank(RankTypes.GREEN).essence(EssenceTypes.DEFENCE)
            .entityType(PvZEntityRegister.GARLIC::get)
            .summonCard(PvZItemRegister.GARLIC_CARD)
            .enjoyCard(PvZItemRegister.GARLIC_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.MORE_GARLIC_LIFE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> UMBRELLA_LEAF = plant("umbrella_leaf", b -> b
            .sunCost(100).requiredLevel(23)
            .coolDown(CoolDowns.LITTLE_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.ENFORCE)
            .entityType(PvZEntityRegister.UMBRELLA_LEAF::get)
            .summonCard(PvZItemRegister.UMBRELLA_LEAF_CARD)
            .enjoyCard(PvZItemRegister.UMBRELLA_LEAF_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD)
            );

    public static final DeferredHolder<IPlantType, IPlantType> MARIGOLD = plant("marigold", b -> b
            .sunCost(50).requiredLevel(55)
            .coolDown(CoolDowns.NORMAL).rank(RankTypes.BLUE).essence(EssenceTypes.ASSIST)
            .entityType(PvZEntityRegister.MARIGOLD::get)
            .summonCard(PvZItemRegister.MARIGOLD_CARD)
            .enjoyCard(PvZItemRegister.MARIGOLD_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD)
            );

    public static final DeferredHolder<IPlantType, IPlantType> MELON_PULT = plant("melon_pult", b -> b
            .sunCost(400).requiredLevel(23)
            .coolDown(CoolDowns.SLOW).rank(RankTypes.PURPLE).essence(EssenceTypes.ARMA)
            .entityType(PvZEntityRegister.MELON_PULT::get)
            .summonCard(PvZItemRegister.MELON_PULT_CARD)
            .enjoyCard(PvZItemRegister.MELON_PULT_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.MORE_MELON_DAMAGE)
            .upgradeTo(PVZPlants.WINTER_MELON)
            .upgradeTo(PVZPlants.GOLDEN_MELON_PULT)
            );

    /* ------------------------------------------------------------------ */
    /* upgrade forms                                                       */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<IPlantType, IPlantType> GATLING_PEA = plant("gatling_pea", b -> b
            .sunCost(450).requiredLevel(28)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.PURPLE).essence(EssenceTypes.APPEASE)
            .entityType(PvZEntityRegister.GATLING_PEA::get)
            .summonCard(PvZItemRegister.GATLING_PEA_CARD)
            .enjoyCard(PvZItemRegister.GATLING_PEA_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.PEA_DAMAGE)
            .upgradeFrom(PVZPlants.REPEATER)
            );

    public static final DeferredHolder<IPlantType, IPlantType> TWIN_SUNFLOWER = plant("twin_sunflower", b -> b
            .sunCost(200).requiredLevel(25)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.BLUE).essence(EssenceTypes.LIGHT)
            .entityType(PvZEntityRegister.TWIN_SUNFLOWER::get)
            .summonCard(PvZItemRegister.TWIN_SUNFLOWER_CARD)
            .enjoyCard(PvZItemRegister.TWIN_SUNFLOWER_ENJOY_CARD)
            .skills(COMMON_SKILLS)
            .upgradeFrom(PVZPlants.SUN_FLOWER)
            );

    public static final DeferredHolder<IPlantType, IPlantType> GLOOM_SHROOM = plant("gloom_shroom", b -> b
            .sunCost(250).requiredLevel(50)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.PURPLE).essence(EssenceTypes.TOXIC)
            .entityType(PvZEntityRegister.GLOOM_SHROOM::get)
            .summonCard(PvZItemRegister.GLOOM_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.GLOOM_SHROOM_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.SPORE_DAMAGE)
            .isShroomPlant()
            .upgradeFrom(PVZPlants.FUME_SHROOM)
            );

    public static final DeferredHolder<IPlantType, IPlantType> CAT_TAIL = plant("cat_tail", b -> b
            .sunCost(350).requiredLevel(52)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.BLACK).essence(EssenceTypes.SPEAR)
            .entityType(PvZEntityRegister.CAT_TAIL::get)
            .summonCard(PvZItemRegister.CAT_TAIL_CARD)
            .enjoyCard(PvZItemRegister.CAT_TAIL_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.MORE_THORN_DAMAGE)
            .placement(Placements.LILY_PAD)
            .upgradeFrom(PVZPlants.LILY_PAD)
            .isWaterPlant()
            );

    public static final DeferredHolder<IPlantType, IPlantType> WINTER_MELON = plant("winter_melon", b -> b
            .sunCost(650).requiredLevel(50)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.BLACK).essence(EssenceTypes.ICE)
            .entityType(PvZEntityRegister.WINTER_MELON::get)
            .summonCard(PvZItemRegister.WINTER_MELON_CARD)
            .enjoyCard(PvZItemRegister.WINTER_MELON_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.MORE_MELON_DAMAGE)
            .upgradeFrom(PVZPlants.MELON_PULT)
            );

    public static final DeferredHolder<IPlantType, IPlantType> GOLD_MAGNET = plant("gold_magnet", b -> b
            .sunCost(200).requiredLevel(55)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.WHITE).essence(EssenceTypes.ASSIST)
            .entityType(PvZEntityRegister.GOLD_MAGNET::get)
            .summonCard(PvZItemRegister.GOLD_MAGNET_CARD)
            .enjoyCard(PvZItemRegister.GOLD_MAGNET_ENJOY_CARD)
            .skills(COMMON_SKILLS)
            .upgradeFrom(PVZPlants.MAGNET_SHROOM)
            );

    public static final DeferredHolder<IPlantType, IPlantType> SPIKE_ROCK = plant("spike_rock", b -> b
            .sunCost(275).requiredLevel(27)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.BLUE).essence(EssenceTypes.SPEAR)
            .entityType(PvZEntityRegister.SPIKE_ROCK::get)
            .summonCard(PvZItemRegister.SPIKE_ROCK_CARD)
            .enjoyCard(PvZItemRegister.SPIKE_ROCK_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.SPIKE_DAMAGE, SkillTypes.MORE_SPIKE)
            .placement(Placements.STABLE)
            .upgradeFrom(PVZPlants.SPIKE_WEED)
            );

    public static final DeferredHolder<IPlantType, IPlantType> COB_CANNON = plant("cob_cannon", b -> b
            .sunCost(700).requiredLevel(60)
            .coolDown(CoolDowns.HUGE_SLOW).rank(RankTypes.BLACK).essence(EssenceTypes.EXPLOSION)
            .entityType(PvZEntityRegister.COB_CANNON::get)
            .summonCard(PvZItemRegister.COB_CANNON_CARD)
            .enjoyCard(PvZItemRegister.COB_CANNON_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.NORMAL_BOMB_DAMAGE)
            .placement(Placements.ANY)
            .upgradeFrom(PVZPlants.KERNEL_PULT)
            );

    public static final DeferredHolder<IPlantType, IPlantType> BEE_SHOOTER = plant("bee_shooter", b -> b
            .sunCost(175).requiredLevel(21)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.APPEASE)
            .entityType(PvZEntityRegister.BEE_SHOOTER::get)
            .summonCard(PvZItemRegister.BEE_SHOOTER_CARD)
            .enjoyCard(PvZItemRegister.BEE_SHOOTER_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.PEA_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> PEA_POD = plant("peapod", b -> b
            .sunCost(125).requiredLevel(25)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.APPEASE)
            .entityType(PvZEntityRegister.PEA_POD::get)
            .summonCard(PvZItemRegister.PEAPOD_CARD)
            .enjoyCard(PvZItemRegister.PEAPOD_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.PEA_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> PRIMAL_PEA_SHOOTER = plant("primal_pea_shooter", b -> b
            .sunCost(175).requiredLevel(27)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.BLUE).essence(EssenceTypes.APPEASE)
            .entityType(PvZEntityRegister.PRIMAL_PEA_SHOOTER::get)
            .summonCard(PvZItemRegister.PRIMAL_PEA_SHOOTER_CARD)
            .enjoyCard(PvZItemRegister.PRIMAL_PEA_SHOOTER_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.PEA_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> BURST_KERNEL_PULT = plant("burst_kernel_pult", b -> b
            .sunCost(125).requiredLevel(15)
            .coolDown(CoolDowns.FAST).rank(RankTypes.GREEN).essence(EssenceTypes.ARMA)
            .entityType(PvZEntityRegister.BURST_KERNEL_PULT::get)
            .summonCard(PvZItemRegister.BURST_KERNEL_PULT_CARD)
            .enjoyCard(PvZItemRegister.BURST_KERNEL_PULT_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.MORE_KERNEL_DAMAGE)
            );


    public static final DeferredHolder<IPlantType, IPlantType> CHORUS_FRUIT_PULT = plant("chorus_fruit_pult", b -> b
            .sunCost(175).requiredLevel(21)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.PURPLE).essence(EssenceTypes.ARMA)
            .entityType(PvZEntityRegister.CHORUS_FRUIT_PULT::get)
            .summonCard(PvZItemRegister.CHORUS_FRUIT_PULT_CARD)
            .enjoyCard(PvZItemRegister.CHORUS_FRUIT_PULT_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD).placement(Placements.END_STONE)
            );


    public static final DeferredHolder<IPlantType, IPlantType> GOLDEN_MELON_PULT = plant("golden_melon_pult", b -> b
            .sunCost(625).requiredLevel(30)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.GOLD).essence(EssenceTypes.ARMA)
            .entityType(PvZEntityRegister.GOLDEN_MELON_PULT::get)
            .summonCard(PvZItemRegister.GOLDEN_MELON_PULT_CARD)
            .enjoyCard(PvZItemRegister.GOLDEN_MELON_PULT_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.MORE_MELON_DAMAGE)
            .upgradeFrom(PVZPlants.MELON_PULT)
            );

    public static final DeferredHolder<IPlantType, IPlantType> FODDER_BUSH = plant("fodder_bush", b -> b
            .sunCost(0).requiredLevel(8)
            .coolDown(CoolDowns.SUPER_FAST).rank(RankTypes.WHITE).essence(EssenceTypes.ASSIST)
            .entityType(PvZEntityRegister.FODDER_BUSH::get)
            .summonCard(PvZItemRegister.FODDER_BUSH_CARD)
            .enjoyCard(PvZItemRegister.FODDER_BUSH_ENJOY_CARD)
            .skills(COMMON_SKILLS)
            );

    public static final DeferredHolder<IPlantType, IPlantType> GRASS_CARP = plant("grass_carp", b -> b
            .sunCost(225).requiredLevel(23)
            .coolDown(CoolDowns.LITTLE_SLOW).rank(RankTypes.PURPLE).essence(EssenceTypes.ASSIST)
            .entityType(PvZEntityRegister.GRASS_CARP::get)
            .summonCard(PvZItemRegister.GRASS_CARP_CARD)
            .enjoyCard(PvZItemRegister.GRASS_CARP_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.PEA_DAMAGE)
            );


    public static final DeferredHolder<IPlantType, IPlantType> SELF_IMITATER = plant("self_imitater", b -> b
            .sunCost(175).requiredLevel(16)
            .coolDown(CoolDowns.SLOW).rank(RankTypes.PURPLE).essence(EssenceTypes.DEFENCE)
            .entityType(PvZEntityRegister.SELF_IMITATER::get)
            .summonCard(PvZItemRegister.SELF_IMITATER_CARD)
            .enjoyCard(PvZItemRegister.SELF_IMITATER_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.IMITATER_CHANCE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> STEEL_PUMPKIN = plant("steel_pumpkin", b -> b
            .sunCost(225).requiredLevel(45)
            .coolDown(CoolDowns.VERY_SLOW).rank(RankTypes.BLUE).essence(EssenceTypes.DEFENCE)
            .entityType(PvZEntityRegister.STEEL_PUMPKIN::get)
            .summonCard(PvZItemRegister.STEEL_PUMPKIN_CARD)
            .enjoyCard(PvZItemRegister.STEEL_PUMPKIN_ENJOY_CARD)
            .skills(COMMON_SKILLS)
            .upgradeFrom(PUMPKIN)
            .outerPlant(SteelPumpkinEntity.SteelPumpkinInfo::new)
            );

    public static final DeferredHolder<IPlantType, IPlantType> MAGNIFYING_GRASS = plant("magnifying_grass", b -> b
            .sunCost(50).requiredLevel(42)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.BLUE).essence(EssenceTypes.ELECTRIC)
            .entityType(PvZEntityRegister.MAGNIFYING_GRASS::get)
            .summonCard(PvZItemRegister.MAGNIFYING_GRASS_CARD)
            .enjoyCard(PvZItemRegister.MAGNIFYING_GRASS_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.LIGHT_BEAM_DAMAGE, SkillTypes.MAGNIFYING_GRASS_DAMAGE)
            );


    public static final DeferredHolder<IPlantType, IPlantType> ROTATE_RADISH = plant("rotate_radish", b -> b
            .sunCost(150).requiredLevel(20)
            .coolDown(CoolDowns.FAST).rank(RankTypes.BLUE).essence(EssenceTypes.ENFORCE)
            .entityType(PvZEntityRegister.ROTATE_RADISH::get)
            .summonCard(PvZItemRegister.ROTATE_RADISH_CARD)
            .enjoyCard(PvZItemRegister.ROTATE_RADISH_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.MORE_SWING_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> BLAZE_WART = plant("blaze_wart", b -> b
            .sunCost(175).requiredLevel(19)
            .coolDown(CoolDowns.FAST).rank(RankTypes.GREEN).essence(EssenceTypes.FLAME)
            .entityType(PvZEntityRegister.BLAZE_WART::get)
            .summonCard(PvZItemRegister.BLAZE_WART_CARD)
            .enjoyCard(PvZItemRegister.BLAZE_WART_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.WART_HEAL_CD)
            );

    public static final DeferredHolder<IPlantType, IPlantType> FIRE_PEASHOOTER = plant("fire_peashooter", b -> b
            .sunCost(225).requiredLevel(17)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.FLAME)
            .entityType(PvZEntityRegister.FIRE_PEASHOOTER::get)
            .summonCard(PvZItemRegister.FIRE_PEASHOOTER_CARD)
            .enjoyCard(PvZItemRegister.FIRE_PEASHOOTER_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.PEA_DAMAGE)
            );


    public static final DeferredHolder<IPlantType, IPlantType> ICE_CABBAGE_PULT= plant("ice_cabbage_pult", b -> b
            .sunCost(175).requiredLevel(17)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.BLUE).essence(EssenceTypes.ICE)
            .entityType(PvZEntityRegister.ICE_CABBAGE_PULT::get)
            .summonCard(PvZItemRegister.ICE_CABBAGE_PULT_CARD)
            .enjoyCard(PvZItemRegister.ICE_CABBAGE_PULT_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.MORE_CABBAGE_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> RE_ICEPEA = plant("re_icepea", b -> b
            .sunCost(275).requiredLevel(22)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.BLUE).essence(EssenceTypes.ICE)
            .entityType(PvZEntityRegister.RE_ICEPEA::get)
            .summonCard(PvZItemRegister.RE_ICEPEA_CARD)
            .enjoyCard(PvZItemRegister.RE_ICEPEA_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.PEA_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> ORIGIN_SHROOM= plant("origin_shroom", b -> b
            .sunCost(150).requiredLevel(18)
            .coolDown(CoolDowns.FAST).rank(RankTypes.GREEN).essence(EssenceTypes.TOXIC)
            .entityType(PvZEntityRegister.ORIGIN_SHROOM::get).isShroomPlant()
            .summonCard(PvZItemRegister.ORIGIN_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.ORIGIN_SHROOM_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.SPORE_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> GOO_PEA_SHOOTER = plant("goo_pea_shooter", b -> b
            .sunCost(175).requiredLevel(28)
            .coolDown(CoolDowns.VERY_FAST).rank(RankTypes.BLUE).essence(EssenceTypes.TOXIC)
            .entityType(PvZEntityRegister.GOO_PEA_SHOOTER::get)
            .summonCard(PvZItemRegister.GOO_PEA_SHOOTER_CARD)
            .enjoyCard(PvZItemRegister.GOO_PEA_SHOOTER_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.PEA_DAMAGE)
            );

    public static final DeferredHolder<IPlantType, IPlantType> SCULK_SHROOM = plant("sculk_shroom", b -> b
            .sunCost(175).requiredLevel(29)
            .coolDown(CoolDowns.FAST).rank(RankTypes.BLACK).essence(EssenceTypes.TOXIC)
            .entityType(PvZEntityRegister.SCULK_SHROOM::get).isShroomPlant()
            .summonCard(PvZItemRegister.SCULK_SHROOM_CARD)
            .enjoyCard(PvZItemRegister.SCULK_SHROOM_ENJOY_CARD)
            .skills(COMMON_SKILLS).skill(SkillTypes.SPORE_DAMAGE)
            );


    public static final DeferredHolder<IPlantType, IPlantType> WATER_POT = plant("water_pot", b -> b
            .sunCost(25).requiredLevel(9).placement(Placements.ANY)
            .coolDown(CoolDowns.HUGE_FAST).rank(RankTypes.GREEN).essence(EssenceTypes.ASSIST)
            .summonCard(PvZItemRegister.WATER_POT_CARD)
            .enjoyCard(PvZItemRegister.WATER_POT_ENJOY_CARD)
            .plantBlock(PvZBlockRegister.WATER_POT)
            );

    public static final DeferredHolder<IPlantType, IPlantType>  POT_GRASS = plant("pot_grass", b -> b
            .sunCost(100).requiredLevel(9)
            .coolDown(CoolDowns.SLOW).rank(RankTypes.GREEN).essence(EssenceTypes.ASSIST)
            .summonCard(PvZItemRegister.POT_GRASS_CARD)
            .enjoyCard(PvZItemRegister. POT_GRASS_ENJOY_CARD)
            .plantBlock(PvZBlockRegister.POT_GRASS)
            );


    /* ------------------------------------------------------------------ */
    /* special                                                             */
    /* ------------------------------------------------------------------ */

    /**
     * 模仿者。无召唤卡升级关系，{@code placement = NONE}——模仿者卡作为容器，
     * 实际放置由内部卡的类型决定。
     */
    public static final DeferredHolder<IPlantType, IPlantType> IMITATER = plant("imitater", b -> b
            .requiredLevel(43)
            .coolDown(CoolDowns.DEFAULT).rank(RankTypes.PURPLE).essence(EssenceTypes.MAGIC)
            .entityType(PvZEntityRegister.IMITATER::get)
            .summonCard(PvZItemRegister.IMITATER_CARD)
            .enjoyCard(PvZItemRegister.IMITATER_ENJOY_CARD)
            .placement(Placements.NONE)
            );
    private static DeferredHolder<IPlantType, IPlantType> plant(String name,
                                                                  Function<PlantTypeBuilder, PlantTypeBuilder> cfg) {
        return PVZRegistryBootstrap.PLANTS.register(name,
                () -> cfg.apply(PlantTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private PVZPlants() {
    }

    /**
     * 触发类加载。主模组在 {@code RegistryHandler.coreRegister()} 中调用。
     */
    public static void init() {
    }
}