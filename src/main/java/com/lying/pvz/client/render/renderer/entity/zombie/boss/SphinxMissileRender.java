package com.lying.pvz.client.render.renderer.entity.zombie.boss;

import com.lying.pvz.client.render.state.entity.misc.PvZEntityBlockRenderState;
import com.lying.pvz.common.level.entity.zombie.boss.SphinxMissileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelSet;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;

/** 狮身终结者导弹渲染：画一个 TNT 方块。 */
public class SphinxMissileRender extends EntityRenderer<SphinxMissileEntity, PvZEntityBlockRenderState> {

    public SphinxMissileRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public PvZEntityBlockRenderState createRenderState() {
        return new PvZEntityBlockRenderState();
    }

    @Override
    public void extractRenderState(SphinxMissileEntity entity, PvZEntityBlockRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.scale = 0.6F;
        state.blockState = Blocks.TNT.defaultBlockState();
    }

    @Override
    public void submit(PvZEntityBlockRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        poseStack.pushPose();
        float f = state.scale;
        poseStack.scale(f, f, f);
        poseStack.translate(-0.5F, -0.5F, -0.5F);

        BlockModelSet set = Minecraft.getInstance().getModelManager().getBlockModelSet();
        BlockModel model = set.get(state.blockState);
        BlockModelRenderState modelState = new BlockModelRenderState();
        model.update(modelState, state.blockState, BlockDisplayContext.create(), 0L);
        modelState.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
