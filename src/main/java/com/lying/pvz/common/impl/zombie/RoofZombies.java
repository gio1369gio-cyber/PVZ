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
import com.lying.pvz.common.misc.PvZLoot;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

/**
 * 屋顶僵尸（8 个）。
 *
 * <p>分类 {@code roof}，优先级 90。
 *
 * <p><b>EDGAR 系列</b>：{@code edgar_090505} / {@code edgar_090517} 是 BOSS，
 * rank {@code MEGA}，xp 极高，无 eatCommon（不主动吃植物）。
 */
public final class RoofZombies {

    private static final String CATEGORY = "roof";
    private static final int PRIORITY = 90;

    /* ---- eatCommonSkill ---- */

    public static final DeferredHolder<IZombieType, IZombieType> LADDER_ZOMBIE = zombie("ladder_zombie", b -> b
            .rank(RankTypes.GREEN).xp(15)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.LADDER_ZOMBIE::get)
            .loot(PvZLoot.LADDER_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> IMP = zombie("imp", b -> b
            .rank(RankTypes.GRAY).xp(2)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.IMP::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    /* ---- commonSkill ---- */

    public static final DeferredHolder<IZombieType, IZombieType> BUNGEE_ZOMBIE = zombie("bungee_zombie", b -> b
            .rank(RankTypes.GREEN).xp(20)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.BUNGEE_ZOMBIE::get)
            .loot(PvZLoot.BUNGEE_ZOMBIE)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );


    public static final DeferredHolder<IZombieType, IZombieType> AIRBORNE_ZOMBIE = zombie("airborne_zombie", b -> b
            .rank(RankTypes.GREEN).xp(10)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.AIRBORNE_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );


    public static final DeferredHolder<IZombieType, IZombieType> CATAPULT_ZOMBIE = zombie("catapult_zombie", b -> b
            .rank(RankTypes.BLUE).xp(32)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.CATAPULT_ZOMBIE::get)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> GARGANTUAR = zombie("gargantuar", b -> b
            .rank(RankTypes.PURPLE).xp(40)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.GARGANTUAR::get)
            .loot(PvZLoot.GARGANTUAR)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> GIGA_GARGANTUAR = zombie("giga_gargantuar", b -> b
            .rank(RankTypes.RED).xp(55)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.GIGA_GARGANTUAR::get)
            .loot(PvZLoot.GIGA_GARGANTUAR)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    /* ---- EDGAR 系列 ---- */

    public static final DeferredHolder<IZombieType, IZombieType> SPHINX_TERMINATOR = zombie("sphinx_terminator", b -> b
            .rank(RankTypes.MEGA).xp(1500)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.SPHINX_TERMINATOR::get)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> EDGAR_090505 = zombie("edgar_090505", b -> b
            .rank(RankTypes.MEGA).xp(1000)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.EDGAR_090505::get)
            .loot(PvZLoot.EDGAR_090505)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> EDGAR_090517 = zombie("edgar_090517", b -> b
            .rank(RankTypes.MEGA).xp(2000)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.EDGAR_090517::get)
            .loot(PvZLoot.EDGAR_090517)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );


    public static final DeferredHolder<IZombieType, IZombieType> EDGAR_090547 = zombie("edgar_090547", b -> b
            .rank(RankTypes.PURPLE).xp(50)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.EDGAR_090547::get)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );



    private static DeferredHolder<IZombieType, IZombieType> zombie(String name,
                                                                    Function<ZombieTypeBuilder, ZombieTypeBuilder> cfg) {
        return PVZRegistryBootstrap.ZOMBIES.register(name,
                () -> cfg.apply(ZombieTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private RoofZombies() {
    }

    public static void init() {
    }
}