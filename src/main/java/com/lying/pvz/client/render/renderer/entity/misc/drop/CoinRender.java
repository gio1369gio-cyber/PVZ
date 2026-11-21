package com.lying.pvz.client.render.renderer.entity.misc.drop;

import com.lying.pvz.client.model.entity.misc.DropModel;
import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.level.entity.drop.CoinEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class CoinRender extends DropRender<CoinEntity, PvZEntityRenderState,
		DropModel<? super PvZEntityRenderState>> {

	private static final Identifier COPPER = StringUtil.id("textures/entity/drop/copper_coin.png");
	private static final Identifier SILVER = StringUtil.id("textures/entity/drop/silver_coin.png");
	private static final Identifier GOLD   = StringUtil.id("textures/entity/drop/gold_coin.png");

	public CoinRender(EntityRendererProvider.Context context) {
		super(context, new DropModel<>(context.bakeLayer(DropModel.LAYER_LOCATION)));
	}

	@Override
	public PvZEntityRenderState createRenderState() {
		return new PvZEntityRenderState();
	}

	@Override
	protected Identifier getTexture(CoinEntity entity) {
		int amount = entity.getAmount();
		if (amount < 10) return COPPER;
		if (amount < 100) return SILVER;
		return GOLD;
	}

	@Override
	protected float getScaleByEntity(CoinEntity entity) {
		int amount = entity.getAmount();
		if (amount < 10) return 0.15F;
		if (amount < 100) return 0.18F;
		return 0.23F;
	}

}