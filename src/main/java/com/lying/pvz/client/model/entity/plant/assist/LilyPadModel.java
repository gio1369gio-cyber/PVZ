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

public class LilyPadModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("lily_pad"), "main");

	private final ModelPart total;

	public LilyPadModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(23, 46).addBox(6.0F, 7.0F, -16.0F, 4.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(31, 30).addBox(10.0F, 7.0F, -15.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(31, 14).addBox(4.0F, 7.0F, -15.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(37, 1).addBox(12.0F, 7.0F, -14.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(1, 1).addBox(0.0F, 7.0F, -10.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(13, 1).addBox(1.0F, 7.0F, -12.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(11, 11).addBox(2.0F, 7.0F, -13.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(2, 24).addBox(3.0F, 7.0F, -14.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(3, 38).addBox(13.0F, 7.0F, -13.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(2, 50).addBox(14.0F, 7.0F, -12.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(1, 12).addBox(15.0F, 7.0F, -10.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-8.0F, 16.0F, 8.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}