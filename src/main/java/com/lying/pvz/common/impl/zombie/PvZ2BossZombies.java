package com.lying.pvz.common.impl.zombie;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.paz.IZombieType;
import com.lying.pvz.common.impl.CoolDowns;
import com.lying.pvz.common.impl.EssenceTypes;
import com.lying.pvz.common.impl.PVZRegistryBootstrap;
import com.lying.pvz.common.impl.RankTypes;
import com.lying.pvz.common.impl.SkillTypes;
import com.lying.pvz.common.impl.type.ZombieTypeBuilder;
import com.lying.pvz.common.init.PvZEntityRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

/**
 * 植物大战僵尸 2 时空 BOSS（4 个四足机甲）。
 *
 * <p>分类 {@code pvz2_boss}，优先级 30。
 *
 * <p>四个 BOSS 共用四足机甲模型，仅装饰部件与召唤的僵尸不同：
 * <ul>
 *   <li>{@code sphinx_inator}：狮身终结者（神秘埃及）；</li>
 *   <li>{@code plank_walker}：甲板漫步者（海盗港湾）；</li>
 *   <li>{@code war_wagon}：战争马车（狂野西部）；</li>
 *   <li>{@code tomorrow_tron}：明日破译者（未来世界）。</li>
 * </ul>
 */
public final class PvZ2BossZombies {

    private static final String CATEGORY = "pvz2_boss";
    private static final int PRIORITY = 30;

    public static final DeferredHolder<IZombieType, IZombieType> SPHINX_INATOR = zombie("sphinx_inator", b -> b
            .rank(RankTypes.MEGA).xp(1500)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.SPHINX_INATOR::get)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> PLANK_WALKER = zombie("plank_walker", b -> b
            .rank(RankTypes.MEGA).xp(1750)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.PLANK_WALKER::get)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> WAR_WAGON = zombie("war_wagon", b -> b
            .rank(RankTypes.MEGA).xp(2000)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.WAR_WAGON::get)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> TOMORROW_TRON = zombie("tomorrow_tron", b -> b
            .rank(RankTypes.MEGA).xp(2500)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.TOMORROW_TRON::get)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    private static DeferredHolder<IZombieType, IZombieType> zombie(String name,
                                                                    Function<ZombieTypeBuilder, ZombieTypeBuilder> cfg) {
        return PVZRegistryBootstrap.ZOMBIES.register(name,
                () -> cfg.apply(ZombieTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private PvZ2BossZombies() {
    }

    public static void init() {
    }
}
