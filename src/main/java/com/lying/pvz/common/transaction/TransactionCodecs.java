package com.lying.pvz.common.transaction;

import com.lying.pvz.api.transaction.GoodKind;
import com.lying.pvz.api.transaction.TradeInjector;
import com.lying.pvz.common.impl.challenge.RaidCodecs;
import com.lying.pvz.common.transaction.goodkind.*;
import com.lying.pvz.common.transaction.injector.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public final class TransactionCodecs {

    private static final Map<String, MapCodec<? extends GoodKind>> GOOD_KIND = new HashMap<>();
    private static final Map<String, TradeInjector> INJECTORS = new HashMap<>();

    private TransactionCodecs() {}

    /** 返回 dispatch 形态的 MapCodec：读 "type" 字段选子 codec。 */
    public static MapCodec<GoodKind> goodKindMapCodec() {
        return Codec.STRING.dispatchMap("type", GoodKind::type, name -> {
            MapCodec<? extends GoodKind> c = GOOD_KIND.get(name);
            return c != null ? c : RaidCodecs.failingMapCodec("type", name);
        });
    }

    public static Codec<GoodKind> goodKindCodec() {
        return goodKindMapCodec().codec();
    }

    public static TradeInjector injector(Identifier id) { return null; }

    public static TradeInjector getInjector(String id) {
        return INJECTORS.get(id);
    }

    public static void registerGoodKind(String name, MapCodec<? extends GoodKind> codec) {
        GOOD_KIND.put(name, codec);
    }

    public static void registerInjector(TradeInjector injector) {
        INJECTORS.put(injector.id().toString(), injector);
    }

    /** 由 mod 初始化阶段调用一次。 */
    public static void init() {
        registerGoodKind("item",   ItemGood.CODEC);
        registerGoodKind("energy", EnergyGood.CODEC);
        registerGoodKind("slot",   SlotGood.CODEC);
        registerGoodKind("money",  MoneyGood.CODEC);

        registerInjector(new EnvelopeInjector());
        registerInjector(new SlotMachineInjector());
        registerInjector(new EnjoyCardInjector());
    }
}