package com.lying.pvz.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * PVZ 普通粒子的抽象基类。
 *
 * <p>提供一套「每 tick 移动 + 受重力 + 落地后水平速度归零」的基础物理行为，
 * 不绑定具体纹理管线，由 {@link #getLayer()} 依据当前 sprite 的图集与透明度自动选择。</p>
 *
 * <p>该类位于 {@code client.particle} 包下，仅在物理客户端加载，因此不需要
 * {@code @OnlyIn}——该注解按 javadoc 说明仅供 Forge/FML 内部使用，modder 应避免使用。</p>
 *
 * @see SingleQuadParticle
 */
public abstract class PvZNormalParticle extends SingleQuadParticle {


    public PvZNormalParticle(ClientLevel level, double x, double y, double z,
                             double xSpeed, double ySpeed, double zSpeed,
                             TextureAtlasSprite sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
        this.setParticleSpeed(xSpeed, ySpeed, zSpeed);
    }


    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        this.move(this.xd, this.yd, this.zd);

        this.yd -= this.gravity;

        if (this.onGround) {
            this.xd *= 0.0D;
            this.zd *= 0.0D;
        }
    }


    @Override
    protected Layer getLayer() {
        return Layer.bySprite(this.sprite);
    }
}