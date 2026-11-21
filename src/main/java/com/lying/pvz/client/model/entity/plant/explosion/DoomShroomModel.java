package com.lying.pvz.client.model.entity.plant.explosion;


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




public class DoomShroomModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			StringUtil.id("doom_shroom"), "main");

	private final ModelPart total;

	public DoomShroomModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(69, 89).addBox(-5.0F, -12.0F, -5.0F, 10.0F, 12.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(4, 70).addBox(-6.0F, -8.0F, -5.0F, 12.0F, 2.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(3, 88).addBox(-7.0F, -6.0F, -6.0F, 14.0F, 3.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(1, 108).addBox(-8.0F, -3.0F, -7.0F, 16.0F, 3.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(69, 118).addBox(-4.0F, -9.0F, -3.0F, 8.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -12.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}



	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}