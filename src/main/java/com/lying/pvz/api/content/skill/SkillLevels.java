package com.lying.pvz.api.content.skill;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 技能等级集合，不可变。
 *
 * <p>key 为 {@link ISkillType#getID()}，value 为等级（{@code > 0}）。
 * 等级 {@code <= 0} 等价于不存在，{@link #with(Identifier, int)} 传非正值会移除该项。
 *
 * <p>同一份数据结构既用于卡片（{@code CARD_SKILLS} 组件），
 * 也用于实体（{@code IPAZEntity#getSkillLevels()}），保证传导模型一致。
 *
 */
public record SkillLevels(Map<Identifier, Integer> levels) {

    public static final SkillLevels EMPTY = new SkillLevels(Map.of());

    public static final Codec<SkillLevels> CODEC = RecordCodecBuilder.<SkillLevels>create(instance ->
            instance.group(
                    Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .fieldOf("levels")
                            .forGetter(SkillLevels::levels)
            ).apply(instance, SkillLevels::new)
    );

    public static final StreamCodec<ByteBuf, SkillLevels> STREAM_CODEC =
            StreamCodec.<ByteBuf, SkillLevels, Map<Identifier, Integer>>composite(
                    ByteBufCodecs.<ByteBuf, Identifier, Integer, Map<Identifier, Integer>>map(
                            HashMap::new,
                            Identifier.STREAM_CODEC,
                            ByteBufCodecs.VAR_INT
                    ),
                    SkillLevels::levels,
                    SkillLevels::new
            );

    public SkillLevels {
        levels = Map.copyOf(levels);
    }

    public int get(Identifier skillID) {
        return this.levels.getOrDefault(skillID, 0);
    }

    public int getLevel(ISkillType type) {
        return this.get(type.getID());
    }

    public boolean isEmpty() {
        return this.levels.isEmpty();
    }

    /**
     * 设置技能等级。{@code level <= 0} 等价于移除；与当前值相同则返回自身。
     */
    public SkillLevels with(Identifier skillID, int level) {
        if (level <= 0) {
            return this.without(skillID);
        }
        if (this.levels.getOrDefault(skillID, 0) == level) {
            return this;
        }
        final Map<Identifier, Integer> copy = new HashMap<>(this.levels);
        copy.put(skillID, level);
        return new SkillLevels(copy);
    }

    /**
     * 移除技能。
     */
    public SkillLevels without(Identifier skillID) {
        if (!this.levels.containsKey(skillID)) {
            return this;
        }
        final Map<Identifier, Integer> copy = new HashMap<>(this.levels);
        copy.remove(skillID);
        return new SkillLevels(copy);
    }

    /**
     * 合并另一个 {@link SkillLevels}。冲突时以 {@code other} 为准。
     */
    public SkillLevels merge(SkillLevels other) {
        if (other.isEmpty()) {
            return this;
        }
        if (this.isEmpty()) {
            return other;
        }
        final Map<Identifier, Integer> copy = new HashMap<>(this.levels);
        copy.putAll(other.levels);
        return new SkillLevels(copy);
    }

    /**
     * 按 ID 过滤。作用面过滤由调用方在 {@code predicate} 内完成。
     */
    public SkillLevels filter(Predicate<Identifier> predicate) {
        final Map<Identifier, Integer> result = new HashMap<>();
        for (var entry : this.levels.entrySet()) {
            if (predicate.test(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result.isEmpty() ? EMPTY : new SkillLevels(result);
    }
}