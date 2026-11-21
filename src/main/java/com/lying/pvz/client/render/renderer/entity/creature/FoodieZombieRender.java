package com.lying.pvz.client.render.renderer.entity.creature;

import com.lying.pvz.client.model.entity.creature.FoodieZombieModel;
import com.lying.pvz.client.render.layer.fullskin.SunLightLayer;
import com.lying.pvz.client.render.renderer.entity.PvZCreatureRender;
import com.lying.pvz.common.level.entity.creature.FoodieZombieEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class FoodieZombieRender extends PvZCreatureRender<FoodieZombieEntity,
		FoodieZombieRender.FoodieZombieRenderState, FoodieZombieModel> {

	private static final Identifier TEXTURE = StringUtil.id("textures/entity/zombie/pool/snorkel_zombie.png");

	public FoodieZombieRender(EntityRendererProvider.Context context) {
		super(context,
				new FoodieZombieModel(context.bakeLayer(FoodieZombieModel.LAYER_LOCATION)),
				0.0F);
		this.addLayer(new SunLightLayer<>(this));
	}

	@Override
	public void extractRenderState(FoodieZombieEntity entity, FoodieZombieRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
            int tick = entity.getGenTick();
            state.sunLight = tick > 0 && tick <= 60;
	}

	@Override
	public FoodieZombieRenderState createRenderState() {
		return new FoodieZombieRenderState();
	}

	@Override
	protected float getScaleByEntity(FoodieZombieRenderState state) {
		float sz = 0.5F;
		if (state.isBaby) {
			sz *= 0.5F;
		}
		return sz;
	}

	@Override
	public Identifier getTextureLocation(FoodieZombieRenderState state) {
		return TEXTURE;
	}

	public static class FoodieZombieRenderState extends LivingEntityRenderState {
        public boolean sunLight;
	}
}