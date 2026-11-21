package com.lying.pvz.common.lottery;

import com.lying.pvz.api.lottery.SlotKind;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;

import java.util.Optional;

/**
 * {@code items} 数组的一项。
 */
public record SlotJson(
        SlotKind kind,
        Optional<Holder<Item>> item,
        Optional<CompoundTag> nbt,
        int weight
) {

    public static final Codec<SlotJson> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(s -> SlotKind.valueOf(s.toUpperCase()), k -> k.name().toLowerCase())
                    .optionalFieldOf("type", SlotKind.ITEM).forGetter(SlotJson::kind),
            BuiltInRegistries.ITEM.holderByNameCodec()
                    .optionalFieldOf("item").forGetter(SlotJson::item),
            CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(SlotJson::nbt),
            Codec.INT.optionalFieldOf("weight", 10).forGetter(SlotJson::weight)
    ).apply(instance, SlotJson::new));
}