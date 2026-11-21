package com.lying.pvz.client.render.state.entity.misc;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PvZEntityRenderState extends EntityRenderState {
    /** 缩放 */
    public float scale = 1.0F;
    /** 额外平移 */
    public @Nullable Vec3 translate;
    /**的纹理；未注册时为 null */
    public @Nullable Identifier texture;
    /** 第二层 energySwirl 纹理；null 表示不叠加 */
    public @Nullable Identifier layerTexture;

    public int icon;
    public @Nullable Vec3 colorBase;
    public @Nullable Vec3 colorChange;
    public int alpha;
    public float yaw;
    /** 插值后的俯仰角（度），供需要外层 Z 旋转的子类使用 */
    public float pitch;
    /** 绕 X 轴的自旋角度（度），供需要 tickCount 驱动的子类使用 */
    public float spin;

    public int attackTime;
    public int animTime;
    public boolean isRun;
    public int tick;
    public boolean onGround;
    public Vec3 deltaMovement;
}
