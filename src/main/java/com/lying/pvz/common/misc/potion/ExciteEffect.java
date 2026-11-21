package com.lying.pvz.common.misc.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/**
 * 兴奋效果（Excitement）。
 *
 * <p>携带的 {@code amplifier} 表示"兴奋等级"（amplifier 0 = 1 级）。每级让卡片 CD
 * 缩短 10%，10 级归零。等级信息由药水实例的 amplifier 直接携带，因此
 * "施加 CD 缩减状态"无需额外的属性修改器——{@link #getCooldownTicks} 在卡片使用时
 * 读取效果等级即可换算。
 *
 * <p>本类的 {@link #applyEffectTick} 保留覆写以承载"按 amplifier 施加状态"的语义钩子；
 * 当前 CD 缩减是即时计算型（使用时读取），故该钩子不做持续写入，仅返回 {@code true}
 * 保持效果正常结算。
 */
public class ExciteEffect extends PvZEffect {

    /** 达到该等级时 CD 归零。 */
    public static final int FULL_REDUCTION_LEVEL = 10;
    /** 每级缩减比例（10%）。 */
    private static final float REDUCTION_PER_LEVEL = 0.10F;

    public ExciteEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    /**
     * 按兴奋等级换算最终 CD（tick）。
     *
     * @param baseCd    未缩减的基础 CD
     * @param amplifier 效果 amplifier（0 = 1 级）
     * @return 缩减后的 CD，等级 ≥ 10 时为 0
     */
    public static int getCooldownTicks(int baseCd, int amplifier) {
        if (baseCd <= 0) return 0;
        final int level = amplifier + 1; // amplifier 0 对应 1 级
        if (level >= FULL_REDUCTION_LEVEL) return 0;
        final float factor = Math.max(0F, 1F - REDUCTION_PER_LEVEL * level);
        return Math.max(0, Math.round(baseCd * factor));
    }

    /**
     * 读取实体身上的兴奋等级；无效果时返回 -1（表示不缩减）。
     */
    public static int getAmplifier(LivingEntity entity) {
        var effect = entity.getEffect(com.lying.pvz.common.init.PvZEffectRegister.EXCITE_EFFECT);
        return effect == null ? -1 : effect.getAmplifier();
    }

    @Override
    public boolean applyEffectTick(net.minecraft.server.level.ServerLevel level,
                                   LivingEntity entity, int amplification) {
        // CD 缩减为即时计算型（卡片使用时读取等级），无需逐 tick 写入状态。
        return true;
    }
}
