package com.lying.pvz.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/**
 * 黄色火焰粒子。
 *
 * <p>继承 {@link PvZNormalParticle}，外观为较大尺寸面片、固定 10 tick 寿命、无物理碰撞、零重力，
 * 初始速度在父类写入后被缩小为原来的 1/10，使火焰缓慢漂移。</p>
 *
 * <p>该类位于 {@code client.particle} 包下，仅在物理客户端加载，因此不需要
 * {@code @OnlyIn}——该注解按 javadoc 说明仅供 Forge/FML 内部使用，modder 应避免使用。</p>
 *
 * @see PvZNormalParticle
 * @see ParticleProvider.Sprite
 */
public class YellowFlameParticle extends PvZNormalParticle {

	public YellowFlameParticle(ClientLevel level, double x, double y, double z,
							   double xSpeed, double ySpeed, double zSpeed,
							   SpriteSet spriteSet) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(0, 1));
		this.quadSize = 0.6F;
		this.lifetime = 10;
		this.hasPhysics = false;
		this.gravity = 0.0F;
		this.xd /= 10;
		this.yd /= 10;
		this.zd /= 10;
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
			return new YellowFlameParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}
}