package com.lying.pvz.client.model.entity.plant.light;



import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.utils.AnimationUtil;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

// Made with Blockbench 4.1.3
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。结构：
//     total (8, 24, -8) —— 注意 x/z 不是 0，作者把整株向日葵往 +x、-z 方向挪了 8 格
//       ├─ leaves (-8, 1, 8)  —— 4 片地叶（w_r1/w_r2/e_r1/e_r2），对称 ±10°
//       └─ body   (-8, 0, 7)  —— 躯干 + head + 双手
//           ├─ stickd_r1（躯干装饰，-15° 前倾）
//           ├─ head（3 层花瓣：中心 + 中层 + 花瓣）
//           ├─ right_hand → leafw_r1（右手叶，双轴 +10°）
//           └─ left_hand  → leafe_r1（左手叶，双轴 -10°）
//
//   setupAnim 双态：
//     1. 攻击 / 超级模式：左右手 zRot 由 getUpDownUpDown(ageInTicks % 10, 10, -30) 驱动
//        —— 这是"双手上下摆动"的挥手动画，周期 10 tick，摆幅 -30°。
//     2. 常态：body.zRot 与 head.zRot 由 60 tick 周期 getUpDownUpDown 驱动，方向相反（±7.5°）
//        —— 这是"整株向日葵轻轻摇头晃脑"的 idle 呼吸动画。
//
//   ⚠️ AnimationUtil.getUpDownUpDown 与 getUpDown 若在 1.21.1 未改签名，本类可直接编译；
//      否则只在调用处包装，几何不受影响。

public class SunFlowerModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("sun_flower"), "main");

	private final ModelPart total;

	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart left_hand;
	private final ModelPart right_hand;

	public SunFlowerModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.body = this.total.getChild("body");
		this.head = this.body.getChild("head");
		this.right_hand = this.body.getChild("right_hand");
		this.left_hand = this.body.getChild("left_hand");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(8.0F, 24.0F, -8.0F));


		PartDefinition leaves = total.addOrReplaceChild("leaves",
				CubeListBuilder.create(),
				PartPose.offset(-8.0F, 1.0F, 8.0F));


		leaves.addOrReplaceChild("w_r1",
				CubeListBuilder.create()
						.texOffs(0, 24).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.1745F, 0.0F, 0.0F));

		leaves.addOrReplaceChild("w_r2",
				CubeListBuilder.create()
						.texOffs(24, 7).addBox(-2.0F, -0.5F, -7.0F, 4.0F, 1.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, -0.1745F, 0.0F, 0.0F));


		leaves.addOrReplaceChild("e_r1",
				CubeListBuilder.create()
						.texOffs(26, 14).addBox(1.0F, -0.5F, -2.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -0.5F, -1.0F, 0.0F, 0.0F, -0.1745F));

		leaves.addOrReplaceChild("e_r2",
				CubeListBuilder.create()
						.texOffs(30, 0).addBox(-7.0F, -0.5F, -2.0F, 6.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -0.5F, -1.0F, 0.0F, 0.0F, 0.1745F));


		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(26, 30).addBox(-1.0F, -14.0F, 1.0F, 2.0F, 7.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(-8.0F, 0.0F, 7.0F));

		body.addOrReplaceChild("stickd_r1",
				CubeListBuilder.create()
						.texOffs(18, 30).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		body.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(22, 20).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 13).addBox(-5.0F, -5.0F, -3.0F, 10.0F, 8.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-7.0F, -7.0F, -2.0F, 14.0F, 12.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -13.0F, 1.0F));

		PartDefinition right_hand = body.addOrReplaceChild("right_hand",
				CubeListBuilder.create(),
				PartPose.offset(-0.5F, -3.5F, 1.0F));

		right_hand.addOrReplaceChild("leafw_r1",
				CubeListBuilder.create()
						.texOffs(8, 32).addBox(-3.5F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.1745F));

		PartDefinition left_hand = body.addOrReplaceChild("left_hand",
				CubeListBuilder.create(),
				PartPose.offset(0.5F, -4.5F, 1.0F));

		left_hand.addOrReplaceChild("leafe_r1",
				CubeListBuilder.create()
						.texOffs(0, 31).addBox(0.5F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, -0.1745F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		final int tick =state.attackTime;
		if (tick > 0 && tick < state.animCD || state.isPlantInSuperMode) {
			final int T = 10;
			this.left_hand.zRot = AnimationUtil.getUpDownUpDown(state.ageInTicks % T, T, -30);
			this.right_hand.zRot = AnimationUtil.getUpDownUpDown(state.ageInTicks % T, T, -30);
		} else {
			this.left_hand.zRot = 0;
			this.right_hand.zRot = 0;
		}
		final int T = 60;
		final int time = state.existTick % 60;
		final float degree = 7.5F;
		this.body.zRot = AnimationUtil.getUpDownUpDown(time, T, degree);
		this.head.zRot = AnimationUtil.getUpDownUpDown(time, T, -degree);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}