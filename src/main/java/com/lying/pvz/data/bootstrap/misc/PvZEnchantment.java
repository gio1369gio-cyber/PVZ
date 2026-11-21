package com.lying.pvz.data.bootstrap.misc;


import com.lying.pvz.common.enchantment.EnchantmentRegister;
import com.lying.pvz.common.enchantment.PvZEnchantments;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public final class PvZEnchantment {

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item>        items       = context.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);

        register(context, EnchantmentRegister.ENERGY_TRANSFER, PvZEnchantments.energyTransfer(items));
        register(context, EnchantmentRegister.RANGE_REACH,     PvZEnchantments.rangeReach(items));
        register(context, EnchantmentRegister.SUN_MENDING,     PvZEnchantments.sunMending(items, enchantments));
        register(context, EnchantmentRegister.SUN_SHOVEL,      PvZEnchantments.sunShovel(items));
        register(context, EnchantmentRegister.CARD_HEAL,       PvZEnchantments.bandage(items));
        register(context, EnchantmentRegister.CHARM,           PvZEnchantments.charm(items));
        register(context, EnchantmentRegister.IMMEDIATE_CD,    PvZEnchantments.immediateCd(items));
        register(context, EnchantmentRegister.BREAK_OUT,       PvZEnchantments.breakOut(items));
        register(context, EnchantmentRegister.DENSELY_PLANT,   PvZEnchantments.denselyPlant(items));
        register(context, EnchantmentRegister.SOILLESS_PLANT,  PvZEnchantments.soillessPlant(items));
    }

    private static void register(BootstrapContext<Enchantment> context,
                                 ResourceKey<Enchantment> key, Enchantment value) {
        context.register(key, value);
    }

    private PvZEnchantment() {}
}