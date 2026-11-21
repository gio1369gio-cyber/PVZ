package com.lying.pvz.client.model.entity.plant.magic;



import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.magic.StrangeCatEntity;
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
import net.minecraft.util.Mth;

// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   1. 几何 1:1 搬运。512×512 贴图 + 极多非整坐标，逐值保留，不要"顺手取整"。
//   2. 结构总览：
//        total
//          ├─ body (0,0,0) —— 64³ 主方块
//          │   └─ bone4 (0,4,0) —— 纯容器
//          │       ├─ bone5 (35,-68,-32) + xRot -15° —— 2 个方块（左右两扇"墙"）
//          │       └─ bone6 (0,0,0) —— 2 个方块（前后两端）
//          ├─ legs (0,0,0) —— 4 个方块（四角各一脚）
//          ├─ left_ear (32,-66,-6) + Z 轴 +50°
//          │   ├─ bone3 (0,0,0) —— 1 方块（不镜像）
//          │   └─ bone2 (0,0,0) + Z 轴 +50° —— 1 方块（不镜像）
//          ├─ right_ear (-35,-66,-6) + Z 轴 -50°
//          │   ├─ bone7 (0,0,0) —— 1 方块 **mirror=true**
//          │   └─ bone8 (0,0,0) + Z 轴 -50° —— 1 方块 **mirror=true**
//          └─ tail (0,-24.8669,35.9447) —— 纯容器
//              ├─ tail1 (0, 9.8669, -7.9447) —— 1 方块
//              ├─ tail2 (0, 6.7538, 22.5237) + xRot -50° —— 1 方块
//              ├─ tail3 (0, -23.3801, 21.2917) + xRot -95° —— 1 方块
//              ├─ tail4 (0, -21, 1) + xRot 15° —— 1 方块
//              ├─ tail5 (2.6699, -19.5361, -3.1281) + xRot 15° —— 纯容器
//              │   ├─ bone10 —— 4 个方块（十字尾尖）
//              │   └─ bone11 + yRot 45° —— 4 个方块（旋转 45° 的十字）
//              └─ tail6 (-2, -61, 39) + 三轴旋转 —— 3 个方块（尾梢 + 2 个小球）
//
//   3. ⚠️ mirror=true 只出现在 right_ear.bone7 / right_ear.bone8（右侧耳朵镜像）。
//      这两个是**独立节点、各自独立 builder**，不存在同 builder 内 mirror 状态混用的问题，
//      直接 .mirror() 链式开启即可。左侧 bone3 / bone2 不镜像（原版如此，不是笔误）。
//
//   4. ⚠️ MathHelper → Mth：
//        1.16.5: net.minecraft.util.math.MathHelper.cos(...)
//        1.21.1: net.minecraft.util.Mth.cos(...)
//        方法名不变，仅类名变化。
//
//   5. setupAnim 三态（由 entity.isResting() + entity.getAttackTime() 决定）：
//        - isResting()：尾巴静止（xRot = 0）
//        - getAttackTime() == 0：idle，20 tick 周期摆动（getUpDownUpDown，幅度 30°）
//        - getAttackTime() > 0：攻击中，用 |cos| 做"甩尾"曲线（0.8 - 0.8*|cos(v*tick)|）
//      依赖 StrangeCatEntity.ANIM_CD（静态常量），若 1.21.1 保持该常量则原样编译；
//      否则只需在调用处同步常量来源，动画曲线不变。

public class StrangeCatModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			StringUtil.id("strange_cat"), "main");

	private final ModelPart total;

	private final ModelPart tail;

	public StrangeCatModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.tail = this.total.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(256, 384).addBox(-33.3301F, -68.0F, -32.0F, 64.0F, 64.0F, 64.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition bone4 = body.addOrReplaceChild("bone4",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 4.0F, 0.0F));


		bone4.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(367, 0).addBox(-71.3301F, 0.0F, 0.0F, 3.0F, 10.0F, 70.0F, CubeDeformation.NONE)
						.texOffs(214, 0).addBox(-4.3301F, 0.0F, 0.0F, 3.0F, 10.0F, 70.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(35.0F, -68.0F, -32.0F, -0.2618F, 0.0F, 0.0F));


		bone4.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(288, 11).addBox(-36.3301F, -49.8827F, 31.6148F, 70.0F, 10.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(294, 40).addBox(-36.3301F, -68.0F, -35.0F, 70.0F, 10.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		total.addOrReplaceChild("legs",
				CubeListBuilder.create()
						.texOffs(368, 338).addBox(-43.3301F, -4.0F, 6.0F, 36.0F, 4.0F, 36.0F, CubeDeformation.NONE)
						.texOffs(368, 298).addBox(4.6699F, -4.0F, 6.0F, 36.0F, 4.0F, 36.0F, CubeDeformation.NONE)
						.texOffs(368, 258).addBox(-43.3301F, -4.0F, -42.0F, 36.0F, 4.0F, 36.0F, CubeDeformation.NONE)
						.texOffs(367, 217).addBox(4.6699F, -4.0F, -42.0F, 36.0F, 4.0F, 36.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		PartDefinition left_ear = total.addOrReplaceChild("left_ear",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(32.0F, -66.0F, -6.0F, 0.0F, 0.0F, 0.8727F));

		left_ear.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(479, 302).addBox(-15.0486F, -5.4591F, -3.999F, 9.0F, 20.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		left_ear.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(459, 0).addBox(-13.855F, 1.0189F, -4.0F, 19.0F, 7.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.8727F));

		PartDefinition right_ear = total.addOrReplaceChild("right_ear",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(-35.0F, -66.0F, -6.0F, 0.0F, 0.0F, -0.8727F));

		right_ear.addOrReplaceChild("bone7",
				CubeListBuilder.create().mirror()
						.texOffs(479, 262).addBox(6.0486F, -5.4591F, -3.99F, 9.0F, 20.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		right_ear.addOrReplaceChild("bone8",
				CubeListBuilder.create().mirror()
						.texOffs(459, 32).addBox(-5.145F, 1.0189F, -4.0F, 19.0F, 7.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.8727F));

		PartDefinition tail = total.addOrReplaceChild("tail",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.8669F, 35.9447F));

		tail.addOrReplaceChild("tail1",
				CubeListBuilder.create()
						.texOffs(6, 448).addBox(-5.3301F, -5.0F, -5.0F, 8.0F, 8.0F, 37.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 9.8669F, -7.9447F));


		tail.addOrReplaceChild("tail2",
				CubeListBuilder.create()
						.texOffs(304, 323).addBox(-5.32F, -29.2438F, -2.3326F, 8.0F, 32.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 6.7538F, 22.5237F, -0.8727F, 0.0F, 0.0F));


		tail.addOrReplaceChild("tail3",
				CubeListBuilder.create()
						.texOffs(222, 331).addBox(-5.3301F, -28.5396F, -11.7591F, 8.0F, 8.0F, 25.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -23.3801F, 21.2917F, -1.6581F, 0.0F, 0.0F));

		tail.addOrReplaceChild("tail4",
				CubeListBuilder.create()
						.texOffs(168, 372).addBox(-5.3301F, -2.336F, 43.0566F, 8.0F, 4.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -21.0F, 1.0F, 0.2618F, 0.0F, 0.0F));


		PartDefinition tail5 = tail.addOrReplaceChild("tail5",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(2.6699F, -19.5361F, -3.1281F, 0.2618F, 0.0F, 0.0F));

		tail5.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.2426F, -28.9745F, 43.9555F, 6.0F, 27.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-7.2426F, -28.9745F, 51.4408F, 6.0F, 27.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-11.4853F, -28.9745F, 48.1982F, 7.0F, 27.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-4.0F, -28.9745F, 48.1982F, 7.0F, 27.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		tail5.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-42.2026F, -28.9745F, 25.9599F, 6.0F, 27.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-42.2026F, -28.9745F, 33.4452F, 6.0F, 27.0F, 7.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-46.4452F, -28.9745F, 30.2026F, 7.0F, 27.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-38.9599F, -28.9745F, 30.2026F, 7.0F, 27.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		tail.addOrReplaceChild("tail6",
				CubeListBuilder.create()
						.texOffs(377, 272).addBox(4.6394F, -8.0881F, -8.206F, 4.0F, 4.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(368, 225).addBox(7.6394F, -10.0881F, -10.206F, 3.0F, 3.0F, 3.0F, CubeDeformation.NONE)
						.texOffs(475, 226).addBox(-1.3606F, -6.0881F, -6.206F, 8.0F, 8.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-2.0F, -61.0F, 39.0F, -0.2618F, -0.1745F, -0.6981F));

		return LayerDefinition.create(mesh, 512, 512);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		final int time = state.attackTime;
		if (state.isResting) {
			this.tail.xRot = 0;
		} else if (time == 0) {
			final int T = 20;
			final int now = state.existTick % T;
			this.tail.xRot = AnimationUtil.getUpDownUpDown(now, T, 30);
		} else {
			final int tick = StrangeCatEntity.ANIM_CD - time;
			final float v = 3.14159F / StrangeCatEntity.ANIM_CD / 2;
			this.tail.xRot = 0.8F - 0.8F * Math.abs(Mth.cos(v * tick));
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}