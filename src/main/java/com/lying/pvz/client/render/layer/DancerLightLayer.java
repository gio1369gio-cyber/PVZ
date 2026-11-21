package com.lying.pvz.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class DancerLightLayer<S extends LivingEntityRenderState,
        M extends EntityModel<? super S>>
        extends RenderLayer<S, M> {

    public DancerLightLayer(RenderLayerParent<S, M> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector,
                       int lightCoords, S state, float yRot, float xRot) {
        if (state.deathTime > 0 || state.isInvisible) {
            return;
        }

        collector.order(0).submitCustomGeometry(poseStack, RenderTypes.lightning(),
                (pose, consumer) -> {
                    for (int i = 0; i < 4; i++) {
                        consumer.addVertex(pose.pose(), 0.0f, -10.0f, 0.0f)
                                .setColor(128, 0, 128, 255);
                        consumer.addVertex(pose.pose(), -0.866f * 2, 2, -0.5F * 2)
                                .setColor(128, 0, 128, 20);
                        consumer.addVertex(pose.pose(), 0.866f * 2, 2, -0.5F * 2)
                                .setColor(128, 0, 128, 20);
                        consumer.addVertex(pose.pose(), 0.0f, 2, 1.0F * 2)
                                .setColor(128, 0, 128, 20);
                        consumer.addVertex(pose.pose(), -0.866f * 2, 2, -0.5F * 2)
                                .setColor(128, 0, 128, 20);
                    }
                });
    }
}