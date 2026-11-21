package com.lying.pvz.client.render.renderer.entity.misc.drop;

import com.lying.pvz.client.model.entity.misc.DropModel;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.drop.JewelEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class JewelRender extends DropRender<JewelEntity, PvZEntityRenderState,
		DropModel<? super PvZEntityRenderState>> {

	private static final Identifier PAZ_ID = StringUtil.id("textures/entity/drop/jewel.png");

	public JewelRender(EntityRendererProvider.Context context) {
		super(context, new DropModel<>(context.bakeLayer(DropModel.LAYER_LOCATION)));
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	protected @Nullable Identifier getTexture(JewelEntity entity) {
		return PAZ_ID;
	}

	@Override
	protected float getScaleByEntity(JewelEntity entity) {
		return 0.32F;
	}


}