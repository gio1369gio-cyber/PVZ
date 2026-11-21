package com.lying.pvz.client.particle.base;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;

/**
 * 通用「横扫」类粒子的抽象基类。
 *
 * <p>该类以 {@link SingleQuadParticle} 为父类，提供一个只播动画帧、不做物理运动的单面片粒子模板：
 * 生命周期固定为 4 tick，外观为灰度随机的方形面片，尺寸随传入的 {@code scale} 线性收缩，
 * 并且始终以全亮度渲染。</p>
 *
 * <p>子类必须实现 {@link #getLayer()} 以决定渲染管线（不透明 / 半透明，以及纹理图集），
 * 还可以覆写 {@link #tick()} 等钩子扩展行为。</p>
 *

 * @see SingleQuadParticle
 * @see SpriteSet
 */
public abstract class SweepParticle extends SingleQuadParticle {


	protected final SpriteSet spriteSet;

	protected SweepParticle(ClientLevel level, double x, double y, double z, double scale, SpriteSet spriteSet) {
		super(level, x, y, z, 0.0D, 0.0D, 0.0D, spriteSet.get(0, 1));
		this.spriteSet = spriteSet;
		this.lifetime = 4;
		float f = this.random.nextFloat() * 0.6F + 0.4F;
		this.setColor(f, f, f);
		this.quadSize = 1.0F - (float) scale * 0.5F;
		this.setSpriteFromAge(spriteSet);
	}


	@Override
	protected int getLightCoords(float partialTick) {
		return 15728880;
	}


	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.setSpriteFromAge(this.spriteSet);
		}
	}
}