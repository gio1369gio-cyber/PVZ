package com.lying.pvz.client.gui.screen.shop;


import com.lying.pvz.common.menu.shop.AbstractDaveShopMenu;
import com.lying.pvz.utils.PlayerUtil;
import com.lying.pvz.utils.enums.Resources;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class DaveShopScreen extends AbstractDaveShopScreen {

	public DaveShopScreen(AbstractDaveShopMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected int getCurrentMoney() {
		if (this.minecraft == null) return 0;
		return PlayerUtil.getResource(this.minecraft.player, Resources.MONEY);
	}

	@Override
	protected Pair<Integer, Integer> getMoneyBarPos() {
		return Pair.of(285, 0);
	}

	@Override
	protected Component getShopTitle() {
		return Component.translatable("gui.pvz.dave_shop.title");
	}
}