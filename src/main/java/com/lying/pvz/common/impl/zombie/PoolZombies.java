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
 * 泳池僵尸（12 个）。
 *
 * <p>分类 {@code pool}，优先级 95。
 */
public final class PoolZombies {

    private static final String CATEGORY = "pool";
    private static final int PRIORITY = 95;

    /* ---- eatCommonSkill ---- */

    public static final DeferredHolder<IZombieType, IZombieType> SNORKEL_ZOMBIE = zombie("snorkel_zombie", b -> b
            .rank(RankTypes.WHITE).xp(5)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.SNORKEL_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> BOBSLE_ZOMBIE = zombie("bobsle_zombie", b -> b
            .rank(RankTypes.WHITE).xp(2)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.BOBSLE_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> DOLPHIN_RIDER_ZOMBIE = zombie("dolphin_rider_zombie", b -> b
            .rank(RankTypes.WHITE).xp(8)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.DOLPHIN_RIDER_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> ZOMBIE_DOLPHIN = zombie("zombie_dolphin", b -> b
            .rank(RankTypes.GRAY).xp(2)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.ZOMBIE_DOLPHIN::get)
            .loot(PvZLoot.ZOMBIE_DOLPHIN)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> JACK_IN_BOX_ZOMBIE = zombie("jack_in_box_zombie", b -> b
            .rank(RankTypes.GREEN).xp(16)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.JACK_IN_BOX_ZOMBIE::get)
            .loot(PvZLoot.JACK_IN_BOX_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> BALLOON_ZOMBIE = zombie("balloon_zombie", b -> b
            .rank(RankTypes.BLUE).xp(10)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.BALLOON_ZOMBIE::get)
            .loot(PvZLoot.BALLOON_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> DIGGER_ZOMBIE = zombie("digger_zombie", b -> b
            .rank(RankTypes.BLUE).xp(20)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.DIGGER_ZOMBIE::get)
            .loot(PvZLoot.DIGGER_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> POGO_ZOMBIE = zombie("pogo_zombie", b -> b
            .rank(RankTypes.GREEN).xp(18)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.POGO_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> YETI_ZOMBIE = zombie("yeti_zombie", b -> b
            .rank(RankTypes.PURPLE).xp(36)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.YETI_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    /* ---- commonSkill ---- */

    public static final DeferredHolder<IZombieType, IZombieType> ZOMBONI = zombie("zomboni", b -> b
            .rank(RankTypes.BLUE).xp(32)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.ZOMBONI::get)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> BOBSLE_TEAM = zombie("bobsle_team", b -> b
            .rank(RankTypes.GREEN).xp(10)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.BOBSLE_TEAM::get)
            .loot(PvZLoot.BOBSLE_TEAM)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> DOLPHIN_RIDER = zombie("dolphin_rider", b -> b
            .rank(RankTypes.GREEN).xp(24)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.DOLPHIN_RIDER::get)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    private static DeferredHolder<IZombieType, IZombieType> zombie(String name,
                                                                    Function<ZombieTypeBuilder, ZombieTypeBuilder> cfg) {
        return PVZRegistryBootstrap.ZOMBIES.register(name,
                () -> cfg.apply(ZombieTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private PoolZombies() {
    }

    public static void init() {
    }
}