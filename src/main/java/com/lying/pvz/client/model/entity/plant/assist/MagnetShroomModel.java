package com.lying.pvz.client.model.entity.plant.assist;


import com.lying.pvz.api.entity.ability.MetalTypes;
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
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

import java.util.function.Function;


// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
// 迁移说明：1.20.6+/NeoForge ModelPart API（1.15 ModelRenderer 结构已逐件对应搬运）

public class MagnetShroomModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("magnet_shroom"), "main");

	private final ModelPart total;

	private final ModelPart magnet;
	private final ModelPart rest_magnet;
	private final ModelPart absorb_magnet;

	public MagnetShroomModel(ModelPart root, Function<Identifier, RenderType> renderType) {
		super(root,renderType);
		this.total = root.getChild("total");
		final ModelPart magnets = this.total.getChild("magnets");
		this.magnet = magnets.getChild("magnet");
		this.rest_magnet = magnets.getChild("rest_magnet");
		this.absorb_magnet = magnets.getChild("absorb_magnet");
	}



    public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 61).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 54).addBox(-1.0F, -5.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(-0.3F))
						.texOffs(0, 49).addBox(-1.0F, -7.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition plate = total.addOrReplaceChild("plate",
				CubeListBuilder.create()
						.texOffs(9, 59).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.1F)),
				PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition bone3 = plate.addOrReplaceChild("bone3",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		bone3.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(9, 56).addBox(0.25F, -1.0F, 0.0F, 3.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.75F, -0.134F, 1.75F, 0.5236F, 0.0F, 0.0F));

		plate.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(9, 53).addBox(-1.5F, -0.241F, -3.5825F, 3.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 1.5708F, 0.0F));


		PartDefinition face = total.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(11, 40).addBox(-2.5F, 0.0F, -1.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(33, 40).addBox(0.5F, 0.0F, -1.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, -11.0F, 0.0F));

		face.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(57, 41).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(1.75F, -0.5F, -1.75F, 0.0F, 0.0F, 0.1745F));

		face.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(19, 39).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-1.75F, -0.5F, -1.75F, 0.0F, 0.0F, -0.1745F));

		total.addOrReplaceChild("elect",
				CubeListBuilder.create()
						.texOffs(1, 38).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, -16.25F, 0.0F));

		PartDefinition magnets = total.addOrReplaceChild("magnets",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -8.0F, 0.0F));


		PartDefinition magnet = magnets.addOrReplaceChild("magnet",
				CubeListBuilder.create()
						.texOffs(25, 57).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, -0.75F, 0.0F));

		magnet.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(38, 58).addBox(0.4575F, -1.4755F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		magnet.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(20, 50).addBox(-2.4575F, -1.4755F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		magnet.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(51, 52).addBox(0.1585F, -0.0425F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

		magnet.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(38, 51).addBox(-3.1585F, -0.0425F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		magnet.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(31, 47).addBox(-4.8246F, 0.3592F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.309F));

		magnet.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(10, 45).addBox(-4.4373F, -7.364F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		magnet.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(51, 44).addBox(1.4373F, -7.364F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		magnet.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(1, 41).addBox(2.8246F, 0.3592F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.309F));

		magnet.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(40, 41).addBox(-5.609F, -8.0288F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		magnet.addOrReplaceChild("bone14",
				CubeListBuilder.create()
						.texOffs(21, 42).addBox(2.609F, -8.0288F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition rest_magnet = magnets.addOrReplaceChild("rest_magnet",
				CubeListBuilder.create()
						.texOffs(1, 1).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, -0.75F, 0.0F));

		rest_magnet.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(1, 7).addBox(0.4575F, -1.4755F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		rest_magnet.addOrReplaceChild("bone15",
				CubeListBuilder.create()
						.texOffs(1, 13).addBox(-2.4575F, -1.4755F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		rest_magnet.addOrReplaceChild("bone16",
				CubeListBuilder.create()
						.texOffs(1, 1).addBox(0.1585F, -0.0425F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

		rest_magnet.addOrReplaceChild("bone17",
				CubeListBuilder.create()
						.texOffs(0, 7).addBox(-3.1585F, -0.0425F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		rest_magnet.addOrReplaceChild("bone18",
				CubeListBuilder.create()
						.texOffs(1, 13).addBox(-4.8246F, 0.3592F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.309F));

		rest_magnet.addOrReplaceChild("bone19",
				CubeListBuilder.create()
						.texOffs(1, 1).addBox(-4.4373F, -7.364F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		rest_magnet.addOrReplaceChild("bone20",
				CubeListBuilder.create()
						.texOffs(1, 1).addBox(1.4373F, -7.364F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		rest_magnet.addOrReplaceChild("bone21",
				CubeListBuilder.create()
						.texOffs(2, 3).addBox(2.8246F, 0.3592F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.309F));

		rest_magnet.addOrReplaceChild("bone22",
				CubeListBuilder.create()
						.texOffs(1, 20).addBox(-5.609F, -8.0288F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		rest_magnet.addOrReplaceChild("bone23",
				CubeListBuilder.create()
						.texOffs(1, 26).addBox(2.609F, -8.0288F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition absorb_magnet = magnets.addOrReplaceChild("absorb_magnet",
				CubeListBuilder.create()
						.texOffs(16, 1).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, -0.75F, 0.0F));

		absorb_magnet.addOrReplaceChild("bone24",
				CubeListBuilder.create()
						.texOffs(15, 1).addBox(0.4575F, -1.4755F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		absorb_magnet.addOrReplaceChild("bone25",
				CubeListBuilder.create()
						.texOffs(16, 2).addBox(-2.4575F, -1.4755F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		absorb_magnet.addOrReplaceChild("bone26",
				CubeListBuilder.create()
						.texOffs(15, 2).addBox(0.1585F, -0.0425F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

		absorb_magnet.addOrReplaceChild("bone27",
				CubeListBuilder.create()
						.texOffs(15, 1).addBox(-3.1585F, -0.0425F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

		absorb_magnet.addOrReplaceChild("bone28",
				CubeListBuilder.create()
						.texOffs(16, 2).addBox(-4.8246F, 0.3592F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.309F));

		absorb_magnet.addOrReplaceChild("bone29",
				CubeListBuilder.create()
						.texOffs(15, 2).addBox(-4.4373F, -7.364F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		absorb_magnet.addOrReplaceChild("bone30",
				CubeListBuilder.create()
						.texOffs(16, 3).addBox(1.4373F, -7.364F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		absorb_magnet.addOrReplaceChild("bone31",
				CubeListBuilder.create()
						.texOffs(16, 3).addBox(2.8246F, 0.3592F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.309F));

		absorb_magnet.addOrReplaceChild("bone32",
				CubeListBuilder.create()
						.texOffs(15, 11).addBox(-5.609F, -8.0288F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		absorb_magnet.addOrReplaceChild("bone33",
				CubeListBuilder.create()
						.texOffs(15, 17).addBox(2.609F, -8.0288F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		final boolean isRest = ! state.isMagnetActive;
		final boolean hasReach = state.metalType != MetalTypes.EMPTY;
		this.magnet.visible = ! isRest;
		this.rest_magnet.visible = (isRest && hasReach);
		this.absorb_magnet.visible = (isRest && ! hasReach);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}