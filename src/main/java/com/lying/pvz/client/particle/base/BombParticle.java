package com.lying.pvz.client.particle.base;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;


public class BombParticle extends SingleQuadParticle {

	protected final SpriteSet spriteSet;


	protected BombParticle(ClientLevel level, double x, double y, double z,
						   double xSpeed, double ySpeed, double zSpeed,
						   RandomSource random, SpriteSet spriteSet) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(0, 1));

		this.lifetime = 6 + random.nextInt(4);
		this.setColor(1.0F, 0.0F, 0.0F);
		this.quadSize = 4.0F;
		this.spriteSet = spriteSet;
		this.hasPhysics = false;

		this.setSpriteFromAge(this.spriteSet);
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


	@Override
	protected int getLightCoords(float partialTick) {
		return 15728880;
	}


	@Override
	protected Layer getLayer() {
		return Layer.bySprite(this.sprite);
	}


	public static class Provider implements ParticleProvider.Sprite<SimpleParticleType> {
		private final SpriteSet spriteSet;


		public Provider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		public SingleQuadParticle createParticle(SimpleParticleType type, ClientLevel level,
												 double x, double y, double z,
												 double xSpeed, double ySpeed, double zSpeed,
												 RandomSource random) {
			return new BombParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, random, this.spriteSet);
		}
	}
}