package com.lying.pvz.client.render.renderer.entity.plant.electric;

import com.lying.pvz.client.model.entity.plant.electric.MagnifyingGrassModel;
import com.lying.pvz.client.render.renderer.entity.plant.PvZPlantRender;
import com.lying.pvz.client.render.state.entity.plant.PvZPlantRenderState;
import com.lying.pvz.common.level.entity.plant.electric.MagnifyingGrassEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

/**
 * 放大镜草渲染器。
 *
 * <p>除植物模型外，在瞄准目标时额外渲染一条从眼睛指向目标的闪电光束。
 * 旧 {@code render(MultiBufferSource)} 覆写已迁移为
 * {@link #extractRenderState}（把目标方向 / 长度打包进 state）+ {@link #submit}（submitCustomGeometry）。
 */
public class MagnifyingGrassRender extends PvZPlantRender<MagnifyingGrassEntity, MagnifyingGrassModel> {

    private static final RenderType BEAM_RENDER_TYPE = RenderTypes.lightning();

    public MagnifyingGrassRender(EntityRendererProvider.Context context) {
        super(context, new MagnifyingGrassModel(context.bakeLayer(MagnifyingGrassModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public void extractRenderState(MagnifyingGrassEntity entity, PvZPlantRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        final LivingEntity target = entity.getActiveAttackTarget();
        if (target == null) {
            state.magnifyingBeam = false;
            return;
        }

        final double px = Mth.lerp(partialTick, entity.xOld, entity.getX());
        final double py = Mth.lerp(partialTick, entity.yOld, entity.getY()) + entity.getEyeHeight();
        final double pz = Mth.lerp(partialTick, entity.zOld, entity.getZ());
        final double tx = Mth.lerp(partialTick, target.xOld, target.getX());
        final double ty = Mth.lerp(partialTick, target.yOld, target.getY()) + target.getBbHeight() * 0.5;
        final double tz = Mth.lerp(partialTick, target.zOld, target.getZ());

        final double dx = tx - px;
        final double dy = ty - py;
        final double dz = tz - pz;
        final double len = Math.sqrt(dx * dx + dy * dy + dz * dz);

        state.magnifyingBeam = true;
        state.beamLength = (float) (len + 1.0);
        state.beamPitch = (float) Math.acos(dy / len);
        state.beamYaw = (float) Math.atan2(dz, dx);
        state.beamEyeHeight = entity.getEyeHeight();
    }

    @Override
    public void submit(PvZPlantRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        if (!state.magnifyingBeam) {
            return;
        }

        final float j = state.ageInTicks;
        final float k = j * 0.5F % 1.0F;
        final float m = state.beamLength;
        final float n = state.beamPitch;
        final float o = state.beamYaw;
        final float q = j * 0.05F * -1.5F;
        final int s = 255;
        final int t = 255;
        final int u = 64;

        poseStack.pushPose();
        poseStack.translate(0.0, state.beamEyeHeight, 0.0);
        poseStack.rotateDegrees(Axis.YP, (1.5707964F - o) * 57.295776F);
        poseStack.rotateDegrees(Axis.XP, n * 57.295776F);

        final float x = Mth.cos(q + 2.3561945F) * 0.282F;
        final float y = Mth.sin(q + 2.3561945F) * 0.282F;
        final float z = Mth.cos(q + 0.7853982F) * 0.282F;
        final float aa = Mth.sin(q + 0.7853982F) * 0.282F;
        final float ab = Mth.cos(q + 3.926991F) * 0.282F;
        final float ac = Mth.sin(q + 3.926991F) * 0.282F;
        final float ad = Mth.cos(q + 5.4977875F) * 0.282F;
        final float ae = Mth.sin(q + 5.4977875F) * 0.282F;
        final float af = Mth.cos(q + 3.1415927F) * 0.2F;
        final float ag = Mth.sin(q + 3.1415927F) * 0.2F;
        final float ah = Mth.cos(q + 0.0F) * 0.2F;
        final float ai = Mth.sin(q + 0.0F) * 0.2F;
        final float aj = Mth.cos(q + 1.5707964F) * 0.2F;
        final float ak = Mth.sin(q + 1.5707964F) * 0.2F;
        final float al = Mth.cos(q + 4.712389F) * 0.2F;
        final float am = Mth.sin(q + 4.712389F) * 0.2F;
        final float aq = -1.0F + k;
        final float ar = m * 2.5F + aq;
        final float as = ((int) state.ageInTicks % 2 == 0) ? 0.5F : 0.0F;

        collector.submitCustomGeometry(poseStack, BEAM_RENDER_TYPE, (pose, consumer) -> {
            final Matrix4f matrix = pose.pose();
            vertex(consumer, matrix, af, m, ag, s, t, u, 0.4999F, ar);
            vertex(consumer, matrix, af, 0.0F, ag, s, t, u, 0.4999F, aq);
            vertex(consumer, matrix, ah, 0.0F, ai, s, t, u, 0.0F, aq);
            vertex(consumer, matrix, ah, m, ai, s, t, u, 0.0F, ar);
            vertex(consumer, matrix, aj, m, ak, s, t, u, 0.4999F, ar);
            vertex(consumer, matrix, aj, 0.0F, ak, s, t, u, 0.4999F, aq);
            vertex(consumer, matrix, al, 0.0F, am, s, t, u, 0.0F, aq);
            vertex(consumer, matrix, al, m, am, s, t, u, 0.0F, ar);
            vertex(consumer, matrix, x, m, y, s, t, u, 0.5F, as + 0.5F);
            vertex(consumer, matrix, z, m, aa, s, t, u, 1.0F, as + 0.5F);
            vertex(consumer, matrix, ad, m, ae, s, t, u, 1.0F, as);
            vertex(consumer, matrix, ab, m, ac, s, t, u, 0.5F, as);
        });

        poseStack.popPose();
    }

    /**
     * 扩展视锥剔除包围盒以覆盖激光光束（从眼睛延伸到目标）。
     *
     * <p>大招光束作为植物实体的自定义几何渲染，默认用植物 1 格大小的包围盒做
     * 视锥剔除，导致玩家背对植物（或光束远端在视锥外）时整条光束被剔除消失。
     * 这里在瞄准目标时把包围盒沿目标方向拉长到目标位置，确保光束始终可见。
     */
    @Override
    protected AABB getBoundingBoxForCulling(MagnifyingGrassEntity entity, float partialTicks) {
        final AABB base = super.getBoundingBoxForCulling(entity, partialTicks);
        final LivingEntity target = entity.getActiveAttackTarget();
        if (target != null && target.isAlive()) {
            return base.minmax(target.getBoundingBox());
        }
        return base;
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix, float f, float g, float h,
                               int i, int j, int k, float l, float m) {
        consumer.addVertex(matrix, f, g, h)
                .setColor(i, j, k, 255)
                .setUv(l, m)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880);
    }
}
