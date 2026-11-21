package com.lying.pvz.api.projectile;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 目标身上的通用标记。
 *
 * <p>标记不是 {@code MobEffect}：它只做"层数 + 到期时间 + 来源"，
 * 具体效果由读取方决定（加伤害、开机制、视觉层）。
 *
 * <p><b>来源隔离</b>：默认 {@link Scope#PER_SOURCE}，层数按
 * {@code (mark id, source UUID)} 分开。A 植物叠的层，B 植物读不到；
 * 只有显式使用 {@link Scope#SHARED} 的机制标记才会跨来源共享。
 *
 * <p><b>到期</b>：每条标记有独立的 {@code expireTick}。加层默认刷新到期时间
 * （{@link Spec#refreshOnAdd()}），到期后整条删除；发射者死亡不会立刻清除，
 * 按 duration 自然失效。
 */
public final class Mark {

    private Mark() {
    }

    /** 标记键：标记 id + 来源实体 UUID（通常是发射者）。 */
    public record Key(Identifier mark, UUID source) {

        public Key {
            Objects.requireNonNull(mark, "mark");
            Objects.requireNonNull(source, "source");
        }
    }

    /**
     * 持久化条目：层数 + 到期时间。
     *
     * <p>{@code expireTick} 是 {@code level.getGameTime()} 的绝对值。
     */
    public record Entry(Identifier mark, UUID source, int stacks, long expireTick) {

        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("mark").forGetter(Entry::mark),
                UUIDUtil.CODEC.fieldOf("source").forGetter(Entry::source),
                Codec.INT.fieldOf("stacks").forGetter(Entry::stacks),
                Codec.LONG.fieldOf("expire").forGetter(Entry::expireTick)
        ).apply(instance, Entry::new));

        public View view() {
            return new View(this.mark, this.source, this.stacks);
        }
    }

    /** 网络视图：只同步渲染需要的字段。 */
    public record View(Identifier mark, UUID source, int stacks) {

        public static final StreamCodec<ByteBuf, View> STREAM_CODEC = StreamCodec.composite(
                Identifier.STREAM_CODEC, View::mark,
                UUIDUtil.STREAM_CODEC, View::source,
                ByteBufCodecs.VAR_INT, View::stacks,
                View::new
        );
    }

    public enum Scope {
        /** 按来源分开计层（默认）。 */
        PER_SOURCE,
        /** 忽略来源，全局共享同一份层数（只给少数机制标记用）。 */
        SHARED
    }

    /**
     * 标记定义。
     *
     * @param mark         标记 id
     * @param maxStacks    最大层数
     * @param duration     持续时间（tick）
     * @param refreshOnAdd 加层时是否刷新到期时间
     * @param scope        计层范围
     * @param visualKey    客户端视觉键
     */
    public record Spec(
            Identifier mark,
            int maxStacks,
            int duration,
            boolean refreshOnAdd,
            Scope scope,
            Identifier visualKey
    ) {

        public Spec {
            Objects.requireNonNull(mark, "mark");
            if (maxStacks <= 0) {
                throw new IllegalArgumentException("maxStacks must be > 0");
            }
            if (duration <= 0) {
                throw new IllegalArgumentException("duration must be > 0");
            }
            Objects.requireNonNull(scope, "scope");
            Objects.requireNonNull(visualKey, "visualKey");
        }
    }

    /** 由 {@code AbstractPAZEntity} 实现。 */
    public interface Holder {

        /** 精确读取：只返回指定来源的层数。 */
        int marks(Key key);

        /** 聚合读取：所有来源的层数之和，只用于 UI/视觉。 */
        int marks(Identifier mark);

        /**
         * 加层并返回加完后的层数。超过 {@link Spec#maxStacks()} 时截断。
         */
        int addMark(Key key, int amount);

        /**
         * 减层并返回实际移除的层数。
         */
        int removeMark(Key key, int amount);

        boolean hasMark(Key key);

        /** 客户端渲染用视图；服务端返回当前全部条目。 */
        List<View> views();
    }
}
