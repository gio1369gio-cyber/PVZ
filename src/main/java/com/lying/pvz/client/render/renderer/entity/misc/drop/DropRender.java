package com.lying.pvz.client.render.renderer.entity.misc.drop;

import com.lying.pvz.client.render.renderer.entity.PvZEntityRender;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.drop.DropEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;

/**
 * 掉落物渲染基类。
 *
 * <p>统一做 billboard：提交前绕相机朝向旋转，再交给 {@link PvZEntityRender}
 * 处理缩放 / 脚底对齐 / 平移 / 模型提交。
 */
public abstract class DropRender<T extends DropEntity, S extends PvZEntityRenderState, M extends EntityModel<? super S>>
		extends PvZEntityRender<T, S, M> {

	protected DropRender(EntityRendererProvider.Context context, M model) {
		super(context, model);
	}

	@Override
	public void submit(S state, PoseStack poseStack,
					   SubmitNodeCollector collector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.rotate(camera.orientation);
		super.submit(state, poseStack, collector, camera);
		poseStack.popPose();
	}
}