package com.lying.pvz.api.transaction;

import com.lying.pvz.api.raid.IAmountDefinition;
import net.minecraft.resources.Identifier;

import java.util.List;

public record TransactionType(
        Identifier id,
        IAmountDefinition goodCount,
        List<GoodType> goods,
        List<Identifier> injectors
) {
    public TransactionType {
        goods = List.copyOf(goods);
        injectors = List.copyOf(injectors);
    }
}