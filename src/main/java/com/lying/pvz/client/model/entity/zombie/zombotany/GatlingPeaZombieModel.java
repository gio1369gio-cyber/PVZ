package com.lying.pvz.client.model.entity.zombie.zombotany;


import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   1. 几何 1:1 搬运。zombotany 分类中顶点最多的模型：
//        骨架 + 豌豆头（10 方块 + 加特林枪管） + 金属头盔（7 方块 + 2 根侧角）。
//   2. 结构与 PeaShooterZombie 的差异：
//        head 的偏移是 (0, -26, -1)，不是 (0, -24, 0)；
//        多出 gar（加特林枪管，4 个十字排布的方块）和 helmet（7 方块）。
//   3. ⚠️ 唯一重写 setupAnim 的 zombotany 模型：
//        先调 super.setupAnim(...) 让基类做步行/攻击的基础动画，
//        然后 **独立** 驱动 gar.zRot = ageInTicks / 5 —— 让加特林枪管持续旋转。
//        这是"边走路边转枪管"的专属动画，其他 zombotany 僵尸没有。
//        ⚠️ 注意 super.setupAnim 必须调用，否则基础的步行/攻击动画会丢失。
//   4. ⚠️ gar 不是挂 head 中心，而是挂在 head 下 (0, -3, -17) —— 即炮管前端位置，
//        这样 gar.zRot 会绕炮管自身中心旋转（而不是绕头中心），视觉上是枪管旋转。
//   5. helmet 的 7 个方块是"金属头盔"外壳：
//        左右立柱 + 顶盖 + 后脑罩 + 两层顶片 + 护颈片。
//        两根 side角（bone / bone2）绕 Y±45° + Z±25° 旋转，是头盔两旁的"耳朵"。
//   6. 无 MatrixStack / IVertexBuilder 相关调用（PVZZombieModel 基类已封装）。

public class GatlingPeaZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("gatling_pea_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;

	private final ModelPart gar;

	public GatlingPeaZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
		this.gar = this.head.getChild("gar");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -20.0F, 0.0F));

		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -20.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(4, 243).addBox(-5.0F, -10.0F, 7.0F, 10.0F, 10.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(200, 224).addBox(-7.0F, -12.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(183, 236).addBox(7.0F, -10.0F, -5.0F, 1.0F, 10.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(164, 234).addBox(-8.0F, -10.0F, -5.0F, 1.0F, 10.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(122, 241).addBox(-5.0F, -13.0F, -5.0F, 10.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(224, 190).addBox(-3.0F, -6.0F, -17.0F, 6.0F, 6.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(234, 169).addBox(-4.0F, -6.0F, -17.0F, 1.0F, 6.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(179, 203).addBox(-3.0F, -7.0F, -17.0F, 6.0F, 1.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(198, 177).addBox(3.0F, -6.0F, -17.0F, 1.0F, 6.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(144, 187).addBox(-3.0F, 0.0F, -17.0F, 6.0F, 1.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -26.0F, -1.0F));


		head.addOrReplaceChild("gar",
				CubeListBuilder.create()
						.texOffs(102, 244).addBox(-3.0F, -1.0F, -5.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(83, 246).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(64, 246).addBox(1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE)
						.texOffs(44, 245).addBox(-1.0F, 1.0F, -5.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -3.0F, -17.0F));


		PartDefinition helmet = head.addOrReplaceChild("helmet",
				CubeListBuilder.create()
						.texOffs(4, 222).addBox(-9.0F, -12.0F, 1.0F, 2.0F, 15.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(21, 221).addBox(7.0F, -13.0F, 1.0F, 2.0F, 16.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(32, 219).addBox(-9.0F, -15.0F, -8.0F, 18.0F, 3.0F, 18.0F, CubeDeformation.NONE)
						.texOffs(112, 210).addBox(-8.0F, -12.0F, 3.0F, 16.0F, 13.0F, 8.0F, CubeDeformation.NONE)
						.texOffs(2, 194).addBox(-8.0F, -17.0F, -7.0F, 16.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(2, 176).addBox(-6.0F, -19.0F, -5.0F, 12.0F, 2.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(230, 210).addBox(-4.0F, 2.0F, -7.0F, 8.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		helmet.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(77, 211).addBox(-0.5255F, -0.4837F, -3.3539F, 9.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-7.0F, 2.0F, 2.0F, 0.0F, 0.7854F, 0.4363F));

		helmet.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(176, 224).addBox(-7.8337F, -1.0611F, -3.9948F, 9.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(7.0F, 3.0F, 2.0F, 0.0F, -0.7854F, -0.4363F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		super.setupAnim(state);
		this.gar.zRot = state.ageInTicks / 5;
	}

	@Override
	public ModelPart getZombieLeftHand() {
		return this.left_hand;
	}

	@Override
	public ModelPart getZombieRightHand() {
		return this.right_hand;
	}

	@Override
	public ModelPart getZombieLeftLeg() {
		return this.left_leg;
	}

	@Override
	public ModelPart getZombieRightLeg() {
		return this.right_leg;
	}

	@Override
	public ModelPart getZombieHead() {
		return this.head;
	}

	@Override
	public ModelPart getZombieUpBody() {
		return this.up;
	}

	@Override
	public ModelPart getZombieWholeBody() {
		return this.total;
	}
}