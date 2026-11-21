package com.lying.pvz.client.model.entity.zombie.roof;


import com.lying.pvz.client.model.entity.zombie.SimpleZombieModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import net.minecraft.client.model.EntityModel;
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
//   几何 1:1 搬运。蹦极僵尸模型（倒挂 + 张手）：
//     total（根，setPos(0, -12, 0) + X 45°，倒挂整体旋转）
//       ├─ right_leg（setPos(-4,12,0) + X -45° + Z 30°，1 个腿方块，X 偏移 -4.25）
//       ├─ left_leg（setPos(4,12,0) + X -45° + Z -30°，1 个腿方块，X 偏移 -3.75）
//       └─ up（setPos(0,12,0) + X 30°，无方块）
//            ├─ body（setPos(0,-7,0)，1 个躯干方块）
//            ├─ left_hand（setPos(12,-20,0) + X -90° + Y -12.5°，1 个手方块）
//            ├─ right_hand（setPos(-12,-20,0) + X -92.5° + Y 12.5°，1 个手方块）
//            └─ head（setPos(0,-24,0) + X -62.5°，1 个头部方块）
//   setupAnim：
//     - 有乘客（被蹦极吊起）时，双手 yRot 随 attackTime 变化，模拟"抓住受害者"的动作
//     - 无乘客时，双手 yRot 恢复为初始值（±12.5°）
//   注意：左右手的初始 PartPose 中，left_hand 的 Y 是 -0.2182F、right_hand 的 Y 是 0.2182F，
//         与 setupAnim 中无乘客分支的值一致；有乘客分支会在此基础上有 ±0.5F 的偏移。

public class BungeeZombieModel extends SimpleZombieModel<PvZZombieRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("bungee_zombie"), "main");

	private final ModelPart total;

	private final ModelPart left_hand;
	private final ModelPart right_hand;

	public BungeeZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		ModelPart up = this.total.getChild("up");
		this.left_hand = up.getChild("left_hand");
		this.right_hand = up.getChild("right_hand");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.7854F, 0.0F, 0.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(44, 0).addBox(-4.25F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-4.0F, 12.0F, 0.0F, -0.7854F, 0.0F, 0.5236F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-3.75F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(4.0F, 12.0F, 0.0F, -0.7854F, 0.0F, -0.5236F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.5236F, 0.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 41).addBox(-8.0F, -17.0F, -4.0F, 16.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -7.0F, 0.0F));

		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(12.0F, -20.0F, 0.0F, -1.5708F, -0.2182F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(-12.0F, -20.0F, 0.0F, -1.6144F, 0.2182F, 0.0F));

		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(16, 96).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, -1.0908F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(PvZZombieRenderState state) {
		if (state.passengers != null && !state.passengers.isEmpty()) {
			int time = Math.max(0, state.attackTime);
			this.left_hand.yRot = -0.2182F + (1 - time * 1F / state.stayTime) * 0.5F;
			this.right_hand.yRot = 0.2182F - (1 - time * 1F / state.stayTime) * 0.5F;
		} else {
			this.left_hand.yRot = -0.2182F;
			this.right_hand.yRot = 0.2182F;
		}
	}

}