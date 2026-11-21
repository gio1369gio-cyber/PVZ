package com.lying.pvz.client.render.renderer.entity.plant.assist;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.entity.ability.MetalTypes;
import com.lying.pvz.client.model.entity.plant.assist.MagnetShroomModel;
import com.lying.pvz.client.render.layer.MetalItemLayer;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.assist.MagnetShroomEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * MagnetShroom 渲染器。
 *
 */
public class MagnetShroomRender extends PvZPlantRender<MagnetShroomEntity, MagnetShroomModel> {

	public MagnetShroomRender(EntityRendererProvider.Context ctx) {
		super(ctx, new MagnetShroomModel(ctx.bakeLayer(MagnetShroomModel.LAYER_LOCATION),
				RenderTypes::entityTranslucentCull), 0.3f);
	}


	@Override
	public void extractRenderState(MagnetShroomEntity entity, PvZPlantRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isMagnetActive = entity.isMagnetActive();
		state.metalType = entity.getMetalType();
		state.metalItem.clear();
		state.hasMetalItem = false;
		state.metalScale = 1.0F;


		if (entity.getMetalType() != MetalTypes.EMPTY) {
			final ItemStack stack = entity.getMetalRenderItem();
			if (!stack.isEmpty()) {
				state.hasMetalItem = true;
				final int cd = entity.getWorkCD();
				state.metalScale = cd > 0
						? Math.max(0.05F, entity.getAttackTime() * 1.0F / cd)
						: 1.0F;
				try {
					Minecraft.getInstance().getItemModelResolver().updateForLiving(state.metalItem, stack, ItemDisplayContext.FIXED, entity);
				} catch (Exception e) {
					PvZMod.LOGGER.error("updateForLiving failed", e);
				}
			}
		}

	}

	@Override
	protected void addPlantLayers() {
		super.addPlantLayers();
		this.addLayer(new MetalItemLayer<>(this));
	}
}