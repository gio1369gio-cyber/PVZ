package com.lying.pvz.common.impl;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.IEssenceType;
import com.lying.pvz.common.impl.type.EssenceTypeBuilder;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.tag.PvZBlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

/**
 * 内置精华类型的 {@link DeferredHolder} 集合。
 *
 * <p>通过 {@link PVZRegistryBootstrap#ESSENCES} 注册到 {@code pvz:essence} registry。
 * 主模组需在 mod 构造期调用一次 {@link #init()} 触发类加载。
 *
 * <p><b>关于 ORIGIN</b>：ORIGIN 是"起源精华"，本身无辐射来源（{@code radiationBlock} 为 null），
 * 也不在 {@code TO_*_ORES} tag 中——旧代码用 {@code null blockSup} + {@code null tag} 表达。
 * 新设计通过"不调用 {@code radiationBlock(...)} / {@code radiationTag(...)}"实现，
 * 对应 getter 返回 {@code Optional.empty()}。
 *
 * <p><b>关于 ELECTRIC / SHADOW</b>：与 ORIGIN 类似，无辐射方块，但有 tag。
 * 只设置 {@code radiationTag(...)}，不设置 {@code radiationBlock(...)}。
 */
public final class EssenceTypes {

    /**
     * 起源精华。无辐射来源。
     */
    public static final DeferredHolder<IEssenceType, IEssenceType> ORIGIN = essence("origin", b -> b
            .essenceItem(PvZItemRegister.ORIGIN_ESSENCE)
            .essenceOre(PvZBlockRegister.ORIGIN_ORE::get));

    public static final DeferredHolder<IEssenceType, IEssenceType> APPEASE = essence("appease", b -> b
            .essenceItem(PvZItemRegister.APPEASE_ESSENCE)
            .essenceOre(PvZBlockRegister.APPEASE_ORE::get)
            .radiationBlock(() -> Blocks.GRASS_BLOCK)
            .radiationTag(() -> PvZBlockTags.TO_APPEASE_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> LIGHT = essence("light", b -> b
            .essenceItem(PvZItemRegister.LIGHT_ESSENCE)
            .essenceOre(PvZBlockRegister.LIGHT_ORE::get)
            .radiationBlock(PvZBlockRegister.LUNAR_STONE)
            .radiationTag(() -> PvZBlockTags.TO_LIGHT_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> EXPLOSION = essence("explosion", b -> b
            .essenceItem(PvZItemRegister.EXPLOSION_ESSENCE)
            .essenceOre(PvZBlockRegister.EXPLOSION_ORE::get)
            .radiationBlock(() -> Blocks.REDSTONE_BLOCK)
            .radiationTag(() -> PvZBlockTags.TO_EXPLOSION_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> DEFENCE = essence("defence", b -> b
            .essenceItem(PvZItemRegister.DEFENCE_ESSENCE)
            .essenceOre(PvZBlockRegister.DEFENCE_ORE::get)
            .radiationBlock(() -> Blocks.GRANITE)
            .radiationTag(() -> PvZBlockTags.TO_DEFENCE_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> ICE = essence("ice", b -> b
            .essenceItem(PvZItemRegister.ICE_ESSENCE)
            .essenceOre(PvZBlockRegister.ICE_ORE::get)
            .radiationBlock(() -> Blocks.SNOW_BLOCK)
            .radiationTag(() -> PvZBlockTags.TO_ICE_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> ENFORCE = essence("enforce", b -> b
            .essenceItem(PvZItemRegister.ENFORCE_ESSENCE)
            .essenceOre(PvZBlockRegister.ENFORCE_ORE::get)
            .radiationBlock(() -> Blocks.ANDESITE)
            .radiationTag(() -> PvZBlockTags.TO_ENFORCE_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> TOXIC = essence("toxic", b -> b
            .essenceItem(PvZItemRegister.TOXIC_ESSENCE)
            .essenceOre(PvZBlockRegister.TOXIC_ORE::get)
            .radiationBlock(() -> Blocks.MYCELIUM)
            .radiationTag(() -> PvZBlockTags.TO_TOXIC_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> ASSIST = essence("assist", b -> b
            .essenceItem(PvZItemRegister.ASSIST_ESSENCE)
            .essenceOre(PvZBlockRegister.ASSIST_ORE::get)
            .radiationBlock(() -> Blocks.DIORITE)
            .radiationTag(() -> PvZBlockTags.TO_ASSIST_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> MAGIC = essence("magic", b -> b
            .essenceItem(PvZItemRegister.MAGIC_ESSENCE)
            .essenceOre(PvZBlockRegister.MAGIC_ORE::get)
            .radiationBlock(() -> Blocks.SOUL_SAND)
            .radiationTag(() -> PvZBlockTags.TO_MAGIC_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> FLAME = essence("flame", b -> b
            .essenceItem(PvZItemRegister.FLAME_ESSENCE)
            .essenceOre(PvZBlockRegister.FLAME_ORE::get)
            .radiationBlock(() -> Blocks.MAGMA_BLOCK)
            .radiationTag(() -> PvZBlockTags.TO_FLAME_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> SPEAR = essence("spear", b -> b
            .essenceItem(PvZItemRegister.SPEAR_ESSENCE)
            .essenceOre(PvZBlockRegister.SPEAR_ORE::get)
            .radiationBlock(() -> Blocks.GRAVEL)
            .radiationTag(() -> PvZBlockTags.TO_SPEAR_ORES));

    public static final DeferredHolder<IEssenceType, IEssenceType> ARMA = essence("arma", b -> b
            .essenceItem(PvZItemRegister.ARMA_ESSENCE)
            .essenceOre(PvZBlockRegister.ARMA_ORE::get)
            .radiationBlock(() -> Blocks.SANDSTONE)
            .radiationTag(() -> PvZBlockTags.TO_ARMA_ORES));

    /**
     * 电精华。无辐射方块，但有 tag。
     */
    public static final DeferredHolder<IEssenceType, IEssenceType> ELECTRIC = essence("electric", b -> b
            .essenceItem(PvZItemRegister.ELECTRIC_ESSENCE)
            .essenceOre(PvZBlockRegister.ELECTRIC_ORE::get)
            // 不设置 radiationBlock —— getRadiationBlock() 返回 Optional.empty()
            .radiationTag(() -> PvZBlockTags.TO_ELECTRIC_ORES));

    /**
     * 暗影精华。无辐射方块，但有 tag。
     */
    public static final DeferredHolder<IEssenceType, IEssenceType> SHADOW = essence("shadow", b -> b
            .essenceItem(PvZItemRegister.SHADOW_ESSENCE)
            .essenceOre(PvZBlockRegister.SHADOW_ORE::get)
            // 不设置 radiationBlock —— getRadiationBlock() 返回 Optional.empty()
            .radiationTag(() -> PvZBlockTags.TO_SHADOW_ORES));

    private EssenceTypes() {
    }

    private static DeferredHolder<IEssenceType, IEssenceType> essence(String name,
                                                                       Function<EssenceTypeBuilder, EssenceTypeBuilder> cfg) {
        return PVZRegistryBootstrap.ESSENCES.register(name,
                () -> cfg.apply(EssenceTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    /**
     * 触发类加载。主模组在 {@code RegistryHandler.coreRegister()} 中调用。
     */
    public static void init() {
    }
}
