package com.lying.pvz.client.render.renderer.entity.misc.bowling;


import com.lying.pvz.client.model.entity.plant.defence.TallNutModel;
import com.lying.pvz.client.render.state.entity.bowling.BowlingRenderState;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.bowling.GiantNutBowlingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;

public class GiantNutBowlingRender extends AbstractBowlingRender<GiantNutBowlingEntity, BowlingRenderState> {


	private static final float TALL_NUT_OFFSET_Y = -0.394F;

	private static final float TALL_NUT_PIVOT_Y = -0.90625F;

	public GiantNutBowlingRender(EntityRendererProvider.Context context) {
		super(context, new TallNutModel(context.bakeLayer(TallNutModel.LAYER_LOCATION)));
	}

	@Override
	public BowlingRenderState createRenderState() {
		return new BowlingRenderState();
	}

	@Override
	protected @Nullable Identifier getPAZId(GiantNutBowlingEntity entity) {
		return PVZPlants.GIANT_NUT.getId();
	}

	@Override
	protected float getRenderSize(GiantNutBowlingEntity entity) {
		return 1.2F;
	}

	@Override
	protected float getRenderOffsetY(GiantNutBowlingEntity entity) {
		return TALL_NUT_OFFSET_Y;
	}

	@Override
	protected float getRenderPivotY(GiantNutBowlingEntity entity) {
		return TALL_NUT_PIVOT_Y;
	}
}