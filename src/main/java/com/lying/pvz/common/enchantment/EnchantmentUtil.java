package com.lying.pvz.common.enchantment;

import com.lying.pvz.common.level.entity.plant.PlantEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class EnchantmentUtil {

	// ===== 等级读取 =====

	public static int getLevel(ItemStack stack, ResourceKey<Enchantment> key) {
		ItemEnchantments enchants = stack.getEnchantments();
		for (var entry : enchants.entrySet()) {
			if (entry.getKey().is(key)) {
				return entry.getIntValue();
			}
		}
		return 0;
	}

	// ===== SunShovelEnchantment =====

	public static int getSunShovelAmount(ItemStack stack, int amount) {
		int lvl = getLevel(stack, EnchantmentRegister.SUN_SHOVEL);
		float percent = Math.min(1F, 0.1F * lvl);
		return Mth.floor(percent * amount);
	}

	// ===== RangeReachEnchantment =====

	public static float getReachDistance(ItemStack stack, float range) {
		final int lvl = getLevel(stack, EnchantmentRegister.RANGE_REACH);
		return range + range * (lvl * 0.2F);
	}

	// ===== SunMendingEnchantment =====

	public static int getSunMendingLevel(ItemStack stack) {
		return getLevel(stack, EnchantmentRegister.SUN_MENDING);
	}

	public static void repairItem(ItemStack stack, int amount) {
		if (!stack.isEmpty() && stack.isDamaged()) {
			final int lvl = getSunMendingLevel(stack);
			final int needSunEach = Math.max(5, 30 - 5 * lvl);
			final int repairDamage = Math.min(stack.getDamageValue(),
					Mth.floor(amount * stack.getXpRepairRatio() / needSunEach));
			stack.setDamageValue(stack.getDamageValue() - repairDamage);
		}
	}

	// ===== BandageEnchantment =====

	public static float getHealPercent(ItemStack stack) {
		final int lvl = getLevel(stack, EnchantmentRegister.CARD_HEAL);
		return 0.2F * (lvl + 1);
	}

	// ===== ImmediateCDEnchantment =====

	public static boolean canImmediateCD(ItemStack stack, RandomSource rand) {
		final int lvl = getLevel(stack, EnchantmentRegister.IMMEDIATE_CD);
		final float chance = (lvl == 1 ? 0.05F : lvl == 2 ? 0.1F : 0.2F);
		return lvl > 0 && rand.nextFloat() < chance;
	}

	// ===== BreakOutEnchantment =====

	public static void checkAndBreakOut(PlantEntity plantEntity, ItemStack stack) {
		if (plantEntity.canStartSuperMode()
				&& plantEntity.getRandom().nextFloat() * 100 < getPlantBreakOutChance(stack)) {
			plantEntity.startSuperMode(false);
		}
	}

	private static float getPlantBreakOutChance(ItemStack stack) {
		final int lvl = getLevel(stack, EnchantmentRegister.BREAK_OUT);
		return lvl == 0 ? 0F : 2.5F * (lvl + 1);
	}

	// ===== DenselyPlantEnchantment =====

	public static int getExtraPlantNum(ItemStack stack) {
		return 10 * getLevel(stack, EnchantmentRegister.DENSELY_PLANT);
	}

	// ===== SoillessPlantEnchantment =====

	public static boolean isSoilless(ItemStack stack) {
		return getLevel(stack, EnchantmentRegister.SOILLESS_PLANT) > 0;
	}
}