package com.lying.pvz.client.render.renderer.entity.misc.drop;

import com.lying.pvz.client.model.entity.misc.DropModel;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.drop.EnergyEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class EnergyRender extends DropRender<EnergyEntity, PvZEntityRenderState,
        DropModel<? super PvZEntityRenderState>> {

	private static final Identifier PAZ_ID = StringUtil.id("textures/entity/drop/energy.png");

	public EnergyRender(EntityRendererProvider.Context context) {
		super(context, new DropModel<>(context.bakeLayer(DropModel.LAYER_LOCATION)));
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	protected @Nullable Identifier getTexture(EnergyEntity entity) {
		return PAZ_ID;
	}


	@Override
	protected float getScaleByEntity(EnergyEntity entity) {
		return 0.5F;
	}

}