package com.lying.pvz.common.transaction.injector;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.lottery.Lottery;
import com.lying.pvz.api.transaction.GoodType;
import com.lying.pvz.api.transaction.TradeInjector;
import com.lying.pvz.common.datapack.LotteryTypeLoader;
import com.lying.pvz.common.transaction.goodkind.ItemGood;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.init.PvZDataComponents;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.others.WeightList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class SlotMachineInjector implements TradeInjector {

    public static final Identifier ID =  StringUtil.id("slot_machine");
    private static final int CHANCE = 2;

    @Override public Identifier id() { return ID; }

    @Override
    public void inject(Context ctx) {
        WeightList<Pair<Identifier, Lottery>> list = new WeightList<>();
        LotteryTypeLoader.getLotteries().forEach((res, lottery) ->
                list.addItem(Pair.of(res, lottery), lottery.tradeWeight()));
        if (list.isEmpty()) {
            PvZMod.LOGGER.warn("No tradable lottery for slot machine injector, skipped");
            return;
        }
        for (int i = 0; i < CHANCE; ++i) {
            Pair<Identifier, Lottery> pair = list.getRandomItem(ctx.random()).orElse(null);
            if (pair == null) break;
            ItemStack stack = new ItemStack(PvZBlockRegister.SLOT_MACHINE.get());
            stack.set(PvZDataComponents.LOTTERY_TYPE.get(), pair.getFirst());
            ctx.sink().accept(new GoodType(
                    ItemGood.of(stack),
                    pair.getSecond().tradePrice(), 100, 2, false));
        }
    }
}