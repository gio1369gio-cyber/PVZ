package com.lying.pvz.common.transaction.goodkind;

import com.lying.pvz.api.transaction.GoodKind;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ItemGood(Holder<Item> item, DataComponentPatch components) implements GoodKind {

    public static final MapCodec<ItemGood> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(ItemGood::item),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY)
                    .forGetter(ItemGood::components)
    ).apply(i, ItemGood::new));

    /** 便捷构造：从一个已组装好的 ItemStack 反推（用于注入器输出）。 */
    public static ItemGood of(ItemStack stack) {
        return new ItemGood(stack.typeHolder(), stack.getComponentsPatch());
    }

    @Override public String type() { return "item"; }

    @Override public ItemStack createStack(HolderLookup.Provider registries) {
        ItemStack stack = new ItemStack(this.item);
        stack.applyComponents(this.components);
        return stack;
    }

    @Override public Component describe(ItemStack stack) { return stack.getHoverName(); }

    @Override public int priority() { return 10; }
}