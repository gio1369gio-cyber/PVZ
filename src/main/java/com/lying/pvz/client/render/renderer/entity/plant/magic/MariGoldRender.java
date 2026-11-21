package com.lying.pvz.client.render.renderer.entity.plant.magic;

import com.lying.pvz.client.model.entity.plant.magic.MariGoldModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.common.level.entity.plant.magic.MariGoldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * MariGold 渲染器。
 *
 * <p><b>泛型参数待核</b>：旧代码写 {@code new MariGoldModel<MariGoldEntity>(...)}，
 * 暗示旧 {@code MariGoldModel} 曾带实体泛型参数。新基类 {@code PvZPlantModel}
 * 已固定 {@code PvZPlantRenderState} 泛型，若 {@code MariGoldModel} 定义未变则
 * <b>不应再带 {@code <MariGoldEntity>}</b>。以实际模型类签名为准：
 * <ul>
 *   <li>若 {@code MariGoldModel} 无泛型参数 → 用下方 {@code new MariGoldModel(...)}</li>
 *   <li>若仍带泛型 → 改回 {@code new MariGoldModel<MariGoldEntity>(...)}</li>
 * </ul>
 */
public class MariGoldRender extends PvZPlantRender<MariGoldEntity, MariGoldModel> {

	public MariGoldRender(EntityRendererProvider.Context ctx) {
		super(ctx, new MariGoldModel(ctx.bakeLayer(MariGoldModel.LAYER_LOCATION)), 0.45f);
	}
}