package com.lying.pvz.client.render.layer;

import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

/**
 * 紫颂果投手发光眼睛层：用发光纹理叠加渲染父模型。
 */
public class ChorusFruitPultEyeLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {

    private static final RenderType CHORUS_FRUIT_PULT_EYES =
            RenderTypes.eyes(StringUtil.id("textures/entity/layer/eyes/chorus_fruit_pult_eyes.png"));

    public ChorusFruitPultEyeLayer(RenderLayerParent<S, M> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector,
                       int lightCoords, S state, float yRot, float xRot) {
        collector.order(0).submitModel(this.getParentModel(), state, poseStack,
                CHORUS_FRUIT_PULT_EYES, lightCoords, OverlayTexture.NO_OVERLAY,
                -1, null, state.outlineColor);
    }
}
