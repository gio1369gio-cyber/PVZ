package com.lying.pvz.client.render.renderer.entity.misc.bowling;

import com.lying.pvz.client.model.entity.plant.defence.WallNutModel;
import com.lying.pvz.client.render.state.entity.bowling.BowlingRenderState;
import com.lying.pvz.common.level.entity.bowling.WallNutBowlingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class WallNutBowlingRender extends AbstractBowlingRender<WallNutBowlingEntity, BowlingRenderState> {

	private static final float WALL_NUT_OFFSET_Y = 1.5F;
	private static final float WALL_NUT_PIVOT_Y = 1.0F;

	public WallNutBowlingRender(EntityRendererProvider.Context context) {
		super(context, new WallNutModel(context.bakeLayer(WallNutModel.LAYER_LOCATION)));
	}

	@Override
	public BowlingRenderState createRenderState() {
		return new BowlingRenderState();
	}

	@Override
	protected float getRenderOffsetY(WallNutBowlingEntity entity) {
		return WALL_NUT_OFFSET_Y;
	}

	@Override
	protected float getRenderPivotY(WallNutBowlingEntity entity) {
		return WALL_NUT_PIVOT_Y;
	}
}