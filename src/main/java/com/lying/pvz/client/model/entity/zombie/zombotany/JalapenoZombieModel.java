package com.lying.pvz.client.model.entity.zombie.zombotany;


import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
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
//   1. 几何 1:1 搬运。骨架 + 墨西哥辣椒头（10 主方块 + 7 节链式辣椒枝）。
//   2. head 子树结构：
//        head（10 方块：主辣椒 + 顶端 3 层装饰）
//          ├─ bone8 → bone9 → bone10（顶上一根短枝，链式 3 节）
//          └─ bone11 → bone12 → bone13 → bone14（侧向辣椒枝，链式 4 节，共 5 节含 bone11 本身）
//      ⚠️ 这是"轴心叠加"的链式结构，每节都挂上一节下，旋转角累计：
//        bone8  X+60°
//        bone9  X+40°
//        bone10 X+55°
//        bone11 Z+15°（另起一根，从 head 直出）
//        bone12 Z-25°
//        bone13 Z-35°
//        bone14 Z-110°
//      不要改成兄弟节点，否则每节的旋转中心会跑偏到 head 原点，辣椒枝错位。
//   3. ⚠️ bone8 的子节点顺序：bone8 自己先定位在 (0, 16, -12) + X+60°，
//      其子 bone9 再 offset (0, -3, 0) + X+40°，以此类推——
//      每节偏移是相对上一节末端，并非绝对坐标，逐层搬运。
//   4. 无 setupAnim 重写。

public class JalapenoZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("jalapeno_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;

	public JalapenoZombieModel(ModelPart root) {
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

		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -20.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(191, 221).addBox(-8.0F, -21.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(194, 196).addBox(-7.0F, -5.0F, -7.0F, 14.0F, 2.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(202, 168).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 3.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(210, 141).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 4.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(212, 114).addBox(-4.0F, 4.0F, -5.0F, 8.0F, 5.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(225, 95).addBox(-3.0F, 9.0F, -6.0F, 5.0F, 4.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(224, 70).addBox(-3.0F, 13.0F, -8.0F, 6.0F, 3.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(227, 54).addBox(-3.0F, 16.0F, -12.0F, 6.0F, 3.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(72, 237).addBox(-7.0F, -22.0F, -7.0F, 14.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(10, 235).addBox(-6.0F, -23.0F, -6.0F, 12.0F, 1.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, -2.0F));


		PartDefinition bone8 = head.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(226, 32).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 16.0F, -12.0F, 1.0472F, 0.0F, 0.0F));

		PartDefinition bone9 = bone8.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(164, 224).addBox(-2.0F, -0.6428F, -0.766F, 4.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.6981F, 0.0F, 0.0F));

		bone9.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(150, 236).addBox(-1.0F, -0.0261F, -0.2521F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -0.0152F, 2.8264F, 0.9599F, 0.0F, 0.0F));


		PartDefinition bone11 = head.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(127, 207).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -23.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone12 = bone11.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(87, 213).addBox(-2.0F, -2.0F, -1.0F, 6.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.2247F, -5.2929F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition bone13 = bone12.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(58, 204).addBox(0.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.6375F, 2.1433F, 0.0F, 0.0F, 0.0F, -0.6109F));

		bone13.addOrReplaceChild("bone14",
				CubeListBuilder.create()
						.texOffs(101, 183).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.7071F, -0.1213F, 0.0F, 0.0F, 0.0F, -1.9199F));

		return LayerDefinition.create(mesh, 256, 256);
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