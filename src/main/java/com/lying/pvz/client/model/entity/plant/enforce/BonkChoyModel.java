package com.lying.pvz.client.model.entity.plant.enforce;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.AnimationUtil;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   1. 几何 1:1 搬运，包括 0.05 与 -0.45 两种膨胀、以及 bone4 独有的 z=-3.501（相对兄弟件 -3.5 差 0.001）。
//   2. 只有 right_arm / left_arm 在 setupAnim 里被驱动，hair / face / body 全程静态，故只抓这两只到字段。
//   3. setupAnim 依赖 AnimationUtil.getUpDown(now, tot, angle)：
//      - 1.16.5 该工具就存在于 com.hungteen.pvz.utils.AnimationUtil；
//      - 1.21.1 只要仍保留同名类/签名，逻辑无需修改，原样调用即可。
//   4. left_arm 局部坐标已经在 y 轴上旋转 π（镜像方向），所以 left 侧方块不是 right 侧的镜像参数，
//      而是"预先镜像过的一整套硬编码数值"——1.16.5 就是如此，原样保留，不要用 .mirror() 取代。

public class BonkChoyModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("bonk_choy"), "main");

	private final ModelPart total;

	private final ModelPart right_arm;
	private final ModelPart left_arm;

	public BonkChoyModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");

		this.right_arm = this.total.getChild("right_arm");
		this.left_arm = this.total.getChild("left_arm");
	}


	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition hair = total.addOrReplaceChild("hair",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -18.25F, -1.0F));

		PartDefinition bone2 = hair.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(82, 110).addBox(-5.0F, -2.5F, -3.5F, 10.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(72, 103).addBox(-6.0F, -7.5F, -3.5F, 12.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -5.0F, 0.0F));

		bone2.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(32, 67).addBox(-4.0F, -0.5F, 0.5F, 8.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.1645F, -3.7418F, 0.4363F, 0.0F, 0.0F));


		hair.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(48, 97).addBox(-4.0F, -15.5F, -3.5F, 8.0F, 3.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(104, 108).addBox(-5.0F, -17.5F, -3.5F, 10.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(100, 101).addBox(-6.0F, -22.5F, -3.5F, 12.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 10.0F, -2.0F, -0.5236F, 0.0F, 0.0F));

		hair.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(72, 96).addBox(-4.0F, -15.5F, -3.5F, 8.0F, 5.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(104, 97).addBox(-5.0F, -17.5F, -3.5F, 10.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(36, 88).addBox(-6.0F, -22.5F, -3.5F, 12.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(10.0F, 7.0F, 0.0F, -0.2182F, 0.0F, -0.8727F));

		hair.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(62, 89).addBox(-4.0F, -15.5F, -3.5F, 8.0F, 5.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(104, 93).addBox(-5.0F, -17.5F, -3.5F, 10.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(36, 81).addBox(-6.0F, -22.5F, -3.5F, 12.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-9.0F, 7.0F, 1.0F, -0.2182F, 0.0F, 0.8727F));

		hair.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(82, 90).addBox(-4.0F, -15.5F, -3.5F, 8.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(104, 89).addBox(-5.0F, -17.5F, -3.5F, 10.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(100, 82).addBox(-6.0F, -22.5F, -3.5F, 12.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(5.0F, 9.0F, 2.0F, 0.0F, 0.0F, -0.4363F));

		hair.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(80, 83).addBox(-4.0F, -15.5F, -3.501F, 8.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(104, 78).addBox(-5.0F, -17.5F, -3.501F, 10.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 76).addBox(-6.0F, -22.5F, -3.501F, 12.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 9.0F, 2.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition right_arm = total.addOrReplaceChild("right_arm",
				CubeListBuilder.create()
						.texOffs(89, 80).addBox(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(-5.0F, -0.75F, 0.0F));

		PartDefinition right1 = right_arm.addOrReplaceChild("right1",
				CubeListBuilder.create()
						.texOffs(63, 78).addBox(-2.8706F, -0.517F, -1.0F, 3.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition right2 = right1.addOrReplaceChild("right2",
				CubeListBuilder.create()
						.texOffs(114, 113).addBox(-3.75F, -0.567F, -0.5F, 4.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-3.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.2618F));


		PartDefinition right3 = right2.addOrReplaceChild("right3",
				CubeListBuilder.create()
						.texOffs(64, 85).addBox(-4.6464F, -0.6464F, -0.5F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));


		PartDefinition right4 = right3.addOrReplaceChild("right4",
				CubeListBuilder.create()
						.texOffs(97, 72).addBox(-5.0F, -1.0F, -1.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.05F)),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.3927F));

		right4.addOrReplaceChild("right5",
				CubeListBuilder.create()
						.texOffs(14, 67).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 4.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition left_arm = total.addOrReplaceChild("left_arm",
				CubeListBuilder.create()
						.texOffs(73, 78).addBox(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(5.0F, -0.75F, 0.0F, 0.0F, 3.1416F, 0.0F));


		PartDefinition left1 = left_arm.addOrReplaceChild("left1",
				CubeListBuilder.create()
						.texOffs(57, 123).addBox(-2.8706F, -0.517F, -1.0F, 3.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition left2 = left1.addOrReplaceChild("left2",
				CubeListBuilder.create()
						.texOffs(38, 74).addBox(-3.75F, -0.567F, -2.0F, 4.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition left3 = left2.addOrReplaceChild("left3",
				CubeListBuilder.create()
						.texOffs(52, 74).addBox(-4.6464F, -0.6464F, -2.0F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));


		PartDefinition left4 = left3.addOrReplaceChild("left4",
				CubeListBuilder.create()
						.texOffs(83, 75).addBox(-5.0F, -1.0F, -1.5F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.05F)),
				PartPose.offsetAndRotation(-3.8232F, -0.1768F, -0.5F, 0.0F, 0.0F, 0.3927F));


		left4.addOrReplaceChild("left5",
				CubeListBuilder.create()
						.texOffs(68, 69).addBox(-4.0F, -2.0F, -2.5F, 4.0F, 4.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		total.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(56, 116).addBox(0.5F, -11.0F, -6.5F, 5.0F, 6.0F, 1.0F, new CubeDeformation(-0.45F))
						.texOffs(0, 88).addBox(-5.5F, -11.0F, -6.5F, 5.0F, 6.0F, 1.0F, new CubeDeformation(-0.45F)),
				PartPose.offset(0.0F, -2.25F, -1.0F));


		total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(96, 117).addBox(1.0F, -19.0F, -4.0F, 8.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(0, 83).addBox(-1.0F, -4.5F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(0.4F))
						.texOffs(0, 100).addBox(-2.0F, -14.5F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(42, 102).addBox(0.0F, -18.0F, -5.0F, 10.0F, 2.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(56, 114).addBox(-1.0F, -16.25F, -6.0F, 12.0F, 2.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(-5.0F, -0.75F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		if (state.attackTime == 0) {
			this.left_arm.xRot = 0;
			this.left_arm.yRot = 3.1416F;
			this.left_arm.zRot = 0;
			this.right_arm.xRot = 0;
			this.right_arm.yRot = 0;
			this.right_arm.zRot = 0;
		} else if (state.attackTime < 0) {
			int now = state.attackTime;
			int tot = state.attackCD;
			this.right_arm.xRot = AnimationUtil.getUpDown(now, tot, 60);
			this.right_arm.yRot = AnimationUtil.getUpDown(now, tot, -60);
		} else {
			int now = -state.attackTime;
			int tot = state.attackCD;
			this.left_arm.xRot = AnimationUtil.getUpDown(now, tot, -60);
			this.left_arm.yRot = -(3.1416F - AnimationUtil.getUpDown(now, tot, 60));
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}