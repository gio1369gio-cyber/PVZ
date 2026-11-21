package com.lying.pvz.client.model.entity.plant.defence;


import com.lying.pvz.client.model.entity.ComponentModel;
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
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;


public class PumpkinModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("pumpkin"), "main");


	public static final ModelLayerLocation ARMOR_LAYER_LOCATION = new ModelLayerLocation(StringUtil.id( "pumpkin_armor"), "main");

	private final ModelPart total;

	public PumpkinModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-8.5F, -10.0F, -8.5F, 17.0F, 10.0F, 17.0F, CubeDeformation.NONE)
						.texOffs(2, 27).addBox(8.0F, -6.0F, -8.5F, 1.0F, 6.0F, 17.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}



	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}


	public static class PumpkinArmorModel extends ComponentModel<LivingEntityRenderState> {

		private final ModelPart total;

		public PumpkinArmorModel(ModelPart root) {
			super(root);
			this.total = root.getChild("total");
		}

		public static LayerDefinition createBodyLayer() {
			return PumpkinModel.createBodyLayer();
		}

		@Override
		public ModelPart getTotalModel() {
			return this.total;
		}
	}
}