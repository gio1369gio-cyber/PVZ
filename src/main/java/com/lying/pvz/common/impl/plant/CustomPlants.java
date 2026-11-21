package com.lying.pvz.common.impl.plant;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.common.impl.CoolDowns;
import com.lying.pvz.common.impl.EssenceTypes;
import com.lying.pvz.common.impl.PVZRegistryBootstrap;
import com.lying.pvz.common.impl.RankTypes;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.type.PlantTypeBuilder;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

/**
 * 自定义扩展植物（2 个）。
 *
 * <p>分类 {@code custom}，优先级 60。
 */
public final class CustomPlants {

    private static final String CATEGORY = "custom";
    private static final int PRIORITY = 60;

    /**
     * 水兵。水生植物，放置候选为 {@code NONE}（由 {@code isWaterPlant()} 设置）。
     */
    public static final DeferredHolder<IPlantType, IPlantType> WATER_GUARD = plant("water_guard", b -> b
            .sunCost(50).requiredLevel(4)
            .coolDown(CoolDowns.LITTLE_SLOW).rank(RankTypes.WHITE).essence(EssenceTypes.DEFENCE)
            .entityType(PvZEntityRegister.WATER_GUARD::get)
            .summonCard(PvZItemRegister.WATER_GUARD_CARD)
            .enjoyCard(PvZItemRegister.WATER_GUARD_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.MORE_GUARD_LIFE)
            .isWaterPlant()
            .group(CATEGORY, PRIORITY)
            );

    /**
     * 黄油投手。{@code commonSunSkill} 展开为 {@code [LESS_SUN, PLANT_MORE_LIFE, FAST_CD, MORE_BUTTER_DAMAGE]}。
     */
    public static final DeferredHolder<IPlantType, IPlantType> BUTTER_PULT = plant("butter_pult", b -> b
            .sunCost(275).requiredLevel(56)
            .coolDown(CoolDowns.NORMAL).rank(RankTypes.BLACK).essence(EssenceTypes.ARMA)
            .entityType(PvZEntityRegister.BUTTER_PULT::get)
            .summonCard(PvZItemRegister.BUTTER_PULT_CARD)
            .enjoyCard(PvZItemRegister.BUTTER_PULT_ENJOY_CARD)
            .skill(SkillTypes.LESS_SUN, SkillTypes.PLANT_MORE_LIFE, SkillTypes.FAST_CD,
                            SkillTypes.MORE_BUTTER_DAMAGE)
            .group(CATEGORY, PRIORITY)
            );

    private static DeferredHolder<IPlantType, IPlantType> plant(String name,
                                                                  Function<PlantTypeBuilder, PlantTypeBuilder> cfg) {
        return PVZRegistryBootstrap.PLANTS.register(name,
                () -> cfg.apply(PlantTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private CustomPlants() {
    }

    public static void init() {
    }
}