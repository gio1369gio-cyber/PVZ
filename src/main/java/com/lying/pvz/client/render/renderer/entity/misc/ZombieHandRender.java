package com.lying.pvz.client.render.renderer.entity.misc;

import com.lying.pvz.client.model.entity.misc.ZombieHandModel;
import com.lying.pvz.client.render.renderer.entity.PvZEntityRender;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.misc.ZombieHandEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class ZombieHandRender extends PvZEntityRender<ZombieHandEntity, PvZEntityRenderState, ZombieHandModel> {

	private static final Identifier ZOMBIE_HAND_TEX = StringUtil.id("textures/entity/zombie/misc/zombie_hand.png");

	public ZombieHandRender(EntityRendererProvider.Context context) {
		super(context, new ZombieHandModel(context.bakeLayer(ZombieHandModel.LAYER_LOCATION)));
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	public void extractRenderState(ZombieHandEntity entity, PvZEntityRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.texture = ZOMBIE_HAND_TEX;
		state.tick = entity.getTick();
	}

	@Override
	protected float getScaleByEntity(ZombieHandEntity entity) {
		return 0.75F;
	}

	@Override
	public Vec3 getTranslateVec(ZombieHandEntity entity) {
		int tick = entity.getTick(); // 1 - 40
		return new Vec3(0, 0.05F * tick, 0);
	}
}