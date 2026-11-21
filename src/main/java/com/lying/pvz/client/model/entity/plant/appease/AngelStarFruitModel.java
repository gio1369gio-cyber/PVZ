package com.lying.pvz.client.model.entity.plant.appease;

import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
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

public class AngelStarFruitModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("angel_star_fruit"), "main");

	private final ModelPart total;

	public AngelStarFruitModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition base = total.addOrReplaceChild("base",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition b1 = base.addOrReplaceChild("b1",
				CubeListBuilder.create()
						.texOffs(10, 13).addBox(-8.0F, -1.95F, 5.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 1.1F, 0.0F, 0.0F, 0.1571F, 0.0F));

		b1.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(10, 13).addBox(-1.0F, -0.5F, -2.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(-8.8205F, -1.45F, -4.4943F, 0.0F, -1.2566F, 0.0F));

		b1.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(10, 13).addBox(-1.0F, -0.5F, -2.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(1.5486F, -1.45F, -9.7776F, 0.0F, -2.5133F, 0.0F));

		b1.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(10, 13).addBox(-1.0F, -0.5F, -2.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(9.7776F, -1.45F, -1.5486F, 0.0F, 2.5133F, 0.0F));

		b1.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(10, 13).addBox(-1.0F, -0.5F, -2.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(4.4943F, -1.45F, 8.8205F, 0.0F, 1.2566F, 0.0F));

		PartDefinition b2 = base.addOrReplaceChild("b2",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.955F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 1.2F, 0.0F, 0.0F, 0.3665F, 0.0F));

		b2.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.955F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.5133F, 0.0F));

		b2.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.955F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.2566F, 0.0F));

		b2.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.955F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.5133F, 0.0F));

		b2.addOrReplaceChild("cube_r8",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.955F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.2566F, 0.0F));


		PartDefinition b3 = base.addOrReplaceChild("b3",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.95F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 1.2F, 0.0F, 0.0F, -0.0524F, 0.0F));

		b3.addOrReplaceChild("cube_r9",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.95F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.5133F, 0.0F));

		b3.addOrReplaceChild("cube_r10",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.95F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.2566F, 0.0F));

		b3.addOrReplaceChild("cube_r11",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.95F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.5133F, 0.0F));

		b3.addOrReplaceChild("cube_r12",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -1.95F, -1.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.2566F, 0.0F));

		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition face = body.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(0, 5).addBox(0.0F, -3.81F, -3.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F))
						.texOffs(0, 0).addBox(-1.0F, -3.81F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, -0.45F, 0.0F));

		face.addOrReplaceChild("cube_r13",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -2.46F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(3.0611F, -1.35F, -2.3772F, 0.0F, 0.9599F, 0.0F));

		face.addOrReplaceChild("cube_r14",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -2.46F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(1.5405F, -1.35F, -3.7629F, 0.0F, -0.3054F, 0.0F));

		face.addOrReplaceChild("cube_r15",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -2.46F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(2.9383F, -1.35F, -3.0735F, 0.0F, -0.6109F, 0.0F));

		face.addOrReplaceChild("cube_r16",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -2.46F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(-1.5405F, -1.35F, -3.7629F, 0.0F, 0.3054F, 0.0F));

		face.addOrReplaceChild("cube_r17",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-2.0F, -2.46F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(-2.4875F, -1.35F, -1.558F, 0.0F, -0.9599F, 0.0F));

		face.addOrReplaceChild("cube_r18",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -2.46F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offsetAndRotation(-2.9383F, -1.35F, -3.0735F, 0.0F, 0.6109F, 0.0F));

		face.addOrReplaceChild("eye",
				CubeListBuilder.create()
						.texOffs(0, 13).addBox(-3.0F, -2.46F, 0.0F, 2.0F, 1.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 17).addBox(1.0F, -2.46F, 0.0F, 2.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.1F, 1.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-7.0F, -1.01F, 0.0F, 7.0F, 1.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		body2.addOrReplaceChild("cube_r19",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-7.0F, -1.01F, 0.0F, 7.0F, 1.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.5133F, 0.0F));

		body2.addOrReplaceChild("cube_r20",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-7.0F, -1.01F, 0.0F, 7.0F, 1.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.2566F, 0.0F));

		body2.addOrReplaceChild("cube_r21",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-7.0F, -1.01F, 0.0F, 7.0F, 1.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.5133F, 0.0F));

		body2.addOrReplaceChild("cube_r22",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-7.0F, -1.01F, 0.0F, 7.0F, 1.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.2566F, 0.0F));

		PartDefinition body3 = body.addOrReplaceChild("body3",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-6.0F, -3.011F, 0.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		body3.addOrReplaceChild("cube_r23",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-6.0F, -3.011F, 0.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.5133F, 0.0F));

		body3.addOrReplaceChild("cube_r24",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-6.0F, -3.011F, 0.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.2566F, 0.0F));

		body3.addOrReplaceChild("cube_r25",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-6.0F, -3.011F, 0.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.5133F, 0.0F));

		body3.addOrReplaceChild("cube_r26",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-6.0F, -3.011F, 0.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.2566F, 0.0F));

		PartDefinition body4 = body.addOrReplaceChild("body4",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-6.0F, -3.0F, 0.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		body4.addOrReplaceChild("cube_r27",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-6.0F, -3.0F, 0.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.5133F, 0.0F));

		body4.addOrReplaceChild("cube_r28",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-6.0F, -3.0F, 0.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.2566F, 0.0F));

		body4.addOrReplaceChild("cube_r29",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-6.0F, -3.0F, 0.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.5133F, 0.0F));

		body4.addOrReplaceChild("cube_r30",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-6.0F, -3.0F, 0.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.2566F, 0.0F));

		PartDefinition body5 = body.addOrReplaceChild("body5",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-6.0F, -4.0F, -1.0F, 7.0F, 2.0F, 7.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		body5.addOrReplaceChild("cube_r31",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-6.0F, -4.0F, -1.0F, 7.0F, 2.0F, 7.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.5133F, 0.0F));

		body5.addOrReplaceChild("cube_r32",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-6.0F, -4.0F, -1.0F, 7.0F, 2.0F, 7.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.2566F, 0.0F));

		body5.addOrReplaceChild("cube_r33",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-6.0F, -4.0F, -1.0F, 7.0F, 2.0F, 7.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.5133F, 0.0F));

		body5.addOrReplaceChild("cube_r34",
				CubeListBuilder.create()
						.texOffs(0, 23).addBox(-6.0F, -4.0F, -1.0F, 7.0F, 2.0F, 7.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.2566F, 0.0F));

		return LayerDefinition.create(mesh, 32, 32);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		this.total.yRot = 3.14159F;
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}


}