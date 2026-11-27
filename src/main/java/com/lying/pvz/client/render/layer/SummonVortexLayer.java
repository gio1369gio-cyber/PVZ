package com.lying.pvz.client.render.layer;

import com.lying.pvz.client.model.entity.zombie.boss.QuadrupedMechModel;
import com.lying.pvz.client.render.state.entity.zombie.PvZZombieRenderState;
import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

/**
 * 召唤漩涡层：SUMMON 相位 2（jaw 冻结在 0.75s）时，在 head 与 jaw 之间的嘴洞里
 * 单独渲染一张漩涡面片。
 *
 * <p>不能像眼睛层那样复用 {@code submitModel(model)}：提交节点会在渲染阶段重新执行
 * {@code model.setupAnim(state)}，把 {@code vortex.visible} 复位为 false、并让 head 立方体
 * 用漩涡材质画出来（这正是「漩涡位置不对」的根因）。这里改为：
 * <ol>
 *   <li>用一个独立的漩涡面片部件——不受主模型 setupAnim 的显隐复位影响；</li>
 *   <li>提交阶段把主模型的 total → head 父链变换手动叠加到 PoseStack，
 *       因此漩涡会跟随头部在 0.75s 的抬头旋转与位移；</li>
 *   <li>只提交这一张面片，head / jaw 立方体不会被漩涡材质覆盖。</li>
 * </ol>
 */
public class SummonVortexLayer<S extends PvZZombieRenderState> extends RenderLayer<S, QuadrupedMechModel> {

    private static final RenderType VORTEX =
            RenderTypes.entityTranslucent(StringUtil.id("textures/entity/layer/summon_vortex.png"));

    /** 独立的漩涡面片，几何/UV/局部偏移与 {@link QuadrupedMechModel} 里的 vortex 一致。 */
    private final ModelPart vortex = bakeVortex();

    public SummonVortexLayer(RenderLayerParent<S, QuadrupedMechModel> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector,
                       int lightCoords, S state, float yRot, float xRot) {
        if (state.summonPhase != 2) return;

        final QuadrupedMechModel model = this.getParentModel();
        // 提交阶段先按当前状态摆好骨骼（幂等；渲染阶段主模型会再算一次，结果一致）。
        model.setupAnim(state);

        poseStack.pushPose();
        // 复刻主模型 root → total → head 父链，让漩涡跟随头部动画（含 0.75s 的抬头旋转）
        model.applyVortexParentTransform(poseStack);

        // 面片自身的缩放与自转：独立部件不会被主模型 setupAnim 复位。
        // 几何是 16x16（UV 覆盖整张螺旋贴图），靠 scale 缩到嘴洞大小，UV 不变→螺旋完整显示。
        this.vortex.xScale = 0.5F;
        this.vortex.yScale = 0.5F;
        this.vortex.zRot = state.summonAnimTime * 0.002F;
        this.vortex.visible = true;

        collector.order(0).submitModelPart(this.vortex, poseStack, VORTEX,
                lightCoords, OverlayTexture.NO_OVERLAY, null);
        poseStack.popPose();
    }

    /**
     * 烘焙一张独立的漩涡面片。几何 16x16（UV 覆盖整张螺旋贴图），靠 scale=0.5 缩到 8x8。
     *
     * <p>位置推导（head 局部坐标，Y 向下为正）：head 立方体底边 y≈5.21；jaw 枢轴建模 y=6.7083，
     * SUMMON 0.75s 时 posVec(0,-7,0) 把它下移到 y≈13.71，jaw 顶边随之落到 y≈12.21。
     * 因此嘴洞是 y∈[5.21, 12.21] 的空隙，中心 y≈8.7 —— 面片就放这里，正好填满张开的嘴，
     * 而不是像旧代码 y=10 那样偏下、且 12x12 大得盖住整个 head。
     */
    private static ModelPart bakeVortex() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("vortex",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 8.7F, -6.5F));
        // UV 图集尺寸必须与实际贴图 summon_vortex.png 的 16x16 一致：
        // 若写成 128x128，16x16 的面片 UV 只覆盖 [0,0.125]，采样到贴图左上角透明渐变区→几乎看不见。
        return LayerDefinition.create(mesh, 16, 16).bakeRoot().getChild("vortex");
    }
}
