package com.lying.pvz.client.model.entity.plant.enforce;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.enforce.ChomperEntity;
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
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   1. 全模型共 30+ 部件，几何 1:1 搬运，未改任何数值。
//   2. 结构：
//        total（根）
//          ├─ body（1 个方块 + 脊柱链 + head 子树）
//          │   ├─ bone → bone2 → bone3 → bone22（4 节脊柱）
//          │   └─ head
//          │       ├─ bone14 → bone15
//          │       ├─ up_mouse（上颚 + 3 颗牙）
//          │       │   ├─ bone9 → bone8
//          │       │   ├─ bone10 → bone11
//          │       │   └─ bone12 → bone13
//          │       ├─ down_mouse（下颚）
//          │       ├─ leaf（7 片冠叶）
//          │       └─ tongue → tongue2（舌头两节）
//          └─ root（根部 + 4 条根须 bone4..bone7）
//   3. 只有 body / head / up_mouse / down_mouse / tongue / tongue2 / leaf 这 7 个部件
//      会在 setupAnim 里被改写姿态，其余部件全部静态，无需保留字段。
//   4. setupAnim 依赖：
//        - ChomperEntity.ATTACK_ANIM_CD（1.16.5 就有的静态常量，1.21.1 保持原值即可）
//        - entity.getRestTick()（是否处于"静息咀嚼"状态）
//        - entity.getRandom()（舌头随机摆动）
//      这三者在 1.21.1 实体类中若名称未变，本方法可原样编译通过。
//   5. ⚠️ 第一个分支的 `tick - T * 2f / 3`（注释写 "7-10 (1-4)"）在 1.16.5 就是这写法，
//      按 Java 优先级即 `tick - (T * 2f / 3)`；**不要**"修正"成 `tick - T / 3` 之类，
//      保持原式以免动作曲线错位。

public class ChomperModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			StringUtil.id("chomper"), "main");

	private final ModelPart total;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart up_mouse;
	private final ModelPart down_mouse;
	private final ModelPart tongue;
	private final ModelPart tongue2;
	private final ModelPart leaf;

	public ChomperModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.body = this.total.getChild("body");
		this.head = this.body.getChild("head");

		this.up_mouse = this.head.getChild("up_mouse");
		this.down_mouse = this.head.getChild("down_mouse");
		this.leaf = this.head.getChild("leaf");
		this.tongue = this.head.getChild("tongue");
		this.tongue2 = this.tongue.getChild("tongue2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition body = total.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(2, 118).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 7.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));


		PartDefinition bone = body.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(14, 118).addBox(-1.0F, 2.5352F, 3.5905F, 2.0F, 6.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -16.9572F, 1.3474F, -0.6545F, 0.0F, 0.0F));

		PartDefinition bone2 = bone.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(27, 116).addBox(-1.0F, -1.07F, 0.2365F, 2.0F, 2.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -2.8145F, -0.7628F, -0.829F, 0.0F, 0.0F));

		PartDefinition bone3 = bone2.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(49, 118).addBox(-1.0F, -7.5847F, 0.6528F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 3.3053F, -5.9933F, -0.8727F, 0.0F, 0.0F));


		bone3.addOrReplaceChild("bone22",
				CubeListBuilder.create()
						.texOffs(61, 120).addBox(-1.0F, -1.7939F, -9.8869F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 4.7601F, -3.6526F, -2.4871F, 0.0F, 0.0F));


		PartDefinition head = body.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(72, 113).addBox(-6.0F, -7.0F, -4.0F, 12.0F, 10.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -22.0F, -2.0F, 0.25F, 0.0F, 0.0F));


		PartDefinition bone14 = head.addOrReplaceChild("bone14",
				CubeListBuilder.create()
						.texOffs(105, 107).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, -0.9599F, 0.0F, 0.0F));

		bone14.addOrReplaceChild("bone15",
				CubeListBuilder.create()
						.texOffs(35, 45).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));


		PartDefinition up_mouse = head.addOrReplaceChild("up_mouse",
				CubeListBuilder.create()
						.texOffs(75, 94).addBox(6.0F, -2.0F, -11.0F, 1.0F, 4.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(22, 96).addBox(-6.0F, -3.0F, -12.0F, 12.0F, 5.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(103, 89).addBox(-5.0F, -2.0F, -13.0F, 10.0F, 4.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(100, 70).addBox(-7.0F, -2.0F, -11.0F, 1.0F, 4.0F, 11.0F, CubeDeformation.NONE)

						.texOffs(13, 110).addBox(-6.0F, 2.0F, -7.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-6.0F, 2.0F, -5.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-6.0F, 2.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-6.0F, 2.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-6.0F, 2.0F, -11.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-5.0F, 2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-3.0F, 2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(-1.0F, 2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(1.0F, 2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(3.0F, 2.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(5.0F, 2.0F, -12.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(5.0F, 2.0F, -10.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(5.0F, 2.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(5.0F, 2.0F, -6.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(13, 110).addBox(5.0F, 2.0F, -4.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.0F, -2.0F, -0.5236F, 0.0F, 0.0F));


		PartDefinition bone9 = up_mouse.addOrReplaceChild("bone9",
				CubeListBuilder.create()
						.texOffs(9, 99).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, -10.0F, -0.0873F, 0.0F, 0.0F));

		bone9.addOrReplaceChild("bone8",
				CubeListBuilder.create()
						.texOffs(51, 43).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition bone10 = up_mouse.addOrReplaceChild("bone10",
				CubeListBuilder.create()
						.texOffs(4, 90).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, -6.0F, -0.1745F, 0.0F, 0.0F));

		bone10.addOrReplaceChild("bone11",
				CubeListBuilder.create()
						.texOffs(62, 44).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition bone12 = up_mouse.addOrReplaceChild("bone12",
				CubeListBuilder.create()
						.texOffs(32, 89).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, -2.0F, -0.2618F, 0.0F, 0.0F));

		bone12.addOrReplaceChild("bone13",
				CubeListBuilder.create()
						.texOffs(19, 91).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.7854F));


		head.addOrReplaceChild("down_mouse",
				CubeListBuilder.create()
						.texOffs(46, 79).addBox(6.0F, -2.0F, -11.0F, 1.0F, 3.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(5, 63).addBox(-6.0F, -2.0F, -12.0F, 12.0F, 4.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(74, 88).addBox(-5.0F, -2.0F, -13.0F, 10.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(73, 71).addBox(-7.0F, -2.0F, -11.0F, 1.0F, 3.0F, 11.0F, CubeDeformation.NONE)

						.texOffs(5, 110).addBox(-6.0F, -3.0F, -4.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-6.0F, -3.0F, -8.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-6.0F, -3.0F, -6.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-6.0F, -3.0F, -10.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-6.0F, -3.0F, -12.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-4.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(-2.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(0.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(2.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(4.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(5.0F, -3.0F, -11.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(5.0F, -3.0F, -9.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(5.0F, -3.0F, -7.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(5, 110).addBox(5.0F, -3.0F, -5.0F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, 0.5236F, 0.0F, 0.0F));


		PartDefinition leaf = head.addOrReplaceChild("leaf",
				CubeListBuilder.create()
						.texOffs(64, 73).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		leaf.addOrReplaceChild("bone16",
				CubeListBuilder.create()
						.texOffs(58, 65).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 0.0F));

		leaf.addOrReplaceChild("bone17",
				CubeListBuilder.create()
						.texOffs(70, 64).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 1.0472F));

		leaf.addOrReplaceChild("bone18",
				CubeListBuilder.create()
						.texOffs(92, 64).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 2.0944F));


		leaf.addOrReplaceChild("bone19",
				CubeListBuilder.create()
						.texOffs(108, 61).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.2618F, 0.0F, -3.1416F));

		leaf.addOrReplaceChild("bone20",
				CubeListBuilder.create()
						.texOffs(119, 61).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, -1.0472F));

		leaf.addOrReplaceChild("bone21",
				CubeListBuilder.create()
						.texOffs(81, 57).addBox(-1.0F, -4.866F, -1.5F, 2.0F, 4.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, -2.0944F));


		PartDefinition tongue = head.addOrReplaceChild("tongue",
				CubeListBuilder.create()
						.texOffs(7, 53).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, -5.0F));

		tongue.addOrReplaceChild("tongue2",
				CubeListBuilder.create()
						.texOffs(23, 52).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -4.0F));

		PartDefinition rootPart = total.addOrReplaceChild("root",
				CubeListBuilder.create()
						.texOffs(44, 52).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		rootPart.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(68, 50).addBox(-7.0F, -1.0F, -2.0F, 7.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		rootPart.addOrReplaceChild("bone5",
				CubeListBuilder.create()
						.texOffs(97, 50).addBox(0.0F, -1.0F, -2.0F, 7.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		rootPart.addOrReplaceChild("bone6",
				CubeListBuilder.create()
						.texOffs(102, 37).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		rootPart.addOrReplaceChild("bone7",
				CubeListBuilder.create()
						.texOffs(75, 38).addBox(-2.0F, 0.0F, -7.0F, 4.0F, 1.0F, 7.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);
		final int tick = state.attackTime;
		final int T = ChomperEntity.ATTACK_ANIM_CD;


		if (tick > 0 && tick <= T / 2) {
			this.body.xRot = 0.05f * tick;
			this.head.xRot = 0.25f - 0.075f * tick;
			if (tick <= T / 3) {
				this.up_mouse.xRot = -0.52f - 0.05f * tick;
				this.down_mouse.xRot = 0.52f + 0.05f * tick;
			} else {
				float tmp = tick - T * 2f / 3;
				this.up_mouse.xRot = -0.82f + 0.2f * tmp;
				this.down_mouse.xRot = 0.82f - 0.2f * tmp;
			}
		} else if (tick > T / 2) {
			final int tmp = tick - T / 2;
			this.body.xRot = 0.5f - 0.05f * tmp;
			this.head.xRot = -0.5f + 0.075f * tmp;
			this.up_mouse.xRot = -0.05f * tmp;
			this.down_mouse.xRot = 0.05f * tmp;
		} else {
			this.body.xRot = 0;
			if (state.restTick > 0) {
				this.tongue.xRot = state.random.nextFloat() - 0.5f;
				this.tongue2.xRot = state.random.nextFloat() - 0.5f;
				this.head.xRot = 1.04f;
				this.up_mouse.xRot = -0.15f;
				this.down_mouse.xRot = 0.15f;
				this.leaf.zRot = 0.52f;
			} else {
				this.tongue.xRot = 0;
				this.tongue2.xRot = 0;
				this.head.xRot = 0.25f;
				this.up_mouse.xRot = -0.52f;
				this.down_mouse.xRot = 0.52f;
				this.leaf.zRot = 0;
			}
		}
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}