package com.lying.pvz.client.render.layer.fullskin;

import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class ColdLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>>
		extends PVZFullSkinLayer<S, M> {

	public ColdLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	protected boolean canRender(S state) {
		if (state.isInvisible) {
			return false;
		}
		return readCold(state);
	}

	private static boolean readCold(LivingEntityRenderState state) {
		if (state instanceof PvZPlantRenderState ps) {
			return ps.cold;
		}
		if (state instanceof PvZZombieRenderState zs) {
			return zs.cold;
		}
		return false;
	}

	@Override
	protected Identifier getResourceLocation(S state) {
		return StringUtil.id("textures/entity/layer/cold_layer.png");
	}
}