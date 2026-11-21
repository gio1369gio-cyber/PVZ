package com.lying.pvz.client.model.entity.bullet;

import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
 * 荆棘（Thorn）模型。
 *
 * <pre>
 * total（setPos(0,24,0)，1 个方块：主体 2×2×3）
 *   ├─ bone（setPos(0,-1,-1)，setRotationAngle(-0.7854,0,0)，1 个方块：2×2×2，CubeDeformation -0.3）
 *   └─ bone2（setPos(0,-1,-1)，setRotationAngle(0,-0.7854,0)，1 个方块：2×2×2，CubeDeformation -0.3）
 * </pre>
 *
 * total 为主体，bone 绕 X 轴 -0.7854 rad、bone2 绕 Y 轴 -0.7854 rad 构成两根斜刺。
 * setupAnim 为空，无旋转/摆动，姿态完全由 createBodyLayer 的初始 pose 决定。
 */
public class ThornModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id( "thorn"), "main");

	private final ModelPart total;

	public ThornModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(0, 5).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, -0.7854F, 0.0F, 0.0F));


		total.addOrReplaceChild("bone2",
				CubeListBuilder.create()
						.texOffs(0, 9).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(mesh, 16, 16);
	}

	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
	}


}