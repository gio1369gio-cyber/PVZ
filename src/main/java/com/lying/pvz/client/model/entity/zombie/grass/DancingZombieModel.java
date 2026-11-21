package com.lying.pvz.client.model.entity.zombie.grass;


import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.DancingZombieEntity;
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
//   几何 1:1 搬运。舞王僵尸模型：
//     total（根，无方块）
//       ├─ right_leg（setPos(5,-30,0)，2 个方块：脚 + 腿）
//       ├─ left_leg（setPos(-5,-30,0)，2 个方块：脚 + 腿）
//       └─ up（setPos(0,-30,0)）
//            ├─ left_hand（setPos(11,-21,1)，1 个手方块）
//            ├─ right_hand（setPos(-11,-22,0)，1 个手方块）
//            ├─ head（setPos(0,-24,0)，10 个方块：头 + 帽 + 头发 + 五官）
//            └─ body（setPos(0,0,0)，1 个躯干方块）
//   setupAnim 三种状态（按优先级）：
//     1. getSummonTime() > 0：召唤模式，双手/双腿锁死，右手 -2.5f
//     2. getAttackTime() > 0：舞蹈模式，同 BackupDancerModel 曲线
//     3. 其他：走默认 PVZZombieModel 逻辑
//   refreshAnim：total.yRot = 0。
//   与 BackupDancerModel 的差异：up 偏移 -30（vs -29），head 有 10 个方块（vs 1）。

public class DancingZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("dancing_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;
	private final ModelPart body;

	public DancingZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
		this.body = this.up.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(218, 239).addBox(-4.0F, 24.0F, -6.0F, 8.0F, 6.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(0, 226).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(5.0F, -30.0F, 0.0F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(36, 239).addBox(-4.0F, 24.0F, -6.0F, 8.0F, 6.0F, 11.0F, CubeDeformation.NONE)
						.texOffs(96, 226).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-5.0F, -30.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -30.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(60, 188).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(11.0F, -21.0F, 1.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(6, 179).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-11.0F, -22.0F, 0.0F));


		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-6.0F, -12.0F, -5.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(200, 204).addBox(-7.0F, -18.0F, -5.0F, 14.0F, 6.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(232, 177).addBox(-8.0F, -12.0F, -1.0F, 2.0F, 5.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(132, 204).addBox(6.0F, -12.0F, -1.0F, 2.0F, 5.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(162, 212).addBox(-6.0F, -12.0F, 7.0F, 12.0F, 5.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(70, 228).addBox(-7.0F, -9.0F, -7.0F, 1.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(31, 228).addBox(6.0F, -9.0F, -7.0F, 1.0F, 1.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(110, 216).addBox(-1.0F, -9.0F, -7.0F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(198, 197).addBox(-6.0F, -11.0F, -7.0F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(205, 176).addBox(1.0F, -11.0F, -7.0F, 5.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(141, 224).addBox(-8.0F, -24.0F, -3.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		super.setupAnim(state);
		if (state.summonTime > 0) {
			this.total.yRot = 0;
			this.left_hand.xRot = 0;
			this.right_hand.xRot = -2.5f;
			this.left_leg.xRot = 0;
			this.right_leg.xRot = 0;
			return;
		}
		if (state.attackTime > 0) {
			int tick = state.attackTime;
			int max = DancingZombieEntity.DANCE_CD;
			this.total.yRot = -Mth.sin(3.14159f * 2 * tick / max);
			this.right_hand.xRot = -3 * Mth.abs(Mth.sin(3.14159f * 4 * tick / max));
			this.left_hand.xRot = -3 * Mth.abs(Mth.sin(3.14159f * 4 * tick / max));
			this.left_leg.xRot = 0;
			this.right_leg.xRot = 0;
			return;
		}
	}

	@Override
	public void refreshAnim() {
		this.total.yRot = 0;
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