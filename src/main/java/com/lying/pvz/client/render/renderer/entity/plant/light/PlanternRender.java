package com.lying.pvz.client.render.renderer.entity.plant.light;

import com.lying.pvz.client.model.entity.plant.light.PlanternModel;
import com.lying.pvz.client.render.layer.PlanternLayerRender;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.light.PlanternEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;

/**
 * Plantern 渲染器。
 *
 * <p><b>渲染类型变更</b>：1.16.5 通过覆写 {@code func_230042_a_}
 * （1.21.1 曾对应 {@code getRenderType(T, boolean, boolean, boolean)}）
 * 返回 {@code RenderType.entityTranslucentCull}。1.20.5 引入 submit 架构后，
 * {@code EntityRenderer} 已无此覆写点；渲染类型改由模型
 * {@code renderType(Identifier)} 提供。因此本类的 {@code getRenderType}
 * 覆写删除，逻辑下移到 {@link PlanternModel}：
 * 构造时传入 {@code RenderType::entityTranslucentCull}。
 *
 * <p><b>PlanternLayerRender</b>：构造器签名在 1.21.1 中由
 * {@code RenderLayer<S extends EntityRenderState, M extends EntityModel<S>>} 约束，
 * 若 {@code new PlanternLayerRender(this)} 不匹配，可能需要显式泛型
 * {@code <PvZPlantRenderState, PlanternModel>} 或补 {@code EntityModelSet} 参数——待 Layer 源码确认。
 */
public class PlanternRender extends PvZPlantRender<PlanternEntity, PlanternModel> {

	public PlanternRender(EntityRendererProvider.Context ctx) {
		super(ctx, new PlanternModel(
						ctx.bakeLayer(PlanternModel.LAYER_LOCATION),
						RenderTypes::entityTranslucentCull),
				0.4f);
	}

	@Override
	protected void addPlantLayers() {
		super.addPlantLayers();
		this.addLayer(new PlanternLayerRender<>(this));
	}
}