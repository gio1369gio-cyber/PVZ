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


// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports

public class FlowerPotModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("flower_pot"), "main");

	private final ModelPart total;

	public FlowerPotModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 53).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 50).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 2.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(0, 47).addBox(-7.0F, -6.0F, -7.0F, 14.0F, 3.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -12.0F, -8.0F, 16.0F, 6.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 48).addBox(-8.0F, -13.0F, -8.0F, 1.0F, 1.0F, 15.0F, CubeDeformation.NONE)
						.texOffs(2, 31).addBox(7.0F, -13.0F, -8.0F, 1.0F, 1.0F, 15.0F, CubeDeformation.NONE)
						.texOffs(1, 32).addBox(-7.0F, -13.0F, -8.0F, 14.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(2, 36).addBox(-8.0F, -13.0F, 7.0F, 16.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(55, 48).addBox(-1.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0F, -11.5F, 5.5F, -0.1309F, 0.0F, -0.3054F));


		body.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(55, 55).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.6109F));


		body.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(55, 44).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(-0.25F, -3.75F, 0.0F, 0.0F, 0.0F, 0.6545F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}