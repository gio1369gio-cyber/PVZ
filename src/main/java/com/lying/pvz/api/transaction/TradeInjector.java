package com.lying.pvz.api.transaction;

import com.lying.pvz.common.level.entity.npc.AbstractDaveEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

import java.util.function.Consumer;

public interface TradeInjector {

    Identifier id();

    void inject(Context ctx);

    record Context(
            AbstractDaveEntity owner,
            RandomSource random,
            HolderLookup.Provider registries,
            Consumer<GoodType> sink
    ) {}
}