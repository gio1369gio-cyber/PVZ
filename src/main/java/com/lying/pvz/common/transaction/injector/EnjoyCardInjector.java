package com.lying.pvz.common.transaction.injector;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.IRankType;
import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.transaction.GoodType;
import com.lying.pvz.api.transaction.TradeInjector;
import com.lying.pvz.common.transaction.goodkind.ItemGood;
import com.lying.pvz.utils.MathUtil;
import com.lying.pvz.utils.StringUtil;
import com.lying.pvz.utils.others.WeightList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class EnjoyCardInjector implements TradeInjector {

    public static final Identifier ID = StringUtil.id("enjoy_card");
    private static final int CHANCE = 7;

    @Override public Identifier id() { return ID; }

    @Override
    public void inject(Context ctx) {
        WeightList<IPlantType> list = new WeightList<>();
        PVZAPI.lookup().plants().forEach(plant -> {
            if (plant.getEnjoyCard().isPresent()) {
                list.addItem(plant, plant.getRank().getWeight());
            }
        });
        if (list.isEmpty()) {
            PvZMod.LOGGER.warn("No plant with enjoy card for injector, skipped");
            return;
        }
        for (int i = 0; i < CHANCE; ++i) {
            IPlantType plantType = list.getRandomItem(ctx.random()).orElse(null);
            if (plantType == null) break;
            IRankType rank = plantType.getRank();
            int cost = ctx.random().nextFloat() < 0.1f
                    ? MathUtil.getRandomInRange(ctx.random(), 1) + rank.getValue()
                    : rank.getValue();
            ItemStack stack = new ItemStack(plantType.getEnjoyCard().get());
            ctx.sink().accept(new GoodType(ItemGood.of(stack), cost, 100, 1, false));
        }
    }
}