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
 * 自定义僵尸（9个）。
 *
 * <p>分类 {@code custom}，优先级 60。

 */
public final class CustomZombies {

    private static final String CATEGORY = "custom";
    private static final int PRIORITY = 60;


    public static final DeferredHolder<IZombieType, IZombieType> RED_EDGE_ZOMBIE = zombie("red_edge_zombie", b -> b
            .rank(RankTypes.GREEN).xp(5)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.RED_EDGE_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );


    public static final DeferredHolder<IZombieType, IZombieType> MC_ZOMBIE = zombie("mc_zombie", b -> b
            .rank(RankTypes.GREEN).xp(5)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.MC_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );


    public static final DeferredHolder<IZombieType, IZombieType> TARGET_ARROW_ZOMBIE = zombie("target_arrow_zombie", b -> b
            .rank(RankTypes.GREEN).xp(8)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.TARGET_ARROW_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );


    public static final DeferredHolder<IZombieType, IZombieType> LAVA_ZOMBIE = zombie("lava_zombie", b -> b
            .rank(RankTypes.PURPLE).xp(36)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.LAVA_ZOMBIE::get)
            .loot(PvZLoot.LAVA_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    /**
     * 哀悼僵尸。
     * <p>注：旧代码此处 loot 为 {@code PvZLoot.LAVA_ZOMBIE}（疑似复制粘贴），保持一致。
     */
    public static final DeferredHolder<IZombieType, IZombieType> MOURNER_ZOMBIE = zombie("mourner_zombie", b -> b
            .rank(RankTypes.GREEN).xp(10)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.MOURNER_ZOMBIE::get)
            .loot(PvZLoot.LAVA_ZOMBIE)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> COFFIN = zombie("coffin", b -> b
            .rank(RankTypes.BLACK).xp(100)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.COFFIN::get)
            .loot(PvZLoot.COFFIN)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> NOBLE_ZOMBIE = zombie("noble_zombie", b -> b
            .rank(RankTypes.MEGA).xp(250)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.NOBLE_ZOMBIE::get)
            .loot(PvZLoot.NOBLE_ZOMBIE)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> TRICK_ZOMBIE = zombie("trick_zombie", b -> b
            .rank(RankTypes.GRAY).xp(3)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.TRICK_ZOMBIE::get)
            .loot(PvZLoot.TRICK_ZOMBIE)
            .skills(SkillTypes.MOVE_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    /**
     * 巨型墓碑。无技能、无 loot。
     * <p>注意：ID 为 {@code giga_tomb_stone}（带下划线），字段名为 {@code GIGA_TOMBSTONE}（无下划线），
     * 与旧代码保持一致。
     */
    public static final DeferredHolder<IZombieType, IZombieType> GIGA_TOMBSTONE = zombie("giga_tomb_stone", b -> b
            .rank(RankTypes.WHITE).xp(1)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.GIGA_TOMB_STONE::get)
            .group(CATEGORY, PRIORITY)
            );




    private static DeferredHolder<IZombieType, IZombieType> zombie(String name,
                                                                    Function<ZombieTypeBuilder, ZombieTypeBuilder> cfg) {
        return PVZRegistryBootstrap.ZOMBIES.register(name,
                () -> cfg.apply(ZombieTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private CustomZombies() {
    }

    public static void init() {
    }
}