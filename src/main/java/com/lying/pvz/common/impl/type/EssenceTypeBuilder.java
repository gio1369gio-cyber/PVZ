package com.lying.pvz.common.impl.type;

import com.lying.pvz.api.content.IEssenceType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * {@link IEssenceType} 的构建器。
 *
 * <p>{@link #essenceItem(Supplier)} 与 {@link #essenceOre(Supplier)} 必填。
 * 辐射目标（block 或 tag）可选，两者可以同时存在。
 */
public final class EssenceTypeBuilder {

    private final Identifier id;
    private Supplier<Item> essenceItem = null;
    private Supplier<Block> essenceOre = null;
    private Supplier<Block> radiationBlock = null;
    private Supplier<TagKey<Block>> radiationTag = null;

    private EssenceTypeBuilder(Identifier id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    public static EssenceTypeBuilder of(Identifier id) {
        return new EssenceTypeBuilder(id);
    }

    public static EssenceTypeBuilder of(String namespace, String path) {
        return new EssenceTypeBuilder(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public EssenceTypeBuilder essenceItem(Supplier<Item> supplier) {
        this.essenceItem = supplier;
        return this;
    }

    public EssenceTypeBuilder essenceOre(Supplier<Block> supplier) {
        this.essenceOre = supplier;
        return this;
    }

    public EssenceTypeBuilder radiationBlock(Supplier<Block> supplier) {
        this.radiationBlock = supplier;
        return this;
    }

    public EssenceTypeBuilder radiationTag(Supplier<TagKey<Block>> supplier) {
        this.radiationTag = supplier;
        return this;
    }

    public IEssenceType build() {
        if (this.essenceItem == null) {
            throw new IllegalStateException("Essence " + this.id + " requires an essenceItem supplier");
        }
        if (this.essenceOre == null) {
            throw new IllegalStateException("Essence " + this.id + " requires an essenceOre supplier");
        }
        return new EssenceTypeImpl(this);
    }

    private static final class EssenceTypeImpl implements IEssenceType {

        private final Identifier id;
        private final Supplier<Item> essenceItem;
        private final Supplier<Block> essenceOre;
        private final Supplier<Block> radiationBlock;
        private final Supplier<TagKey<Block>> radiationTag;

        EssenceTypeImpl(EssenceTypeBuilder b) {
            this.id = b.id;
            this.essenceItem = b.essenceItem;
            this.essenceOre = b.essenceOre;
            this.radiationBlock = b.radiationBlock;
            this.radiationTag = b.radiationTag;
        }

        @Override
        public Identifier getID() {
            return this.id;
        }

        @Override
        public MutableComponent getText() {
            return Component.translatable(
                    "misc." + this.id.getNamespace() + ".essence." + this.id.getPath());
        }

        @Override
        public Item getEssenceItem() {
            return this.essenceItem.get();
        }

        @Override
        public Block getEssenceOre() {
            return this.essenceOre.get();
        }

        @Override
        public Optional<Block> getRadiationBlock() {
            return this.radiationBlock == null ? Optional.empty() : Optional.ofNullable(this.radiationBlock.get());
        }

        @Override
        public Optional<TagKey<Block>> getRadiationBlockTag() {
            return this.radiationTag == null ? Optional.empty() : Optional.ofNullable(this.radiationTag.get());
        }
    }
}