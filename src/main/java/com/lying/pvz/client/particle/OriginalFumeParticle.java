package com.lying.pvz.client.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class OriginalFumeParticle extends PvZNormalParticle {
    public OriginalFumeParticle(ClientLevel level, double x, double y, double z,
                                double xSpeed, double ySpeed, double zSpeed,SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed,spriteSet.get(0,1));
        this.quadSize = 0.3F;
        this.lifetime = this.random.nextInt(5) + 15;
        this.hasPhysics = false;
        this.gravity = 0.0F;
        this.xd /= 10.0;
        this.yd /= 10.0;
        this.zd /= 10.0;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            OriginalFumeParticle particle = new OriginalFumeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,this.sprite);
            return particle;
        }
    }
}