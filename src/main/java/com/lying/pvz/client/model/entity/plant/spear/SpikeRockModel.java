package com.lying.pvz.client.model.entity.plant.spear;


import com.lying.pvz.client.model.entity.plant.PvZPlantModel;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.spear.SpikeRockEntity;
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

// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。尖刺岩（SpikeRock）模型：
//     total（1 个底座方块 24×1×24）
//       ├─ t1（setPos(0,0,0)，1 方块 + spike1 子件）—— 中部尖刺
//       ├─ t2（setPos(10,0,0) + Z 轴 +10°，1 方块 + spike2 子件）—— 右侧尖刺
//       ├─ t3（setPos(-10,0,0) + Z 轴 -10°，1 方块 + spike3 子件）—— 左侧尖刺
//       └─ spikes（纯容器，15 根 spikeN 分散成放射状，全在 y=-1 高度）
//
//   setupAnim 双态：
//     1. 可见性：t1/t2/t3 分别按 (spikeNum / spikesCount) > 0 / 1/3 / 2/3 逐级显示
//        —— 即"尖刺岩收集到一定数量的尖刺后，三根主刺逐个出现"。
//     2. 动画：三根主刺在攻击时按 AnimationUtil 摆动：
//        - t3.zRot = getUpDown(now, tot, +25)
//        - t2.zRot = getUpDown(now, tot, -25)   —— 反向
//        - t1.xRot = getUpDownUpDown(now, tot, +25) —— 用 UpDownUpDown 而非 UpDown
//     三根刺方向各不相同，是 1.16.5 刻意设计的"三向刺出"动画，保留原样。
//
//   依赖 SpikeRockEntity.ATTACK_ANIM_CD（静态常量），若 1.21.1 保留则原样编译。

public class SpikeRockModel extends PvZPlantModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("spike_rock"), "main");

	private final ModelPart total;

	private final ModelPart t1;
	private final ModelPart t2;
	private final ModelPart t3;

	public SpikeRockModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.t1 = this.total.getChild("t1");
		this.t2 = this.total.getChild("t2");
		this.t3 = this.total.getChild("t3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(31, 102).addBox(-12.0F, -1.0F, -12.0F, 24.0F, 1.0F, 24.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		PartDefinition t1 = total.addOrReplaceChild("t1",
				CubeListBuilder.create()
						.texOffs(119, 89).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.05F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		t1.addOrReplaceChild("spike1",
				CubeListBuilder.create()
						.texOffs(119, 96).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.7854F));


		PartDefinition t2 = total.addOrReplaceChild("t2",
				CubeListBuilder.create()
						.texOffs(108, 87).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		t2.addOrReplaceChild("spike2",
				CubeListBuilder.create()
						.texOffs(108, 95).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.7854F));


		PartDefinition t3 = total.addOrReplaceChild("t3",
				CubeListBuilder.create()
						.texOffs(98, 87).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		t3.addOrReplaceChild("spike3",
				CubeListBuilder.create()
						.texOffs(96, 95).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition spikes = total.addOrReplaceChild("spikes",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		spikes.addOrReplaceChild("spike4",
				CubeListBuilder.create()
						.texOffs(86, 96).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, -9.0F, 0.0F, 0.0F, 0.7854F));


		spikes.addOrReplaceChild("spike5",
				CubeListBuilder.create()
						.texOffs(87, 89).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(10.0F, -1.0F, -9.0F, -0.7854F, 0.7854F, 0.0F));


		spikes.addOrReplaceChild("spike6",
				CubeListBuilder.create()
						.texOffs(75, 96).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-10.0F, -1.0F, -9.0F, -0.7854F, -0.7854F, 0.0F));


		spikes.addOrReplaceChild("spike7",
				CubeListBuilder.create()
						.texOffs(76, 88).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-6.0F, -1.0F, -5.0F, -0.7854F, 0.7854F, 0.0F));


		spikes.addOrReplaceChild("spike8",
				CubeListBuilder.create()
						.texOffs(117, 81).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(6.0F, -1.0F, -5.0F, -0.7854F, -0.7854F, 0.0F));

		spikes.addOrReplaceChild("spike9",
				CubeListBuilder.create()
						.texOffs(106, 80).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-10.0F, -1.0F, 9.0F, 0.7854F, 0.7854F, 0.0F));

		spikes.addOrReplaceChild("spike10",
				CubeListBuilder.create()
						.texOffs(95, 80).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(10.0F, -1.0F, 9.0F, -0.7854F, -0.7854F, 0.0F));

		spikes.addOrReplaceChild("spike11",
				CubeListBuilder.create()
						.texOffs(85, 81).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -1.0F, 9.0F, 0.0F, 0.0F, 0.7854F));


		spikes.addOrReplaceChild("spike12",
				CubeListBuilder.create()
						.texOffs(75, 81).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-6.0F, -1.0F, 5.0F, -0.7854F, -0.7854F, 0.0F));

		spikes.addOrReplaceChild("spike13",
				CubeListBuilder.create()
						.texOffs(118, 74).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(6.0F, -1.0F, 5.0F, 0.7854F, 0.7854F, 0.0F));


		spikes.addOrReplaceChild("spike14",
				CubeListBuilder.create()
						.texOffs(105, 72).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-5.0F, -1.0F, 0.0F, 0.7854F, 0.0F, 0.0F));


		spikes.addOrReplaceChild("spike15",
				CubeListBuilder.create()
						.texOffs(90, 72).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(5.0F, -1.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZPlantRenderState state) {
		super.setupAnim(state);

		this.t2.visible = (state.spikeNum * 1F / state.spikeNumCount > 2F / 3);
		this.t3.visible = (state.spikeNum * 1F / state.spikeNumCount > 1F / 3);
		this.t1.visible = (state.spikeNum * 1F / state.spikeNumCount > 0);


		final int tot = SpikeRockEntity.ATTACK_ANIM_CD;
		final int now = tot - state.attackTime;
		final float angle = 25F;
		this.t3.zRot = AnimationUtil.getUpDown(now, tot, angle);
		this.t2.zRot = AnimationUtil.getUpDown(now, tot, -angle);
		this.t1.xRot = AnimationUtil.getUpDownUpDown(now, tot, angle);
	}

	@Override
	public ModelPart getPlantWholeBody() {
		return this.total;
	}
}