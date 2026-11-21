package com.lying.pvz.client.model.entity.plant.arma;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

public class MelonPultModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("melon_pult"), "main");

	private final ModelPart total;
	private final ModelPart pult;
	private final ModelPart melon;

	public MelonPultModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.pult = this.total.getChild("pult");
		this.melon = this.pult.getChild("bone").getChild("melon");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -10.5F, -5.0F, 14.0F, 10.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(32, 44).addBox(-7.0F, -8.0F, -5.2F, 14.0F, 2.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("nw_r1",
				CubeListBuilder.create()
						.texOffs(29, 0).addBox(-1.0F, 0.0F, -8.0F, 11.0F, 0.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, -0.0873F));


		total.addOrReplaceChild("sw_r1",
				CubeListBuilder.create()
						.texOffs(13, 47).addBox(-1.0F, 0.0F, -1.0F, 11.0F, 0.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, -0.0873F));


		total.addOrReplaceChild("se_r1",
				CubeListBuilder.create()
						.texOffs(-9, 47).addBox(-10.0F, 0.0F, -1.0F, 11.0F, 0.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0873F));


		total.addOrReplaceChild("ne_r1",
				CubeListBuilder.create()
						.texOffs(39, 18).addBox(-10.0F, 0.0F, -8.0F, 11.0F, 0.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0873F));


		PartDefinition pult = total.addOrReplaceChild("pult",
				CubeListBuilder.create()
						.texOffs(38, 51).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 11.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bone = pult.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(48, 9).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(0, 20).addBox(-5.0F, -2.0F, 3.0F, 10.0F, 5.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 44).addBox(-4.0F, -2.0F, 4.0F, 8.0F, 4.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, -0.2182F, 0.0F, 0.0F));


		bone.addOrReplaceChild("melon",
				CubeListBuilder.create()
						.texOffs(32, 27).addBox(-4.0F, -3.5F, -3.0F, 8.0F, 9.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -5.5F, 8.0F, -0.6545F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		if (state.attackTime > 0) {
			float percent = 1 - state.attackTime * 1.0F /state.pultAnimTime;
			pult.xRot = (1F - Mth.abs(Mth.cos(percent * 3.14159F))) * 1.5F;
			this.melon.visible = (percent < 0.5);
		} else {
			pult.xRot = Mth.sin(state.ageInTicks / 10) / 8;
			this.melon.visible = true;
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

}