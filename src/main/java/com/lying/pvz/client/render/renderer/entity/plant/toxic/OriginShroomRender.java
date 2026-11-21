package com.lying.pvz.client.render.renderer.entity.plant.toxic;

import com.lying.pvz.client.model.entity.plant.toxic.OriginShroomModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.toxic.OriginShroomEntity;
import com.lying.pvz.utils.AnimationUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class OriginShroomRender extends PvZPlantRender<OriginShroomEntity, OriginShroomModel> {

    public OriginShroomRender(EntityRendererProvider.Context context) {
        super(context, new OriginShroomModel(context.bakeLayer(OriginShroomModel.LAYER_LOCATION)), 0.75f);
    }


    @Override
    protected float getScaleByEntity(PvZPlantRenderState state) {
        float size = super.getScaleByEntity(state);
        int tick;
        if (state.isPlantInSuperMode) {
            tick = state.superTime % 10;
            return size + AnimationUtil.upDown((float) tick, 10.0F, 0.05F);
        } else {
            tick = state.shootTick + 10 - state.shootCD;
            return tick >= 0 ? size + AnimationUtil.upDown((float) tick, 10.0F, 0.05F) : size;
        }
    }

}