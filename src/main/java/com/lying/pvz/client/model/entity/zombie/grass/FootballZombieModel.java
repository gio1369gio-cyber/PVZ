package com.lying.pvz.client.model.entity.zombie.grass;

import java.util.Optional;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
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
import net.minecraft.util.Mth;

// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。橄榄球僵尸模型：
//     total（根，无方块）
//       ├─ left_leg（setPos(5,-31,0)，4 个方块：大腿 + 小腿 + 脚背 + 脚底）
//       ├─ right_leg（setPos(-6,-32,0)，4 个方块：大腿 + 小腿 + 脚背 + 脚底）
//       └─ up（setPos(0,-31,0)）
//            ├─ body（1 个躯干方块）
//            ├─ head（1 个头部方块）
//            │    └─ helmet（setPos(0,0,0)，15 个方块：盔顶 + 4 面 + 细节）
//            ├─ left_hand（setPos(14,-27,0)，10 个方块：手 + 5 指）
//            │    ├─ bone（setPos(9,4,0) + Z -50°，1 个方块）
//            │    └─ hand（setPos(2,11,-1)）
//            │         ├─ bone3~bone7（5 指，Z 旋转从 -65° 到 30°）
//            └─ right_hand（setPos(-14,-27,0) + Y 180°，10 个方块）
//                 ├─ bone2（setPos(9,4,0) + Z -50°，1 个方块）
//                 └─ hand2（setPos(2,7,1) + Y 180°）
//                      └─ bone8~bone12（5 指）
//   updateFreeParts：helmet.visible = entity.hasMetal()
//   doWalkAnimation：腿摆动 ×1.5，手摆动 ÷3（重物感）
//   doPreAttackPose：左手 +HAND_MAX_ANGLE/3，右手 -HAND_MAX_ANGLE/3（左右反向）
//   doHandEat：左手 +HAND_MAX_ANGLE/3 - getUpDown，右手 -HAND_MAX_ANGLE/3 + getUpDown
//   MathHelper → Mth：cos 方法不变。

public class FootballZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("football_zombie"), "main");

	private final ModelPart total;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart helmet;
	private final ModelPart left_hand;
	private final ModelPart right_hand;

	public FootballZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.right_leg = this.total.getChild("right_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.head = this.up.getChild("head");
		this.helmet = this.head.getChild("helmet");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(236, 237).addBox(-2.0F, 16.0F, -2.0F, 4.0F, 12.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(202, 241).addBox(-3.0F, 28.0F, -6.0F, 6.0F, 3.0F, 9.0F, CubeDeformation.NONE)
						.texOffs(237, 229).addBox(-3.0F, 27.0F, -6.0F, 6.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 232).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 18.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(5.0F, -31.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(32, 236).addBox(-1.0F, 17.0F, -2.0F, 4.0F, 12.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(54, 242).addBox(-2.0F, 29.0F, -6.0F, 6.0F, 3.0F, 9.0F, CubeDeformation.NONE)
						.texOffs(88, 251).addBox(-2.0F, 28.0F, -6.0F, 6.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(108, 232).addBox(-2.0F, -1.0F, -3.0F, 6.0F, 18.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-6.0F, -32.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -31.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(197, 4).addBox(-9.0F, -32.0F, -5.0F, 18.0F, 32.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(189, 49).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -32.0F, 0.0F));


		head.addOrReplaceChild("helmet",
				CubeListBuilder.create()
						.texOffs(178, 86).addBox(-9.0F, -18.0F, -10.0F, 18.0F, 2.0F, 19.0F, CubeDeformation.NONE)
						.texOffs(194, 111).addBox(-7.0F, -19.0F, -7.0F, 14.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(214, 132).addBox(9.0F, -16.0F, -9.0F, 1.0F, 16.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(215, 172).addBox(-9.0F, -16.0F, 9.0F, 18.0F, 16.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(208, 195).addBox(-9.0F, 0.0F, 5.0F, 18.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(247, 207).addBox(-9.0F, -4.0F, -10.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(246, 217).addBox(8.0F, -4.0F, -10.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(152, 220).addBox(-10.0F, -16.0F, -9.0F, 1.0F, 16.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(197, 231).addBox(-8.0F, -1.0F, -10.0F, 16.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(192, 219).addBox(-8.0F, -4.0F, -10.0F, 16.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(182, 212).addBox(-7.0F, -16.0F, -10.0F, 1.0F, 15.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(140, 225).addBox(6.0F, -16.0F, -10.0F, 1.0F, 15.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(86, 239).addBox(2.0F, -3.0F, -10.0F, 1.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(56, 240).addBox(-3.0F, -3.0F, -10.0F, 1.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(57, 233).addBox(-3.0F, -16.0F, -10.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(30, 227).addBox(2.0F, -16.0F, -10.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition left_hand = up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(3, 214).addBox(-5.0F, -5.0F, -5.0F, 20.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(60, 216).addBox(-5.0F, -4.0F, -6.0F, 16.0F, 6.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(106, 213).addBox(-5.0F, -4.0F, 5.0F, 16.0F, 6.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(144, 201).addBox(-5.0F, -4.0F, -5.0F, 1.0F, 6.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(4, 193).addBox(-5.0F, 2.0F, -5.0F, 15.0F, 3.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(100, 199).addBox(11.0F, -4.0F, -6.0F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(115, 203).addBox(13.0F, -4.0F, -6.0F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(128, 200).addBox(11.0F, -4.0F, 5.0F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(171, 204).addBox(13.0F, -4.0F, 5.0F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(126, 174).addBox(-5.0F, 5.0F, -5.0F, 10.0F, 6.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(14.0F, -27.0F, 0.0F));

		left_hand.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(60, 197).addBox(2.0F, -1.0F, -5.0F, 8.0F, 1.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(9.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.8727F));


		PartDefinition hand = left_hand.addOrReplaceChild("hand",
				CubeListBuilder.create()
						.texOffs(4, 160).addBox(-4.0F, -5.0F, -20.0F, 4.0F, 4.0F, 24.0F, CubeDeformation.NONE),
				PartPose.offset(2.0F, 11.0F, -1.0F));


		hand.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(66, 183).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, -3.0F, -20.0F, 0.0F, 0.0F, -1.1345F));

		hand.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(76, 184).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-3.0F, -5.0F, -20.0F, 0.0F, 0.0F, -0.4363F));

		hand.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(88, 184).addBox(-1.0F, -4.0F, 0.0F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-2.0F, -5.0F, -20.0F));

		hand.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(101, 185).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0F, -5.0F, -20.0F, 0.0F, 0.0F, 0.4363F));

		hand.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(114, 184).addBox(0.0F, -3.0F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0F, -4.0F, -20.0F, 0.0F, 0.0F, 0.5236F));


		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(4, 143).addBox(-5.0F, -5.0F, -5.0F, 20.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(53, 160).addBox(-5.0F, -4.0F, -6.0F, 16.0F, 6.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(62, 146).addBox(-5.0F, -4.0F, 5.0F, 16.0F, 6.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(100, 157).addBox(-5.0F, -4.0F, -5.0F, 1.0F, 6.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(4, 124).addBox(-5.0F, 2.0F, -5.0F, 15.0F, 3.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(107, 142).addBox(11.0F, -4.0F, -6.0F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(124, 151).addBox(13.0F, -4.0F, -6.0F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(135, 160).addBox(11.0F, -4.0F, 5.0F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(172, 172).addBox(13.0F, -4.0F, 5.0F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(7, 97).addBox(-5.0F, 5.0F, -5.0F, 10.0F, 6.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-14.0F, -27.0F, 0.0F, 0.0F, 3.1416F, 0.0F));


		right_hand.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(63, 127).addBox(2.0F, -1.0F, -5.0F, 8.0F, 1.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(9.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.8727F));


		PartDefinition hand2 = right_hand.addOrReplaceChild("hand2",
				CubeListBuilder.create()
						.texOffs(61, 92).addBox(0.0609F, -1.0F, -20.6055F, 4.0F, 4.0F, 24.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.0F, 7.0F, 1.0F, 0.0F, 3.1416F, 0.0F));


		hand2.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(131, 129).addBox(-1.4226F, -1.0937F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(5.0609F, 2.0F, -20.6055F, 0.0F, 0.0F, 1.1345F));

		hand2.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(156, 136).addBox(-0.9128F, -2.9962F, 0.0F, 1.0F, 3.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0609F, 0.0F, -20.6055F, 0.0F, 0.0F, -0.6981F));

		hand2.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(180, 152).addBox(-0.9128F, -3.9962F, 0.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.0609F, 0.0F, -20.6055F, 0.0F, 0.0F, -0.3491F));

		hand2.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(153, 92).addBox(-1.9962F, -3.9128F, 0.0F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(4.0609F, -1.0F, -20.6055F));

		hand2.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(169, 120).addBox(-0.8051F, -4.5F, 0.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(3.0F, 1.0F, -20.6055F, 0.0F, 0.0F, 0.5236F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
        this.helmet.visible = state.hasMetal;
	}

	// 1.16.5: 腿摆动 ×1.5，手摆动 ÷3（重物感）；MathHelper → Mth
	// 与默认 PVZZombieModel.doWalkAnimation 的差异：本方法不使用 isLeftHandFree/isRightHandFree 判断
	@Override
	protected void doWalkAnimation(float limbSwing, float limbSwingAmount,
								   float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.right_leg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 1.5f;
		this.left_leg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 1.5f;
		this.right_hand.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / 3;
		this.left_hand.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / 3;
	}

	// 1.16.5: 左手 +HAND_MAX_ANGLE/3，右手 -HAND_MAX_ANGLE/3（左右反向）
	// 与默认 PVZZombieModel.doPreAttackPose 的差异：左右手旋转符号相反
	@Override
	protected void doPreAttackPose() {
		if (this.isLeftHandFree || this.isRightHandFree) {
			if (this.isLeftHandFree) {
				this.getZombieLeftHand().xRot = HAND_MAX_ANGLE / 3;
			}
			if (this.isRightHandFree) {
				this.getZombieRightHand().xRot = -HAND_MAX_ANGLE / 3;
			}
		}
	}

	// 1.16.5: 左手 +HAND_MAX_ANGLE/3 - getUpDown，右手 -HAND_MAX_ANGLE/3 + getUpDown
	// 与默认 PVZZombieModel.doHandEat 的差异：左右手旋转符号相反、getUpDown 符号相反
	@Override
	protected void doHandEat(PvZZombieRenderState state) {
		if (this.isLeftHandFree) {
			this.getZombieLeftHand().xRot = HAND_MAX_ANGLE / 3
					- AnimationUtil.getUpDown(MAX_ANIM_CD - state.animTime, MAX_ANIM_CD, -70);
		}
		if (this.isRightHandFree) {
			this.getZombieRightHand().xRot = -HAND_MAX_ANGLE / 3
					+ AnimationUtil.getUpDown(MAX_ANIM_CD - state.animTime, MAX_ANIM_CD, -70);
		}
	}

	@Override
	public Optional<ModelPart> getHelmet() {
		return Optional.ofNullable(this.helmet);
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