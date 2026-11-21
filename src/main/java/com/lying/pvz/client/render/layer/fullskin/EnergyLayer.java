package com.lying.pvz.client.render.layer.fullskin;

import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.ZombieEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class EnergyLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>>
		extends PVZFullSkinLayer<S, M> {

	public EnergyLayer(RenderLayerParent<S, M> parent) {
		super(parent);
		this.scale = 1.1F;
	}

	@Override
	protected boolean canRender(S state) {
		if (state.isInvisible) {
			return false;
		}
		if (state instanceof PvZZombieRenderState zs) {
			return zs.variantType == ZombieEntity.VariantType.SUPER;
		}
		if (state instanceof PvZPlantRenderState ps) {
			return ps.superMode;
		}
		return false;
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
		return StringUtil.id("textures/entity/layer/energy_armor.png");
	}
}