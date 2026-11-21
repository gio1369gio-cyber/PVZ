package com.lying.pvz.client.model.entity.misc;


import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
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


/**
 * 礼盒（Gift Box）模型，texture 64×64。
 *
 * <pre>
 * total（setPos(0,24,0)，2 个方块）
 *   ├─ 盒体 8×7×8（texOffs 0,0），y=-7、z=-4
 *   ├─ 顶部丝带结 2×1×2（texOffs 0,16），y=-8、z=-1
 *   ├─ cover_r1（setRotationAngle(0.0436,-0.0873,-0.0873)，1 个方块：盒盖 9×2×9）
 *   ├─ tape1_r1（setRotationAngle(-0.0873,0,0)，1 个方块：顶带 2×1×4）
 *   ├─ tape2_r1（setRotationAngle(0,0.2618,0)，1 个方块：侧带 2×2×3）
 *   └─ tapehang1_r1（setRotationAngle(0,-0.0873,0)，2 个方块：左右垂带 1×5×2）
 * </pre>
 *
 * total 承担整体位移；盒体与丝带结为其直接几何体。4 个 cube_r* 子节点分别
 * 是略微倾斜的盒盖、顶部丝带、侧面丝带、以及左右两条垂下的丝带，
 * 各带微小旋转（0.0436～0.2618 rad）营造"未完全对齐"的礼盒观感。
 *
 * setupAnim 为空实现，无旋转/摆动，姿态完全由 createBodyLayer 的初始 pose 决定。
 */
public class GiftBoxModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("gift_box"), "main");

	private final ModelPart total;

	public GiftBoxModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 16).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 8.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("cover_r1",
				CubeListBuilder.create()
						.texOffs(0, 16).addBox(-4.0F, -7.5F, -4.3F, 9.0F, 2.0F, 9.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0436F, -0.0873F, -0.0873F));


		total.addOrReplaceChild("tape1_r1",
				CubeListBuilder.create()
						.texOffs(0, 27).addBox(-1.0F, 0.4F, 0.0F, 2.0F, 1.0F, 4.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.0873F, 0.0F, 0.0F));


		total.addOrReplaceChild("tape2_r1",
				CubeListBuilder.create()
						.texOffs(12, 27).addBox(-0.5F, -1.4F, -3.3F, 2.0F, 2.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, -7.5F, 0.0F, 0.0F, 0.2618F, 0.0F));


		total.addOrReplaceChild("tapehang1_r1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.7F, -7.1F, -0.6F, 1.0F, 5.0F, 2.0F, CubeDeformation.NONE)
						.texOffs(0, 0).addBox(3.5F, -6.0F, -1.5F, 1.0F, 5.0F, 2.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.0873F, 0.0F));


		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
	}

}