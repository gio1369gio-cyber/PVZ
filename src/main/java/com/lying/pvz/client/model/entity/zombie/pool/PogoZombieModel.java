package com.lying.pvz.client.model.entity.zombie.pool;


import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Mth;

// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。弹簧高跷僵尸模型：
//     total（根，setPos(0, 24, 2) + X 5°，无方块）
//       ├─ zombie（setPos(0,-6,5)，无方块）
//       │    ├─ up（setPos(0,-24,0)，无方块）
//       │    │    ├─ body（setPos(0,-7,0)，1 个躯干方块）
//       │    │    ├─ left_hand（setPos(12,-20,0) + X -25° + Y 5°，1 个手方块）
//       │    │    ├─ right_hand（setPos(-12,-20,0) + X -25° + Y -5°，1 个手方块）
//       │    │    └─ head（setPos(0,-24,0)，1 个头部方块）
//       │    │         └─ bone3（setPos(0,-3.091,-8.0834)，7 个方块：眼镜框）
//       │    ├─ left_leg（setPos(4,-24,0) + X -25° + Y -15°，1 个腿方块）
//       │    └─ right_leg（setPos(-4,-24,0) + X -25° + Y 15°，1 个腿方块）
//       └─ pogo（setPos(0,0,0)，7 个方块：弹簧杆 + 弹簧 + 踏板 + 左右把手）
//   setupAnim：
//     hasPogo() 时（踩在弹簧上）：
//       - 双手/双腿/整体固定为踩踏姿势（X -25° + Y ±5°/-15°+15°）
//       - total.xRot = 5°，head 跟随视线
//     hasPogo() 为 false 时：
//       - 清零双手/双腿的 Y 旋转，total.xRot 归零
//       - 调用 super.setupAnim 走标准行走/攻击逻辑
//     最后 updateFreeParts + pogo.visible = entity.hasMetal()
//   tickPartAnim：清零双手/双腿的 Y 旋转和 X 旋转，total.xRot 归零，再调用 super
//   renderBody：pogo.visible = false（掉落身体时隐藏弹簧），再调用 super
//   注意：setupAnim 末尾的 pogo.visible = entity.hasMetal() 与 updateFreeParts 中的
//         isLeftHandFree/isRightHandFree 判断是两套独立逻辑，原样保留。

public class PogoZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("pogo_zombie"), "main");

	private final ModelPart total;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	private final ModelPart pogo;

	public PogoZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		ModelPart zombie = this.total.getChild("zombie");
		this.up = zombie.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
		this.left_leg = zombie.getChild("left_leg");
		this.right_leg = zombie.getChild("right_leg");
		this.pogo = this.total.getChild("pogo");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 24.0F, 2.0F, 0.0873F, 0.0F, 0.0F));


		PartDefinition zombie = total.addOrReplaceChild("zombie",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -6.0F, 5.0F));


		PartDefinition up = zombie.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(80, 96).addBox(-8.0F, -17.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -7.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(104, 62).addBox(-4.0F, -4.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(12.0F, -20.0F, 0.0F, -0.4363F, 0.0873F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(103, 26).addBox(-2.0F, -4.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-12.0F, -20.0F, 0.0F, -0.4363F, -0.0873F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(20, 100).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		head.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(76, 12).addBox(-8.0F, -5.2921F, -0.2749F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(105, 12).addBox(7.0F, -5.2921F, -0.2749F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(56, 18).addBox(-7.0F, -5.2921F, -0.2749F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(36, 14).addBox(5.0F, -5.2921F, -0.2749F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(16, 21).addBox(-1.0F, -5.2921F, -0.2749F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(20, 4).addBox(-5.0F, -6.2921F, -0.2749F, 4.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 10).addBox(1.0F, -6.2921F, -0.2749F, 4.0F, 3.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -3.091F, -8.0834F));


		zombie.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(58, 62).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, -24.0F, 0.0F, -0.4363F, -0.2618F, 0.0F));


		zombie.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(67, 27).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, -24.0F, 0.0F, -0.4363F, 0.2618F, 0.0F));

		total.addOrReplaceChild("pogo",
				CubeListBuilder.create()
						.texOffs(24, 85).addBox(-1.0F, -10.0F, -6.0F, 2.0F, 10.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(36, 66).addBox(-2.0F, -22.0F, -7.0F, 4.0F, 12.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(32, 32).addBox(-3.0F, -38.0F, -8.0F, 6.0F, 16.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(0, 87).addBox(3.0F, -34.0F, -6.0F, 9.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(6, 51).addBox(-8.0F, -8.0F, -6.0F, 7.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(2, 67).addBox(1.0F, -8.0F, -6.0F, 7.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(4, 37).addBox(-12.0F, -34.0F, -6.0F, 9.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		if (state.hasPogo) {
			this.left_hand.xRot = -0.4363F;
			this.left_hand.yRot = 0.0873F;
			this.right_hand.xRot = -0.4363F;
			this.right_hand.yRot = -0.0873F;
			this.left_leg.xRot = -0.4363F;
			this.left_leg.yRot = -0.2618F;
			this.right_leg.xRot = -0.4363F;
			this.right_leg.yRot = 0.2618F;
			this.total.xRot = 0.0873F;
			this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
			this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
		} else {
			this.left_hand.yRot = 0;
			this.right_hand.yRot = 0;
			this.left_leg.yRot = 0;
			this.right_leg.yRot = 0;
			this.total.xRot = 0;
			super.setupAnim(state);
		}
		this.updateFreeParts(state);
		this.pogo.visible = state.hasMetal;
	}

	@Override
	public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
                             float ageInTicks, float netHeadYaw, float headPitch) {
		this.left_hand.yRot = 0;
		this.right_hand.yRot = 0;
		this.left_leg.yRot = 0;
		this.right_leg.yRot = 0;
		this.left_leg.xRot = 0;
		this.right_leg.xRot = 0;
		this.total.xRot = 0;
		super.tickPartAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}


	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {
		this.pogo.visible = false;
		super.renderBody(entity, stack, collector, renderType, packedLight, packedOverlay);
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