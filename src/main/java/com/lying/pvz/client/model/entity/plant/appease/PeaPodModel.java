package com.lying.pvz.client.model.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

// PeaPodModel —— 豌豆荚植物
// 形状：主茎荚体（pod）+ 左右分出的两条荚枝（podright/podleft，各挂一个豌豆头 head2/head3）
//        + 主茎上沿高度排列的三个豌豆头（head5/head4/head，连同左右两个共 5 个，按实体 Count 显示 1~5 个）
//        + 底部向下四片呈十字展开的叶片（down 下的 n_r1/w_r1/e_r1/s_r1）
public class PeaPodModel extends PvZPlantModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("pea_pod"), "main");

    private final ModelPart total;
    private final ModelPart pod;
    private final ModelPart pod_r1;
    private final ModelPart podright;
    private final ModelPart podright_r1;
    private final ModelPart head2;
    private final ModelPart podleft;
    private final ModelPart podleft_r1;
    private final ModelPart head3;
    private final ModelPart head;
    private final ModelPart head4;
    private final ModelPart head5;
    private final ModelPart down;
    private final ModelPart n_r1;
    private final ModelPart w_r1;
    private final ModelPart e_r1;
    private final ModelPart s_r1;
    private final ModelPart bone;
    private final ModelPart getPlantWholeBody;

    public PeaPodModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.pod = this.total.getChild("pod");
        this.pod_r1 = this.pod.getChild("pod_r1");
        this.podright = this.pod.getChild("podright");
        this.podright_r1 = this.podright.getChild("podright_r1");
        this.head2 = this.podright.getChild("head2");
        this.podleft = this.pod.getChild("podleft");
        this.podleft_r1 = this.podleft.getChild("podleft_r1");
        this.head3 = this.podleft.getChild("head3");
        this.head = this.pod.getChild("head");
        this.head4 = this.pod.getChild("head4");
        this.head5 = this.pod.getChild("head5");
        this.down = this.total.getChild("down");
        this.n_r1 = this.down.getChild("n_r1");
        this.w_r1 = this.down.getChild("w_r1");
        this.e_r1 = this.down.getChild("e_r1");
        this.s_r1 = this.down.getChild("s_r1");
        this.bone = this.down.getChild("bone");
        // 原代码中该字段是未挂载的空 ModelRenderer，1.21.1 无等价；字段本身从未被读取，用 root 占位
        this.getPlantWholeBody = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition pod = total.addOrReplaceChild("pod",
                CubeListBuilder.create()
                        .texOffs(26, 43).addBox(-5.0F, -16.5F, -2.0F, 1.0F, 14.0F, 5.0F, CubeDeformation.NONE)
                        .texOffs(2, 64).addBox(-4.0F, -16.5F, 2.0F, 8.0F, 14.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(2, 79).addBox(-4.0F, -2.95F, -1.0F, 8.0F, 1.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(40, 45).addBox(4.0F, -16.5F, -2.0F, 1.0F, 14.0F, 5.0F, CubeDeformation.NONE)
                        .texOffs(52, 28).addBox(-3.5F, -26.0F, -2.5F, 7.0F, 1.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(74, 2).addBox(-0.05F, -31.0F, -3.5F, 0.05F, 5.0F, 4.0F, CubeDeformation.NONE)
                        .texOffs(35, 13).addBox(-5.0F, -3.0F, -2.5F, 10.0F, 1.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(58, 19).addBox(3.5F, -25.0F, -2.55F, 1.0F, 9.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(58, 19).addBox(-4.5F, -25.0F, -2.55F, 1.0F, 9.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(58, 19).addBox(-5.5F, -17.0F, -2.55F, 1.0F, 15.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(58, 19).addBox(4.5F, -17.0F, -2.55F, 1.0F, 15.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(10, 43).addBox(-3.0F, -25.5F, -1.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE)
                        .texOffs(2, 20).addBox(-4.0F, -15.5F, 3.05F, 8.0F, 12.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(7, 50).addBox(3.0F, -24.9F, -2.0F, 1.0F, 8.4F, 5.0F, CubeDeformation.NONE)
                        .texOffs(46, 14).addBox(-4.0F, -24.9F, -2.0F, 1.0F, 8.4F, 5.0F, CubeDeformation.NONE)
                        .texOffs(2, 21).addBox(-3.0F, -23.5F, 3.0F, 6.0F, 6.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(2, 23).addBox(-3.0F, -14.0F, 4.0F, 6.0F, 9.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 1.0F, 0.0F));

        pod.addOrReplaceChild("pod_r1",
                CubeListBuilder.create()
                        .texOffs(48, 64).addBox(-3.0F, -6.5F, -0.5F, 6.0F, 9.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -18.0F, 2.5F, 0.0F, 3.1416F, 0.0F));

        PartDefinition podright = pod.addOrReplaceChild("podright",
                CubeListBuilder.create()
                        .texOffs(37, 73).addBox(-8.05F, -8.0F, -2.0F, 1.0F, 9.0F, 5.0F, CubeDeformation.NONE)
                        .texOffs(4, 84).addBox(-7.05F, -8.0F, 2.0F, 6.0F, 9.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(49, 73).addBox(-1.05F, -8.0F, -2.0F, 1.0F, 9.0F, 5.0F, CubeDeformation.NONE)
                        .texOffs(11, 44).addBox(-7.0F, -8.5F, -1.0F, 6.0F, 1.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(2, 22).addBox(-7.0F, -7.0F, 3.0F, 6.0F, 7.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(52, 28).addBox(-7.5F, -9.0F, -2.5F, 7.0F, 1.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(21, 15).addBox(-7.5F, 0.5F, -2.5F, 7.0F, 1.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(58, 20).addBox(-8.5F, -8.5F, -2.55F, 1.0F, 10.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(58, 21).addBox(-0.5F, -8.5F, -2.55F, 1.0F, 10.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(2, 79).addBox(-8.0F, 0.55F, -1.0F, 8.0F, 1.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-5.0F, -3.5F, 0.0F, 0.0F, 0.1309F, -0.0873F));

        podright.addOrReplaceChild("podright_r1",
                CubeListBuilder.create()
                        .texOffs(74, 2).addBox(-0.05F, 0.0F, -1.0F, 0.05F, 5.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-4.0F, -13.5F, -2.5F, 0.0398F, -0.1685F, 0.0456F));

        podright.addOrReplaceChild("head2",
                CubeListBuilder.create()
                        .texOffs(84, 33).addBox(-11.5F, -8.0F, -4.0F, 7.0F, 7.0F, 7.0F, CubeDeformation.NONE)
                        .texOffs(98, 38).addBox(-8.75F, -3.75F, -5.0F, 1.5F, 1.5F, 1.0F, CubeDeformation.NONE)
                        .texOffs(120, 31).addBox(-9.5F, -4.5F, -6.0F, 3.0F, 3.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(4.0F, 1.5F, 0.0F));

        PartDefinition podleft = pod.addOrReplaceChild("podleft",
                CubeListBuilder.create()
                        .texOffs(2, 79).addBox(0.0F, 0.55F, -1.0F, 8.0F, 1.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(2, 46).addBox(0.5F, -8.5F, -0.5F, 7.0F, 1.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(2, 22).addBox(1.0F, -7.0F, 3.0F, 6.0F, 7.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(22, 15).addBox(0.5F, 0.5F, -2.5F, 7.0F, 1.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(58, 21).addBox(7.5F, -8.5F, -2.55F, 1.0F, 10.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(52, 28).addBox(0.5F, -9.5F, -2.5F, 7.0F, 1.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(58, 20).addBox(-0.5F, -8.5F, -2.55F, 1.0F, 10.0F, 2.0F, CubeDeformation.NONE)
                        .texOffs(37, 73).addBox(-0.05F, -7.5F, -2.0F, 1.0F, 8.5F, 5.0F, CubeDeformation.NONE)
                        .texOffs(4, 84).addBox(0.95F, -7.5F, 2.0F, 6.0F, 8.5F, 1.0F, CubeDeformation.NONE)
                        .texOffs(49, 73).addBox(6.95F, -7.5F, -2.0F, 1.0F, 8.5F, 5.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(5.0F, -3.5F, 0.0F, 0.0F, -0.1745F, 0.0873F));

        podleft.addOrReplaceChild("podleft_r1",
                CubeListBuilder.create()
                        .texOffs(74, 2).addBox(-0.05F, -1.0F, -1.0F, 0.05F, 5.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(4.0F, -13.5F, -2.5F, -0.0015F, 0.002F, -0.0208F));

        podleft.addOrReplaceChild("head3",
                CubeListBuilder.create()
                        .texOffs(84, 52).addBox(6.5F, -8.0F, -4.0F, 7.0F, 7.0F, 7.0F, CubeDeformation.NONE)
                        .texOffs(98, 57).addBox(9.25F, -3.75F, -5.0F, 1.5F, 1.5F, 1.0F, CubeDeformation.NONE)
                        .texOffs(120, 50).addBox(8.5F, -4.5F, -6.0F, 3.0F, 3.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(-6.0F, 1.5F, 0.0F));

        pod.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(84, 68).addBox(-2.5F, -8.0F, -5.0F, 7.0F, 7.0F, 7.0F, CubeDeformation.NONE)
                        .texOffs(98, 73).addBox(0.25F, -3.75F, -6.0F, 1.5F, 1.5F, 1.0F, CubeDeformation.NONE)
                        .texOffs(120, 66).addBox(-0.5F, -4.5F, -7.0F, 3.0F, 3.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(-1.0F, -2.0F, 0.0F));

        pod.addOrReplaceChild("head5",
                CubeListBuilder.create()
                        .texOffs(84, 114).addBox(-2.5F, -23.0F, -4.0F, 7.0F, 7.0F, 7.0F, CubeDeformation.NONE)
                        .texOffs(98, 119).addBox(0.25F, -18.75F, -5.0F, 1.5F, 1.5F, 1.0F, CubeDeformation.NONE)
                        .texOffs(120, 112).addBox(-0.5F, -19.5F, -6.0F, 3.0F, 3.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(-1.0F, -2.0F, 0.0F));

        pod.addOrReplaceChild("head4",
                CubeListBuilder.create()
                        .texOffs(84, 89).addBox(-2.5F, -15.5F, -4.0F, 7.0F, 7.0F, 7.0F, CubeDeformation.NONE)
                        .texOffs(98, 94).addBox(0.25F, -11.25F, -5.0F, 1.5F, 1.5F, 1.0F, CubeDeformation.NONE)
                        .texOffs(120, 87).addBox(-0.5F, -12.0F, -6.0F, 3.0F, 3.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(-1.0F, -2.0F, 0.0F));

        PartDefinition down = total.addOrReplaceChild("down",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        down.addOrReplaceChild("n_r1",
                CubeListBuilder.create()
                        .texOffs(2, 18).addBox(-2.0F, -2.3F, -8.0F, 4.0F, 1.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.0873F, -0.7854F, 0.0F));

        down.addOrReplaceChild("w_r1",
                CubeListBuilder.create()
                        .texOffs(6, 27).addBox(0.0F, -2.3F, -2.0F, 8.0F, 1.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, -0.0437F, -0.7854F, 0.1309F));

        down.addOrReplaceChild("e_r1",
                CubeListBuilder.create()
                        .texOffs(26, 27).addBox(-8.0F, -2.3F, -2.0F, 8.0F, 1.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.1309F, -0.7854F, -0.0873F));

        down.addOrReplaceChild("s_r1",
                CubeListBuilder.create()
                        .texOffs(22, 18).addBox(-2.0F, -2.3F, 0.0F, 4.0F, 1.0F, 8.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, -0.0437F, -0.7854F, -0.0873F));

        down.addOrReplaceChild("bone",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(PvZPlantRenderState state) {
        super.setupAnim(state);
        this.head.visible = state.count  >= 1;
        this.head2.visible = state.count >= 2;
        this.head3.visible = state.count >= 3;
        this.head4.visible = state.count >= 4;
        this.head5.visible = state.count >= 5;
    }


    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }
}