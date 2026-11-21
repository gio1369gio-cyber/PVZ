package com.lying.pvz.client.render.layer.component;

import com.lying.pvz.client.model.entity.plant.defence.WallNutModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.Identifier;

public class WallNutArmorLayer extends ComponentLayer<PvZPlantRenderState, WallNutModel> {

    private static final Identifier ARMOR1 = StringUtil.id("textures/entity/plant/defence/wall_nut_armor.png");
    private static final Identifier ARMOR2 = StringUtil.id("textures/entity/plant/defence/wall_nut_armor_1.png");
    private static final Identifier ARMOR3 = StringUtil.id("textures/entity/plant/defence/wall_nut_armor_2.png");

    public WallNutArmorLayer(RenderLayerParent<PvZPlantRenderState, WallNutModel> parent) {
        super(parent, new WallNutModel.WallNutArmorModel(
                Minecraft.getInstance().getEntityModels().bakeLayer(WallNutModel.ARMOR_LAYER_LOCATION)));
    }

    @Override
    public boolean canRender(PvZPlantRenderState state) {
        return state.innerDefenceLife > 0;
    }

    @Override
    public Identifier getRenderTexture(PvZPlantRenderState state) {
        final double percent = state.innerDefenceLife / state.superTime;
        return percent > 2 / 3F ? ARMOR1 : percent > 1 / 3F ? ARMOR2 : ARMOR3;
    }

    /** 叠加在本体之上；如需被其他层盖住可调小 */
    @Override
    protected int getOrder() {
        return 1;
    }
}