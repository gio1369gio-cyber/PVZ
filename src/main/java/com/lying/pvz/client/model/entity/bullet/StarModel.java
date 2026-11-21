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
 * 星星（Star）模型。
 *
 * <pre>
 * total（setPos(0,24,0)，setRotationAngle(0,0.7854,0)，1 个方块：星瓣 3×1×3）
 *   ├─ cube_r1（setPos(0,0,0)，setRotationAngle(0,2.5133,0)，1 个方块：星瓣 3×1×3）
 *   ├─ cube_r2（setPos(0,0,0)，setRotationAngle(0,1.2566,0)，1 个方块：星瓣 3×1×3）
 *   ├─ cube_r3（setPos(0,0,0)，setRotationAngle(0,-2.5133,0)，1 个方块：星瓣 3×1×3）
 *   └─ cube_r4（setPos(0,0,0)，setRotationAngle(0,-1.2566,0)，1 个方块：星瓣 3×1×3）
 * </pre>
 *
 * 5 个 3×1×3 方块绕 Y 轴等角分布（0.7854 / 1.2566 / 2.5133 / -1.2566 / -2.5133 rad），
 * 构成平面星形。setupAnim 为空，无旋转/摆动，姿态完全由 createBodyLayer 的初始 pose 决定。
 */
public class StarModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id( "star"), "main");

	private final ModelPart total;

	public StarModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 12).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		total.addOrReplaceChild("cube_r1",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.5133F, 0.0F));


		total.addOrReplaceChild("cube_r2",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.2566F, 0.0F));


		total.addOrReplaceChild("cube_r3",
				CubeListBuilder.create()
						.texOffs(0, 4).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.5133F, 0.0F));


		total.addOrReplaceChild("cube_r4",
				CubeListBuilder.create()
						.texOffs(0, 8).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.2566F, 0.0F));

		return LayerDefinition.create(mesh, 16, 16);
	}

	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
	}

}