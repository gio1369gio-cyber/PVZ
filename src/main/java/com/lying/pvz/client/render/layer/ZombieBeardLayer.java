package com.lying.pvz.client.render.layer;

import com.lying.pvz.client.render.layer.fullskin.PVZFullSkinLayer;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.PVZZombieEntity;
import com.lying.pvz.utils.StringUtil;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class ZombieBeardLayer<S extends LivingEntityRenderState,
		M extends EntityModel<? super S>>
		extends PVZFullSkinLayer<S, M> {

	public ZombieBeardLayer(RenderLayerParent<S, M> parent) {
		super(parent);
	}

	@Override
	protected boolean canRender(S state) {
		if (state.isInvisible) {
			return false;
		}
		return state instanceof PvZZombieRenderState zs
				&& zs.variantType == PVZZombieEntity.VariantType.BEARD;
	}

	@Override
	protected Identifier getResourceLocation(S state) {
		return StringUtil.id("textures/entity/layer/zombie_beard.png");
	}
}