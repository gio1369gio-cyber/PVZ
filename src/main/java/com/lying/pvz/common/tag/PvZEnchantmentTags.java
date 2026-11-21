package com.lying.pvz.common.tag;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 按作用面分组的附魔 tag，用于卡片 → 植物/子弹传导过滤。
 * <p>tag JSON 内容做附魔时填。
 */
public class PvZEnchantmentTags {
    /** 仅作用于卡片的附魔，不下传。 */
    public static final TagKey<Enchantment> CARD = pvzTag("card");

    /** 传导给植物实体的附魔。 */
    public static final TagKey<Enchantment> PLANT = pvzTag("plant");

    /** 经植物暂存、最终传给子弹实体的附魔。 */
    public static final TagKey<Enchantment> PROJECTILE = pvzTag("projectile");

    private static TagKey<Enchantment> pvzTag(String name) {
        return TagKey.create(Registries.ENCHANTMENT, StringUtil.id("enchantment/" + name));
    }
}
