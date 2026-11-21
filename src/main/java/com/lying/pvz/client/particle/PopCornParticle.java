package com.lying.pvz.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/**
 * 爆米花粒子。
 *
 * <p>继承 {@link PvZNormalParticle}，外观为小尺寸面片、较短随机寿命、受物理碰撞、较大重力。
 * 与其它切片粒子不同，本类把初始 Y 速度取为 {@code nextFloat() / 2}（始终向上），
 * 水平速度则围绕 0 随机，使爆米花向上弹起后散开。</p>
 *
 * <p>该类位于 {@code client.particle} 包下，仅在物理客户端加载，因此不需要
 * {@code @OnlyIn}——该注解按 javadoc 说明仅供 Forge/FML 内部使用，modder 应避免使用。</p>
 *
 * @see PvZNormalParticle
 * @see ParticleProvider.Sprite
 */
public class PopCornParticle extends PvZNormalParticle {


	public PopCornParticle(ClientLevel level, double x, double y, double z,
						   double xSpeed, double ySpeed, double zSpeed,
						   SpriteSet spriteSet) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(0, 1));
		this.quadSize = 0.25F;
		this.lifetime = this.random.nextInt(20) + 10;
		this.hasPhysics = true;
		this.gravity = 0.1F;
		this.xd = level.getRandom().nextFloat() - 0.5;
		this.yd = level.getRandom().nextFloat() / 2;
		this.zd = level.getRandom().nextFloat() - 0.5;
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
			return new PopCornParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}
}