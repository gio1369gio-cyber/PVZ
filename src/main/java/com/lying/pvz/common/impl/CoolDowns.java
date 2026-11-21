package com.lying.pvz.common.impl;

import com.lying.pvz.PvZMod;
import com.lying.pvz.api.content.skill.ICoolDown;
import com.lying.pvz.common.impl.type.CoolDownTypeBuilder;
import com.lying.pvz.utils.MathUtil;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

/**
 * 内置冷却类型的 {@link DeferredHolder} 集合。
 *
 * <p>通过 {@link PVZRegistryBootstrap#COOL_DOWNS} 注册到 {@code pvz:cool_down} registry。
 * 主模组需在 mod 构造期调用一次 {@link #init()} 触发类加载。
 */
public final class CoolDowns {

    /** 默认冷却：固定 50 tick，不随等级变化。 */
    public static final DeferredHolder<ICoolDown, ICoolDown> DEFAULT = coolDown("default", b -> b
            .cd(level -> 50));

    public static final DeferredHolder<ICoolDown, ICoolDown> SUPER_FAST = coolDown("super_fast", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 80, 60)));

    public static final DeferredHolder<ICoolDown, ICoolDown> HUGE_FAST = coolDown("huge_fast", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 160, 100)));

    public static final DeferredHolder<ICoolDown, ICoolDown> VERY_FAST = coolDown("very_fast", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 300, 200)));

    public static final DeferredHolder<ICoolDown, ICoolDown> FAST = coolDown("fast", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 400, 300)));

    public static final DeferredHolder<ICoolDown, ICoolDown> LITTLE_FAST = coolDown("little_fast", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 500, 360)));

    public static final DeferredHolder<ICoolDown, ICoolDown> NORMAL = coolDown("normal", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 640, 480)));

    public static final DeferredHolder<ICoolDown, ICoolDown> LITTLE_SLOW = coolDown("little_slow", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 800, 600)));

    public static final DeferredHolder<ICoolDown, ICoolDown> SLOW = coolDown("slow", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 1000, 720)));

    public static final DeferredHolder<ICoolDown, ICoolDown> VERY_SLOW = coolDown("very_slow", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 1440, 960)));

    public static final DeferredHolder<ICoolDown, ICoolDown> HUGE_SLOW = coolDown("huge_slow", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 2400, 1600)));

    public static final DeferredHolder<ICoolDown, ICoolDown> SUPER_SLOW = coolDown("super_slow", b -> b
            .cd(level -> MathUtil.getIncreaseAverage(level, SkillTypes.COOL_DOWN_LEVEL, 4800, 3200)));

    private CoolDowns() {
    }

    private static DeferredHolder<ICoolDown, ICoolDown> coolDown(String name,
                                                                  Function<CoolDownTypeBuilder, CoolDownTypeBuilder> cfg) {
        return PVZRegistryBootstrap.COOL_DOWNS.register(name,
                () -> cfg.apply(CoolDownTypeBuilder.of(PvZMod.MOD_ID, name)).build());
    }

    /**
     * 触发类加载。主模组在 {@code RegistryHandler.coreRegister()} 中调用。
     */
    public static void init() {
    }
}
