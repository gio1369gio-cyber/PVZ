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


public class ConeHeadModel extends HumanoidModel<HumanoidRenderState> {
	public static final ModelLayerLocation CONE_HEAD_LAYER = new ModelLayerLocation(StringUtil.id("cone_head"), "main");

	private final ModelPart cone;

	public ConeHeadModel(ModelPart root) {
		super(root);
		this.head.skipDraw = true;
		this.hat.skipDraw = true;

		this.body.skipDraw     = true;
		this.rightArm.skipDraw = true;
		this.leftArm.skipDraw  = true;
		this.rightLeg.skipDraw = true;
		this.leftLeg.skipDraw  = true;

		this.cone = this.head.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = mesh.getRoot();
		PartDefinition head = root.getChild("head");

		head.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(24, 24).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(24, 27).addBox(-6.0F, 0.0F, 4.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(24, 30).addBox(4.0F, 0.0F, -4.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(32, 13).addBox(-6.0F, 0.0F, -4.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 13).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 24).addBox(-3.0F, -9.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 33).addBox(-2.0F, -12.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 33).addBox(-1.0F, -15.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}
}