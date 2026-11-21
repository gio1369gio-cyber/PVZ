package com.lying.pvz.api.entity.ability;

import com.lying.pvz.api.content.paz.IPlantType;

/**
 * 可承载外层植物的植物（南瓜壳、梯子、睡莲套在别的植物外）。
 *
 * <p>默认所有内层植物都能放外层；外层植物（{@code PlantTrait#OUTER}）
 * 覆写 {@link #canPlaceOuter()} 返回 {@code false}，避免外层套外层。
 */
public interface ICanPlaceOuter {

    /**
     * 当前是否可以放置外层植物（外层槽为空）。
     */
    boolean canPlaceOuter();

    /**
     * 放置外层植物。
     *
     * @param type    外层植物类型，必须带 {@code PlantTrait#OUTER}
     * @param sunCost 消耗的阳光（用于外层记录；部分外层植物会按比例返还）
     */
    void placeOuter(IPlantType type, int sunCost);

    /**
     * 移除外层植物。外层防御破碎、内层死亡、手动铲除时调用。
     */
    void removeOuter();
}