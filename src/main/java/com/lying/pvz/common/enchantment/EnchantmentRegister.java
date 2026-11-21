package com.lying.pvz.common.enchantment;

import com.lying.pvz.utils.StringUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentRegister {

    public static final ResourceKey<Enchantment> ENERGY_TRANSFER = key("energy_transfer");
    public static final ResourceKey<Enchantment> BREAK_OUT = key("break_out");
    public static final ResourceKey<Enchantment> CHARM = key("charm");
    public static final ResourceKey<Enchantment> SUN_SHOVEL = key("sun_shovel");
    public static final ResourceKey<Enchantment> SOILLESS_PLANT = key("soilless_plant");
    public static final ResourceKey<Enchantment> SUN_MENDING = key("sun_mending");
    public static final ResourceKey<Enchantment> DENSELY_PLANT = key("densely_plant");
    public static final ResourceKey<Enchantment> IMMEDIATE_CD = key("immediate_cd");
    public static final ResourceKey<Enchantment> RANGE_REACH = key("range_reach");
    public static final ResourceKey<Enchantment> CARD_HEAL = key("bandage");


    public static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, StringUtil.id(name));
    }

}