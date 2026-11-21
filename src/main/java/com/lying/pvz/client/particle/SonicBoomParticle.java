package com.lying.pvz.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SonicBoomParticle extends PvZNormalParticle {

    public SonicBoomParticle(ClientLevel level, double x, double y, double z,
                             double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(0,5));
        this.quadSize = 0.25F;
        this.lifetime = 25;
        this.hasPhysics = false;
        this.gravity = 0.0F;
        this.xd /= 10.0;
        this.yd /= 10.0;
        this.zd /= 10.0;
        this.setSpriteFromAge(spriteSet);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            SonicBoomParticle particle = new SonicBoomParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
            return particle;
        }
    }
}