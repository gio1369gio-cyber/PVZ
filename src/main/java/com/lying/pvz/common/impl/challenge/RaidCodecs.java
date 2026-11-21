package com.lying.pvz.common.impl.challenge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public final class RaidCodecs {

    private RaidCodecs() {
    }

    /**
     * 强 type 模式：{@code "type"} 必填，缺失直接报错。
     */
    public static <T> Codec<T> dispatch(
            String typeKey,
            Map<String, MapCodec<? extends T>> registry,
            Function<? super T, String> typeGetter) {
        return Codec.STRING.dispatch(typeKey, typeGetter, name -> {
            final MapCodec<? extends T> codec = registry.get(name);
            return codec != null ? codec : failingMapCodec(typeKey, name);
        });
    }

    /** 总是失败的 MapCodec，用于未知 type 分支，避免 NPE。 */
    public static <T> MapCodec<T> failingMapCodec(String typeKey, String typeName) {
        final String message = "Unknown " + typeKey + ": " + typeName;
        return new MapCodec<>() {
            @Override
            public <O> DataResult<T> decode(DynamicOps<O> ops, MapLike<O> input) {
                return DataResult.error(() -> message);
            }

            @Override
            public <O> RecordBuilder<O> encode(T input, DynamicOps<O> ops, RecordBuilder<O> prefix) {
                return prefix.withErrorsFrom(DataResult.error(() -> message));
            }

            @Override
            public <O> Stream<O> keys(DynamicOps<O> ops) {
                return Stream.empty();
            }

            @Override
            public String toString() {
                return "failing[" + message + "]";
            }
        };
    }
}