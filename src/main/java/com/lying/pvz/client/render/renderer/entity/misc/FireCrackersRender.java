package com.lying.pvz.client.render.renderer.entity.misc;

import com.lying.pvz.client.model.entity.misc.FireCrackersModel;
import com.lying.pvz.client.render.renderer.entity.PvZEntityRender;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.misc.FireCrackersEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class FireCrackersRender extends PvZEntityRender<FireCrackersEntity, PvZEntityRenderState, FireCrackersModel> {

	private static final Identifier FIRE_CRACKERS_TEX = StringUtil.id("textures/entity/misc/fire_crackers.png");

	public FireCrackersRender(EntityRendererProvider.Context context) {
		super(context, new FireCrackersModel(context.bakeLayer(FireCrackersModel.LAYER_LOCATION)));
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	public void extractRenderState(FireCrackersEntity entity, PvZEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.texture =  FIRE_CRACKERS_TEX;
	}


	@Override
	protected float getScaleByEntity(FireCrackersEntity entity) {
		return 1F + 0.1F * Mth.sin(entity.getFuse() * 0.1F);
	}
}