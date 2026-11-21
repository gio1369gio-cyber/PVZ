package com.lying.pvz.client.particle.bomb;

import com.lying.pvz.client.particle.base.BombParticle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/**
 * 樱桃炸弹粒子。
 *
 * <p>在 {@link BombParticle} 的基础上复写外观参数：随机寿命 {@code 6 ~ 9}、
 * 纯红色调 {@code (1.0, 0.0, 0.0)}、面片尺寸 {@code 4.0}，并关闭物理模拟。</p>
 *
 * <p>该类位于 {@code client.particle} 包下，仅在物理客户端加载，因此不需要
 * {@code @OnlyIn}——该注解按 javadoc 说明仅供 Forge/FML 内部使用，modder 应避免使用。</p>
 *
 * @see BombParticle
 * @see ParticleProvider.Sprite
 */
public class CherryBombParticle extends BombParticle {


	public CherryBombParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed,
							  RandomSource random, SpriteSet spriteSet) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, random, spriteSet);
		this.lifetime = 6 + random.nextInt(4);
		this.setColor(1.0F, 0.0F, 0.0F);
		this.quadSize = 4.0F;
		this.hasPhysics = false;
		this.setSpriteFromAge(this.spriteSet);
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
			return new CherryBombParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, random, this.spriteSet);
		}
	}
}