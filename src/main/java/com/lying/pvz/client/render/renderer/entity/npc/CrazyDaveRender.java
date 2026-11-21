package com.lying.pvz.client.render.renderer.entity.npc;

import com.lying.pvz.client.model.entity.npc.CrazyDaveModel;
import com.lying.pvz.client.render.renderer.entity.PvZCreatureRender;
import com.lying.pvz.common.level.entity.npc.CrazyDaveEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class CrazyDaveRender extends PvZCreatureRender<CrazyDaveEntity, LivingEntityRenderState, CrazyDaveModel> {

	private static final Identifier TEXTURE = StringUtil.id("textures/entity/npc/crazy_dave.png");

	public CrazyDaveRender(EntityRendererProvider.Context context) {
		super(context, new CrazyDaveModel(context.bakeLayer(CrazyDaveModel.LAYER_LOCATION)), 0.4F);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	protected float getScaleByEntity(LivingEntityRenderState state) {
		return 0.6F;
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return TEXTURE;
	}
}