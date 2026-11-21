package com.lying.pvz.client.model.entity.zombie.custom;


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

// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。岩浆僵尸模型（带岩浆护甲）：
//     total（根，setPos(0, 24, 0)）
//       ├─ right_leg（setPos(-4,-25,0)，1 个腿方块）
//       │    └─ right_foot（setPos(0,24,0)，3 个方块：脚 + 左右脚侧）
//       ├─ left_leg（setPos(4,-25,0)，1 个腿方块）
//       │    └─ left_foot（setPos(0,24,0)，3 个方块：脚 + 左右脚侧）
//       └─ up（setPos(0,-25,0)）
//            ├─ body（setPos(0,0,0)，7 个方块：躯干 + 背包左右 + 背包上下 + 中缝）
//            ├─ head（setPos(0,-25,0)，9 个方块：头 + 4 面 + 上下发丝）
//            ├─ left_hand（setPos(11,-22,0)，1 个手方块）
//            │    └─ armor（setPos(3,2,0)，1 个方块）+ bone（Y -65°）+ bone2（Y 65°）
//            └─ right_hand（setPos(-11,-22,0)，1 个手方块）
//                 └─ bone3（1 个方块）+ bone4（Y -65°）+ bone5（Y 65°）
//   setupAnim：全部继承 PVZZombieModel，本类不重写。
//   所有方块 CubeDeformation.NONE，无 MathHelper 调用。

public class LavaZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("lava_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart left_hand;
	private final ModelPart right_hand;

	public LavaZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.head = this.up.getChild("head");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition right_leg = total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(29, 186).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 23.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -25.0F, 0.0F));

		right_leg.addOrReplaceChild("right_foot",
				CubeListBuilder.create()
						.texOffs(65, 202).addBox(-3.0F, -1.0F, -12.0F, 6.0F, 2.0F, 17.0F, CubeDeformation.NONE)
						.texOffs(124, 218).addBox(3.0F, -1.0F, -11.0F, 1.0F, 2.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(146, 214).addBox(-4.0F, -1.0F, -11.0F, 1.0F, 2.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition left_leg = total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(6, 154).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 23.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -25.0F, 0.0F));


		left_leg.addOrReplaceChild("left_foot",
				CubeListBuilder.create()
						.texOffs(77, 170).addBox(-3.0F, -1.0F, -12.0F, 6.0F, 2.0F, 17.0F, CubeDeformation.NONE)
						.texOffs(55, 171).addBox(3.0F, -1.0F, -11.0F, 1.0F, 2.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(134, 196).addBox(-4.0F, -1.0F, -11.0F, 1.0F, 2.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -25.0F, 0.0F));

		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 223).addBox(-8.0F, -25.0F, -4.0F, 16.0F, 25.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(54, 226).addBox(-8.0F, -22.0F, 4.0F, 7.0F, 21.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(90, 224).addBox(1.0F, -22.0F, 4.0F, 7.0F, 21.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(125, 251).addBox(-5.0F, -23.0F, 7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(127, 242).addBox(-5.0F, -1.0F, 7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(147, 233).addBox(-5.0F, -13.0F, 11.0F, 10.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 193).addBox(-1.0F, -21.0F, 4.0F, 2.0F, 19.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(200, 228).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(163, 236).addBox(7.0F, -9.0F, -8.0F, 1.0F, 4.0F, 15.0F, CubeDeformation.NONE)
						.texOffs(224, 206).addBox(-8.0F, -9.0F, -8.0F, 1.0F, 4.0F, 15.0F, CubeDeformation.NONE)
						.texOffs(180, 218).addBox(-7.0F, -9.0F, 7.0F, 14.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(226, 193).addBox(-7.0F, -10.0F, -8.0F, 14.0F, 6.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(191, 204).addBox(-6.0F, -4.0F, -8.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(230, 184).addBox(-6.0F, -11.0F, -8.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(238, 176).addBox(-4.0F, -3.0F, -8.0F, 8.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(238, 167).addBox(-4.0F, -12.0F, -8.0F, 8.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -25.0F, 0.0F));


		PartDefinition left_hand = up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(202, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 25.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(11.0F, -22.0F, 0.0F));

		PartDefinition armor = left_hand.addOrReplaceChild("armor",
				CubeListBuilder.create()
						.texOffs(208, 36).addBox(0.0F, -2.0F, -4.0F, 1.0F, 6.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(3.0F, 2.0F, 0.0F));


		armor.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(216, 56).addBox(0.0403F, -2.0F, -0.0774F, 1.0F, 6.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, -1.1345F, 0.0F));


		armor.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(219, 76).addBox(0.9063F, -3.0F, -6.4226F, 1.0F, 6.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, 0.0F, 1.1345F, 0.0F));


		PartDefinition right_hand = up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(232, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 25.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-11.0F, -22.0F, 0.0F));


		PartDefinition bone3 = right_hand.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(238, 54).addBox(-5.0F, 3.0F, -4.0F, 1.0F, 6.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(1.0F, -3.0F, 0.0F));


		bone3.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(242, 36).addBox(-1.9063F, -2.0F, -6.4226F, 1.0F, 6.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 5.0F, -2.0F, 0.0F, -1.1345F, 0.0F));


		right_hand.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(242, 74).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 6.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-3.0F, 2.0F, 3.0F, 0.0F, 1.1345F, 0.0F));

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