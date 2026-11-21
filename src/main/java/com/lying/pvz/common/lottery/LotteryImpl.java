package com.lying.pvz.common.lottery;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.lottery.Lottery;
import com.lying.pvz.api.lottery.LotteryKind;
import com.lying.pvz.api.lottery.SlotEntry;
import com.lying.pvz.api.lottery.SlotKind;
import com.lying.pvz.common.level.item.card.ImitaterCardItem;
import com.lying.pvz.common.level.item.card.PlantCardItem;
import com.lying.pvz.utils.others.WeightList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link LotteryJson} → {@link Lottery} 的运行时转换。
 *
 * <p>本阶段禁止构造 {@link net.minecraft.world.item.ItemStack}：
 * {@code apply()} 执行时 Holder 的组件表尚未绑定，
 * 因此只保存 {@link SlotEntry} 的构造参数，实际构造由 {@link SlotEntry#createStack()} 完成。
 */
public final class LotteryImpl {

    private LotteryImpl() {
    }

    @Nullable
    public static Lottery from(Identifier id, LotteryJson json) {
        final List<SlotEntry> entries = new ArrayList<>();

        if (json.core().kind() == LotteryKind.ALL_PLANT_CARDS) {
            for (Item item : BuiltInRegistries.ITEM) {
                if (item instanceof PlantCardItem plantCard && plantCard.isEnjoy()) {
                    // 不构造 ItemStack，只保存 Holder
                    entries.add(new SlotEntry(SlotKind.ITEM,BuiltInRegistries.ITEM.wrapAsHolder(item), 10));
                }
            }
        } else if (json.core().kind() == LotteryKind.ALL_SUMMON_CARDS) {
            for (Item item : BuiltInRegistries.ITEM) {
                if (item instanceof ImitaterCardItem summonCard && summonCard.isEnjoy()) {
                    entries.add(new SlotEntry(SlotKind.ITEM, BuiltInRegistries.ITEM.wrapAsHolder(item), 10));
                }
            }
        }

        for (SlotJson slot : json.items()) {
            final SlotEntry entry = toEntry(id, slot);
            if (entry != null) {
                entries.add(entry);
            }
        }

        if (entries.isEmpty()) {
            PvZMod.LOGGER.error("Skipping loading lottery {} because it has no slot types", id);
            return null;
        }

        final WeightList<SlotEntry> table = new WeightList<>();
        for (SlotEntry entry : entries) {
            table.addItem(entry, entry.weight());
        }

        final int slotCount = json.slotCount().orElse(entries.size());
        return new Lottery(id, json.core().kind(), json.core().sunCost(), json.core().tradePrice(),
                json.core().tradeWeight(), slotCount, List.copyOf(entries), table);
    }

    @Nullable
    private static SlotEntry toEntry(Identifier id, SlotJson slot) {
        if (slot.kind() != SlotKind.ITEM) {
            return new SlotEntry(slot.kind(), slot.weight());
        }
        if (slot.item().isEmpty()) {
            PvZMod.LOGGER.warn("Lottery {} has an ITEM slot without 'item' field, skipped", id);
            return null;
        }
        return new SlotEntry(slot.kind(), slot.item().get(), slot.nbt().orElse(null),
                slot.weight(), "");
    }
}