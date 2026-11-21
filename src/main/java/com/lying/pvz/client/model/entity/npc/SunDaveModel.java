package com.lying.pvz.client.model.entity.npc;


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
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;


/**
 * 太阳戴夫（Sun Dave）模型，texture 128×128。
 *
 * <pre>
 * total（setPos(0,24,0)，无方块，仅变换节点）
 *   ├─ right_leg（setPos(-4,-25,0)，1 个方块：腿 7×25×6）
 *   ├─ left_leg （setPos( 3,-25,0)，1 个方块：腿 7×25×6）
 *   └─ up（setPos(0,-25,0)，无方块）
 *        ├─ body（setPos(-1,0,0)，1 个方块：躯干 14×24×8）
 *        ├─ left_hand（setPos(10,-21,0)，1 个方块：手臂 6×25×6）
 *        ├─ right_hand（setPos(-9,-21,0)，1 个方块：手臂 6×25×6，mirror）
 *        └─ head（setPos(0,-25,0)，2 个方块：头主体 14×14×14 + 下巴薄片 14×2×1）
 *             └─ ring（setPos(0,-17,0)，无方块，仅变换节点）
 *                  ├─ bone （setPos(0,0,-8)，1 个方块：横杆 16×1×1）
 *                  ├─ bone2（setPos(0,0, 7)，1 个方块：横杆 16×1×1）
 *                  ├─ bone3（setPos(-7,0,0)，1 个方块：纵杆 1×1×14）
 *                  └─ bone4（setPos( 8,0,0)，1 个方块：纵杆 1×1×14）
 * </pre>
 *
 * 结构与 {@link CrazyDaveModel} 基本一致（total / up / 双腿 / 双手 / head），
 * 差异只在头顶：帽子换成 ring。ring 由 4 根 1×1 细杆（前后 16×1×1 横杆、
 * 左右 1×1×14 纵杆）在 y=-17 处围成一圈，位于头顶上方，构成"太阳环"。
 *
 * setupAnim：head 的 yRot/xRot 由 {@code state.yRot}/{@code state.xRot}（度）换算为弧度；
 * 四肢按 {@code state.walkAnimationPos} × 0.6662F 的余弦做行走摆动，左右相位差 π；
 * ring.yRot 随 {@code state.ageInTicks / 4} 持续旋转，速率约为 CrazyDaveModel 帽子（1 rad/tick）
 * 的 1/4，环在头顶缓慢旋转。
 */
public class SunDaveModel extends EntityModel<LivingEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("sun_dave"), "main");

	private final ModelPart total;
	private final ModelPart head;
	private final ModelPart ring;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart right_hand;
	private final ModelPart left_hand;

	public SunDaveModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		ModelPart up = this.total.getChild("up");
		this.left_hand = up.getChild("left_hand");
		this.right_hand = up.getChild("right_hand");
		this.head = up.getChild("head");
		this.ring = this.head.getChild("ring");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		total.addOrReplaceChild("right_leg",
				CubeListBuilder.create()
						.texOffs(1, 96).addBox(-3.0F, 0.0F, -3.0F, 7.0F, 25.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(-4.0F, -25.0F, 0.0F));


		total.addOrReplaceChild("left_leg",
				CubeListBuilder.create()
						.texOffs(30, 96).addBox(-3.0F, 0.0F, -3.0F, 7.0F, 25.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(3.0F, -25.0F, 0.0F));


		PartDefinition up = total.addOrReplaceChild("up",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -25.0F, 0.0F));


		up.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(59, 94).addBox(-6.0F, -24.0F, -4.0F, 14.0F, 24.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(-1.0F, 0.0F, 0.0F));


		up.addOrReplaceChild("left_hand",
				CubeListBuilder.create()
						.texOffs(102, 3).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 25.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(10.0F, -21.0F, 0.0F));


		up.addOrReplaceChild("right_hand",
				CubeListBuilder.create()
						.texOffs(102, 36).addBox(-4.0F, -3.0F, -3.0F, 6.0F, 25.0F, 6.0F, CubeDeformation.NONE).mirror(),
				PartPose.offset(-9.0F, -21.0F, 0.0F));


		PartDefinition head = up.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(1, 1).addBox(-7.0F, -13.0F, -7.0F, 14.0F, 14.0F, 14.0F, CubeDeformation.NONE)
						.texOffs(1, 33).addBox(-7.0F, 1.0F, -7.0F, 14.0F, 2.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -25.0F, 0.0F));


		PartDefinition ring = head.addOrReplaceChild("ring",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, -17.0F, 0.0F));


		ring.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(61, 2).addBox(-8.0F, -1.0F, 0.0F, 16.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, -8.0F));


		ring.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(62, 20).addBox(-8.0F, -1.0F, 0.0F, 16.0F, 1.0F, 1.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.0F, 7.0F));


		ring.addOrReplaceChild("bone3",
				CubeListBuilder.create()
						.texOffs(63, 27).addBox(-1.0F, -1.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(-7.0F, 0.0F, 0.0F));


		ring.addOrReplaceChild("bone4",
				CubeListBuilder.create()
						.texOffs(62, 46).addBox(-1.0F, -1.0F, -7.0F, 1.0F, 1.0F, 14.0F, CubeDeformation.NONE),
				PartPose.offset(8.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 128, 128);
	}


	@Override
	public void setupAnim(LivingEntityRenderState state) {
		super.setupAnim(state);
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
		this.right_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.left_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
		this.right_hand.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.left_hand.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
		this.ring.yRot = state.ageInTicks / 4;
	}


}