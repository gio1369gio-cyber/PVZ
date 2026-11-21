package com.lying.pvz.client.render.renderer.entity.misc.bowling;

import com.lying.pvz.client.model.entity.plant.defence.WallNutModel;
import com.lying.pvz.client.render.state.entity.bowling.BowlingRenderState;
import com.lying.pvz.common.level.entity.bowling.ExplosionBowlingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ExplosionBowlingRender extends AbstractBowlingRender<ExplosionBowlingEntity, BowlingRenderState> {

	public ExplosionBowlingRender(EntityRendererProvider.Context context) {
		super(context,
				new WallNutModel(context.bakeLayer(WallNutModel.LAYER_LOCATION)));
	}

	@Override
	public BowlingRenderState createRenderState() {
		return new BowlingRenderState();
	}

//	@Override
//	protected @Nullable Identifier getPAZId(ExplosionBowlingEntity entity) {
//		return PVZPlants.EXPLODE_O_NUT.getId();
//	}
}