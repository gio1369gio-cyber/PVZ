package com.lying.pvz.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/**
 * 泥土迸射粒子。
 *
 * <p>继承 {@link PvZNormalParticle}，仅调整外观与物理参数：小面片、
 * 较长随机寿命、受物理碰撞、较小重力。该粒子不做动画，纹理在创建时固定。</p>
 *
 * <p>该类位于 {@code client.particle} 包下，仅在物理客户端加载，因此不需要
 * {@code @OnlyIn}——该注解按 javadoc 说明仅供 Forge/FML 内部使用，modder 应避免使用。</p>
 *
 * @see PvZNormalParticle
 * @see ParticleProvider.Sprite
 */
public class DirtBurstOutParticle extends PvZNormalParticle {

	public DirtBurstOutParticle(ClientLevel level, double x, double y, double z,
								double xSpeed, double ySpeed, double zSpeed,
								SpriteSet spriteSet) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(0, 1));
		this.quadSize = 0.05F;
		this.lifetime = 30 + this.random.nextInt(30);
		this.hasPhysics = true;
		this.gravity = 0.02F;
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
			return new DirtBurstOutParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}
}