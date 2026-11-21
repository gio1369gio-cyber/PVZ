package com.lying.pvz.client.model.entity.zombie.grass;

import java.util.Optional;

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

// Made with Blockbench 3.7.1
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。路障僵尸模型：
//     total（根，无方块）
//       ├─ right_leg（setPos(-4,-24,0)，1 个腿方块）
//       ├─ left_leg（setPos(4,-24,0)，1 个腿方块）
//       └─ up（setPos(0,0,0)，无方块）
//            ├─ body（setPos(0,-24,0)，1 个躯干方块）
//            ├─ left_hand（setPos(12,-44,0)，1 个手方块）
//            ├─ right_hand（setPos(-12,-44,0)，1 个手方块）
//            └─ head（setPos(0,-48,0)，1 个头部方块）
//                 └─ defence（setPos(0,-16,0)，无方块，三层路障）
//                      ├─ defence3（setPos(0,17,0)，2 个方块，锥尖）
//                      ├─ defence2（setPos(0,-6,0)，2 个方块，锥中）
//                      └─ defence1（setPos(0,1,0)，6 个方块，锥底）
//   updateFreeParts：defence3/2/1.visible = entity.hasConeHead(3/2/1)，路障逐层脱落。
//   getHelmet：返回 defence。
//   注意：up 的 Y 偏移为 0，不是 -24；legs 与 up 都在 total 下同一层级。
//   所有方块 CubeDeformation.NONE，无 MathHelper 调用。

public class ConeHeadZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("cone_head_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;
	private final ModelPart defence;
	private final ModelPart defence1;
	private final ModelPart defence2;
	private final ModelPart defence3;

	public ConeHeadZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
		this.defence = this.head.getChild("defence");
		this.defence1 = this.defence.getChild("defence1");
		this.defence2 = this.defence.getChild("defence2");
		this.defence3 = this.defence.getChild("defence3");
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
				PartPose.offset(0.0F, 0.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(12.0F, -44.0F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-12.0F, -44.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(16, 96).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -48.0F, 0.0F));


		PartDefinition defence = head.addOrReplaceChild("defence",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -16.0F, 0.0F));


		defence.addOrReplaceChild("defence3",
				CubeListBuilder.create()
						.texOffs(205, 59).addBox(-2.0F, -37.0F, -2.0F, 4.0F, 3.0F, 4.0F, CubeDeformation.NONE)
						.texOffs(234, 60).addBox(-1.0F, -39.0F, -1.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 17.0F, 0.0F));


		defence.addOrReplaceChild("defence2",
				CubeListBuilder.create()
						.texOffs(142, 81).addBox(-3.0F, -11.0F, -3.0F, 6.0F, 4.0F, 6.0F, CubeDeformation.NONE)
						.texOffs(217, 26).addBox(-5.0F, -7.0F, -5.0F, 10.0F, 5.0F, 10.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -6.0F, 0.0F));


		defence.addOrReplaceChild("defence1",
				CubeListBuilder.create()
						.texOffs(139, 2).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 2.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(216, 4).addBox(-9.0F, -3.0F, -9.0F, 18.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(215, 15).addBox(-9.0F, -3.0F, 8.0F, 18.0F, 3.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(139, 30).addBox(-9.0F, -3.0F, -8.0F, 1.0F, 3.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(181, 28).addBox(8.0F, -3.0F, -8.0F, 1.0F, 3.0F, 16.0F, CubeDeformation.NONE)
						.texOffs(139, 56).addBox(-7.0F, -9.0F, -7.0F, 14.0F, 6.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 1.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
	}


	@Override
	public void updateFreeParts(PvZZombieRenderState state) {
		super.updateFreeParts(state);
		this.defence3.visible = state.innerDefenceStage >= 3;
		this.defence2.visible = state.innerDefenceStage >= 2;
		this.defence1.visible = state.innerDefenceStage >= 1;
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

	@Override
	public Optional<ModelPart> getHelmet() {
		return Optional.ofNullable(this.defence);
	}
}