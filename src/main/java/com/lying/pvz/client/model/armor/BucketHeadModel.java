package com.lying.pvz.client.model.armor;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;


public class BucketHeadModel extends HumanoidModel<HumanoidRenderState> {
	public static final ModelLayerLocation BUCKET_HEAD_LAYER = new ModelLayerLocation(StringUtil.id("bucket_head"), "main");

	private final ModelPart total;
	private final ModelPart pole;

	public BucketHeadModel(ModelPart root) {
		super(root);
		this.head.skipDraw = true;
		this.hat.skipDraw = true;
		this.body.skipDraw     = true;
		this.rightArm.skipDraw = true;
		this.leftArm.skipDraw  = true;
		this.rightLeg.skipDraw = true;
		this.leftLeg.skipDraw  = true;

		this.total = this.head.getChild("total");
		this.pole = this.total.getChild("pole");

	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh =  HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = mesh.getRoot();
		PartDefinition head = root.getChild("head");

		PartDefinition total = head.addOrReplaceChild("total", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 20).addBox(-5.0F, 0.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 20).addBox(4.0F, 0.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(22, 33).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(22, 35).addBox(-4.0F, 0.0F, 4.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		 total.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(22, 20).addBox(5.0F, -1.0F, -8.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 31).addBox(-6.0F, -1.0F, -8.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(22, 31).addBox(-5.0F, -1.0F, -8.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0472F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

}