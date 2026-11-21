package com.lying.pvz.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class GoldNuggetParticle extends PvZNormalParticle {
    public GoldNuggetParticle(ClientLevel level, double x, double y, double z,
                              double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.get(0,1));
        this.quadSize = 0.2F;
        this.lifetime = this.random.nextInt(20) + 10;
        this.hasPhysics = true;
        this.gravity = 0.1F;
        this.xd = (double) this.random.nextFloat() - 0.5;
        this.yd = (double) (-this.random.nextFloat());
        this.zd = (double) this.random.nextFloat() - 0.5;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z,
                                                 double xAux, double yAux, double zAux, RandomSource random) {
            GoldNuggetParticle particle = new GoldNuggetParticle(level, x, y, z, xAux, yAux, zAux,this.sprite);
            return particle;
        }
    }
}