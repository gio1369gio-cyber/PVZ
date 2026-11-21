package com.lying.pvz.client.render.renderer.entity.misc.bowling;

import com.lying.pvz.client.model.entity.plant.defence.WallNutModel;
import com.lying.pvz.client.render.state.entity.bowling.BowlingRenderState;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.common.level.entity.bowling.ExplosionBowlingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;

public class ExplosionBowlingRender extends AbstractBowlingRender<ExplosionBowlingEntity, BowlingRenderState> {

	private static final float WALL_NUT_OFFSET_Y = 1.5F;
	private static final float WALL_NUT_PIVOT_Y = 1.0F;

	public ExplosionBowlingRender(EntityRendererProvider.Context context) {
		super(context, new WallNutModel(context.bakeLayer(WallNutModel.LAYER_LOCATION_2)));
	}

	@Override
	public BowlingRenderState createRenderState() {
		return new BowlingRenderState();
	}

	@Override
	protected @Nullable Identifier getPAZId(ExplosionBowlingEntity entity) {
		return PVZPlants.EXPLODE_O_NUT.getId();
	}


	@Override
	protected float getRenderOffsetY(ExplosionBowlingEntity entity) {
		return WALL_NUT_OFFSET_Y;
	}

	@Override
	protected float getRenderPivotY(ExplosionBowlingEntity entity) {
		return WALL_NUT_PIVOT_Y;
	}

}