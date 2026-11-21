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
 * 梗植物（1 个）。
 *
 * <p>分类 {@code meme}，优先级 80。
 */
public final class MemePlants {

    private static final String CATEGORY = "meme";
    private static final int PRIORITY = 80;

    /**
     * 怪猫。可放置于任意方块。
     *
     * <p>技能 {@code cdSkill([LESS_SUN])} 展开为 {@code [FAST_CD, LESS_SUN]}。
     * {@code LESS_SUN} 为 {@code CARD} 作用域，召唤时由卡片侧过滤，不会传到植物实体。
     */
    public static final DeferredHolder<IPlantType, IPlantType> STRANGE_CAT = plant("strange_cat", b -> b
            .sunCost(300).requiredLevel(58)
            .coolDown(CoolDowns.HUGE_SLOW).rank(RankTypes.PURPLE).essence(EssenceTypes.MAGIC)
            .entityType(PvZEntityRegister.STRANGE_CAT::get)
            .summonCard(PvZItemRegister.STRANGE_CAT_CARD)
            .enjoyCard(PvZItemRegister.STRANGE_CAT_ENJOY_CARD)
            .skill(SkillTypes.FAST_CD, SkillTypes.LESS_SUN)
            .placement(Placements.ANY)
            .group(CATEGORY, PRIORITY)
            );

    private static DeferredHolder<IPlantType, IPlantType> plant(String name,
                                                                  Function<PlantTypeBuilder, PlantTypeBuilder> cfg) {
        return PVZRegistryBootstrap.PLANTS.register(name,
                () -> cfg.apply(PlantTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private MemePlants() {
    }

    public static void init() {
    }
}