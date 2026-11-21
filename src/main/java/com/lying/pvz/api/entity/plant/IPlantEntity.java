package com.lying.pvz.api.entity.plant;

import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.entity.IPAZEntity;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/**
 * 植物实体契约。
 */
public interface IPlantEntity extends IPAZEntity {

    /**
     * 在自身之上放置 / 移除外层植物（如南瓜壳）。
     *
     * @param placed 是否已成功放置外层植物
     */
    void setPumpkin(boolean placed);

    /**
     * 应用卡片数据（技能 + 附魔）。
     *
     * <p>由召唤流程在实体生成后调用：
     * <ul>
     *   <li>{@code skills} 由 {@code ICardType#getSkillsForPlant} 导出（已排除 CARD 面）</li>
     *   <li>{@code enchantments} 由 {@code ICardType#getEnchantmentsForPlant} 导出（已排除卡片专属 tag）</li>
     * </ul>
     */
    void applyCardData(SkillLevels skills, ItemEnchantments enchantments);
}