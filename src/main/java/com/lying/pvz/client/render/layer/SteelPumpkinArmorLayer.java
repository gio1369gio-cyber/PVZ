package com.lying.pvz.client.render.layer;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.client.IClientPAZInfo;
import com.lying.pvz.client.model.entity.plant.defence.SteelPumpkinModel;
import com.lying.pvz.client.render.layer.component.ComponentLayer;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.impl.plant.PVZPlants;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

/**
 * 钢南瓜外层护甲层。有外层防御生命时渲染钢南瓜装甲外壳。
 */
public class SteelPumpkinArmorLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends ComponentLayer<S, M> {

    private static final Identifier FALLBACK_TEXTURE = StringUtil.id("entity/plant/missing");

    public SteelPumpkinArmorLayer(RenderLayerParent<S, M> parent, ModelPart modelPart) {
        super(parent, new SteelPumpkinModel.SteelPumpkinArmorModel(modelPart));
    }

    @Override
    public boolean canRender(LivingEntityRenderState state) {
        return state instanceof PvZPlantRenderState ps && ps.outerDefenceLife > 0 && ps.steelPumpkin;
    }

    @Override
    public Identifier getRenderTexture(S state) {
        return PVZAPI.client().paz(PVZPlants.STEEL_PUMPKIN.get().getID())
                .map(IClientPAZInfo::getTexture)
                .orElse(FALLBACK_TEXTURE);
    }
}
