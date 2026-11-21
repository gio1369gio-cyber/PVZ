package com.lying.pvz.client.model.entity.plant.defence;


import com.lying.pvz.client.model.entity.ComponentModel;
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


// Made with Blockbench 3.6.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
// 迁移说明：1.20.6+/NeoForge ModelPart API（1.15 ModelRenderer 结构已逐件对应搬运）

public class WallNutModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			StringUtil.id("wall_nut"), "main");

	public static final ModelLayerLocation LAYER_LOCATION_2 = new ModelLayerLocation(
			StringUtil.id("explode_nut"), "main");

	/** 组件（盔甲）模型独立层定义 */
	public static final ModelLayerLocation ARMOR_LAYER_LOCATION = new ModelLayerLocation(
			StringUtil.id( "wall_nut_armor"), "main");

	private final ModelPart body;

	public WallNutModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 42).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 13.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(0, 27).addBox(-6.0F, -17.0F, -6.0F, 12.0F, 3.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(0, 57).addBox(-7.0F, -14.0F, -6.6F, 14.0F, 13.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(56, 0).addBox(-3.0F, -12.0F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public ModelPart getPlantWholeBody() {
		return this.body;
	}

	/**
	 * 组件（盔甲）模型。注意贴图尺寸与本体不同（64×64）。
	 */
	public static class WallNutArmorModel extends ComponentModel<PvZPlantRenderState> {

		private final ModelPart body;

		public WallNutArmorModel(ModelPart root) {
			super(root);
			this.body = root.getChild("body");
		}

		public static LayerDefinition createBodyLayer() {
			MeshDefinition mesh = new MeshDefinition();
			PartDefinition root = mesh.getRoot();

			root.addOrReplaceChild("body",
					CubeListBuilder.create()
							.texOffs(0, 0).addBox(-8.0F, -15.0F, -8.0F, 16.0F, 13.0F, 16.0F, CubeDeformation.NONE)
							.texOffs(0, 29).addBox(-7.0F, -18.0F, -7.0F, 14.0F, 3.0F, 14.0F, CubeDeformation.NONE),
					PartPose.offset(0.0F, 24.0F, 0.0F));

			return LayerDefinition.create(mesh, 64, 64);
		}

		@Override
		public ModelPart getTotalModel() {
			return this.body;
		}
	}
}