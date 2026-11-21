package com.lying.pvz.client.render.renderer.entity.npc;

import com.lying.pvz.client.model.entity.npc.SunDaveModel;
import com.lying.pvz.client.render.renderer.entity.PvZCreatureRender;
import com.lying.pvz.common.level.entity.npc.SunDaveEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class SunDaveRender extends PvZCreatureRender<SunDaveEntity, LivingEntityRenderState, SunDaveModel> {

	private static final Identifier TEXTURE = StringUtil.id("textures/entity/npc/sun_dave.png");

	public SunDaveRender(EntityRendererProvider.Context context) {
		super(context, new SunDaveModel(context.bakeLayer(SunDaveModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	protected float getScaleByEntity(LivingEntityRenderState state) {
		return 0.55F;
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return TEXTURE;
	}
}