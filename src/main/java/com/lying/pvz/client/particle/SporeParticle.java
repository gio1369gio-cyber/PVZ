package com.lying.pvz.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/**
 * 孢子粒子。
 *
 * <p>继承 {@link PvZNormalParticle}，外观为小尺寸面片、无物理碰撞、零重力，
 * 并带有随机紫色调。寿命依赖调用方传入的 X 速度：速度极小时寿命很短（1~2 tick），
 * 否则寿命较长（20~39 tick），用于区分「孢子快速消散」与「孢子缓慢漂浮」两种用法。</p>
 *
 * <p>该类位于 {@code client.particle} 包下，仅在物理客户端加载，因此不需要
 * {@code @OnlyIn}——该注解按 javadoc 说明仅供 Forge/FML 内部使用，modder 应避免使用。</p>
 *
 * @see PvZNormalParticle
 * @see ParticleProvider.Sprite
 */
public class SporeParticle extends PvZNormalParticle {

	public SporeParticle(ClientLevel level, double x, double y, double z,
						 double xSpeed, double ySpeed, double zSpeed,
						 SpriteSet spriteSet) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(0, 1));
		this.quadSize = 0.2F;
		if (xSpeed <= 0.01F) {
			this.lifetime = this.random.nextInt(2) + 1;
		} else {
			this.lifetime = this.random.nextInt(20) + 20;
		}
		this.hasPhysics = false;
		this.gravity = 0.0F;
		float f = this.random.nextFloat() * 0.6F + 0.4F;
		this.setColor(f * 0.9F, f * 0.3F, f);
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
			return new SporeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}
}