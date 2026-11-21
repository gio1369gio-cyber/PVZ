package com.lying.pvz.api.card;

import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.paz.IPlantType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * 卡片"内容"解析结果。
 *
 * <p>卡片的世界行为分两层：
 * <ul>
 *   <li>{@link #spawnType()}：卡片直接生成的目标类型（模仿者卡是"模仿者"本身）。</li>
 *   <li>{@link #effectiveType()}：数值 / 技能 / 放置规则所应用的类型（模仿者卡是内部卡的类型）。</li>
 * </ul>
 *
 * <p>普通植物卡 {@code spawnType == effectiveType}；模仿者卡两者不同。
 *
 * @param spawnType      卡片直接召唤的目标类型；{@link #isEmpty()} 为 {@code true} 时为 {@code null}
 * @param effectiveStack 携带"有效数值"的物品栈；普通卡是卡片自身，模仿者卡是内部卡
 * @param isEmpty        是否为"空内容"（卡未配置有效内容）
 */
public record CardContent(IPAZType spawnType, ItemStack effectiveStack, boolean isEmpty) {

    public static final CardContent EMPTY = new CardContent(null, ItemStack.EMPTY, true);

    public static CardContent of(IPAZType spawnType, ItemStack effectiveStack) {
        return new CardContent(spawnType, effectiveStack, false);
    }

    /**
     * 数值 / 技能 / 附魔 / 放置规则所对应的类型。
     *
     * <p>若 {@code effectiveStack} 自身是卡片，则返回该卡片的类型（模仿者卡读取内部卡的类型）；
     * 否则回退到 {@link #spawnType()}。
     *
     * @return 空内容时为 {@code null}；否则为有效类型
     */
    public IPAZType effectiveType() {
        if (this.isEmpty) {
            return null;
        }
        if (this.effectiveStack.getItem() instanceof ICardType card) {
            return card.getType();
        }
        return this.spawnType;
    }
    public @Nullable IPlantType asPlantType() {
        return this.effectiveType() instanceof IPlantType plant ? plant : null;
    }
    /**
     * 是否为模仿者卡（{@link #spawnType()} 与 {@link #effectiveType()} 不一致）。
     */
    public boolean isImitater() {
        return !this.isEmpty && this.spawnType != this.effectiveType();
    }
}