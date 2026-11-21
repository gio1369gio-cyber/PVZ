package com.lying.pvz.common.impl.plant;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.CoolDowns;
import com.lying.pvz.common.impl.EssenceTypes;
import com.lying.pvz.common.impl.PVZRegistryBootstrap;
import com.lying.pvz.common.impl.Placements;
import com.lying.pvz.common.impl.RankTypes;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.type.PlantTypeBuilder;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

/**
 * 其他植物（5 个）。
 *
 * <p>分类 {@code other}，优先级 50。
 */
public final class OtherPlants {

    private static final String CATEGORY = "other";
    private static final int PRIORITY = 50;

    /**
     * 寒冰莴苣。{@code commonSkill([])} = {@code [PLANT_MORE_LIFE, FAST_CD]}。
     */
    public static final DeferredHolder<IPlantType, IPlantType> ICEBERG_LETTUCE = plant("iceberg_lettuce", b -> b
            .sunCost(10).requiredLevel(3)
            .coolDown(CoolDowns.LITTLE_FAST).rank(RankTypes.WHITE).essence(EssenceTypes.ICE)
            .entityType(PvZEntityRegister.ICEBERG_LETTUCE::get)
            .summonCard(PvZItemRegister.ICEBERG_LETTUCE_CARD)
            .enjoyCard(PvZItemRegister.ICEBERG_LETTUCE_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD)
            .group(CATEGORY, PRIORITY)
            );

    /**
     *  Bonk Choy。{@code commonSkill([MORE_SWING_DAMAGE])}。
     */
    public static final DeferredHolder<IPlantType, IPlantType> BONK_CHOY = plant("bonk_choy", b -> b
            .sunCost(175).requiredLevel(6)
            .coolDown(CoolDowns.LITTLE_FAST).rank(RankTypes.BLUE).essence(EssenceTypes.ENFORCE)
            .entityType(PvZEntityRegister.BONK_CHOY::get)
            .summonCard(PvZItemRegister.BONK_CHOY_CARD)
            .enjoyCard(PvZItemRegister.BONK_CHOY_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.MORE_SWING_DAMAGE)
            .group(CATEGORY, PRIORITY)
            );

    /**
     * 黄金叶。仅可放置于金块或金地砖。
     */
    public static final DeferredHolder<IPlantType, IPlantType> GOLD_LEAF = plant("gold_leaf", b -> b
            .sunCost(75).requiredLevel(26)
            .coolDown(CoolDowns.NORMAL).rank(RankTypes.BLUE).essence(EssenceTypes.LIGHT)
            .entityType(PvZEntityRegister.GOLD_LEAF::get)
            .summonCard(PvZItemRegister.GOLD_LEAF_CARD)
            .enjoyCard(PvZItemRegister.GOLD_LEAF_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.ADVANCE_GOLD)
            .placement(Placements.GOLD)
            .group(CATEGORY, PRIORITY)
            );

    /**
     * 天使星果。{@code commonSunSkill([MORE_STAR_DAMAGE, TEN_STARS])}。
     */
    public static final DeferredHolder<IPlantType, IPlantType> ANGEL_STAR_FRUIT = plant("angel_star_fruit", b -> b
            .sunCost(225).requiredLevel(46)
            .coolDown(CoolDowns.LITTLE_FAST).rank(RankTypes.PURPLE).essence(EssenceTypes.APPEASE)
            .entityType(PvZEntityRegister.ANGEL_STAR_FRUIT::get)
            .summonCard(PvZItemRegister.ANGEL_STAR_FRUIT_CARD)
            .enjoyCard(PvZItemRegister.ANGEL_STAR_FRUIT_ENJOY_CARD)
            .skill(SkillTypes.LESS_SUN, SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD,
                            SkillTypes.MORE_STAR_DAMAGE, SkillTypes.TEN_STARS)
            .group(CATEGORY, PRIORITY)
            );

    /**
     * 竹笋。{@code commonSkill([SMALL_BOMB_DAMAGE])}。
     */
    public static final DeferredHolder<IPlantType, IPlantType> BAMBOO_LORD = plant("bamboo_lord", b -> b
            .sunCost(75).requiredLevel(6)
            .coolDown(CoolDowns.NORMAL).rank(RankTypes.BLUE).essence(EssenceTypes.EXPLOSION)
            .entityType(PvZEntityRegister.BAMBOO_LORD::get)
            .summonCard(PvZItemRegister.BAMBOO_LORD_CARD)
            .enjoyCard(PvZItemRegister.BAMBOO_LORD_ENJOY_CARD)
            .skill(SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD, SkillTypes.SMALL_BOMB_DAMAGE)
            .group(CATEGORY, PRIORITY)
            );

    private static DeferredHolder<IPlantType, IPlantType> plant(String name,
                                                                  Function<PlantTypeBuilder, PlantTypeBuilder> cfg) {
        return PVZRegistryBootstrap.PLANTS.register(name,
                () -> cfg.apply(PlantTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private OtherPlants() {
    }

    public static void init() {
    }
}