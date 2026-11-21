package com.lying.pvz.client.model.entity.plant.arma;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.arma.BurstKernelPultEntity;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// BurstKernelPultModel —— 爆裂玉米投掷器
// 形状：底座一圈六片向外辐射的叶片（leaves 下 leave1~leave6，每片由 3 层骨链 bone/bone3/bone2 叠成弧形叶），
//       叶片中心上方是果头（head，5 层逐层收缩的圆台 + face 上两只眼睛和两道眉），
//       果头上方伸出炮臂（pult），炮臂末端托篮（basket），篮中三选一显示玉米粒（corn/popped/pop_corn）
public class BurstKernelPultModel extends PvZPlantModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("burst_kernel_pult"), "main");

    private final ModelPart total;
    private final ModelPart head;
    private final ModelPart face;
    private final ModelPart cube_r1;
    private final ModelPart cube_r2;
    private final ModelPart leaves;
    private final ModelPart leave1;
    private final ModelPart cube_r3;
    private final ModelPart bone;
    private final ModelPart cube_r4;
    private final ModelPart bone3;
    private final ModelPart cube_r5;
    private final ModelPart bone2;
    private final ModelPart cube_r6;
    private final ModelPart leave2;
    private final ModelPart cube_r7;
    private final ModelPart bone4;
    private final ModelPart cube_r8;
    private final ModelPart bone5;
    private final ModelPart cube_r9;
    private final ModelPart bone6;
    private final ModelPart cube_r10;
    private final ModelPart leave3;
    private final ModelPart cube_r11;
    private final ModelPart bone7;
    private final ModelPart cube_r12;
    private final ModelPart bone8;
    private final ModelPart cube_r13;
    private final ModelPart bone9;
    private final ModelPart cube_r14;
    private final ModelPart leave4;
    private final ModelPart cube_r15;
    private final ModelPart bone10;
    private final ModelPart cube_r16;
    private final ModelPart bone11;
    private final ModelPart cube_r17;
    private final ModelPart bone12;
    private final ModelPart cube_r18;
    private final ModelPart leave5;
    private final ModelPart cube_r19;
    private final ModelPart bone13;
    private final ModelPart cube_r20;
    private final ModelPart bone14;
    private final ModelPart cube_r21;
    private final ModelPart bone15;
    private final ModelPart cube_r22;
    private final ModelPart leave6;
    private final ModelPart cube_r23;
    private final ModelPart bone16;
    private final ModelPart cube_r24;
    private final ModelPart bone17;
    private final ModelPart cube_r25;
    private final ModelPart bone18;
    private final ModelPart cube_r26;
    private final ModelPart pult;
    private final ModelPart cube_r27;
    private final ModelPart cube_r28;
    private final ModelPart cube_r29;
    private final ModelPart basket;
    private final ModelPart popped;
    private final ModelPart corn;
    private final ModelPart pop_corn;
    private final ModelPart getPlantWholeBody;

    public BurstKernelPultModel(ModelPart root) {
        super(root);
        this.total = root.getChild("total");
        this.head = this.total.getChild("head");
        this.face = this.head.getChild("face");
        this.cube_r1 = this.face.getChild("cube_r1");
        this.cube_r2 = this.face.getChild("cube_r2");
        this.leaves = this.total.getChild("leaves");
        this.leave1 = this.leaves.getChild("leave1");
        this.cube_r3 = this.leave1.getChild("cube_r3");
        this.bone = this.leave1.getChild("bone");
        this.cube_r4 = this.bone.getChild("cube_r4");
        this.bone3 = this.bone.getChild("bone3");
        this.cube_r5 = this.bone3.getChild("cube_r5");
        this.bone2 = this.bone3.getChild("bone2");
        this.cube_r6 = this.bone2.getChild("cube_r6");
        this.leave2 = this.leaves.getChild("leave2");
        this.cube_r7 = this.leave2.getChild("cube_r7");
        this.bone4 = this.leave2.getChild("bone4");
        this.cube_r8 = this.bone4.getChild("cube_r8");
        this.bone5 = this.bone4.getChild("bone5");
        this.cube_r9 = this.bone5.getChild("cube_r9");
        this.bone6 = this.bone5.getChild("bone6");
        this.cube_r10 = this.bone6.getChild("cube_r10");
        this.leave3 = this.leaves.getChild("leave3");
        this.cube_r11 = this.leave3.getChild("cube_r11");
        this.bone7 = this.leave3.getChild("bone7");
        this.cube_r12 = this.bone7.getChild("cube_r12");
        this.bone8 = this.bone7.getChild("bone8");
        this.cube_r13 = this.bone8.getChild("cube_r13");
        this.bone9 = this.bone8.getChild("bone9");
        this.cube_r14 = this.bone9.getChild("cube_r14");
        this.leave4 = this.leaves.getChild("leave4");
        this.cube_r15 = this.leave4.getChild("cube_r15");
        this.bone10 = this.leave4.getChild("bone10");
        this.cube_r16 = this.bone10.getChild("cube_r16");
        this.bone11 = this.bone10.getChild("bone11");
        this.cube_r17 = this.bone11.getChild("cube_r17");
        this.bone12 = this.bone11.getChild("bone12");
        this.cube_r18 = this.bone12.getChild("cube_r18");
        this.leave5 = this.leaves.getChild("leave5");
        this.cube_r19 = this.leave5.getChild("cube_r19");
        this.bone13 = this.leave5.getChild("bone13");
        this.cube_r20 = this.bone13.getChild("cube_r20");
        this.bone14 = this.bone13.getChild("bone14");
        this.cube_r21 = this.bone14.getChild("cube_r21");
        this.bone15 = this.bone14.getChild("bone15");
        this.cube_r22 = this.bone15.getChild("cube_r22");
        this.leave6 = this.leaves.getChild("leave6");
        this.cube_r23 = this.leave6.getChild("cube_r23");
        this.bone16 = this.leave6.getChild("bone16");
        this.cube_r24 = this.bone16.getChild("cube_r24");
        this.bone17 = this.bone16.getChild("bone17");
        this.cube_r25 = this.bone17.getChild("cube_r25");
        this.bone18 = this.bone17.getChild("bone18");
        this.cube_r26 = this.bone18.getChild("cube_r26");
        this.pult = this.total.getChild("pult");
        this.cube_r27 = this.pult.getChild("cube_r27");
        this.cube_r28 = this.pult.getChild("cube_r28");
        this.cube_r29 = this.pult.getChild("cube_r29");
        this.basket = this.pult.getChild("basket");
        this.popped = this.basket.getChild("popped");
        this.corn = this.basket.getChild("corn");
        this.pop_corn = this.basket.getChild("pop_corn");
        // 原代码中该字段是未挂载的空 ModelRenderer，从未被读取；1.21.1 无等价，用 root 占位
        this.getPlantWholeBody = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition total = root.addOrReplaceChild("total",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        // ===== head + face =====
        PartDefinition head = total.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 111).addBox(-6.0F, -4.5F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(-0.4F))
                        .texOffs(48, 111).addBox(-6.0F, -8.25F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(-0.8F))
                        .texOffs(0, 95).addBox(-6.0F, -11.25F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(-1.2F))
                        .texOffs(48, 98).addBox(-5.0F, -12.25F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(-0.7F))
                        .texOffs(88, 109).addBox(-5.0F, -13.75F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(-1.3F))
                        .texOffs(88, 102).addBox(-3.0F, -13.25F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(-0.1F))
                        .texOffs(44, 117).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(-0.4F)),
                PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition face = head.addOrReplaceChild("face",
                CubeListBuilder.create()
                        .texOffs(2, 84).addBox(-3.0F, -7.75F, -5.65F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F))
                        .texOffs(1, 89).addBox(0.5F, -7.75F, -5.65F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        face.addOrReplaceChild("cube_r1",
                CubeListBuilder.create()
                        .texOffs(98, 123).addBox(-3.0F, -0.7649F, 0.25F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
                PartPose.offset(3.3279F, -8.2351F, -5.5F));

        face.addOrReplaceChild("cube_r2",
                CubeListBuilder.create()
                        .texOffs(98, 126).addBox(-3.0F, -1.0F, 0.25F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
                PartPose.offset(-0.75F, -8.0F, -5.5F));

        // ===== leaves + 6 片叶（结构完全一致，仅 leave 位置/朝向/贴图偏移不同）=====
        PartDefinition leaves = total.addOrReplaceChild("leaves",
                CubeListBuilder.create()
                        .texOffs(0, 82).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // 数组顺序与源码 leave1~leave6 对齐；每片的三层骨链字段名顺序保持源码
        String[] leaveNames    = {"leave1", "leave2", "leave3", "leave4", "leave5", "leave6"};
        float[][] leavePose    = {{-5F,0F,-3F,-0.5236F},{0F,0F,-5F,-1.5708F},{5F,0F,-3F,-2.618F},{-5F,0F,3F,0.7854F},{0F,0F,5F,1.5708F},{5F,0F,3F,2.3562F}};
        String[] cubeRNames    = {"cube_r3","cube_r7","cube_r11","cube_r15","cube_r19","cube_r23"};
        int[][]  cubeRUV       = {{36,100},{108,100},{64,91},{36,86},{108,88},{64,84}};
        String[][] boneNames   = {{"bone","bone3","bone2"},{"bone4","bone5","bone6"},{"bone7","bone8","bone9"},{"bone10","bone11","bone12"},{"bone13","bone14","bone15"},{"bone16","bone17","bone18"}};
        String[] cubeR4Names   = {"cube_r4","cube_r8","cube_r12","cube_r16","cube_r20","cube_r24"};
        int[][]  cubeR4UV      = {{43,111},{97,96},{85,92},{50,86},{98,85},{112,82}};
        String[] cubeR5Names   = {"cube_r5","cube_r9","cube_r13","cube_r17","cube_r21","cube_r25"};
        int[][]  cubeR5UV      = {{79,102},{49,93},{114,95},{96,91},{84,87},{79,82}};
        String[] cubeR6Names   = {"cube_r6","cube_r10","cube_r14","cube_r18","cube_r22","cube_r26"};
        int[][]  cubeR6UV      = {{37,112},{79,99},{108,96},{118,125},{108,125},{93,83}};

        for (int i = 0; i < 6; i++) {
            float[] lp = leavePose[i];
            PartDefinition leave = leaves.addOrReplaceChild(leaveNames[i],
                    CubeListBuilder.create(),
                    PartPose.offsetAndRotation(lp[0], lp[1], lp[2], 0.0F, lp[3], 0.0F));

            leave.addOrReplaceChild(cubeRNames[i],
                    CubeListBuilder.create()
                            .texOffs(cubeRUV[i][0], cubeRUV[i][1])
                            .addBox(-3.0F, -1.0F, -3.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(-0.4F)),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

            PartDefinition boneA = leave.addOrReplaceChild(boneNames[i][0],
                    CubeListBuilder.create(),
                    PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

            boneA.addOrReplaceChild(cubeR4Names[i],
                    CubeListBuilder.create()
                            .texOffs(cubeR4UV[i][0], cubeR4UV[i][1])
                            .addBox(-3.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(-0.4F)),
                    PartPose.offsetAndRotation(0.4377F, -0.6026F, -3.5F, 0.0F, 0.0F, 0.2618F));

            PartDefinition boneB = boneA.addOrReplaceChild(boneNames[i][1],
                    CubeListBuilder.create(),
                    PartPose.offsetAndRotation(-1.9981F, -0.0872F, 0.0F, 0.0F, 0.0F, -0.3491F));

            boneB.addOrReplaceChild(cubeR5Names[i],
                    CubeListBuilder.create()
                            .texOffs(cubeR5UV[i][0], cubeR5UV[i][1])
                            .addBox(-4.9981F, -1.0872F, 2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(-0.4F)),
                    PartPose.offsetAndRotation(2.9218F, -0.5407F, -4.0F, 0.0F, 0.0F, 0.1745F));

            PartDefinition boneC = boneB.addOrReplaceChild(boneNames[i][2],
                    CubeListBuilder.create(),
                    PartPose.offsetAndRotation(-3.0019F, 0.0872F, 0.0F, 0.0F, 0.0F, 0.3927F));

            boneC.addOrReplaceChild(cubeR6Names[i],
                    CubeListBuilder.create()
                            .texOffs(cubeR6UV[i][0], cubeR6UV[i][1])
                            .addBox(-2.9676F, -0.3365F, 0.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
                    PartPose.offsetAndRotation(1.0857F, -2.3915F, -1.5F, 0.0F, 0.0F, 0.48F));
        }

        // ===== pult =====
        PartDefinition pult = total.addOrReplaceChild("pult",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -13.0F, 0.0F));

        pult.addOrReplaceChild("cube_r27",
                CubeListBuilder.create()
                        .texOffs(120, 108).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(-0.4F)),
                PartPose.offsetAndRotation(0.0F, -1.6192F, 9.0169F, -1.6144F, 0.0F, 0.0F));

        pult.addOrReplaceChild("cube_r28",
                CubeListBuilder.create()
                        .texOffs(0, 112).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(-0.4F)),
                PartPose.offsetAndRotation(0.0F, -3.8077F, 1.2387F, -1.8326F, 0.0F, 0.0F));

        pult.addOrReplaceChild("cube_r29",
                CubeListBuilder.create()
                        .texOffs(90, 111).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(-0.4F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

        // ===== basket + 三选一玉米 =====
        PartDefinition basket = pult.addOrReplaceChild("basket",
                CubeListBuilder.create()
                        .texOffs(37, 79).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE)
                        .texOffs(55, 81).addBox(-3.0F, -4.0F, 6.0F, 6.0F, 3.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(103, 81).addBox(-3.0F, -4.0F, -1.0F, 6.0F, 3.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(28, 73).addBox(3.0F, -4.0F, 0.0F, 1.0F, 3.0F, 6.0F, CubeDeformation.NONE)
                        .texOffs(14, 73).addBox(-4.0F, -4.0F, 0.0F, 1.0F, 3.0F, 6.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.5F, 18.0F));

        basket.addOrReplaceChild("popped",
                CubeListBuilder.create()
                        .texOffs(110, 67).addBox(-1.5F, -10.0F, 2.0F, 3.0F, 10.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(110, 76).addBox(-1.1F, -10.3F, 2.3F, 2.3F, 0.3F, 2.3F, CubeDeformation.NONE),
                PartPose.offset(0.0F, -1.0F, 0.0F));

        basket.addOrReplaceChild("corn",
                CubeListBuilder.create()
                        .texOffs(90, 69).addBox(-1.5F, -10.0F, 2.0F, 3.0F, 10.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(91, 78).addBox(-1.1F, -10.3F, 2.3F, 2.3F, 0.3F, 2.3F, CubeDeformation.NONE),
                PartPose.offset(0.0F, -1.0F, 0.0F));

        basket.addOrReplaceChild("pop_corn",
                CubeListBuilder.create()
                        .texOffs(71, 79).addBox(-1.0F, -3.5F, 2.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(PvZPlantRenderState state) {
        super.setupAnim(state);
        if (state.attackTime > 0) {
            float percent = 1.0F - (float) state.attackTime / (float) state.pultAnimTime;
            this.pult.xRot = (1.0F - Mth.abs(Mth.cos(percent * 3.14159F))) * 1.5F;
            this.corn.visible = (double) percent < 0.5 &&   state.currentBullet1  == BurstKernelPultEntity.CornTypes.CORN;
            this.popped.visible = (double) percent < 0.5 && state.currentBullet1 == BurstKernelPultEntity.CornTypes.BURST_CORN;
            this.pop_corn.visible = (double) percent < 0.5 && state.currentBullet1 == BurstKernelPultEntity.CornTypes.POP_CORN;
        } else {
            this.pult.xRot = Mth.sin(state.ageInTicks / 10.0F) / 8.0F;
            this.pop_corn.visible = state.entityType == PvZEntityRegister.BURST_KERNEL_PULT.get();
        }
    }

    @Override
    public ModelPart getPlantWholeBody() {
        return this.total;
    }

}