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


// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
// 迁移说明：1.20.6+/NeoForge ModelPart API（1.15 ModelRenderer 结构已逐件对应搬运）
// 注意：side 全部 11 个方块为 mirror=true，使用 CubeListBuilder.mirror() 一次性开启

public class WaterGuardModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("water_guard"), "main");

	private final ModelPart total;

	public WaterGuardModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(188, 221).addBox(-8.0F, -17.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("side",
				CubeListBuilder.create().mirror()
						.texOffs(144, 234).addBox(-2.0F, -1.0F, -8.0F, 4.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(165, 214).addBox(2.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(221, 202).addBox(-4.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(202, 199).addBox(4.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(189, 202).addBox(-8.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(136, 233).addBox(-7.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(147, 211).addBox(-6.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(154, 193).addBox(-5.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(228, 186).addBox(5.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(204, 186).addBox(6.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(186, 187).addBox(7.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -3.1416F));


		total.addOrReplaceChild("side2",
				CubeListBuilder.create()
						.texOffs(92, 235).addBox(-2.0F, -1.0F, -8.0F, 4.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(69, 229).addBox(2.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(109, 216).addBox(-4.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(89, 211).addBox(4.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(75, 248).addBox(-8.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(48, 244).addBox(-7.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(22, 241).addBox(-6.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(39, 225).addBox(-5.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(3, 236).addBox(5.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(21, 223).addBox(6.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(5, 221).addBox(7.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-8.0F, -9.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		total.addOrReplaceChild("side3",
				CubeListBuilder.create()
						.texOffs(43, 205).addBox(-2.0F, -1.0F, -8.0F, 4.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(10, 198).addBox(2.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(82, 192).addBox(-4.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(121, 200).addBox(4.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(144, 195).addBox(-8.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(66, 192).addBox(-7.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(35, 189).addBox(-6.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(6, 179).addBox(-5.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(112, 184).addBox(5.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(160, 179).addBox(6.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(140, 184).addBox(7.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(8.0F, -9.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		total.addOrReplaceChild("side4",
				CubeListBuilder.create()
						.texOffs(213, 163).addBox(-2.0F, -1.0F, -8.0F, 4.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(189, 157).addBox(2.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(34, 170).addBox(-4.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(71, 175).addBox(4.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(104, 183).addBox(-8.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(6, 166).addBox(-7.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(59, 163).addBox(-6.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(22, 154).addBox(-5.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(131, 169).addBox(5.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(115, 167).addBox(6.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(160, 168).addBox(7.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -17.0F, 0.0F));

		total.addOrReplaceChild("side5",
				CubeListBuilder.create()
						.texOffs(212, 140).addBox(-2.0F, -1.0F, -8.0F, 4.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(161, 146).addBox(2.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(121, 147).addBox(-4.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(86, 156).addBox(4.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(9, 150).addBox(-8.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(50, 147).addBox(-7.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(77, 140).addBox(-6.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(104, 134).addBox(-5.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(148, 138).addBox(5.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(192, 139).addBox(6.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(30, 144).addBox(7.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -9.0F, -8.0F, 1.5708F, 0.0F, 0.0F));


		total.addOrReplaceChild("side6",
				CubeListBuilder.create()
						.texOffs(7, 121).addBox(-2.0F, -1.0F, -8.0F, 4.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(53, 121).addBox(2.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(91, 115).addBox(-4.0F, -1.0F, -7.0F, 2.0F, 1.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(133, 122).addBox(4.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(173, 131).addBox(-8.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(209, 126).addBox(-7.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(230, 118).addBox(-6.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(9, 102).addBox(-5.0F, -1.0F, -6.0F, 1.0F, 1.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(41, 105).addBox(5.0F, -1.0F, -5.0F, 1.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(76, 108).addBox(6.0F, -1.0F, -4.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(121, 107).addBox(7.0F, -1.0F, -2.0F, 1.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -9.0F, 8.0F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}