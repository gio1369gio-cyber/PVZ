package com.lying.pvz.client.model.entity.plant.spear;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
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

// Made with Blockbench 4.1.3
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。猫尾草模型：
//     bone（根，无方块）
//       ├─ buttom（4 方块，底座片，对称排布）
//       ├─ head（3 方块 + hat_r1 帽子 + left_ear/right_ear 子树）
//       │   ├─ hat_r1（setPos(0,-6,-1) + X -15°，1 个 10×2×10 帽子）
//       │   ├─ left_ear（in_r1 2 方块 + lear_r1 1 方块）
//       │   └─ right_ear（in_r2 2 方块 + rear_r1 1 方块）
//       └─ tail（3 个旋转子件：spike_r1 / sec_r1 / fir_r1 组成猫尾）
//
//   setupAnim：tail.xRot 由 entity.getAnimCD() 与 getAttackTime() 驱动
//     —— (0.8 - 0.8*|cos(v*tick)|) 是经典"甩尾"曲线，v = π / animCD
//     与 StrangeCat / CobCannon 的甩尾曲线同款，1:1 保留。
//
//   MathHelper → Mth：cos 方法不变，仅类名变化。

public class CatTailModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("cat_tail"), "main");

	private final ModelPart bone;
	private final ModelPart tail;

	public CatTailModel(ModelPart root) {
		super(root);
		this.bone = root.getChild("bone");
		this.tail = this.bone.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition bone = root.addOrReplaceChild("bone",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		bone.addOrReplaceChild("buttom",
				CubeListBuilder.create()
						.texOffs(24, 12).addBox(-6.0F, -2.0F, -8.0F, 6.0F, 2.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(26, 29).addBox(0.0F, -2.0F, 1.0F, 5.0F, 2.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(0, 28).addBox(1.0F, -1.0F, -7.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(26, 22).addBox(-7.0F, -1.0F, 0.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition head = bone.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 12).addBox(-4.0F, -9.0F, -5.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(32, 0).addBox(-2.0F, -3.0F, -5.5F, 4.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 7).addBox(-1.0F, -3.05F, -5.7F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		head.addOrReplaceChild("hat_r1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 2.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -6.0F, -1.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition left_ear = head.addOrReplaceChild("left_ear",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -6.0F, -1.0F));

		left_ear.addOrReplaceChild("in_r1",
				CubeListBuilder.create()
						.texOffs(44, 18).addBox(-1.4F, -1.9F, -6.6F, 3.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(43, 6).addBox(-1.3F, -1.9F, -6.5F, 3.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.5F, -4.5F, 2.5F, 0.1745F, 0.0F, 0.1745F));

		left_ear.addOrReplaceChild("lear_r1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(3.0F, -5.0F, -2.0F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.5236F, 0.0F));


		PartDefinition right_ear = head.addOrReplaceChild("right_ear",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -6.0F, -1.0F));

		right_ear.addOrReplaceChild("in_r2",
				CubeListBuilder.create()
						.texOffs(42, 12).addBox(-1.6F, -1.9F, -6.6F, 3.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(42, 0).addBox(-1.7F, -1.9F, -6.5F, 3.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.5F, -4.5F, 2.5F, 0.1745F, 0.0F, -0.1745F));

		right_ear.addOrReplaceChild("rear_r1",
				CubeListBuilder.create()
						.texOffs(32, 36).addBox(-5.0F, -5.0F, -2.0F, 2.0F, 4.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, -0.5236F, 0.0F));


		PartDefinition tail = bone.addOrReplaceChild("tail",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -1.5F, 3.0F));

		tail.addOrReplaceChild("spike_r1",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(-1.0F, 1.18F, 6.05F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(15, 28).addBox(-0.5F, 1.68F, 3.05F, 1.0F, 1.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -2.18F, 3.95F, 2.2253F, 0.0F, 0.0F));

		tail.addOrReplaceChild("sec_r1",
				CubeListBuilder.create()
						.texOffs(30, 4).addBox(-0.5F, 0.39F, 0.95F, 1.0F, 1.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -0.59F, 2.05F, 1.4399F, 0.0F, 0.0F));

		tail.addOrReplaceChild("fir_r1",
				CubeListBuilder.create()
						.texOffs(9, 35).addBox(-0.5F, -2.0F, 3.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 1.5F, -3.0F, 0.2618F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		int tick = state.attackTime;
		float v = 3.14159F / state.animCD;

		this.tail.xRot = 0.8F - 0.8F * Math.abs(Mth.cos(v * tick));
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.bone;
	}
}