package com.lying.pvz.common.impl;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.card.PvZCardComponents;
import com.lying.pvz.api.content.skill.ISkillType;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.content.skill.SkillScope;
import com.lying.pvz.common.impl.type.SkillTypeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 内置技能类型的 {@link DeferredHolder} 集合。
 *
 * <p>通过 {@link PVZRegistryBootstrap#SKILLS} 注册到 {@code pvz:skill} registry。
 * 主模组需要在 mod 构造期调用一次 {@link #init()} 触发类加载，
 * 否则静态常量不会进入 {@code DeferredRegister} 的 entries 列表。
 */
public final class SkillTypes {

    /** {@link SkillLevels} 在 NBT 中的键名。 */
    public static final String SKILL_TAG = "paz_skill_tag";

    /** 冷却技能的最大等级；被 {@link CoolDowns} 用作公式参数。 */
    public static final int COOL_DOWN_LEVEL = 5;

    /* ------------------------------------------------------------------ */
    /* CARD 面技能                                                         */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<ISkillType, ISkillType> FAST_CD = skill("fast_cd", b -> b
            .scope(SkillScope.CARD)
            .values(0F, 1F, 2F, 3F, 4F, 5F)
            .costs(1, 2, 3, 3, 4));

    public static final DeferredHolder<ISkillType, ISkillType> LESS_SUN = skill("less_sun", b -> b
            .scope(SkillScope.CARD)
            .values(0F, 25F, 50F, 75F)
            .costs(3, 5, 7));

    /* ------------------------------------------------------------------ */
    /* PLANT 面技能                                                        */
    /* ------------------------------------------------------------------ */

    public static final DeferredHolder<ISkillType, ISkillType> PLANT_MORE_LIFE = skill("plant_more_life", b -> b
            .scope(SkillScope.PLANT)
            .values(20F, 30F, 40F, 50F)
            .costs(2, 3, 4));

    public static final DeferredHolder<ISkillType, ISkillType> PEA_DAMAGE = skill("pea_damage", b -> b
            .scope(SkillScope.PROJECTILE)
            .values(1.5F, 1.75F, 2F, 2.25F, 2.5F)
            .costs(2, 4, 5, 6));

    public static final DeferredHolder<ISkillType, ISkillType> NORMAL_BOMB_DAMAGE = skill("normal_bomb_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(150F, 180F, 210F, 240F, 270F, 300F)
            .costs(2, 3, 3, 4, 5));

    public static final DeferredHolder<ISkillType, ISkillType> NUT_MORE_LIFE = skill("nut_more_life", b -> b
            .scope(SkillScope.PLANT)
            .values(300F, 340F, 380F, 420F, 460F, 500F)
            .costs(3, 4, 5, 6, 8));

    public static final DeferredHolder<ISkillType, ISkillType> MINE_FAST_PREPARE = skill("mine_fast_prepare", b -> b
            .scope(SkillScope.PLANT)
            .values(240F, 190F, 140F, 90F, 40F)
            .costs(3, 3, 4, 6));

    public static final DeferredHolder<ISkillType, ISkillType> NORMAL_ENHANCE_STRENGTH = skill("normal_enhance_strength", b -> b
            .scope(SkillScope.PLANT)
            .values(140F, 180F, 220F, 260F, 300F)
            .costs(3, 4, 6, 7));

    public static final DeferredHolder<ISkillType, ISkillType> SPORE_DAMAGE = skill("spore_damage", b -> b
            .scope(SkillScope.PROJECTILE)
            .values(1.25F, 1.5F, 1.75F, 2F, 2.25F)
            .costs(2, 4, 5, 6));

    public static final DeferredHolder<ISkillType, ISkillType> HIGH_EXPLODE_DAMAGE = skill("high_explode_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(200F, 250F, 300F, 350F, 400F, 450F, 500F)
            .costs(3, 3, 4, 5, 6, 8));

    public static final DeferredHolder<ISkillType, ISkillType> SQUASH_AGAIN = skill("squash_again", b -> b
            .scope(SkillScope.PLANT)
            .values(0.1F, 0.2F, 0.3F, 0.4F)
            .costs(1, 2, 4));

    public static final DeferredHolder<ISkillType, ISkillType> SPIKE_DAMAGE = skill("spike_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(2F, 2.4F, 2.8F, 3.2F, 3.6F)
            .costs(2, 3, 5, 7));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_SPIKE = skill("more_spike", b -> b
            .scope(SkillScope.PLANT)
            .values(4F, 7F, 10F, 13F)
            .costs(1, 3, 5));

    public static final DeferredHolder<ISkillType, ISkillType> HEAT_PEA_RANGE = skill("heat_pea_range", b -> b
            .scope(SkillScope.PROJECTILE)
            .values(1.5F, 2F, 2.5F, 3F)
            .costs(2, 4, 6));

    public static final DeferredHolder<ISkillType, ISkillType> WOOD_MORE_LIFE = skill("wood_more_life", b -> b
            .scope(SkillScope.PLANT)
            .values(50F, 60F, 70F, 80F, 90F, 100F)
            .costs(1, 2, 3, 5, 7));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_LIGHT_RANGE = skill("more_light_range", b -> b
            .scope(SkillScope.PLANT)
            .values(20F, 30F, 40F)
            .costs(3, 4));

    public static final DeferredHolder<ISkillType, ISkillType> NIGHT_VISION = skill("night_vision", b -> b
            .scope(SkillScope.PLANT)
            .values(0F, 600F, 1200F)
            .costs(2, 4));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_THORN_DAMAGE = skill("more_thorn_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(2F, 2.3F, 2.6F, 2.9F, 3.2F, 3.5F)
            .costs(2, 3, 5, 6, 9));

    public static final DeferredHolder<ISkillType, ISkillType> BLOW_STRENGTH = skill("blow_strength", b -> b
            .scope(SkillScope.PLANT)
            .values(10F, 20F, 30F, 40F, 50F)
            .costs(2, 3, 5, 7));

    public static final DeferredHolder<ISkillType, ISkillType> SPLIT_DOUBLE_CHANCE = skill("split_double_chance", b -> b
            .scope(SkillScope.PROJECTILE)
            .values(0.2F, 0.35F, 0.5F, 0.65F, 0.8F)
            .costs(2, 3, 5, 7));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_STAR_DAMAGE = skill("more_star_damage", b -> b
            .scope(SkillScope.PROJECTILE)
            .values(1.75F, 2.15F, 2.55F, 2.95F, 3.35F, 3.75F)
            .costs(3, 5, 6, 8, 10));

    public static final DeferredHolder<ISkillType, ISkillType> LESS_WORK_CD = skill("less_work_cd", b -> b
            .scope(SkillScope.PLANT)
            .values(600F, 500F, 400F, 300F)
            .costs(2, 3, 6));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_CABBAGE_DAMAGE = skill("more_cabbage_damage", b -> b
            .scope(SkillScope.PROJECTILE)
            .values(3F, 3.5F, 4F, 4.5F, 5F)
            .costs(2, 3, 6, 7));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_KERNEL_DAMAGE = skill("more_kernel_damage", b -> b
            .scope(SkillScope.PROJECTILE)
            .values(1.5F, 2F, 2.5F, 3F)
            .costs(3, 5, 8));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_GARLIC_LIFE = skill("more_garlic_life", b -> b
            .scope(SkillScope.PLANT)
            .values(100F, 125F, 150F, 175F, 200F)
            .costs(2, 3, 5, 6));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_MELON_DAMAGE = skill("more_melon_damage", b -> b
            .scope(SkillScope.PROJECTILE)
            .values(6F, 7.2F, 8.4F, 9.6F, 10.8F, 12F)
            .costs(3, 5, 6, 7, 8));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_SWING_DAMAGE = skill("more_swing_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(3F, 3.6F, 4.2F, 4.8F, 5.4F, 6F)
            .costs(2, 3, 4, 6, 8));

    public static final DeferredHolder<ISkillType, ISkillType> ADVANCE_GOLD = skill("advance_gold", b -> b
            .scope(SkillScope.PLANT)
            .values(1F, 2F, 3F)
            .costs(6, 12));

    public static final DeferredHolder<ISkillType, ISkillType> TEN_STARS = skill("ten_stars", b -> b
            .scope(SkillScope.PROJECTILE)
            .values(0.2F, 0.3F, 0.4F, 0.5F, 0.6F)
            .costs(1, 3, 5, 7));

    public static final DeferredHolder<ISkillType, ISkillType> SMALL_BOMB_DAMAGE = skill("small_bomb_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(60F, 75F, 90F, 105F, 120F)
            .costs(2, 3, 4, 5));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_GUARD_LIFE = skill("more_guard_life", b -> b
            .scope(SkillScope.PLANT)
            .values(150F, 180F, 210F, 240F, 270F, 300F)
            .costs(2, 3, 4, 4, 5));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_BUTTER_DAMAGE = skill("more_butter_damage", b -> b
            .scope(SkillScope.PROJECTILE)
            .values(0.1F, 0.3F, 0.5F, 0.7F, 0.9F, 1.1F)
            .costs(1, 3, 4, 6, 10));

    public static final DeferredHolder<ISkillType, ISkillType> ZOMBIE_FAST_MOVE = skill("zombie_fast_move", b -> b
            .scope(SkillScope.PLANT)
            .values(0F, 0.1F, 0.2F, 0.3F)
            .costs(2, 5, 8));

    public static final DeferredHolder<ISkillType, ISkillType> HIGH_EAT_DAMAGE = skill("high_eat_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(5F, 7.5F, 10F, 12.5F, 15F)
            .costs(2, 3, 5, 6));

    public static final DeferredHolder<ISkillType, ISkillType> TOUGH_BODY = skill("tough_body", b -> b
            .scope(SkillScope.PLANT)
            .values(0F, 3F, 6F, 9F, 12F, 15F)
            .costs(2, 4, 5, 7, 8));

    public static final DeferredHolder<ISkillType, ISkillType> IMITATER_CHANCE = skill("imitater_chance", b -> b
            .scope(SkillScope.PLANT)
            .values(3F, 4F, 5F, 6F)
            .costs(2, 3, 5));

    public static final DeferredHolder<ISkillType, ISkillType> LIGHT_BEAM_DAMAGE = skill("light_beam_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(35F, 40F, 45F, 55F, 65F, 75F)
            .costs(2, 3, 4, 5, 6));

    public static final DeferredHolder<ISkillType, ISkillType> MAGNIFYING_GRASS_DAMAGE = skill("magnifying_grass_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(0.5F, 0.8F, 1.25F)
            .costs(2, 4));

    public static final DeferredHolder<ISkillType, ISkillType> WART_HEAL_CD = skill("wart_heal_cd", b -> b
            .scope(SkillScope.PLANT)
            .values(15F, 14F, 13F, 11F, 9F)
            .costs(2, 3, 4, 5));

    public static final DeferredHolder<ISkillType, ISkillType> GRASS_CARP_DAMAGE = skill("grass_carp_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(1F, 1.3F, 1.7F, 2.0F, 2.3F, 2.6F)
            .costs(2, 3, 4, 5, 6));

    public static final DeferredHolder<ISkillType, ISkillType> GRASS_CARP_HEAL_RANGE = skill("grass_carp_heal_rage", b -> b
            .scope(SkillScope.PLANT)
            .values(9F, 11F, 13F, 14F, 15F)
            .costs(2, 3, 5, 6));

    public static final DeferredHolder<ISkillType, ISkillType> MORE_CHORUS_FRUIT_DAMAGE = skill("more_chorus_fruit_damage", b -> b
            .scope(SkillScope.PLANT)
            .values(1.5F, 2.0F, 3.0F, 3.5F)
            .costs(3, 4, 7));

    /* ------------------------------------------------------------------ */
    /* 僵尸通用技能组                                                      */
    /* ------------------------------------------------------------------ */

    /** 僵尸通用啃食技能组：高啃食伤害、加速冷却、移速、坚韧。 */
    public static final List<Supplier<ISkillType>> EAT_COMMON_SKILLS = List.of(
            HIGH_EAT_DAMAGE, FAST_CD, ZOMBIE_FAST_MOVE, TOUGH_BODY);

    /** 僵尸通用（非啃食）技能组：加速冷却、移速、坚韧。 */
    public static final List<Supplier<ISkillType>> MOVE_COMMON_SKILLS = List.of(
            FAST_CD, ZOMBIE_FAST_MOVE, TOUGH_BODY);

    private SkillTypes() {
    }

    private static DeferredHolder<ISkillType, ISkillType> skill(String name,
                                                                 Function<SkillTypeBuilder, SkillTypeBuilder> cfg) {
        return PVZRegistryBootstrap.SKILLS.register(name,
                () -> cfg.apply(SkillTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    /**
     * 触发类加载。主模组在 {@code RegistryHandler.coreRegister()} 中调用。
     *
     * <p>方法体为空——调用的目的是触发 {@code <clinit>}，把静态 {@link DeferredHolder}
     * 注册到 {@link PVZRegistryBootstrap#SKILLS}。
     */
    public static void init() {
    }

    /* ------------------------------------------------------------------ */
    /* 工具方法                                                            */
    /* ------------------------------------------------------------------ */

    /**
     * 从 NBT 读取指定技能的等级。
     *
     * <p>NBT 中的键为 {@link #SKILL_TAG}，值为 {@link SkillLevels}。
     */
    public static int getSkillLevel(CompoundTag nbt, ISkillType type) {
        return nbt.read(SKILL_TAG, SkillLevels.CODEC)
                .map(levels -> levels.getLevel(type))
                .orElse(0);
    }

    /**
     * 从物品栈读取指定技能的等级。
     */
    public static int getSkillLevel(ItemStack stack, ISkillType type) {
        return stack.getOrDefault(PvZCardComponents.CARD_SKILLS.get(), SkillLevels.EMPTY)
                .getLevel(type);
    }

    /**
     * 向 NBT 写入指定技能的等级。
     */
    public static void addSkillLevel(CompoundTag nbt, ISkillType type, int level) {
        final SkillLevels current = nbt.read(SKILL_TAG, SkillLevels.CODEC).orElse(SkillLevels.EMPTY);
        nbt.store(SKILL_TAG, SkillLevels.CODEC, current.with(type.getID(), level));
    }

    /**
     * 向物品栈写入指定技能的等级。
     */
    public static void addSkillLevel(ItemStack stack, ISkillType type, int level) {
        stack.update(PvZCardComponents.CARD_SKILLS.get(), SkillLevels.EMPTY,
                levels -> levels.with(type.getID(), level));
    }

    /**
     * 按 {@link Identifier} 查询技能类型。实现走 {@link PVZAPI#lookup()}，不维护本地 Map。
     */
    public static ISkillType getSkillType(Identifier id) {
        return PVZAPI.lookup().skill(id).orElse(null);
    }
}
