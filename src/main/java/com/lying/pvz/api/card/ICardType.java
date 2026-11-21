package com.lying.pvz.api.card;

import com.lying.pvz.api.PVZAPI;
import com.lying.pvz.api.content.paz.IPAZType;
import com.lying.pvz.api.content.skill.SkillLevels;
import com.lying.pvz.api.content.skill.SkillScope;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;

/**
 * 卡片物品的行为契约。
 *
 * <p><b>这是物品接口，不是注册表类型。</b>具体卡片物品（如植物卡、模仿者卡）
 * 实现本接口。附属模组想扩展卡种，继承实现层的 {@code AbstractCardItem} 即可。
 *
 * <p>所有方法都接受 {@link ItemStack}，因为卡片行为可能依赖组件
 * （技能、附魔、模仿内容等）。
 */
public interface ICardType {

    /**
     * 卡片直接召唤的目标类型（模仿者卡为"模仿者"本身）。
     */
    IPAZType getType();

    /**
     * 是否为“体验”变体。
     *
     * <p>体验变体的具体可用性由卡片实现决定：
     * <ul>
     *   <li>植物体验卡：可正常使用（与普通卡行为一致，使用后消耗而非设置长冷却）；</li>
     *   <li>模仿者体验卡：仅作合成材料，不参与使用流程。</li>
     * </ul>
     */
    boolean isEnjoy();

    /**
     * 解析卡片内容。返回 {@link CardContent#EMPTY} 表示卡片当前无效。
     */
    CardContent resolveContent(ItemStack stack);

    /**
     * 解锁检查需要检查的类型列表。任一处于锁定状态，卡片即视为锁定。
     */
    List<IPAZType> getLockCheckTypes(ItemStack stack);

    /**
     * 卡片自身的附魔能力值。
     *
     * <p>返回 0 表示不可附魔（不设置 {@code ENCHANTABLE} 组件）。
     */
    int getCardEnchantmentValue(ItemStack stack);

    /**
     * 导出给植物实体的技能（排除 {@link SkillScope#CARD}）。
     *
     * <p>{@link SkillScope#PROJECTILE} 包含在内，由植物暂存、发射时再传给弹药。
     *
     * <p>默认实现：从 {@code stack} 的 {@code CARD_SKILLS} 组件读取，按作用面过滤。
     * 若卡片物品使用其他存储方式，可覆写本方法。
     */
    default SkillLevels getSkillsForPlant(ItemStack stack) {
        final SkillLevels levels = stack.getOrDefault(
                PvZCardComponents.CARD_SKILLS.get(),
                SkillLevels.EMPTY);
        return levels.filter(id -> PVZAPI.lookup().skill(id)
                .map(skill -> skill.getScope() != SkillScope.CARD)
                .orElse(false));
    }

    /**
     * 导出给植物实体的附魔（排除卡片专属 tag）。
     *
     * <p>具体过滤规则由主模组实现。
     */
    ItemEnchantments getEnchantmentsForPlant(ItemStack stack, HolderLookup.Provider registries);
}