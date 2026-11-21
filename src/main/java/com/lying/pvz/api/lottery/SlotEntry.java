package com.lying.pvz.api.lottery;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jspecify.annotations.Nullable;

/**
 * 奖池中的一个条目，不可变。
 *
 * <p>为避免在数据包重载阶段（{@code SimplePreparableReloadListener.apply()}）
 * 直接构造 {@link ItemStack} —— 此时 {@code Holder<Item>} 的组件表尚未绑定，
 * {@code new ItemStack(holder)} 会抛 {@code "Components not bound yet"}。
 * 本类只保存构造所需参数，实际构造推迟到 {@link #createStack()}。
 *
 * <p>{@code itemHolder} 与 {@code nbt} 仅在 {@link SlotKind#ITEM} 时可能非空。
 *
 * <p>{@code identity} 用于区分同物品不同效果的场景（如"加速蘑菇卡"vs"普通蘑菇卡"），
 * 空字符串表示无特殊身份。
 */
public record SlotEntry(
        SlotKind kind,
        @Nullable Holder<Item> itemHolder,
        @Nullable CompoundTag nbt,
        int weight,
        String identity
) {

    /** 非 ITEM 条目（无物品、无 NBT、无 identity）。 */
    public SlotEntry(SlotKind kind, int weight) {
        this(kind, null, null, weight, "");
    }

    /** ITEM 条目，无 NBT、无 identity。 */
    public SlotEntry(SlotKind kind, Holder<Item> itemHolder, int weight) {
        this(kind, itemHolder, null, weight, "");
    }

    /**
     * 构造实际 {@link ItemStack}。
     *
     * <p>只能在组件已绑定的阶段（渲染 / 发放）调用；
     * 不要在 {@code apply()} 阶段调用。
     *
     * @return 非 ITEM 条目返回 {@code null}；ITEM 条目返回新构造的 {@link ItemStack}
     */
    @Nullable
    public ItemStack createStack() {
        if (kind != SlotKind.ITEM || itemHolder == null) {
            return null;
        }
        final ItemStack stack = new ItemStack(itemHolder);
        if (nbt != null && !nbt.isEmpty()) {
            // 复制一份 NBT，避免外部持有/修改 record 字段影响已发出的 stack
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt.copy()));
        }
        return stack;
    }
}