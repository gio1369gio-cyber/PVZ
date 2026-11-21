package com.lying.pvz.client.render.renderer.entity.misc.drop;

import com.lying.pvz.utils.StringUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

/**
 * 红太阳渲染器。复用 SunRender 的全部几何/颜色/alpha/光照逻辑，
 * 仅覆盖三处差异：RenderType（带裁剪的半透明）、billboard 额外 180 度翻转、面法线朝上。
 *
 * 前提：RedSunEntity 是 SunEntity 的子类（否则泛型不兼容，见基类注释）。
 */
public class RedSunRender extends SunRender {

    private static final Identifier RED_SUN_LOCATION = StringUtil.id("textures/entity/drop/red_sun.png");

    public RedSunRender(EntityRendererProvider.Context context) {
        super(context, RenderTypes.entityTranslucentCull(RED_SUN_LOCATION));
    }

    @Override
    protected void applyBillboard(PoseStack poseStack, CameraRenderState camera) {
        poseStack.rotate(camera.orientation);
        poseStack.rotateDegrees(Axis.YP, 180.0F);
    }

    @Override
    protected float[] faceNormal() {
        return new float[]{0.0F, 1.0F, 0.0F};
    }
}