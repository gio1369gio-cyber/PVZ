package com.lying.pvz.client.render.layer;

import com.lying.pvz.client.render.layer.fullskin.PVZFullSkinLayer;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.Identifier;

public class PlanternLayerRender<S extends PvZPlantRenderState, M extends EntityModel<? super S>> extends PVZFullSkinLayer<S, M> {

	public PlanternLayerRender(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	protected boolean canRender(S state) {
		return true;
	}

	@Override
	protected Identifier getResourceLocation(S state) {
		return StringUtil.id("textures/entity/layer/plantern_light.png");
	}
}