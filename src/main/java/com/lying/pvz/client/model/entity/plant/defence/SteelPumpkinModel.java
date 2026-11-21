package com.lying.pvz.client.model.entity.plant.defence;

import com.lying.pvz.client.model.entity.ComponentModel;
import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

// SteelPumpkinModel —— 钢铁南瓜
// 形状：一个 17×17 的方底（total 朝向旋转 90°），四面围一圈薄壳，
//       左侧（旋转后朝前）一块高 26 的长面板 + 一块高 15 的内层（膨胀 0.1），
//       内部一个可开关的实心内核（inside，仅 entity.isSolid() 时可见）
public class SteelPumpkinModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("steel_pumpkin"), "main");

	private final ModelPart total;
	private final ModelPart inside;

	public SteelPumpkinModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.inside = this.total.getChild("inside");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-8.5F, -10.0F, -8.5F, 17.0F, 10.0F, 17.0F, CubeDeformation.NONE)
						.texOffs(2, 27).addBox(8.0F, -6.0F, -8.5F, 1.0F, 6.0F, 17.0F, CubeDeformation.NONE)
						.texOffs(0, 86).addBox(-8.75F, -26.0F, -8.0F, 1.0F, 26.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(34, 97).addBox(-8.75F, -15.0F, -8.0F, 1.0F, 15.0F, 16.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		total.addOrReplaceChild("inside",
				CubeListBuilder.create()
						.texOffs(0, 50).addBox(-8.0F, -9.5F, -8.0F, 16.0F, 9.0F, 16.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		this.inside.visible = state.isSolid ;
	}



	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

	public static class SteelPumpkinArmorModel extends ComponentModel<LivingEntityRenderState> {

		public static final ModelLayerLocation ARMOR_LAYER_LOCATION = new ModelLayerLocation(StringUtil.id( "steel_pumpkin_armor"), "main");

		private final ModelPart total;

		public SteelPumpkinArmorModel(ModelPart root) {
			super(root);
			this.total = root.getChild("total");
		}

		public static LayerDefinition createArmorLayer() {
			MeshDefinition mesh = new MeshDefinition();
			PartDefinition root = mesh.getRoot();

			root.addOrReplaceChild("total",
					CubeListBuilder.create()
							.texOffs(0, 0).addBox(-8.5F, -10.0F, -8.5F, 17.0F, 10.0F, 17.0F, CubeDeformation.NONE)
							.texOffs(2, 27).addBox(8.0F, -6.0F, -8.5F, 1.0F, 6.0F, 17.0F, CubeDeformation.NONE)
							.texOffs(0, 86).addBox(-8.75F, -26.0F, -8.0F, 1.0F, 26.0F, 16.0F, CubeDeformation.NONE)
							.texOffs(34, 97).addBox(-8.75F, -15.0F, -8.0F, 1.0F, 15.0F, 16.0F, new CubeDeformation(0.1F)),
					PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

			return LayerDefinition.create(mesh, 128, 128);
		}

		public ModelPart getTotalModel() {
			return this.total;
		}
	}
}