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
 * 草地僵尸（14 个）。
 *
 * <p>分类 {@code grass}，优先级 100。
 *
 * <p><b>技能展开</b>：
 * <ul>
 *   <li>{@code eatCommonSkill([])} → {@code [HIGH_EAT_DAMAGE, FAST_CD, ZOMBIE_FAST_MOVE, TOUGH_BODY]}</li>
 * </ul>
 *
 * <p><b>无技能</b>：{@code tomb_stone} 无技能（旧代码未调用任何 {@code skill()}）。
 */
public final class GrassZombies {

    private static final String CATEGORY = "grass";
    private static final int PRIORITY = 100;

    /* ---- 带 loot + eatCommonSkill ---- */

    public static final DeferredHolder<IZombieType, IZombieType> NORMAL_ZOMBIE = zombie("normal_zombie", b -> b
            .rank(RankTypes.WHITE).xp(5)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.NORMAL_ZOMBIE::get)
            .loot(PvZLoot.NORMAL_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> FLAG_ZOMBIE = zombie("flag_zombie", b -> b
            .rank(RankTypes.WHITE).xp(10)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.FLAG_ZOMBIE::get)
            .loot(PvZLoot.FLAG_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> CONEHEAD_ZOMBIE = zombie("conehead_zombie", b -> b
            .rank(RankTypes.GREEN).xp(20)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.CONEHEAD_ZOMBIE::get)
            .loot(PvZLoot.CONEHEAD_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> BUCKETHEAD_ZOMBIE = zombie("buckethead_zombie", b -> b
            .rank(RankTypes.BLUE).xp(30)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.BUCKETHEAD_ZOMBIE::get)
            .loot(PvZLoot.BUCKETHEAD_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> SCREENDOOR_ZOMBIE = zombie("screendoor_zombie", b -> b
            .rank(RankTypes.GREEN).xp(10)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.SCREENDOOR_ZOMBIE::get)
            .loot(PvZLoot.SCREENDOOR_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> FOOTBALL_ZOMBIE = zombie("football_zombie", b -> b
            .rank(RankTypes.BLUE).xp(32)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.FOOTBALL_ZOMBIE::get)
            .loot(PvZLoot.FOOTBALL_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> GIGA_FOOTBALL_ZOMBIE = zombie("giga_football_zombie", b -> b
            .rank(RankTypes.GOLD).xp(40)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.GIGA_FOOTBALL_ZOMBIE::get)
            .loot(PvZLoot.GIGA_FOOTBALL_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    /* ---- 无 loot + eatCommonSkill ---- */

    public static final DeferredHolder<IZombieType, IZombieType> POLE_ZOMBIE = zombie("pole_zombie", b -> b
            .rank(RankTypes.GREEN).xp(25)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.POLE_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> NEWSPAPER_ZOMBIE = zombie("newspaper_zombie", b -> b
            .rank(RankTypes.WHITE).xp(8)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.NEWSPAPER_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> DANCING_ZOMBIE = zombie("dancing_zombie", b -> b
            .rank(RankTypes.BLUE).xp(20)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.DANCING_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> BACKUP_DANCER = zombie("backup_dancer", b -> b
            .rank(RankTypes.WHITE).xp(2)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.BACKUP_DANCER::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> OLD_ZOMBIE = zombie("old_zombie", b -> b
            .rank(RankTypes.BLUE).xp(25)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.OLD_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> SUNDAY_EDITION_ZOMBIE = zombie("sunday_edition_zombie", b -> b
            .rank(RankTypes.GOLD).xp(50)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.SUNDAY_EDITION_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    /* ---- 无技能 ---- */

    /**
     * 墓碑。无任何技能——纯障碍物。
     */
    public static final DeferredHolder<IZombieType, IZombieType> TOMB_STONE = zombie("tomb_stone", b -> b
            .rank(RankTypes.GRAY).xp(1)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.TOMB_STONE::get)
            .group(CATEGORY, PRIORITY)
            );

    private static DeferredHolder<IZombieType, IZombieType> zombie(String name,
                                                                    Function<ZombieTypeBuilder, ZombieTypeBuilder> cfg) {
        return PVZRegistryBootstrap.ZOMBIES.register(name,
                () -> cfg.apply(ZombieTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private GrassZombies() {
    }

    public static void init() {
    }
}