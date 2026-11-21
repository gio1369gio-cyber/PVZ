package com.lying.pvz.client.model.entity.zombie.roof;



import com.lying.pvz.api.client.model.IZombieModel;
import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.base.EdgarRobotEntity;
import com.lying.pvz.utils.AnimationUtil;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;


// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。埃德加机器人模型（独立 EntityModel + IZombieModel）：
//     total（根，setPos(0, 24, 0)）
//       ├─ left_leg（setPos(6,-41,0)，2 个方块：大腿 + 侧面装饰）
//       │    └─ left_shoo（setPos(-6,41,0)，1 个 12×8×16 大脚方块）
//       ├─ right_leg（setPos(-7,-41,0)，2 个方块：大腿 + 侧面装饰）
//       │    └─ right_shoo（setPos(7,41,0)，1 个 12×8×16 大脚方块）
//       └─ up（setPos(0,-41,0)，无方块）
//            ├─ body（setPos(0,0,0)，1 个 22×40×12 躯干方块）
//            │    ├─ pipe1（setPos(9,-39,6) + X -35°，2 个方块：右管道弯折）
//            │    └─ pipe2（setPos(9,-39,6) + X -35°，2 个方块：左管道弯折）
//            ├─ head（setPos(0,-40,-3)，7 个方块：头 + 4 个耳朵/天线装饰）
//            │    ├─ mouse（setPos(0,6,-1)，22 个方块：嘴部栅格 + 上下牙 + 螺丝孔）
//            │    ├─ lace（setPos(0,0,2) + X 65°，4 个方块：脸部边框）
//            │    ├─ red_eyes（setPos(-2,-1,-17)，2 个方块：红色眼睛，FLAME 状态可见）
//            │    ├─ yellow_eyes（setPos(-2,-1,-17)，2 个方块：黄色眼睛，其他状态可见）
//            │    └─ blue_eyes（setPos(-2,-1,-17)，2 个方块：蓝色眼睛，ICE 状态可见）
//            ├─ left_arm（setPos(15,-35,2)，1 个 10×48×10 手臂方块）
//            └─ right_arm（setPos(-16,-35,2)，1 个 10×48×10 手臂方块）
//   setupAnim：三组眼睛按 RobotState 互斥切换（FLAME→red、ICE→blue、其他→yellow），
//             并按状态驱动 mouse.xRot / right_arm.xRot / left_arm.xRot
//   tickPartAnim：空实现
//   renderBody：setAllInvis() 后按 BodyType 单独渲染对应部件
//   注意：原代码 RIGHT_HAND 分支渲染的是 this.right_leg 而不是 this.right_arm，
//         这是原代码的 bug，迁移时原样保留 + 注释，不做"修正"。

public class EdgarRobotModel extends EntityModel<PvZZombieRenderState> implements IZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("edgar_robot"), "main");

	private final ModelPart total;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart head;

	private final ModelPart mouse;
	private final ModelPart red_eyes;
	private final ModelPart yellow_eyes;
	private final ModelPart blue_eyes;
	private final ModelPart left_arm;
	private final ModelPart right_arm;

	public EdgarRobotModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.right_leg = this.total.getChild("right_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.head = this.up.getChild("head");
		this.mouse = this.head.getChild("mouse");
		this.red_eyes = this.head.getChild("red_eyes");
		this.yellow_eyes = this.head.getChild("yellow_eyes");
		this.blue_eyes = this.head.getChild("blue_eyes");
		this.left_arm = this.up.getChild("left_arm");
		this.right_arm = this.up.getChild("right_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition left_leg = total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(155, 211).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 33.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(202, 220).addBox(-5.0F, 12.0F, -4.0F, 10.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(6.0F, -41.0F, 0.0F));

		left_leg.addOrReplaceChild("left_shoo",
				CubeListBuilder.create()
						.texOffs(198, 230).addBox(0.0F, -8.0F, -8.0F, 12.0F, 8.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(-6.0F, 41.0F, 0.0F));


		PartDefinition right_leg = total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(154, 164).addBox(-4.0F, 0.0F, -3.0F, 10.0F, 33.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(230, 220).addBox(-4.0F, 12.0F, -4.0F, 10.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-7.0F, -41.0F, 0.0F));


		right_leg.addOrReplaceChild("right_shoo",
				CubeListBuilder.create()
						.texOffs(198, 187).addBox(-12.0F, -8.0F, -8.0F, 12.0F, 8.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(7.0F, 41.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -41.0F, 0.0F));


		PartDefinition body = up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(186, 2).addBox(-11.0F, -40.0F, -4.0F, 22.0F, 40.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		body.addOrReplaceChild("pipe1",
				CubeListBuilder.create()
						.texOffs(200, 170).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(245, 166).addBox(-2.0F, -2.0F, 10.0F, 2.0F, 16.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(9.0F, -39.0F, 6.0F, -0.6109F, 0.0F, 0.0F));


		body.addOrReplaceChild("pipe2",
				CubeListBuilder.create()
						.texOffs(226, 150).addBox(-18.0F, -2.0F, 0.0F, 2.0F, 2.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(210, 146).addBox(-18.0F, -2.0F, 10.0F, 2.0F, 16.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(9.0F, -39.0F, 6.0F, -0.6109F, 0.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(188, 58).addBox(-8.0F, -9.0F, -16.0F, 16.0F, 13.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(195, 118).addBox(2.0F, -13.0F, -13.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(195, 134).addBox(-4.0F, -13.0F, -13.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(172, 97).addBox(8.0F, -4.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(171, 77).addBox(9.0F, -5.0F, -10.0F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(176, 103).addBox(-9.0F, -4.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(154, 98).addBox(-11.0F, -5.0F, -10.0F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -40.0F, -3.0F));


		head.addOrReplaceChild("mouse",
				CubeListBuilder.create()
						.texOffs(186, 92).addBox(-8.0F, 0.0F, -15.0F, 16.0F, 3.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(134, 6).addBox(-8.0F, -2.0F, -3.0F, 16.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-1.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(1.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(4.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-5.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-7.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -2.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -2.0F, -6.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -2.0F, -7.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -2.0F, -12.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-1.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(2.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(6.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-5.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -1.0F, -11.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -1.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -1.0F, -12.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -1.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -1.0F, -5.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 6.0F, -1.0F));


		head.addOrReplaceChild("lace",
				CubeListBuilder.create()
						.texOffs(151, 151).addBox(-11.0F, -3.0F, 0.0F, 22.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(155, 126).addBox(-11.0F, -3.0F, -11.0F, 3.0F, 3.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(152, 108).addBox(8.0F, -3.0F, -11.0F, 3.0F, 3.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(102, 247).addBox(-11.0F, -3.0F, -14.0F, 22.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 1.1345F, 0.0F, 0.0F));


		head.addOrReplaceChild("red_eyes",
				CubeListBuilder.create()
						.texOffs(86, 249).addBox(-5.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(89, 241).addBox(4.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-2.0F, -1.0F, -17.0F));


		head.addOrReplaceChild("yellow_eyes",
				CubeListBuilder.create()
						.texOffs(92, 231).addBox(-5.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(94, 222).addBox(4.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-2.0F, -1.0F, -17.0F));


		head.addOrReplaceChild("blue_eyes",
				CubeListBuilder.create()
						.texOffs(92, 202).addBox(-5.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(91, 212).addBox(4.0F, -5.0F, 0.9F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-2.0F, -1.0F, -17.0F));

		up.addOrReplaceChild("left_arm",
				CubeListBuilder.create()
						.texOffs(108, 180).addBox(-4.0F, -5.0F, -5.0F, 10.0F, 48.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(15.0F, -35.0F, 2.0F));


		up.addOrReplaceChild("right_arm",
				CubeListBuilder.create()
						.texOffs(106, 112).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 48.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(-16.0F, -35.0F, 2.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}

	@Override
	public void setupAnim(PvZZombieRenderState state) {
		super.setupAnim(state);
		this.red_eyes.visible = (state.robotState == EdgarRobotEntity.EdgarStates.FLAME);
		this.blue_eyes.visible = (state.robotState == EdgarRobotEntity.EdgarStates.ICE);
		this.yellow_eyes.visible = (!this.red_eyes.visible && !this.blue_eyes.visible);
		if (state.robotState == EdgarRobotEntity.EdgarStates.FLAME
				|| state.robotState == EdgarRobotEntity.EdgarStates.ICE) {
			this.mouse.xRot = AnimationUtil.getUp(state.attackTime, state.animShootCD, 30);
			this.right_arm.xRot = 0;
			this.left_arm.xRot = 0;
		} else if (state.robotState == EdgarRobotEntity.EdgarStates.CAR) {
			this.right_arm.xRot = AnimationUtil.getUpDown(state.attackTime, state.animThrowCD, -60);
			this.mouse.xRot = 0;
			this.left_arm.xRot = 0;
		} else if (state.robotState == EdgarRobotEntity.EdgarStates.STEAL) {
			this.left_arm.xRot = AnimationUtil.getUpDown(state.attackTime, state.animStealCD, -120);
			this.right_arm.xRot = AnimationUtil.getUpDown(state.attackTime, state.animStealCD, -120);
			this.mouse.xRot = 0;
		} else {
			this.left_arm.xRot = 0;
			this.right_arm.xRot = 0;
			this.mouse.xRot = 0;
		}
	}



	@Override
	public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
                             float ageInTicks, float netHeadYaw, float headPitch) {

	}


	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {
		switch (entity.getBodyType()) {
			case LEFT_LEG -> submitPart(this.left_leg, stack, collector, renderType, packedLight, packedOverlay);
			case RIGHT_LEG -> submitPart(this.right_leg, stack, collector, renderType, packedLight, packedOverlay);
			case LEFT_HAND -> submitPart(this.left_arm, stack, collector, renderType, packedLight, packedOverlay);
			case RIGHT_HAND -> submitPart(this.right_arm, stack, collector, renderType, packedLight, packedOverlay);
			case BODY -> submitPart(this.body, stack, collector, renderType, packedLight, packedOverlay);
			case HEAD -> submitPart(this.head, stack, collector, renderType, packedLight, packedOverlay);
			default -> { }
		}
	}

	private void submitPart(ModelPart part, PoseStack stack, SubmitNodeCollector collector,
							RenderType renderType, int packedLight, int packedOverlay) {
		final boolean savedVisible = part.visible;
		final float sx = part.x, sy = part.y, sz = part.z;

		this.setAllInvis();
		part.visible = true;
		part.setPos(0, 24, 0);
		collector.submitModelPart(part, stack, renderType, packedLight, packedOverlay, null);

		part.visible = savedVisible;
		part.setPos(sx, sy, sz);
	}

	public void setAllInvis() {
		this.left_leg.visible = false;
		this.right_leg.visible = false;
		this.left_arm.visible = false;
		this.right_arm.visible = false;
		this.head.visible = false;
		this.body.visible = false;
	}

	@Override
	public EntityModel<? extends EntityRenderState> getZombieModel() {
		return this;
	}
}