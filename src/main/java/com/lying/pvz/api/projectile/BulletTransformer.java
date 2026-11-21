package com.lying.pvz.api.projectile;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

/**
 * 飞行中触发器。由 {@code BulletTransformers} 按实体类型 / 实体 tag 注册。
 *
 * <p>典型实现：火炬树桩、蓝火树桩，以及未来"经过某植物获得 buff"的机制。
 *
 * <p><b>触发语义</b>：
 * <ul>
 *   <li>只在本 tick 位移段与 provider 的 AABB 相交时触发，不做"当前重叠"判定；</li>
 *   <li>同一发子弹对同一 {@code (transformer, provider)} 只触发一次；</li>
 *   <li>触发器不直接改材质——它返回 {@link Result#variant()}，
 *       由 Kind 的 {@code VariantTable} 决定最终外观与数值。</li>
 * </ul>
 */
public interface BulletTransformer {

    Identifier id();

    boolean applies(IBullet bullet, Entity provider);

    Result transform(IBullet bullet, Entity provider);

    /**
     * 触发结果。
     *
     * @param variant           目标变体；{@link #KEEP_VARIANT} 表示保持不变
     * @param damageAdd         命中伤害加算
     * @param damageMultiplier  命中伤害乘算，默认 1
     */
    record Result(int variant, float damageAdd, float damageMultiplier) {

        public static final int KEEP_VARIANT = -1;

        public Result {
            if (damageMultiplier < 0.0F) {
                throw new IllegalArgumentException("damageMultiplier must be >= 0");
            }
        }

        public static Result none() {
            return new Result(KEEP_VARIANT, 0.0F, 1.0F);
        }

        public static Result variant(int variant) {
            return new Result(variant, 0.0F, 1.0F);
        }

        public boolean isNone() {
            return this.variant == KEEP_VARIANT
                    && this.damageAdd == 0.0F
                    && this.damageMultiplier == 1.0F;
        }
    }
}
