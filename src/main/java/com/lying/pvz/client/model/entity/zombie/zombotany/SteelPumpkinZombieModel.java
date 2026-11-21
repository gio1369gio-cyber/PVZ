package com.lying.pvz.client.model.entity.zombie.zombotany;


import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;


import java.util.Optional;

// SteelPumpkinZombieModel —— 钢铁南瓜僵尸
// 形状：加长版标准僵尸骨架（双臂前伸），身上挂一把带长梯的锤子（hammer + ladder + floor 五级横档），
//       护盾（ladder）被打掉后隐藏、双手同时解放
public class SteelPumpkinZombieModel extends PvZZombieModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("steel_pumpkin_zombie"), "main");

    private final ModelPart total;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    private final ModelPart up;
    private final ModelPart body;
    private final ModelPart hammer;
    private final ModelPart left_hand;
    private final ModelPart right_hand;
    private final ModelPart head;
    private final ModelPart teeth_r1;
    private final ModelPart ladder;
    private final ModelPart floor;

    public SteelPumpkinZombieModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.right_leg = this.total.getChild("right_leg");
        this.left_leg = this.total.getChild("left_leg");
        this.up = this.total.getChild("up");
        this.body = this.up.getChild("body");
        this.hammer = this.body.getChild("hammer");
        this.left_hand = this.up.getChild("left_hand");
        this.right_hand = this.up.getChild("right_hand");
        this.head = this.up.getChild("head");
        this.teeth_r1 = this.head.getChild("teeth_r1");
        this.ladder = this.up.getChild("ladder");
        this.floor = this.ladder.getChild("floor");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        total.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(44, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(-4.0F, -23.0F, 0.0F));

        total.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(4.0F, -23.0F, 0.0F));

        PartDefinition up = total.addOrReplaceChild("up",
                CubeListBuilder.create(),
                PartPose.ZERO);

        PartDefinition body = up.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 41).addBox(-8.0F, -25.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, -23.0F, 0.0F));

        body.addOrReplaceChild("hammer",
                CubeListBuilder.create()
                        .texOffs(0, 163).addBox(-1.0F, -6.0F, -0.5F, 1.0F, 9.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(0, 175).addBox(-1.0F, 3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                        .texOffs(0, 179).addBox(-1.0F, -5.75F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.4F))
                        .texOffs(6, 163).addBox(-1.0F, -5.75F, -3.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.1F)),
                PartPose.offsetAndRotation(9.0F, -1.0F, 0.5F, 0.4363F, 0.0F, 0.0F));

        up.addOrReplaceChild("left_hand",
                CubeListBuilder.create()
                        .texOffs(96, 60).addBox(0.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(8.0F, -48.0F, 0.0F, -0.8727F, 0.0F, 0.0F));

        up.addOrReplaceChild("right_hand",
                CubeListBuilder.create()
                        .texOffs(96, 0).addBox(-8.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-8.0F, -48.0F, 0.0F, -0.8727F, 0.0F, 0.0F));

        PartDefinition head = up.addOrReplaceChild("head",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -48.0F, 0.0F));

        head.addOrReplaceChild("teeth_r1",
                CubeListBuilder.create()
                        .texOffs(129, 26).addBox(9.0F, -54.5F, -8.5F, 1.0F, 6.0F, 17.0F, new CubeDeformation(1.0F))
                        .texOffs(128, 0).addBox(-7.5F, -58.5F, -8.5F, 17.0F, 10.0F, 17.0F, new CubeDeformation(1.0F))
                        .texOffs(128, 50).addBox(-7.0F, -58.5F, -8.0F, 16.0F, 9.0F, 16.0F, new CubeDeformation(1.0F)),
                PartPose.offsetAndRotation(0.0F, 48.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition ladder = up.addOrReplaceChild("ladder",
                CubeListBuilder.create()
                        .texOffs(15, 202).addBox(-12.0F, -22.0F, -2.0F, 3.0F, 50.0F, 4.0F, new CubeDeformation(0.1F))
                        .texOffs(0, 202).addBox(9.0F, -22.0F, -2.0F, 3.0F, 50.0F, 4.0F, new CubeDeformation(0.1F))
                        .texOffs(0, 194).addBox(8.0F, 28.0F, -3.0F, 5.0F, 1.0F, 6.0F, CubeDeformation.NONE)
                        .texOffs(0, 185).addBox(-13.0F, 28.0F, -3.0F, 5.0F, 1.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -38.0F, -20.0F, 0.2182F, 0.0F, 0.0F));

        ladder.addOrReplaceChild("floor",
                CubeListBuilder.create()
                        .texOffs(31, 250).addBox(-9.0F, 20.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
                        .texOffs(31, 242).addBox(-9.0F, 12.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
                        .texOffs(30, 234).addBox(-9.0F, 4.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
                        .texOffs(32, 225).addBox(-9.0F, -4.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE)
                        .texOffs(30, 217).addBox(-9.0F, -12.0F, -2.0F, 18.0F, 2.0F, 4.0F, CubeDeformation.NONE),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public void updateFreeParts(PvZZombieRenderState state) {
        super.updateFreeParts(state);
        final boolean hasLadder = state.hasMetal;
        this.ladder.visible = hasLadder;
        this.isLeftHandFree = !hasLadder;
        this.isRightHandFree = !hasLadder;
    }

    @Override
    public void refreshAnim() {
        super.refreshAnim();
        this.right_hand.xRot = -0.8727f;
        this.left_hand.xRot = -0.8727f;
    }

    @Override
    public Optional<ModelPart> getHandDefence() {
        return Optional.ofNullable(this.ladder);
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