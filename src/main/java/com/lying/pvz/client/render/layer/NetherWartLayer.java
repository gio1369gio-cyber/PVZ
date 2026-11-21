package com.lying.pvz.client.render.layer;

import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelSet;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 烈焰疣头顶的地狱疣方块层。
 *
 * <p>当烈焰疣血量 ≥ 25%（即 {@code hasNetherWart()}）时，
 * 在其头顶渲染一个地狱疣方块。旧 {@code BlockRenderDispatcher.renderSingleBlock}
 * 迁移为 {@code BlockModelSet} + {@link BlockModelRenderState}。
 */
public class NetherWartLayer<S extends PvZPlantRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {

    private static final BlockState NETHER_WART = Blocks.NETHER_WART.defaultBlockState();

    public NetherWartLayer(RenderLayerParent<S, M> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector,
                       int lightCoords, S state, float yRot, float xRot) {
        if (state.percentHealth < 0.25F) {
            return;
        }

        final BlockModelSet blockModels = Minecraft.getInstance().getModelManager().getBlockModelSet();
        final BlockModel blockModel = blockModels.get(NETHER_WART);
        final BlockModelRenderState modelState = new BlockModelRenderState();
        blockModel.update(modelState, NETHER_WART, BlockDisplayContext.create(), 0L);

        poseStack.pushPose();
        // 旧坐标变换：头顶偏右前的位置，再以方块 0..1 空间居中。
        poseStack.translate(0.20000000298023224, -0.2999999940395355, 0.5);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(-0.3, -1, -1);
        poseStack.translate(-0.5, -0.5, -0.5);
        modelState.submit(poseStack, collector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
