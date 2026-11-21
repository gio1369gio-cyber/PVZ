package com.lying.pvz.client.model.entity.zombie.pool;


import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.api.entity.ability.BodyType;
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


// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。气球僵尸模型：
//     total（根，无方块）
//       ├─ balloon（setPos(0,-34,0.75)，气球+线整体）
//       │    ├─ other（setPos(0,1,0)，14 个方块：气球主体 16×20×16 + 4 面 + 上下盖 + 绳子）
//       │    └─ tile（setPos(0,-0.5,0) + X -20°，4 个方块：吊篮四边形）
//       └─ zombie（setPos(0,-30,0) + X 60°，僵尸本体，整体前倾）
//            ├─ left_leg（setPos(4,7,0) + X -45°，1 个腿方块）
//            ├─ right_leg（setPos(-4,7,0) + X -45°，1 个腿方块）
//            └─ up（setPos(0,6,0) + X 15°，1 个躯干方块）
//                 ├─ left_hand（setPos(9,-21,0) + X -100°，1 个手方块）
//                 ├─ right_hand（setPos(-8,-21,-1) + X -100°，1 个手方块）
//                 └─ head（setPos(0,-24,0) + X -60°，3 个方块：头 + 帽檐 + 顶部装饰）
//                      └─ bone（setPos(0,-17,0)，4 个方块：螺旋桨叶片，十字形）
//   setupAnim：
//     hasBalloon() 时：
//       - zombie / 双腿 / 躯干 / 双手 / 头 全部固定在特定角度（气球吊挂姿势）
//       - head.yRot 归零
//       - bone.yRot = ageInTicks（螺旋桨高速旋转）
//     hasBalloon() 为 false 时：
//       - up.xRot / zombie.xRot 归零（恢复站立姿势）
//       - 调用 super.setupAnim 走标准行走/攻击逻辑
//       - bone.yRot = ageInTicks / 4（螺旋桨慢速旋转）
//     balloon.visible = hasBalloon()
//   updateFreeParts：isLeftHandFree / isRightHandFree = !hasBalloon()（挂气球时双手不自由）
//   tickPartAnim：重置所有肢体角度，bodyType==HEAD 时 bone.yRot = ageInTicks/4，再调用 super
//   renderBody：balloon.visible = false（掉落身体部件时不显示气球），再调用 super
//   注意：setupAnim 内部重复调用了两次 updateFreeParts，原样保留。

public class BalloonZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("balloon_zombie"), "main");

	private final ModelPart total;
	private final ModelPart balloon;
	private final ModelPart zombie;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;
	private final ModelPart bone;

	public BalloonZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.balloon = this.total.getChild("balloon");
		this.zombie = this.total.getChild("zombie");
		this.left_leg = this.zombie.getChild("left_leg");
		this.right_leg = this.zombie.getChild("right_leg");
		this.up = this.zombie.getChild("up");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
		this.bone = this.head.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition balloon = total.addOrReplaceChild("balloon",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -34.0F, 0.75F));

		balloon.addOrReplaceChild("other",
				CubeListBuilder.create()
						.texOffs(78, 91).addBox(-1.0F, -20.0F, -1.0F, 2.0F, 21.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(84, 86).addBox(-3.0F, -21.0F, -3.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(87, 75).addBox(-5.0F, -22.0F, -5.0F, 10.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(73, 78).addBox(-3.0F, -44.0F, -3.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(54, 56).addBox(-5.0F, -43.0F, -5.0F, 10.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 28).addBox(-8.0F, -42.0F, -8.0F, 16.0F, 20.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(98, 0).addBox(-7.0F, -41.0F, -9.0F, 14.0F, 18.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(106, 19).addBox(-5.0F, -39.0F, 9.0F, 10.0F, 14.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(68, 0).addBox(-7.0F, -41.0F, 8.0F, 14.0F, 18.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(84, 19).addBox(-5.0F, -39.0F, -10.0F, 10.0F, 14.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(98, 43).addBox(8.0F, -41.0F, -7.0F, 1.0F, 18.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(46, 0).addBox(-10.0F, -39.0F, -5.0F, 1.0F, 14.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(64, 24).addBox(-9.0F, -41.0F, -7.0F, 1.0F, 18.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(24, 0).addBox(9.0F, -39.0F, -5.0F, 1.0F, 14.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 38).addBox(-2.0F, -15.0F, -2.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 1.0F, 0.0F));


		balloon.addOrReplaceChild("tile",
				CubeListBuilder.create()
						.texOffs(34, 24).addBox(-8.0F, 1.0F, 0.0F, 16.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, 10.0F, 0.0F, 16.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 25).addBox(8.0F, 1.0F, 0.0F, 1.0F, 10.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 10).addBox(-9.0F, 1.0F, 0.0F, 1.0F, 10.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition zombie = total.addOrReplaceChild("zombie",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -30.0F, 0.0F, 1.0472F, 0.0F, 0.0F));


		zombie.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 97).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 25.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, 7.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		zombie.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(24, 97).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 25.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 7.0F, 0.0F, -0.7854F, 0.0F, 0.0F));


		PartDefinition up = zombie.addOrReplaceChild("up",
				CubeListBuilder.create()
						.texOffs(0, 65).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.2618F, 0.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(48, 98).addBox(-1.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(9.0F, -21.0F, 0.0F, -1.7453F, 0.0F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(49, 68).addBox(-6.0F, -4.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-8.0F, -21.0F, -1.0F, -1.7453F, 0.0F, 0.0F));

		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(72, 100).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(87, 93).addBox(-3.0F, -15.0F, -3.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(18, 99).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, -1.0472F, 0.0F, 0.0F));


		head.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(115, 112).addBox(-5.0F, -1.0F, 0.0F, 5.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(116, 106).addBox(-1.0F, -1.0F, -5.0F, 1.0F, 1.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(115, 104).addBox(0.0F, -1.0F, -1.0F, 5.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(114, 98).addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -17.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(PvZZombieRenderState state) {
		final boolean hasBalloon = state.hasBalloon ;
		if (hasBalloon) {
			this.zombie.xRot = 1.0472F;
			this.left_leg.xRot = -0.7854F;
			this.right_leg.xRot = -0.7854F;
			this.up.xRot = 0.2618F;
			this.left_hand.xRot = -1.7453F;
			this.right_hand.xRot = -1.7453F;
			this.head.xRot = -1.0472F;
			this.head.yRot = 0;
			this.bone.yRot = state.ageInTicks;
			this.updateFreeParts(state);
		} else {
			super.setupAnim(state);
			// 基础姿态里 zombie(60°)/up(15°) 是吊挂姿态，super.setupAnim 的 resetPose 会把它复位回去，
			// 必须在 super 之后清零，否则气球破了身体仍保持前倾的空中姿态。
			this.zombie.xRot = 0.0F;
			this.up.xRot = 0.0F;
			this.bone.yRot = state.ageInTicks / 4;
		}
		this.updateFreeParts(state);
		this.balloon.visible = hasBalloon;
	}


	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		final boolean hasBalloon = state.hasBalloon;
		this.isLeftHandFree = !hasBalloon;
		this.isRightHandFree = !hasBalloon;
	}

	// 1.16.5: 重置所有肢体角度；bodyType==HEAD 时 bone.yRot = ageInTicks/4
	// 1.21.1: 参数类型改为 PoseStack / VertexConsumer
	@Override
	public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
                             float ageInTicks, float netHeadYaw, float headPitch) {
		this.left_leg.xRot = 0;
		this.right_leg.xRot = 0;
		this.up.xRot = 0;
		this.head.xRot = 0;
		this.left_hand.xRot = 0;
		this.right_hand.xRot = 0;
		this.zombie.xRot = 0;
		if (entity.getBodyType() == BodyType.HEAD) {
			this.bone.yRot = ageInTicks / 4;
		}
		super.tickPartAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {
		super.renderBody(entity, stack, collector, renderType, packedLight, packedOverlay);
		this.balloon.visible = false;
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