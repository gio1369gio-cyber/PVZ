package com.lying.pvz.client.model.entity.misc;


import com.lying.pvz.client.render.state.entity.misc.PvZBulletRenderState;
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
 * 鞭炮（Fire Crackers）模型，texture 64×64。
 *
 * <pre>
 * total（setPos(0,21.25,0)，setRotationAngle(0,0,0.7854)，2 个方块：主杆 2×2×17 + 末端 2×2×2，CubeDeformation -0.8/-0.75）
 *   └─ bombs（setPos(0,1,0)，无方块）
 *        ├─ bone  （setPos(0,0,8)）  → bomb1, bomb2
 *        ├─ bone2 （setPos(0,0,6)）  → bomb3, bomb4
 *        ├─ bone3 （setPos(0,0,6)）  → bomb5, bomb6
 *        ├─ bone4 （setPos(0,0,4)）  → bomb7, bomb8
 *        ├─ bone5 （setPos(0,0,4)）  → bomb9, bomb10
 *        ├─ bone6 （setPos(0,0,4)）  → bomb11, bomb12
 *        ├─ bone7 （setPos(0,0,4)）  → bomb13, bomb14
 *        ├─ bone8 （setPos(0,-1,6)，setRotationAngle(0,0,1.5708)）→ bomb15, bomb16
 *        ├─ bone9 （同上）→ bomb17, bomb18
 *        ├─ bone10（同上）→ bomb19, bomb20
 *        ├─ bone11（同上）→ bomb21, bomb22
 *        ├─ bone12（同上）→ bomb23, bomb24
 *        ├─ bone13（同上）→ bomb25, bomb26
 *        └─ bone14（同上）→ bomb27, bomb28
 * </pre>
 *
 * total 为一根对角线摆放的引线主体（Z 轴 -8 到 +9），末端带 2×2×2 收口。
 * bombs 挂 14 个中继节点，bone～bone7 沿 +Z 依次排布，bone8～bone14 整体绕 Z 轴
 * 旋转 1.5708 rad 后继续沿 +Z 堆叠，形成引线上串起的两段鞭炮串。
 *
 * 每根 bone 下挂 2 个 bomb，bomb 几何统一为：
 *   主体 4×2×2（CubeDeformation -0.5）+ 内芯 3×2×2（CubeDeformation -0.82）
 * 左 bomb 绕 Y 轴 -0.5236，右 bomb 绕 Y 轴 -2.618，构成左右对称的"V"字对。
 * 各 bone 的 bomb 之间在 X/Z 上有递增偏移（bomb5 起 Z 基准带 ±0.7321、±2.7321 等
 * 三角函数偏移，bomb13 起 X 也随序号左移），整体呈向外张开的扇形。
 *
 * 全部 28 个 bomb 使用相同纹理 UV 与 CubeDeformation，为同一模型片段的重复实例。
 * setupAnim 为空实现，无旋转/摆动，姿态完全由 createBodyLayer 的初始 pose 决定。
 */
public class FireCrackersModel extends EntityModel<PvZEntityRenderState> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(StringUtil.id("fire_crackers"), "main");

	private final ModelPart total;

	public FireCrackersModel(ModelPart root) {
		super(root);
		this.total = root.getChild("total");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();


		PartDefinition total = root.addOrReplaceChild("total",
				CubeListBuilder.create()
						.texOffs(0, 46).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 17.0F, new CubeDeformation(-0.8F))
						.texOffs(0, 46).addBox(-1.0F, -1.0F, 7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)),
				PartPose.offsetAndRotation(0.0F, 21.25F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bombs = total.addOrReplaceChild("bombs",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 1.0F, 0.0F));


		PartDefinition bone = bombs.addOrReplaceChild("bone",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 8.0F));


		bone.addOrReplaceChild("bomb1",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-4.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, -1.0F, 0.0F, -0.5236F, 0.0F));


		bone.addOrReplaceChild("bomb2",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-4.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 0.0F, -1.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone2 = bombs.addOrReplaceChild("bone2",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 6.0F));

		bone2.addOrReplaceChild("bomb3",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-4.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, -1.0F, 0.0F, -0.5236F, 0.0F));

		bone2.addOrReplaceChild("bomb4",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-4.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 0.0F, -1.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone3 = bombs.addOrReplaceChild("bone3",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 6.0F));


		bone3.addOrReplaceChild("bomb5",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-5.0F, -2.0F, -2.7321F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-3.0F, -2.0F, -2.7321F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, -1.0F, 0.0F, -0.5236F, 0.0F));

		bone3.addOrReplaceChild("bomb6",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-5.0F, -2.0F, 0.7321F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-3.0F, -2.0F, 0.7321F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 0.0F, -1.0F, 0.0F, -2.618F, 0.0F));

		PartDefinition bone4 = bombs.addOrReplaceChild("bone4",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 4.0F));

		bone4.addOrReplaceChild("bomb7",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-5.0F, -2.0F, -2.7321F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-3.0F, -2.0F, -2.7321F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, -1.0F, 0.0F, -0.5236F, 0.0F));

		bone4.addOrReplaceChild("bomb8",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-5.0F, -2.0F, 0.7321F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-3.0F, -2.0F, 0.7321F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 0.0F, -1.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone5 = bombs.addOrReplaceChild("bone5",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 4.0F));

		bone5.addOrReplaceChild("bomb9",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-6.0F, -2.0F, -4.4641F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-4.0F, -2.0F, -4.4641F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, -1.0F, 0.0F, -0.5236F, 0.0F));

		bone5.addOrReplaceChild("bomb10",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-6.0F, -2.0F, 2.4641F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-4.0F, -2.0F, 2.4641F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 0.0F, -1.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone6 = bombs.addOrReplaceChild("bone6",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 4.0F));

		bone6.addOrReplaceChild("bomb11",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-7.0F, -2.0F, -6.1962F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-5.0F, -2.0F, -6.1962F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, -1.0F, 0.0F, -0.5236F, 0.0F));

		bone6.addOrReplaceChild("bomb12",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-7.0F, -2.0F, 4.1962F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-5.0F, -2.0F, 4.1962F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 0.0F, -1.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone7 = bombs.addOrReplaceChild("bone7",
				CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 4.0F));

		bone7.addOrReplaceChild("bomb13",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-8.0F, -2.0F, -7.9282F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-6.0F, -2.0F, -7.9282F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, -1.0F, 0.0F, -0.5236F, 0.0F));

		bone7.addOrReplaceChild("bomb14",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-8.0F, -2.0F, 5.9282F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-6.0F, -2.0F, 5.9282F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 0.0F, -1.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone8 = bombs.addOrReplaceChild("bone8",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, 0.0F, 0.0F, 1.5708F));


		bone8.addOrReplaceChild("bomb15",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-4.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 1.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		bone8.addOrReplaceChild("bomb16",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-4.0F, -2.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 1.0F, 0.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone9 = bombs.addOrReplaceChild("bone9",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, 0.0F, 0.0F, 1.5708F));

		bone9.addOrReplaceChild("bomb17",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-5.0F, -2.0F, -2.7321F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-3.0F, -2.0F, -2.7321F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 1.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		bone9.addOrReplaceChild("bomb18",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-5.0F, -2.0F, 0.7321F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-3.0F, -2.0F, 0.7321F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 1.0F, 0.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone10 = bombs.addOrReplaceChild("bone10",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, 0.0F, 0.0F, 1.5708F));

		bone10.addOrReplaceChild("bomb19",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-6.0F, -2.0F, -4.4641F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-4.0F, -2.0F, -4.4641F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 1.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		bone10.addOrReplaceChild("bomb20",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-6.0F, -2.0F, 2.4641F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-4.0F, -2.0F, 2.4641F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 1.0F, 0.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone11 = bombs.addOrReplaceChild("bone11",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, 0.0F, 0.0F, 1.5708F));

		bone11.addOrReplaceChild("bomb21",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-7.0F, -2.0F, -6.1962F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-5.0F, -2.0F, -6.1962F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 1.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		bone11.addOrReplaceChild("bomb22",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-7.0F, -2.0F, 4.1962F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-5.0F, -2.0F, 4.1962F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 1.0F, 0.0F, 0.0F, -2.618F, 0.0F));

		PartDefinition bone12 = bombs.addOrReplaceChild("bone12",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, 0.0F, 0.0F, 1.5708F));

		bone12.addOrReplaceChild("bomb23",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-8.0F, -2.0F, -7.9282F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-6.0F, -2.0F, -7.9282F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 1.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		bone12.addOrReplaceChild("bomb24",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-8.0F, -2.0F, 5.9282F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-6.0F, -2.0F, 5.9282F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 1.0F, 0.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone13 = bombs.addOrReplaceChild("bone13",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, 0.0F, 0.0F, 1.5708F));

		bone13.addOrReplaceChild("bomb25",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-9.0F, -2.0F, -9.6603F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-7.0F, -2.0F, -9.6603F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 1.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		bone13.addOrReplaceChild("bomb26",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-9.0F, -2.0F, 7.6603F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-7.0F, -2.0F, 7.6603F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 1.0F, 0.0F, 0.0F, -2.618F, 0.0F));


		PartDefinition bone14 = bombs.addOrReplaceChild("bone14",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, 0.0F, 0.0F, 1.5708F));

		bone14.addOrReplaceChild("bomb27",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-10.0F, -2.0F, -11.3923F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-8.0F, -2.0F, -11.3923F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(-0.25F, 1.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		bone14.addOrReplaceChild("bomb28",
				CubeListBuilder.create()
						.texOffs(0, 58).addBox(-10.0F, -2.0F, 9.3923F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 54).addBox(-8.0F, -2.0F, 9.3923F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.82F)),
				PartPose.offsetAndRotation(0.25F, 1.0F, 0.0F, 0.0F, -2.618F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(PvZEntityRenderState state) {
		super.setupAnim(state);
	}


}