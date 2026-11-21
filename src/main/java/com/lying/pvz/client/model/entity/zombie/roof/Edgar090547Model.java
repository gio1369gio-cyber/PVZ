package com.lying.pvz.client.model.entity.zombie.roof;

import com.lying.pvz.api.client.model.IZombieModel;
import com.lying.pvz.api.entity.IBodyEntity;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.AnimationUtil;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Mth;


// Edgar090547Model —— Edgar090547 僵尸（Boss 级）
// 形状：巨型僵尸骨架（上身 block 22×40×12、双臂加大、双腿加粗），
//       背部背一个方形背包（back，含四面围板），背包里藏一个迷你小鬼（imp，含腿/身体/双臂/头/小方块），
//       头部有帽檐（bone3）、红/白双色眼（red/white）、愤怒眉（angry）、平静眉（caim）、
//       双臂各握一根长杆（pole/pole2，杆上带 3 段装饰块 + 若干小方块），
//       躯干后侧伸出两根烟囱管（pipe1/pipe2）
//       灯泡红白交替闪烁，攻击时双臂交替挥动
public class Edgar090547Model extends EntityModel<PvZZombieRenderState> implements IZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("edgar_090547"), "main");

	private final ModelPart total;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart pipe1;
	private final ModelPart pipe2;
	private final ModelPart head;
	private final ModelPart red;
	private final ModelPart mouse;
	private final ModelPart bone3;
	private final ModelPart angry;
	private final ModelPart caim;
	private final ModelPart white;
	private final ModelPart left_arm;
	private final ModelPart pole;
	private final ModelPart right_arm;
	private final ModelPart pole2;
	private final ModelPart back;
	private final ModelPart imp;
	private final ModelPart left_leg3;
	private final ModelPart right_leg3;
	private final ModelPart up3;
	private final ModelPart body3;
	private final ModelPart right_arm2;
	private final ModelPart cube_r1;
	private final ModelPart left_arm2;
	private final ModelPart cube_r2;
	private final ModelPart head3;
	private final ModelPart bone;

	public Edgar090547Model(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.right_leg = this.total.getChild("right_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.pipe1 = this.body.getChild("pipe1");
		this.pipe2 = this.body.getChild("pipe2");
		this.head = this.up.getChild("head");
		this.red = this.head.getChild("red");
		this.mouse = this.head.getChild("mouse");
		this.bone3 = this.head.getChild("bone3");
		this.angry = this.head.getChild("angry");
		this.caim = this.head.getChild("caim");
		this.white = this.head.getChild("white");
		this.left_arm = this.up.getChild("left_arm");
		this.pole = this.left_arm.getChild("pole");
		this.right_arm = this.up.getChild("right_arm");
		this.pole2 = this.right_arm.getChild("pole2");
		this.back = this.up.getChild("back");
		this.imp = this.back.getChild("imp");
		this.left_leg3 = this.imp.getChild("left_leg3");
		this.right_leg3 = this.imp.getChild("right_leg3");
		this.up3 = this.imp.getChild("up3");
		this.body3 = this.up3.getChild("body3");
		this.right_arm2 = this.up3.getChild("right_arm2");
		this.cube_r1 = this.right_arm2.getChild("cube_r1");
		this.left_arm2 = this.up3.getChild("left_arm2");
		this.cube_r2 = this.left_arm2.getChild("cube_r2");
		this.head3 = this.up3.getChild("head3");
		this.bone = this.head3.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(155, 211).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 33.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(198, 230).addBox(-6.0F, 33.0F, -8.0F, 12.0F, 8.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(6.0F, -41.0F, 0.0F));

		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(154, 164).addBox(-4.0F, 0.0F, -3.0F, 10.0F, 33.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(198, 187).addBox(-5.0F, 33.0F, -8.0F, 12.0F, 8.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(-7.0F, -41.0F, 0.0F));

		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -41.0F, 0.0F));

		PartDefinition body = up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(186, 2).addBox(-11.0F, -40.0F, -4.0F, 22.0F, 40.0F, 12.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		body.addOrReplaceChild("pipe1",
				CubeListBuilder.create()
						.texOffs(200, 170).addBox(1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(245, 166).addBox(1.0F, -2.0F, 10.0F, 2.0F, 16.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(9.0F, -39.0F, 6.0F, -0.6109F, 0.0F, 0.0F));

		body.addOrReplaceChild("pipe2",
				CubeListBuilder.create()
						.texOffs(226, 150).addBox(-21.0F, -2.0F, 0.0F, 2.0F, 2.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(210, 146).addBox(-21.0F, -2.0F, 10.0F, 2.0F, 16.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(9.0F, -39.0F, 6.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(176, 103).addBox(-9.0F, -4.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(172, 97).addBox(8.0F, -4.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(188, 58).addBox(-8.0F, -9.0F, -16.0F, 16.0F, 13.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(195, 118).addBox(2.0F, -13.0F, -13.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(195, 134).addBox(-4.0F, -13.0F, -13.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -40.0F, -3.0F));

		head.addOrReplaceChild("red",
				CubeListBuilder.create()
						.texOffs(171, 77).addBox(9.0F, -86.0F, -13.0F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(154, 98).addBox(-11.0F, -86.0F, -13.0F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 81.0F, 3.0F));

		head.addOrReplaceChild("mouse",
				CubeListBuilder.create()
						.texOffs(186, 91).addBox(-8.0F, 0.0F, -15.0F, 16.0F, 3.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(134, 6).addBox(-8.0F, -2.0F, -3.0F, 16.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-1.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(1.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(4.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-5.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-7.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -2.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -2.0F, -6.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -2.0F, -7.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -2.0F, -12.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-1.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(2.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(6.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-5.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -1.0F, -15.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -1.0F, -11.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-8.0F, -1.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -1.0F, -12.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -1.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(7.0F, -1.0F, -5.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 6.0F, -1.0F));

		head.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(151, 151).addBox(-11.0F, -3.0F, 0.0F, 22.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(155, 126).addBox(-11.0F, -3.0F, -11.0F, 3.0F, 3.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(152, 108).addBox(8.0F, -3.0F, -11.0F, 3.0F, 3.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(102, 247).addBox(-11.0F, -3.0F, -14.0F, 22.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 1.1345F, 0.0F, 0.0F));

		head.addOrReplaceChild("angry",
				CubeListBuilder.create()
						.texOffs(117, 52).addBox(-5.0F, -5.0F, 0.0F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(117, 62).addBox(4.0F, -5.0F, 0.0F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(-2.0F, -1.0F, -17.0F));

		head.addOrReplaceChild("caim",
				CubeListBuilder.create()
						.texOffs(143, 54).addBox(2.0F, -6.0F, -17.0F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(142, 46).addBox(-7.0F, -6.0F, -17.0F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		head.addOrReplaceChild("white",
				CubeListBuilder.create()
						.texOffs(66, 133).addBox(9.0F, -5.0F, -10.0F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(73, 125).addBox(-11.0F, -5.0F, -10.0F, 2.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		PartDefinition left_arm = up.addOrReplaceChild("left_arm",
				CubeListBuilder.create()
						.texOffs(108, 180).addBox(-4.0F, 1.0F, -5.0F, 10.0F, 18.0F, 10.0F, new CubeDeformation(0.6F))
						.texOffs(110, 182).addBox(-3.0F, 20.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.6F)),
				PartPose.offset(15.0F, -35.0F, 2.0F));

		left_arm.addOrReplaceChild("pole",
				CubeListBuilder.create()
						.texOffs(34, 214).addBox(14.0F, 0.0F, 19.0F, 4.0F, 4.0F, 38.0F, CubeDeformation.NONE)
						.texOffs(0, 229).addBox(14.0F, -10.0F, 33.0F, 4.0F, 24.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(0, 237).addBox(14.0F, -6.0F, 23.0F, 4.0F, 16.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(0, 237).addBox(14.0F, -6.0F, 23.0F, 4.0F, 16.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(49, 197).addBox(15.0F, -4.0F, 20.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(54, 198).addBox(15.0F, 6.0F, 20.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(54, 192).addBox(15.0F, -7.0F, 30.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(57, 197).addBox(15.0F, 10.0F, 30.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-15.0F, 76.0F, -2.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition right_arm = up.addOrReplaceChild("right_arm",
				CubeListBuilder.create()
						.texOffs(106, 112).addBox(-5.0F, 1.0F, -5.0F, 10.0F, 18.0F, 10.0F, new CubeDeformation(0.6F))
						.texOffs(110, 182).addBox(-4.0F, 20.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.6F)),
				PartPose.offset(-16.0F, -35.0F, 2.0F));

		right_arm.addOrReplaceChild("pole2",
				CubeListBuilder.create()
						.texOffs(34, 214).addBox(-19.0F, 0.0F, 19.0F, 4.0F, 4.0F, 38.0F, CubeDeformation.NONE)
						.texOffs(0, 229).addBox(-19.0F, -10.0F, 33.0F, 4.0F, 24.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(0, 237).addBox(-19.0F, -6.0F, 23.0F, 4.0F, 16.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(0, 237).addBox(-19.0F, -6.0F, 23.0F, 4.0F, 16.0F, 3.0F, new CubeDeformation(0.2F))
						.texOffs(78, 182).addBox(-18.0F, -4.0F, 20.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(58, 204).addBox(-18.0F, 6.0F, 20.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(87, 179).addBox(-18.0F, -7.0F, 30.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(63, 199).addBox(-18.0F, 10.0F, 30.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(16.0F, 76.0F, -2.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition back = up.addOrReplaceChild("back",
				CubeListBuilder.create()
						.texOffs(36, 167).addBox(-8.0F, -15.0F, -15.0F, 16.0F, 17.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(40, 84).addBox(-8.0F, 2.0F, -15.0F, 16.0F, 1.0F, 17.0F, CubeDeformation.NONE)
						.texOffs(5, 167).addBox(8.0F, -15.0F, -15.0F, 1.0F, 17.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(33, 96).addBox(-8.0F, -15.0F, 2.0F, 16.0F, 17.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(29, 211).addBox(-9.0F, -15.0F, -15.0F, 1.0F, 17.0F, 18.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -23.0F, 23.0F));

		PartDefinition imp = back.addOrReplaceChild("imp",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 3.0F, -1.0F));

		imp.addOrReplaceChild("left_leg3",
				CubeListBuilder.create()
						.texOffs(125, 82).addBox(-1.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(2.0F, -12.0F, 0.0F));

		imp.addOrReplaceChild("right_leg3",
				CubeListBuilder.create()
						.texOffs(6, 76).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 13.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-3.0F, -12.0F, 0.0F));

		PartDefinition up3 = imp.addOrReplaceChild("up3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -12.0F, 0.0F));

		up3.addOrReplaceChild("body3",
				CubeListBuilder.create()
						.texOffs(2, 24).addBox(-5.0F, -16.0F, -2.0F, 10.0F, 15.0F, 4.0F, CubeDeformation.NONE),
				PartPose.ZERO);

		PartDefinition right_arm2 = up3.addOrReplaceChild("right_arm2",
				CubeListBuilder.create(),
				PartPose.offset(-7.0F, -14.0F, 0.0F));

		right_arm2.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(0, 1).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(0.25F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition left_arm2 = up3.addOrReplaceChild("left_arm2",
				CubeListBuilder.create(),
				PartPose.offset(7.0F, -14.0F, 0.0F));

		left_arm2.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(146, 68).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition head3 = up3.addOrReplaceChild("head3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -16.0F, 0.0F));

		head3.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(49, 23).addBox(-5.0F, -97.0F, 17.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.3F)),
				PartPose.offset(0.0F, 89.0F, -22.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}

	@Override
	public void setupAnim(PvZZombieRenderState state) {
		this.head.yRot = state.yRot / (180f / (float) Math.PI);
		this.head.xRot = state.xRot / (180f / (float) Math.PI);
		if (state.attackTime  == 0) {
			this.left_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662f) * 1.4f * state.walkAnimationSpeed;
			this.right_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662f + (float) Math.PI) * 1.4f * state.walkAnimationSpeed;
			this.right_arm.xRot = Mth.cos(state.walkAnimationPos* 0.6662f) * 1.4f * state.walkAnimationSpeed;
			this.left_arm.xRot = Mth.cos(state.walkAnimationPos * 0.6662f + (float) Math.PI) * 1.4f * state.walkAnimationSpeed;
			this.up.xRot = 0;
		} else if (state.attackTime > 0) {
			int total = state.crushCD;
			int now = total - state.attackTime;
			int frontT = total * 2 / 3;
			if (now < frontT) {
				this.left_arm.xRot = AnimationUtil.getUpDown(now, frontT, -120);
				this.left_arm.yRot = 0;
			} else {
				this.left_arm.xRot = AnimationUtil.getUpDown(now - frontT, total - frontT, 30);
				this.left_arm.yRot = AnimationUtil.getUpDown(now - frontT, total - frontT, 30);
			}
			if (now < frontT * 1f) {
				this.right_arm.xRot = AnimationUtil.getUpDown(now, frontT, -120);
				this.right_arm.yRot = 0;
			} else {
				this.right_arm.xRot = AnimationUtil.getUpDown(now - frontT, total - frontT, 30);
				this.right_arm.yRot = AnimationUtil.getUpDown(now - frontT, total - frontT, 30);
			}
			this.up.xRot = AnimationUtil.getUpDownUpDown(now, total, -15);
			this.right_leg.xRot = 0;
			this.left_leg.xRot = 0;
		}
		this.caim.visible = !state.isAngry;
		this.angry.visible =state.isAngry;
		int T = 10 << 1;
		int current = state.existTick % T;
		boolean flag = current < T >> 1;
		this.red.visible = flag;
		this.white.visible = !flag;
	}



	public void setRotationAngle(ModelPart modelPart, float x, float y, float z) {
		modelPart.xRot = x;
		modelPart.yRot = y;
		modelPart.zRot = z;
	}

	@Override
	public void tickPartAnim(IBodyEntity entity, float limbSwing, float limbSwingAmount,
							 float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderBody(IBodyEntity entity, PoseStack stack, SubmitNodeCollector collector, RenderType renderType, int packedLight, int packedOverlay) {
		this.angry.visible = entity.hasHandDefence();

	}

	@Override
	public EntityModel<? extends EntityRenderState> getZombieModel() {
		return this;
	}
}