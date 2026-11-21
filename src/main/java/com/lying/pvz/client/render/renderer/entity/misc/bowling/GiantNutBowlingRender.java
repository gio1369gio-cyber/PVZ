package com.lying.pvz.client.render.renderer.entity.misc.bowling;


import com.lying.pvz.client.model.entity.plant.defence.WallNutModel;
import com.lying.pvz.client.render.state.entity.bowling.BowlingRenderState;
import com.lying.pvz.common.level.entity.bowling.GiantNutBowlingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GiantNutBowlingRender extends AbstractBowlingRender<GiantNutBowlingEntity, BowlingRenderState> {

	public GiantNutBowlingRender(EntityRendererProvider.Context context) {
		super(context, new WallNutModel(context.bakeLayer(WallNutModel.LAYER_LOCATION)));
	}

	@Override
	public BowlingRenderState createRenderState() {
		return new BowlingRenderState();
	}

//	@Override
//	protected @Nullable Identifier getPAZId(GiantNutBowlingEntity entity) {
//		return PVZPlants.GIANT_NUT.getId();
//	}

	@Override
	protected float getRenderSize(GiantNutBowlingEntity entity) {
		return 1.2F;
	}
}