package com.lying.pvz.client.render.layer.fullskin;

import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class SunLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>>
		extends PVZFullSkinLayer<S, M> {

	public SunLayer(RenderLayerParent<S, M> parent) {
		super(parent);
		this.scale = 1.1F;
	}

	@Override
	protected boolean canRender(S state) {
		if (state.isInvisible) {
			return false;
		}
		return state instanceof PvZZombieRenderState zs
				&& zs.variantType == PVZZombieEntity.VariantType.SUN;
	}

	@Override
	protected float getU(float f) {
		return 0.01f * f;
	}

	@Override
	protected float getV(float f) {
		return 0.01f * f;
	}

	@Override
	protected Identifier getResourceLocation(S state) {
		return StringUtil.id("textures/entity/layer/sun_layer.png");
	}
}