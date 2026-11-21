package com.lying.pvz.client.model.entity.plant.appease;

import java.util.Optional;

import com.lying.pvz.client.model.entity.plant.PlantShooterModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
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

public class SplitPeaModel extends PlantShooterModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("split_pea"), "main");

	private final ModelPart total;
	private final ModelPart body;
	private final ModelPart head;

	public SplitPeaModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.body = this.total.getChild("body");
		this.head = this.body.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 31).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 12.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));


		PartDefinition head = body.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(40, 8).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0873F, 0.0F, 0.0F));


		PartDefinition back = head.addOrReplaceChild("back",
				CubeListBuilder.create()
						.texOffs(30, 0).addBox(-2.0F, -1.0F, -0.7F, 4.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(10, 32).addBox(-5.0F, -5.0F, 1.0F, 10.0F, 1.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -2.6F, 8.2F));

		back.addOrReplaceChild("mouth_r1",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-3.0F, -3.0F, -1.0F, 6.0F, 6.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 1.0F, 3.2F, 0.0F, 3.1416F, 0.0F));

		back.addOrReplaceChild("head_r1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, -4.0F, 0.0F, 3.1416F, 0.0F));

		head.addOrReplaceChild("front",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.0F, -22.0F, -9.0F, 10.0F, 10.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(30, 0).addBox(-2.0F, -17.0F, -12.0F, 4.0F, 4.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(44, 0).addBox(-3.0F, -18.0F, -13.0F, 6.0F, 6.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 12.0F, 0.0F));


		PartDefinition down = total.addOrReplaceChild("down",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		down.addOrReplaceChild("n_r1",
				CubeListBuilder.create()
						.texOffs(4, 20).addBox(-2.0F, -1.0F, -7.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, -0.1745F, -0.7854F, 0.0F));

		down.addOrReplaceChild("w_r1",
				CubeListBuilder.create()
						.texOffs(6, 27).addBox(1.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.1745F, -0.7854F, -0.1745F));

		down.addOrReplaceChild("e_r1",
				CubeListBuilder.create()
						.texOffs(26, 27).addBox(-7.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, -0.1745F, -0.7854F, 0.1745F));

		down.addOrReplaceChild("s_r1",
				CubeListBuilder.create()
						.texOffs(24, 20).addBox(-2.0F, -1.0F, 1.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.1745F, -0.7854F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		this.total.yRot = state.roundTick * 3.1415926F / 10;

	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

	@Override
	public Optional<ModelPart> getHeadModel() {
		return Optional.ofNullable(this.head);
	}

	@Override
	public Optional<ModelPart> getBodyModel() {
		return Optional.ofNullable(this.body);
	}

}