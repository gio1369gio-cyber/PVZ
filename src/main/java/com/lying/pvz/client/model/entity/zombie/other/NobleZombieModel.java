package com.lying.pvz.client.model.entity.zombie.other;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。贵族僵尸模型：
//     total（根，无方块）
//       ├─ right_leg（setPos(-4,-24,0)，1 个腿方块）
//       ├─ left_leg（setPos(4,-24,0)，1 个腿方块）
//       └─ up（setPos(0,-24,0)）
//            ├─ body（setPos(0,0,0)，1 个躯干方块）
//            ├─ left_hand（setPos(12,-20,0)，1 个手方块）
//            ├─ right_hand（setPos(-12,-20,0)，1 个手方块）
//            │    └─ stick（setPos(0,18,0)，3 个方块：权杖杆 + 杖头 + 手部护套）
//            └─ head（setPos(0,-24,0)，1 个头部方块）
//                 └─ bone（setPos(0,-16,0)，2 个方块：帽檐 + 帽子）
//   setupAnim：调用 super 后，若 attackTime < 20，right_hand.xRot = -2f（举杖）。
//   MathHelper 未使用。
//   stick 和 bone 在 setupAnim 中从未被访问，无需取回为字段。

public class NobleZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("noble_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;

	public NobleZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
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
				PartPose.offset(0.0F, 0.0F, 0.0F));

		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -20.0F, 0.0F));


		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -20.0F, 0.0F));


		right_hand.addOrReplaceChild("stick",
				CubeListBuilder.create()
						.texOffs(59, 105).addBox(0.0F, -1.0F, -32.0F, 1.0F, 1.0F, 26.0F, CubeDeformation.NONE)
						.texOffs(20, 102).addBox(-0.5F, -1.5F, -34.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(75, 84).addBox(-1.0F, -2.0F, -6.0F, 3.0F, 3.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 18.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(16, 96).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		head.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(0, 74).addBox(-11.0F, -1.0F, -10.0F, 22.0F, 1.0F, 20.0F, CubeDeformation.NONE)
						.texOffs(56, 37).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 5.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -16.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		super.setupAnim(state);
		if (state.attackTime < 20) {
			this.right_hand.xRot = -2f;
		}
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