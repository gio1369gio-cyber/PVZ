package com.lying.pvz.client.model.entity.plant.toxic;

import java.util.Optional;

import com.lying.pvz.client.model.entity.plant.PlantShooterModel;
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
//   几何 1:1 搬运。胆小菇模型：
//     total（根，1 个底座方块）
//       └─ body（setPos(0,-1,0)，1 个身体方块，膨胀 -0.1F）
//            └─ head（setPos(0,-16,0)，4 个方块：嘴 2 层 + 菌盖 + 下沿）
//                 └─ hat（setPos(0,-9,-1) + X -35°，1 个 12×12×13 蘑菇帽）
//   setupAnim：继承 PlantShooterModel，通过 getHeadModel() / getBodyModel() 驱动头部/身体摆动。
//   1.16.5: Optional<ModelRenderer> / 1.21.1: Optional<ModelPart>。
//   无 MathHelper 调用。

public class ScaredyShroomModel extends PlantShooterModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("scaredy_shroom"), "main");

	private final ModelPart total;

	private final ModelPart body;
	private final ModelPart head;

	public ScaredyShroomModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.body = this.total.getChild("body");
		this.head = this.body.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(17, 55).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 40).addBox(-2.0F, -17.25F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(-0.1F)),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(52, 2).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(1, 3).addBox(-2.0F, -5.0F, -7.0F, 4.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(31, 33).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(17, 55).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -16.0F, 0.0F));


		head.addOrReplaceChild("hat",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-6.0F, -10.1161F, -3.4601F, 12.0F, 12.0F, 13.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -9.0F, -1.0F, -0.6109F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public Optional<ModelPart> getHeadModel() {
		return Optional.ofNullable(this.head);
	}

	@Override
	public Optional<ModelPart> getBodyModel() {
		return Optional.ofNullable(this.body);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}