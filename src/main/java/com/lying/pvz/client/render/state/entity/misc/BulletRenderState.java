package com.lying.pvz.client.render.state.entity.misc;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * 新弹药体系的统一渲染状态。
 *
 * <p>继承 {@link PvZEntityRenderState}，同时支持：
 * <ul>
 *   <li>物品视觉：{@link #item}；</li>
 *   <li>方块视觉：{@link #blockState}；</li>
 *   <li>模型视觉：{@link PvZEntityRenderState#texture} / {@link PvZEntityRenderState#spin}。</li>
 * </ul>
 */
public class BulletRenderState extends PvZEntityRenderState {

    public int variant;
    public @Nullable Identifier visualKey;
    public BlockState blockState = Blocks.AIR.defaultBlockState();
    public final ItemStackRenderState item = new ItemStackRenderState();
}
