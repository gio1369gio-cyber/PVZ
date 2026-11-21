package com.lying.pvz.client.model.entity.plant;

import com.lying.pvz.api.client.model.IPlantModel;
import com.lying.pvz.client.model.entity.PvZEntityModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

/**
 * 植物模型基类。
 *
 * <p>固定 {@link PvZPlantRenderState} 作为 render state 类型，不再带泛型参数。
 */
public abstract class PvZPlantModel extends PvZEntityModel<PvZPlantRenderState> implements IPlantModel {

    protected PvZPlantModel(ModelPart root) {
        super(root);
    }

    protected PvZPlantModel(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
    }


    public abstract ModelPart getPlantWholeBody();

    @Override
    public EntityModel<? extends EntityRenderState> getPlantModel() {
        return this;
    }
}
