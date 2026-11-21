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
 * 疯狂戴夫（Crazy Dave）模型，texture 128×128。
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
 *             └─ hat（setPos(0,-16,0)，2 个方块：帽檐 4×3×10 + 帽体 16×11×16）
 * </pre>
 *
 * total 承担整体位移；双腿直接挂在 total 下，上半身集中在 up 节点下（body、双手、head）。
 * hat 为 head 子节点，独立于 head 的偏航/俯仰之外再做绕 Y 轴自旋。
 *
 * setupAnim：head 的 yRot/xRot 由 {@code state.yRot}/{@code state.xRot}（度）换算为弧度；
 * 四肢按 {@code state.walkAnimationPos} × 0.6662F 的余弦做行走摆动，左右相位差 π；
 * hat.yRot 随 {@code state.ageInTicks} 持续旋转（速率 1 rad/tick），形成帽子打转效果。
 */
public class CrazyDaveModel extends EntityModel<LivingEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("crazy_dave"), "main");

	private final ModelPart total;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart right_hand;
	private final ModelPart left_hand;

	public CrazyDaveModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.right_leg = this.total.getChild("right_leg");
		this.left_leg = this.total.getChild("left_leg");
		ModelPart up = this.total.getChild("up");
		this.left_hand = up.getChild("left_hand");
		this.right_hand = up.getChild("right_hand");
		this.head = up.getChild("head");
		this.hat = this.head.getChild("hat");
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


		head.addOrReplaceChild("hat",
				CubeListBuilder.create()
						.texOffs(1, 40).addBox(-2.0F, -3.0F, -18.0F, 4.0F, 3.0F, 10.0F, CubeDeformation.NONE)
						.texOffs(3, 61).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 11.0F, 16.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -16.0F, 0.0F));

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
		this.hat.yRot = state.ageInTicks * 1.0f;
	}


}