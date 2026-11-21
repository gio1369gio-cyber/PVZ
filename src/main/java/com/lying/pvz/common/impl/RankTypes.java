package com.lying.pvz.common.impl;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.common.impl.type.RankTypeBuilder;
import com.lying.pvz.common.init.PvZItemRegister;
import com.lying.pvz.common.tag.PvZItemTags;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

/**
 * 内置卡片等级（稀有度）的 {@link DeferredHolder} 集合。
 *
 * <p>通过 {@link PVZRegistryBootstrap#RANKS} 注册到 {@code pvz:rank} registry。
 * 主模组需在 mod 构造期调用一次 {@link #init()} 触发类加载。
 *
 * <p><b>等级顺序</b>（从常见到稀有）：
 * {@code gray < white < green < blue < purple < gold < red < black < mega}。
 *
 * <p><b>关于 MEGA 等级</b>：{@code cardTag} / {@code material} 为 {@code Optional.empty()}，
 * 因此 {@link IRankType#getCardTag()} / {@link IRankType#getMaterial()} 返回空。
 * 旧代码用 {@code null supplier} 表达同一语义，新设计改用 {@link RankTypeBuilder#build()}
 * 的"未设置"分支处理。
 */
public final class RankTypes {

    public static final DeferredHolder<IRankType, IRankType> GRAY = rank("gray", b -> b
            .enchantPoint(15)
            .weight(10)
            .value(1)
            .templateCard(PvZItemRegister.GRAY_CARD)
            .cardTag(() -> PvZItemTags.GRAY_CARDS)
            .material(() -> PvZItemTags.GRAY_MATERIALS));

    public static final DeferredHolder<IRankType, IRankType> WHITE = rank("white", b -> b
            .enchantPoint(14)
            .weight(10)
            .value(2)
            .templateCard(PvZItemRegister.WHITE_CARD)
            .cardTag(() -> PvZItemTags.WHITE_CARDS)
            .material(() -> PvZItemTags.WHITE_MATERIALS));

    public static final DeferredHolder<IRankType, IRankType> GREEN = rank("green", b -> b
            .enchantPoint(12)
            .weight(9)
            .value(2)
            .templateCard(PvZItemRegister.GREEN_CARD)
            .cardTag(() -> PvZItemTags.GREEN_CARDS)
            .material(() -> PvZItemTags.GREEN_MATERIALS));

    public static final DeferredHolder<IRankType, IRankType> BLUE = rank("blue", b -> b
            .enchantPoint(12)
            .weight(9)
            .value(3)
            .templateCard(PvZItemRegister.BLUE_CARD)
            .cardTag(() -> PvZItemTags.BLUE_CARDS)
            .material(() -> PvZItemTags.BLUE_MATERIALS));

    public static final DeferredHolder<IRankType, IRankType> PURPLE = rank("purple", b -> b
            .enchantPoint(10)
            .weight(8)
            .value(3)
            .templateCard(PvZItemRegister.PURPLE_CARD)
            .cardTag(() -> PvZItemTags.PURPLE_CARDS)
            .material(() -> PvZItemTags.PURPLE_MATERIALS));

    public static final DeferredHolder<IRankType, IRankType> GOLD = rank("gold", b -> b
            .enchantPoint(10)
            .weight(7)
            .value(3)
            .templateCard(PvZItemRegister.GOLD_CARD)
            .cardTag(() -> PvZItemTags.GOLD_CARDS)
            .material(() -> PvZItemTags.GOLD_MATERIALS));

    public static final DeferredHolder<IRankType, IRankType> RED = rank("red", b -> b
            .enchantPoint(8)
            .weight(6)
            .value(4)
            .templateCard(PvZItemRegister.RED_CARD)
            .cardTag(() -> PvZItemTags.RED_CARDS)
            .material(() -> PvZItemTags.RED_MATERIALS));

    public static final DeferredHolder<IRankType, IRankType> BLACK = rank("black", b -> b
            .enchantPoint(6)
            .weight(5)
            .value(5)
            .templateCard(PvZItemRegister.BLACK_CARD)
            .cardTag(() -> PvZItemTags.BLACK_CARDS)
            .material(() -> PvZItemTags.BLACK_MATERIALS));

    /**
     * 最高等级。不可附魔、无卡片 tag、无拆解材料。
     */
    public static final DeferredHolder<IRankType, IRankType> MEGA = rank("mega", b -> b
            .enchantPoint(0)
            .weight(1)
            .value(10)
            .templateCard(PvZItemRegister.MEGA_CARD)
            // 不设置 cardTag / material —— getCardTag() / getMaterial() 返回 Optional.empty()
            );

    private RankTypes() {
    }

    private static DeferredHolder<IRankType, IRankType> rank(String name,
                                                              Function<RankTypeBuilder, RankTypeBuilder> cfg) {
        return PVZRegistryBootstrap.RANKS.register(name,
                () -> cfg.apply(RankTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    /**
     * 触发类加载。主模组在 {@code RegistryHandler.coreRegister()} 中调用。
     */
    public static void init() {
    }
}
