package com.lying.pvz.client.model.entity.plant.assist;

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


// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

public class GoldMagnetModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("gold_magnet"), "main");

	private final ModelPart total;

	public GoldMagnetModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 52).addBox(-0.9443F, -8.2791F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.28F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition plate = total.addOrReplaceChild("plate",
				CubeListBuilder.create()
						.texOffs(8, 59).addBox(-2.0F, -1.5F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(-0.3F)),
				PartPose.offset(0.0F, -7.0F, 0.0F));


		PartDefinition bone3 = plate.addOrReplaceChild("bone3",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		bone3.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(9, 56).addBox(-0.25F, -1.2165F, 0.125F, 4.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(-1.75F, -0.1402F, 1.5902F, 0.5236F, 0.0F, 0.0F));

		plate.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(9, 53).addBox(-2.0F, -0.5428F, -2.5722F, 4.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 1.5708F, 0.0F));

		plate.addOrReplaceChild("bone15",
				CubeListBuilder.create()
						.texOffs(53, 37).addBox(-2.75F, -0.75F, -1.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(-1.75F, -0.75F, -1.0F, 0.5236F, -0.5236F, -0.5236F));
		plate.addOrReplaceChild("bone16",
				CubeListBuilder.create()
						.texOffs(42, 37).addBox(-0.25F, -0.75F, -1.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(1.75F, -0.75F, -1.0F, 0.5236F, 0.5236F, 0.5236F));
		plate.addOrReplaceChild("bone17",
				CubeListBuilder.create()
						.texOffs(32, 37).addBox(-2.75F, -0.75F, -0.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(-1.75F, -0.75F, 1.0F, -0.5236F, 0.5236F, -0.5236F));
		plate.addOrReplaceChild("bone18",
				CubeListBuilder.create()
						.texOffs(24, 35).addBox(-0.25F, -0.75F, -0.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(1.75F, -0.75F, 1.0F, -0.5236F, -0.5236F, 0.5236F));
		plate.addOrReplaceChild("bone19",
				CubeListBuilder.create()
						.texOffs(53, 37).addBox(-2.75F, -0.75F, -1.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(-1.75F, -0.75F, 0.5F, 0.0F, 0.0F, 0.3927F));
		plate.addOrReplaceChild("bone20",
				CubeListBuilder.create()
						.texOffs(42, 37).addBox(-0.25F, -0.75F, -1.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(1.75F, -0.75F, 0.5F, 0.0F, 0.0F, -0.3927F));

		PartDefinition magnet = total.addOrReplaceChild("magnet",
				CubeListBuilder.create()
						.texOffs(25, 57).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, -8.75F, 0.0F));

		magnet.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(38, 58).addBox(0.4575F, -1.4755F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));
		magnet.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(20, 50).addBox(-2.4575F, -1.4755F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		magnet.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(51, 52).addBox(0.1585F, -0.0425F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.0472F));
		magnet.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(38, 51).addBox(-3.1585F, -0.0425F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0472F));
		magnet.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(31, 47).addBox(-4.8246F, 0.3592F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.309F));
		magnet.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(10, 45).addBox(-4.4373F, -7.364F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		magnet.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(51, 44).addBox(1.4373F, -7.364F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		magnet.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(1, 41).addBox(2.8246F, 0.3592F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.309F));
		magnet.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(40, 41).addBox(-5.609F, -8.0288F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));
		magnet.addOrReplaceChild("bone14",
				CubeListBuilder.create()
						.texOffs(21, 42).addBox(2.609F, -8.0288F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition face = total.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(11, 40).addBox(-2.5F, 0.0F, -1.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(33, 40).addBox(0.5F, 0.0F, -1.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, -11.0F, 0.0F));

		face.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(57, 41).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(1.75F, -0.5F, -1.75F, 0.0F, 0.0F, 0.1745F));
		face.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(19, 39).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.75F, -0.5F, -1.75F, 0.0F, 0.0F, -0.1745F));


		total.addOrReplaceChild("elect",
				CubeListBuilder.create()
						.texOffs(1, 38).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, -16.25F, 0.0F));

		PartDefinition base = total.addOrReplaceChild("base",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		base.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(56, 58).addBox(-1.171F, -3.2198F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
		base.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-1.2479F, -3.7826F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(0.75F, -2.25F, 0.0F, 0.0F, 0.0F, -0.1309F));
		base.addOrReplaceChild("leave1",
				CubeListBuilder.create()
						.texOffs(20, 56).addBox(-0.25F, -0.75F, -1.75F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(0.75F, -2.25F, 0.0F, -0.0873F, 0.0F, -0.1309F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

}