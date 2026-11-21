package com.lying.pvz.common.level.item.tool.plant;

import com.lying.pvz.common.attachment.player.PlayerDataManager;
import com.lying.pvz.common.init.PvZDataComponents;
import com.lying.pvz.common.init.PvZSoundRegister;
import com.lying.pvz.common.level.item.component.SunStorage;
import com.lying.pvz.common.level.item.consume.SunReleaseEffect;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class SunStorageSaplingItem extends Item {

	public static final long DEFAULT_STORAGE = 0L;

	/** 可复用版本。 */
	public SunStorageSaplingItem(Identifier id, int max) {
		this(id,max, false);
	}

	/** 一次性版本。 */
	public SunStorageSaplingItem(Identifier id) {
		this(id,10000, true);
	}

	private SunStorageSaplingItem(Identifier id ,int max, boolean once) {
		super(new Properties().setId(ResourceKey.create(Registries.ITEM,id))
				.stacksTo(1)
				.component(PvZDataComponents.SUN_STORAGE.get(), new SunStorage(DEFAULT_STORAGE, max, once))
				.component(DataComponents.CONSUMABLE,
						Consumable.builder().consumeSeconds(3.0F)
								.animation(ItemUseAnimation.EAT).sound(SoundEvents.GENERIC_EAT)
								.hasConsumeParticles(false).onConsume(SunReleaseEffect.INSTANCE).build())
		);
	}

	// ============================ 释放太阳 ============================

	/**
	 * 把物品内的太阳尽可能倒入玩家，剩余保留在物品里。
	 *
	 * @return 实际释放的量（0 表示什么都没释放）
	 */
	public static int releaseSunTo(Player player, ItemStack stack) {
		if (player.level().isClientSide()) {
			return 0;
		}
		SunStorage storage = getStorage(stack);
		if (storage.amount() <= 0) {
			return 0;
		}

		PlayerDataManager data = PlayerUtil.getManager(player);
		int sunNum = data.getResource(Resources.SUN_NUM);
		int maxNum = PlayerUtil.getPlayerMaxSunNum(data.getResource(Resources.TREE_LVL));
		if (sunNum >= maxNum) {
			return 0;
		}

		long amount = storage.amount();
		long newAmount;
		int newSunNum;
		if (sunNum + amount > maxNum) {
			newSunNum = maxNum;
			newAmount = amount - (maxNum - sunNum);
		} else {
			newSunNum = (int) (sunNum + amount);
			newAmount = 0;
		}

		setStorage(stack, storage.withAmount(newAmount));
		data.setResource(Resources.SUN_NUM, newSunNum);
		PlayerUtil.playClientSound(player, PvZSoundRegister.SUN_PICK.get());

		return (int) (amount - newAmount);
	}

	// ============================ 组件门面 ============================

	public static SunStorage getStorage(ItemStack stack) {
		return stack.getOrDefault(PvZDataComponents.SUN_STORAGE.get(), new SunStorage(0L, 0L, false));
	}

	public static void setStorage(ItemStack stack, SunStorage storage) {
		stack.set(PvZDataComponents.SUN_STORAGE.get(), storage);
	}

	// ============================ 使用行为 ============================

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		boolean once = getStorage(stack).once();
		ItemStack result = super.finishUsingItem(stack, level, entity);
		// 可复用物品：Consumable 已 consume(1)，恢复计数
		if (!once && result.getCount() == 0) {
			result.setCount(1);
		}
		return result;
	}

	// ============================ 耐久条 ============================

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return !getStorage(stack).once();
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		SunStorage storage = getStorage(stack);
		if (storage.max() <= 0) {
			return 0;
		}
		return (int) Math.round(13.0D * storage.amount() / storage.max());
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return 0x00FF00;
	}

	// ============================ Tooltip ============================

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
								Consumer<Component> builder, TooltipFlag flag) {
		getStorage(stack).addToTooltip(context, builder, flag, stack.getComponents());
	}
}