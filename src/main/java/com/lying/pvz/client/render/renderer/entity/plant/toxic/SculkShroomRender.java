package com.lying.pvz.client.render.renderer.entity.plant.toxic;

import com.lying.pvz.client.model.entity.plant.toxic.SculkShroomModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.toxic.SculkShroomEntity;
import com.lying.pvz.utils.AnimationUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SculkShroomRender extends PvZPlantRender<SculkShroomEntity, SculkShroomModel> {

    public SculkShroomRender(EntityRendererProvider.Context context) {
        super(context, new SculkShroomModel(context.bakeLayer(SculkShroomModel.LAYER_LOCATION)), 0.75f);
    }


    @Override
    protected float getScaleByEntity(PvZPlantRenderState state) {
        float size = super.getScaleByEntity(state);
        int T;
        int tick;
        if (state.isPlantInSuperMode) {
            T = state.shootCD;
            tick =state.superTime % T;
            return size + AnimationUtil.upDown((float) tick, (float) T, 0.1F);
        } else {
            T = state.shootCD;
            tick =state.shootTick + T - state.shootCD;
            return tick >= 0 ? size + AnimationUtil.upDown((float) tick, (float) T, 0.1F) : size;
        }
    }

}