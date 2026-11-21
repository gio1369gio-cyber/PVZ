package com.lying.pvz.client.render.renderer.entity.npc;

import com.lying.pvz.client.model.entity.npc.PennyModel;
import com.lying.pvz.client.render.renderer.entity.PvZCreatureRender;
import com.lying.pvz.common.level.entity.npc.PennyEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class PennyRender extends PvZCreatureRender<PennyEntity, LivingEntityRenderState, PennyModel> {

	public static final Identifier PENNY_TEX = StringUtil.id("textures/entity/npc/panney.png");

	public PennyRender(EntityRendererProvider.Context context) {
		super(context, new PennyModel(context.bakeLayer(PennyModel.LAYER_LOCATION)), 1.0F);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	protected float getScaleByEntity(LivingEntityRenderState state) {
		return 1.5F;
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return PENNY_TEX;
	}
}