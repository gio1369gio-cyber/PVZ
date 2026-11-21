package com.lying.pvz.client.model.entity.bullet;

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
 * 靶心箭（Target Arrow）模型。
 *
 * <pre>
 * total（setPos(0,24,-1)，2 个方块：箭杆 1×1×6（CubeDeformation -0.3）+ 靶面 8×8×0）
 *   ├─ cube_r1（setPos(0,-4,-2)，setRotationAngle(0,-0.7854,0)，1 个方块：箭头 1×1×1，CubeDeformation -0.3）
 *   └─ bone（setPos(0,-4,3.25)，1 个方块：尾羽 1×2×2，CubeDeformation -0.45）
 *        ├─ cube_r2（setRotationAngle(0,0,1.5708)，1 个方块）
 *        ├─ cube_r3（setRotationAngle(0,0,-3.1416)，1 个方块）
 *        └─ cube_r4（setRotationAngle(0,0,-1.5708)，1 个方块）
 * </pre>
 *
 * total 主体为箭杆 + 带孔靶面；cube_r1 是箭头，bone 及其 3 个子方块绕 Z 轴 90° 等分构成四向尾羽。
 * setupAnim 为空，无旋转/摆动，姿态完全由 createBodyLayer 的初始 pose 决定。
 */
public class TargetArrowModel extends EntityModel<PvZEntityRenderState> {


	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("target_arrow"), "main");

	private final ModelPart total;


	public TargetArrowModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}


	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 1).addBox(-0.5F, -4.5F, -2.3F, 1.0F, 1.0F, 6.0F, new CubeDeformation(-0.3F))
						.texOffs(0, 8).addBox(-4.0F, -8.0F, -2.0F, 8.0F, 8.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 24.0F, -1.0F));


		total.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(12, 0).addBox(-0.5F, -0.5F, -0.55F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)),
				PartPose.offsetAndRotation(0.0F, -4.0F, -2.0F, 0.0F, -0.7854F, 0.0F));


		PartDefinition bone = total.addOrReplaceChild("bone",
				CubeListBuilder.create()
						.texOffs(10, 3).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offset(0.0F, -4.0F, 3.25F));

		bone.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(10, 3).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));


		bone.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(10, 3).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));


		bone.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(10, 4).addBox(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.45F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));


		return LayerDefinition.create(mesh, 16, 16);
	}

	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
	}

}