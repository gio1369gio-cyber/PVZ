package com.lying.pvz.client.model.entity.plant.arma;



import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.init.PvZEntityRegister;
import com.lying.pvz.common.level.entity.plant.arma.KernelPultEntity;
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

public class KernelPultModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("kernel_pult"), "main");

	private final ModelPart total;

	private final ModelPart pult;
	private final ModelPart kernel;
	private final ModelPart butter;

	public KernelPultModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.pult = this.total.getChild("pult");
		ModelPart basket = this.pult.getChild("basket");
		this.butter = basket.getChild("butter");
		this.kernel = basket.getChild("kernel");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = total.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 111).addBox(-6.0F, -4.5F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(-0.4F))
						.texOffs(48, 111).addBox(-6.0F, -8.25F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(-0.8F))
						.texOffs(0, 95).addBox(-6.0F, -11.25F, -6.0F, 12.0F, 5.0F, 12.0F, new CubeDeformation(-1.2F))
						.texOffs(48, 98).addBox(-5.0F, -12.25F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(-0.7F))
						.texOffs(88, 109).addBox(-5.0F, -13.75F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(-1.3F))
						.texOffs(88, 102).addBox(-3.0F, -13.25F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(-0.1F))
						.texOffs(44, 117).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition face = head.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(2, 84).addBox(-3.0F, -7.75F, -5.65F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F))
						.texOffs(1, 89).addBox(0.5F, -7.75F, -5.65F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		face.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(98, 123).addBox(-3.0F, 0.0F, 0.25F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(3.3279F, -8.2351F, -5.5F, 0.0F, 0.0F, 0.2443F));

		face.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(98, 126).addBox(-3.0F, -1.0F, 0.25F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(-0.75F, -8.0F, -5.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition leaves = total.addOrReplaceChild("leaves",
				CubeListBuilder.create()
						.texOffs(0, 82).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leave1 = leaves.addOrReplaceChild("leave1",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-5.0F, 0.0F, -3.0F, 0.0F, -0.5236F, 0.0F));

		leave1.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(36, 100).addBox(-3.0F, -1.0F, -3.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone1 = leave1.addOrReplaceChild("bone",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		bone1.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(43, 111).addBox(-3.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.4377F, -0.6026F, -3.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone3_1 = bone1.addOrReplaceChild("bone3",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-1.9981F, -0.0872F, 0.0F, 0.0F, 0.0F, -0.3491F));

		bone3_1.addOrReplaceChild("cube_r5",
				CubeListBuilder.create()
						.texOffs(79, 102).addBox(-4.9981F, -1.0872F, 2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(2.9218F, -0.5407F, -4.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone2_1 = bone3_1.addOrReplaceChild("bone2",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-3.0019F, 0.0872F, 0.0F, 0.0F, 0.0F, 0.3927F));

		bone2_1.addOrReplaceChild("cube_r6",
				CubeListBuilder.create()
						.texOffs(37, 112).addBox(-2.9676F, -0.3365F, 0.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(1.0857F, -2.3915F, -1.5F, 0.0F, 0.0F, 0.48F));


		PartDefinition leave2 = leaves.addOrReplaceChild("leave2",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, 0.0F, -1.5708F, 0.0F));

		leave2.addOrReplaceChild("cube_r7",
				CubeListBuilder.create()
						.texOffs(108, 100).addBox(-3.0F, -1.0F, -3.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone4 = leave2.addOrReplaceChild("bone4",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		bone4.addOrReplaceChild("cube_r8",
				CubeListBuilder.create()
						.texOffs(97, 96).addBox(-3.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.4377F, -0.6026F, -3.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone5_1 = bone4.addOrReplaceChild("bone5",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-1.9981F, -0.0872F, 0.0F, 0.0F, 0.0F, -0.3491F));

		bone5_1.addOrReplaceChild("cube_r9",
				CubeListBuilder.create()
						.texOffs(49, 93).addBox(-4.9981F, -1.0872F, 2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(2.9218F, -0.5407F, -4.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone6_1 = bone5_1.addOrReplaceChild("bone6",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-3.0019F, 0.0872F, 0.0F, 0.0F, 0.0F, 0.3927F));

		bone6_1.addOrReplaceChild("cube_r10",
				CubeListBuilder.create()
						.texOffs(79, 99).addBox(-2.9676F, -0.3365F, 0.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(1.0857F, -2.3915F, -1.5F, 0.0F, 0.0F, 0.48F));

		PartDefinition leave3 = leaves.addOrReplaceChild("leave3",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(5.0F, 0.0F, -3.0F, 0.0F, -2.618F, 0.0F));

		leave3.addOrReplaceChild("cube_r11",
				CubeListBuilder.create()
						.texOffs(64, 91).addBox(-3.0F, -1.0F, -3.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone7 = leave3.addOrReplaceChild("bone7",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		bone7.addOrReplaceChild("cube_r12",
				CubeListBuilder.create()
						.texOffs(85, 92).addBox(-3.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.4377F, -0.6026F, -3.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone8_1 = bone7.addOrReplaceChild("bone8",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-1.9981F, -0.0872F, 0.0F, 0.0F, 0.0F, -0.3491F));

		bone8_1.addOrReplaceChild("cube_r13",
				CubeListBuilder.create()
						.texOffs(114, 95).addBox(-4.9981F, -1.0872F, 2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(2.9218F, -0.5407F, -4.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone9_1 = bone8_1.addOrReplaceChild("bone9",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-3.0019F, 0.0872F, 0.0F, 0.0F, 0.0F, 0.3927F));

		bone9_1.addOrReplaceChild("cube_r14",
				CubeListBuilder.create()
						.texOffs(108, 96).addBox(-2.9676F, -0.3365F, 0.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(1.0857F, -2.3915F, -1.5F, 0.0F, 0.0F, 0.48F));

		PartDefinition leave4 = leaves.addOrReplaceChild("leave4",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-5.0F, 0.0F, 3.0F, 0.0F, 0.7854F, 0.0F));

		leave4.addOrReplaceChild("cube_r15",
				CubeListBuilder.create()
						.texOffs(36, 86).addBox(-3.0F, -1.0F, -3.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone10 = leave4.addOrReplaceChild("bone10",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		bone10.addOrReplaceChild("cube_r16",
				CubeListBuilder.create()
						.texOffs(50, 86).addBox(-3.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.4377F, -0.6026F, -3.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone11_1 = bone10.addOrReplaceChild("bone11",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-1.9981F, -0.0872F, 0.0F, 0.0F, 0.0F, -0.3491F));

		bone11_1.addOrReplaceChild("cube_r17",
				CubeListBuilder.create()
						.texOffs(96, 91).addBox(-4.9981F, -1.0872F, 2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(2.9218F, -0.5407F, -4.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone12_1 = bone11_1.addOrReplaceChild("bone12",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-3.0019F, 0.0872F, 0.0F, 0.0F, 0.0F, 0.3927F));

		bone12_1.addOrReplaceChild("cube_r18",
				CubeListBuilder.create()
						.texOffs(118, 125).addBox(-2.9676F, -0.3365F, 0.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(1.0857F, -2.3915F, -1.5F, 0.0F, 0.0F, 0.48F));

		PartDefinition leave5 = leaves.addOrReplaceChild("leave5",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, 0.0F, 1.5708F, 0.0F));

		leave5.addOrReplaceChild("cube_r19",
				CubeListBuilder.create()
						.texOffs(108, 88).addBox(-3.0F, -1.0F, -3.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone13 = leave5.addOrReplaceChild("bone13",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		bone13.addOrReplaceChild("cube_r20",
				CubeListBuilder.create()
						.texOffs(98, 85).addBox(-3.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.4377F, -0.6026F, -3.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone14_1 = bone13.addOrReplaceChild("bone14",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-1.9981F, -0.0872F, 0.0F, 0.0F, 0.0F, -0.3491F));

		bone14_1.addOrReplaceChild("cube_r21",
				CubeListBuilder.create()
						.texOffs(84, 87).addBox(-4.9981F, -1.0872F, 2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(2.9218F, -0.5407F, -4.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone15_1 = bone14_1.addOrReplaceChild("bone15",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-3.0019F, 0.0872F, 0.0F, 0.0F, 0.0F, 0.3927F));

		bone15_1.addOrReplaceChild("cube_r22",
				CubeListBuilder.create()
						.texOffs(108, 125).addBox(-2.9676F, -0.3365F, 0.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(1.0857F, -2.3915F, -1.5F, 0.0F, 0.0F, 0.48F));


		PartDefinition leave6 = leaves.addOrReplaceChild("leave6",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(5.0F, 0.0F, 3.0F, 0.0F, 2.3562F, 0.0F));

		leave6.addOrReplaceChild("cube_r23",
				CubeListBuilder.create()
						.texOffs(64, 84).addBox(-3.0F, -1.0F, -3.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone16 = leave6.addOrReplaceChild("bone16",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		bone16.addOrReplaceChild("cube_r24",
				CubeListBuilder.create()
						.texOffs(112, 82).addBox(-3.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.4377F, -0.6026F, -3.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition bone17_1 = bone16.addOrReplaceChild("bone17",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-1.9981F, -0.0872F, 0.0F, 0.0F, 0.0F, -0.3491F));

		bone17_1.addOrReplaceChild("cube_r25",
				CubeListBuilder.create()
						.texOffs(79, 82).addBox(-4.9981F, -1.0872F, 2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(2.9218F, -0.5407F, -4.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone18_1 = bone17_1.addOrReplaceChild("bone18",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-3.0019F, 0.0872F, 0.0F, 0.0F, 0.0F, 0.3927F));

		bone18_1.addOrReplaceChild("cube_r26",
				CubeListBuilder.create()
						.texOffs(93, 83).addBox(-2.9676F, -0.3365F, 0.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(1.0857F, -2.3915F, -1.5F, 0.0F, 0.0F, 0.48F));

		PartDefinition pult = total.addOrReplaceChild("pult",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -13.0F, 0.0F));

		pult.addOrReplaceChild("cube_r27",
				CubeListBuilder.create()
						.texOffs(120, 108).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.0F, -1.6192F, 9.0169F, -1.6144F, 0.0F, 0.0F));

		pult.addOrReplaceChild("cube_r28",
				CubeListBuilder.create()
						.texOffs(0, 112).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.0F, -3.8077F, 1.2387F, -1.8326F, 0.0F, 0.0F));


		pult.addOrReplaceChild("cube_r29",
				CubeListBuilder.create()
						.texOffs(90, 111).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(-0.4F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));


		PartDefinition basket = pult.addOrReplaceChild("basket",
				CubeListBuilder.create()
						.texOffs(37, 79).addBox(-4.0F, -1.0F, 0.0F, 6.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(55, 81).addBox(-4.0F, -4.0F, 6.0F, 6.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(103, 81).addBox(-4.0F, -4.0F, -1.0F, 6.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(28, 73).addBox(2.0F, -4.0F, 0.0F, 1.0F, 3.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(14, 73).addBox(-5.0F, -4.0F, 0.0F, 1.0F, 3.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.5F, 18.0F));


		basket.addOrReplaceChild("butter",
				CubeListBuilder.create()
						.texOffs(104, 69).addBox(-4.0F, -6.0F, 0.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		basket.addOrReplaceChild("kernel",
				CubeListBuilder.create()
						.texOffs(0, 99).addBox(-2.0F, -5.0F, 2.0F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		if (state.attackTime > 0) {
			float percent = 1 - state.attackTime * 1.0F / state.pultAnimTime;
			pult.xRot = (1F - Mth.abs(Mth.cos(percent * 3.14159F))) * 1.5F;
			this.kernel.visible = (percent < 0.5) && state.currentBullet == KernelPultEntity.CornTypes.KERNEL;
			this.butter.visible = (percent < 0.5) && state.currentBullet == KernelPultEntity.CornTypes.BUTTER;
		} else {
			pult.xRot = Mth.sin(state.ageInTicks / 10) / 8;
			this.butter.visible = (state.entityType == PvZEntityRegister.BUTTER_PULT.get());
			this.kernel.visible = !this.butter.visible;
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}

}