package com.lying.pvz.common.impl.type;

import com.lying.pvz.api.content.IRankType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * {@link IRankType} 的构建器。
 *
 * <p>{@link #templateCard(Supplier)} 必填；{@link #cardTag(Supplier)} / {@link #material(Supplier)}
 * 可选——未设置时 {@link IRankType#getCardTag()} / {@link IRankType#getMaterial()} 返回 {@code Optional.empty()}。
 * 这与旧代码中 MEGA 等级返回 {@code null} 的语义对齐，但不再引入 NPE 风险。
 */
public final class RankTypeBuilder {

    private final Identifier id;
    private int weight = 10;
    private int value = 10;
    private int enchantPoint = 10;
    private Supplier<Item> templateCard = null;
    private Supplier<TagKey<Item>> cardTag = null;
    private Supplier<TagKey<Item>> material = null;

    private RankTypeBuilder(Identifier id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    public static RankTypeBuilder of(Identifier id) {
        return new RankTypeBuilder(id);
    }

    public static RankTypeBuilder of(String namespace, String path) {
        return new RankTypeBuilder(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public RankTypeBuilder weight(int weight) {
        this.weight = weight;
        return this;
    }

    public RankTypeBuilder value(int value) {
        this.value = value;
        return this;
    }

    public RankTypeBuilder enchantPoint(int point) {
        this.enchantPoint = point;
        return this;
    }

    public RankTypeBuilder templateCard(Supplier<Item> supplier) {
        this.templateCard = supplier;
        return this;
    }

    public RankTypeBuilder cardTag(Supplier<TagKey<Item>> supplier) {
        this.cardTag = supplier;
        return this;
    }

    public RankTypeBuilder material(Supplier<TagKey<Item>> supplier) {
        this.material = supplier;
        return this;
    }

    public IRankType build() {
        if (this.templateCard == null) {
            throw new IllegalStateException("Rank " + this.id + " requires a templateCard supplier");
        }
        return new RankTypeImpl(this);
    }

    private static final class RankTypeImpl implements IRankType {

        private final Identifier id;
        private final int weight;
        private final int value;
        private final int enchantPoint;
        private final Supplier<Item> templateCard;
        private final Supplier<TagKey<Item>> cardTag;
        private final Supplier<TagKey<Item>> material;

        RankTypeImpl(RankTypeBuilder b) {
            this.id = b.id;
            this.weight = b.weight;
            this.value = b.value;
            this.enchantPoint = b.enchantPoint;
            this.templateCard = b.templateCard;
            this.cardTag = b.cardTag;
            this.material = b.material;
        }

        @Override
        public Identifier getID() {
            return this.id;
        }

        @Override
        public MutableComponent getText() {
            return Component.translatable(
                    "misc." + this.id.getNamespace() + ".rank." + this.id.getPath());
        }

        @Override
        public int getWeight() {
            return this.weight;
        }

        @Override
        public int getValue() {
            return this.value;
        }

        @Override
        public int getEnchantPoint() {
            return this.enchantPoint;
        }

        @Override
        public Item getTemplateCard() {
            return this.templateCard.get();
        }

        @Override
        public Optional<TagKey<Item>> getCardTag() {
            return this.cardTag == null ? Optional.empty() : Optional.ofNullable(this.cardTag.get());
        }

        @Override
        public Optional<TagKey<Item>> getMaterial() {
            return this.material == null ? Optional.empty() : Optional.ofNullable(this.material.get());
        }
    }
}