package com.lying.pvz.api.content.card;

import net.minecraft.world.level.block.state.BlockState;

/**
 * 卡片放置规则：判断给定方块状态是否可作为放置基座。
 *
 * <p><b>主推签名</b>为 {@link #canPlaceOnBlockState(BlockState)}，可感知方块状态
 * 委托给 {@code state.getBlock().defaultBlockState()}。
 *
 * <p>实现方只需覆写 {@link #canPlaceOnBlockState(BlockState)}；
 * 调用方可以按需用任一签名，语义一致。
 */
public interface ICardPlacement {

    /**
     * 判断该方块状态是否可作为放置基座。
     *
     * @param state 目标位置的方块状态，调用方保证非空
     */
    boolean canPlaceOnBlockState(BlockState state);


}