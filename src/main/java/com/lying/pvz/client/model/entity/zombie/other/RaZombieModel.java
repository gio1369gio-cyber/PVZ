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

// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。Ra 僵尸模型：
//     total（根，无方块）
//       ├─ right_leg（setPos(-4,-24,0)，1 个腿方块）
//       ├─ left_leg（setPos(4,-24,0)，1 个腿方块）
//       └─ up（setPos(0,-24,0)）
//            ├─ body（setPos(0,0,0)，1 个躯干方块）
//            ├─ left_hand（setPos(12,-20,0)，1 个手方块）
//            ├─ right_hand（setPos(-12,-20,0) + X -30°，1 个手方块）
//            │    └─ collector（setPos(1,-1.5,-24) + X -90° + Y -90°，1 个 10×0×10 平板）
//            │         ├─ cube_r1（setPos(0,0.5,0) + Y 45°，1 个方块，镜像）
//            │         ├─ group2（setPos(0,-7.5,0) + Y -10°，含 cube_r2/cube_r3，各 4 个方块）
//            │         └─ group3（setPos(0,-7.5,0) + Y -10°，4 个方块 + cube_r4）
//            └─ head（setPos(0,-24,0)，7 个方块：头 + 帽 + 左右鬓角 + 下巴三层）
//                 └─ bone（setPos(0,-14,-9) + Y -45°，1 个方块，膨胀 -0.05F，鼻子）
//   setupAnim：若 checkCanWorkNow()，right_hand.xRot = -1f（举起收集器），然后调用 super。
//   updateFreeParts：isRightHandFree = !checkCanWorkNow()（工作时右手不自由）。
//   注意：setupAnim 中先设置 right_hand.xRot，再调用 super；
//         由于 updateFreeParts 已在 super 之前把 isRightHandFree 设为 false（工作时），
//         super 中的 doWalkAnimation 不会覆盖 right_hand.xRot。
//   MathHelper 未使用。
//   collector / cube_r1 / group2 / group3 / bone 在 setupAnim 中从未被访问，无需取回为字段。

public class RaZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("ra_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;

	public RaZombieModel(ModelPart root) {
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
						.texOffs(0, 96).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(32, 96).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));

		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(80, 96).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(96, 64).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -20.0F, 0.0F));


		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(64, 64).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-12.0F, -20.0F, 0.0F, -0.5236F, 0.0F, 0.0F));


		PartDefinition collector = right_hand.addOrReplaceChild("collector",
				CubeListBuilder.create()
						.texOffs(88, 54).addBox(-5.0F, -0.49F, -5.0F, 10.0F, 0.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0F, -1.5F, -24.0F, -1.5708F, -1.5708F, 0.0F));


		collector.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(0, 26).addBox(-1.5F, -2.02F, -2.0F, 3.0F, 1.0F, 37.0F, CubeDeformation.NONE).mirror(),
				PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.7854F, 0.0F));


		PartDefinition group2 = collector.addOrReplaceChild("group2",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -7.5F, 0.0F, 0.0F, -0.1745F, 0.0F));

		group2.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(56, 98).addBox(-1.5F, -1.01F, -10.0F, 3.0F, 1.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(64, 104).addBox(5.0F, -1.01F, -1.5F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(64, 108).addBox(-1.5F, -1.01F, 5.0F, 3.0F, 1.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(64, 114).addBox(-10.0F, -1.01F, -1.5F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		group2.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(64, 118).addBox(5.0F, -1.01F, -1.5F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(64, 122).addBox(-1.5F, -1.01F, 5.0F, 3.0F, 1.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(72, 96).addBox(-10.0F, -1.01F, -1.5F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(81, 58).addBox(-1.5F, -1.01F, -10.0F, 3.0F, 1.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition group3 = collector.addOrReplaceChild("group3",
				CubeListBuilder.create()
						.texOffs(0, 76).addBox(-10.0F, 6.98F, -1.5F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 70).addBox(-1.5F, 6.98F, 5.0F, 3.0F, 1.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(0, 66).addBox(5.0F, 6.98F, -1.5F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(48, 69).addBox(-1.5F, 6.98F, -10.0F, 3.0F, 1.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -7.5F, 0.0F, 0.0F, -0.1745F, 0.0F));

		group3.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(54, 59).addBox(5.0F, -1.02F, -1.5F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(88, 66).addBox(-1.5F, -1.02F, 5.0F, 3.0F, 1.0F, 5.0F, CubeDeformation.NONE).mirror()
						.texOffs(48, 75).addBox(-10.0F, -1.02F, -1.5F, 5.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(24, 97).addBox(-1.5F, -1.02F, -10.0F, 3.0F, 1.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 64).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(74, 1).addBox(-9.0F, -20.0F, -9.0F, 18.0F, 7.0F, 9.0F, CubeDeformation.NONE)
						.texOffs(0, 43).addBox(8.0F, -19.0F, -8.0F, 4.0F, 14.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(0, 23).addBox(-12.0F, -19.0F, -8.0F, 4.0F, 14.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(45, 49).addBox(-9.0F, -19.0F, 0.0F, 18.0F, 5.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(87, 48).addBox(-9.0F, -18.0F, 3.0F, 18.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(43, 43).addBox(-9.0F, -17.0F, 5.0F, 18.0F, 3.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		head.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(20, 57).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.0F, -14.0F, -9.0F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		super.setupAnim(state);
		if (state.sunLight ) {
			this.right_hand.xRot = -1F;
		}

	}

	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		this.isRightHandFree = !state.sunLight;
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