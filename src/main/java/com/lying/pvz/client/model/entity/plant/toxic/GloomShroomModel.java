package com.lying.pvz.client.model.entity.plant.toxic;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;


// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。忧郁菇模型：
//     total（根，2 个方块：底座 12×5×12 + 膨胀 0.1F 的内层 12×3×12）
//       └─ head（setPos(0,0,0)，4 个方块：3 层菌盖 + 1 个横向宽片，膨胀 -0.3F / 0.0F / -0.2F）
//            ├─ face（setPos(0,0,0)，2 个眼睛方块）
//            └─ shoot（setPos(0,0,0)，8 个子级 shoot1~shoot8，围绕头部放射状分布）
//                 ├─ shoot1（前，2 个方块）
//                 ├─ shoot2 + cube_r1（左前，Y -45°，2 个方块）
//                 ├─ shoot3 + cube_r2（左，2 个方块）
//                 ├─ shoot4 + cube_r3（左后，Y -45°，2 个方块）
//                 ├─ shoot5（后，2 个方块）
//                 ├─ shoot6 + cube_r3（右后，Y -45°，2 个方块）
//                 ├─ shoot7 + cube_r4（右，2 个方块）
//                 └─ shoot8（右前，Y -45°，2 个方块）
//   setupAnim：空实现，原代码即如此。
//   所有方块使用 CubeDeformation，无 MathHelper 调用。

public class GloomShroomModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("gloom_shroom"), "main");

	private final ModelPart total;

	public GloomShroomModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(46, 28).addBox(-6.0F, -5.0F, -6.0F, 12.0F, 5.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(42, 48).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.1F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = total.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 40).addBox(-6.5F, -12.0F, -6.5F, 13.0F, 7.0F, 13.0F, new CubeDeformation(-0.3F))
						.texOffs(45, 8).addBox(-6.5F, -11.0F, -6.5F, 13.0F, 6.0F, 13.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-5.5F, -11.0F, -7.5F, 11.0F, 6.0F, 15.0F, new CubeDeformation(-0.2F))
						.texOffs(0, 21).addBox(-7.5F, -11.0F, -5.5F, 15.0F, 6.0F, 11.0F, new CubeDeformation(-0.2F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		head.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(0, 4).addBox(-5.0F, -12.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(2.0F, -12.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shoot = head.addOrReplaceChild("shoot",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		shoot.addOrReplaceChild("shoot1",
				CubeListBuilder.create()
						.texOffs(50, 28).addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(41, 45).addBox(-1.5F, -1.5F, -3.25F, 3.0F, 3.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -8.5F, -7.25F));


		PartDefinition shoot2 = shoot.addOrReplaceChild("shoot2",
				CubeListBuilder.create()
						.texOffs(0, 8).addBox(-3.25F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-6.25F, -8.5F, -6.25F, 0.0F, -0.7854F, 0.0F));

		shoot2.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(5, 47).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)),
				PartPose.offsetAndRotation(0.25F, 0.5F, -0.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition shoot3 = shoot.addOrReplaceChild("shoot3",
				CubeListBuilder.create()
						.texOffs(0, 40).addBox(-3.25F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(-7.25F, -8.5F, 0.0F));

		shoot3.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(0, 46).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)),
				PartPose.offsetAndRotation(0.25F, 0.5F, -0.5F, 0.0F, 1.5708F, 0.0F));

		shoot.addOrReplaceChild("shoot4",
				CubeListBuilder.create()
						.texOffs(56, 4).addBox(-0.5F, -0.5F, -1.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(43, 0).addBox(-1.5F, -1.5F, 1.25F, 3.0F, 3.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-6.25F, -8.5F, 6.25F, 0.0F, -0.7854F, 0.0F));


		shoot.addOrReplaceChild("shoot5",
				CubeListBuilder.create()
						.texOffs(56, 0).addBox(-0.5F, -0.5F, -1.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(43, 5).addBox(-1.5F, -1.5F, 1.25F, 3.0F, 3.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -8.5F, 7.25F));

		PartDefinition shoot6 = shoot.addOrReplaceChild("shoot6",
				CubeListBuilder.create()
						.texOffs(0, 27).addBox(1.25F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(6.25F, -8.5F, 6.25F, 0.0F, -0.7854F, 0.0F));

		shoot6.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(45, 27).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)),
				PartPose.offsetAndRotation(0.75F, 0.5F, -0.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition shoot7 = shoot.addOrReplaceChild("shoot7",
				CubeListBuilder.create()
						.texOffs(0, 21).addBox(1.25F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(7.25F, -8.5F, 0.0F));

		shoot7.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(7, 11).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)),
				PartPose.offsetAndRotation(0.75F, 0.5F, -0.5F, 0.0F, 1.5708F, 0.0F));


		shoot.addOrReplaceChild("shoot8",
				CubeListBuilder.create()
						.texOffs(51, 2).addBox(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(43, 10).addBox(-1.5F, -1.5F, -3.25F, 3.0F, 3.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(6.25F, -8.5F, -6.25F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

}