package com.lying.pvz.client.render.layer.fullskin;

import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class HealLightLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>>
		extends PVZFullSkinLayer<S, M> {

	public HealLightLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	protected boolean canRender(S state) {
		if (state.isInvisible) {
			return false;
		}
		return state instanceof PvZPlantRenderState ps && ps.lightTick > 0;
	}

	@Override
	protected Identifier getResourceLocation(S state) {
		return StringUtil.id("textures/entity/layer/heal_light.png");
	}
}