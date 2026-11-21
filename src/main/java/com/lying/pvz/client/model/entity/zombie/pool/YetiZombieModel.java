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


// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
//
// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   几何 1:1 搬运。雪人僵尸模型（放大版僵尸，256×256 纹理）：
//     total（根，setPos(0, 24, 2)）
//       ├─ right_leg（setPos(-7,-36,0)，2 个方块：腿 12×36×12 + 脚 12×2×6）
//       ├─ left_leg（setPos(6,-36,0)，2 个方块：腿 12×36×12 + 脚 12×2×6）
//       └─ up（setPos(0,-36,0)）
//            ├─ body（1 个 24×36×16 躯干方块）
//            ├─ left_hand（setPos(18,-30,0)，1 个 12×40×12 手方块）
//            ├─ right_hand（setPos(-19,-30,0)，1 个 12×40×12 手方块）
//            └─ head（setPos(0,-36,0)，2 个方块：24×24×24 头 + 下巴）
//   setupAnim：全部继承 PVZZombieModel，本类不重写。
//   所有方块 CubeDeformation.NONE，无 MathHelper 调用。
//   与普通僵尸的比例：腿 36 高（vs 24）、躯干 36 高（vs 24）、头 24×24×24（vs 16×16×16），
//   整体约 1.5 倍放大。

public class YetiZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("yeti_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;

	public YetiZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 2.0F));


		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(5, 152).addBox(-5.0F, 0.0F, -6.0F, 12.0F, 36.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(156, 164).addBox(-5.0F, 34.0F, -12.0F, 12.0F, 2.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-7.0F, -36.0F, 0.0F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(8, 206).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 36.0F, 12.0F, CubeDeformation.NONE)
						.texOffs(155, 178).addBox(-6.0F, 34.0F, -12.0F, 12.0F, 2.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(6.0F, -36.0F, 0.0F));

		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -36.0F, 0.0F));

		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(174, 202).addBox(-12.0F, -36.0F, -8.0F, 24.0F, 36.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(204, 144).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 40.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(18.0F, -30.0F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(203, 86).addBox(-5.0F, -6.0F, -6.0F, 12.0F, 40.0F, 12.0F, CubeDeformation.NONE),
				PartPose.offset(-19.0F, -30.0F, 0.0F));


		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(68, 205).addBox(-12.0F, -24.0F, -12.0F, 24.0F, 24.0F, 24.0F, CubeDeformation.NONE)
						.texOffs(156, 195).addBox(-4.0F, -1.0F, -13.0F, 8.0F, 5.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -36.0F, 0.0F));

		return LayerDefinition.create(mesh, 256, 256);
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