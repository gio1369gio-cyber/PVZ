package com.lying.pvz.api.entity.ability;

import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

/**
 * 可被魅惑（阵营反转）。
 *
 * <p>被魅惑后，植物归入 {@link GroupType#ZOMBIES}，僵尸归入 {@link GroupType#PLANTS}。
 */
public interface ICanBeCharmed {

    boolean isCharmed();

    /**
     * 施加魅惑。
     *
     * @param entity 施加魅惑的来源；{@code null} 表示手动反转
     */
    void onCharmedBy(@Nullable LivingEntity entity);
}