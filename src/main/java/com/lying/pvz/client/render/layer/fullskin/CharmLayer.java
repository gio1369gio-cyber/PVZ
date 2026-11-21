package com.lying.pvz.client.render.layer.fullskin;

import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class CharmLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>>
		extends PVZFullSkinLayer<S, M> {

	public CharmLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	protected boolean canRender(S state) {
		if (state.isInvisible) {
			return false;
		}
		if (state instanceof PvZZombieRenderState zs) {
			return zs.charmed;
		}
		if (state instanceof PvZPlantRenderState ps) {
			return ps.superMode || ps.charmed;
		}
		return false;
	}

	@Override
	protected float getU(float f) {
		return 0.02f * f;
	}

	@Override
	protected float getV(float f) {
		return 0.02f * f;
	}

	@Override
	protected Identifier getResourceLocation(S state) {
		return StringUtil.id("textures/entity/layer/charm.png");
	}
}