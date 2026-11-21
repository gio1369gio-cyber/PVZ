package com.lying.pvz.client.render.status;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

/**
 * 状态视觉描述。
 *
 * <p>Phase 4 只落两种形态：
 * <ul>
 *   <li>{@link FullSkin}：整模型叠加层（寒冷）。</li>
 *   <li>{@link Particle}：按固定间隔在头顶/身体生成的粒子（黄油、冰冻、标记）。</li>
 * </ul>
 * 后续要加"头贴"或"冰壳"时，在这里追加一个 record 即可，不需要新增 Layer 类。
 */
public sealed interface StatusVisual {

    record FullSkin(Identifier texture, int color) implements StatusVisual {
    }

    record Particle(Supplier<ParticleOptions> particle, int count,
                    float heightRatio, int intervalTicks) implements StatusVisual {
    }
}
