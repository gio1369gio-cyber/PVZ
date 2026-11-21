package com.lying.pvz.client.render.renderer.entity.zombie.zombotany;

import com.lying.pvz.client.model.entity.zombie.zombotany.WallNutZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.zombotany.WallNutZombieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * WallNutZombie 渲染器。
 *
 * <p><b>注意</b>：{@link WallNutZombieModel}（{@code client.model.entity.zombie.zombotany} 包）
 * 与植物侧 {@code WallNutModel}（{@code client.model.entity.plant.defence} 包）是<b>不同的类</b>，
 * 不要混淆。本类只引用 zombie 侧模型。
 */
public class WallNutZombieRender
		extends AbstractZombotanyRender<WallNutZombieEntity, WallNutZombieModel> {

	public WallNutZombieRender(EntityRendererProvider.Context ctx) {
		super(ctx, new WallNutZombieModel(ctx.bakeLayer(WallNutZombieModel.LAYER_LOCATION)), 0.4F);
	}

	@Override
	public void extractRenderState(WallNutZombieEntity entity, PvZZombieRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.percentHealth = entity.getHealth()/entity.getMaxHealth();
	}
}