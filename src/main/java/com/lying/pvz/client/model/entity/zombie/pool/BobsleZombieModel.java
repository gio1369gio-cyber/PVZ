package com.lying.pvz.client.model.entity.zombie.pool;


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
//   几何 1:1 搬运。雪橇僵尸模型（普通僵尸的雪橇版，无额外装备）：
//     total（根，无方块）
//       ├─ right_leg（setPos(-4,-25,0)，2 个方块：腿 + 脚）
//       ├─ left_leg（setPos(4,-25,0)，2 个方块：腿 + 脚）
//       └─ up（setPos(0,-25,0)）
//            ├─ body（1 个躯干方块）
//            ├─ left_hand（setPos(10,-21,0)，1 个手方块）
//            ├─ right_hand（setPos(-10,-21,0)，1 个手方块）
//            └─ head（setPos(0,-23,0)，11 个方块：头 + 4 条发丝 + 左右鬓角 + 下巴三层）
//   setupAnim：全部继承 PVZZombieModel，本类不重写。
//   所有方块 CubeDeformation.NONE，无 MathHelper 调用。
//   与 NormalZombieModel 的差异：腿位置 -25（vs -24），头位置 -23（vs -24），
//   头部有 11 个方块（vs 1 个），手部偏移 ±10（vs ±12）。

public class BobsleZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("bobsle_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;

	public BobsleZombieModel(ModelPart root) {
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
						.texOffs(184, 229).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 21.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(149, 238).addBox(-3.0F, 21.0F, -6.0F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -25.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(238, 228).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 21.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(203, 240).addBox(-3.0F, 21.0F, -6.0F, 6.0F, 4.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -25.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -25.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(5, 224).addBox(-8.0F, -23.0F, -3.0F, 16.0F, 23.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(54, 227).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(10.0F, -21.0F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(77, 224).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 23.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-10.0F, -21.0F, 0.0F));


		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(3, 192).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(109, 234).addBox(-7.0F, -6.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(59, 199).addBox(-7.0F, -10.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(100, 201).addBox(6.0F, -6.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(142, 200).addBox(6.0F, -10.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(187, 214).addBox(-6.0F, -6.0F, 6.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(222, 217).addBox(-6.0F, -10.0F, 6.0F, 12.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(181, 192).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(222, 198).addBox(-5.0F, -11.0F, -7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(134, 187).addBox(-5.0F, -5.0F, -7.0F, 10.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -23.0F, 0.0F));

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