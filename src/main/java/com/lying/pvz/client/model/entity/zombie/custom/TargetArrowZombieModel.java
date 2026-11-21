package com.lying.pvz.client.model.entity.zombie.custom;


import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;


import java.util.Optional;

// TargetArrowZombieModel —— 靶心箭僵尸
// 形状：标准僵尸骨架 + 右手（target）挂一个大圆靶（护盾件）
//       靶被打掉后隐藏、双手同时变为自由
public class TargetArrowZombieModel extends PvZZombieModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("target_arrow_zombie"), "main");

    private final ModelPart total;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart up;
    private final ModelPart body;
    private final ModelPart left_hand;
    private final ModelPart right_hand;
    private final ModelPart target;
    private final ModelPart head;

    public TargetArrowZombieModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.right_leg = this.total.getChild("right_leg");
        this.left_leg = this.total.getChild("left_leg");
        this.up = this.total.getChild("up");
        this.body = this.up.getChild("body");
        this.left_hand = this.up.getChild("left_hand");
        this.right_hand = this.up.getChild("right_hand");
        this.target = this.right_hand.getChild("target");
        this.head = this.up.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        total.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(44, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(-4.0F, -24.0F, 0.0F));

        total.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(4.0F, -24.0F, 0.0F));

        PartDefinition up = total.addOrReplaceChild("up",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -24.0F, 0.0F));

        up.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        up.addOrReplaceChild("left_hand",
                CubeListBuilder.create()
                        .texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(12.0F, -20.0F, 0.0F, -1.0472F, 0.0F, 0.0F));

        PartDefinition right_hand = up.addOrReplaceChild("right_hand",
                CubeListBuilder.create()
                        .texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-12.0F, -20.0F, 0.0F, -1.0472F, 0.0F, 0.0F));

        right_hand.addOrReplaceChild("target",
                CubeListBuilder.create()
                        .texOffs(192, 184).addBox(-10.0F, -16.0F, 1.0F, 20.0F, 20.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(12.0F, 23.0F, 1.0F, 1.0472F, 0.0F, 0.0F));

        up.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(16, 96).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, -24.0F, 0.0F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public void updateFreeParts(PvZZombieRenderState state) {
        super.updateFreeParts(state);
        final boolean isPartDestroyed = ! state.canPartsExist;
        this.target.visible = !isPartDestroyed;
        this.isLeftHandFree = isPartDestroyed;
        this.isRightHandFree = isPartDestroyed;
    }

    @Override
    public void refreshAnim() {
        this.getZombieLeftHand().xRot = -1.0472f;
        this.getZombieRightHand().xRot = -1.0472f;
    }

    @Override
    public Optional<ModelPart> getHandDefence() {
        return Optional.ofNullable(this.target);
    }

    @Override
    public ModelPart getZombieLeftHand() {
        return this.left_hand;
    }

    @Override
    public ModelPart getZombieRightHand() {
        return this.right_hand;
    }

    @Override
    public ModelPart getZombieLeftLeg() {
        return this.left_leg;
    }

    @Override
    public ModelPart getZombieRightLeg() {
        return this.right_leg;
    }

    @Override
    public ModelPart getZombieHead() {
        return this.head;
    }

    @Override
    public ModelPart getZombieUpBody() {
        return this.up;
    }

    @Override
    public ModelPart getZombieWholeBody() {
        return this.total;
    }
}