package com.lying.pvz.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

/**
 * 蓝色火焰粒子。
 *
 * <p>行为与外观完全继承自 {@link YellowFlameParticle}，仅在注册时作为独立的粒子类型使用。
 * 若后续需要与黄色火焰区分外观，可在本类构造末尾覆写 {@code setColor} 或 {@code quadSize}。</p>
 *
 * <p>该类位于 {@code client.particle} 包下，仅在物理客户端加载，因此不需要
 * {@code @OnlyIn}——该注解按 javadoc 说明仅供 Forge/FML 内部使用，modder 应避免使用。</p>
 *
 * @see YellowFlameParticle
 * @see PvZNormalParticle
 */
public class BlueFlameParticle extends YellowFlameParticle {

    public BlueFlameParticle(ClientLevel level, double x, double y, double z,
                             double xSpeed, double ySpeed, double zSpeed,
                             SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed,
                                       RandomSource random) {
            return new BlueFlameParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}