package com.lying.pvz.api.content.skill;

import com.lying.pvz.api.content.IPVZType;

/**
 * 召唤卡冷却。
 *
 * <p>不同技能等级返回不同 tick 数；等级由卡片上的冷却类技能决定。
 * 显示文本由 {@link IPVZType#getText()} 提供。
 */
public interface ICoolDown extends IPVZType {

    /**
     * 指定技能等级下的冷却 tick 数。
     *
     * @param level 技能等级，从 0 开始，范围 {@code [0, maxLevel]}
     */
    int getCD(int level);
}