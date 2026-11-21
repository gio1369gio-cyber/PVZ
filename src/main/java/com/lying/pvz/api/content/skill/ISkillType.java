package com.lying.pvz.api.content.skill;

import com.lying.pvz.api.content.IPVZType;
import net.minecraft.network.chat.MutableComponent;

/**
 * 技能类型。
 *
 * <p>技能是<b>数据驱动、带等级</b>的能力描述。它本身不携带任何玩家 / 卡片 / 实体的具体等级；
 * 具体等级由 {@link SkillLevels} 保存。
 *
 * <p>传导模型由 {@link #getScope()} 决定：
 * <ul>
 *   <li>{@link SkillScope#CARD}：只作用于卡片，不传导。</li>
 *   <li>{@link SkillScope#PLANT}：召唤时传导给实体。</li>
 *   <li>{@link SkillScope#PROJECTILE}：召唤时传导给实体，发射弹药时再传给弹药。</li>
 * </ul>
 *
 * <p><b>等级索引约定</b>：{@link #getValueAt(int)} 与 {@link #getCostAt(int)} 均为 0-indexed。
 * {@code getValueAt(0)} 是未学习时的值，{@code getValueAt(maxLevel)} 是满级时的值；
 * {@code getCostAt(0)} 是从 0 升到 1 的花费，{@code getCostAt(maxLevel - 1)} 是最后一级的花费。
 *
 * <p><b>越界行为</b>：{@code level} 超出 {@code [0, maxLevel]} 时，实现<b>必须</b>抛
 * {@link IllegalArgumentException}，不做静默 clamp。调用方若持有不可信的等级数据
 * （如存档被篡改），应先自行 {@code Math.clamp(level, 0, getMaxLevel())}。
 */
public interface ISkillType extends IPVZType {

    /**
     * 技能作用面。
     */
    SkillScope getScope();

    /**
     * 指定等级下的数值。
     *
     * @param level 等级（0 = 未学习，maxLevel = 满级），范围 {@code [0, maxLevel]}
     * @throws IllegalArgumentException 当 {@code level < 0} 或 {@code level > maxLevel}
     */
    float getValueAt(int level);

    /**
     * 从 {@code level} 升到 {@code level + 1} 所需的技能点数。
     *
     * @param level 当前等级，范围 {@code [0, maxLevel - 1]}
     * @throws IllegalArgumentException 当 {@code level < 0} 或 {@code level >= maxLevel}
     */
    int getCostAt(int level);

    /**
     * 最大等级。
     */
    int getMaxLevel();

    /**
     * 冲突组标识。同一张卡上相同 {@code getConflictGroup()} 的技能不能共存。
     * 返回空字符串表示不参与冲突判定。
     */
    String getConflictGroup();

    /**
     * 卡片携带此技能时额外增加的阳光消耗。
     *
     * <p>默认 0；返回负值表示降低消耗。
     */
    default int getExtraSun() {
        return 0;
    }

    /**
     * 描述文本（已含翻译与样式）。
     */
    MutableComponent getDescription();
}