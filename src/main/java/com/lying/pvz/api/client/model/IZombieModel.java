package com.lying.pvz.api.client.model;

import com.lying.pvz.api.entity.IBodyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;

/**
 * 僵尸模型接口。
 *
 * <p><b>加载约束</b>：本接口 import 客户端专用类型，<b>禁止服务端引用</b>。
 *
 * <p><b>1.21 渲染系统</b>：{@code EntityModel<T>} 的 {@code T} 是
 * {@link EntityRenderState}。本接口不带实体泛型。
 */
public interface IZombieModel {

    /**
     * 更新断肢动画状态。仅用于渲染掉落肢体实体，不用于本体。
     */
    void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
                      float ageInTicks, float netHeadYaw, float headPitch);

    /**
     * 渲染断肢。仅用于渲染掉落肢体实体，不用于本体。
     *
     * <p>1.21 走 collector 管线：调用方先在 {@link #tickPartAnim} 里摆好 ModelPart，
     * 再由本方法把 ModelPart 逐块提交到 {@link SubmitNodeCollector}。
     *
     * @param renderType 已由调用方通过 {@code getZombieModel().renderType(texture)} 算好
     */
    void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector,
                    RenderType renderType, int packedLight, int packedOverlay);

    /**
     * 返回本体模型。
     */
    EntityModel<? extends EntityRenderState> getZombieModel();
}