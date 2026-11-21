package com.lying.pvz.client.gui.screen.shop;

import com.lying.pvz.common.menu.shop.AbstractDaveShopMenu;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PennyShopScreen extends AbstractDaveShopScreen {

	public PennyShopScreen(AbstractDaveShopMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected int getCurrentMoney() {
		if (this.minecraft == null) return 0;
		return PlayerUtil.getResource(this.minecraft.player, Resources.GEM_NUM);
	}

	@Override
	protected Pair<Integer, Integer> getMoneyBarPos() {
		return Pair.of(285, 44);
	}

	@Override
	protected Component getShopTitle() {
		return Component.translatable("gui.pvz.penny_shop.title");
	}
}