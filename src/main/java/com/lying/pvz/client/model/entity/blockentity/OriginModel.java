package com.lying.pvz.client.model.entity.blockentity;

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
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * 原点（Origin）模型，texture 32×32。
 *
 * <pre>
 * total（setPos(0,24,0)，无方块，仅变换节点）
 *   ├─ center（setPos(0,-3,0)，1 个方块：内芯 3×3×3）
 *   └─ layer （setPos(0,-3,0)，1 个方块：外壳 6×6×6）
 * </pre>
 *
 * 两层同心立方体：内芯 3×3×3 静止，外壳 6×6×6 将内芯完全包住。
 * 二者均在 total 下、同一位置 (0,-3,0)，构成"核心 + 旋转罩"的观感。
 *
 * setupAnim：先调用 {@code super.setupAnim(state)}（即 {@code resetPose()}），
 * 随后 layer 三轴分别按 {@code state.ageInTicks} 持续旋转：
 * <pre>
 * layer.xRot = ageInTicks * 0.03
 * layer.yRot = ageInTicks * 0.035
 * layer.zRot = ageInTicks * 0.025
 * </pre>
 * 三轴速率不同且不可公度，外壳呈缓慢翻滚。center 不参与动画。
 */
public class OriginModel extends EntityModel<EntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("origin"), "main");

	private final ModelPart total;

	private final ModelPart layer;

	public OriginModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
		this.layer = this.total.getChild("layer");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		total.addOrReplaceChild("center",
				CubeListBuilder.create()
						.texOffs(0, 12).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -3.0F, 0.0F));

		total.addOrReplaceChild("layer",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -3.0F, 0.0F));


		return LayerDefinition.create(mesh, 32, 32);
	}

	@Override
	public void setupAnim(EntityRenderState state) {
		super.setupAnim(state);
		this.layer.xRot = state.ageInTicks * 0.03F;
		this.layer.yRot = state.ageInTicks * 0.035F;
		this.layer.zRot = state.ageInTicks * 0.025F;
	}


}