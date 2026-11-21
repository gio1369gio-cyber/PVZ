package com.lying.pvz.client.model.entity.zombie.zombotany;


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


// 迁移说明（1.16.5 → 1.21.1 NeoForge）：
//   1. 几何 1:1 搬运。骨架 + 南瓜头盔。
//   2. ⚠️ 关键结构差异：头盔 helmet 挂在 **up** 下（不是 head），
//      与 PeaShooter/Squash/TallNut 的"头就是 head 子树"不同：
//        up
//          ├─ body（标准躯干）
//          ├─ left_hand / right_hand（标准手）
//          ├─ head（内层 16³，作为僵尸原头）
//          └─ helmet（外层南瓜，5 面 + 1 斜柄）
//      这是 1.16.5 刻意设计：僵尸的原头在 helmet 内部，helmet 是"外壳"。
//   3. helmet 是 5 面围墙（不是完整 24³）：
//        底盖 24×1×24 + 前/后/左/右各 24×16×1 → 中间空洞，露出里面僵尸的脸。
//   4. bone 是头盔顶的一根 30° 斜柄（2×5×2 小方块）。
//   5. 无 setupAnim 重写。

public class PumpkinZombieModel extends PvZZombieModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("pumpkin_zombie"), "main");

	private final ModelPart total;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart up;
	private final ModelPart body;
	private final ModelPart left_hand;
	private final ModelPart right_hand;
	private final ModelPart head;
	private final ModelPart helmet;

	public PumpkinZombieModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		this.up = this.total.getChild("up");
		this.body = this.up.getChild("body");
		this.left_hand = this.up.getChild("left_hand");
		this.right_hand = this.up.getChild("right_hand");
		this.head = this.up.getChild("head");
		this.helmet = this.up.getChild("helmet");
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


		up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(16, 96).addBox(-8.0F, -17.0F, -8.0F, 16.0F, 16.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));


		PartDefinition helmet = up.addOrReplaceChild("helmet",
				CubeListBuilder.create()
						.texOffs(142, 4).addBox(-12.0F, -1.0F, -12.0F, 24.0F, 1.0F, 24.0F, CubeDeformation.NONE)
						.texOffs(140, 38).addBox(-12.0F, -17.0F, -12.0F, 24.0F, 16.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(198, 39).addBox(-12.0F, -17.0F, 11.0F, 24.0F, 16.0F, 1.0F, CubeDeformation.NONE)
						.texOffs(140, 63).addBox(-12.0F, -17.0F, -11.0F, 1.0F, 16.0F, 22.0F, CubeDeformation.NONE)
						.texOffs(193, 65).addBox(11.0F, -17.0F, -11.0F, 1.0F, 16.0F, 22.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		// bone：南瓜顶斜柄（30° Z 轴旋转）
		helmet.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(244, 111).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -17.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

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