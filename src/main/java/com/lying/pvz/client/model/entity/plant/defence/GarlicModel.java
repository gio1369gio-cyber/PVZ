package com.lying.pvz.client.model.entity.plant.defence;


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


public class GarlicModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("garlic"), "main");

	private final ModelPart total;

	public GarlicModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(-4.0F, 18.0F, -5.0F));


		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(0.0F, 5.0F, 1.0F, 8.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(0, 24).addBox(-1.0F, 4.0F, 0.0F, 10.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 44).addBox(-1.0F, -4.0F, -1.0F, 10.0F, 8.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(46, 57).addBox(0.0F, -3.0F, -1.75F, 8.0F, 6.0F, 1.0F, new CubeDeformation(-0.1F))
						.texOffs(46, 50).addBox(0.0F, -3.0F, 11.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(-0.1F))
						.texOffs(42, 32).addBox(-2.0F, -4.0F, 0.0F, 1.0F, 8.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(34, 28).addBox(-2.75F, -3.0F, 1.0F, 1.0F, 6.0F, 8.0F, new CubeDeformation(-0.1F))
						.texOffs(46, 18).addBox(9.75F, -3.0F, 1.0F, 1.0F, 6.0F, 8.0F, new CubeDeformation(-0.1F))
						.texOffs(42, 0).addBox(9.0F, -4.0F, 0.0F, 1.0F, 8.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 13).addBox(0.0F, -5.0F, 0.0F, 8.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(32, 49).addBox(0.0F, -6.0F, 2.0F, 1.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(24, 34).addBox(-1.0F, -5.0F, 1.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(28, 14).addBox(8.0F, -5.0F, 1.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(40, 19).addBox(7.0F, -6.0F, 2.0F, 1.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(1.0F, -6.0F, 1.0F, 6.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(32, 0).addBox(2.0F, -7.0F, 2.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(58, 39).addBox(5.0F, -8.0F, 4.0F, 1.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(58, 36).addBox(2.0F, -8.0F, 4.0F, 1.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 51).addBox(3.0F, -8.0F, 3.0F, 2.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 46).addBox(1.0F, -7.0F, 3.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(32, 44).addBox(6.0F, -7.0F, 3.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition hair = body.addOrReplaceChild("hair",
				CubeListBuilder.create()
						.texOffs(56, 18).addBox(3.0F, -5.0F, 4.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		hair.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(60, 20).addBox(0.0F, -5.0F, -1.0F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(3.0F, -4.0F, 5.0F, -0.8727F, 0.0F, 0.3927F));

		hair.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(54, 32).addBox(0.0F, -4.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(3.25F, -4.25F, 6.0F, 0.5672F, 0.0F, -0.7854F));

		hair.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(54, 37).addBox(0.0F, -5.0F, -1.0F, 1.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(3.5F, -4.0F, 6.0F, 0.5672F, 0.0F, 0.3927F));

		PartDefinition face = total.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(0, 39).addBox(0.5F, -2.75F, -2.1F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 25).addBox(5.5F, -2.75F, -2.1F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 10).addBox(1.5F, 0.0F, -2.1F, 5.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(0, 18).addBox(1.25F, 0.25F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F))
						.texOffs(0, 15).addBox(2.25F, 0.25F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		face.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(0, 10).addBox(-1.5F, -0.5F, 0.15F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(7.1871F, 0.4045F, -2.25F, 0.0F, 0.0F, -0.0873F));

		face.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(0, 10).addBox(-1.5F, -0.5F, 0.15F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(1.8091F, 0.4917F, -2.25F, 0.0F, 0.0F, 0.0873F));

		face.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(0, 30).addBox(-2.75F, -0.7F, 0.4F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(2.5F, -3.5F, -1.75F, 0.0F, 0.0F, -0.2182F));

		face.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(0, 35).addBox(-0.25F, -0.75F, 0.4F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(5.25F, -3.5F, -1.75F, 0.0F, 0.0F, 0.2182F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}