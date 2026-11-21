package com.lying.pvz.client.model.entity.bullet;

import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * 球（Ball）模型。
 *
 * <pre>
 * total（setPos(0,24,0)，1 个方块：球体 4×4×4）
 * </pre>
 *
 */
public class BallModel extends EntityModel<PvZEntityRenderState> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("ball"), "main");

    private final ModelPart total;


    public BallModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("total",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 24.0F, 0.0F));


        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void setupAnim(PvZEntityRenderState state) {
        super.setupAnim(state);
    }

}