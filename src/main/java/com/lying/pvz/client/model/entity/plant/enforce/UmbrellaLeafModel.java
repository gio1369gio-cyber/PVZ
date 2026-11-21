package com.lying.pvz.client.model.entity.plant.enforce;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
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
//   1. 几何 1:1 搬运，全部 -0.4 膨胀原样写成 new CubeDeformation(-0.4F)。
//   2. 结构：total
//              ├─ body     （4 个方块：根茎 + 三段伞柱）
//              ├─ leaves   （纯容器，挂 7 片叶，每片叶 4 个 cube_r）
//              └─ face     （2 只眼睛）
//   3. leaves 下 7 片叶的几何"看起来"是同一片旋转复制，但 leaf3 的 cube_r9..r12 的 addBox
//      参数被 1.16.5 作者手工微调过（x/y/z 有 0.01~0.87 的补偿差值），
//      所以**不能**用循环或复制粘贴统一生成，必须逐件照抄数值。leaf1/2/4/5/6/7 的
//      cube 参数虽一致，仍按原码逐件展开，方便日后对照 diff。
//   4. setupAnim 在 1.16.5 就是空实现，迁移后保持空实现。

public class UmbrellaLeafModel extends PvZPlantModel {


	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("umbrella_leaf"), "main");

	private final ModelPart total;

	public UmbrellaLeafModel(ModelPart root) {
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
						.texOffs(52, 58).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(21, 46).addBox(-2.5F, -6.0F, -2.5F, 5.0F, 4.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(0, 50).addBox(-3.5F, -13.0F, -3.5F, 7.0F, 7.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(28, 55).addBox(-4.0F, -14.0F, -4.0F, 8.0F, 1.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition leaves = total.addOrReplaceChild("leaves",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -11.0F, -4.1F));

		PartDefinition leaf1 = leaves.addOrReplaceChild("leaf1",
				CubeListBuilder.create(),
				PartPose.offset(-0.5F, -3.0F, 4.1F));


		leaf1.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(36, 47).addBox(-1.5F, -1.1F, -12.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.2608F, -5.47F, 0.1309F, 0.0F, 0.0F));


		leaf1.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(-2.5F, -1.1F, -10.0F, 9.0F, 1.0F, 10.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.6544F, -5.2564F, 0.0873F, 0.0F, 0.0F));

		leaf1.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(41, 52).addBox(-1.5F, -1.0F, -1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.7075F, -5.26F, -0.2182F, 0.0F, 0.0F));

		leaf1.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(8, 47).addBox(-1.5F, 0.1F, -2.5F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 48).addBox(-0.5F, 0.1F, -1.3F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.5F, -2.4F, -0.3491F, 0.0F, 0.0F));

		PartDefinition leaf2 = leaves.addOrReplaceChild("leaf2",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -3.0F, 3.1F, -0.0873F, -0.9599F, 0.0F));

		leaf2.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(36, 47).addBox(-1.5F, -1.1F, -12.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.2608F, -5.47F, 0.1309F, 0.0F, 0.0F));

		leaf2.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(-2.5F, -1.1F, -10.0F, 9.0F, 1.0F, 10.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.6544F, -5.2564F, 0.0873F, 0.0F, 0.0F));

		leaf2.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(41, 52).addBox(-1.5F, -1.0F, -1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.7075F, -5.26F, -0.2182F, 0.0F, 0.0F));

		leaf2.addOrReplaceChild("cube_r8",
				CubeListBuilder.create()
						.texOffs(8, 47).addBox(-1.5F, 0.1F, -2.5F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 48).addBox(-0.5F, 0.1F, -1.3F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.5F, -2.4F, -0.3491F, 0.0F, 0.0F));

		PartDefinition leaf3 = leaves.addOrReplaceChild("leaf3",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-0.5F, -3.0F, 5.6F, -0.0873F, 0.9599F, 0.0F));

		leaf3.addOrReplaceChild("cube_r9",
				CubeListBuilder.create()
						.texOffs(36, 47).addBox(0.0155F, -1.1382F, -12.8742F, 5.0F, 1.0F, 3.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.2608F, -5.47F, 0.1309F, 0.0F, 0.0F));

		leaf3.addOrReplaceChild("cube_r10",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(-0.9845F, -1.1F, -10.875F, 9.0F, 1.0F, 10.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.6544F, -5.2564F, 0.0873F, 0.0F, 0.0F));

		leaf3.addOrReplaceChild("cube_r11",
				CubeListBuilder.create()
						.texOffs(41, 52).addBox(0.0155F, -0.7369F, -1.8345F, 7.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.7075F, -5.26F, -0.2182F, 0.0F, 0.0F));

		leaf3.addOrReplaceChild("cube_r12",
				CubeListBuilder.create()
						.texOffs(8, 47).addBox(0.0155F, 0.4698F, -3.293F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 48).addBox(1.0155F, 0.4698F, -2.093F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.5F, -2.4F, -0.3491F, 0.0F, 0.0F));


		PartDefinition leaf4 = leaves.addOrReplaceChild("leaf4",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.25F, -3.0F, 4.1F, 0.0F, 1.9635F, 0.0F));

		leaf4.addOrReplaceChild("cube_r13",
				CubeListBuilder.create()
						.texOffs(36, 47).addBox(-1.5F, -1.1F, -12.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.2608F, -5.47F, 0.1309F, 0.0F, 0.0F));

		leaf4.addOrReplaceChild("cube_r14",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(-2.5F, -1.1F, -10.0F, 9.0F, 1.0F, 10.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.6544F, -5.2564F, 0.0873F, 0.0F, 0.0F));

		leaf4.addOrReplaceChild("cube_r15",
				CubeListBuilder.create()
						.texOffs(41, 52).addBox(-1.5F, -1.0F, -1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.7075F, -5.26F, -0.2182F, 0.0F, 0.0F));

		leaf4.addOrReplaceChild("cube_r16",
				CubeListBuilder.create()
						.texOffs(8, 47).addBox(-1.5F, 0.1F, -2.5F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 48).addBox(-0.5F, 0.1F, -1.3F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.5F, -2.4F, -0.3491F, 0.0F, 0.0F));


		PartDefinition leaf5 = leaves.addOrReplaceChild("leaf5",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -3.0F, 3.35F, 0.0F, -1.9635F, 0.0F));

		leaf5.addOrReplaceChild("cube_r17",
				CubeListBuilder.create()
						.texOffs(36, 47).addBox(-1.5F, -1.1F, -12.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.2608F, -5.47F, 0.1309F, 0.0F, 0.0F));

		leaf5.addOrReplaceChild("cube_r18",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(-2.5F, -1.1F, -10.0F, 9.0F, 1.0F, 10.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.6544F, -5.2564F, 0.0873F, 0.0F, 0.0F));

		leaf5.addOrReplaceChild("cube_r19",
				CubeListBuilder.create()
						.texOffs(41, 52).addBox(-1.5F, -1.0F, -1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.7075F, -5.26F, -0.2182F, 0.0F, 0.0F));

		leaf5.addOrReplaceChild("cube_r20",
				CubeListBuilder.create()
						.texOffs(8, 47).addBox(-1.5F, 0.1F, -2.5F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 48).addBox(-0.5F, 0.1F, -1.3F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.5F, -2.4F, -0.3491F, 0.0F, 0.0F));


		PartDefinition leaf6 = leaves.addOrReplaceChild("leaf6",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -3.0F, 4.6F, -0.0873F, 2.7053F, 0.0F));

		leaf6.addOrReplaceChild("cube_r21",
				CubeListBuilder.create()
						.texOffs(36, 47).addBox(-1.5F, -1.1F, -12.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.2608F, -5.47F, 0.1309F, 0.0F, 0.0F));

		leaf6.addOrReplaceChild("cube_r22",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(-2.5F, -1.1F, -10.0F, 9.0F, 1.0F, 10.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.6544F, -5.2564F, 0.0873F, 0.0F, 0.0F));

		leaf6.addOrReplaceChild("cube_r23",
				CubeListBuilder.create()
						.texOffs(41, 52).addBox(-1.5F, -1.0F, -1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.7075F, -5.26F, -0.2182F, 0.0F, 0.0F));

		leaf6.addOrReplaceChild("cube_r24",
				CubeListBuilder.create()
						.texOffs(8, 47).addBox(-1.5F, 0.1F, -2.5F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 48).addBox(-0.5F, 0.1F, -1.3F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.5F, -2.4F, -0.3491F, 0.0F, 0.0F));

		PartDefinition leaf7 = leaves.addOrReplaceChild("leaf7",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.75F, -3.0F, 4.1F, -0.0873F, -2.7053F, 0.0F));

		leaf7.addOrReplaceChild("cube_r25",
				CubeListBuilder.create()
						.texOffs(36, 47).addBox(-1.5F, -1.1F, -12.0F, 5.0F, 1.0F, 3.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.2608F, -5.47F, 0.1309F, 0.0F, 0.0F));

		leaf7.addOrReplaceChild("cube_r26",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(-2.5F, -1.1F, -10.0F, 9.0F, 1.0F, 10.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.6544F, -5.2564F, 0.0873F, 0.0F, 0.0F));

		leaf7.addOrReplaceChild("cube_r27",
				CubeListBuilder.create()
						.texOffs(41, 52).addBox(-1.5F, -1.0F, -1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.5F, 0.7075F, -5.26F, -0.2182F, 0.0F, 0.0F));

		leaf7.addOrReplaceChild("cube_r28",
				CubeListBuilder.create()
						.texOffs(8, 47).addBox(-1.5F, 0.1F, -2.5F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 48).addBox(-0.5F, 0.1F, -1.3F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.5F, 0.5F, -2.4F, -0.3491F, 0.0F, 0.0F));

		total.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(28, 55).addBox(1.25F, -11.75F, -3.92F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(28, 59).addBox(-3.25F, -11.75F, -3.92F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}