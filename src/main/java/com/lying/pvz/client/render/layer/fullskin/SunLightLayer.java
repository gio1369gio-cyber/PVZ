package com.lying.pvz.client.render.layer.fullskin;

import com.lying.pvz.client.render.renderer.entity.creature.FoodieZombieRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class SunLightLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>>
		extends PVZFullSkinLayer<S, M> {

	public SunLightLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	protected boolean canRender(S state) {
		if (state.isInvisible) {
			return false;
		}
		if (state instanceof PvZPlantRenderState ps) {
			return ps.sunLight;
		}
		if (state instanceof PvZZombieRenderState zs) {
			return zs.sunLight;
		}
		if (state instanceof FoodieZombieRender.FoodieZombieRenderState fs){
			return fs.sunLight;
		}
		return false;
	}

	@Override
	protected Identifier getResourceLocation(S state) {
		return StringUtil.id("textures/entity/layer/sun_light.png");
	}
}