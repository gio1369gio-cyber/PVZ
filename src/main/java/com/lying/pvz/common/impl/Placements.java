package com.lying.pvz.common.impl;

import com.lying.pvz.api.content.card.ICardPlacement;
import com.lying.pvz.common.init.PvZBlockRegister;
import com.lying.pvz.common.tag.PvZBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;

/**
 * 内置放置规则常量。
 */
public final class Placements {

    /**
     * 任何方块都不可放置。
     */
    public static final ICardPlacement NONE = block -> false;

    /**
     * 任何方块都可放置。
     */
    public static final ICardPlacement ANY = block -> true;

    /**
     * 标准可放置方块：{@link PvZBlockTags#PLANT_SUIT_BLOCKS}。
     */
    public static final ICardPlacement COMMON = block ->
            block.is(PvZBlockTags.PLANT_SUIT_BLOCKS);

    /**
     * 稳定可放置方块：{@link PvZBlockTags#PLANT_SUIT_BLOCKS} 且不是睡莲。
     * 用于土豆雷、地刺等需要"实心支撑"的植物。
     */
    public static final ICardPlacement STABLE = block ->
            block.is(PvZBlockTags.PLANT_SUIT_BLOCKS)
                    && !block.is(PvZBlockRegister.LILY_PAD.get());

    /**
     * 金块或金地砖。用于黄金叶。
     */
    public static final ICardPlacement GOLD = block ->
            block.is(PvZBlockTags.GOLD_TILES)
                    || block.is(Blocks.GOLD_BLOCK);

    /**
     * 沙地或标准方块。用于仙人掌。
     */
    public static final ICardPlacement SAND = block ->
            block.is(PvZBlockTags.PLANT_SUIT_BLOCKS)
                    || block.is(BlockTags.SAND);

    /**
     * 菌丝或标准方块。用于蘑菇植物。
     */
    public static final ICardPlacement SHROOM = block ->
            block.is(PvZBlockTags.PLANT_SUIT_BLOCKS)
                    || block.is(Blocks.MYCELIUM);

    /**
     * 仅睡莲。
     */
    public static final ICardPlacement LILY_PAD = block ->
            block.is(PvZBlockRegister.LILY_PAD.get());

    /**
     * 仅水。
     */
    public static final ICardPlacement WATER = block ->
            block.is(Blocks.WATER) || block.is(PvZBlockRegister.WATER_POT);

    public static final ICardPlacement SOUL_SAND = (block) ->
            block.is(PvZBlockTags.PLANT_SUIT_BLOCKS) || block.is(Blocks.SOUL_SAND);

    public static final ICardPlacement END_STONE = (block) ->
            block.is(PvZBlockTags.PLANT_SUIT_BLOCKS) || block.is(Blocks.END_STONE);

    private Placements() {
    }
}