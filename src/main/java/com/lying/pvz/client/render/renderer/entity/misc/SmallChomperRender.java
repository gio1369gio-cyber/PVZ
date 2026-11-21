package com.lying.pvz.client.render.renderer.entity.misc;

import com.lying.pvz.client.model.entity.misc.SmallChomperModel;
import com.lying.pvz.client.render.renderer.entity.PvZEntityRender;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.misc.SmallChomperEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class SmallChomperRender extends PvZEntityRender<SmallChomperEntity, PvZEntityRenderState, SmallChomperModel> {

	private static final Identifier CHOMPER_TEX = StringUtil.id("textures/entity/plant/enforce/chomper.png");

	public SmallChomperRender(EntityRendererProvider.Context context) {
		super(context, new SmallChomperModel(context.bakeLayer(SmallChomperModel.LAYER_LOCATION)));
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	public void extractRenderState(SmallChomperEntity entity, PvZEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.texture = CHOMPER_TEX;
		state.tick = entity.getTick();
	}

	@Override
	protected float getScaleByEntity(SmallChomperEntity entity) {
		return 1.0F;
	}

	@Override
	public Vec3 getTranslateVec(SmallChomperEntity entity) {
		int tick = entity.getTick(); // 1 - 20
		return new Vec3(0, 1F - 0.05F * tick, 0);
	}
}