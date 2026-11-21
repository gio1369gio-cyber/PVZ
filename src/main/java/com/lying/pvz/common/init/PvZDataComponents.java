package com.lying.pvz.common.init;

import com.lying.pvz.PvZMod;
import com.lying.pvz.common.level.item.component.SunStorage;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PvZDataComponents {

    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PvZMod.MOD_ID);


    /** 老虎机 */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Identifier>> LOTTERY_TYPE =
            COMPONENTS .registerComponentType("lottery_type",
                    builder -> builder.persistent(Identifier.CODEC)
                            .networkSynchronized(Identifier.STREAM_CODEC));

    /**
     * 太阳数量。
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SunStorage>> SUN_STORAGE =
            COMPONENTS.registerComponentType("sun_storage", builder -> builder
                    .persistent(SunStorage.CODEC)
                    .networkSynchronized(SunStorage.STREAM_CODEC));


    /** 花园指南针目标坐标（未设置 = null）。 */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> COMPASS_TARGET =
            COMPONENTS.registerComponentType("compass_target",
                    builder -> builder.persistent(BlockPos.CODEC)
                            .networkSynchronized(BlockPos.STREAM_CODEC));


    /** 保龄球手套绑定的植物身份字符串（IPlantType.getIdentity()）。 */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Identifier>> BOWLING_TYPE =
            COMPONENTS.registerComponentType("bowling_type",
                    builder -> builder.persistent(Identifier.CODEC)
                            .networkSynchronized(Identifier.STREAM_CODEC));

    /** 卡包（*/
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> CARD_CONTENTS =
            COMPONENTS.registerComponentType("card_contents", builder -> builder
                    .persistent(ItemContainerContents.CODEC)
                    .networkSynchronized(ItemContainerContents.STREAM_CODEC));

    /**
     * 挑战类型。
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Identifier>> CHALLENGE_TYPE =
            COMPONENTS.registerComponentType("challenge_type", builder -> builder
                    .persistent(Identifier.CODEC)
                    .networkSynchronized(Identifier.STREAM_CODEC));
}
