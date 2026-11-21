package com.lying.pvz.client.model.entity.zombie.grass;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.common.level.entity.zombie.grass.BackupDancerEntity;
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
//   几何 1:1 搬运。伴舞僵尸模型：
//     total（根，无方块）
//       ├─ right_leg（setPos(5,-30,0)，2 个方块：腿 + 脚）
//       ├─ left_leg（setPos(-5,-30,0)，2 个方块：腿 + 脚）
//       └─ up（setPos(0,-29,0)）
//            ├─ left_hand（setPos(11,-22,1)，1 个手方块）
//            ├─ right_hand（setPos(-11,-23,0)，1 个手方块）
//            ├─ head（setPos(0,-25,0)，1 个头部方块）
//            └─ body（setPos(0,-1,0)，1 个躯干方块）
//   setupAnim：若 getAttackTime() > 0，进入"舞蹈模式"：
//     total.yRot = -sin(2π·tick/max) —— 整体左右摇摆
//     right_hand/left_hand.xRot = -3·|sin(4π·tick/max)| —— 双手高频下摆
//     left_leg/right_leg.xRot = 0 —— 双腿锁死
//     否则走默认 PVZZombieModel 逻辑。
//   refreshAnim：total.yRot = 0（恢复朝向）。
//   与 DancingZombieModel 的舞蹈曲线完全一致，仅 up 的 Y 偏移略有差异（-29 vs -30）。

public class BackupDancerModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("backup_dancer"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;
	private final ModelPart body;

	public BackupDancerModel(ModelPart root) {
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
				PartPose.offset(0.0F, -29.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(60, 188).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(11.0F, -22.0F, 1.0F));

		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(6, 179).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-11.0F, -23.0F, 0.0F));


		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -14.0F, -6.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -25.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(141, 224).addBox(-8.0F, -24.0F, -3.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}

	@Override
	public void setupAnim(PvZZombieRenderState state) {
		super.setupAnim(state);
		if (state.attackTime > 0){
			int tick  = state.attackTime;
			int max = BackupDancerEntity.DANCE_CD;
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