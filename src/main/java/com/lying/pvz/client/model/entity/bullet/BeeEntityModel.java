package com.lying.pvz.client.model.entity.bullet;

import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import net.minecraft.util.Mth;

public class BeeEntityModel extends EntityModel<PvZEntityRenderState> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("bee"), "main");

    private final ModelPart bone;
    private final ModelPart body;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    private final ModelPart stinger;
    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;

    public BeeEntityModel(ModelPart root) {
        super(root);
        this.bone = root.getChild("bone");
        this.body = this.bone.getChild("body");
        this.rightWing = this.bone.getChild("rightWing");
        this.leftWing = this.bone.getChild("leftWing");
        this.stinger = this.body.getChild("stinger");
        this.leftAntenna = this.body.getChild("leftAntenna");
        this.rightAntenna = this.body.getChild("rightAntenna");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition bone = root.addOrReplaceChild("bone",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 19.0F, 0.0F));

        PartDefinition body = bone.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        body.addOrReplaceChild("stinger",
                CubeListBuilder.create()
                        .texOffs(26, 7).addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        body.addOrReplaceChild("leftAntenna",
                CubeListBuilder.create()
                        .texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, -2.0F, -5.0F));

        body.addOrReplaceChild("rightAntenna",
                CubeListBuilder.create()
                        .texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, -2.0F, -5.0F));

        bone.addOrReplaceChild("rightWing",
                CubeListBuilder.create()
                        .texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(-1.5F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.0F));

        bone.addOrReplaceChild("leftWing",
                CubeListBuilder.create().mirror(true)
                        .texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(1.5F, -4.0F, -3.0F, 0.0F, 0.2618F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(PvZEntityRenderState state) {
        this.rightWing.xRot = 0.0F;
        this.leftAntenna.xRot = 0.0F;
        this.rightAntenna.xRot = 0.0F;
        this.bone.xRot = 0.0F;
        this.bone.y = 19.0F;
        boolean bl = state.onGround  &&  state.deltaMovement.lengthSqr() < 1.0E-7;
        float l;
        if (bl) {
            this.rightWing.yRot = -0.2618F;
            this.rightWing.zRot = 0.0F;
            this.leftWing.xRot = 0.0F;
            this.leftWing.yRot = 0.2618F;
            this.leftWing.zRot = 0.0F;
        } else {
            l = state.ageInTicks * 2.1F;
            this.rightWing.yRot = 0.0F;
            this.rightWing.zRot = Mth.cos(l) * 3.1415927F * 0.15F;
            this.leftWing.xRot = this.rightWing.xRot;
            this.leftWing.yRot = this.rightWing.yRot;
            this.leftWing.zRot = -this.rightWing.zRot;
            this.bone.xRot = 0.0F;
            this.bone.yRot = 0.0F;
            this.bone.zRot = 0.0F;
        }
    }

}