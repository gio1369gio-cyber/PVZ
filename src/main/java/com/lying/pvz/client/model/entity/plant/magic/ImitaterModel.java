package com.lying.pvz.client.model.entity.plant.magic;


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


// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。模仿者（土豆地雷变体）模型：
//     total
//       ├─ body（3 个方块：头部 + 中段 + 底部，膨胀 0.4/0.2/0.0）
//       ├─ head（1 个方块 + cube_r1/cube_r2 两根 0.1 膨胀的"头发"）
//       ├─ hands（4 只手 hand1~hand4，各自 Z 轴不同角度，膨胀 -0.25）
//       └─ face（4 个方块 + cube_r3 两只嘴角，膨胀 -0.45 / -0.25）
//
//   setupAnim：total.yRot = ageInTicks / 3 —— 整体绕 Y 轴旋转（约 1.88 秒一圈）
//   1.16.5 原样，保留。

public class ImitaterModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("imitater"), "main");

	private final ModelPart total;

	public ImitaterModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 53).addBox(-4.5F, -17.0F, -3.0F, 9.0F, 5.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(30, 52).addBox(-4.5F, -6.25F, -3.0F, 9.0F, 6.0F, 6.0F, new CubeDeformation(0.4F))
						.texOffs(0, 41).addBox(-4.5F, -12.25F, -3.0F, 9.0F, 6.0F, 6.0F, new CubeDeformation(0.2F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = total.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 57).addBox(-1.0F, -2.25F, 0.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -17.0F, 0.0F));

		head.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(48, 35).addBox(-4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.5F, -0.75F, -1.5F, 0.0F, 0.0F, -0.1309F));

		head.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(32, 43).addBox(-4.0F, -1.0F, -2.0F, 9.0F, 2.0F, 7.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(-0.75F, -0.5F, -1.5F, 0.0F, 0.0F, -0.1309F));


		PartDefinition hands = total.addOrReplaceChild("hands",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		hands.addOrReplaceChild("hand1",
				CubeListBuilder.create()
						.texOffs(56, 56).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(-4.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

		hands.addOrReplaceChild("hand2",
				CubeListBuilder.create()
						.texOffs(56, 54).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 0.0F, 0.48F, -0.1745F));

		hands.addOrReplaceChild("hand3",
				CubeListBuilder.create()
						.texOffs(56, 52).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(4.0F, -3.0F, 0.0F, 0.0F, -2.6616F, 0.6981F));


		hands.addOrReplaceChild("hand4",
				CubeListBuilder.create()
						.texOffs(28, 56).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(3.0F, -14.0F, 2.0F, 0.0F, 2.7053F, 0.6981F));


		PartDefinition face = total.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(31, 46).addBox(-3.5F, 2.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(-0.45F))
						.texOffs(32, 53).addBox(-2.5F, 3.0F, -3.85F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
						.texOffs(32, 53).addBox(1.5F, 3.0F, -3.85F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
						.texOffs(24, 43).addBox(0.5F, 2.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(-0.45F)),
				PartPose.offset(0.0F, -18.0F, 0.5F));

		face.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(0, 54).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.45F))
						.texOffs(32, 53).addBox(-4.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.45F)),
				PartPose.offsetAndRotation(2.0F, 4.5F, -3.5F, -0.0436F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		this.total.yRot = state.ageInTicks / 3;
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}