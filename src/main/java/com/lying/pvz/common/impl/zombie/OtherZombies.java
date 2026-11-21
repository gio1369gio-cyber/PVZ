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
 * 其他僵尸（1 个）。
 *
 * <p>分类 {@code other}，优先级 70。
 */
public final class OtherZombies {

    private static final String CATEGORY = "other";
    private static final int PRIORITY = 70;

    public static final DeferredHolder<IZombieType, IZombieType> RA_ZOMBIE = zombie("ra_zombie", b -> b
            .rank(RankTypes.GREEN).xp(10)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.RA_ZOMBIE::get)
            .loot(PvZLoot.RA_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> FLOWER_POT_ZOMBIE = zombie("flower_pot_zombie", b -> b
            .rank(RankTypes.GREEN).xp(10)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.FLOWER_POT_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );


    public static final DeferredHolder<IZombieType, IZombieType> MJ_ZOMBIE = zombie("mj_zombie", b -> b
            .rank(RankTypes.GREEN).xp(80)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.MJ_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );



    public static final DeferredHolder<IZombieType, IZombieType> DANCER_BACKUP_ZOMBIE = zombie("dancer_backup_zombie", b -> b
            .rank(RankTypes.GREEN).xp(8)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.DANCER_BACKUP_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );


    private static DeferredHolder<IZombieType, IZombieType> zombie(String name,
                                                                    Function<ZombieTypeBuilder, ZombieTypeBuilder> cfg) {
        return PVZRegistryBootstrap.ZOMBIES.register(name,
                () -> cfg.apply(ZombieTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private OtherZombies() {
    }

    public static void init() {
    }
}