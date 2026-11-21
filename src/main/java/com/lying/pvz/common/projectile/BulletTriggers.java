package com.lying.pvz.common.projectile;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.resources.Identifier;

/**
 * 飞行触发器信号 id。
 *
 * <p>触发器只发这些 {@link Identifier}，具体变成哪个变体由
 * {@code BulletKind.VariantTable} 决定。
 */
public final class BulletTriggers {

    public static final Identifier HEAT = StringUtil.id("heat");
    public static final Identifier BLUE_HEAT = StringUtil.id("blue_heat");

    private BulletTriggers() {
    }
}
