package com.lying.pvz.client.model.entity.misc;


import com.lying.pvz.client.render.state.entity.misc.PvZEntityRenderState;
import com.lying.pvz.common.init.PvZEntityRegister;
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
 * 掉落物（Drop）模型。
 *
 * <pre>
 * body（setPos(0,-8,0)，1 个方块：64×64 平面，厚度 0）
 * </pre>
 *
 * setupAnim：当 {@code state.entityType == PvZEntityRegister.SUN.get()} 时，
 * body 绕 Z 轴持续旋转（{@code state.ageInTicks / 50} 弧度），否则 zRot 归零。
 * 泛型 {@code T} 必须与渲染器的 render state 类型一致，否则 {@code setupAnim} 覆盖失败。
 */
public class DropModel<T extends PvZEntityRenderState> extends EntityModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id( "drop"), "main");

	private final ModelPart body;

	public DropModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		root.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-32.0F, -32.0F, 0.0F, 64.0F, 64.0F, 0.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, -8.0F, 0.0F));


		return LayerDefinition.create(mesh, 128, 128);
	}

	@Override
	public void setupAnim(T state) {
		super.setupAnim(state);
		if (state.entityType == PvZEntityRegister.SUN.get()) {
			this.body.zRot = state.ageInTicks / 50;
		} else {
			this.body.zRot = 0;
		}
	}
}