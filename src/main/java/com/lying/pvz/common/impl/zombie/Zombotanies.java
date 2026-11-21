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

import java.util.Collections;

/**
 * 植物僵尸（10 个）。
 *
 * <p>分类 {@code zombotany}，优先级 50。
 *
 * <p>全部为 {@code eatCommonSkill}。
 */
public final class Zombotanies {

    private static final String CATEGORY = "zombotany";
    private static final int PRIORITY = 50;

    public static final DeferredHolder<IZombieType, IZombieType> PEASHOOTER_ZOMBIE = zombie("peashooter_zombie", b -> b
            .rank(RankTypes.GREEN).xp(8)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.PEASHOOTER_ZOMBIE::get)
            .loot(PvZLoot.PEASHOOTER_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> WALLNUT_ZOMBIE = zombie("wallnut_zombie", b -> b
            .rank(RankTypes.GREEN).xp(25)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.WALLNUT_ZOMBIE::get)
            .loot(PvZLoot.WALLNUT_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> GATLINGPEA_ZOMBIE = zombie("gatlingpea_zombie", b -> b
            .rank(RankTypes.PURPLE).xp(10)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.GATLINGPEA_ZOMBIE::get)
            .loot(PvZLoot.GATLINGPEA_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> SQUASH_ZOMBIE = zombie("squash_zombie", b -> b
            .rank(RankTypes.BLUE).xp(10)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.SQUASH_ZOMBIE::get)
            .loot(PvZLoot.SQUASH_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> JALAPENO_ZOMBIE = zombie("jalapeno_zombie", b -> b
            .rank(RankTypes.GOLD).xp(12)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.JALAPENO_ZOMBIE::get)
            .loot(PvZLoot.JALAPENO_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> TALLNUT_ZOMBIE = zombie("tallnut_zombie", b -> b
            .rank(RankTypes.PURPLE).xp(36)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.TALLNUT_ZOMBIE::get)
            .loot(PvZLoot.TALLNUT_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType, IZombieType> PUMPKIN_ZOMBIE = zombie("pumpkin_zombie", b -> b
            .rank(RankTypes.PURPLE).xp(33)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.PUMPKIN_ZOMBIE::get)
            .loot(PvZLoot.PUMPKIN_ZOMBIE)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY, PRIORITY)
            );

    public static final DeferredHolder<IZombieType,IZombieType> SUN_FLOWER_ZOMBIE = zombie("sun_flower_zombie", b -> b
            .rank(RankTypes.GREEN)
            .xp(22)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.SUN_FLOWER_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY,PRIORITY)
            );

    public static final DeferredHolder<IZombieType,IZombieType> CHOMPER_ZOMBIE = zombie("chomper_zombie", b -> b
            .rank(RankTypes.PURPLE)
            .xp(22)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.CHOMPER_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY,PRIORITY)
            );


    public static final DeferredHolder<IZombieType,IZombieType> STEEL_PUMPKIN_ZOMBIE = zombie("steel_pumpkin_zombie", b -> b
            .rank(RankTypes.PURPLE)
            .xp(25)
            .coolDown(CoolDowns.DEFAULT).essence(EssenceTypes.ORIGIN)
            .entityType(PvZEntityRegister.STEEL_PUMPKIN_ZOMBIE::get)
            .skills(SkillTypes.EAT_COMMON_SKILLS)
            .group(CATEGORY,PRIORITY)
            );



    private static DeferredHolder<IZombieType, IZombieType> zombie(String name,
                                                                    Function<ZombieTypeBuilder, ZombieTypeBuilder> cfg) {
        return PVZRegistryBootstrap.ZOMBIES.register(name,
                () -> cfg.apply(ZombieTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    private Zombotanies() {
    }

    public static void init() {
    }
}