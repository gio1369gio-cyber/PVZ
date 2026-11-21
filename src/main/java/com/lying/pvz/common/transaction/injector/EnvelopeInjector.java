package com.lying.pvz.common.transaction.injector;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.raid.IChallengeDefinition;
import com.lying.pvz.api.transaction.GoodType;
import com.lying.pvz.api.transaction.TradeInjector;
import com.lying.pvz.common.transaction.goodkind.ItemGood;
import com.lying.pvz.common.level.item.display.ChallengeEnvelopeItem;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.others.WeightList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class EnvelopeInjector implements TradeInjector {

    public static final Identifier ID = StringUtil.id("envelope");
    private static final int CHANCE = 6;

    @Override public Identifier id() { return ID; }

    @Override
    public void inject(Context ctx) {
        WeightList<Pair<Identifier, IChallengeDefinition>> list = new WeightList<>();
        PVZAPI.raid().definitions().forEach((res, def) -> {
            if (def.canTrade()) list.addItem(Pair.of(res, def), def.getTradeWeight());
        });
        if (list.isEmpty()) {
            PvZMod.LOGGER.warn("No tradable challenge for envelope injector, skipped");
            return;
        }
        for (int i = 0; i < CHANCE; ++i) {
            Pair<Identifier, IChallengeDefinition> pair = list.getRandomItem(ctx.random()).orElse(null);
            if (pair == null) break;
            ItemStack stack = ChallengeEnvelopeItem.getChallengeEnvelope(pair.getFirst());
            ctx.sink().accept(new GoodType(
                    ItemGood.of(stack),
                    pair.getSecond().getTradePrice(), 100, 1, false));
        }
    }
}