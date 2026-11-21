package com.lying.pvz.client.model.entity.plant.assist;


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

// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
// 迁移说明：1.20.6+/NeoForge ModelPart API（1.15 ModelRenderer 结构已逐件对应搬运）

public class GraveBusterModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("grave_buster"), "main");

	private final ModelPart total;

	private final ModelPart m1;
	private final ModelPart m2;
	private final ModelPart m3;
	private final ModelPart m4;
	private final ModelPart m5;
	private final ModelPart m6;
	private final ModelPart m7;
	private final ModelPart m8;

	public GraveBusterModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		final ModelPart mouse = this.total.getChild("mouse");
		this.m1 = mouse.getChild("front").getChild("m1");
		this.m2 = mouse.getChild("front").getChild("m2");
		this.m3 = mouse.getChild("left").getChild("m3");
		this.m4 = mouse.getChild("left").getChild("m4");
		this.m5 = mouse.getChild("back").getChild("m5");
		this.m6 = mouse.getChild("back").getChild("m6");
		this.m7 = mouse.getChild("right").getChild("m7");
		this.m8 = mouse.getChild("right").getChild("m8");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(1, 99).addBox(-8.0F, -32.0F, -6.0F, 16.0F, 16.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(59, 110).addBox(-7.0F, -32.0F, -7.0F, 14.0F, 16.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(92, 110).addBox(-6.0F, -32.0F, 7.0F, 12.0F, 16.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(97, 91).addBox(-7.0F, -32.0F, 6.0F, 14.0F, 16.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(69, 91).addBox(-6.0F, -32.0F, -8.0F, 12.0F, 16.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 8.0F, 0.0F));


		PartDefinition mouse = total.addOrReplaceChild("mouse",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 8.0F, 0.0F));


		PartDefinition front = mouse.addOrReplaceChild("front",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -16.0F, -7.0F));

		front.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(58, 97).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(-5.0F, 0.0F, 0.0F));

		front.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(48, 99).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(6.0F, 0.0F, 0.0F));

		front.addOrReplaceChild("m1",
				CubeListBuilder.create()
						.texOffs(54, 84).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		front.addOrReplaceChild("m2",
				CubeListBuilder.create()
						.texOffs(40, 84).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));


		PartDefinition back = mouse.addOrReplaceChild("back",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -16.0F, 7.0F));

		back.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(29, 85).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(-5.0F, 0.0F, 0.0F));

		back.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(18, 84).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(6.0F, 0.0F, 0.0F));

		back.addOrReplaceChild("m5",
				CubeListBuilder.create()
						.texOffs(3, 83).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		back.addOrReplaceChild("m6",
				CubeListBuilder.create()
						.texOffs(115, 77).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));


		PartDefinition left = mouse.addOrReplaceChild("left",
				CubeListBuilder.create(),
				PartPose.offset(-7.0F, -16.0F, 0.0F));

		left.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(104, 77).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 5.0F));

		left.addOrReplaceChild("m4",
				CubeListBuilder.create()
						.texOffs(89, 76).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, -0.0873F));

		left.addOrReplaceChild("m3",
				CubeListBuilder.create()
						.texOffs(74, 76).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, 0.0F, 0.0873F));

		left.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(118, 65).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -5.0F));


		PartDefinition right = mouse.addOrReplaceChild("right",
				CubeListBuilder.create(),
				PartPose.offset(7.0F, -16.0F, 0.0F));

		right.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(107, 64).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 5.0F));

		right.addOrReplaceChild("m7",
				CubeListBuilder.create()
						.texOffs(92, 62).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, -0.0873F));

		right.addOrReplaceChild("m8",
				CubeListBuilder.create()
						.texOffs(77, 62).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 8.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 0.0F, 0.0F, 0.0873F));

		right.addOrReplaceChild("bone16",
				CubeListBuilder.create()
						.texOffs(64, 68).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -5.0F));


		PartDefinition head = total.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(73, 49).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(84, 43).addBox(-6.0F, -1.0F, 5.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(63, 50).addBox(-6.0F, -1.0F, -6.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(56, 48).addBox(5.0F, -1.0F, -6.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(48, 48).addBox(5.0F, -1.0F, 5.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		PartDefinition right_wine = head.addOrReplaceChild("right_wine",
				CubeListBuilder.create()
						.texOffs(30, 74).addBox(-2.0F, -3.0F, -3.0F, 1.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-8.0F, 0.0F, 0.0F));

		right_wine.addOrReplaceChild("bone17",
				CubeListBuilder.create()
						.texOffs(46, 73).addBox(0.0F, -1.0F, -4.0F, 1.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0F, 0.0F, 5.0F, -0.5236F, 1.0472F, 0.0F));

		right_wine.addOrReplaceChild("bone18",
				CubeListBuilder.create()
						.texOffs(99, 38).addBox(0.0F, -1.0F, -2.0F, 1.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0F, 0.0F, -5.0F, 0.5236F, -1.0472F, 0.0F));

		right_wine.addOrReplaceChild("bone19",
				CubeListBuilder.create()
						.texOffs(15, 76).addBox(-3.0F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0F, -1.0F, -2.0F, -0.5236F, -0.2618F, 0.0F));

		right_wine.addOrReplaceChild("bone20",
				CubeListBuilder.create()
						.texOffs(3, 74).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0F, -1.0F, 2.0F, 0.5236F, 0.2618F, 0.0F));


		PartDefinition left_wine = head.addOrReplaceChild("left_wine",
				CubeListBuilder.create()
						.texOffs(26, 64).addBox(1.0F, -3.0F, -3.0F, 1.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(8.0F, 0.0F, 0.0F));

		left_wine.addOrReplaceChild("bone25",
				CubeListBuilder.create()
						.texOffs(61, 57).addBox(-1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0F, 0.0F, -5.0F, 0.5236F, 1.0472F, 0.0F));

		left_wine.addOrReplaceChild("bone26",
				CubeListBuilder.create()
						.texOffs(43, 64).addBox(-1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 5.0F, -0.5236F, -1.0472F, 0.0F));

		left_wine.addOrReplaceChild("bone31",
				CubeListBuilder.create()
						.texOffs(12, 68).addBox(0.0F, -1.0F, -3.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0F, -1.0F, -2.0F, -0.5236F, 0.2618F, 0.0F));

		left_wine.addOrReplaceChild("bone32",
				CubeListBuilder.create()
						.texOffs(3, 62).addBox(0.0F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(1.0F, -1.0F, 2.0F, 0.5236F, -0.2618F, 0.0F));

		PartDefinition back_wine = head.addOrReplaceChild("back_wine",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, -0.2618F, 0.0F, 0.0F));

		back_wine.addOrReplaceChild("bone21",
				CubeListBuilder.create()
						.texOffs(123, 53).addBox(-1.0F, -7.0F, 0.0F, 1.0F, 7.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-5.0F, 1.0F, -1.0F, -0.2618F, 0.0F, 1.0472F));

		back_wine.addOrReplaceChild("bone22",
				CubeListBuilder.create()
						.texOffs(116, 54).addBox(0.0F, -7.0F, 0.0F, 1.0F, 7.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(5.0F, 1.0F, -1.0F, -0.2618F, 0.0F, -1.0472F));

		PartDefinition bone29 = back_wine.addOrReplaceChild("bone29",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -2.0F, 1.0F, 0.5236F, 0.0F, 0.0F));

		bone29.addOrReplaceChild("bone30",
				CubeListBuilder.create()
						.texOffs(105, 58).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.0F, 0.7854F, 0.0F));


		PartDefinition front_wine = head.addOrReplaceChild("front_wine",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.2618F, 0.0F, 0.0F));

		front_wine.addOrReplaceChild("bone24",
				CubeListBuilder.create()
						.texOffs(123, 43).addBox(0.0F, -7.0F, -1.0F, 1.0F, 7.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(5.0F, 1.0F, 1.0F, 0.2618F, 0.0F, -1.0472F));

		front_wine.addOrReplaceChild("bone23",
				CubeListBuilder.create()
						.texOffs(116, 43).addBox(-1.0F, -7.0F, -1.0F, 1.0F, 7.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-5.0F, 1.0F, 1.0F, 0.2618F, 0.0F, 1.0472F));

		PartDefinition bone28 = front_wine.addOrReplaceChild("bone28",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -2.0F, -1.0F, -0.5236F, 0.0F, 0.0F));

		bone28.addOrReplaceChild("bone27",
				CubeListBuilder.create()
						.texOffs(104, 50).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 0.0F, 0.7854F, 0.0F));


		head.addOrReplaceChild("bone33",
				CubeListBuilder.create()
						.texOffs(83, 50).addBox(0.0F, -1.0F, -7.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-5.0F, 0.0F, 4.0F, -0.2618F, -0.7854F, 0.0F));

		head.addOrReplaceChild("bone34",
				CubeListBuilder.create()
						.texOffs(40, 52).addBox(-1.0F, -1.0F, -7.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(5.0F, 0.0F, 4.0F, -0.2618F, 0.7854F, 0.0F));

		head.addOrReplaceChild("bone35",
				CubeListBuilder.create()
						.texOffs(19, 51).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-5.0F, 0.0F, -4.0F, 0.2618F, 0.7854F, 0.0F));

		head.addOrReplaceChild("bone36",
				CubeListBuilder.create()
						.texOffs(3, 45).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(5.0F, 0.0F, -4.0F, 0.2618F, -0.7854F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		final float angle = 0.15f;
		if(state.eatingTomb) {
			int x = state.attackTime;
			final int times = 7;
			float T = state.eatTombCD * 1.0f / times;
			double w = 2 * Math.PI / T;
			this.m1.xRot = (float) - Math.cos(w * x) * angle;
			this.m2.xRot = (float) Math.cos(w * x) * angle;
			this.m3.zRot = (float) Math.cos(w * x) * angle;
			this.m4.zRot = (float) - Math.cos(w * x) * angle;
			this.m5.xRot = (float) Math.cos(w * x) * angle;
			this.m6.xRot = (float) - Math.cos(w * x) * angle;
			this.m7.zRot = (float) - Math.cos(w * x) * angle;
			this.m8.zRot = (float) Math.cos(w * x) * angle;
		} else {
			this.m1.xRot = -angle;
			this.m2.xRot = angle;
			this.m3.zRot = angle;
			this.m4.zRot = -angle;
			this.m5.xRot = angle;
			this.m6.xRot = -angle;
			this.m7.zRot = -angle;
			this.m8.zRot = angle;
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}