package com.lying.pvz.common.level.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

/**
 * 储光树苗存储组件：当前存量 / 上限 / 是否一次性。
 */
public record SunStorage(long amount, long max, boolean once) implements TooltipProvider {

    public static final Codec<SunStorage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("amount").forGetter(SunStorage::amount),
            Codec.LONG.fieldOf("max").forGetter(SunStorage::max),
            Codec.BOOL.fieldOf("once").forGetter(SunStorage::once)
    ).apply(instance, SunStorage::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SunStorage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, SunStorage::amount,
            ByteBufCodecs.VAR_LONG, SunStorage::max,
            ByteBufCodecs.BOOL, SunStorage::once,
            SunStorage::new
    );

    public boolean isFull() {
        return this.amount >= this.max;
    }

    public SunStorage withAmount(long newAmount) {
        return new SunStorage(newAmount, this.max, this.once);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer,
                             TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(Component.translatable("tooltip.pvz.sun_storage_sapling.use")
                .withStyle(ChatFormatting.GREEN));
        consumer.accept(Component.translatable("tooltip.pvz.sun_storage_sapling.amount", this.amount)
                .withStyle(ChatFormatting.YELLOW));
        if (this.once) {
            consumer.accept(Component.translatable("tooltip.pvz.sun_storage_sapling.once")
                    .withStyle(ChatFormatting.RED));
        }
    }
}
