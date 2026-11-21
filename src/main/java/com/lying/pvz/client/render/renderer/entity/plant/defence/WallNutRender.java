package com.lying.pvz.client.render.renderer.entity.plant.defence;

import com.lying.pvz.client.model.entity.plant.defence.WallNutModel;
import com.lying.pvz.client.render.layer.component.WallNutArmorLayer;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.defence.WallNutEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

/**
 * WallNut 渲染器。
 *
 * <p>按健康比例切贴图（TEX1/TEX2/TEX3）。原 1.16.5 从 entity 读 health/maxHealth；
 * 迁移后 {@code getTextureLocation} 参数是 render state，故需
 * {@link PvZPlantRenderState} 新增 {@code health}、{@code maxHealth} 字段，
 * 并在 {@code extractRenderState} 填充（见下方 PvZPlantRenderState 补丁）。
 */
public class WallNutRender extends PvZPlantRender<WallNutEntity, WallNutModel> {

	private final Identifier TEX1 = StringUtil.id("textures/entity/plant/defence/wall_nut.png");
	private final Identifier TEX2 = StringUtil.id("textures/entity/plant/defence/wall_nut_1.png");
	private final Identifier TEX3 = StringUtil.id("textures/entity/plant/defence/wall_nut_2.png");

	public WallNutRender(EntityRendererProvider.Context ctx) {
		super(ctx, new WallNutModel(ctx.bakeLayer(WallNutModel.LAYER_LOCATION)), 0.55f);
	}

	@Override
	protected void addPlantLayers() {
		super.addPlantLayers();
		this.addLayer(new WallNutArmorLayer(this));
	}

	@Override
	public void extractRenderState(WallNutEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.percentHealth = entity.getHealth()/entity.getMaxHealth();
	}

	@Override
	public Identifier getTextureLocation(PvZPlantRenderState state) {
		final float percent = state.percentHealth;
		return percent > 2 / 3F ? TEX1 : percent > 1 / 3F ? TEX2 : TEX3;
	}
}