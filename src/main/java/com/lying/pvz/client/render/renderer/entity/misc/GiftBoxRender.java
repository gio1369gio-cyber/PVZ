package com.lying.pvz.client.render.renderer.entity.misc;

import com.lying.pvz.client.model.entity.misc.GiftBoxModel;
import com.lying.pvz.client.render.renderer.entity.PvZEntityRender;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.drop.GiftBoxEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class GiftBoxRender extends PvZEntityRender<GiftBoxEntity, PvZEntityRenderState, GiftBoxModel> {

	private static final Identifier RES = StringUtil.id("textures/entity/misc/gift_box.png");

	public GiftBoxRender(EntityRendererProvider.Context context) {
		super(context, new GiftBoxModel(context.bakeLayer(GiftBoxModel.LAYER_LOCATION)));
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	public void extractRenderState(GiftBoxEntity entity,
								   PvZEntityRenderState state,
								   float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.texture = RES;
	}

	@Override
	protected float getScaleByEntity(GiftBoxEntity entity) {
		return 1.0F;
	}
}