package com.lying.pvz.api.entity.ability;

import com.lying.pvz.api.content.paz.IPlantType;
import com.lying.pvz.api.entity.plant.IPlantEntity;

/**
 * 可被升级的植物。内层升级 = 旧植物被新植物替换，旧植物的技能、附魔、外层传递到新植物。
 *
 * <p>与"外层叠加"区分：外层走 {@link ICanPlaceOuter}，不换实体；
 * 内层升级走本接口，产生新实体并 {@code discard} 旧实体。
 *
 * <p>默认 {@link #canBeUpgraded()} 检查是否为植物且目标类型有
 * {@link IPlantType#getUpgradeTo()}；特殊植物可覆写加额外条件。
 */
public interface ICanBeUpgraded {

    /**
     * 当前是否可被升级。
     */
    default boolean canBeUpgraded() {
        if (!(this instanceof IPlantEntity plant)) {
            return false;
        }
        return plant.getPAZType() instanceof IPlantType plantType
                && !plantType.getUpgradeTo().isEmpty();
    }

    /**
     * 执行升级：把 {@code newPlant} 作为新实体，本实体的数据合并到新实体后
     * {@code discard} 自身。
     *
     * <p>子类覆写前应先调 {@code super.onUpgrade(newPlant)}。
     */
    void onUpgrade(IPlantEntity newPlant);
}