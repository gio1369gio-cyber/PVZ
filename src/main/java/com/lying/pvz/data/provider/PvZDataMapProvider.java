package com.lying.pvz.data.provider;

import com.lying.pvz.common.init.PvZBlockRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.CopyPropertiesProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.BlockTransformAppender;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PvZDataMapProvider extends DataMapProvider {

    public PvZDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(NeoForgeDataMaps.BLOCK_TRANSFORM_APPENDERS)   // ← 复数！
                .add(BlockTransformers.AXE,                          // ResourceKey<BlockTransformer>
                        new BlockTransformAppender(List.of(
                                stripTransform(PvZBlockRegister.NUT_LOG.get(),
                                        PvZBlockRegister.STRIPPED_NUT_LOG.get()),
                                stripTransform(PvZBlockRegister.NUT_WOOD.get(),
                                        PvZBlockRegister.STRIPPED_NUT_WOOD.get())
                        )),
                        false                                          // 追加，不替换
                );
    }

    /**
     * 构造一条"匹配 from → 替换为 to，并复制同名属性（AXIS）"的规则。
     * 必须用 CopyPropertiesProvider，不能用 BlockTransformData.builder(BlockPredicate, Block)。
     */
    private static BlockTransformer.BlockTransformData stripTransform(Block from, Block to) {
        return BlockTransformer.BlockTransformData.builder(
                        RuleBasedStateProvider.ifTrueThenProvide(
                                BlockPredicate.matchesBlocks(from),
                                new CopyPropertiesProvider(to)))   // ← 必须，保 AXIS
                .sound(SoundEvents.AXE_STRIP)
                .build();
    }
}
