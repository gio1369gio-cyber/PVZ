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
//   1. 几何 1:1 搬运。骨架 + 坚果头 + 眼睛 + 三级血条。
//   2. head 子树：
//        head（5 方块：20³ 主体 + 顶/底盖 + 左右壳）
//          └─ face（眼睛容器，1 个固定眼睛）
//              ├─ stage1（血量 > 2/3：正常眼位）
//              ├─ stage2（血量 > 1/3：中间眼位）
//              └─ stage3（血量 ≤ 1/3：低眼位）
//      stage1/2/3 三组几何完全一样（各 2 个 1×1×1 小方块），只有 y 偏移不同，
//      通过 updateFreeParts 切换 visible 来表现"血量越低眼睛越下垂"。
//   3. ⚠️ 重写了 updateFreeParts(WallNutZombieEntity entity)：
//        1.16.5 原版第一步 super.updateFreeParts(entity)；
//        然后按 health/maxHealth 比值三分段，stage1/2/3 互斥显示。
//      注意三分段边界值：> 2/3、> 1/3、<= 1/3（stage3 是 <=，不是 <）。
//   4. 无 setupAnim 重写 —— 交给基类。

public class WallNutZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("wall_nut_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;

	private final ModelPart stage1;
	private final ModelPart stage2;
	private final ModelPart stage3;

	public WallNutZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
		final ModelPart face = this.head.getChild("face");
		this.stage1 = face.getChild("stage1");
		this.stage2 = face.getChild("stage2");
		this.stage3 = face.getChild("stage3");
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
						.texOffs(92, 134).addBox(-8.0F, -1.0F, -8.0F, 16.0F, 1.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 135).addBox(-8.0F, -26.0F, -8.0F, 16.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(0, 180).addBox(-10.0F, -25.0F, -10.0F, 20.0F, 24.0F, 20.0F, CubeDeformation.NONE)
						.texOffs(86, 219).addBox(-11.0F, -23.0F, -8.0F, 1.0F, 20.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(88, 178).addBox(10.0F, -23.0F, -8.0F, 1.0F, 20.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		PartDefinition face = head.addOrReplaceChild("face",
				CubeListBuilder.create()
						.texOffs(0, 248).addBox(-1.0F, -11.0F, -10.1F, 2.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		face.addOrReplaceChild("stage1",
				CubeListBuilder.create()
						.texOffs(1, 252).addBox(-2.0F, -2.0F, 0.15F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 243).addBox(1.0F, -2.0F, 0.15F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -10.0F, -10.25F));

		face.addOrReplaceChild("stage2",
				CubeListBuilder.create()
						.texOffs(1, 252).addBox(-2.0F, -1.0F, 0.15F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 243).addBox(1.0F, -1.0F, 0.15F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -10.0F, -10.25F));


		face.addOrReplaceChild("stage3",
				CubeListBuilder.create()
						.texOffs(1, 252).addBox(-2.0F, 0.0F, 0.15F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(0, 243).addBox(1.0F, 0.0F, 0.15F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -10.0F, -10.25F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		final float percent = state.percentHealth;
		this.stage1.visible = (percent > 2F / 3);
		this.stage2.visible = (percent > 1F / 3);
		this.stage3.visible = (percent <= 1F / 3);
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