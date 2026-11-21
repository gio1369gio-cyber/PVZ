package com.lying.pvz.api.lottery;

import com.lying.pvz.utils.others.WeightList;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * 已加载的奖池，不可变。
 *
 * <p>{@code entries} 是索引访问用的列表，{@code weightedTable} 是随机抽取用的权重表，
 * 两者构造后不再修改。
 */
public record Lottery(
        Identifier id,
        LotteryKind kind,
        int sunCost,
        int tradePrice,
        int tradeWeight,
        int slotCount,
        List<SlotEntry> entries,
        WeightList<SlotEntry> weightedTable
) {

    public Lottery {
        entries = List.copyOf(entries);
    }

    /**
     * 按索引取条目。
     *
     * <p>返回 {@code null} 表示索引越界。常见于：
     * <ul>
     *   <li>{@code /reload} 后 {@code entries.size()} 变化，而 BlockEntity 缓存的
     *       {@code slotIndices} 仍是旧值；</li>
     *   <li>旧存档里的 {@code slot_i_j} 来自更早版本、条目更少的奖池。</li>
     * </ul>
     * 调用方必须处理 {@code null}。
     */
    @Nullable
    public SlotEntry getSlotType(int index) {
        if (index < 0 || index >= this.entries.size()) return null;
        return this.entries.get(index);
    }

    public int getSize() {
        return this.entries.size();
    }

    /**
     * 按权重抽一个条目，返回其在 {@code entries} 中的索引。
     *
     * <p>权重表返回空，或返回的条目不在 {@code entries} 里时，返回 {@link Optional#empty()}。
     */
    public Optional<Integer> getRandomEntryIndex(RandomSource rand) {
        return this.weightedTable.getRandomItem(rand)
                .map(this.entries::indexOf)
                .filter(i -> i >= 0);
    }
}