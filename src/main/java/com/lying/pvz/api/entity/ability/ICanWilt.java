package com.lying.pvz.api.entity.ability;

/**
 * 会枯萎的植物。枯萎 = 地形不匹配（水植物上岸、陆植物落水、
 * 放置方块被破坏）时持续掉血。
 *
 * <p>默认所有植物都会枯萎。不需要枯萎的植物通过
 * {@code PlantTrait#IMMUNE_TO_WILT} 关闭，或覆写 {@link #shouldWilt()} 返回 {@code false}。
 */
public interface ICanWilt {

    /**
     * 当前是否应当枯萎。由植物 tick 周期调用。
     */
    boolean shouldWilt();
}