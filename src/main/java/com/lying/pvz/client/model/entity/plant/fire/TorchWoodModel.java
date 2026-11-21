package com.lying.pvz.client.model.entity.plant.fire;


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


public class TorchWoodModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("torch_wood"), "main");

	private final ModelPart total;

	public TorchWoodModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(48, 11).addBox(-7.0F, -9.0F, -7.0F, 14.0F, 2.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(0, 25).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 3.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-7.5F, -10.0F, -7.5F, 15.0F, 10.0F, 15.0F, CubeDeformation.NONE)
						.texOffs(0, 44).addBox(-7.0F, -12.0F, -7.0F, 14.0F, 12.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}