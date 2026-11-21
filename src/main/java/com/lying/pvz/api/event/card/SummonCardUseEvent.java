package com.lying.pvz.api.event.card;

import com.lying.pvz.api.card.CardContent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

/**
 * 召唤卡成功使用事件。
 *
 * <p>在卡片成功召唤（放置植物 / 生成实体 / 放置方块）后触发（服务端）。
 * 阳光已扣除、冷却已应用、实体已生成。
 *
 * <p>本事件不继承 {@code ICancellableEvent}——卡片已经生效，监听方只能观察。
 */
public class SummonCardUseEvent extends Event {

    private final Player player;
    private final ItemStack heldStack;
    private final CardContent content;

    /**
     * @param player    使用卡片的玩家
     * @param heldStack 玩家手持的卡片物品栈（模仿者卡时为模仿者卡本身）
     * @param content   解析后的卡片内容；{@link CardContent#effectiveStack()} 为内部卡
     */
    public SummonCardUseEvent(Player player, ItemStack heldStack, CardContent content) {
        this.player = player;
        this.heldStack = heldStack;
        this.content = content;
    }

    public Player getPlayer() {
        return this.player;
    }

    /**
     * 玩家手持的卡片物品栈。
     * <p>普通卡：{@code getItem()} 即卡片本身；模仿者卡：外部模仿者卡。
     */
    public ItemStack getHeldStack() {
        return this.heldStack;
    }

    /**
     * 解析后的卡片内容。{@link CardContent#effectiveStack()} 为实际生效的内部卡
     * （普通卡 = 自身，模仿者卡 = 内部卡）。
     */
    public CardContent getContent() {
        return this.content;
    }
}