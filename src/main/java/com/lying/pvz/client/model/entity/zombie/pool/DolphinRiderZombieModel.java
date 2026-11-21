package com.lying.pvz.client.model.entity.zombie.pool;

import com.lying.pvz.client.model.entity.zombie.PvZZombieModel;
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
//   几何 1:1 搬运。海豚骑士僵尸模型（不含海豚，海豚在独立 DolphinRiderModel 中）：
//     total（根，无方块）
//       ├─ left_leg（setPos(4,-24,0)，2 个方块：腿 + 脚）
//       ├─ right_leg（setPos(-4,-24,0)，2 个方块：腿 + 脚）
//       └─ up（setPos(0,-24,0)）
//            ├─ head（setPos(0,-24,0)，1 个头部方块）
//            ├─ right_hand（setPos(-9,-22,0)，1 个手方块）
//            ├─ left_hand（setPos(9,-22,0)，1 个手方块）
//            └─ body（setPos(0,0,0)，1 个躯干方块）
//   setupAnim：全部继承 PVZZombieModel，本类不重写。
//   所有方块 CubeDeformation.NONE，无 MathHelper 调用。
//   与 NormalZombieModel 的差异：手部偏移 ±9（vs ±12），手方块宽 4（vs 8）。

public class DolphinRiderZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("dolphin_rider_zombie"), "main");

	private final ModelPart total;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart up;
	private final ModelPart head;
	private final ModelPart right_hand;
	private final ModelPart left_hand;
	private final ModelPart body;

	public DolphinRiderZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.left_leg = this.total.getChild("left_leg");
		this.right_leg = this.total.getChild("right_leg");
		this.up = this.total.getChild("up");
		this.head = this.up.getChild("head");
		this.right_hand = this.up.getChild("right_hand");
		this.left_hand = this.up.getChild("left_hand");
		this.body = this.up.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(112, 100).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 24.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 22.0F, -6.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(4.0F, -24.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(93, 100).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 24.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-2.0F, 22.0F, -6.0F, 4.0F, 2.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -24.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 104).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(112, 66).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 24.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(-9.0F, -22.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(112, 66).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 24.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offset(9.0F, -22.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-7.0F, -24.0F, -3.0F, 14.0F, 24.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
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