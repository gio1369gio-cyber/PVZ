package com.lying.pvz.client.particle;

import com.lying.pvz.client.particle.base.SweepParticle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/**
 * 绿色横扫粒子。
 *
 * <p>继承 {@link SweepParticle}，在父类基础上染色为绿色，并在每 tick 额外把自身位置
 * 上移 {@code 0.6} 格，形成向上飘升的视觉效果。</p>
 *
 * <p>该类位于 {@code client.particle} 包下，仅在物理客户端加载，因此不需要
 * {@code @OnlyIn}——该注解按 javadoc 说明仅供 Forge/FML 内部使用，modder 应避免使用。</p>
 *
 * @see SweepParticle
 * @see ParticleProvider.Sprite
 */
public class GreenSweepParticle extends SweepParticle {


	protected GreenSweepParticle(ClientLevel level, double x, double y, double z, double scale, SpriteSet spriteSet) {
		super(level, x, y, z, scale, spriteSet);
		this.setColor(0.0F, 0.8F, 0.0F);
	}


	@Override
	public void tick() {
		super.tick();
		this.y += 0.6;
	}


	@Override
	protected Layer getLayer() {
		return Layer.bySprite(this.sprite);
	}


	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public Provider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		public SingleQuadParticle createParticle(SimpleParticleType type, ClientLevel level,
												 double x, double y, double z,
												 double xSpeed, double ySpeed, double zSpeed,
												 RandomSource random) {
			return new GreenSweepParticle(level, x, y, z, xSpeed, this.spriteSet);
		}
	}
}